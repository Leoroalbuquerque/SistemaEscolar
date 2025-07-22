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

    public TurmaServico(IRepositorio<Turma, String> turmaRepositorio,
                        AlunoServico alunoServico,
                        DisciplinaServico disciplinaServico,
                        ProfessorServico professorServico) {
        if (turmaRepositorio == null || alunoServico == null || disciplinaServico == null || professorServico == null) {
            throw new IllegalArgumentException("Todos os serviços e repositórios devem ser fornecidos e não podem ser nulos.");
        }
        this.turmaRepositorio = turmaRepositorio;
        this.alunoServico = alunoServico;
        this.disciplinaServico = disciplinaServico;
        this.professorServico = professorServico;
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

        if (turma.getProfessorResponsavel() == null ||
            turma.getProfessorResponsavel().getRegistroFuncional() == null ||
            turma.getProfessorResponsavel().getRegistroFuncional().trim().isEmpty()) {
            throw new DadoInvalidoException("Professor responsável da turma é obrigatório.");
        }

        try {
            professorServico.buscarProfessor(turma.getProfessorResponsavel().getRegistroFuncional());
        } catch (EntidadeNaoEncontradaException e) {
            throw new EntidadeNaoEncontradaException("Professor responsável com registro " + turma.getProfessorResponsavel().getRegistroFuncional() + " não encontrado para associar à turma.");
        } catch (DadoInvalidoException e) {
            throw new DadoInvalidoException("Erro na validação do registro funcional do professor responsável: " + e.getMessage());
        }

        if (turmaRepositorio.buscarPorId(turma.getCodigo()).isPresent()) {
            throw new DadoInvalidoException("Já existe uma turma com o código: " + turma.getCodigo());
        }

        turmaRepositorio.salvar(turma);
    }

    public Turma buscarTurma(String codigo) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new DadoInvalidoException("Código para busca da turma não pode ser nulo ou vazio.");
        }
        return turmaRepositorio.buscarPorId(codigo)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Turma com código " + codigo + " não encontrada."));
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

        if (turma.getProfessorResponsavel() == null ||
            turma.getProfessorResponsavel().getRegistroFuncional() == null ||
            turma.getProfessorResponsavel().getRegistroFuncional().trim().isEmpty()) {
            throw new DadoInvalidoException("Professor responsável da turma é obrigatório para atualização.");
        }

        try {
            professorServico.buscarProfessor(turma.getProfessorResponsavel().getRegistroFuncional());
        } catch (EntidadeNaoEncontradaException e) {
            throw new EntidadeNaoEncontradaException("Professor responsável com registro " + turma.getProfessorResponsavel().getRegistroFuncional() + " não encontrado para atualização da turma.");
        } catch (DadoInvalidoException e) {
            throw new DadoInvalidoException("Erro na validação do registro funcional do professor responsável para atualização: " + e.getMessage());
        }

        if (turmaRepositorio.buscarPorId(turma.getCodigo()).isEmpty()) {
            throw new EntidadeNaoEncontradaException("Turma com código " + turma.getCodigo() + " não encontrada para atualização.");
        }

        turmaRepositorio.atualizar(turma);
    }

    public boolean deletarTurma(String codigo) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new DadoInvalidoException("Código para deleção da turma não pode ser nulo ou vazio.");
        }
        if (turmaRepositorio.buscarPorId(codigo).isEmpty()) {
            throw new EntidadeNaoEncontradaException("Turma com código " + codigo + " não encontrada para deleção.");
        }
        return turmaRepositorio.deletar(codigo);
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
}