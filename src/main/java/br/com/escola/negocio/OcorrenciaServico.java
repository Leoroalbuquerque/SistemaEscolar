package br.com.escola.negocio;

import br.com.escola.dados.IRepositorio;
import br.com.escola.excecoes.DadoInvalidoException;
import br.com.escola.excecoes.EntidadeNaoEncontradaException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class OcorrenciaServico {

    private IRepositorio<Ocorrencia, String> repositorioOcorrencias;
    private AlunoServico alunoServico;
    private FuncionarioServico funcionarioServico;
    private ProfessorServico professorServico;

    public OcorrenciaServico(IRepositorio<Ocorrencia, String> repositorioOcorrencias,
                             AlunoServico alunoServico,
                             FuncionarioServico funcionarioServico,
                             ProfessorServico professorServico) {
        this.repositorioOcorrencias = repositorioOcorrencias;
        this.alunoServico = alunoServico;
        this.funcionarioServico = funcionarioServico;
        this.professorServico = professorServico;
    }

    public void adicionarOcorrencia(Ocorrencia ocorrencia) throws DadoInvalidoException, EntidadeNaoEncontradaException {
        if (ocorrencia == null) {
            throw new DadoInvalidoException("Ocorrência não pode ser nula.");
        }
        if (ocorrencia.getId() == null || ocorrencia.getId().trim().isEmpty()) {
            throw new DadoInvalidoException("ID da ocorrência não pode ser nulo ou vazio.");
        }
        if (ocorrencia.getDescricao() == null || ocorrencia.getDescricao().trim().isEmpty()) {
            throw new DadoInvalidoException("Descrição da ocorrência não pode ser nula ou vazia.");
        }
        if (ocorrencia.getDataHora() == null) {
            throw new DadoInvalidoException("Data e hora da ocorrência não podem ser nulas.");
        }
        if (ocorrencia.getRegistradorId() == null || ocorrencia.getRegistradorId().trim().isEmpty()) {
            throw new DadoInvalidoException("ID do registrador da ocorrência não pode ser nulo ou vazio.");
        }
        if (ocorrencia.getAluno() == null || ocorrencia.getAluno().getMatricula() == null || ocorrencia.getAluno().getMatricula().trim().isEmpty()) {
            throw new DadoInvalidoException("Aluno da ocorrência e sua matrícula não podem ser nulos ou vazios.");
        }

        alunoServico.buscarAluno(ocorrencia.getAluno().getMatricula());

        boolean registradorEncontrado = false;
        try {
            funcionarioServico.buscarFuncionario(ocorrencia.getRegistradorId());
            registradorEncontrado = true;
        } catch (EntidadeNaoEncontradaException | DadoInvalidoException e) {
            try {
                professorServico.buscarProfessor(ocorrencia.getRegistradorId());
                registradorEncontrado = true;
            } catch (EntidadeNaoEncontradaException | DadoInvalidoException ex) {
            }
        }

        if (!registradorEncontrado) {
            throw new EntidadeNaoEncontradaException("Registrador com ID " + ocorrencia.getRegistradorId() + " não encontrado como funcionário ou professor.");
        }

        if (repositorioOcorrencias.buscarPorId(ocorrencia.getId()).isPresent()) {
            throw new DadoInvalidoException("Ocorrência com ID " + ocorrencia.getId() + " já existe.");
        }

        repositorioOcorrencias.salvar(ocorrencia);
    }

    public Ocorrencia buscarOcorrencia(String id) throws EntidadeNaoEncontradaException, DadoInvalidoException {
        if (id == null || id.trim().isEmpty()) {
            throw new DadoInvalidoException("ID da ocorrência não pode ser nulo ou vazio para busca.");
        }
        return repositorioOcorrencias.buscarPorId(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Ocorrência com ID " + id + " não encontrada."));
    }

    public List<Ocorrencia> listarTodasOcorrencias() {
        return repositorioOcorrencias.listarTodos();
    }

    public void atualizarOcorrencia(Ocorrencia ocorrencia) throws EntidadeNaoEncontradaException, DadoInvalidoException {
        if (ocorrencia == null || ocorrencia.getId() == null || ocorrencia.getId().trim().isEmpty()) {
            throw new DadoInvalidoException("Ocorrência e seu ID não podem ser nulos ou vazios para atualização.");
        }
        if (repositorioOcorrencias.buscarPorId(ocorrencia.getId()).isEmpty()) {
            throw new EntidadeNaoEncontradaException("Ocorrência com ID " + ocorrencia.getId() + " não encontrada para atualização.");
        }

        if (ocorrencia.getAluno() != null && (ocorrencia.getAluno().getMatricula() == null || ocorrencia.getAluno().getMatricula().trim().isEmpty())) {
            throw new DadoInvalidoException("Matrícula do aluno da ocorrência não pode ser nula ou vazia na atualização.");
        }
        if (ocorrencia.getAluno() != null) {
            alunoServico.buscarAluno(ocorrencia.getAluno().getMatricula());
        }

        if (ocorrencia.getRegistradorId() == null || ocorrencia.getRegistradorId().trim().isEmpty()) {
            throw new DadoInvalidoException("ID do registrador da ocorrência não pode ser nulo ou vazio na atualização.");
        }

        boolean registradorEncontrado = false;
        try {
            funcionarioServico.buscarFuncionario(ocorrencia.getRegistradorId());
            registradorEncontrado = true;
        } catch (EntidadeNaoEncontradaException | DadoInvalidoException e) {
            try {
                professorServico.buscarProfessor(ocorrencia.getRegistradorId());
                registradorEncontrado = true;
            } catch (EntidadeNaoEncontradaException | DadoInvalidoException ex) {
            }
        }

        if (!registradorEncontrado) {
            throw new EntidadeNaoEncontradaException("Registrador com ID " + ocorrencia.getRegistradorId() + " não encontrado como funcionário ou professor para atualização da ocorrência.");
        }

        repositorioOcorrencias.atualizar(ocorrencia);
    }

    public boolean deletarOcorrencia(String id) throws EntidadeNaoEncontradaException, DadoInvalidoException {
        if (id == null || id.trim().isEmpty()) {
            throw new DadoInvalidoException("ID da ocorrência não pode ser nulo ou vazio para exclusão.");
        }
        if (repositorioOcorrencias.buscarPorId(id).isEmpty()) {
            throw new EntidadeNaoEncontradaException("Ocorrência com ID " + id + " não encontrada para exclusão.");
        }
        return repositorioOcorrencias.deletar(id);
    }

    public void registrarMedidasOcorrencia(String idOcorrencia, String medidas) throws EntidadeNaoEncontradaException, DadoInvalidoException {
        Ocorrencia ocorrencia = buscarOcorrencia(idOcorrencia);
        ocorrencia.registrarMedidas(medidas);
        atualizarOcorrencia(ocorrencia);
    }

    public void encerrarOcorrencia(String idOcorrencia) throws EntidadeNaoEncontradaException, DadoInvalidoException {
        Ocorrencia ocorrencia = buscarOcorrencia(idOcorrencia);
        ocorrencia.encerrarOcorrencia();
        atualizarOcorrencia(ocorrencia);
    }

    public void limparRepositorio() {
        if (repositorioOcorrencias instanceof br.com.escola.dados.OcorrenciaRepositorioJson) {
            ((br.com.escola.dados.OcorrenciaRepositorioJson) repositorioOcorrencias).limpar();
        } else {
            System.err.println("O repositório de Ocorrências não suporta a operação de limpeza direta.");
        }
    }
}