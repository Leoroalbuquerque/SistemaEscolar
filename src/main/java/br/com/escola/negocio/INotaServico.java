package br.com.escola.negocio;

import br.com.escola.excecoes.DadoInvalidoException;
import br.com.escola.excecoes.EntidadeNaoEncontradaException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface INotaServico {
    void adicionarNota(Nota nota) throws DadoInvalidoException, EntidadeNaoEncontradaException, IOException;
    Nota buscarNota(String matriculaAluno, String codigoDisciplina, String tipoAvaliacao, LocalDate dataLancamento) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException;
    void atualizarNota(Nota nota) throws DadoInvalidoException, EntidadeNaoEncontradaException, IOException;
    boolean deletarNota(String matriculaAluno, String codigoDisciplina, String tipoAvaliacao, LocalDate dataLancamento) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException;
    List<Nota> listarTodasNotas() throws IOException;
    List<Nota> buscarNotasPorAluno(String matriculaAluno) throws DadoInvalidoException, IOException;
    List<Nota> buscarNotasPorDisciplina(String codigoDisciplina) throws DadoInvalidoException, IOException;
    List<Nota> buscarNotasPorAlunoEDisciplina(String matriculaAluno, String codigoDisciplina) throws DadoInvalidoException, IOException;
}