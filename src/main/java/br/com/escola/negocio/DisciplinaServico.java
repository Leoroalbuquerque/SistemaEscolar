package br.com.escola.negocio;

import br.com.escola.dados.IRepositorio;
import br.com.escola.excecoes.DadoInvalidoException;
import br.com.escola.excecoes.EntidadeNaoEncontradaException;

import java.util.List;
import java.util.Optional;

public class DisciplinaServico {

    private final IRepositorio<Disciplina, String> disciplinaRepositorio;

    public DisciplinaServico(IRepositorio<Disciplina, String> disciplinaRepositorio) {
        this.disciplinaRepositorio = disciplinaRepositorio;
    }

    public void adicionarDisciplina(Disciplina disciplina) throws DadoInvalidoException {
        if (disciplina == null) {
            throw new DadoInvalidoException("Disciplina não pode ser nula.");
        }
        if (disciplina.getCodigo() == null || disciplina.getCodigo().trim().isEmpty()) {
            throw new DadoInvalidoException("Código da disciplina é obrigatório.");
        }
        if (disciplina.getNome() == null || disciplina.getNome().trim().isEmpty()) {
            throw new DadoInvalidoException("Nome da disciplina é obrigatório.");
        }
        if (disciplinaRepositorio.buscarPorId(disciplina.getCodigo()).isPresent()) {
            throw new DadoInvalidoException("Já existe uma disciplina com o código: " + disciplina.getCodigo());
        }
        disciplinaRepositorio.adicionar(disciplina);
    }

    public Disciplina buscarDisciplina(String codigo) throws EntidadeNaoEncontradaException, DadoInvalidoException {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new DadoInvalidoException("Código para busca da disciplina não pode ser nulo ou vazio.");
        }
        return disciplinaRepositorio.buscarPorId(codigo)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Disciplina com código " + codigo + " não encontrada."));
    }

    public void atualizarDisciplina(Disciplina disciplina) throws DadoInvalidoException, EntidadeNaoEncontradaException {
        if (disciplina == null) {
            throw new DadoInvalidoException("Disciplina não pode ser nula para atualização.");
        }
        if (disciplina.getCodigo() == null || disciplina.getCodigo().trim().isEmpty()) {
            throw new DadoInvalidoException("Código da disciplina é obrigatório para atualização.");
        }
        if (disciplina.getNome() == null || disciplina.getNome().trim().isEmpty()) {
            throw new DadoInvalidoException("Nome da disciplina é obrigatório para atualização.");
        }
        if (disciplinaRepositorio.buscarPorId(disciplina.getCodigo()).isEmpty()) {
            throw new EntidadeNaoEncontradaException("Disciplina com código " + disciplina.getCodigo() + " não encontrada para atualização.");
        }
        disciplinaRepositorio.atualizar(disciplina);
    }

    public boolean deletarDisciplina(String codigo) throws EntidadeNaoEncontradaException, DadoInvalidoException {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new DadoInvalidoException("Código para deleção da disciplina não pode ser nulo ou vazio.");
        }
        return disciplinaRepositorio.deletar(codigo);
    }

    public List<Disciplina> listarTodasDisciplinas() {
        return disciplinaRepositorio.listarTodos();
    }
}
