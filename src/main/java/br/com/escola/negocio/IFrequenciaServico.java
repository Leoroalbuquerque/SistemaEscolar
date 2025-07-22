package br.com.escola.negocio;

import br.com.escola.excecoes.DadoInvalidoException;
import br.com.escola.excecoes.EntidadeNaoEncontradaException;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface IFrequenciaServico {

    void registrarFrequencia(String matriculaAluno, String codigoDisciplina, LocalDate data, boolean presenca)
            throws DadoInvalidoException, EntidadeNaoEncontradaException, IOException;

    void justificarFalta(String matriculaAluno, String codigoDisciplina, LocalDate data, String justificativa)
            throws DadoInvalidoException, EntidadeNaoEncontradaException, IOException;

    Frequencia buscarFrequencia(String matriculaAluno, String codigoDisciplina, LocalDate data)
            throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException;

    List<Frequencia> listarFrequenciasPorAluno(String matriculaAluno) throws DadoInvalidoException, IOException;

    List<Frequencia> listarFrequenciasPorDisciplina(String codigoDisciplina) throws DadoInvalidoException, IOException;

    List<Frequencia> listarFrequenciasPorAlunoEDisciplina(String matriculaAluno, String codigoDisciplina)
            throws DadoInvalidoException, IOException;

    List<Frequencia> listarTodasFrequencias() throws IOException;

    void atualizarFrequencia(Frequencia frequenciaAtualizada)
            throws DadoInvalidoException, EntidadeNaoEncontradaException, IOException;

    boolean deletarFrequencia(String matriculaAluno, String codigoDisciplina, LocalDate data)
            throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException;
}