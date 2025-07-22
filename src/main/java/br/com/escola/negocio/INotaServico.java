package br.com.escola.negocio;

import br.com.escola.excecoes.DadoInvalidoException;
import br.com.escola.excecoes.EntidadeNaoEncontradaException;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public interface INotaServico {
    void adicionarNota(Nota nota) throws DadoInvalidoException, EntidadeNaoEncontradaException, IOException;
    Nota buscarNota(String matriculaAluno, String codigoDisciplina, String tipoAvaliacao, Date dataLancamento) throws EntidadeNaoEncontradaException, DadoInvalidoException;
    void atualizarNota(Nota nota) throws DadoInvalidoException, EntidadeNaoEncontradaException;
    boolean deletarNota(String matriculaAluno, String codigoDisciplina, String tipoAvaliacao, Date dataLancamento) throws EntidadeNaoEncontradaException, DadoInvalidoException;
    List<Nota> listarTodasNotas();
    List<Nota> buscarNotasPorAluno(String matriculaAluno) throws DadoInvalidoException;
    List<Nota> buscarNotasPorDisciplina(String codigoDisciplina) throws DadoInvalidoException;
    List<Nota> buscarNotasPorAlunoEDisciplina(String matriculaAluno, String codigoDisciplina) throws DadoInvalidoException;
}