package br.com.escola.excecoes;

public class DadoInvalidoException extends Exception {

    public DadoInvalidoException(String mensagem) {
        super(mensagem);
    }

    public DadoInvalidoException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}