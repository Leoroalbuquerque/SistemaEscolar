package br.com.escola.negocio;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Turma implements Serializable {
    private static final long serialVersionUID = 1L;

    private String codigo;
    private String nomeTurma;
    private int anoLetivo;
    private SerieEscolar serieEscolar;
    private String turno;

    @JsonManagedReference("turma-disciplinas")
    private List<DisciplinaTurma> atribuicoesDisciplinaTurma;
    private List<Aluno> alunosMatriculados;
    private List<Professor> professoresAssociados;

    public Turma() {
        this.atribuicoesDisciplinaTurma = new ArrayList<>();
        this.alunosMatriculados = new ArrayList<>();
        this.professoresAssociados = new ArrayList<>();
    }

    public Turma(String codigo, String nomeTurma, int anoLetivo, SerieEscolar serieEscolar, String turno) {
        this();
        this.codigo = codigo;
        this.nomeTurma = nomeTurma;
        this.anoLetivo = anoLetivo;
        this.serieEscolar = serieEscolar;
        this.turno = turno;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNomeTurma() {
        return nomeTurma;
    }

    public void setNomeTurma(String nomeTurma) {
        this.nomeTurma = nomeTurma;
    }

    public int getAnoLetivo() {
        return anoLetivo;
    }

    public void setAnoLetivo(int anoLetivo) {
        this.anoLetivo = anoLetivo;
    }

    public SerieEscolar getSerieEscolar() {
        return serieEscolar;
    }

    public void setSerieEscolar(SerieEscolar serieEscolar) {
        this.serieEscolar = serieEscolar;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public List<Professor> getProfessoresAssociados() {
        return new ArrayList<>(professoresAssociados);
    }

    public void adicionarProfessor(Professor professor) {
        if (professor != null && !this.professoresAssociados.contains(professor)) {
            this.professoresAssociados.add(professor);
        }
    }

    public void removerProfessor(Professor professor) {
        this.professoresAssociados.remove(professor);
    }

    public List<DisciplinaTurma> getAtribuicoesDisciplinaTurma() {
        return new ArrayList<>(atribuicoesDisciplinaTurma);
    }

    public List<Disciplina> getDisciplinas() {
        return atribuicoesDisciplinaTurma.stream()
                .map(DisciplinaTurma::getDisciplina)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public void adicionarAtribuicaoDisciplinaTurma(DisciplinaTurma disciplinaTurma) {
        if (disciplinaTurma != null && !this.atribuicoesDisciplinaTurma.contains(disciplinaTurma)) {
            this.atribuicoesDisciplinaTurma.add(disciplinaTurma);
        }
    }

    public void removerAtribuicaoDisciplinaTurma(DisciplinaTurma disciplinaTurma) {
        this.atribuicoesDisciplinaTurma.remove(disciplinaTurma);
    }

    public List<Aluno> getAlunosMatriculados() {
        return new ArrayList<>(alunosMatriculados);
    }

    public void matricularAluno(Aluno aluno) {
        if (aluno != null && !this.alunosMatriculados.contains(aluno)) {
            this.alunosMatriculados.add(aluno);
        }
    }

    public void desmatricularAluno(Aluno aluno) {
        this.alunosMatriculados.remove(aluno);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Turma turma = (Turma) o;
        return Objects.equals(codigo, turma.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }

    @Override
    public String toString() {
        return "Turma{" +
                "codigo='" + codigo + '\'' +
                ", nomeTurma='" + nomeTurma + '\'' +
                ", anoLetivo=" + anoLetivo +
                ", serieEscolar=" + (serieEscolar != null ? serieEscolar.getCodigoSerie() : "N/A") +
                ", turno='" + turno + '\'' +
                '}';
    }
}