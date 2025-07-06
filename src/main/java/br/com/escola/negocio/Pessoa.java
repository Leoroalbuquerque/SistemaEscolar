package br.com.escola.negocio;

import java.util.Objects;

public abstract class Pessoa {
    private String nome;
    private String cpf;

    public Pessoa(String nome, String cpf) {
        this.nome = nome;
        this.cpf = cpf;
    }

    public Pessoa() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public abstract void exibirInformacoes();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pessoa pessoa)) return false; 
        
        return Objects.equals(this.cpf, pessoa.cpf);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.cpf);
    }
}