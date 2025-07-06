package br.com.escola.dados;

import br.com.escola.negocio.Aluno;
import br.com.escola.excecoes.EntidadeNaoEncontradaException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RepositorioAlunoDAO implements IRepositorio<Aluno, String> {

    private List<Aluno> alunos = new ArrayList<>();
    private static final String NOME_ARQUIVO = "alunos.txt";

    @Override
    public void salvar(Aluno entidade) {
        this.alunos.add(entidade);
        System.out.println("Aluno " + entidade.getNome() + " salvo na lista.");
    }

    @Override
    public Aluno buscarPorId(String id) throws EntidadeNaoEncontradaException {
        System.out.println("Buscando aluno com matrícula: " + id);
        Aluno alunoEncontrado = this.alunos.stream()
            .filter(a -> a.getMatricula().equals(id))
            .findFirst()
            .orElse(null);

        if (alunoEncontrado == null) {
            throw new EntidadeNaoEncontradaException("Aluno com matrícula " + id + " não encontrado.");
        }
        return alunoEncontrado;
    }

    @Override
    public List<Aluno> listarTodos() {
        System.out.println("Listando todos os alunos.");
        return new ArrayList<>(this.alunos);
    }

    @Override
    public void atualizar(Aluno entidade) {
        System.out.println("Atualizando aluno com matrícula: " + entidade.getMatricula());
        this.alunos.removeIf(a -> a.getMatricula().equals(entidade.getMatricula()));
        this.alunos.add(entidade);
    }

    @Override
    public void deletar(String id) {
        System.out.println("Deletando aluno com matrícula: " + id);
        this.alunos.removeIf(aluno -> aluno.getMatricula().equals(id));
    }
}