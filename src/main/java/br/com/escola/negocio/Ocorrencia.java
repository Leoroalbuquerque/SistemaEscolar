package br.com.escola.negocio;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Ocorrencia implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private LocalDateTime dataHora;
    private String descricao;
    private String registradorId;
    private Aluno aluno;
    private String medidasTomadas;
    private boolean encerrada;

    public Ocorrencia() {
    }

    public Ocorrencia(String id, LocalDateTime dataHora, String registradorId, Aluno aluno, String descricao, String medidasTomadas) {
        this.id = id;
        this.dataHora = dataHora;
        this.descricao = descricao;
        this.registradorId = registradorId;
        this.aluno = aluno;
        this.medidasTomadas = "";
        this.encerrada = false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getRegistradorId() {
        return registradorId;
    }

    public void setRegistradorId(String registradorId) {
        this.registradorId = registradorId;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public String getMedidasTomadas() {
        return medidasTomadas;
    }

    public void setMedidasTomadas(String medidasTomadas) {
        this.medidasTomadas = medidasTomadas;
    }

    public boolean isEncerrada() {
        return encerrada;
    }

    public void setEncerrada(boolean encerrada) {
        this.encerrada = encerrada;
    }

    public void registrarMedidas(String medidas) {
        if (this.encerrada) {
            System.out.println("Atenção: Ocorrência já encerrada. Medidas não serão registradas.");
            return;
        }
        this.medidasTomadas = medidas != null ? medidas : "";
    }

    public void encerrarOcorrencia() {
        this.encerrada = true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ocorrencia that = (Ocorrencia) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Ocorrencia{" + "id='" + id + '\'' + ", dataHora=" + dataHora + ", aluno=" + (aluno != null ? aluno.getNome() : "N/A") + ", encerrada=" + encerrada + '}';
    }
}