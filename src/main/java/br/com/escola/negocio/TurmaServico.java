package br.com.escola.negocio;

import br.com.escola.dados.IRepositorio;
import br.com.escola.excecoes.DadoInvalidoException;
import br.com.escola.excecoes.EntidadeNaoEncontradaException;

import java.io.IOException;
import java.util.List;

public class TurmaServico {

    private final IRepositorio<Turma, String> turmaRepositorio;
    private final AlunoServico alunoServico;
    private final DisciplinaServico disciplinaServico;
    private final ProfessorServico professorServico;
    private final SerieEscolarServico serieEscolarServico;

    public TurmaServico(IRepositorio<Turma, String> turmaRepositorio,
                        AlunoServico alunoServico,
                        DisciplinaServico disciplinaServico,
                        ProfessorServico professorServico,
                        SerieEscolarServico serieEscolarServico) {
        if (turmaRepositorio == null || alunoServico == null || disciplinaServico == null || professorServico == null || serieEscolarServico == null) {
            throw new IllegalArgumentException("Todos os serviços e repositórios devem ser fornecidos e não podem ser nulos.");
        }
        this.turmaRepositorio = turmaRepositorio;
        this.alunoServico = alunoServico;
        this.disciplinaServico = disciplinaServico;
        this.professorServico = professorServico;
        this.serieEscolarServico = serieEscolarServico;
    }

    public void adicionarTurma(Turma turma) throws DadoInvalidoException, EntidadeNaoEncontradaException, IOException {
        if (turma == null) {
            throw new DadoInvalidoException("Turma não pode ser nula.");
        }
        if (turma.getCodigo() == null || turma.getCodigo().trim().isEmpty()) {
            throw new DadoInvalidoException("Código da turma é obrigatório.");
        }
        if (turma.getNomeTurma() == null || turma.getNomeTurma().trim().isEmpty()) {
            throw new DadoInvalidoException("Nome da turma é obrigatório.");
        }
        if (turma.getAnoLetivo() <= 0) {
            throw new DadoInvalidoException("Ano letivo da turma deve ser um valor positivo.");
        }
        if (turma.getSerieEscolar() == null || turma.getSerieEscolar().getCodigoSerie() == null || turma.getSerieEscolar().getCodigoSerie().trim().isEmpty()) {
            throw new DadoInvalidoException("Série escolar da turma é obrigatória.");
        }
        if (turma.getTurno() == null || turma.getTurno().trim().isEmpty()) {
            throw new DadoInvalidoException("Turno da turma é obrigatório.");
        }

        serieEscolarServico.buscarSerieEscolar(turma.getSerieEscolar().getCodigoSerie());

        try {
            turmaRepositorio.buscarPorId(turma.getCodigo());
            throw new DadoInvalidoException("Já existe uma turma com o código: " + turma.getCodigo());
        } catch (EntidadeNaoEncontradaException e) {
            turmaRepositorio.salvar(turma);
        }
    }

