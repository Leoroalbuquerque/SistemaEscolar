package br.com.escola.negocio;

import br.com.escola.dados.NotaRepositorioJson;
import br.com.escola.excecoes.DadoInvalidoException;
import br.com.escola.excecoes.EntidadeNaoEncontradaException;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public class NotaServico implements INotaServico {

    private NotaRepositorioJson repositorioNotas;
    private AlunoServico alunoServico;
    private DisciplinaServico disciplinaServico;

    public NotaServico(NotaRepositorioJson repositorioNotas, AlunoServico alunoServico, DisciplinaServico disciplinaServico) {
        this.repositorioNotas = repositorioNotas;
        this.alunoServico = alunoServico;
        this.disciplinaServico = disciplinaServico;
    }

    private void validarNota(Nota nota) throws DadoInvalidoException {
        if (nota == null) {
            throw new DadoInvalidoException("A nota não pode ser nula.");
        }
        if (nota.getAluno() == null || nota.getAluno().getMatricula() == null || nota.getAluno().getMatricula().trim().isEmpty()) {
            throw new DadoInvalidoException("A nota deve estar associada a um aluno válido com matrícula.");
        }
        if (nota.getDisciplina() == null || nota.getDisciplina().getCodigo() == null || nota.getDisciplina().getCodigo().trim().isEmpty()) {
            throw new DadoInvalidoException("A nota deve estar associada a uma disciplina válida com código.");
        }
        if (nota.getValor() < 0 || nota.getValor() > 100) {
            throw new DadoInvalidoException("O valor da nota deve estar entre 0 e 100.");
        }
        if (nota.getTipoAvaliacao() == null || nota.getTipoAvaliacao().trim().isEmpty()) {
            throw new DadoInvalidoException("O tipo de avaliação não pode ser vazio.");
        }
        if (nota.getDataLancamento() == null) {
            throw new DadoInvalidoException("A data de lançamento da nota não pode ser nula.");
        }
    }

    @Override
    public void adicionarNota(Nota nota) throws DadoInvalidoException, EntidadeNaoEncontradaException, IOException {
        validarNota(nota);
        try {
            alunoServico.buscarAluno(nota.getAluno().getMatricula());
            disciplinaServico.buscarDisciplina(nota.getDisciplina().getCodigo());
        } catch (EntidadeNaoEncontradaException e) {
            throw new DadoInvalidoException("Aluno ou Disciplina associados à nota não encontrados: " + e.getMessage());
        }

        if (repositorioNotas.buscar(nota.getAluno().getMatricula(), nota.getDisciplina().getCodigo(), nota.getTipoAvaliacao(), nota.getDataLancamento()) != null) {
            throw new DadoInvalidoException("Já existe uma nota registrada com os mesmos critérios (aluno, disciplina, tipo e data de lançamento).");
        }
        repositorioNotas.adicionar(nota);
    }

    @Override
    public Nota buscarNota(String matriculaAluno, String codigoDisciplina, String tipoAvaliacao, Date dataLancamento) throws EntidadeNaoEncontradaException, DadoInvalidoException {
        if (matriculaAluno == null || matriculaAluno.trim().isEmpty() || codigoDisciplina == null || codigoDisciplina.trim().isEmpty() || tipoAvaliacao == null || tipoAvaliacao.trim().isEmpty() || dataLancamento == null) {
            throw new DadoInvalidoException("Todos os campos (matrícula do aluno, código da disciplina, tipo de avaliação e data de lançamento) são obrigatórios para buscar uma nota.");
        }
        Nota nota = repositorioNotas.buscar(matriculaAluno, codigoDisciplina, tipoAvaliacao, dataLancamento);
        if (nota == null) {
            throw new EntidadeNaoEncontradaException("Nota não encontrada para o aluno: " + matriculaAluno + ", disciplina: " + codigoDisciplina + ", tipo: " + tipoAvaliacao + " e data: " + dataLancamento);
        }
        return nota;
    }

    @Override
    public void atualizarNota(Nota notaAtualizada) throws DadoInvalidoException, EntidadeNaoEncontradaException {
        validarNota(notaAtualizada);
        try {
            alunoServico.buscarAluno(notaAtualizada.getAluno().getMatricula());
            disciplinaServico.buscarDisciplina(notaAtualizada.getDisciplina().getCodigo());
        } catch (EntidadeNaoEncontradaException e) {
            throw new DadoInvalidoException("Aluno ou Disciplina associados à nota atualizada não encontrados: " + e.getMessage());
        }

        Nota notaExistente = repositorioNotas.buscar(notaAtualizada.getAluno().getMatricula(), notaAtualizada.getDisciplina().getCodigo(), notaAtualizada.getTipoAvaliacao(), notaAtualizada.getDataLancamento());
        if (notaExistente == null) {
             throw new EntidadeNaoEncontradaException("Nota a ser atualizada não encontrada.");
        }
        repositorioNotas.atualizar(notaAtualizada);
    }

    @Override
    public boolean deletarNota(String matriculaAluno, String codigoDisciplina, String tipoAvaliacao, Date dataLancamento) throws EntidadeNaoEncontradaException, DadoInvalidoException {
        if (matriculaAluno == null || matriculaAluno.trim().isEmpty() || codigoDisciplina == null || codigoDisciplina.trim().isEmpty() || tipoAvaliacao == null || tipoAvaliacao.trim().isEmpty() || dataLancamento == null) {
            throw new DadoInvalidoException("Todos os campos (matrícula do aluno, código da disciplina, tipo de avaliação e data de lançamento) são obrigatórios para deletar uma nota.");
        }
        if (repositorioNotas.buscar(matriculaAluno, codigoDisciplina, tipoAvaliacao, dataLancamento) == null) {
            throw new EntidadeNaoEncontradaException("Nota não encontrada para exclusão para o aluno: " + matriculaAluno + ", disciplina: " + codigoDisciplina + ", tipo: " + tipoAvaliacao + " e data: " + dataLancamento);
        }
        return repositorioNotas.deletar(matriculaAluno, codigoDisciplina, tipoAvaliacao, dataLancamento);
    }

    @Override
    public List<Nota> listarTodasNotas() {
        return repositorioNotas.listarTodos();
    }

    @Override
    public List<Nota> buscarNotasPorAluno(String matriculaAluno) throws DadoInvalidoException {
        if (matriculaAluno == null || matriculaAluno.trim().isEmpty()) {
            throw new DadoInvalidoException("A matrícula do aluno não pode ser vazia para a busca de notas.");
        }
        return repositorioNotas.buscarNotasPorAluno(matriculaAluno);
    }

    @Override
    public List<Nota> buscarNotasPorDisciplina(String codigoDisciplina) throws DadoInvalidoException {
        if (codigoDisciplina == null || codigoDisciplina.trim().isEmpty()) {
            throw new DadoInvalidoException("O código da disciplina não pode ser vazio para a busca de notas.");
        }
        return repositorioNotas.buscarNotasPorDisciplina(codigoDisciplina);
    }

    @Override
    public List<Nota> buscarNotasPorAlunoEDisciplina(String matriculaAluno, String codigoDisciplina) throws DadoInvalidoException {
        if (matriculaAluno == null || matriculaAluno.trim().isEmpty() || codigoDisciplina == null || codigoDisciplina.trim().isEmpty()) {
            throw new DadoInvalidoException("A matrícula do aluno e o código da disciplina são obrigatórios para a busca de notas.");
        }
        return repositorioNotas.buscarNotasPorAlunoEDisciplina(matriculaAluno, codigoDisciplina);
    }
}