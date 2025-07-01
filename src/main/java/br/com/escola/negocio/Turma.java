package br.com.escola.negocio;

import java.util.List;

public class Turma {
    private String codigoTurma;
    private int anoLetivo;
    private Professor professorResponsavel;
    private List<Aluno> alunos;
    private List<Disciplina> disciplinas;

    public void adicionarAluno(Aluno aluno) {}
    public void removerAluno(Aluno aluno) {}
    public void adicionarDisciplina(Disciplina disciplina) {}
    public void removerDisciplina(Disciplina disciplina) {}

    public String getCodigoTurma() {
        return codigoTurma;
    }

    public void setCodigoTurma(String codigoTurma) {
        this.codigoTurma = codigoTurma;
    }

    public int getAnoLetivo() {
        return anoLetivo;
    }

    public void setAnoLetivo(int anoLetivo) {
        this.anoLetivo = anoLetivo;
    }

    public Professor getProfessorResponsavel() {
        return professorResponsavel;
    }

    public void setProfessorResponsavel(Professor professorResponsavel) {
        this.professorResponsavel = professorResponsavel;
    }

    public List<Aluno> getAlunos() {
        return alunos;
    }

    public void setAlunos(List<Aluno> alunos) {
        this.alunos = alunos;
    }

    public List<Disciplina> getDisciplinas() {
        return disciplinas;
    }

    public void setDisciplinas(List<Disciplina> disciplinas) {
        this.disciplinas = disciplinas;
    }
}