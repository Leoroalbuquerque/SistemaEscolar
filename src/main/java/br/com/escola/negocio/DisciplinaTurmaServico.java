package br.com.escola.negocio;

import br.com.escola.dados.DisciplinaTurmaRepositorioJson;
import br.com.escola.excecoes.EntidadeNaoEncontradaException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DisciplinaTurmaServico implements Serializable {
    private static final long serialVersionUID = 1L;

    private DisciplinaTurmaRepositorioJson repositorioDisciplinaTurma;

    public DisciplinaTurmaServico(DisciplinaTurmaRepositorioJson repositorioDisciplinaTurma) {
        if (repositorioDisciplinaTurma == null) {
            throw new IllegalArgumentException("O repositório de DisciplinaTurma não pode ser nulo.");
        }
        this.repositorioDisciplinaTurma = repositorioDisciplinaTurma;
    }

    public void adicionarDisciplinaTurma(DisciplinaTurma disciplinaTurma) throws IOException, IllegalArgumentException {
        if (disciplinaTurma == null) {
            throw new IllegalArgumentException("A associação de disciplina-turma não pode ser nula.");
        }
        if (disciplinaTurma.getDisciplina() == null || disciplinaTurma.getProfessor() == null || disciplinaTurma.getTurma() == null) {
            throw new IllegalArgumentException("A associação de disciplina-turma deve ter uma disciplina, um professor e uma turma válidos.");
        }

        DisciplinaTurma existing = repositorioDisciplinaTurma.buscar(disciplinaTurma.getDisciplina(), disciplinaTurma.getProfessor(), disciplinaTurma.getTurma());
        if (existing != null) {
            throw new IllegalArgumentException("Esta atribuição de disciplina para esta turma por este professor já existe.");
        }

        this.repositorioDisciplinaTurma.salvar(disciplinaTurma);
        System.out.println("Atribuição '" + disciplinaTurma + "' adicionada com sucesso.");
    }

    public void removerDisciplinaTurma(String idDisciplinaTurma) throws IOException, EntidadeNaoEncontradaException {
        if (idDisciplinaTurma == null || idDisciplinaTurma.trim().isEmpty()) {
            throw new IllegalArgumentException("O ID da associação de disciplina-turma não pode ser nulo ou vazio.");
        }
        
        DisciplinaTurma dt = repositorioDisciplinaTurma.buscarPorId(idDisciplinaTurma);
        if (dt == null) {
            throw new EntidadeNaoEncontradaException("Atribuição de disciplina-turma com ID " + idDisciplinaTurma + " não encontrada para remoção.");
        }
        
        this.repositorioDisciplinaTurma.remover(dt);
        System.out.println("Atribuição com ID '" + idDisciplinaTurma + "' removida com sucesso.");
    }
    
    public void atualizarDisciplinaTurma(DisciplinaTurma disciplinaTurma) throws IOException, EntidadeNaoEncontradaException {
        if (disciplinaTurma == null || disciplinaTurma.getId() == null || disciplinaTurma.getId().trim().isEmpty()) {
            throw new IllegalArgumentException("A associação de disciplina-turma ou seu ID não podem ser nulos para atualização.");
        }
        if (disciplinaTurma.getDisciplina() == null || disciplinaTurma.getProfessor() == null || disciplinaTurma.getTurma() == null) {
            throw new IllegalArgumentException("A associação de disciplina-turma deve ter uma disciplina, um professor e uma turma válidos.");
        }
        
        DisciplinaTurma existing = repositorioDisciplinaTurma.buscarPorId(disciplinaTurma.getId());
        if (existing == null) {
            throw new EntidadeNaoEncontradaException("Atribuição de disciplina-turma com ID " + disciplinaTurma.getId() + " não encontrada para atualização.");
        }

        this.repositorioDisciplinaTurma.atualizar(disciplinaTurma);
        System.out.println("Atribuição '" + disciplinaTurma + "' atualizada com sucesso.");
    }

    public DisciplinaTurma buscarPorAtributos(Disciplina disciplina, Professor professor, Turma turma) throws IOException {
        return repositorioDisciplinaTurma.buscar(disciplina, professor, turma);
    }

    public DisciplinaTurma buscarPorId(String id) throws IOException {
        return repositorioDisciplinaTurma.buscarPorId(id);
    }

    public List<DisciplinaTurma> listarTodas() throws IOException {
        return repositorioDisciplinaTurma.listarTodos();
    }

    public List<DisciplinaTurma> listarDisciplinasPorProfessor(Professor professor) throws IOException {
        if (professor == null) {
            return new ArrayList<>();
        }
        return repositorioDisciplinaTurma.listarPorProfessor(professor);
    }

    public List<DisciplinaTurma> listarProfessoresPorTurma(Turma turma) throws IOException {
        if (turma == null) {
            return new ArrayList<>();
        }
        return repositorioDisciplinaTurma.listarPorTurma(turma);
    }

    public List<DisciplinaTurma> listarTurmasPorDisciplina(Disciplina disciplina) throws IOException {
        if (disciplina == null) {
            return new ArrayList<>();
        }
        return repositorioDisciplinaTurma.listarPorDisciplina(disciplina);
    }
}