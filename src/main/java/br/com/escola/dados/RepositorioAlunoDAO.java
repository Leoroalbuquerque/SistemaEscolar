package br.com.escola.dados;

import br.com.escola.negocio.Aluno;
import br.com.escola.excecoes.EntidadeNaoEncontradaException;
import br.com.escola.excecoes.DadoInvalidoException;
import java.util.List;
import java.util.Optional;

public class RepositorioAlunoDAO implements IRepositorio<Aluno, String> {

    @Override
    public void adicionar(Aluno entidade) throws DadoInvalidoException {
        System.out.println("Adicionando aluno (DAO genérico): " + entidade.getNome());
    }

    @Override
    public Optional<Aluno> buscarPorId(String chave) {
        System.out.println("Buscando aluno (DAO genérico): " + chave);
        if (chave == null || chave.trim().isEmpty()) {
            return Optional.empty();
        }
        return Optional.empty();
    }

    @Override
    public void atualizar(Aluno entidade) throws DadoInvalidoException, EntidadeNaoEncontradaException {
        System.out.println("Atualizando aluno (DAO genérico): " + entidade.getNome());
    }

    @Override
    public boolean deletar(String chave) throws EntidadeNaoEncontradaException, DadoInvalidoException {
        System.out.println("Deletando aluno (DAO genérico): " + chave);
        return false;
    }

    @Override
    public List<Aluno> listarTodos() {
        System.out.println("Listando todos os alunos (DAO genérico)");
        return new java.util.ArrayList<>();
    }
}
