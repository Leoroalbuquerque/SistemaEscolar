package br.com.escola.negocio;

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
}