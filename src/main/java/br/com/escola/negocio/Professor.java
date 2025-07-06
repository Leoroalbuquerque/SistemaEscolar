package br.com.escola.negocio;

import br.com.escola.util.DiarioClasse;
import java.util.List;
import java.util.Map;

public class Professor extends Pessoa {
    private String numeroRegistro;
    private String departamento;
    private List<Disciplina> disciplinasLecionadas;

    public Professor(String nome, String cpf, String numeroRegistro, String departamento) {
        super(nome, cpf);
        this.numeroRegistro = numeroRegistro;
        this.departamento = departamento;
    }

    public Professor() {
        super();
    }

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

    @Override
    public void exibirInformacoes() {
        System.out.println("Tipo: Professor");
        System.out.println("Nome: " + getNome());
        System.out.println("CPF: " + getCpf());
        System.out.println("Nº Registro: " + this.numeroRegistro);
        System.out.println("Departamento: " + this.departamento);
    }
}