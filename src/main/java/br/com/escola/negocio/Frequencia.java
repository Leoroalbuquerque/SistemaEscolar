package br.com.escola.negocio;

import java.util.Date;

public class Frequencia {
    private Date data;
    private boolean presenca;
    private String justificativa;
    private Disciplina disciplina;
    private Aluno aluno;

    public void justificarFalta(String justificativa) {}
    public void registrarPresenca(boolean presente) {}

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public boolean isPresenca() {
        return presenca;
    }

    public void setPresenca(boolean presenca) {
        this.presenca = presenca;
    }

    public String getJustificativa() {
        return justificativa;
    }

    public void setJustificativa(String justificativa) {
        this.justificativa = justificativa;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(Disciplina disciplina) {
        this.disciplina = disciplina;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }
}