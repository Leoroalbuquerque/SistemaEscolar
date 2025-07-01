package br.com.escola.dados;

import java.util.List;

public interface IRepositorio<T, ID> {
    void salvar(T entidade);
    T buscarPorId(ID id);
    List<T> listarTodos();
    void atualizar(T entidade);
    void deletar(ID id);
}