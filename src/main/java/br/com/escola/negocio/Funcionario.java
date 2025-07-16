package br.com.escola.negocio;

import java.io.Serializable;
import java.util.Objects;

public class Funcionario extends Pessoa implements Serializable {
    private static final long serialVersionUID = 1L;

    private String cargo;
    private String matriculaFuncional;
    private double salario;

    public Funcionario() {
        super(); // Chama o construtor padrão de Pessoa
    }

    public Funcionario(String nome, String cpf, String telefone, String email, 
                       String cargo, String matriculaFuncional, double salario) {
        super(nome, cpf, telefone, email); // Chama o construtor completo de Pessoa
        this.cargo = cargo;
        this.matriculaFuncional = matriculaFuncional;
        this.salario = salario;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getMatriculaFuncional() {
        return matriculaFuncional;
    }

    public void setMatriculaFuncional(String matriculaFuncional) {
        this.matriculaFuncional = matriculaFuncional;
    }

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false; 
        Funcionario that = (Funcionario) o;
        return Objects.equals(matriculaFuncional, that.matriculaFuncional);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), matriculaFuncional);
    }

    @Override
    public String toString() {
        return "Funcionario{" +
               "nome='" + getNome() + '\'' +
               ", matriculaFuncional='" + matriculaFuncional + '\'' +
               ", cargo='" + cargo + '\'' +
               '}';
    }
}