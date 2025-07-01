package br.com.escola.negocio;

import br.com.escola.util.BoletimEscolar;
import br.com.escola.util.HistoricoEscolar;
import java.util.List;

public class Aluno extends Pessoa {
    private String matricula;
    private int anoLetivo;
    private List<Nota> notas;
    private List<Frequencia> frequencia;
    private List<Ocorrencia> ocorrencias;
    private List<Responsavel> responsaveis;

    public double calcularMedia(Disciplina disciplina) {
        return 0.0;
    }

    public double calcularFrequencia(Disciplina disciplina) {
        return 0.0;
    }

    public String verificarSituacaoAprovacao() {
        return null;
    }

    public BoletimEscolar gerarBoletim() {
        return null;
    }

    public HistoricoEscolar gerarHistorico() {
        return null;
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

    public List<Nota> getNotas() {
        return notas;
    }

    public void setNotas(List<Nota> notas) {
        this.notas = notas;
    }

    public List<Frequencia> getFrequencia() {
        return frequencia;
    }

    public void setFrequencia(List<Frequencia> frequencia) {
        this.frequencia = frequencia;
    }

    public List<Ocorrencia> getOcorrencias() {
        return ocorrencias;
    }

    public void setOcorrencias(List<Ocorrencia> ocorrencias) {
        this.ocorrencias = ocorrencias;
    }

    public List<Responsavel> getResponsaveis() {
        return responsaveis;
    }

    public void setResponsaveis(List<Responsavel> responsaveis) {
        this.responsaveis = responsaveis;
    }
}