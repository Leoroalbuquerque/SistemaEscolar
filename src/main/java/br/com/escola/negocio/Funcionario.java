package br.com.escola.negocio;

public class Funcionario extends Pessoa {
    private String identificacao;
    private String cargo;
    private String setor;

    public Funcionario(String nome, String cpf, String identificacao, String cargo, String setor) {
        super(nome, cpf);
        this.identificacao = identificacao;
        this.cargo = cargo;
        this.setor = setor;
    }

    public Funcionario() {
        super();
    }

    public String getIdentificacao() {
        return identificacao;
    }

    public void setIdentificacao(String identificacao) {
        this.identificacao = identificacao;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getSetor() {
        return setor;
    }

    public void setSetor(String setor) {
        this.setor = setor;
    }

    @Override
    public void exibirInformacoes() {
        System.out.println("Tipo: Funcionário");
        System.out.println("Nome: " + getNome());
        System.out.println("CPF: " + getCpf());
        System.out.println("Identificação: " + this.identificacao);
        System.out.println("Cargo: " + this.cargo);
        System.out.println("Setor: " + this.setor);
    }
}