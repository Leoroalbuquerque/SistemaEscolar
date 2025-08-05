package br.com.escola.negocio;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonBackReference;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DisciplinaTurma implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private Disciplina disciplina;
    @JsonBackReference("professor-disciplinas")
    private Professor professor;
    @JsonBackReference("turma-disciplinas")
    private Turma turma;

    public DisciplinaTurma() {
        this.id = UUID.randomUUID().toString();
    }

    public DisciplinaTurma(Disciplina disciplina, Turma turma, int par) {
        this();
        this.disciplina = disciplina;
        this.turma = turma;
        this.professor = null;
    }

    public DisciplinaTurma(Disciplina disciplina, Professor professor, Turma turma) {
        this();
        this.disciplina = disciplina;
        this.professor = professor;
        this.turma = turma;
    }

    public DisciplinaTurma(String id, Disciplina disciplina, Professor professor, Turma turma) {
        this.id = id;
        this.disciplina = disciplina;
        this.professor = professor;
        this.turma = turma;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(Disciplina disciplina) {
        this.disciplina = disciplina;
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    public Turma getTurma() {
        return turma;
    }

    public void setTurma(Turma turma) {
        this.turma = turma;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DisciplinaTurma that = (DisciplinaTurma) o;
        return Objects.equals(id, that.id) ||
               (id == null && that.id == null &&
                Objects.equals(disciplina, that.disciplina) &&
                Objects.equals(professor, that.professor) &&
                Objects.equals(turma, that.turma));
    }

    @Override
    public int hashCode() {
        return Objects.hash(id != null ? id : Objects.hash(disciplina, professor, turma));
    }

    @Override
    public String toString() {
        String disciplinaNome = (disciplina != null ? disciplina.getNome() : "N/A");
        String professorNome = (professor != null ? professor.getNome() : "N/A");
        String turmaNome = (turma != null ? turma.getNomeTurma() : "N/A");
        return "DisciplinaTurma{" +
                "id='" + id + '\'' +
                ", disciplina=" + disciplinaNome +
                ", professor=" + professorNome +
                ", turma=" + turmaNome +
                '}';
    }
}