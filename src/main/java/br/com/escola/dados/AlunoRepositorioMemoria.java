package br.com.escola.dados;

import br.com.escola.negocio.Aluno;
import br.com.escola.excecoes.EntidadeNaoEncontradaException;
import br.com.escola.excecoes.DadoInvalidoException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AlunoRepositorioMemoria implements IRepositorio<Aluno, String> {

    private List<Aluno> alunos = new ArrayList<>();

    @Override
    public void adicionar(Aluno entidade) throws DadoInvalidoException {
        if (entidade == null || entidade.getMatricula() == null || entidade.getMatricula().trim().isEmpty()) {
            throw new DadoInvalidoException("Erro: Tentativa de adicionar aluno nulo ou com matrícula vazia/nula.");
        }
        if (entidade.getNome() == null || entidade.getNome().trim().isEmpty()) {
            throw new DadoInvalidoException("Erro: Nome do aluno é obrigatório para adição.");
        }

        boolean existe = this.alunos.stream()
                .anyMatch(a -> a != null && a.getMatricula() != null && a.getMatricula().equals(entidade.getMatricula()));
        if (existe) {
            throw new DadoInvalidoException("Aluno com matrícula " + entidade.getMatricula() + " já existe na memória. Não foi salvo novamente.");
        }

        this.alunos.add(entidade);
        System.out.println("Aluno " + entidade.getNome() + " salvo na lista.");
    }

    @Override
    public Optional<Aluno> buscarPorId(String id) {
        System.out.println("Buscando aluno com matrícula: " + id);
        if (id == null || id.trim().isEmpty()) {
            return Optional.empty();
        }
        return this.alunos.stream()
                .filter(a -> a.getMatricula().equals(id))
                .findFirst();
    }

    @Override
    public List<Aluno> listarTodos() {
        System.out.println("Listando todos os alunos.");
        return new ArrayList<>(this.alunos);
    }

    @Override
    public void atualizar(Aluno entidade) throws EntidadeNaoEncontradaException, DadoInvalidoException {
        if (entidade == null || entidade.getMatricula() == null || entidade.getMatricula().trim().isEmpty()) {
            throw new DadoInvalidoException("Não é possível atualizar: Aluno ou matrícula inválida.");
        }
        if (entidade.getNome() == null || entidade.getNome().trim().isEmpty()) {
            throw new DadoInvalidoException("Nome do aluno é obrigatório para atualização.");
        }

        System.out.println("Atualizando aluno com matrícula: " + entidade.getMatricula());
        Optional<Aluno> alunoExistenteOpt = buscarPorId(entidade.getMatricula());
        if (alunoExistenteOpt.isEmpty()) {
            throw new EntidadeNaoEncontradaException("Aluno com matrícula " + entidade.getMatricula() + " não encontrado para atualização.");
        }

        this.alunos.removeIf(a -> a.getMatricula().equals(entidade.getMatricula()));
        this.alunos.add(entidade);
    }

    @Override
    public boolean deletar(String id) throws EntidadeNaoEncontradaException, DadoInvalidoException {
        if (id == null || id.trim().isEmpty()) {
            throw new DadoInvalidoException("Matrícula para deleção não pode ser nula ou vazia.");
        }
        System.out.println("Deletando aluno com matrícula: " + id);
        boolean removido = this.alunos.removeIf(aluno -> aluno.getMatricula().equals(id));
        if (removido) {
            return true;
        } else {
            throw new EntidadeNaoEncontradaException("Aluno com matrícula " + id + " não encontrado para exclusão.");
        }
    }
}
