package br.com.escola.negocio;

import br.com.escola.dados.IRepositorio;
import br.com.escola.excecoes.DadoInvalidoException;
import br.com.escola.excecoes.EntidadeNaoEncontradaException;

import java.util.List;
import java.util.Optional; // Importação necessária para Optional
import java.util.stream.Collectors;

public class ProfessorServico {

    private final IRepositorio<Professor, String> professorRepositorio;

    public ProfessorServico(IRepositorio<Professor, String> professorRepositorio) {
        this.professorRepositorio = professorRepositorio;
    }

    public void adicionarProfessor(Professor professor) throws DadoInvalidoException {
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

        // CORRIGIDO: Usa buscarPorId e Optional para verificar existência
        if (professorRepositorio.buscarPorId(professor.getRegistroFuncional()).isPresent()) {
            throw new DadoInvalidoException("Já existe um professor com o registro funcional: " + professor.getRegistroFuncional());
        }
        
        professorRepositorio.adicionar(professor);
    }

    public Professor buscarProfessor(String registroFuncional) throws EntidadeNaoEncontradaException, DadoInvalidoException {
        if (registroFuncional == null || registroFuncional.trim().isEmpty()) {
            throw new DadoInvalidoException("Registro funcional para busca não pode ser nulo ou vazio.");
        }
        // CORRIGIDO: Usa buscarPorId do repositório que retorna Optional
        return professorRepositorio.buscarPorId(registroFuncional)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Professor com registro " + registroFuncional + " não encontrado."));
    }

    public void atualizarProfessor(Professor professor) throws DadoInvalidoException, EntidadeNaoEncontradaException {
        if (professor == null) {
            throw new DadoInvalidoException("Professor não pode ser nulo para atualização.");
        }
        if (professor.getRegistroFuncional() == null || professor.getRegistroFuncional().trim().isEmpty()) {
            throw new DadoInvalidoException("Registro funcional do professor é obrigatório para atualização.");
        }
        if (professor.getNome() == null || professor.getNome().trim().isEmpty()) {
            throw new DadoInvalidoException("Nome do professor é obrigatório para atualização.");
        }
        if (!professor.getRegistroFuncional().matches("[A-Z]{3}\\d{3}")) {
            throw new DadoInvalidoException("Formato do registro funcional inválido. Esperado XXX999 (ex: PRO123).");
        }

        // CORRIGIDO: Verifica se o professor a ser atualizado realmente existe
        if (professorRepositorio.buscarPorId(professor.getRegistroFuncional()).isEmpty()) {
            throw new EntidadeNaoEncontradaException("Professor com registro " + professor.getRegistroFuncional() + " não encontrado para atualização.");
        }

        professorRepositorio.atualizar(professor);
    }

    public boolean deletarProfessor(String registroFuncional) throws EntidadeNaoEncontradaException, DadoInvalidoException {
        if (registroFuncional == null || registroFuncional.trim().isEmpty()) {
            throw new DadoInvalidoException("Registro funcional para deleção não pode ser nulo ou vazio.");
        }
        // O repositório já lança EntidadeNaoEncontradaException, então podemos chamar diretamente
        return professorRepositorio.deletar(registroFuncional);
    }

    public List<Professor> listarTodosProfessores() {
        return professorRepositorio.listarTodos();
    }

    public List<Professor> buscarProfessoresPorEspecialidade(String especialidade) throws DadoInvalidoException {
        if (especialidade == null || especialidade.trim().isEmpty()) {
            throw new DadoInvalidoException("Especialidade para busca não pode ser nula ou vazia.");
        }
        return professorRepositorio.listarTodos().stream()
                .filter(p -> p.getEspecialidade() != null && p.getEspecialidade().equalsIgnoreCase(especialidade.trim()))
                .collect(Collectors.toList());
    }
}