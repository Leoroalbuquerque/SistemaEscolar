package br.com.escola.negocio;

import java.util.Date;

public class Avaliacao {
    private String nome;
    private Date data;
    private Date prazoNotas;
    private Disciplina disciplina;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Date getPrazoNotas() {
        return prazoNotas;
    }

    public void setPrazoNotas(Date prazoNotas) {
        this.prazoNotas = prazoNotas;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(Disciplina disciplina) {
        this.disciplina = disciplina;
    }
}