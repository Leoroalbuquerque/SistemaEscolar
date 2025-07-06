package br.com.escola.negocio;

import br.com.escola.excecoes.DadoInvalidoException;
import br.com.escola.excecoes.EntidadeNaoEncontradaException;
import java.util.List;

public interface IAlunoServico {

    Aluno criarAluno(Aluno aluno) throws DadoInvalidoException;

    Aluno consultarAlunoPorMatricula(String matricula) throws EntidadeNaoEncontradaException;

    Aluno atualizarAluno(Aluno aluno) throws DadoInvalidoException, EntidadeNaoEncontradaException;

    boolean excluirAluno(String matricula) throws EntidadeNaoEncontradaException;

    List<Aluno> listarTodosAlunos();

    boolean transferirAlunoTurma(String matriculaAluno, String codigoNovaTurma);
    Aluno matricularAluno(Aluno aluno, Turma turma, int anoLetivo);
    boolean cancelarMatricula(String matricula);
}