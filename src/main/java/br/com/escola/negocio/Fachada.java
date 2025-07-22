package br.com.escola.negocio;

import br.com.escola.dados.DisciplinaTurmaRepositorioJson;
import br.com.escola.dados.AlunoRepositorioJson;
import br.com.escola.dados.AvaliacaoRepositorioJson;
import br.com.escola.dados.CalendarioEscolarRepositorioJson;
import br.com.escola.dados.DisciplinaRepositorioJson;
import br.com.escola.dados.FuncionarioRepositorioJson;
import br.com.escola.dados.NotaRepositorioJson;
import br.com.escola.dados.OcorrenciaRepositorioJson;
import br.com.escola.dados.ProfessorRepositorioJson;
import br.com.escola.dados.ResponsavelRepositorioJson;
import br.com.escola.dados.TurmaRepositorioJson;
import br.com.escola.dados.FrequenciaRepositorioJson;
import br.com.escola.excecoes.DadoInvalidoException;
import br.com.escola.excecoes.EntidadeNaoEncontradaException;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class Fachada implements IFachada {

    private static Fachada instance;

    private final AlunoRepositorioJson alunoRepositorio;
    private final AvaliacaoRepositorioJson avaliacaoRepositorio;
    private final DisciplinaRepositorioJson disciplinaRepositorio;
    private final FuncionarioRepositorioJson funcionarioRepositorio;
    private final ProfessorRepositorioJson professorRepositorio;
    private final ResponsavelRepositorioJson responsavelRepositorio;
    private final TurmaRepositorioJson turmaRepositorio;
    private final OcorrenciaRepositorioJson ocorrenciaRepositorio;
    private final CalendarioEscolarRepositorioJson calendarioEscolarRepositorio;
    private final DisciplinaTurmaRepositorioJson disciplinaTurmaRepositorio;
    private final NotaRepositorioJson notaRepositorio;
    private final FrequenciaRepositorioJson frequenciaRepositorio;

    private final DisciplinaTurmaServico disciplinaTurmaServico;
    private final AlunoServico alunoServico;
    private final ProfessorServico professorServico;
    private final DisciplinaServico disciplinaServico;
    private final TurmaServico turmaServico;
    private final FuncionarioServico funcionarioServico;
    private final ResponsavelServico responsavelServico;
    private final AvaliacaoServico avaliacaoServico;
    private final OcorrenciaServico ocorrenciaServico;
    private final CalendarioEscolarServico calendarioEscolarServico;
    private final NotaServico notaServico;
    private final FrequenciaServico frequenciaServico;
    private final Acesso acesso;

    private Fachada() {
        this.alunoRepositorio = new AlunoRepositorioJson();
        this.professorRepositorio = new ProfessorRepositorioJson();
        this.disciplinaRepositorio = new DisciplinaRepositorioJson();
        this.funcionarioRepositorio = new FuncionarioRepositorioJson();
        this.responsavelRepositorio = new ResponsavelRepositorioJson();
        this.turmaRepositorio = new TurmaRepositorioJson();
        this.avaliacaoRepositorio = new AvaliacaoRepositorioJson();
        this.ocorrenciaRepositorio = new OcorrenciaRepositorioJson();
        this.calendarioEscolarRepositorio = new CalendarioEscolarRepositorioJson();
        this.disciplinaTurmaRepositorio = new DisciplinaTurmaRepositorioJson();
        this.notaRepositorio = new NotaRepositorioJson();
        this.frequenciaRepositorio = new FrequenciaRepositorioJson();

        this.acesso = new Acesso();

        this.responsavelServico = new ResponsavelServico(this.responsavelRepositorio);
        this.alunoServico = new AlunoServico(this.alunoRepositorio, this.responsavelServico);
        this.professorServico = new ProfessorServico(this.professorRepositorio);
        this.disciplinaServico = new DisciplinaServico(this.disciplinaRepositorio);
        this.disciplinaTurmaServico = new DisciplinaTurmaServico(this.disciplinaTurmaRepositorio);

        this.turmaServico = new TurmaServico(this.turmaRepositorio,
                                             this.alunoServico,
                                             this.disciplinaServico,
                                             this.professorServico);

        this.funcionarioServico = new FuncionarioServico(this.funcionarioRepositorio);
        this.avaliacaoServico = new AvaliacaoServico(this.avaliacaoRepositorio);

        this.ocorrenciaServico = new OcorrenciaServico(this.ocorrenciaRepositorio,
                                                       this.alunoServico,
                                                       this.funcionarioServico,
                                                       this.professorServico);

        this.calendarioEscolarServico = new CalendarioEscolarServico(this.calendarioEscolarRepositorio,
                                                                   this.avaliacaoServico);

        this.notaServico = new NotaServico(this.notaRepositorio, this.alunoServico, this.disciplinaServico);
        this.frequenciaServico = new FrequenciaServico(this.frequenciaRepositorio, this.alunoServico, this.disciplinaServico);
    }

    public static Fachada getInstance() {
        if (instance == null) {
            instance = new Fachada();
        }
        return instance;
    }

    @Override
    public Usuario autenticarUsuario(String nomeUsuario, String senha) {
        return acesso.autenticar(nomeUsuario, senha);
    }

    @Override
    public boolean checarPermissao(Usuario usuario, String funcionalidade) {
        return acesso.checarPermissao(usuario, funcionalidade);
    }

    @Override
    public void adicionarAluno(Aluno aluno) throws DadoInvalidoException, EntidadeNaoEncontradaException, IOException {
        alunoServico.adicionarAluno(aluno);
    }

    @Override
    public Aluno buscarAluno(String matricula) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        return alunoServico.buscarAluno(matricula);
    }

    @Override
    public List<Aluno> listarTodosAlunos() throws IOException {
        return alunoServico.listarTodosAlunos();
    }

    @Override
    public void atualizarAluno(Aluno aluno) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        alunoServico.atualizarAluno(aluno);
    }

    @Override
    public boolean deletarAluno(String matricula) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        return alunoServico.deletarAluno(matricula);
    }

    @Override
    public void adicionarResponsavelAoAluno(String matriculaAluno, String cpfResponsavel) throws DadoInvalidoException, EntidadeNaoEncontradaException, IOException {
        alunoServico.adicionarResponsavelAoAluno(matriculaAluno, cpfResponsavel);
    }

    @Override
    public void removerResponsavelDoAluno(String matriculaAluno, String cpfResponsavel) throws DadoInvalidoException, EntidadeNaoEncontradaException, IOException {
        alunoServico.removerResponsavelDoAluno(matriculaAluno, cpfResponsavel);
    }

    @Override
    public List<Aluno> buscarAlunosPorAnoLetivo(int anoLetivo) throws DadoInvalidoException, IOException {
        return alunoServico.buscarAlunosPorAnoLetivo(anoLetivo);
    }

    @Override
    public void adicionarProfessor(Professor professor) throws DadoInvalidoException, IOException {
        professorServico.adicionarProfessor(professor);
    }

    @Override
    public Professor buscarProfessor(String registroFuncional) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        return professorServico.buscarProfessor(registroFuncional);
    }

    @Override
    public List<Professor> listarTodosProfessores() throws IOException {
        return professorServico.listarTodosProfessores();
    }

    @Override
    public void atualizarProfessor(Professor professor) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        professorServico.atualizarProfessor(professor);
    }

    @Override
    public boolean deletarProfessor(String registroFuncional) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        return professorServico.deletarProfessor(registroFuncional);
    }

    @Override
    public void adicionarDisciplina(Disciplina disciplina) throws DadoInvalidoException, IOException {
        disciplinaServico.adicionarDisciplina(disciplina);
    }

    @Override
    public Disciplina buscarDisciplina(String codigo) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        return disciplinaServico.buscarDisciplina(codigo);
    }

    @Override
    public List<Disciplina> listarTodasDisciplinas() throws IOException {
        return disciplinaServico.listarTodasDisciplinas();
    }

    @Override
    public void atualizarDisciplina(Disciplina disciplina) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        disciplinaServico.atualizarDisciplina(disciplina);
    }

    @Override
    public boolean deletarDisciplina(String codigo) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        return disciplinaServico.deletarDisciplina(codigo);
    }

    @Override
    public void adicionarTurma(Turma turma) throws DadoInvalidoException, EntidadeNaoEncontradaException, IOException {
        turmaServico.adicionarTurma(turma);
    }

    @Override
    public Turma buscarTurma(String codigo) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        return turmaServico.buscarTurma(codigo);
    }

    @Override
    public List<Turma> listarTodasTurmas() throws IOException {
        return turmaServico.listarTodasTurmas();
    }

    @Override
    public void atualizarTurma(Turma turma) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        turmaServico.atualizarTurma(turma);
    }

    @Override
    public boolean deletarTurma(String codigo) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        return turmaServico.deletarTurma(codigo);
    }

    @Override
    public void matricularAlunoNaTurma(String codigoTurma, String matriculaAluno) throws DadoInvalidoException, EntidadeNaoEncontradaException, IOException {
        turmaServico.matricularAlunoNaTurma(codigoTurma, matriculaAluno);
    }

    @Override
    public void desmatricularAlunoDaTurma(String codigoTurma, String matriculaAluno) throws DadoInvalidoException, EntidadeNaoEncontradaException, IOException {
        turmaServico.desmatricularAlunoDaTurma(codigoTurma, matriculaAluno);
    }

    @Override
    public void adicionarDisciplinaNaTurma(String codigoTurma, String codigoDisciplina) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        Turma turma = turmaServico.buscarTurma(codigoTurma);
        Disciplina disciplina = disciplinaServico.buscarDisciplina(codigoDisciplina);

        Professor professorAssociado = null;
        List<Professor> professores = professorServico.listarTodosProfessores();
        if (!professores.isEmpty()) {
            professorAssociado = professores.get(0);
        } else {
            throw new EntidadeNaoEncontradaException("Nenhum professor disponível para associar à disciplina na turma.");
        }

        DisciplinaTurma novaAssociacao = new DisciplinaTurma(disciplina, professorAssociado, turma);
        disciplinaTurmaServico.adicionarDisciplinaTurma(novaAssociacao);

        professorAssociado.adicionarDisciplinaLecionada(novaAssociacao);

        professorServico.atualizarProfessor(professorAssociado);
    }

    @Override
    public void removerDisciplinaDaTurma(String codigoTurma, String codigoDisciplina) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        Turma turma = turmaServico.buscarTurma(codigoTurma);
        Disciplina disciplina = disciplinaServico.buscarDisciplina(codigoDisciplina);

        DisciplinaTurma associacaoParaRemover = disciplinaTurmaServico.buscarPorAtributos(disciplina, null, turma);
        
        if (associacaoParaRemover == null) {
            List<DisciplinaTurma> atribuicoesDaTurma = disciplinaTurmaServico.listarProfessoresPorTurma(turma);
            for (DisciplinaTurma dt : atribuicoesDaTurma) {
                if (dt.getDisciplina().getCodigo().equals(codigoDisciplina)) {
                    associacaoParaRemover = dt;
                    break;
                }
            }
        }

        if (associacaoParaRemover == null) {
            throw new EntidadeNaoEncontradaException("Associação da Disciplina " + disciplina.getNome() + " com a Turma " + turma.getNomeTurma() + " não encontrada para remoção.");
        }

        disciplinaTurmaServico.removerDisciplinaTurma(associacaoParaRemover.getId());
        
        Professor professorAssociado = associacaoParaRemover.getProfessor();
        if (professorAssociado != null) {
            professorAssociado.removerDisciplinaLecionada(associacaoParaRemover);
            professorServico.atualizarProfessor(professorAssociado);
        }
    }

    @Override
    public void atribuirDisciplinaProfessorTurma(String codigoDisciplina, String registroProfessor, String codigoTurma)
            throws DadoInvalidoException, EntidadeNaoEncontradaException, IOException {
        Disciplina disciplina = disciplinaServico.buscarDisciplina(codigoDisciplina);
        Professor professor = professorServico.buscarProfessor(registroProfessor);
        Turma turma = turmaServico.buscarTurma(codigoTurma);

        DisciplinaTurma novaAtribuicao = new DisciplinaTurma(disciplina, professor, turma);
        disciplinaTurmaServico.adicionarDisciplinaTurma(novaAtribuicao);

        professor.adicionarDisciplinaLecionada(novaAtribuicao);
        
        professorServico.atualizarProfessor(professor);
    }

    @Override
    public void removerAtribuicaoDisciplinaProfessorTurma(String codigoDisciplina, String registroProfessor, String codigoTurma)
            throws DadoInvalidoException, EntidadeNaoEncontradaException, IOException {

        Disciplina disciplina = disciplinaServico.buscarDisciplina(codigoDisciplina);
        Professor professor = professorServico.buscarProfessor(registroProfessor);
        Turma turma = turmaServico.buscarTurma(codigoTurma);

        DisciplinaTurma atribuicaoParaRemover = disciplinaTurmaServico.buscarPorAtributos(disciplina, professor, turma);
        if (atribuicaoParaRemover == null) {
            throw new EntidadeNaoEncontradaException("Atribuição de disciplina para professor e turma não encontrada para remoção.");
        }

        disciplinaTurmaServico.removerDisciplinaTurma(atribuicaoParaRemover.getId());

        professor.removerDisciplinaLecionada(atribuicaoParaRemover);
        
        professorServico.atualizarProfessor(professor);
    }

    @Override
    public List<DisciplinaTurma> listarTodasAtribuicoesDisciplinaTurma() throws IOException {
        return disciplinaTurmaServico.listarTodas();
    }

    @Override
    public List<DisciplinaTurma> buscarAtribuicoesPorProfessor(String registroProfessor)
            throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        Professor professor = professorServico.buscarProfessor(registroProfessor);
        return disciplinaTurmaServico.listarDisciplinasPorProfessor(professor);
    }

    @Override
    public List<DisciplinaTurma> buscarAtribuicoesPorTurma(String codigoTurma)
            throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        Turma turma = turmaServico.buscarTurma(codigoTurma);
        return disciplinaTurmaServico.listarProfessoresPorTurma(turma);
    }

    @Override
    public List<DisciplinaTurma> buscarAtribuicoesPorDisciplina(String codigoDisciplina)
            throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        Disciplina disciplina = disciplinaServico.buscarDisciplina(codigoDisciplina);
        return disciplinaTurmaServico.listarTurmasPorDisciplina(disciplina);
    }

    @Override
    public void adicionarFuncionario(Funcionario funcionario) throws DadoInvalidoException, IOException {
        funcionarioServico.adicionarFuncionario(funcionario);
    }

    @Override
    public Funcionario buscarFuncionario(String cpf) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        return funcionarioServico.buscarFuncionario(cpf);
    }

    @Override
    public List<Funcionario> listarTodosFuncionarios() throws IOException {
        return funcionarioServico.listarTodosFuncionarios();
    }

    @Override
    public void atualizarFuncionario(Funcionario funcionario) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        funcionarioServico.atualizarFuncionario(funcionario);
    }

    @Override
    public boolean deletarFuncionario(String cpf) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        return funcionarioServico.deletarFuncionario(cpf);
    }

    @Override
    public void adicionarResponsavel(Responsavel responsavel) throws DadoInvalidoException, IOException {
        responsavelServico.adicionarResponsavel(responsavel);
    }

    @Override
    public Responsavel buscarResponsavel(String cpf) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        return responsavelServico.buscarResponsavel(cpf);
    }

    @Override
    public List<Responsavel> listarTodosResponsaveis() throws IOException {
        return responsavelServico.listarTodosResponsaveis();
    }

    @Override
    public void atualizarResponsavel(Responsavel responsavel) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        responsavelServico.atualizarResponsavel(responsavel);
    }

    @Override
    public boolean deletarResponsavel(String cpf) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        return responsavelServico.deletarResponsavel(cpf);
    }

    @Override
    public void criarAvaliacao(String id, String nomeAvaliacao, LocalDate dataInicio, LocalDate dataFim, Disciplina disciplina) throws DadoInvalidoException, IOException {
        Avaliacao novaAvaliacao = new Avaliacao(id, nomeAvaliacao, dataInicio, dataFim, disciplina);
        avaliacaoServico.salvarAvaliacao(novaAvaliacao);
    }

    @Override
    public Avaliacao consultarAvaliacao(String id) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        return avaliacaoServico.buscarAvaliacaoPorId(id);
    }

    @Override
    public List<Avaliacao> listarTodasAvaliacoes() throws IOException {
        return avaliacaoServico.listarTodasAvaliacoes();
    }

    @Override
    public void atualizarAvaliacao(Avaliacao avaliacao) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        avaliacaoServico.atualizarAvaliacao(avaliacao);
    }

    @Override
    public boolean excluirAvaliacao(String id) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        return avaliacaoServico.deletarAvaliacao(id);
    }

    @Override
    public void adicionarOcorrencia(String id, LocalDateTime dataHora, String descricao, String registradorId, Aluno aluno) throws DadoInvalidoException, EntidadeNaoEncontradaException, IOException {
        Ocorrencia novaOcorrencia = new Ocorrencia(id, dataHora, descricao, registradorId, aluno);
        ocorrenciaServico.adicionarOcorrencia(novaOcorrencia);
    }

    @Override
    public Ocorrencia buscarOcorrencia(String id) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        return ocorrenciaServico.buscarOcorrencia(id);
    }

    @Override
    public List<Ocorrencia> listarTodasOcorrencias() throws IOException {
        return ocorrenciaServico.listarTodasOcorrencias();
    }

    @Override
    public void atualizarOcorrencia(Ocorrencia ocorrencia) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        ocorrenciaServico.atualizarOcorrencia(ocorrencia);
    }

    @Override
    public boolean deletarOcorrencia(String id) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        return ocorrenciaServico.deletarOcorrencia(id);
    }

    @Override
    public void registrarMedidasOcorrencia(String idOcorrencia, String medidas) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        ocorrenciaServico.registrarMedidasOcorrencia(idOcorrencia, medidas);
    }

    @Override
    public void encerrarOcorrencia(String idOcorrencia) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        ocorrenciaServico.encerrarOcorrencia(idOcorrencia);
    }

    @Override
    public void adicionarCalendario(int anoLetivo) throws DadoInvalidoException, IOException {
        CalendarioEscolar novoCalendario = new CalendarioEscolar(anoLetivo);
        calendarioEscolarServico.adicionarCalendario(novoCalendario);
    }

    @Override
    public CalendarioEscolar buscarCalendario(int anoLetivo) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        return calendarioEscolarServico.buscarCalendario(anoLetivo);
    }

    @Override
    public List<CalendarioEscolar> listarTodosCalendarios() throws IOException {
        return calendarioEscolarServico.listarTodosCalendarios();
    }

    @Override
    public void atualizarCalendario(CalendarioEscolar calendario) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        calendarioEscolarServico.atualizarCalendario(calendario);
    }

    @Override
    public boolean deletarCalendario(int anoLetivo) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        return calendarioEscolarServico.deletarCalendario(anoLetivo);
    }

    @Override
    public void adicionarDataLetivaCalendario(int anoLetivo, LocalDate data) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        calendarioEscolarServico.adicionarDataLetiva(anoLetivo, data);
    }

    @Override
    public void removerDataLetivaCalendario(int anoLetivo, LocalDate data) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        calendarioEscolarServico.removerDataLetiva(anoLetivo, data);
    }

    @Override
    public void adicionarFeriadoCalendario(int anoLetivo, LocalDate data) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        calendarioEscolarServico.adicionarFeriado(anoLetivo, data);
    }

    @Override
    public void removerFeriadoCalendario(int anoLetivo, LocalDate data) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        calendarioEscolarServico.removerFeriado(anoLetivo, data);
    }

    @Override
    public void registrarAvaliacaoNoCalendario(int anoLetivo, String idAvaliacao) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        calendarioEscolarServico.registrarAvaliacaoNoCalendario(anoLetivo, idAvaliacao);
    }

    @Override
    public void removerAvaliacaoDoCalendario(int anoLetivo, String idAvaliacao) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        calendarioEscolarServico.removerAvaliacaoDoCalendario(anoLetivo, idAvaliacao);
    }

    @Override
    public void adicionarNota(Nota nota) throws DadoInvalidoException, EntidadeNaoEncontradaException, IOException {
        notaServico.adicionarNota(nota);
    }

    @Override
    public Nota buscarNota(String matriculaAluno, String codigoDisciplina, String tipoAvaliacao, Date dataLancamento) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        return notaServico.buscarNota(matriculaAluno, codigoDisciplina, tipoAvaliacao, dataLancamento);
    }

    @Override
    public void atualizarNota(Nota nota) throws DadoInvalidoException, EntidadeNaoEncontradaException, IOException {
        notaServico.atualizarNota(nota);
    }

    @Override
    public boolean deletarNota(String matriculaAluno, String codigoDisciplina, String tipoAvaliacao, Date dataLancamento) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        return notaServico.deletarNota(matriculaAluno, codigoDisciplina, tipoAvaliacao, dataLancamento);
    }

    @Override
    public List<Nota> listarTodasNotas() throws IOException {
        return notaServico.listarTodasNotas();
    }

    @Override
    public List<Nota> buscarNotasPorAluno(String matriculaAluno) throws DadoInvalidoException, IOException {
        return notaServico.buscarNotasPorAluno(matriculaAluno);
    }

    @Override
    public List<Nota> buscarNotasPorDisciplina(String codigoDisciplina) throws DadoInvalidoException, IOException {
        return notaServico.buscarNotasPorDisciplina(codigoDisciplina);
    }

    @Override
    public List<Nota> buscarNotasPorAlunoEDisciplina(String matriculaAluno, String codigoDisciplina) throws DadoInvalidoException, IOException {
        return notaServico.buscarNotasPorAlunoEDisciplina(matriculaAluno, codigoDisciplina);
    }

    @Override
    public void registrarFrequencia(String matriculaAluno, String codigoDisciplina, LocalDate data, boolean presenca) throws DadoInvalidoException, EntidadeNaoEncontradaException, IOException {
        frequenciaServico.registrarFrequencia(matriculaAluno, codigoDisciplina, data, presenca);
    }

    @Override
    public void justificarFalta(String matriculaAluno, String codigoDisciplina, LocalDate data, String justificativa) throws DadoInvalidoException, EntidadeNaoEncontradaException, IOException {
        frequenciaServico.justificarFalta(matriculaAluno, codigoDisciplina, data, justificativa);
    }

    @Override
    public Frequencia buscarFrequencia(String matriculaAluno, String codigoDisciplina, LocalDate data) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        return frequenciaServico.buscarFrequencia(matriculaAluno, codigoDisciplina, data);
    }

    @Override
    public List<Frequencia> listarFrequenciasPorAluno(String matriculaAluno) throws DadoInvalidoException, IOException {
        return frequenciaServico.listarFrequenciasPorAluno(matriculaAluno);
    }

    @Override
    public List<Frequencia> listarFrequenciasPorDisciplina(String codigoDisciplina) throws DadoInvalidoException, IOException {
        return frequenciaServico.listarFrequenciasPorDisciplina(codigoDisciplina);
    }

    @Override
    public List<Frequencia> listarFrequenciasPorAlunoEDisciplina(String matriculaAluno, String codigoDisciplina) throws DadoInvalidoException, IOException {
        return frequenciaServico.listarFrequenciasPorAlunoEDisciplina(matriculaAluno, codigoDisciplina);
    }

    @Override
    public List<Frequencia> listarTodasFrequencias() throws IOException {
        return frequenciaServico.listarTodasFrequencias();
    }

    @Override
    public void atualizarFrequencia(Frequencia frequenciaAtualizada) throws DadoInvalidoException, EntidadeNaoEncontradaException, IOException {
        frequenciaServico.atualizarFrequencia(frequenciaAtualizada);
    }

    @Override
    public boolean deletarFrequencia(String matriculaAluno, String codigoDisciplina, LocalDate data) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        return frequenciaServico.deletarFrequencia(matriculaAluno, codigoDisciplina, data);
    }

    @Override
    public void limparTodosRepositorios() throws IOException {
        alunoRepositorio.limpar();
        professorRepositorio.limpar();
        disciplinaRepositorio.limpar();
        funcionarioRepositorio.limpar();
        responsavelRepositorio.limpar();
        turmaRepositorio.limpar();
        avaliacaoRepositorio.limpar();
        ocorrenciaRepositorio.limpar();
        calendarioEscolarRepositorio.limpar();
        disciplinaTurmaRepositorio.limpar();
        notaRepositorio.limpar();
        frequenciaRepositorio.limpar();
    }
}