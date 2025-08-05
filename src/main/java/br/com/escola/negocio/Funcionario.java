package br.com.escola.negocio;

import java.io.Serializable;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Funcionario extends Pessoa implements Serializable {
    private static final long serialVersionUID = 1L;

    private String matriculaFuncional;
    private String cargo;
    private double salario;

    public Funcionario() {
        super();
    }

    public Funcionario(String nome, String cpf, String telefone, String email, 
                       String matriculaFuncional,String cargo, double salario) { 
        super(nome, cpf, telefone, email);
        this.matriculaFuncional = matriculaFuncional;
        this.cargo = cargo;
        this.salario = salario;
    }

    public String getMatriculaFuncional() {
        return matriculaFuncional;
    }

    public void setMatriculaFuncional(String matriculaFuncional) {
        this.matriculaFuncional = matriculaFuncional;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
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
                ", salario=" + salario +
                '}';
    }

    public String toLine() {
        return getNome() + "|" + getCpf() + "|" + getTelefone() + "|" + getEmail() + "|" +
               matriculaFuncional + "|" + cargo + "|" + salario;
    }

    public static Funcionario fromLine(String line) {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }
        String[] parts = line.split("\\|");
        if (parts.length < 7) {
            System.err.println("Linha mal formatada para Funcionario: " + line);
            return null;
        }
        try {
            String nome = parts[0];
            String cpf = parts[1];
            String telefone = parts[2];
            String email = parts[3];
            String matriculaFuncional = parts[4];
            String cargo = parts[5];
            double salario = Double.parseDouble(parts[6]);
            return new Funcionario(nome, cpf, telefone, email, matriculaFuncional, cargo, salario);
        } catch (NumberFormatException e) {
            System.err.println("Erro de formato numérico ao parsear salário em Funcionario: " + line + " - " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println("Erro ao parsear linha para Funcionario: " + line + " - " + e.getMessage());
            return null;
        }
    }
}