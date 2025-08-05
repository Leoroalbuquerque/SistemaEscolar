package br.com.escola.negocio;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "registroFuncional")
public class Professor extends Pessoa implements Serializable {
    private static final long serialVersionUID = 1L;

    private String registroFuncional;
    private String especialidade;
    private double salario;
    @JsonManagedReference("professor-disciplinas")
    private List<DisciplinaTurma> disciplinasLecionadas;

    public Professor() {
        super();
        this.disciplinasLecionadas = new ArrayList<>();
    }

    public Professor(String nome, String cpf, String telefone, String email,
                     String registroFuncional, String especialidade, double salario) {
        super(nome, cpf, telefone, email);
        this.registroFuncional = registroFuncional;
        this.especialidade = especialidade;
        this.salario = salario;
        this.disciplinasLecionadas = new ArrayList<>();
    }

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

    public List<DisciplinaTurma> getDisciplinasLecionadas() {
        return new ArrayList<>(disciplinasLecionadas);
    }

    public void adicionarDisciplinaLecionada(DisciplinaTurma disciplinaTurma) {
        if (disciplinaTurma != null && !this.disciplinasLecionadas.contains(disciplinaTurma)) {
            this.disciplinasLecionadas.add(disciplinaTurma);
        }
    }

    public void removerDisciplinaLecionada(DisciplinaTurma disciplinaTurma) {
        if (disciplinaTurma != null) {
            this.disciplinasLecionadas.remove(disciplinaTurma);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
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