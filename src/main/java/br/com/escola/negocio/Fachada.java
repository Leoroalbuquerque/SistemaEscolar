package br.com.escola.negocio;

import br.com.escola.dados.AlunoRepositorioJson;
import br.com.escola.dados.DisciplinaRepositorioJson;
import br.com.escola.dados.ProfessorRepositorioJson;
import br.com.escola.dados.TurmaRepositorioJson;
import br.com.escola.dados.FuncionarioRepositorioJson;
import br.com.escola.dados.ResponsavelRepositorioJson;
import br.com.escola.excecoes.DadoInvalidoException;
import br.com.escola.excecoes.EntidadeNaoEncontradaException;

import java.util.List;

public class Fachada {

    private static Fachada instance;
    private AlunoServico alunoServico;
    private ProfessorServico professorServico;
    private DisciplinaServico disciplinaServico;
    private TurmaServico turmaServico;
    private FuncionarioServico funcionarioServico;
    private ResponsavelServico responsavelServico;

    private AlunoRepositorioJson alunoRepositorio;
    private DisciplinaRepositorioJson disciplinaRepositorio;
    private ProfessorRepositorioJson professorRepositorio;
    private TurmaRepositorioJson turmaRepositorio;
    private FuncionarioRepositorioJson funcionarioRepositorio;
    private ResponsavelRepositorioJson responsavelRepositorio;

    private Fachada() {
        this.alunoRepositorio = new AlunoRepositorioJson();
        this.disciplinaRepositorio = new DisciplinaRepositorioJson();
        this.professorRepositorio = new ProfessorRepositorioJson();
        this.turmaRepositorio = new TurmaRepositorioJson();
        this.funcionarioRepositorio = new FuncionarioRepositorioJson();
        this.responsavelRepositorio = new ResponsavelRepositorioJson();

        this.responsavelServico = new ResponsavelServico(this.responsavelRepositorio);
        this.alunoServico = new AlunoServico(this.alunoRepositorio, this.responsavelServico);
        this.professorServico = new ProfessorServico(this.professorRepositorio);
        this.disciplinaServico = new DisciplinaServico(this.disciplinaRepositorio);
        this.turmaServico = new TurmaServico(this.turmaRepositorio, this.alunoServico, this.disciplinaServico, this.professorServico);
        this.funcionarioServico = new FuncionarioServico(this.funcionarioRepositorio);
    }

    public static synchronized Fachada getInstance() {
        if (instance == null) {
            instance = new Fachada();
        }
        return instance;
    }

    public void limparTodosOsDados() {
        this.alunoRepositorio.limpar();
        this.disciplinaRepositorio.limpar();
        this.professorRepositorio.limpar();
        this.turmaRepositorio.limpar();
        this.funcionarioRepositorio.limpar();
        this.responsavelRepositorio.limpar();
        System.out.println("DEBUG: Todos os arquivos JSON foram limpos.");
    }

    public void adicionarAluno(Aluno aluno) throws DadoInvalidoException {
        alunoServico.adicionarAluno(aluno);
    }

    public Aluno buscarAluno(String matricula) throws EntidadeNaoEncontradaException, DadoInvalidoException {
        return alunoServico.buscarAluno(matricula);
    }

    public void atualizarAluno(Aluno aluno) throws DadoInvalidoException, EntidadeNaoEncontradaException {
        alunoServico.atualizarAluno(aluno);
    }

    public boolean deletarAluno(String matricula) throws EntidadeNaoEncontradaException, DadoInvalidoException {
        return alunoServico.deletarAluno(matricula);
    }

    public List<Aluno> listarTodosAlunos() {
        return alunoServico.listarTodosAlunos();
    }

    public void adicionarResponsavelAoAluno(String matriculaAluno, String cpfResponsavel) throws DadoInvalidoException, EntidadeNaoEncontradaException {
        alunoServico.adicionarResponsavelAoAluno(matriculaAluno, cpfResponsavel);
    }

    public void removerResponsavelDoAluno(String matriculaAluno, String cpfResponsavel) throws DadoInvalidoException, EntidadeNaoEncontradaException {
        alunoServico.removerResponsavelDoAluno(matriculaAluno, cpfResponsavel);
    }

    public List<Aluno> buscarAlunosPorAnoLetivo(int anoLetivo) throws DadoInvalidoException {
        return alunoServico.buscarAlunosPorAnoLetivo(anoLetivo);
    }

    public void adicionarProfessor(Professor professor) throws DadoInvalidoException {
        professorServico.adicionarProfessor(professor);
    }

    public Professor buscarProfessor(String registroFuncional) throws EntidadeNaoEncontradaException, DadoInvalidoException {
        return professorServico.buscarProfessor(registroFuncional);
    }

    public void atualizarProfessor(Professor professor) throws DadoInvalidoException, EntidadeNaoEncontradaException {
        professorServico.atualizarProfessor(professor);
    }

    public boolean deletarProfessor(String registroFuncional) throws EntidadeNaoEncontradaException, DadoInvalidoException {
        return professorServico.deletarProfessor(registroFuncional);
    }

