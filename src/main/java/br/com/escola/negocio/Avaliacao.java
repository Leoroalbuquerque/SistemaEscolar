package br.com.escola.negocio;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Avaliacao implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String nomeAvaliacao;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private Disciplina disciplina;

    public Avaliacao() {
    }

    public Avaliacao(String id, String nomeAvaliacao, LocalDate dataInicio, LocalDate dataFim, Disciplina disciplina) {
        this.id = id;
        this.nomeAvaliacao = nomeAvaliacao;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.disciplina = disciplina;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNomeAvaliacao() {
        return nomeAvaliacao;
    }

    public void setNomeAvaliacao(String nomeAvaliacao) {
        this.nomeAvaliacao = nomeAvaliacao;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(Disciplina disciplina) {
        this.disciplina = disciplina;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Avaliacao avaliacao = (Avaliacao) o;
        return Objects.equals(id, avaliacao.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return nomeAvaliacao + " (ID: " + id + ")";
    }
}