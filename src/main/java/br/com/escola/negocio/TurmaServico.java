package br.com.escola.negocio;

import br.com.escola.dados.IRepositorio;
import br.com.escola.excecoes.DadoInvalidoException;
import br.com.escola.excecoes.EntidadeNaoEncontradaException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TurmaServico {

    private final IRepositorio<Turma, String> turmaRepositorio;
    private final AlunoServico alunoServico;
    private final DisciplinaServico disciplinaServico;
    private final ProfessorServico professorServico;

    public TurmaServico(IRepositorio<Turma, String> turmaRepositorio,
                        AlunoServico alunoServico,
                        DisciplinaServico disciplinaServico,
                        ProfessorServico professorServico) {
        this.turmaRepositorio = turmaRepositorio;
        this.alunoServico = alunoServico;
        this.disciplinaServico = disciplinaServico;
        this.professorServico = professorServico;
    }

    public void adicionarTurma(Turma turma) throws DadoInvalidoException, EntidadeNaoEncontradaException { // Adicionado EntidadeNaoEncontradaException
        if (turma == null) {
            throw new DadoInvalidoException("Turma não pode ser nula.");
        }
        if (turma.getCodigo() == null || turma.getCodigo().trim().isEmpty()) {
            throw new DadoInvalidoException("Código da turma é obrigatório.");
        }
        if (turma.getNomeTurma() == null || turma.getNomeTurma().trim().isEmpty()) {
            throw new DadoInvalidoException("Nome da turma é obrigatório.");
        }

        // Verifica o professor coordenador
        if (turma.getProfessorCoordenador() == null ||
            turma.getProfessorCoordenador().getRegistroFuncional() == null ||
            turma.getProfessorCoordenador().getRegistroFuncional().trim().isEmpty()) {
            throw new DadoInvalidoException("Professor coordenador da turma é obrigatório.");
        }
        
        // CORREÇÃO AQUI: Re-lançar EntidadeNaoEncontradaException se o professor não for encontrado.
        // Isso permite que o teste de Turma no SistemaEscolarTeste.java trate essa exceção corretamente.
        try {
            professorServico.buscarProfessor(turma.getProfessorCoordenador().getRegistroFuncional());
        } catch (EntidadeNaoEncontradaException e) {
            // Se o professor não foi encontrado, é uma EntidadeNaoEncontradaException
            // A mensagem original do buscarProfessor já é boa, podemos re-lançar.
            throw new EntidadeNaoEncontradaException("Professor coordenador com registro " + turma.getProfessorCoordenador().getRegistroFuncional() + " não encontrado para associar à turma.");
        } catch (DadoInvalidoException e) {
            // Este catch deve ser para validações INTERNAS do ProfessorServico.buscarProfessor
            // (ex: se o registro funcional passado para buscarProfessor for nulo/vazio)
            throw new DadoInvalidoException("Erro na validação do registro funcional do professor coordenador: " + e.getMessage());
        }


        // Usa buscarPorId e Optional para verificar existência da turma
        if (turmaRepositorio.buscarPorId(turma.getCodigo()).isPresent()) {
            throw new DadoInvalidoException("Já existe uma turma com o código: " + turma.getCodigo());
        }

        turmaRepositorio.adicionar(turma);
    }

    public Turma buscarTurma(String codigo) throws EntidadeNaoEncontradaException, DadoInvalidoException {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new DadoInvalidoException("Código para busca da turma não pode ser nulo ou vazio.");
        }
        return turmaRepositorio.buscarPorId(codigo)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Turma com código " + codigo + " não encontrada."));
    }

    public void atualizarTurma(Turma turma) throws DadoInvalidoException, EntidadeNaoEncontradaException {
        if (turma == null) {
            throw new DadoInvalidoException("Turma não pode ser nula para atualização.");
        }
        if (turma.getCodigo() == null || turma.getCodigo().trim().isEmpty()) {
            throw new DadoInvalidoException("Código da turma é obrigatório para atualização.");
        }
        if (turma.getNomeTurma() == null || turma.getNomeTurma().trim().isEmpty()) {
            throw new DadoInvalidoException("Nome da turma é obrigatório para atualização.");
        }

        // Verifica e valida o professor coordenador
        if (turma.getProfessorCoordenador() == null ||
            turma.getProfessorCoordenador().getRegistroFuncional() == null ||
            turma.getProfessorCoordenador().getRegistroFuncional().trim().isEmpty()) {
            throw new DadoInvalidoException("Professor coordenador da turma é obrigatório para atualização.");
        }
        try {
            professorServico.buscarProfessor(turma.getProfessorCoordenador().getRegistroFuncional());
        } catch (EntidadeNaoEncontradaException e) {
            throw new EntidadeNaoEncontradaException("Professor coordenador com registro " + turma.getProfessorCoordenador().getRegistroFuncional() + " não encontrado para atualização da turma.");
        } catch (DadoInvalidoException e) {
            throw new DadoInvalidoException("Erro na validação do registro funcional do professor coordenador para atualização: " + e.getMessage());
        }

        // Verifica se a turma a ser atualizada realmente existe
        if (turmaRepositorio.buscarPorId(turma.getCodigo()).isEmpty()) {
            throw new EntidadeNaoEncontradaException("Turma com código " + turma.getCodigo() + " não encontrada para atualização.");
        }

        turmaRepositorio.atualizar(turma);
    }

    public boolean deletarTurma(String codigo) throws EntidadeNaoEncontradaException, DadoInvalidoException {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new DadoInvalidoException("Código para deleção da turma não pode ser nulo ou vazio.");
        }
        return turmaRepositorio.deletar(codigo);
    }

    public List<Turma> listarTodasTurmas() {
        return turmaRepositorio.listarTodos();
    }

    public void adicionarDisciplinaNaTurma(String codigoTurma, String codigoDisciplina) throws DadoInvalidoException, EntidadeNaoEncontradaException {
        if (codigoTurma == null || codigoTurma.trim().isEmpty()) {
            throw new DadoInvalidoException("Código da turma não pode ser nulo ou vazio.");
        }
        if (codigoDisciplina == null || codigoDisciplina.trim().isEmpty()) {
            throw new DadoInvalidoException("Código da disciplina não pode ser nulo ou vazio.");
        }

        Turma turma = buscarTurma(codigoTurma);
        Disciplina disciplina = disciplinaServico.buscarDisciplina(codigoDisciplina);

        if (turma.getDisciplinas().contains(disciplina)) {
            throw new DadoInvalidoException("A disciplina '" + disciplina.getNome() + "' já está associada a esta turma.");
        }
        turma.adicionarDisciplina(disciplina);
        turmaRepositorio.atualizar(turma);
    }

    public void removerDisciplinaDaTurma(String codigoTurma, String codigoDisciplina) throws DadoInvalidoException, EntidadeNaoEncontradaException {
        if (codigoTurma == null || codigoTurma.trim().isEmpty()) {
            throw new DadoInvalidoException("Código da turma não pode ser nulo ou vazio.");
        }
        if (codigoDisciplina == null || codigoDisciplina.trim().isEmpty()) {
            throw new DadoInvalidoException("Código da disciplina não pode ser nulo ou vazio.");
        }

        Turma turma = buscarTurma(codigoTurma);
        Disciplina disciplina = disciplinaServico.buscarDisciplina(codigoDisciplina);

        if (!turma.getDisciplinas().contains(disciplina)) {
            throw new EntidadeNaoEncontradaException("A disciplina '" + disciplina.getNome() + "' não está associada a esta turma.");
        }
        turma.removerDisciplina(disciplina);
        turmaRepositorio.atualizar(turma);
    }

    public void matricularAlunoNaTurma(String codigoTurma, String matriculaAluno) throws DadoInvalidoException, EntidadeNaoEncontradaException {
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

    public void desmatricularAlunoDaTurma(String codigoTurma, String matriculaAluno) throws DadoInvalidoException, EntidadeNaoEncontradaException {
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