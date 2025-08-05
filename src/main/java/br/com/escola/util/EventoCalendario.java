package br.com.escola.util;

import java.io.Serializable;
import java.time.LocalDate;

public class EventoCalendario implements Serializable {
    
    private String id;
    private LocalDate data;
    private String descricao;
    private String tipo;

    public EventoCalendario() {
    }

    public EventoCalendario(String id, LocalDate data, String descricao, String tipo) {
        this.id = id;
        this.data = data;
        this.descricao = descricao;
        this.tipo = tipo;
    }
    
    public EventoCalendario(LocalDate data, String descricao, String tipo) {
        this(null, data, descricao, tipo);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return "EventoCalendario{" +
                "id='" + id + '\'' +
                ", data=" + data +
                ", descricao='" + descricao + '\'' +
                ", tipo='" + tipo + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventoCalendario that = (EventoCalendario) o;
        return data.equals(that.data) && descricao.equals(that.descricao) && tipo.equals(that.tipo);
    }
}