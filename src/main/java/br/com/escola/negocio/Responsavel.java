package br.com.escola.negocio;

import java.io.Serializable;
import java.util.Objects;

public class Responsavel extends Pessoa implements Serializable {
    private static final long serialVersionUID = 1L;

    private String parentesco;
    private String cpfResponsavel;
    private boolean principal;

    public Responsavel() {
        super();
    }

    public Responsavel(String nome, String cpf, String telefone, String email,
                       String parentesco, String cpfResponsavel, boolean principal) {
        super(nome, cpf, telefone, email);
        this.parentesco = parentesco;
        this.cpfResponsavel = cpfResponsavel;
        this.principal = principal;
    }

    public String getParentesco() {
        return parentesco;
    }

    public void setParentesco(String parentesco) {
        this.parentesco = parentesco;
    }

    public String getCpfResponsavel() {
        return cpfResponsavel;
    }

    public void setCpfResponsavel(String cpfResponsavel) {
        this.cpfResponsavel = cpfResponsavel;
    }

    public boolean isPrincipal() {
        return principal;
    }

    public void setPrincipal(boolean principal) {
        this.principal = principal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Responsavel that = (Responsavel) o;
        return Objects.equals(cpfResponsavel, that.cpfResponsavel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), cpfResponsavel);
    }

    @Override
    public String toString() {
        return "Responsavel{" +
               "nome='" + getNome() + '\'' +
               ", cpf='" + getCpf() + '\'' +
               ", telefone='" + getTelefone() + '\'' +
               ", email='" + getEmail() + '\'' +
               ", parentesco='" + parentesco + '\'' +
               ", cpfResponsavel='" + cpfResponsavel + '\'' +
               ", principal=" + principal +
               '}';
    }
}
