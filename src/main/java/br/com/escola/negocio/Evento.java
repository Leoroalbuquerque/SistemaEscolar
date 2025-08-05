package br.com.escola.negocio;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public class Evento implements Serializable {
    private static final long serialVersionUID = 1L;

    private UUID id;
    private String descricao;
    private LocalDate data;

    public Evento() {
        this.id = UUID.randomUUID();
    }
    
    public Evento(String descricao, LocalDate data) {
        this();
        this.descricao = descricao;
        this.data = data;
    }

    public UUID getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Evento evento = (Evento) o;
        return Objects.equals(id, evento.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}