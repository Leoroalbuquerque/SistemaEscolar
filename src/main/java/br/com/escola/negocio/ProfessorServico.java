package br.com.escola.negocio;

import br.com.escola.dados.IRepositorio;
import br.com.escola.excecoes.DadoInvalidoException;
import br.com.escola.excecoes.EntidadeNaoEncontradaException;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class ProfessorServico {
    private final IRepositorio<Professor, String> professorRepositorio;

    public ProfessorServico(IRepositorio<Professor, String> professorRepositorio) {
        this.professorRepositorio = professorRepositorio;
    }

    public void adicionarProfessor(Professor professor) throws DadoInvalidoException, EntidadeNaoEncontradaException, IOException {
        validarProfessor(professor);
        
        try {
            professorRepositorio.buscarPorId(professor.getRegistroFuncional());
            throw new DadoInvalidoException("Já existe um professor com o registro funcional: " + professor.getRegistroFuncional());
        } catch (EntidadeNaoEncontradaException e) {
            professorRepositorio.salvar(professor);
        }
    }

    public Professor buscarProfessor(String registroFuncional) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        if (registroFuncional == null || registroFuncional.trim().isEmpty()) {
            throw new DadoInvalidoException("Registro funcional para busca não pode ser nulo ou vazio.");
        }
        return professorRepositorio.buscarPorId(registroFuncional);
    }

    public Professor buscarProfessorPorCpf(String cpf) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        if (cpf == null || cpf.trim().isEmpty()) {
            throw new DadoInvalidoException("CPF para busca não pode ser nulo ou vazio.");
        }
        return professorRepositorio.listarTodos().stream()
                .filter(p -> p.getCpf() != null && p.getCpf().equals(cpf))
                .findFirst()
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Professor com CPF " + cpf + " não encontrado."));
    }

    public void atualizarProfessor(Professor professor) throws DadoInvalidoException, EntidadeNaoEncontradaException, IOException {
        validarProfessor(professor);
        professorRepositorio.buscarPorId(professor.getRegistroFuncional());
        professorRepositorio.atualizar(professor);
    }

    public void deletarProfessor(String registroFuncional) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        if (registroFuncional == null || registroFuncional.trim().isEmpty()) {
            throw new DadoInvalidoException("Registro funcional para deleção não pode ser nulo ou vazio.");
        }
        professorRepositorio.buscarPorId(registroFuncional);
        professorRepositorio.deletar(registroFuncional);
    }

    public List<Professor> listarTodosProfessores() throws IOException {
        return professorRepositorio.listarTodos();
    }

    public List<Professor> buscarProfessoresPorEspecialidade(String especialidade) throws DadoInvalidoException, IOException {
        if (especialidade == null || especialidade.trim().isEmpty()) {
            throw new DadoInvalidoException("Especialidade para busca não pode ser nula ou vazia.");
        }
        return professorRepositorio.listarTodos().stream()
                .filter(p -> p.getEspecialidade() != null && p.getEspecialidade().equalsIgnoreCase(especialidade.trim()))
                .collect(Collectors.toList());
    }

    private void validarProfessor(Professor professor) throws DadoInvalidoException {
        if (professor == null) {
            throw new DadoInvalidoException("Professor não pode ser nulo.");
        }
        if (professor.getRegistroFuncional() == null || professor.getRegistroFuncional().trim().isEmpty()) {
            throw new DadoInvalidoException("Registro funcional do professor é obrigatório.");
        }
        if (professor.getNome() == null || professor.getNome().trim().isEmpty()) {
            throw new DadoInvalidoException("Nome do professor é obrigatório.");
        }
        if (!professor.getRegistroFuncional().matches("[A-Z]{3}\\d{3}")) {
            throw new DadoInvalidoException("Formato do registro funcional inválido. Esperado XXX999 (ex: PRO123).");
        }
    }
}