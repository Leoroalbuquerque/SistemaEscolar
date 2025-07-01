package br.com.escola.negocio;

import java.util.Date;

public class Ocorrencia {
    private Date data;
    private String descricao;
    private String registrador;
    private Aluno aluno;

    public void registrarMedidas(String medidas) {}
    public void encerrarOcorrencia() {}

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getRegistrador() {
        return registrador;
    }

    public void setRegistrador(String registrador) {
        this.registrador = registrador;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }
}