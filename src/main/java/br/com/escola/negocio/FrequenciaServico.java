package br.com.escola.negocio;

import br.com.escola.dados.FrequenciaRepositorioJson;
import br.com.escola.excecoes.DadoInvalidoException;
import br.com.escola.excecoes.EntidadeNaoEncontradaException;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class FrequenciaServico implements IFrequenciaServico {

    private final FrequenciaRepositorioJson frequenciaRepositorio;
    private final AlunoServico alunoServico;
    private final DisciplinaServico disciplinaServico;

    public FrequenciaServico(FrequenciaRepositorioJson frequenciaRepositorio,
                             AlunoServico alunoServico,
                             DisciplinaServico disciplinaServico) {
        this.frequenciaRepositorio = Objects.requireNonNull(frequenciaRepositorio, "FrequenciaRepositorio nao pode ser nulo.");
        this.alunoServico = Objects.requireNonNull(alunoServico, "AlunoServico nao pode ser nulo.");
        this.disciplinaServico = Objects.requireNonNull(disciplinaServico, "DisciplinaServico nao pode ser nulo.");
    }

    @Override
    public void registrarFrequencia(String matriculaAluno, String codigoDisciplina, LocalDate data, boolean presenca)
            throws DadoInvalidoException, EntidadeNaoEncontradaException, IOException {
        if (matriculaAluno == null || matriculaAluno.isBlank() || codigoDisciplina == null || codigoDisciplina.isBlank() || data == null) {
            throw new DadoInvalidoException("Matricula do aluno, codigo da disciplina e data nao podem ser nulos ou vazios.");
        }

        Aluno aluno = alunoServico.buscarAluno(matriculaAluno);
        Disciplina disciplina = disciplinaServico.buscarDisciplina(codigoDisciplina);

        Frequencia frequenciaExistente = frequenciaRepositorio.buscar(matriculaAluno, codigoDisciplina, data);
        if (frequenciaExistente != null) {
            frequenciaExistente.setPresenca(presenca);
            frequenciaExistente.setJustificativa(null);
            frequenciaRepositorio.atualizar(frequenciaExistente);
            System.out.println("Frequencia de " + aluno.getNome() + " para " + disciplina.getNome() + " em " + data + " atualizada para: " + (presenca ? "Presente" : "Ausente"));
        } else {
            Frequencia novaFrequencia = new Frequencia(data, presenca, null, disciplina, aluno);
            frequenciaRepositorio.salvar(novaFrequencia);
            System.out.println("Frequencia de " + aluno.getNome() + " para " + disciplina.getNome() + " em " + data + " registrada como: " + (presenca ? "Presente" : "Ausente"));
        }
    }

    @Override
    public void justificarFalta(String matriculaAluno, String codigoDisciplina, LocalDate data, String justificativa)
            throws DadoInvalidoException, EntidadeNaoEncontradaException, IOException {
        if (matriculaAluno == null || matriculaAluno.isBlank() || codigoDisciplina == null || codigoDisciplina.isBlank() || data == null || justificativa == null || justificativa.isBlank()) {
            throw new DadoInvalidoException("Matricula do aluno, codigo da disciplina, data e justificativa nao podem ser nulos ou vazios.");
        }

        Frequencia frequencia = frequenciaRepositorio.buscar(matriculaAluno, codigoDisciplina, data);
        if (frequencia == null) {
            throw new EntidadeNaoEncontradaException("Frequencia para aluno " + matriculaAluno + ", disciplina " + codigoDisciplina + " na data " + data + " nao encontrada.");
        }

        if (!frequencia.isPresenca()) {
            frequencia.setJustificativa(justificativa);
            frequenciaRepositorio.atualizar(frequencia);
            System.out.println("Falta de " + frequencia.getAluno().getNome() + " em " + frequencia.getDisciplina().getNome() + " na data " + data + " justificada: " + justificativa);
        } else {
            throw new DadoInvalidoException("Nao e possivel justificar a falta, pois o aluno estava presente nesta frequencia.");
        }
    }

    @Override
    public Frequencia buscarFrequencia(String matriculaAluno, String codigoDisciplina, LocalDate data)
            throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        if (matriculaAluno == null || matriculaAluno.isBlank() || codigoDisciplina == null || codigoDisciplina.isBlank() || data == null) {
            throw new DadoInvalidoException("Matricula do aluno, codigo da disciplina e data nao podem ser nulos ou vazios.");
        }

        Frequencia frequencia = frequenciaRepositorio.buscar(matriculaAluno, codigoDisciplina, data);
        if (frequencia == null) {
            throw new EntidadeNaoEncontradaException("Frequencia para aluno " + matriculaAluno + ", disciplina " + codigoDisciplina + " na data " + data + " nao encontrada.");
        }
        return frequencia;
    }

    @Override
    public List<Frequencia> listarFrequenciasPorAluno(String matriculaAluno) throws DadoInvalidoException, IOException {
        if (matriculaAluno == null || matriculaAluno.isBlank()) {
            throw new DadoInvalidoException("Matricula do aluno nao pode ser nula ou vazia.");
        }
        return frequenciaRepositorio.buscarPorAluno(matriculaAluno);
    }

    @Override
    public List<Frequencia> listarFrequenciasPorDisciplina(String codigoDisciplina) throws DadoInvalidoException, IOException {
        if (codigoDisciplina == null || codigoDisciplina.isBlank()) {
            throw new DadoInvalidoException("Codigo da disciplina nao pode ser nulo ou vazio.");
        }
        return frequenciaRepositorio.buscarPorDisciplina(codigoDisciplina);
    }

    @Override
    public List<Frequencia> listarFrequenciasPorAlunoEDisciplina(String matriculaAluno, String codigoDisciplina) throws DadoInvalidoException, IOException {
        if (matriculaAluno == null || matriculaAluno.isBlank() || codigoDisciplina == null || codigoDisciplina.isBlank()) {
            throw new DadoInvalidoException("Matricula do aluno e codigo da disciplina nao podem ser nulos ou vazios.");
        }
        return frequenciaRepositorio.buscarPorAlunoEDisciplina(matriculaAluno, codigoDisciplina);
    }

    @Override
    public List<Frequencia> listarTodasFrequencias() throws IOException {
        return frequenciaRepositorio.listarTodos();
    }

    @Override
    public void atualizarFrequencia(Frequencia frequenciaAtualizada) throws DadoInvalidoException, EntidadeNaoEncontradaException, IOException {
        if (frequenciaAtualizada == null || frequenciaAtualizada.getAluno() == null || frequenciaAtualizada.getAluno().getMatricula() == null ||
            frequenciaAtualizada.getDisciplina() == null || frequenciaAtualizada.getDisciplina().getCodigo() == null ||
            frequenciaAtualizada.getData() == null) {
            throw new DadoInvalidoException("Dados da frequencia para atualizacao sao invalidos ou incompletos.");
        }

        Frequencia frequenciaExistente = frequenciaRepositorio.buscar(
                frequenciaAtualizada.getAluno().getMatricula(),
                frequenciaAtualizada.getDisciplina().getCodigo(),
                frequenciaAtualizada.getData()
        );

        if (frequenciaExistente == null) {
            throw new EntidadeNaoEncontradaException("Frequencia a ser atualizada nao encontrada.");
        }

        frequenciaRepositorio.atualizar(frequenciaAtualizada);
        System.out.println("Frequencia atualizada com sucesso para aluno " + frequenciaAtualizada.getAluno().getNome() +
                           ", disciplina " + frequenciaAtualizada.getDisciplina().getNome() +
                           " na data " + frequenciaAtualizada.getData());
    }

    @Override
    public boolean deletarFrequencia(String matriculaAluno, String codigoDisciplina, LocalDate data)
            throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        if (matriculaAluno == null || matriculaAluno.isBlank() || codigoDisciplina == null || codigoDisciplina.isBlank() || data == null) {
            throw new DadoInvalidoException("Matricula do aluno, codigo da disciplina e data nao podem ser nulos ou vazios.");
        }

        boolean deletado = frequenciaRepositorio.deletar(matriculaAluno, codigoDisciplina, data);
        if (!deletado) {
            throw new EntidadeNaoEncontradaException("Frequencia para aluno " + matriculaAluno + ", disciplina " + codigoDisciplina + " na data " + data + " nao encontrada para exclusao.");
        }
        System.out.println("Frequencia de " + matriculaAluno + " para " + codigoDisciplina + " em " + data + " deletada com sucesso.");
        return true;
    }
}