    public Turma buscarTurma(String codigo) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new DadoInvalidoException("Código para busca da turma não pode ser nulo ou vazio.");
        }
        return turmaRepositorio.buscarPorId(codigo);
    }

    public void atualizarTurma(Turma turma) throws DadoInvalidoException, EntidadeNaoEncontradaException, IOException {
        if (turma == null) {
            throw new DadoInvalidoException("Turma não pode ser nula para atualização.");
        }
        if (turma.getCodigo() == null || turma.getCodigo().trim().isEmpty()) {
            throw new DadoInvalidoException("Código da turma é obrigatório para atualização.");
        }
        if (turma.getNomeTurma() == null || turma.getNomeTurma().trim().isEmpty()) {
            throw new DadoInvalidoException("Nome da turma é obrigatório para atualização.");
        }
        if (turma.getAnoLetivo() <= 0) {
            throw new DadoInvalidoException("Ano letivo da turma deve ser um valor positivo para atualização.");
        }
        if (turma.getSerieEscolar() == null || turma.getSerieEscolar().getCodigoSerie() == null || turma.getSerieEscolar().getCodigoSerie().trim().isEmpty()) {
            throw new DadoInvalidoException("Série escolar da turma é obrigatória para atualização.");
        }
        if (turma.getTurno() == null || turma.getTurno().trim().isEmpty()) {
            throw new DadoInvalidoException("Turno da turma é obrigatório para atualização.");
        }

        serieEscolarServico.buscarSerieEscolar(turma.getSerieEscolar().getCodigoSerie());

        turmaRepositorio.buscarPorId(turma.getCodigo());

        turmaRepositorio.atualizar(turma);
    }

    public void deletarTurma(String codigo) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new DadoInvalidoException("Código para deleção da turma não pode ser nulo ou vazio.");
        }
        turmaRepositorio.buscarPorId(codigo);
        turmaRepositorio.deletar(codigo);
    }

    public List<Turma> listarTodasTurmas() throws IOException {
        return turmaRepositorio.listarTodos();
    }

    public void matricularAlunoNaTurma(String codigoTurma, String matriculaAluno) throws DadoInvalidoException, EntidadeNaoEncontradaException, IOException {
        if (codigoTurma == null || codigoTurma.trim().isEmpty()) {
            throw new DadoInvalidoException("Código da turma não pode ser nulo ou vazio.");
        }
        if (matriculaAluno == null || matriculaAluno.trim().isEmpty()) {
            throw new DadoInvalidoException("Matrícula do aluno não pode ser nulo ou vazio.");
        }

        Turma turma = buscarTurma(codigoTurma);
        Aluno aluno = alunoServico.buscarAluno(matriculaAluno);

        if (turma.getAlunosMatriculados().contains(aluno)) {
            throw new DadoInvalidoException("O aluno '" + aluno.getNome() + "' já está matriculado nesta turma.");
        }

        turma.matricularAluno(aluno);
        turmaRepositorio.atualizar(turma);
    }

    public void desmatricularAlunoDaTurma(String codigoTurma, String matriculaAluno) throws DadoInvalidoException, EntidadeNaoEncontradaException, IOException {
        if (codigoTurma == null || codigoTurma.trim().isEmpty()) {
            throw new DadoInvalidoException("Código da turma não pode ser nulo ou vazio.");
        }
        if (matriculaAluno == null || matriculaAluno.trim().isEmpty()) {
            throw new DadoInvalidoException("Matrícula do aluno não pode ser nulo ou vazio.");
        }

        Turma turma = buscarTurma(codigoTurma);
        Aluno aluno = alunoServico.buscarAluno(matriculaAluno);

        if (!turma.getAlunosMatriculados().contains(aluno)) {
            throw new EntidadeNaoEncontradaException("O aluno '" + aluno.getNome() + "' não está matriculado nesta turma.");
        }

        turma.desmatricularAluno(aluno);
        turmaRepositorio.atualizar(turma);
    }

    public void adicionarProfessorNaTurma(String codigoTurma, String cpfProfessor) throws DadoInvalidoException, EntidadeNaoEncontradaException, IOException {
        if (codigoTurma == null || codigoTurma.trim().isEmpty()) {
            throw new DadoInvalidoException("Código da turma não pode ser nulo ou vazio.");
        }
        if (cpfProfessor == null || cpfProfessor.trim().isEmpty()) {
            throw new DadoInvalidoException("CPF do professor não pode ser nulo ou vazio.");
        }

        Turma turma = buscarTurma(codigoTurma);
        Professor professor = professorServico.buscarProfessorPorCpf(cpfProfessor);

        if (turma.getProfessoresAssociados().contains(professor)) {
            throw new DadoInvalidoException("O professor '" + professor.getNome() + "' já está associado a esta turma.");
        }

        turma.adicionarProfessor(professor);
        turmaRepositorio.atualizar(turma);
    }

    public void removerProfessorDaTurma(String codigoTurma, String cpfProfessor) throws DadoInvalidoException, EntidadeNaoEncontradaException, IOException {
        if (codigoTurma == null || codigoTurma.trim().isEmpty()) {
            throw new DadoInvalidoException("Código da turma não pode ser nulo ou vazio.");
        }
        if (cpfProfessor == null || cpfProfessor.trim().isEmpty()) {
            throw new DadoInvalidoException("CPF do professor não pode ser nulo ou vazio.");
        }

        Turma turma = buscarTurma(codigoTurma);
        Professor professor = professorServico.buscarProfessorPorCpf(cpfProfessor);

        if (!turma.getProfessoresAssociados().contains(professor)) {
            throw new EntidadeNaoEncontradaException("O professor '" + professor.getNome() + "' não está associado a esta turma.");
        }

        turma.removerProfessor(professor);
        turmaRepositorio.atualizar(turma);
    }

    public void adicionarDisciplinaNaTurma(String codigoTurma, String codigoDisciplina) throws DadoInvalidoException, EntidadeNaoEncontradaException, IOException {
        if (codigoTurma == null || codigoTurma.trim().isEmpty()) {
            throw new DadoInvalidoException("Código da turma não pode ser nulo ou vazio.");
        }
        if (codigoDisciplina == null || codigoDisciplina.trim().isEmpty()) {
            throw new DadoInvalidoException("Código da disciplina não pode ser nulo ou vazio.");
        }

        Turma turma = buscarTurma(codigoTurma);
        Disciplina disciplina = disciplinaServico.buscarDisciplina(codigoDisciplina);

        boolean jaAssociada = turma.getDisciplinas().stream()
                                    .anyMatch(d -> d.getCodigo().equals(disciplina.getCodigo()));

        if (jaAssociada) {
            throw new DadoInvalidoException("A disciplina '" + disciplina.getNome() + "' já está associada a esta turma.");
        }

        DisciplinaTurma novaAtribuicao = new DisciplinaTurma(disciplina, turma, 0);

        turma.adicionarAtribuicaoDisciplinaTurma(novaAtribuicao);
        turmaRepositorio.atualizar(turma);
    }

    public void removerDisciplinaDaTurma(String codigoTurma, String codigoDisciplina) throws DadoInvalidoException, EntidadeNaoEncontradaException, IOException {
        if (codigoTurma == null || codigoTurma.trim().isEmpty()) {
            throw new DadoInvalidoException("Código da turma não pode ser nulo ou vazio.");
        }
        if (codigoDisciplina == null || codigoDisciplina.trim().isEmpty()) {
            throw new DadoInvalidoException("Código da disciplina não pode ser nulo ou vazio.");
        }

        Turma turma = buscarTurma(codigoTurma);
        
        DisciplinaTurma atribuicaoParaRemover = turma.getAtribuicoesDisciplinaTurma().stream()
            .filter(dt -> dt.getDisciplina() != null && dt.getDisciplina().getCodigo().equals(codigoDisciplina))
            .findFirst()
            .orElseThrow(() -> new EntidadeNaoEncontradaException("A disciplina com código " + codigoDisciplina + " não está associada a esta turma."));

        turma.removerAtribuicaoDisciplinaTurma(atribuicaoParaRemover);
        turmaRepositorio.atualizar(turma);
    }

    public List<Aluno> listarAlunosNaTurma(String codigoTurma) throws DadoInvalidoException, EntidadeNaoEncontradaException, IOException {
        if (codigoTurma == null || codigoTurma.trim().isEmpty()) {
            throw new DadoInvalidoException("Código da turma não pode ser nulo ou vazio.");
        }
        Turma turma = turmaRepositorio.buscarPorId(codigoTurma);
        return turma.getAlunosMatriculados();
    }
}