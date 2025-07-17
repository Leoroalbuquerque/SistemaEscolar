package br.com.escola.negocio;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Aluno extends Pessoa implements Serializable {
    private static final long serialVersionUID = 1L;

    private String matricula;
    private int anoLetivo;
    private List<String> cpfsResponsaveis;

    public Aluno() {
        super();
        this.cpfsResponsaveis = new ArrayList<>();
    }

    public Aluno(String nome, String cpf, String telefone, String email, String matricula, int anoLetivo) {
        super(nome, cpf, telefone, email);
        this.matricula = matricula;
        this.anoLetivo = anoLetivo;
        this.cpfsResponsaveis = new ArrayList<>();
    }

    public Aluno(String nome, String cpf, String telefone, String email, String matricula, int anoLetivo, List<String> cpfsResponsaveis) {
        super(nome, cpf, telefone, email);
        this.matricula = matricula;
        this.anoLetivo = anoLetivo;
        this.cpfsResponsaveis = cpfsResponsaveis != null ? new ArrayList<>(cpfsResponsaveis) : new ArrayList<>();
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public int getAnoLetivo() {
        return anoLetivo;
    }

    public void setAnoLetivo(int anoLetivo) {
        this.anoLetivo = anoLetivo;
    }

    public List<String> getCpfsResponsaveis() {
        return cpfsResponsaveis;
    }

    public void setCpfsResponsaveis(List<String> cpfsResponsaveis) {
        this.cpfsResponsaveis = cpfsResponsaveis != null ? new ArrayList<>(cpfsResponsaveis) : new ArrayList<>();
    }

    public void adicionarResponsavel(String cpfResponsavel) {
        if (cpfResponsavel != null && !cpfResponsavel.trim().isEmpty() && !this.cpfsResponsaveis.contains(cpfResponsavel)) {
            this.cpfsResponsaveis.add(cpfResponsavel);
        }
    }

    public void removerResponsavel(String cpfResponsavel) {
        if (cpfResponsavel != null) {
            this.cpfsResponsaveis.remove(cpfResponsavel);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Aluno aluno = (Aluno) o;
        return Objects.equals(matricula, aluno.matricula);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), matricula);
    }

    @Override
    public String toString() {
        return "Aluno{" +
               "nome='" + getNome() + '\'' +
               ", matricula='" + matricula + '\'' +
               ", anoLetivo=" + anoLetivo +
               ", cpfsResponsaveis=" + cpfsResponsaveis +
               '}';
    }
}
