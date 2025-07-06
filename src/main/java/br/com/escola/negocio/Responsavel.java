package br.com.escola.negocio;

import java.util.List;

public class Responsavel extends Pessoa {
    private String parentesco;

    public Responsavel(String nome, String cpf, String parentesco) {
        super(nome, cpf);
        this.parentesco = parentesco;
    }

    public Responsavel() {
        super();
    }

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

    @Override
    public void exibirInformacoes() {
        System.out.println("Tipo: Responsável");
        System.out.println("Nome: " + getNome());
        System.out.println("CPF: " + getCpf());
        System.out.println("Parentesco: " + this.parentesco);
    }
}