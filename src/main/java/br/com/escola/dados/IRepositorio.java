package br.com.escola.dados;

import br.com.escola.excecoes.DadoInvalidoException;
import br.com.escola.excecoes.EntidadeNaoEncontradaException;
import java.util.List;
import java.util.Optional; // Importante para o retorno de buscarPorId

public interface IRepositorio<T, ID> {
    void adicionar(T entidade) throws DadoInvalidoException;
    Optional<T> buscarPorId(ID id); // MÉTODO CRUCIAL: Retorna Optional<T>
    void atualizar(T entidade) throws EntidadeNaoEncontradaException, DadoInvalidoException;
    boolean deletar(ID id) throws EntidadeNaoEncontradaException, DadoInvalidoException;
    List<T> listarTodos();
}