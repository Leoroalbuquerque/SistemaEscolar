package br.com.escola.negocio;

import br.com.escola.util.DiarioClasse;
import java.util.List;
import java.util.Map;

public class Professor extends Pessoa {
    private String numeroRegistro;
    private String departamento;
    private List<Disciplina> disciplinasLecionadas;

    public void lancarNota(Aluno aluno, Disciplina disciplina, double valor, String tipo) {
    }

    public void registrarFrequencia(Turma turma, Map<Aluno, Boolean> presencas) {
    }

    public DiarioClasse gerarDiarioClasse(Turma turma, Disciplina disciplina) {
        return null;
    }

    public String getNumeroRegistro() {
        return numeroRegistro;
    }

    public void setNumeroRegistro(String numeroRegistro) {
        this.numeroRegistro = numeroRegistro;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public List<Disciplina> getDisciplinasLecionadas() {
        return disciplinasLecionadas;
    }

    public void setDisciplinasLecionadas(List<Disciplina> disciplinasLecionadas) {
        this.disciplinasLecionadas = disciplinasLecionadas;
    }
}