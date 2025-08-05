package br.com.escola.negocio;

import br.com.escola.dados.IRepositorio;
import br.com.escola.excecoes.DadoInvalidoException;
import br.com.escola.excecoes.EntidadeNaoEncontradaException;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class AlunoServico {

    private final IRepositorio<Aluno, String> alunoRepositorio;
    private final ResponsavelServico responsavelServico;

    public AlunoServico(IRepositorio<Aluno, String> alunoRepositorio, ResponsavelServico responsavelServico) {
        this.alunoRepositorio = alunoRepositorio;
        this.responsavelServico = responsavelServico;
    }

    public void adicionarAluno(Aluno aluno) throws DadoInvalidoException, IOException {
        if (aluno == null) {
            throw new DadoInvalidoException("Aluno não pode ser nulo.");
        }
        if (aluno.getMatricula() == null || aluno.getMatricula().trim().isEmpty()) {
            throw new DadoInvalidoException("Matrícula do aluno é obrigatória.");
        }
        if (aluno.getNome() == null || aluno.getNome().trim().isEmpty()) {
            throw new DadoInvalidoException("Nome do aluno é obrigatório.");
        }
        if (aluno.getAnoLetivo() <= 0) {
            throw new DadoInvalidoException("Ano letivo do aluno deve ser um valor positivo.");
        }
        if (!aluno.getMatricula().matches("\\d{7}")) {
            throw new DadoInvalidoException("Formato da matrícula inválido. Esperado 7 dígitos numéricos (ex: 1234567).");
        }
        
        if (aluno.getCpfsResponsaveis() != null && !aluno.getCpfsResponsaveis().isEmpty()) {
            for (String cpfResponsavel : aluno.getCpfsResponsaveis()) {
                try {
                    responsavelServico.buscarResponsavel(cpfResponsavel); 
                } catch (EntidadeNaoEncontradaException | DadoInvalidoException e) {
                    throw new DadoInvalidoException("Responsável com CPF " + cpfResponsavel + " não encontrado ou CPF inválido. " + e.getMessage());
                }
            }
        }
        
        try {
            alunoRepositorio.buscarPorId(aluno.getMatricula());
            throw new DadoInvalidoException("Já existe um aluno cadastrado com a matrícula: " + aluno.getMatricula());
        } catch (EntidadeNaoEncontradaException e) {
            alunoRepositorio.salvar(aluno);
        }
    }

    public Aluno buscarAluno(String matricula) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        if (matricula == null || matricula.trim().isEmpty()) {
            throw new DadoInvalidoException("Matrícula para busca não pode ser nula ou vazia.");
        }
        return alunoRepositorio.buscarPorId(matricula); 
    }

    public void atualizarAluno(Aluno aluno) throws DadoInvalidoException, EntidadeNaoEncontradaException, IOException {
        if (aluno == null) {
            throw new DadoInvalidoException("Aluno não pode ser nulo para atualização.");
        }
        if (aluno.getMatricula() == null || aluno.getMatricula().trim().isEmpty()) {
            throw new DadoInvalidoException("Matrícula do aluno é obrigatória para atualização.");
        }
        if (aluno.getNome() == null || aluno.getNome().trim().isEmpty()) {
            throw new DadoInvalidoException("Nome do aluno é obrigatório para atualização.");
        }
        if (aluno.getAnoLetivo() <= 0) {
            throw new DadoInvalidoException("Ano letivo do aluno deve ser um valor positivo para atualização.");
        }
        if (!aluno.getMatricula().matches("\\d{7}")) {
            throw new DadoInvalidoException("Formato da matrícula inválido. Esperado 7 dígitos numéricos (ex: 1234567).");
        }
        
        if (aluno.getCpfsResponsaveis() != null && !aluno.getCpfsResponsaveis().isEmpty()) {
            for (String cpfResponsavel : aluno.getCpfsResponsaveis()) {
                try {
                    responsavelServico.buscarResponsavel(cpfResponsavel);
                } catch (EntidadeNaoEncontradaException | DadoInvalidoException e) {
                    throw new DadoInvalidoException("Responsável com CPF " + cpfResponsavel + " não encontrado ou CPF inválido para atualização do aluno. " + e.getMessage());
                }
            }
        }
        
        alunoRepositorio.atualizar(aluno);
    }

    public void deletarAluno(String matricula) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        if (matricula == null || matricula.trim().isEmpty()) {
            throw new DadoInvalidoException("Matrícula para deleção não pode ser nula ou vazia.");
        }
        alunoRepositorio.deletar(matricula);
    }

    public List<Aluno> listarTodosAlunos() throws IOException {
        return alunoRepositorio.listarTodos();
    }

    public void adicionarResponsavelAoAluno(String matriculaAluno, String cpfResponsavel) throws DadoInvalidoException, EntidadeNaoEncontradaException, IOException {
        if (matriculaAluno == null || matriculaAluno.trim().isEmpty() || cpfResponsavel == null || cpfResponsavel.trim().isEmpty()) {
            throw new DadoInvalidoException("Matrícula do aluno e CPF do responsável são obrigatórios.");
        }
        Aluno aluno = buscarAluno(matriculaAluno);
        responsavelServico.buscarResponsavel(cpfResponsavel);
        
        if (aluno.getCpfsResponsaveis().contains(cpfResponsavel)) {
            throw new DadoInvalidoException("Responsável com CPF " + cpfResponsavel + " já está associado a este aluno.");
        }
        
        aluno.adicionarResponsavel(cpfResponsavel);
        alunoRepositorio.atualizar(aluno);
    }

    public void removerResponsavelDoAluno(String matriculaAluno, String cpfResponsavel) throws DadoInvalidoException, EntidadeNaoEncontradaException, IOException {
        if (matriculaAluno == null || matriculaAluno.trim().isEmpty() || cpfResponsavel == null || cpfResponsavel.trim().isEmpty()) {
            throw new DadoInvalidoException("Matrícula do aluno e CPF do responsável são obrigatórios.");
        }
        Aluno aluno = buscarAluno(matriculaAluno);
        
        if (!aluno.getCpfsResponsaveis().contains(cpfResponsavel)) {
            throw new EntidadeNaoEncontradaException("Responsável com CPF " + cpfResponsavel + " não está associado a este aluno.");
        }
        
        aluno.removerResponsavel(cpfResponsavel);
        alunoRepositorio.atualizar(aluno);
    }

    public List<Aluno> buscarAlunosPorAnoLetivo(int anoLetivo) throws DadoInvalidoException, IOException {
        if (anoLetivo <= 0) {
            throw new DadoInvalidoException("Ano letivo deve ser um valor positivo.");
        }
        return alunoRepositorio.listarTodos().stream()
                .filter(a -> a.getAnoLetivo() == anoLetivo)
                .collect(Collectors.toList());
    }
}