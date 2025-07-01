package br.com.escola.negocio;

import java.util.Date;
import java.util.List;

public class CalendarioEscolar {
    private int anoLetivo;
    private List<Date> datasLetivas;
    private List<Date> feriados;
    private List<Avaliacao> avaliacoes;

    public void adicionarDataLetiva(Date data) {}
    public void adicionarFeriado(Date data) {}
    public void registrarAvaliacao(Avaliacao avaliacao) {}

    public int getAnoLetivo() {
        return anoLetivo;
    }

    public void setAnoLetivo(int anoLetivo) {
        this.anoLetivo = anoLetivo;
    }

    public List<Date> getDatasLetivas() {
        return datasLetivas;
    }

    public void setDatasLetivas(List<Date> datasLetivas) {
        this.datasLetivas = datasLetivas;
    }

    public List<Date> getFeriados() {
        return feriados;
    }

    public void setFeriados(List<Date> feriados) {
        this.feriados = feriados;
    }

    public List<Avaliacao> getAvaliacoes() {
        return avaliacoes;
    }

    public void setAvaliacoes(List<Avaliacao> avaliacoes) {
        this.avaliacoes = avaliacoes;
    }
}