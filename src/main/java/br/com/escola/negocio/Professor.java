package br.com.escola.negocio;

import java.io.Serializable;
import java.util.Objects;

public class Professor extends Pessoa implements Serializable {
    private static final long serialVersionUID = 1L;

    private String registroFuncional;
    private String especialidade;
    private double salario;

    public Professor(String nome, String cpf, String telefone, String email, String registroFuncional, String especialidade, double salario) {
        super(nome, cpf, telefone, email);
        this.registroFuncional = registroFuncional;
        this.especialidade = especialidade;
        this.salario = salario;
    }

    // Construtor padrão para desserialização JSON (Importante para Jackson)
    public Professor() {
        super();
    }

    // Getters e Setters
    public String getRegistroFuncional() {
        return registroFuncional;
    }

    public void setRegistroFuncional(String registroFuncional) {
        this.registroFuncional = registroFuncional;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
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
        if (!super.equals(o)) return false; // Inclui a comparação da classe pai
        Professor professor = (Professor) o;
        return Objects.equals(registroFuncional, professor.registroFuncional);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), registroFuncional);
    }

    @Override
    public String toString() {
        return "Professor{" +
               "nome='" + getNome() + '\'' +
               ", registroFuncional='" + registroFuncional + '\'' +
               ", especialidade='" + especialidade + '\'' +
               ", salario=" + salario +
               '}';
    }
}