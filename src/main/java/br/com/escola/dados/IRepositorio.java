package br.com.escola.dados;

import java.util.List;
import br.com.escola.excecoes.EntidadeNaoEncontradaException;

public interface IRepositorio<T, ID> {
    void salvar(T entidade);
    T buscarPorId(ID id) throws EntidadeNaoEncontradaException;
    List<T> listarTodos();
    void atualizar(T entidade);
    void deletar(ID id);
}