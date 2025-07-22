package br.com.escola.negocio;

import java.time.LocalDate;

public class PessoaFactory {

    public static Pessoa criarPessoa(TipoPessoa tipo) {
        if (tipo == null) {
            throw new IllegalArgumentException("O tipo de pessoa não pode ser nulo.");
        }

        switch (tipo) {
            case ALUNO:
                return new Aluno();
            case PROFESSOR:
                return new Professor();
            case RESPONSAVEL:
                return new Responsavel();
            case FUNCIONARIO:
                return new Funcionario();
            default:
                throw new IllegalArgumentException("Tipo de pessoa desconhecido: " + tipo);
        }
    }

    public static Aluno criarAluno(String nome, String cpf, String telefone, String email, String matricula, LocalDate dataNascimento, int anoLetivo, java.util.List<String> cpfsResponsaveis) {
        return new Aluno(nome, cpf, telefone, email, matricula, dataNascimento, anoLetivo, cpfsResponsaveis);
    }

    public static Responsavel criarResponsavel(String nome, String cpfPessoa, String telefonePessoa, String emailPessoa, String parentesco, String cpfResponsavel, boolean principal) {
        return new Responsavel(nome, cpfPessoa, telefonePessoa, emailPessoa, parentesco, cpfResponsavel, principal);
    }
    
    public static Responsavel criarResponsavel(String nome, String cpfPessoa, String telefonePessoa, String cpfResponsavel) {
        return new Responsavel(nome, cpfPessoa, telefonePessoa, cpfResponsavel);
    }
}