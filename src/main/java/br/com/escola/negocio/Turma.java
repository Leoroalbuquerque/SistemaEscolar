package br.com.escola.negocio;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Turma implements Serializable {
    private static final long serialVersionUID = 1L;

    private String codigo;
    private String nomeTurma;
    private int anoLetivo;
    private Professor professorCoordenador;
    private List<Disciplina> disciplinas;
    private List<Aluno> alunosMatriculados;

    // CONSTRUTOR PADRÃO ADICIONADO AQUI!
    public Turma() {
        // Inicializa as listas para evitar NullPointerExceptions ao desserializar JSON vazio
        this.disciplinas = new ArrayList<>();
        this.alunosMatriculados = new ArrayList<>();
    }

    public Turma(String codigo, String nomeTurma, int anoLetivo, Professor professorCoordenador) {
        this(); // Chama o construtor padrão para inicializar as listas
        this.codigo = codigo;
        this.nomeTurma = nomeTurma;
        this.anoLetivo = anoLetivo;
        this.professorCoordenador = professorCoordenador;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNomeTurma() {
        return nomeTurma;
    }

    public void setNomeTurma(String nomeTurma) {
        this.nomeTurma = nomeTurma;
    }

    public int getAnoLetivo() {
        return anoLetivo;
    }

    public void setAnoLetivo(int anoLetivo) {
        this.anoLetivo = anoLetivo;
    }

    public Professor getProfessorCoordenador() {
        return professorCoordenador;
    }

    public void setProfessorCoordenador(Professor professorCoordenador) {
        this.professorCoordenador = professorCoordenador;
    }

    public List<Disciplina> getDisciplinas() {
        return new ArrayList<>(disciplinas);
    }

    public void setDisciplinas(List<Disciplina> disciplinas) { // Adicionado setter para Gson
        this.disciplinas = disciplinas != null ? new ArrayList<>(disciplinas) : new ArrayList<>();
    }

    public void adicionarDisciplina(Disciplina disciplina) {
        if (disciplina != null && !this.disciplinas.contains(disciplina)) {
            this.disciplinas.add(disciplina);
        }
    }

    public void removerDisciplina(Disciplina disciplina) {
        if (disciplina != null) {
            this.disciplinas.remove(disciplina);
        }
    }

    public List<Aluno> getAlunosMatriculados() {
        return new ArrayList<>(alunosMatriculados);
    }

    public void setAlunosMatriculados(List<Aluno> alunosMatriculados) { // Adicionado setter para Gson
        this.alunosMatriculados = alunosMatriculados != null ? new ArrayList<>(alunosMatriculados) : new ArrayList<>();
    }

    public void matricularAluno(Aluno aluno) {
        if (aluno != null && !this.alunosMatriculados.contains(aluno)) {
            this.alunosMatriculados.add(aluno);
        }
    }

    public void desmatricularAluno(Aluno aluno) {
        if (aluno != null) {
            this.alunosMatriculados.remove(aluno);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Turma turma = (Turma) o;
        return Objects.equals(codigo, turma.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }

    @Override
    public String toString() {
        return "Turma{" +
                "codigo='" + codigo + '\'' +
                ", nomeTurma='" + nomeTurma + '\'' +
                ", anoLetivo=" + anoLetivo +
                ", professorCoordenador=" + (professorCoordenador != null ? professorCoordenador.getNome() : "N/A") +
                ", numDisciplinas=" + disciplinas.size() +
                ", numAlunos=" + alunosMatriculados.size() +
                '}';
    }
}