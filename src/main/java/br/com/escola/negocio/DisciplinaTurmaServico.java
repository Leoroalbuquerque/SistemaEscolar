package br.com.escola.negocio;

import br.com.escola.dados.DisciplinaTurmaRepositorioJson;
import br.com.escola.excecoes.DadoInvalidoException;
import br.com.escola.excecoes.EntidadeNaoEncontradaException;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DisciplinaTurmaServico {
    private final DisciplinaTurmaRepositorioJson repositorio;
    private final TurmaServico turmaServico;
    private final DisciplinaServico disciplinaServico;
    private final ProfessorServico professorServico;

    public DisciplinaTurmaServico(DisciplinaTurmaRepositorioJson repositorio, 
                                TurmaServico turmaServico,
                                DisciplinaServico disciplinaServico,
                                ProfessorServico professorServico) {
        this.repositorio = repositorio;
        this.turmaServico = turmaServico;
        this.disciplinaServico = disciplinaServico;
        this.professorServico = professorServico;
    }

    public void adicionarDisciplinaComProfessorNaTurma(String codigoTurma, String codigoDisciplina, String registroProfessor)
            throws DadoInvalidoException, EntidadeNaoEncontradaException, IOException {
        Turma turma = turmaServico.buscarTurma(codigoTurma);
        Disciplina disciplina = disciplinaServico.buscarDisciplina(codigoDisciplina);
        Professor professor = professorServico.buscarProfessor(registroProfessor);

        List<DisciplinaTurma> disciplinasTurma = listarPorTurma(codigoTurma);
        DisciplinaTurma atribuicaoExistente = disciplinasTurma.stream()
                .filter(dt -> dt.getDisciplina().equals(disciplina))
                .findFirst()
                .orElse(null);

        if (atribuicaoExistente != null) {
            if (atribuicaoExistente.getProfessor() != null && atribuicaoExistente.getProfessor().equals(professor)) {
                throw new DadoInvalidoException("O professor já está atribuído a esta disciplina e turma.");
            }
            if (atribuicaoExistente.getProfessor() != null) {
                throw new DadoInvalidoException("A disciplina '" + disciplina.getNome() + "' já tem um professor atribuído nesta turma.");
            }
            atribuicaoExistente.setProfessor(professor);
            repositorio.atualizar(atribuicaoExistente);
        } else {
            DisciplinaTurma novaAtribuicao = new DisciplinaTurma(null, disciplina, professor, turma);
            repositorio.salvar(novaAtribuicao);
        }
    }

    public void removerDisciplinaTurma(String id) throws EntidadeNaoEncontradaException, IOException {
        repositorio.deletar(id);
    }

    public void atualizarDisciplinaTurma(DisciplinaTurma disciplinaTurma) throws EntidadeNaoEncontradaException, IOException {
        repositorio.atualizar(disciplinaTurma);
    }

    public List<DisciplinaTurma> listarTodas() throws IOException {
        return repositorio.listarTodos();
    }

    public List<DisciplinaTurma> listarPorProfessor(String registroProfessor) throws EntidadeNaoEncontradaException, IOException, DadoInvalidoException {
        Professor professor = professorServico.buscarProfessor(registroProfessor);
        return repositorio.listarTodos().stream()
                .filter(dt -> dt.getProfessor() != null && dt.getProfessor().equals(professor))
                .collect(Collectors.toList());
    }

    public List<DisciplinaTurma> listarPorTurma(String codigoTurma) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        Turma turma = turmaServico.buscarTurma(codigoTurma);
        return repositorio.listarTodos().stream()
                .filter(dt -> dt.getTurma().equals(turma))
                .collect(Collectors.toList());
    }

    public List<DisciplinaTurma> listarPorDisciplina(String codigoDisciplina) throws EntidadeNaoEncontradaException, IOException, DadoInvalidoException {
        Disciplina disciplina = disciplinaServico.buscarDisciplina(codigoDisciplina);
        return repositorio.listarTodos().stream()
                .filter(dt -> dt.getDisciplina().equals(disciplina))
                .collect(Collectors.toList());
    }

    public DisciplinaTurma buscarPorAtributos(Disciplina disciplina, Professor professor, Turma turma) 
            throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        return repositorio.listarTodos().stream()
                .filter(dt -> dt.getDisciplina().equals(disciplina)
                        && dt.getTurma().equals(turma)
                        && Objects.equals(dt.getProfessor(), professor))
                .findFirst()
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Atribuição não encontrada."));
    }

    public DisciplinaTurma buscarAtribuicao(Turma turma, Disciplina disciplina) 
            throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        return repositorio.listarTodos().stream()
                .filter(dt -> dt.getTurma().equals(turma) && dt.getDisciplina().equals(disciplina))
                .findFirst()
                .orElse(null);
    }

    public List<Disciplina> buscarDisciplinasPorTurma(String codigoTurma) 
            throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        Turma turma = turmaServico.buscarTurma(codigoTurma);
        return listarPorTurma(turma.getCodigo()).stream()
                .map(DisciplinaTurma::getDisciplina)
                .collect(Collectors.toList());
    }

    public List<Professor> buscarProfessoresPorTurma(String codigoTurma) 
            throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        Turma turma = turmaServico.buscarTurma(codigoTurma);
        return listarPorTurma(turma.getCodigo()).stream()
                .map(DisciplinaTurma::getProfessor)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}