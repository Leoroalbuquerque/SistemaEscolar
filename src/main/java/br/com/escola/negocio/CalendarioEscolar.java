package br.com.escola.negocio;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CalendarioEscolar implements Serializable {
    private static final long serialVersionUID = 1L;

    private int anoLetivo;
    private List<LocalDate> datasLetivas;
    private List<LocalDate> feriados;
    private List<Avaliacao> avaliacoes;

    public CalendarioEscolar() {
        this.datasLetivas = new ArrayList<>();
        this.feriados = new ArrayList<>();
        this.avaliacoes = new ArrayList<>();
    }

    public CalendarioEscolar(int anoLetivo) {
        this();
        this.anoLetivo = anoLetivo;
    }

    public int getAnoLetivo() {
        return anoLetivo;
    }

    public void setAnoLetivo(int anoLetivo) {
        this.anoLetivo = anoLetivo;
    }

    public List<LocalDate> getDatasLetivas() {
        return datasLetivas;
    }

    public void setDatasLetivas(List<LocalDate> datasLetivas) {
        this.datasLetivas = datasLetivas;
    }

    public List<LocalDate> getFeriados() {
        return feriados;
    }

    public void setFeriados(List<LocalDate> feriados) {
        this.feriados = feriados;
    }

    public List<Avaliacao> getAvaliacoes() {
        return avaliacoes;
    }

    public void setAvaliacoes(List<Avaliacao> avaliacoes) {
        this.avaliacoes = avaliacoes;
    }

    public void adicionarDataLetiva(LocalDate data) {
        if (data != null && !this.datasLetivas.contains(data)) {
            this.datasLetivas.add(data);
        }
    }

    public void removerDataLetiva(LocalDate data) {
        if (data != null) {
            this.datasLetivas.remove(data);
        }
    }

    public void adicionarFeriado(LocalDate data) {
        if (data != null && !this.feriados.contains(data)) {
            this.feriados.add(data);
        }
    }

    public void removerFeriado(LocalDate data) {
        if (data != null) {
            this.feriados.remove(data);
        }
    }

    public void registrarAvaliacao(Avaliacao avaliacao) {
        if (avaliacao != null && !this.avaliacoes.contains(avaliacao)) {
            this.avaliacoes.add(avaliacao);
        }
    }

    public void removerAvaliacao(Avaliacao avaliacao) {
        if (avaliacao != null) {
            this.avaliacoes.remove(avaliacao);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CalendarioEscolar that = (CalendarioEscolar) o;
        return anoLetivo == that.anoLetivo;
    }

    @Override
    public int hashCode() {
        return Objects.hash(anoLetivo);
    }

    @Override
    public String toString() {
        return "CalendarioEscolar{" +
                "anoLetivo=" + anoLetivo +
                ", datasLetivas=" + datasLetivas.size() + " datas" +
                ", feriados=" + feriados.size() + " feriados" +
                ", avaliacoes=" + avaliacoes.size() + " avaliações" +
                '}';
    }
}