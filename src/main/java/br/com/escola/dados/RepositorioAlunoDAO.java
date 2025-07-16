package br.com.escola.dados;

import br.com.escola.negocio.Aluno;
import br.com.escola.excecoes.EntidadeNaoEncontradaException;
import br.com.escola.excecoes.DadoInvalidoException;
import java.util.List;
import java.util.Optional; // Adicionado: Importação para Optional

// Assumindo que esta classe também implementa IRepositorio
public class RepositorioAlunoDAO implements IRepositorio<Aluno, String> {

    // Exemplo de implementação mínima para compilar e seguir a interface
    @Override
    public void adicionar(Aluno entidade) throws DadoInvalidoException {
        // Implemente a lógica de adição aqui
        System.out.println("Adicionando aluno (DAO genérico): " + entidade.getNome());
        // Lançaria uma exceção real ou adicionaria à base de dados
        // Para este exemplo, não há base de dados real
    }

    @Override // CORRIGIDO: Nome do método para 'buscarPorId' e retorno para 'Optional<Aluno>'
    public Optional<Aluno> buscarPorId(String chave) { // Não lança exceções diretamente
        // Implemente a lógica de busca aqui
        System.out.println("Buscando aluno (DAO genérico): " + chave);
        // Retorna Optional.empty() se não encontrar, em vez de lançar EntidadeNaoEncontradaException
        // Se a chave for inválida (nula/vazia), também retorna Optional.empty()
        if (chave == null || chave.trim().isEmpty()) {
            return Optional.empty();
        }
        
        // Exemplo de retorno (em uma implementação real, viria do DAO)
        // return Optional.ofNullable(seuDAO.findById(chave));
        return Optional.empty(); // Substitua pela lógica real do seu DAO
    }

    @Override
    public void atualizar(Aluno entidade) throws DadoInvalidoException, EntidadeNaoEncontradaException {
        // Implemente a lógica de atualização aqui
        System.out.println("Atualizando aluno (DAO genérico): " + entidade.getNome());
        // Lançaria EntidadeNaoEncontradaException se o aluno não existisse
        // Para este exemplo, apenas simula
        // throw new EntidadeNaoEncontradaException("Aluno não encontrado para atualização no DAO genérico.");
    }

    @Override
    public boolean deletar(String chave) throws EntidadeNaoEncontradaException, DadoInvalidoException {
        // Implemente a lógica de deleção aqui
        System.out.println("Deletando aluno (DAO genérico): " + chave);
        // Lançaria EntidadeNaoEncontradaException se o aluno não existisse
        // Para este exemplo, apenas simula
        // return true; // Se a deleção foi bem-sucedida
        // throw new EntidadeNaoEncontradaException("Aluno não encontrado para deleção no DAO genérico.");
        return false; // Ou true, dependendo da lógica real da deleção
    }

    @Override
    public List<Aluno> listarTodos() {
        // Implemente a lógica de listagem aqui
        System.out.println("Listando todos os alunos (DAO genérico)");
        return new java.util.ArrayList<>(); // Retorna uma lista vazia para este exemplo
    }
}