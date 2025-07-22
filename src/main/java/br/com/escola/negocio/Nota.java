package br.com.escola.negocio;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class Nota implements Serializable {
    private static final long serialVersionUID = 1L;

    private double valor;
    private String tipoAvaliacao;
    private Date dataLancamento;
    private Disciplina disciplina;
    private Aluno aluno;

    public Nota() {
    }

    public Nota(double valor, String tipoAvaliacao, Date dataLancamento, Disciplina disciplina, Aluno aluno) {
        this.valor = valor;
        this.tipoAvaliacao = tipoAvaliacao;
        this.dataLancamento = dataLancamento;
        this.disciplina = disciplina;
        this.aluno = aluno;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getTipoAvaliacao() {
        return tipoAvaliacao;
    }

    public void setTipoAvaliacao(String tipoAvaliacao) {
        this.tipoAvaliacao = tipoAvaliacao;
    }

    public Date getDataLancamento() {
        return dataLancamento;
    }

    public void setDataLancamento(Date dataLancamento) {
        this.dataLancamento = dataLancamento;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Nota nota = (Nota) o;
        return Double.compare(nota.valor, valor) == 0 &&
               Objects.equals(tipoAvaliacao, nota.tipoAvaliacao) &&
               Objects.equals(dataLancamento, nota.dataLancamento) &&
               Objects.equals(disciplina, nota.disciplina) &&
               Objects.equals(aluno, nota.aluno);
    }

    @Override
    public int hashCode() {
        return Objects.hash(valor, tipoAvaliacao, dataLancamento, disciplina, aluno);
    }

    @Override
    public String toString() {
        String alunoNome = (aluno != null) ? aluno.getNome() : "N/A";
        String disciplinaNome = (disciplina != null) ? disciplina.getNome() : "N/A";
        return "Nota{" +
                "valor=" + valor +
                ", tipoAvaliacao='" + tipoAvaliacao + '\'' +
                ", dataLancamento=" + dataLancamento +
                ", disciplina='" + disciplinaNome + '\'' +
                ", aluno='" + alunoNome + '\'' +
                '}';
    }
}