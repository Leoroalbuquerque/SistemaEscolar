package br.com.escola.util;

import br.com.escola.negocio.Turma;
import br.com.escola.negocio.Disciplina;
import br.com.escola.negocio.Professor;
import br.com.escola.negocio.Aluno;
import br.com.escola.negocio.Frequencia;
import java.util.List;

public class DiarioClasse {
    private Turma turma;
    private Disciplina disciplina;
    private Professor professor;
    private List<Aluno> alunos;
    private List<Frequencia> frequencias;

    public DiarioClasse(Turma turma, Disciplina disciplina, Professor professor, List<Aluno> alunos, List<Frequencia> frequencias) {
        this.turma = turma;
        this.disciplina = disciplina;
        this.professor = professor;
        this.alunos = alunos;
        this.frequencias = frequencias;
    }

    public Turma getTurma() {
        return turma;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public Professor getProfessor() {
        return professor;
    }
    
    public List<Aluno> getAlunos() {
        return alunos;
    }

    public List<Frequencia> getFrequencias() {
        return frequencias;
    }
}