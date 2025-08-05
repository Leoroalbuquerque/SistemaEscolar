package br.com.escola.negocio;

import java.io.Serializable;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SerieEscolar implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum NivelEscolar {
        INFANTIL,
        FUNDAMENTAL_1,
        FUNDAMENTAL_2,
        MEDIO
    }

    private String codigoSerie;
    private String nomeSerie;
    private NivelEscolar nivel;
    private int ordem;

    public SerieEscolar() {
    }

    public SerieEscolar(String codigoSerie, String nomeSerie, NivelEscolar nivel, int ordem) {
        this.codigoSerie = codigoSerie;
        this.nomeSerie = nomeSerie;
        this.nivel = nivel;
        this.ordem = ordem;
    }

    public String getCodigoSerie() {
        return codigoSerie;
    }

    public void setCodigoSerie(String codigoSerie) {
        this.codigoSerie = codigoSerie;
    }

    public String getNomeSerie() {
        return nomeSerie;
    }

    public void setNomeSerie(String nomeSerie) {
        this.nomeSerie = nomeSerie;
    }

    public NivelEscolar getNivel() {
        return nivel;
    }

    public void setNivel(NivelEscolar nivel) {
        this.nivel = nivel;
    }

    public int getOrdem() {
        return ordem;
    }

    public void setOrdem(int ordem) {
        this.ordem = ordem;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SerieEscolar that = (SerieEscolar) o;
        return Objects.equals(codigoSerie, that.codigoSerie);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigoSerie);
    }

    @Override
    public String toString() {
        return nomeSerie;
    }
}