    public List<Professor> listarTodosProfessores() {
        return professorServico.listarTodosProfessores();
    }

    public List<Professor> buscarProfessoresPorEspecialidade(String especialidade) throws DadoInvalidoException {
        return professorServico.buscarProfessoresPorEspecialidade(especialidade);
    }

    public void adicionarDisciplina(Disciplina disciplina) throws DadoInvalidoException {
        disciplinaServico.adicionarDisciplina(disciplina);
    }

    public Disciplina buscarDisciplina(String codigo) throws EntidadeNaoEncontradaException, DadoInvalidoException {
        return disciplinaServico.buscarDisciplina(codigo);
    }

    public void atualizarDisciplina(Disciplina disciplina) throws DadoInvalidoException, EntidadeNaoEncontradaException {
        disciplinaServico.atualizarDisciplina(disciplina);
    }

    public boolean deletarDisciplina(String codigo) throws EntidadeNaoEncontradaException, DadoInvalidoException {
        return disciplinaServico.deletarDisciplina(codigo);
    }

    public List<Disciplina> listarTodasDisciplinas() {
        return disciplinaServico.listarTodasDisciplinas();
    }

    public void adicionarTurma(Turma turma) throws DadoInvalidoException, EntidadeNaoEncontradaException {
        turmaServico.adicionarTurma(turma);
    }

    public Turma buscarTurma(String codigo) throws EntidadeNaoEncontradaException, DadoInvalidoException {
        return turmaServico.buscarTurma(codigo);
    }

    public void atualizarTurma(Turma turma) throws DadoInvalidoException, EntidadeNaoEncontradaException {
        turmaServico.atualizarTurma(turma);
    }

    public boolean deletarTurma(String codigo) throws EntidadeNaoEncontradaException, DadoInvalidoException {
        return turmaServico.deletarTurma(codigo);
    }

    public List<Turma> listarTodasTurmas() {
        return turmaServico.listarTodasTurmas();
    }

    public void adicionarDisciplinaNaTurma(String codigoTurma, String codigoDisciplina) throws DadoInvalidoException, EntidadeNaoEncontradaException {
        turmaServico.adicionarDisciplinaNaTurma(codigoTurma, codigoDisciplina);
    }

    public void removerDisciplinaDaTurma(String codigoTurma, String codigoDisciplina) throws DadoInvalidoException, EntidadeNaoEncontradaException {
        turmaServico.removerDisciplinaDaTurma(codigoTurma, codigoDisciplina);
    }

    public void matricularAlunoNaTurma(String codigoTurma, String matriculaAluno) throws DadoInvalidoException, EntidadeNaoEncontradaException {
        turmaServico.matricularAlunoNaTurma(codigoTurma, matriculaAluno);
    }

    public void desmatricularAlunoDaTurma(String codigoTurma, String matriculaAluno) throws DadoInvalidoException, EntidadeNaoEncontradaException {
        turmaServico.desmatricularAlunoDaTurma(codigoTurma, matriculaAluno);
    }

    public void adicionarFuncionario(Funcionario funcionario) throws DadoInvalidoException {
        funcionarioServico.adicionarFuncionario(funcionario);
    }

    public Funcionario buscarFuncionario(String matriculaFuncional) throws EntidadeNaoEncontradaException, DadoInvalidoException {
        return funcionarioServico.buscarFuncionario(matriculaFuncional);
    }

    public void atualizarFuncionario(Funcionario funcionario) throws DadoInvalidoException, EntidadeNaoEncontradaException {
        funcionarioServico.atualizarFuncionario(funcionario);
    }

    public boolean deletarFuncionario(String matriculaFuncional) throws EntidadeNaoEncontradaException, DadoInvalidoException {
        return funcionarioServico.deletarFuncionario(matriculaFuncional);
    }

    public List<Funcionario> listarTodosFuncionarios() {
        return funcionarioServico.listarTodosFuncionarios();
    }

    public List<Funcionario> buscarFuncionariosPorCargo(String cargo) throws DadoInvalidoException {
        return funcionarioServico.buscarFuncionariosPorCargo(cargo);
    }

    public void adicionarResponsavel(Responsavel responsavel) throws DadoInvalidoException {
        responsavelServico.adicionarResponsavel(responsavel);
    }

    public Responsavel buscarResponsavel(String cpfResponsavel) throws EntidadeNaoEncontradaException, DadoInvalidoException {
        return responsavelServico.buscarResponsavel(cpfResponsavel);
    }

    public void atualizarResponsavel(Responsavel responsavel) throws DadoInvalidoException, EntidadeNaoEncontradaException {
        responsavelServico.atualizarResponsavel(responsavel);
    }

    public boolean deletarResponsavel(String cpfResponsavel) throws EntidadeNaoEncontradaException, DadoInvalidoException {
        return responsavelServico.deletarResponsavel(cpfResponsavel);
    }

    public List<Responsavel> listarTodosResponsaveis() {
        return responsavelServico.listarTodosResponsaveis();
    }

    public List<Responsavel> buscarResponsaveisPrincipais() {
        return responsavelServico.buscarResponsaveisPrincipais();
    }
}
