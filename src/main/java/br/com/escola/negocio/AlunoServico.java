package br.com.escola.negocio;

import br.com.escola.dados.IRepositorio;
import br.com.escola.dados.RepositorioAlunoDAO;
import br.com.escola.excecoes.DadoInvalidoException;
import br.com.escola.excecoes.EntidadeNaoEncontradaException;
import java.util.List;

public class AlunoServico implements IAlunoServico {
    private IRepositorio<Aluno, String> repositorioAlunos;

    public AlunoServico() {
        this.repositorioAlunos = new RepositorioAlunoDAO();
    }

    private void validarAluno(Aluno aluno) throws DadoInvalidoException {
        if (aluno == null) {
            throw new DadoInvalidoException("O objeto aluno não pode ser nulo.");
        }
        if (aluno.getNome() == null || aluno.getNome().trim().isEmpty()) {
            throw new DadoInvalidoException("Nome do aluno é obrigatório.");
        }
        if (aluno.getMatricula() == null || aluno.getMatricula().trim().isEmpty()) {
            throw new DadoInvalidoException("Matrícula do aluno é obrigatória.");
        }
    }

    @Override
    public Aluno criarAluno(Aluno aluno) throws DadoInvalidoException {
        validarAluno(aluno);

        try {
            repositorioAlunos.buscarPorId(aluno.getMatricula());
            throw new DadoInvalidoException("Já existe um aluno cadastrado com a matrícula: " + aluno.getMatricula());
        } catch (EntidadeNaoEncontradaException e) {
            System.out.println("Matrícula " + aluno.getMatricula() + " disponível. Prosseguindo com o cadastro.");
        }

        repositorioAlunos.salvar(aluno);
        System.out.println("Aluno " + aluno.getNome() + " salvo com sucesso.");
        return aluno;
    }

    @Override
    public Aluno consultarAlunoPorMatricula(String matricula) throws EntidadeNaoEncontradaException {
        try {
            return repositorioAlunos.buscarPorId(matricula);
        } catch (EntidadeNaoEncontradaException e) {
            System.err.println("Erro no AlunoServico ao consultar aluno com matrícula " + matricula + ": " + e.getMessage());
            throw e;
        }
    }

    @Override
    public Aluno atualizarAluno(Aluno aluno) throws DadoInvalidoException, EntidadeNaoEncontradaException {
        validarAluno(aluno);
        try {
            repositorioAlunos.buscarPorId(aluno.getMatricula());
            repositorioAlunos.atualizar(aluno);
            System.out.println("Aluno " + aluno.getNome() + " atualizado com sucesso.");
            return aluno;
        } catch (EntidadeNaoEncontradaException e) {
            System.err.println("Erro no AlunoServico ao tentar atualizar aluno: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public boolean excluirAluno(String matricula) throws EntidadeNaoEncontradaException {
        try {
            repositorioAlunos.buscarPorId(matricula);
            repositorioAlunos.deletar(matricula);
            System.out.println("Aluno com matrícula " + matricula + " excluído com sucesso.");
            return true;
        } catch (EntidadeNaoEncontradaException e) {
            System.err.println("Erro no AlunoServico ao tentar excluir aluno: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public List<Aluno> listarTodosAlunos() {
        return repositorioAlunos.listarTodos();
    }

    @Override
    public boolean transferirAlunoTurma(String matriculaAluno, String codigoNovaTurma) { return false; }

    @Override
    public Aluno matricularAluno(Aluno aluno, Turma turma, int anoLetivo) { return null; }

    @Override
    public boolean cancelarMatricula(String matricula) { return false; }
}