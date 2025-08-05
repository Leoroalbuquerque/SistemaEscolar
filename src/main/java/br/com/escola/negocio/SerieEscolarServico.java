package br.com.escola.negocio;

import br.com.escola.dados.IRepositorio;
import br.com.escola.excecoes.DadoInvalidoException;
import br.com.escola.excecoes.EntidadeNaoEncontradaException;

import java.io.IOException;
import java.util.List;

public class SerieEscolarServico {
    private final IRepositorio<SerieEscolar, String> repositorio;

    public SerieEscolarServico(IRepositorio<SerieEscolar, String> repositorio) {
        this.repositorio = repositorio;
    }

    public void adicionarSerieEscolar(SerieEscolar serie) throws DadoInvalidoException, IOException {
        if (serie == null || serie.getCodigoSerie() == null || serie.getCodigoSerie().trim().isEmpty()) {
            throw new DadoInvalidoException("Código da série escolar é obrigatório.");
        }
        if (serie.getNomeSerie() == null || serie.getNomeSerie().trim().isEmpty()) {
            throw new DadoInvalidoException("Nome da série escolar é obrigatório.");
        }
        if (serie.getNivel() == null) {
            throw new DadoInvalidoException("Nível escolar é obrigatório.");
        }
        if (serie.getOrdem() <= 0) {
            throw new DadoInvalidoException("Ordem da série escolar deve ser um valor positivo.");
        }

        try {
            repositorio.buscarPorId(serie.getCodigoSerie());
            throw new DadoInvalidoException("Série escolar com código " + serie.getCodigoSerie() + " já existe.");
        } catch (EntidadeNaoEncontradaException e) {
            repositorio.salvar(serie);
        }
    }

    public SerieEscolar buscarSerieEscolar(String codigo) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new DadoInvalidoException("Código da série escolar para busca é obrigatório.");
        }
        return repositorio.buscarPorId(codigo);
    }

    public List<SerieEscolar> listarTodasSeriesEscolares() throws IOException {
        return repositorio.listarTodos();
    }

    public void atualizarSerieEscolar(SerieEscolar serieAtualizada) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        if (serieAtualizada == null || serieAtualizada.getCodigoSerie() == null || serieAtualizada.getCodigoSerie().trim().isEmpty()) {
            throw new DadoInvalidoException("Código da série escolar para atualização é obrigatório.");
        }
        if (serieAtualizada.getNomeSerie() == null || serieAtualizada.getNomeSerie().trim().isEmpty()) {
            throw new DadoInvalidoException("Nome da série escolar é obrigatório na atualização.");
        }
        if (serieAtualizada.getNivel() == null) {
            throw new DadoInvalidoException("Nível escolar é obrigatório na atualização.");
        }
        if (serieAtualizada.getOrdem() <= 0) {
            throw new DadoInvalidoException("Ordem da série escolar deve ser um valor positivo na atualização.");
        }
        
        repositorio.buscarPorId(serieAtualizada.getCodigoSerie());
        repositorio.atualizar(serieAtualizada);
    }

    public void deletarSerieEscolar(String codigo) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new DadoInvalidoException("Código da série escolar para exclusão é obrigatório.");
        }
        repositorio.buscarPorId(codigo);
        repositorio.deletar(codigo);
    }
}