package br.com.escola.negocio;

import java.time.LocalDate;

public class Frequencia {
    private LocalDate data;
    private boolean presenca;
    private String justificativa;
    private Disciplina disciplina;
    private Aluno aluno;

    public Frequencia(LocalDate data, boolean presenca, String justificativa, Disciplina disciplina, Aluno aluno) {
        this.data = data;
        this.presenca = presenca;
        this.justificativa = justificativa;
        this.disciplina = disciplina;
        this.aluno = aluno;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Frequencia that = (Frequencia) o;
        return data.equals(that.data) &&
               aluno.getMatricula().equals(that.aluno.getMatricula()) &&
               disciplina.getCodigo().equals(that.disciplina.getCodigo());
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(data, aluno.getMatricula(), disciplina.getCodigo());
    }

    @Override
    public String toString() {
        return "Frequencia{" +
               "data=" + data +
               ", presenca=" + presenca +
               ", justificativa='" + justificativa + '\'' +
               ", disciplina=" + (disciplina != null ? disciplina.getNome() : "N/A") +
               ", aluno=" + (aluno != null ? aluno.getNome() : "N/A") +
               '}';
    }
}