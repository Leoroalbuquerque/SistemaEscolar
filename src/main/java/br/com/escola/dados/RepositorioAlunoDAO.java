package br.com.escola.dados;

import br.com.escola.negocio.Aluno;
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
    public Aluno buscarPorId(String id) {
        System.out.println("Buscando aluno com matrícula: " + id);
        return this.alunos.stream()
            .filter(a -> a.getMatricula().equals(id))
            .findFirst()
            .orElse(null);
    }

    @Override
    public List<Aluno> listarTodos() {
        System.out.println("Listando todos os alunos.");
        return new ArrayList<>(this.alunos);
    }

    @Override
    public void atualizar(Aluno entidade) {
        System.out.println("Atualizando aluno com matrícula: " + entidade.getMatricula());
    }

@Override
public void deletar(String id) {
    System.out.println("Deletando aluno com matrícula: " + id);
    this.alunos.removeIf(aluno -> aluno.getMatricula().equals(id));
    }
}