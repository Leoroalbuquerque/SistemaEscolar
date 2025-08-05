package br.com.escola.negocio;

import br.com.escola.dados.AlunoRepositorioJson;
import br.com.escola.dados.AvaliacaoRepositorioJson;
import br.com.escola.dados.CalendarioEscolarRepositorioJson;
import br.com.escola.dados.DisciplinaRepositorioJson;
import br.com.escola.dados.DisciplinaTurmaRepositorioJson;
import br.com.escola.dados.FrequenciaRepositorioJson;
import br.com.escola.dados.FuncionarioRepositorioJson;
import br.com.escola.dados.NotaRepositorioJson;
import br.com.escola.dados.OcorrenciaRepositorioJson;
import br.com.escola.dados.ProfessorRepositorioJson;
import br.com.escola.dados.ResponsavelRepositorioJson;
import br.com.escola.dados.SerieEscolarRepositorioJson;
import br.com.escola.dados.TurmaRepositorioJson;
import br.com.escola.excecoes.DadoInvalidoException;
import br.com.escola.excecoes.EntidadeNaoEncontradaException;
import br.com.escola.util.BoletimEscolar;
import br.com.escola.util.DiarioClasse;
import br.com.escola.util.EventoCalendario;
import br.com.escola.util.HistoricoEscolar;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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
    private final SerieEscolarRepositorioJson serieEscolarRepositorio;
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
    private final SerieEscolarServico serieEscolarServico;
    private final Acesso acesso;

    public Fachada() {
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
        this.serieEscolarRepositorio = new SerieEscolarRepositorioJson();
        this.acesso = new Acesso();
        this.responsavelServico = new ResponsavelServico(this.responsavelRepositorio);
        this.alunoServico = new AlunoServico(this.alunoRepositorio, this.responsavelServico);
        this.professorServico = new ProfessorServico(this.professorRepositorio);
        this.disciplinaServico = new DisciplinaServico(this.disciplinaRepositorio);
        this.serieEscolarServico = new SerieEscolarServico(this.serieEscolarRepositorio);
        this.turmaServico = new TurmaServico(this.turmaRepositorio, this.alunoServico, this.disciplinaServico, this.professorServico, this.serieEscolarServico);
        this.funcionarioServico = new FuncionarioServico(this.funcionarioRepositorio);
        this.avaliacaoServico = new AvaliacaoServico(this.avaliacaoRepositorio);
        this.ocorrenciaServico = new OcorrenciaServico(this.ocorrenciaRepositorio, this.alunoServico, this.funcionarioServico, this.professorServico);
        this.calendarioEscolarServico = new CalendarioEscolarServico(this.calendarioEscolarRepositorio, this.avaliacaoServico);
        this.notaServico = new NotaServico(this.notaRepositorio, this.alunoServico, this.disciplinaServico);
        this.frequenciaServico = new FrequenciaServico(this.frequenciaRepositorio, this.alunoServico, this.disciplinaServico);
        this.disciplinaTurmaServico = new DisciplinaTurmaServico(this.disciplinaTurmaRepositorio, this.turmaServico, this.disciplinaServico, this.professorServico);
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
    public boolean criarNovoUsuario(String nomeUsuario, String senha, String perfil) throws DadoInvalidoException, IOException {
        boolean sucesso = acesso.criarUsuario(nomeUsuario, senha, perfil);
        if (!sucesso) {
            throw new DadoInvalidoException("Nome de usuário '" + nomeUsuario + "' já existe.");
        }
        return true;
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
    public void deletarAluno(String matricula) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        alunoServico.deletarAluno(matricula);
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
        try {
            professorServico.adicionarProfessor(professor);
        } catch (EntidadeNaoEncontradaException ex) {
            System.getLogger(Fachada.class.getName()).log(System.Logger.Level.ERROR, "Erro ao adicionar professor", ex);
        }
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
    public void deletarProfessor(String registroFuncional) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        professorServico.deletarProfessor(registroFuncional);
    }

    @Override
    public void adicionarDisciplina(Disciplina disciplina) throws DadoInvalidoException, IOException {
        try {
            disciplinaServico.adicionarDisciplina(disciplina);
        } catch (EntidadeNaoEncontradaException ex) {
            System.getLogger(Fachada.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
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
    public void deletarDisciplina(String codigo) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        disciplinaServico.deletarDisciplina(codigo);
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
    public void deletarTurma(String codigo) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        turmaServico.deletarTurma(codigo);
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
    public void adicionarDisciplinaComProfessorNaTurma(String codigoTurma, String codigoDisciplina, String registroProfessor)
            throws DadoInvalidoException, EntidadeNaoEncontradaException, IOException {
        Turma turma = turmaServico.buscarTurma(codigoTurma);
        Disciplina disciplina = disciplinaServico.buscarDisciplina(codigoDisciplina);
        Professor professor = professorServico.buscarProfessor(registroProfessor);

        if (turma == null) {
            throw new EntidadeNaoEncontradaException("Turma com código " + codigoTurma + " não encontrada.");
        }
        if (disciplina == null) {
            throw new EntidadeNaoEncontradaException("Disciplina com código " + codigoDisciplina + " não encontrada.");
        }
        if (professor == null) {
            throw new EntidadeNaoEncontradaException("Professor com registro funcional " + registroProfessor + " não encontrado.");
        }

        disciplinaTurmaServico.adicionarDisciplinaComProfessorNaTurma(
                turma.getCodigo(),
                disciplina.getCodigo(),
                professor.getRegistroFuncional()
        );
    }

    @Override
    public void removerDisciplinaDaTurma(String codigoTurma, String codigoDisciplina) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        Disciplina disciplina = buscarDisciplina(codigoDisciplina);
        Turma turma = buscarTurma(codigoTurma);
        if (turma == null) throw new EntidadeNaoEncontradaException("Turma com código " + codigoTurma + " não encontrada.");
        if (disciplina == null) throw new EntidadeNaoEncontradaException("Disciplina com código " + codigoDisciplina + " não encontrada.");
        List<DisciplinaTurma> atribuicoesParaRemover = disciplinaTurmaServico.listarPorDisciplina(disciplina.getCodigo()).stream().filter(dt -> dt.getTurma().getCodigo().equals(turma.getCodigo())).collect(Collectors.toList());
        if (atribuicoesParaRemover.isEmpty()) {
            throw new EntidadeNaoEncontradaException("Disciplina " + disciplina.getNome() + " não encontrada na turma " + turma.getNomeTurma() + " para remoção.");
        }
        for (DisciplinaTurma dt : atribuicoesParaRemover) {
            disciplinaTurmaServico.removerDisciplinaTurma(dt.getId());
        }
    }

    @Override
    public void removerProfessorDeDisciplinaNaTurma(String codigoTurma, String codigoDisciplina) throws DadoInvalidoException, EntidadeNaoEncontradaException, IOException {
        Disciplina disciplina = disciplinaServico.buscarDisciplina(codigoDisciplina);
        Turma turma = turmaServico.buscarTurma(codigoTurma);
        DisciplinaTurma atribuicao = disciplinaTurmaServico.buscarAtribuicao(turma, disciplina);
        if (atribuicao == null) {
            throw new EntidadeNaoEncontradaException("Atribuição de disciplina e turma não encontrada.");
        }
        if (atribuicao.getProfessor() == null) {
            throw new DadoInvalidoException("Nenhum professor está atribuído a esta disciplina e turma.");
        }
        atribuicao.setProfessor(null);
        disciplinaTurmaServico.atualizarDisciplinaTurma(atribuicao);
    }

    @Override
    public void removerAtribuicaoDisciplinaProfessorTurma(String codigoDisciplina, String registroProfessor, String codigoTurma) throws DadoInvalidoException, EntidadeNaoEncontradaException, IOException {
        Disciplina disciplina = disciplinaServico.buscarDisciplina(codigoDisciplina);
        Professor professor = professorServico.buscarProfessor(registroProfessor);
        Turma turma = turmaServico.buscarTurma(codigoTurma);
        if (turma == null) throw new EntidadeNaoEncontradaException("Turma com código " + codigoTurma + " não encontrada.");
        if (disciplina == null) throw new EntidadeNaoEncontradaException("Disciplina com código " + codigoDisciplina + " não encontrada.");
        if (professor == null) throw new EntidadeNaoEncontradaException("Professor com registro funcional " + registroProfessor + " não encontrado.");
        DisciplinaTurma atribuicaoParaRemover = null;
        try {
            atribuicaoParaRemover = disciplinaTurmaServico.buscarPorAtributos(disciplina, professor, turma);
        } catch (EntidadeNaoEncontradaException e) {
            atribuicaoParaRemover = disciplinaTurmaServico.buscarAtribuicao(turma, disciplina);
            if (atribuicaoParaRemover != null && atribuicaoParaRemover.getProfessor() != null && atribuicaoParaRemover.getProfessor().getCpf().equals(professor.getCpf())) {
                atribuicaoParaRemover.setProfessor(null);
                disciplinaTurmaServico.atualizarDisciplinaTurma(atribuicaoParaRemover);
                return;
            } else {
                throw new EntidadeNaoEncontradaException("Atribuição de disciplina para professor e turma não encontrada para remoção ou o professor não está associado.");
            }
        }
        if (atribuicaoParaRemover != null) {
            disciplinaTurmaServico.removerDisciplinaTurma(atribuicaoParaRemover.getId());
        } else {
            throw new EntidadeNaoEncontradaException("Atribuição de disciplina para professor e turma não encontrada para remoção.");
        }
    }

    @Override
    public List<DisciplinaTurma> listarTodasAtribuicoesDisciplinaTurma() throws IOException {
        return disciplinaTurmaServico.listarTodas();
    }

    @Override
    public List<DisciplinaTurma> buscarAtribuicoesPorProfessor(String registroProfessor) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        return disciplinaTurmaServico.listarPorProfessor(registroProfessor);
    }

    @Override
    public List<DisciplinaTurma> buscarAtribuicoesPorTurma(String codigoTurma) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        return disciplinaTurmaServico.listarPorTurma(codigoTurma);
    }

    @Override
    public List<DisciplinaTurma> buscarAtribuicoesPorDisciplina(String codigoDisciplina) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        return disciplinaTurmaServico.listarPorDisciplina(codigoDisciplina);
    }

    @Override
    public List<Disciplina> buscarDisciplinasPorTurma(String codigoTurma) throws IOException, EntidadeNaoEncontradaException, DadoInvalidoException {
        return disciplinaTurmaServico.buscarDisciplinasPorTurma(codigoTurma);
    }

    @Override
    public List<Professor> buscarProfessoresPorTurma(String codigoTurma) throws IOException, EntidadeNaoEncontradaException, DadoInvalidoException {
        return disciplinaTurmaServico.buscarProfessoresPorTurma(codigoTurma);
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
    public void deletarFuncionario(String cpf) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        funcionarioServico.deletarFuncionario(cpf);
    }

    @Override
    public void adicionarResponsavel(Responsavel responsavel) throws DadoInvalidoException, IOException {
        try {
            responsavelServico.adicionarResponsavel(responsavel);
        } catch (EntidadeNaoEncontradaException ex) {
            System.getLogger(Fachada.class.getName()).log(System.Logger.Level.ERROR, "Erro ao adicionar responsável", ex);
        }
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
    public void deletarResponsavel(String cpf) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        responsavelServico.deletarResponsavel(cpf);
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
    public void excluirAvaliacao(String id) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        avaliacaoServico.deletarAvaliacao(id);
    }

    @Override
    public void adicionarOcorrencia(String id, LocalDateTime dataHora, String registradorId, Aluno aluno, String descricao, String medidasTomadas) throws DadoInvalidoException, EntidadeNaoEncontradaException, IOException {
        Ocorrencia novaOcorrencia = new Ocorrencia(id, dataHora, registradorId, aluno, descricao, medidasTomadas);
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
    public void deletarOcorrencia(String id) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        ocorrenciaServico.deletarOcorrencia(id);
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
    public void deletarCalendario(int anoLetivo) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        calendarioEscolarServico.deletarCalendario(anoLetivo);
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
    public void adicionarEventoNoCalendario(int anoLetivo, EventoCalendario evento) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        calendarioEscolarServico.adicionarEventoNoCalendario(anoLetivo, evento);
    }

    @Override
    public void removerEventoDoCalendario(int anoLetivo, String idEvento) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        calendarioEscolarServico.removerEventoDoCalendario(anoLetivo, idEvento);
    }

    @Override
    public void adicionarNota(Nota nota) throws DadoInvalidoException, EntidadeNaoEncontradaException, IOException {
        notaServico.adicionarNota(nota);
    }

    @Override
    public Nota buscarNota(String matriculaAluno, String codigoDisciplina, String tipoAvaliacao, LocalDate dataLancamento) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        return notaServico.buscarNota(matriculaAluno, codigoDisciplina, tipoAvaliacao, dataLancamento);
    }

    @Override
    public void atualizarNota(Nota nota) throws DadoInvalidoException, EntidadeNaoEncontradaException, IOException {
        notaServico.atualizarNota(nota);
    }

    @Override
    public void deletarNota(String matriculaAluno, String codigoDisciplina, String tipoAvaliacao, LocalDate dataLancamento) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        notaServico.deletarNota(matriculaAluno, codigoDisciplina, tipoAvaliacao, dataLancamento);
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
    public void deletarFrequencia(String matriculaAluno, String codigoDisciplina, LocalDate data) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        frequenciaServico.deletarFrequencia(matriculaAluno, codigoDisciplina, data);
    }

    @Override
    public void adicionarSerieEscolar(SerieEscolar serie) throws DadoInvalidoException, IOException {
        serieEscolarServico.adicionarSerieEscolar(serie);
    }

    @Override
    public SerieEscolar buscarSerieEscolar(String codigo) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        return serieEscolarServico.buscarSerieEscolar(codigo);
    }

    @Override
    public List<SerieEscolar> listarTodasSeriesEscolares() throws IOException {
        return serieEscolarServico.listarTodasSeriesEscolares();
    }

    @Override
    public void atualizarSerieEscolar(SerieEscolar serie) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        serieEscolarServico.atualizarSerieEscolar(serie);
    }

    @Override
    public void deletarSerieEscolar(String codigo) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        serieEscolarServico.deletarSerieEscolar(codigo);
    }

    @Override
    public BoletimEscolar gerarBoletim(String matriculaAluno) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        Aluno aluno = alunoServico.buscarAluno(matriculaAluno);
        List<Nota> notasDoAluno = notaServico.buscarNotasPorAluno(matriculaAluno);
        List<Frequencia> frequenciasDoAluno = frequenciaServico.listarFrequenciasPorAluno(matriculaAluno);
        return new BoletimEscolar(aluno, notasDoAluno, frequenciasDoAluno);
    }

    @Override
    public DiarioClasse gerarDiario(String codigoTurma, String codigoDisciplina) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        Turma turma = turmaServico.buscarTurma(codigoTurma);
        Disciplina disciplina = disciplinaServico.buscarDisciplina(codigoDisciplina);
        Professor professor = disciplinaTurmaServico.listarPorTurma(turma.getCodigo()).stream()
                .filter(dt -> dt.getDisciplina().getCodigo().equals(disciplina.getCodigo()))
                .map(DisciplinaTurma::getProfessor)
                .findFirst()
                .orElse(null);
        List<Aluno> alunos = turmaServico.listarAlunosNaTurma(codigoTurma);
        List<Frequencia> frequencias = frequenciaServico.listarFrequenciasPorDisciplina(disciplina.getCodigo()).stream()
                .filter(f -> alunos.stream().anyMatch(a -> a.getMatricula().equals(f.getAluno().getMatricula())))
                .collect(Collectors.toList());
        return new DiarioClasse(turma, disciplina, professor, alunos, frequencias);
    }

    @Override
    public HistoricoEscolar gerarHistorico(String matriculaAluno) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        Aluno aluno = alunoServico.buscarAluno(matriculaAluno);
        List<Nota> notasDoAluno = notaServico.buscarNotasPorAluno(matriculaAluno);
        List<Frequencia> frequenciasDoAluno = frequenciaServico.listarFrequenciasPorAluno(matriculaAluno);

        Map<String, List<Nota>> notasPorDisciplina = notasDoAluno.stream()
                .collect(Collectors.groupingBy(nota -> nota.getDisciplina().getCodigo()));

        Map<String, List<Frequencia>> frequenciasPorDisciplina = frequenciasDoAluno.stream()
                .collect(Collectors.groupingBy(frequencia -> frequencia.getDisciplina().getCodigo()));

        return new HistoricoEscolar(aluno, notasPorDisciplina, frequenciasPorDisciplina);
    }

    public void limparTodosRepositorios() throws IOException {
        alunoRepositorio.limpar();
        avaliacaoRepositorio.limpar();
        disciplinaRepositorio.limpar();
        funcionarioRepositorio.limpar();
        professorRepositorio.limpar();
        responsavelRepositorio.limpar();
        turmaRepositorio.limpar();
        ocorrenciaRepositorio.limpar();
        calendarioEscolarRepositorio.limpar();
        disciplinaTurmaRepositorio.limpar();
        notaRepositorio.limpar();
        frequenciaRepositorio.limpar();
        serieEscolarRepositorio.limpar();
    }
}