package br.com.escola.dados;

import br.com.escola.excecoes.DadoInvalidoException;
import br.com.escola.excecoes.EntidadeNaoEncontradaException;
import java.io.IOException;
import java.util.List;

public interface IRepositorio<T, ID> {
    void salvar(T entidade) throws IOException, DadoInvalidoException;
    T buscarPorId(ID id) throws IOException, EntidadeNaoEncontradaException, DadoInvalidoException; // DadoInvalidoException se o ID for inválido (ex: null)
    List<T> listarTodos() throws IOException;
    void atualizar(T entidade) throws IOException, EntidadeNaoEncontradaException, DadoInvalidoException;
    void deletar(ID id) throws IOException, EntidadeNaoEncontradaException, DadoInvalidoException;
    void limpar() throws IOException;
}