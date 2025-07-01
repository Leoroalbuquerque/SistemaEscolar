package br.com.escola.negocio;

import java.util.List;

public class Responsavel extends Pessoa {
    private String parentesco;

    public List<Aluno> listarAlunos() {
        return null;
    }

    public void receberNotificacoes(String mensagem) {
    }

    public String getParentesco() {
        return parentesco;
    }

    public void setParentesco(String parentesco) {
        this.parentesco = parentesco;
    }
}