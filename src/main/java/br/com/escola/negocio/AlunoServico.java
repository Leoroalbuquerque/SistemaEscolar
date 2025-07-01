package br.com.escola.negocio;

import br.com.escola.dados.IRepositorio;
import br.com.escola.dados.RepositorioAlunoDAO;
import java.util.List;

public class AlunoServico {
    private IRepositorio<Aluno, String> repositorioAlunos;

    public AlunoServico() {
        this.repositorioAlunos = new RepositorioAlunoDAO();
    }

    public Aluno criarAluno(Aluno aluno) {
        repositorioAlunos.salvar(aluno);
        return aluno;
    }

    public Aluno consultarAlunoPorMatricula(String matricula) {
        return repositorioAlunos.buscarPorId(matricula);
    }

    public Aluno atualizarAluno(Aluno aluno) {
        repositorioAlunos.atualizar(aluno);
        return aluno;
    }

    public boolean excluirAluno(String matricula) {
        repositorioAlunos.deletar(matricula);
        return true;
    }

    public List<Aluno> listarTodosAlunos() {
        return repositorioAlunos.listarTodos();
    }

    public boolean transferirAlunoTurma(String matriculaAluno, String codigoNovaTurma) { return false; }
    public Aluno matricularAluno(Aluno aluno, Turma turma, int anoLetivo) { return null; }
    public boolean cancelarMatricula(String matricula) { return false; }
}