package br.com.escola.dados;

import br.com.escola.negocio.Aluno;
import br.com.escola.excecoes.EntidadeNaoEncontradaException;
import br.com.escola.excecoes.DadoInvalidoException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RepositorioAlunoDAO implements IRepositorio<Aluno, String> {

    private List<Aluno> alunosEmMemoria;

    public RepositorioAlunoDAO() {
        this.alunosEmMemoria = new ArrayList<>();
    }

    @Override
    public void salvar(Aluno entidade) throws DadoInvalidoException {
        if (entidade == null || entidade.getMatricula() == null || entidade.getMatricula().trim().isEmpty()) {
            throw new DadoInvalidoException("Aluno ou matrícula não pode ser nulo/vazio para salvar (DAO de teste).");
        }
        if (buscarPorId(entidade.getMatricula()).isPresent()) {
            throw new DadoInvalidoException("Aluno com matrícula " + entidade.getMatricula() + " já existe (DAO de teste).");
        }
        this.alunosEmMemoria.add(entidade);
        System.out.println("DEBUG: Aluno salvo em memória (DAO de teste): " + entidade.getNome() + " - " + entidade.getMatricula());
    }

    @Override
    public Optional<Aluno> buscarPorId(String id) {
        if (id == null || id.trim().isEmpty()) {
            return Optional.empty();
        }
        Optional<Aluno> encontrado = this.alunosEmMemoria.stream()
                                         .filter(a -> a.getMatricula() != null && a.getMatricula().equals(id))
                                         .findFirst();
        System.out.println("DEBUG: Buscando aluno em memória (DAO de teste): " + id + " -> " + (encontrado.isPresent() ? "Encontrado" : "Não encontrado"));
        return encontrado;
    }

    @Override
    public void atualizar(Aluno entidade) throws DadoInvalidoException, EntidadeNaoEncontradaException {
        if (entidade == null || entidade.getMatricula() == null || entidade.getMatricula().trim().isEmpty()) {
            throw new DadoInvalidoException("Aluno ou matrícula não pode ser nulo/vazio para atualizar (DAO de teste).");
        }
        Optional<Aluno> existenteOpt = buscarPorId(entidade.getMatricula());
        if (existenteOpt.isEmpty()) {
            throw new EntidadeNaoEncontradaException("Aluno com matrícula " + entidade.getMatricula() + " não encontrado para atualização (DAO de teste).");
        }

        this.alunosEmMemoria.removeIf(a -> a.getMatricula() != null && a.getMatricula().equals(entidade.getMatricula()));
        this.alunosEmMemoria.add(entidade);
        System.out.println("DEBUG: Aluno atualizado em memória (DAO de teste): " + entidade.getNome() + " - " + entidade.getMatricula());
    }

    @Override
    public boolean deletar(String id) throws EntidadeNaoEncontradaException, DadoInvalidoException {
        if (id == null || id.trim().isEmpty()) {
            throw new DadoInvalidoException("Matrícula não pode ser nula/vazia para deletar (DAO de teste).");
        }
        boolean removido = this.alunosEmMemoria.removeIf(a -> a.getMatricula() != null && a.getMatricula().equals(id));
        if (!removido) {
            throw new EntidadeNaoEncontradaException("Aluno com matrícula " + id + " não encontrado para deleção (DAO de teste).");
        }
        System.out.println("DEBUG: Aluno deletado em memória (DAO de teste): " + id + " -> " + removido);
        return removido;
    }

    @Override
    public List<Aluno> listarTodos() {
        System.out.println("DEBUG: Listando todos os alunos em memória (DAO de teste). Total: " + this.alunosEmMemoria.size());
        return new ArrayList<>(this.alunosEmMemoria);
    }

    @Override
    public void limpar() {
        this.alunosEmMemoria.clear();
        System.out.println("DEBUG: Repositório de alunos em memória limpo (DAO de teste).");
    }
}