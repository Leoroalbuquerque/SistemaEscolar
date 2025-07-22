package br.com.escola.dados;

import br.com.escola.excecoes.DadoInvalidoException;
import br.com.escola.excecoes.EntidadeNaoEncontradaException;
import java.util.List;
import java.util.Optional;

public interface IRepositorio<T, ID> {
    void salvar(T entidade) throws DadoInvalidoException;
    Optional<T> buscarPorId(ID id);
    void atualizar(T entidade) throws EntidadeNaoEncontradaException, DadoInvalidoException;
    boolean deletar(ID id) throws EntidadeNaoEncontradaException, DadoInvalidoException;
    List<T> listarTodos();
    void limpar();
}