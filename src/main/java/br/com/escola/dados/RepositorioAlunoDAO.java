package br.com.escola.dados;

import br.com.escola.negocio.Aluno;
import br.com.escola.excecoes.EntidadeNaoEncontradaException;
import br.com.escola.excecoes.DadoInvalidoException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RepositorioAlunoDAO implements IRepositorio<Aluno, String> {

    private List<Aluno> alunosEmMemoria;

    public RepositorioAlunoDAO() {
        this.alunosEmMemoria = new ArrayList<>();
    }

    @Override
    public void salvar(Aluno entidade) throws DadoInvalidoException, IOException {
        if (entidade == null || entidade.getMatricula() == null || entidade.getMatricula().trim().isEmpty()) {
            throw new DadoInvalidoException("Aluno ou matrícula não pode ser nulo/vazio para salvar.");
        }
        try {
            buscarPorId(entidade.getMatricula());
            throw new DadoInvalidoException("Aluno com matrícula " + entidade.getMatricula() + " já existe.");
        } catch (EntidadeNaoEncontradaException e) {
            this.alunosEmMemoria.add(entidade);
        }
    }

    @Override
    public Aluno buscarPorId(String id) throws IOException, EntidadeNaoEncontradaException, DadoInvalidoException {
        if (id == null || id.trim().isEmpty()) {
            throw new DadoInvalidoException("Matrícula para busca não pode ser nula ou vazia.");
        }
        return this.alunosEmMemoria.stream()
                .filter(a -> Objects.equals(a.getMatricula(), id))
                .findFirst()
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Aluno com matrícula " + id + " não encontrado."));
    }

    @Override
    public void atualizar(Aluno entidade) throws DadoInvalidoException, EntidadeNaoEncontradaException, IOException {
        if (entidade == null || entidade.getMatricula() == null || entidade.getMatricula().trim().isEmpty()) {
            throw new DadoInvalidoException("Aluno ou matrícula não pode ser nulo/vazio para atualizar.");
        }
        
        boolean encontrada = false;
        for (int i = 0; i < alunosEmMemoria.size(); i++) {
            if (Objects.equals(alunosEmMemoria.get(i).getMatricula(), entidade.getMatricula())) {
                alunosEmMemoria.set(i, entidade);
                encontrada = true;
                break;
            }
        }

        if (!encontrada) {
            throw new EntidadeNaoEncontradaException("Aluno com matrícula " + entidade.getMatricula() + " não encontrado para atualização.");
        }
    }

    @Override
    public void deletar(String id) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        if (id == null || id.trim().isEmpty()) {
            throw new DadoInvalidoException("Matrícula não pode ser nula/vazia para deletar.");
        }
        boolean removido = this.alunosEmMemoria.removeIf(a -> Objects.equals(a.getMatricula(), id));
        if (!removido) {
            throw new EntidadeNaoEncontradaException("Aluno com matrícula " + id + " não encontrado para exclusão.");
        }
    }

    @Override
    public List<Aluno> listarTodos() throws IOException {
        return new ArrayList<>(this.alunosEmMemoria);
    }

    @Override
    public void limpar() throws IOException {
        this.alunosEmMemoria.clear();
    }
}