package br.com.escola.dados;

import br.com.escola.negocio.Aluno;
import br.com.escola.excecoes.EntidadeNaoEncontradaException;
import br.com.escola.excecoes.DadoInvalidoException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AlunoRepositorioMemoria implements IRepositorio<Aluno, String> {

    private List<Aluno> alunos = new ArrayList<>();

    @Override
    public void salvar(Aluno entidade) throws IOException, DadoInvalidoException {
        if (entidade == null || entidade.getMatricula() == null || entidade.getMatricula().trim().isEmpty()) {
            throw new DadoInvalidoException("Erro: Tentativa de salvar aluno nulo ou com matrícula vazia/nula.");
        }
        if (entidade.getNome() == null || entidade.getNome().trim().isEmpty()) {
            throw new DadoInvalidoException("Erro: Nome do aluno é obrigatório para salvar.");
        }

        try {
            buscarPorId(entidade.getMatricula());
            throw new DadoInvalidoException("Aluno com matrícula " + entidade.getMatricula() + " já existe. Use 'atualizar' para modificar.");
        } catch (EntidadeNaoEncontradaException e) {
            this.alunos.add(entidade);
        }
    }

    @Override
    public Aluno buscarPorId(String id) throws IOException, EntidadeNaoEncontradaException, DadoInvalidoException {
        if (id == null || id.trim().isEmpty()) {
            throw new DadoInvalidoException("ID para busca não pode ser nulo ou vazio.");
        }
        return this.alunos.stream()
                .filter(a -> a.getMatricula().equals(id))
                .findFirst()
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Aluno com matrícula " + id + " não encontrado."));
    }

    @Override
    public List<Aluno> listarTodos() throws IOException {
        return new ArrayList<>(this.alunos);
    }

    @Override
    public void atualizar(Aluno entidade) throws IOException, EntidadeNaoEncontradaException, DadoInvalidoException {
        if (entidade == null || entidade.getMatricula() == null || entidade.getMatricula().trim().isEmpty()) {
            throw new DadoInvalidoException("Não é possível atualizar: Aluno ou matrícula inválida.");
        }
        if (entidade.getNome() == null || entidade.getNome().trim().isEmpty()) {
            throw new DadoInvalidoException("Nome do aluno é obrigatório para atualização.");
        }

        int index = -1;
        for (int i = 0; i < alunos.size(); i++) {
            if (alunos.get(i).getMatricula().equals(entidade.getMatricula())) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            throw new EntidadeNaoEncontradaException("Aluno com matrícula " + entidade.getMatricula() + " não encontrado para atualização.");
        }

        this.alunos.set(index, entidade);
    }

    @Override
    public void deletar(String id) throws IOException, EntidadeNaoEncontradaException, DadoInvalidoException {
        if (id == null || id.trim().isEmpty()) {
            throw new DadoInvalidoException("Matrícula para deleção não pode ser nula ou vazia.");
        }
        boolean removido = this.alunos.removeIf(aluno -> aluno.getMatricula().equals(id));
        if (!removido) {
            throw new EntidadeNaoEncontradaException("Aluno com matrícula " + id + " não encontrado para exclusão.");
        }
    }

    @Override
    public void limpar() throws IOException {
        this.alunos.clear();
    }
}