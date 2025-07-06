package br.com.escola.fachada;

import br.com.escola.negocio.AlunoServico;

public class EscolaFachada {
    private static EscolaFachada instance;

    private AlunoServico alunoServico;

    private EscolaFachada() {
        this.alunoServico = new AlunoServico();
    }

    public static EscolaFachada getInstance() {
        if (instance == null) {
            instance = new EscolaFachada();
        }
        return instance;
    }

    public AlunoServico getAlunoServico() {
        return alunoServico;
    }
}