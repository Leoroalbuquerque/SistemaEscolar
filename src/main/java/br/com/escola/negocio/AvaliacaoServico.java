package br.com.escola.negocio;

import br.com.escola.dados.IRepositorio;
import br.com.escola.excecoes.DadoInvalidoException;
import br.com.escola.excecoes.EntidadeNaoEncontradaException;
import java.util.List;
import java.util.Optional;

public class AvaliacaoServico {

    private IRepositorio<Avaliacao, String> repositorioAvaliacoes;

    public AvaliacaoServico(IRepositorio<Avaliacao, String> repositorioAvaliacoes) {
        this.repositorioAvaliacoes = repositorioAvaliacoes;
    }

    public void salvarAvaliacao(Avaliacao avaliacao) throws DadoInvalidoException {
        if (avaliacao == null || avaliacao.getId() == null || avaliacao.getId().isEmpty()) {
            throw new DadoInvalidoException("Avaliação e seu ID não podem ser nulos ou vazios.");
        }
        repositorioAvaliacoes.salvar(avaliacao);
    }

    public Avaliacao buscarAvaliacaoPorId(String id) throws EntidadeNaoEncontradaException {
        Optional<Avaliacao> avaliacaoOpt = repositorioAvaliacoes.buscarPorId(id);
        if (avaliacaoOpt.isEmpty()) {
            throw new EntidadeNaoEncontradaException("Avaliação com ID " + id + " não encontrada.");
        }
        return avaliacaoOpt.get();
    }

    public List<Avaliacao> listarTodasAvaliacoes() {
        return repositorioAvaliacoes.listarTodos();
    }

    public void atualizarAvaliacao(Avaliacao avaliacao) throws EntidadeNaoEncontradaException, DadoInvalidoException {
        if (avaliacao == null || avaliacao.getId() == null || avaliacao.getId().isEmpty()) {
            throw new DadoInvalidoException("Avaliação e seu ID não podem ser nulos ou vazios para atualização.");
        }
        repositorioAvaliacoes.atualizar(avaliacao);
    }

    public boolean deletarAvaliacao(String id) throws EntidadeNaoEncontradaException, DadoInvalidoException {
        if (id == null || id.isEmpty()) {
            throw new DadoInvalidoException("ID da avaliação não pode ser nulo ou vazio para exclusão.");
        }
        return repositorioAvaliacoes.deletar(id);
    }

    public void limparRepositorio() {
        repositorioAvaliacoes.limpar();
    }
}