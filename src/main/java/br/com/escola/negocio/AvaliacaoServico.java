package br.com.escola.negocio;

import br.com.escola.dados.IRepositorio;
import br.com.escola.excecoes.DadoInvalidoException;
import br.com.escola.excecoes.EntidadeNaoEncontradaException;
import java.io.IOException;
import java.util.List;

public class AvaliacaoServico {

    private IRepositorio<Avaliacao, String> repositorioAvaliacoes;

    public AvaliacaoServico(IRepositorio<Avaliacao, String> repositorioAvaliacoes) {
        this.repositorioAvaliacoes = repositorioAvaliacoes;
    }

    public void salvarAvaliacao(Avaliacao avaliacao) throws DadoInvalidoException, IOException {
        if (avaliacao == null || avaliacao.getId() == null || avaliacao.getId().trim().isEmpty()) {
            throw new DadoInvalidoException("Avaliação e seu ID não podem ser nulos ou vazios.");
        }
        repositorioAvaliacoes.salvar(avaliacao);
    }

    public Avaliacao buscarAvaliacaoPorId(String id) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        if (id == null || id.trim().isEmpty()) {
            throw new DadoInvalidoException("ID da avaliação não pode ser nulo ou vazio.");
        }
        return repositorioAvaliacoes.buscarPorId(id);
    }

    public List<Avaliacao> listarTodasAvaliacoes() throws IOException {
        return repositorioAvaliacoes.listarTodos();
    }

    public void atualizarAvaliacao(Avaliacao avaliacao) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        if (avaliacao == null || avaliacao.getId() == null || avaliacao.getId().trim().isEmpty()) {
            throw new DadoInvalidoException("Avaliação e seu ID não podem ser nulos ou vazios para atualização.");
        }
        repositorioAvaliacoes.atualizar(avaliacao);
    }

    public void deletarAvaliacao(String id) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        if (id == null || id.trim().isEmpty()) {
            throw new DadoInvalidoException("ID da avaliação não pode ser nulo ou vazio para exclusão.");
        }
        repositorioAvaliacoes.deletar(id);
    }

    public void limparRepositorio() throws IOException {
        repositorioAvaliacoes.limpar();
    }
}