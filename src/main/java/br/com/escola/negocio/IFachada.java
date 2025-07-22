package br.com.escola.negocio;

import br.com.escola.excecoes.DadoInvalidoException;
import br.com.escola.excecoes.EntidadeNaoEncontradaException;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface IFachada {

    Usuario autenticarUsuario(String nomeUsuario, String senha);
    boolean checarPermissao(Usuario usuario, String funcionalidade);

    void adicionarAluno(Aluno aluno) throws DadoInvalidoException, EntidadeNaoEncontradaException, IOException;
    Aluno buscarAluno(String matricula) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException;
    List<Aluno> listarTodosAlunos() throws IOException;
    void atualizarAluno(Aluno aluno) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException;
    boolean deletarAluno(String matricula) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException;
    void adicionarResponsavelAoAluno(String matriculaAluno, String cpfResponsavel) throws DadoInvalidoException, EntidadeNaoEncontradaException, IOException;
    void removerResponsavelDoAluno(String matriculaAluno, String cpfResponsavel) throws DadoInvalidoException, EntidadeNaoEncontradaException, IOException;
    List<Aluno> buscarAlunosPorAnoLetivo(int anoLetivo) throws DadoInvalidoException, IOException;

    void adicionarProfessor(Professor professor) throws DadoInvalidoException, IOException;
    Professor buscarProfessor(String registroFuncional) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException;
    List<Professor> listarTodosProfessores() throws IOException;
    void atualizarProfessor(Professor professor) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException;
    boolean deletarProfessor(String registroFuncional) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException;

    void adicionarDisciplina(Disciplina disciplina) throws DadoInvalidoException, IOException;
    Disciplina buscarDisciplina(String codigo) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException;
    List<Disciplina> listarTodasDisciplinas() throws IOException;
    void atualizarDisciplina(Disciplina disciplina) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException;
    boolean deletarDisciplina(String codigo) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException;

    void adicionarTurma(Turma turma) throws DadoInvalidoException, EntidadeNaoEncontradaException, IOException;
    Turma buscarTurma(String codigo) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException;
    List<Turma> listarTodasTurmas() throws IOException;
    void atualizarTurma(Turma turma) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException;
    boolean deletarTurma(String codigo) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException;
    void matricularAlunoNaTurma(String codigoTurma, String matriculaAluno) throws DadoInvalidoException, EntidadeNaoEncontradaException, IOException;
    void desmatricularAlunoDaTurma(String codigoTurma, String matriculaAluno) throws DadoInvalidoException, EntidadeNaoEncontradaException, IOException;

    void adicionarDisciplinaNaTurma(String codigoTurma, String codigoDisciplina) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException;
    void removerDisciplinaDaTurma(String codigoTurma, String codigoDisciplina) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException;
    void atribuirDisciplinaProfessorTurma(String codigoDisciplina, String registroProfessor, String codigoTurma) throws DadoInvalidoException, EntidadeNaoEncontradaException, IOException;
    void removerAtribuicaoDisciplinaProfessorTurma(String codigoDisciplina, String registroProfessor, String codigoTurma) throws DadoInvalidoException, EntidadeNaoEncontradaException, IOException;
    List<DisciplinaTurma> listarTodasAtribuicoesDisciplinaTurma() throws IOException;
    List<DisciplinaTurma> buscarAtribuicoesPorProfessor(String registroProfessor) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException;
    List<DisciplinaTurma> buscarAtribuicoesPorTurma(String codigoTurma) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException;
    List<DisciplinaTurma> buscarAtribuicoesPorDisciplina(String codigoDisciplina) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException;

    void adicionarFuncionario(Funcionario funcionario) throws DadoInvalidoException, IOException;
    Funcionario buscarFuncionario(String cpf) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException;
    List<Funcionario> listarTodosFuncionarios() throws IOException;
    void atualizarFuncionario(Funcionario funcionario) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException;
    boolean deletarFuncionario(String cpf) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException;

    void adicionarResponsavel(Responsavel responsavel) throws DadoInvalidoException, IOException;
    Responsavel buscarResponsavel(String cpf) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException;
    List<Responsavel> listarTodosResponsaveis() throws IOException;
    void atualizarResponsavel(Responsavel responsavel) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException;
    boolean deletarResponsavel(String cpf) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException;

    void criarAvaliacao(String id, String nomeAvaliacao, LocalDate dataInicio, LocalDate dataFim, Disciplina disciplina) throws DadoInvalidoException, IOException;
    Avaliacao consultarAvaliacao(String id) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException;
    List<Avaliacao> listarTodasAvaliacoes() throws IOException;
    void atualizarAvaliacao(Avaliacao avaliacao) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException;
    boolean excluirAvaliacao(String id) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException;

    void adicionarOcorrencia(String id, LocalDateTime dataHora, String descricao, String registradorId, Aluno aluno) throws DadoInvalidoException, EntidadeNaoEncontradaException, IOException;
    Ocorrencia buscarOcorrencia(String id) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException;
    List<Ocorrencia> listarTodasOcorrencias() throws IOException;
    void atualizarOcorrencia(Ocorrencia ocorrencia) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException;
    boolean deletarOcorrencia(String id) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException;
    void registrarMedidasOcorrencia(String idOcorrencia, String medidas) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException;
    void encerrarOcorrencia(String idOcorrencia) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException;

    void adicionarCalendario(int anoLetivo) throws DadoInvalidoException, IOException;
    CalendarioEscolar buscarCalendario(int anoLetivo) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException;
    List<CalendarioEscolar> listarTodosCalendarios() throws IOException;
    void atualizarCalendario(CalendarioEscolar calendario) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException;
    boolean deletarCalendario(int anoLetivo) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException;
    void adicionarDataLetivaCalendario(int anoLetivo, LocalDate data) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException;
    void removerDataLetivaCalendario(int anoLetivo, LocalDate data) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException;
    void adicionarFeriadoCalendario(int anoLetivo, LocalDate data) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException;
    void removerFeriadoCalendario(int anoLetivo, LocalDate data) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException;
    void registrarAvaliacaoNoCalendario(int anoLetivo, String idAvaliacao) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException;
    void removerAvaliacaoDoCalendario(int anoLetivo, String idAvaliacao) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException;

    void adicionarNota(Nota nota) throws DadoInvalidoException, EntidadeNaoEncontradaException, IOException;
    Nota buscarNota(String matriculaAluno, String codigoDisciplina, String tipoAvaliacao, Date dataLancamento) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException;
    void atualizarNota(Nota nota) throws DadoInvalidoException, EntidadeNaoEncontradaException, IOException;
    boolean deletarNota(String matriculaAluno, String codigoDisciplina, String tipoAvaliacao, Date dataLancamento) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException;
    List<Nota> listarTodasNotas() throws IOException;
    List<Nota> buscarNotasPorAluno(String matriculaAluno) throws DadoInvalidoException, IOException;
    List<Nota> buscarNotasPorDisciplina(String codigoDisciplina) throws DadoInvalidoException, IOException;
    List<Nota> buscarNotasPorAlunoEDisciplina(String matriculaAluno, String codigoDisciplina) throws DadoInvalidoException, IOException;

    void registrarFrequencia(String matriculaAluno, String codigoDisciplina, LocalDate data, boolean presenca) throws DadoInvalidoException, EntidadeNaoEncontradaException, IOException;
    void justificarFalta(String matriculaAluno, String codigoDisciplina, LocalDate data, String justificativa) throws DadoInvalidoException, EntidadeNaoEncontradaException, IOException;
    Frequencia buscarFrequencia(String matriculaAluno, String codigoDisciplina, LocalDate data) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException;
    List<Frequencia> listarFrequenciasPorAluno(String matriculaAluno) throws DadoInvalidoException, IOException;
    List<Frequencia> listarFrequenciasPorDisciplina(String codigoDisciplina) throws DadoInvalidoException, IOException;
    List<Frequencia> listarFrequenciasPorAlunoEDisciplina(String matriculaAluno, String codigoDisciplina) throws DadoInvalidoException, IOException;
    List<Frequencia> listarTodasFrequencias() throws IOException;
    void atualizarFrequencia(Frequencia frequenciaAtualizada) throws DadoInvalidoException, EntidadeNaoEncontradaException, IOException;
    boolean deletarFrequencia(String matriculaAluno, String codigoDisciplina, LocalDate data) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException;

    void limparTodosRepositorios() throws IOException;
}