package br.com.escola.negocio;

import br.com.escola.dados.IRepositorio;
import br.com.escola.excecoes.DadoInvalidoException;
import br.com.escola.excecoes.EntidadeNaoEncontradaException;
import java.io.IOException;
import java.util.List;

public class DisciplinaServico {
    private final IRepositorio<Disciplina, String> disciplinaRepositorio;

    public DisciplinaServico(IRepositorio<Disciplina, String> disciplinaRepositorio) {
        this.disciplinaRepositorio = disciplinaRepositorio;
    }

    public void adicionarDisciplina(Disciplina disciplina) throws DadoInvalidoException, EntidadeNaoEncontradaException, IOException {
        validarDisciplina(disciplina);
        
        try {
            disciplinaRepositorio.buscarPorId(disciplina.getCodigo());
            throw new DadoInvalidoException("Já existe uma disciplina com o código: " + disciplina.getCodigo());
        } catch (EntidadeNaoEncontradaException e) {
            disciplinaRepositorio.salvar(disciplina);
        }
    }

    public Disciplina buscarDisciplina(String codigo) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new DadoInvalidoException("Código para busca da disciplina não pode ser nulo ou vazio.");
        }
        return disciplinaRepositorio.buscarPorId(codigo);
    }

    public void atualizarDisciplina(Disciplina disciplina) throws DadoInvalidoException, EntidadeNaoEncontradaException, IOException {
        validarDisciplina(disciplina);
        disciplinaRepositorio.buscarPorId(disciplina.getCodigo());
        disciplinaRepositorio.atualizar(disciplina);
    }

    public void deletarDisciplina(String codigo) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new DadoInvalidoException("Código para deleção da disciplina não pode ser nulo ou vazio.");
        }
        disciplinaRepositorio.buscarPorId(codigo);
        disciplinaRepositorio.deletar(codigo);
    }

    public List<Disciplina> listarTodasDisciplinas() throws IOException {
        return disciplinaRepositorio.listarTodos();
    }

    private void validarDisciplina(Disciplina disciplina) throws DadoInvalidoException {
        if (disciplina == null) {
            throw new DadoInvalidoException("Disciplina não pode ser nula.");
        }
        if (disciplina.getCodigo() == null || disciplina.getCodigo().trim().isEmpty()) {
            throw new DadoInvalidoException("Código da disciplina é obrigatório.");
        }
        if (disciplina.getNome() == null || disciplina.getNome().trim().isEmpty()) {
            throw new DadoInvalidoException("Nome da disciplina é obrigatório.");
        }
    }
}