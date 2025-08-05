package br.com.escola.negocio;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Aluno extends Pessoa implements Serializable {
    private static final long serialVersionUID = 1L;

    private String matricula;
    private int anoLetivo;
    private List<String> cpfsResponsaveis;
    private LocalDate dataNascimento;

    public Aluno() {
        super();
        this.cpfsResponsaveis = new ArrayList<>();
    }

    public Aluno(String nome, String cpf, String telefone, String email, String matricula, LocalDate dataNascimento, int anoLetivo) {
        super(nome, cpf, telefone, email);
        this.matricula = matricula;
        this.dataNascimento = dataNascimento;
        this.anoLetivo = anoLetivo;
        this.cpfsResponsaveis = new ArrayList<>();
    }

    public Aluno(String nome, String cpf, String telefone, String email, String matricula, LocalDate dataNascimento, int anoLetivo, List<String> cpfsResponsaveis) {
        super(nome, cpf, telefone, email);
        this.matricula = matricula;
        this.dataNascimento = dataNascimento;
        this.anoLetivo = anoLetivo;
        this.cpfsResponsaveis = cpfsResponsaveis != null ? new ArrayList<>(cpfsResponsaveis) : new ArrayList<>();
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

    public List<String> getCpfsResponsaveis() {
        return cpfsResponsaveis;
    }

    public void setCpfsResponsaveis(List<String> cpfsResponsaveis) {
        this.cpfsResponsaveis = cpfsResponsaveis != null ? new ArrayList<>(cpfsResponsaveis) : new ArrayList<>();
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public void adicionarResponsavel(String cpfResponsavel) {
        if (cpfResponsavel != null && !cpfResponsavel.trim().isEmpty() && !this.cpfsResponsaveis.contains(cpfResponsavel)) {
            this.cpfsResponsaveis.add(cpfResponsavel);
        }
    }

    public void removerResponsavel(String cpfResponsavel) {
        if (cpfResponsavel != null) {
            this.cpfsResponsaveis.remove(cpfResponsavel);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Aluno aluno = (Aluno) o;
        return Objects.equals(matricula, aluno.matricula);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), matricula);
    }

    @Override
    public String toString() {
        return "Aluno{" +
                "nome='" + getNome() + '\'' +
                ", matricula='" + matricula + '\'' +
                ", anoLetivo=" + anoLetivo +
                ", dataNascimento=" + dataNascimento +
                ", cpfsResponsaveis=" + cpfsResponsaveis +
                '}';
    }

    public String toLine() {
        String responsaveisStr = String.join(",", this.cpfsResponsaveis);
        String dataNascimentoStr = (dataNascimento != null) ? dataNascimento.toString() : "";
        
        return getNome() + "|" + getCpf() + "|" + getTelefone() + "|" + getEmail() + "|" +
                matricula + "|" + anoLetivo + "|" + dataNascimentoStr + "|" + responsaveisStr;
    }

    public static Aluno fromLine(String line) {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }
        String[] parts = line.split("\\|");
        if (parts.length < 8) {
            System.err.println("Linha mal formatada para Aluno: " + line + " - Formato esperado: nome|cpf|telefone|email|matricula|anoLetivo|dataNascimento|cpfResponsavel1,cpfResponsavel2,...");
            return null;
        }

        try {
            String nome = parts[0];
            String cpf = parts[1];
            String telefone = parts[2];
            String email = parts[3];
            String matricula = parts[4];
            int anoLetivo = Integer.parseInt(parts[5]);
            LocalDate dataNascimento = null;
            if (parts[6] != null && !parts[6].trim().isEmpty()) {
                dataNascimento = LocalDate.parse(parts[6]);
            }
            
            List<String> cpfsResponsaveis = new ArrayList<>();
            if (parts.length > 7 && parts[7] != null && !parts[7].trim().isEmpty()) {
                cpfsResponsaveis = Arrays.asList(parts[7].split(","));
            }

            return new Aluno(nome, cpf, telefone, email, matricula, dataNascimento, anoLetivo, cpfsResponsaveis);
        } catch (NumberFormatException e) {
            System.err.println("Erro de formato numérico ao parsear anoLetivo em Aluno: " + line + " - " + e.getMessage());
            return null;
        } catch (java.time.format.DateTimeParseException e) {
            System.err.println("Erro de formato de data ao parsear dataNascimento em Aluno: " + line + " - " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println("Erro ao parsear linha para Aluno: " + line + " - " + e.getMessage());
            return null;
        }
    }

    public String getResponsavel() {
        if (this.cpfsResponsaveis != null && !this.cpfsResponsaveis.isEmpty()) {
            return this.cpfsResponsaveis.get(0);
        }
        return "";
    }
}