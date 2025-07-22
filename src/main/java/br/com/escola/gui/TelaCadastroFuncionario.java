package br.com.escola.gui;

import br.com.escola.negocio.Funcionario;
import br.com.escola.negocio.Fachada;
import br.com.escola.negocio.IFachada;
import br.com.escola.excecoes.DadoInvalidoException;
import br.com.escola.excecoes.EntidadeNaoEncontradaException;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TelaCadastroFuncionario extends JDialog {

    private JTextField campoCPF;
    private JTextField campoNome;
    private JTextField campoTelefone;
    private JTextField campoEmail;
    private JTextField campoMatriculaFuncional;
    private JTextField campoCargo;
    private JTextField campoSalario;

    private JButton btnAdicionar;
    private JButton btnBuscar;
    private JButton btnAtualizar;
    private JButton btnDeletar;
    private JButton btnLimpar;
    private JButton btnListarTodos;

    private JTextArea areaResultados;

    private IFachada fachada;

    public TelaCadastroFuncionario(JFrame parent, boolean modal) {
        super(parent, "Cadastro de Funcionários", modal);

        this.fachada = Fachada.getInstance();

        setSize(700, 650);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parent);

        JPanel panelPrincipal = new JPanel(new BorderLayout());
        JPanel panelForm = new JPanel(new GridLayout(7, 2, 10, 10));
        panelForm.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        panelForm.add(new JLabel("CPF:"));
        campoCPF = new JTextField(20);
        panelForm.add(campoCPF);

        panelForm.add(new JLabel("Nome:"));
        campoNome = new JTextField(20);
        panelForm.add(campoNome);

        panelForm.add(new JLabel("Telefone:"));
        campoTelefone = new JTextField(20);
        panelForm.add(campoTelefone);

        panelForm.add(new JLabel("Email:"));
        campoEmail = new JTextField(20);
        panelForm.add(campoEmail);

        panelForm.add(new JLabel("Matrícula Funcional:"));
        campoMatriculaFuncional = new JTextField(20);
        panelForm.add(campoMatriculaFuncional);

        panelForm.add(new JLabel("Cargo:"));
        campoCargo = new JTextField(20);
        panelForm.add(campoCargo);

        panelForm.add(new JLabel("Salário:"));
        campoSalario = new JTextField(20);
        panelForm.add(campoSalario);

        JPanel panelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnAdicionar = new JButton("Adicionar");
        btnBuscar = new JButton("Buscar");
        btnAtualizar = new JButton("Atualizar");
        btnDeletar = new JButton("Deletar");
        btnLimpar = new JButton("Limpar Campos");
        btnListarTodos = new JButton("Listar Todos");

        panelBotoes.add(btnAdicionar);
        panelBotoes.add(btnBuscar);
        panelBotoes.add(btnAtualizar);
        panelBotoes.add(btnDeletar);
        panelBotoes.add(btnLimpar);
        panelBotoes.add(btnListarTodos);

        areaResultados = new JTextArea(10, 50);
        areaResultados.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(areaResultados);

        panelPrincipal.add(panelForm, BorderLayout.NORTH);
        panelPrincipal.add(panelBotoes, BorderLayout.CENTER);
        panelPrincipal.add(scrollPane, BorderLayout.SOUTH);

        add(panelPrincipal);

        btnAdicionar.addActionListener(e -> adicionarFuncionario());
        btnBuscar.addActionListener(e -> buscarFuncionario());
        btnAtualizar.addActionListener(e -> atualizarFuncionario());
        btnDeletar.addActionListener(e -> deletarFuncionario());
        btnLimpar.addActionListener(e -> limparCampos());
        btnListarTodos.addActionListener(e -> listarTodosFuncionarios());
    }

    private void adicionarFuncionario() {
        try {
            String cpfPessoa = campoCPF.getText();
            String nome = campoNome.getText();
            String telefonePessoa = campoTelefone.getText();
            String emailPessoa = campoEmail.getText();
            String matriculaFuncional = campoMatriculaFuncional.getText();
            String cargo = campoCargo.getText();

            double salario = 0.0;
            try {
                salario = Double.parseDouble(campoSalario.getText());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Salário inválido. Use um número.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Funcionario funcionario = new Funcionario(nome, cpfPessoa, telefonePessoa,
                                                      emailPessoa, matriculaFuncional, cargo, salario);

            fachada.adicionarFuncionario(funcionario);
            JOptionPane.showMessageDialog(this, "Funcionário adicionado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparCampos();
            listarTodosFuncionarios();
        } catch (DadoInvalidoException e) {
            JOptionPane.showMessageDialog(this, "Erro ao adicionar funcionário: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ocorreu um erro inesperado: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void buscarFuncionario() {
        try {
            String matriculaFuncionalBusca = campoMatriculaFuncional.getText();

            if (matriculaFuncionalBusca.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Informe a Matrícula Funcional para buscar.", "Campo Vazio", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Funcionario funcionario = fachada.buscarFuncionario(matriculaFuncionalBusca);
            exibirFuncionario(funcionario);
        } catch (EntidadeNaoEncontradaException e) {
            JOptionPane.showMessageDialog(this, "Funcionário não encontrado: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            limparCampos();
        } catch (DadoInvalidoException e) {
            JOptionPane.showMessageDialog(this, "Matrícula Funcional inválida: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ocorreu um erro inesperado: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void atualizarFuncionario() {
        try {
            String cpfPessoa = campoCPF.getText();
            String nome = campoNome.getText();
            String telefonePessoa = campoTelefone.getText();
            String emailPessoa = campoEmail.getText();
            String matriculaFuncional = campoMatriculaFuncional.getText();
            String cargo = campoCargo.getText();

            double salario = 0.0;
            try {
                salario = Double.parseDouble(campoSalario.getText());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Salário inválido. Use um número.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Funcionario funcionarioAtualizado = new Funcionario(nome, cpfPessoa, telefonePessoa,
                                                                 emailPessoa, matriculaFuncional, cargo, salario);

            fachada.atualizarFuncionario(funcionarioAtualizado);
            JOptionPane.showMessageDialog(this, "Funcionário atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparCampos();
            listarTodosFuncionarios();
        } catch (DadoInvalidoException | EntidadeNaoEncontradaException e) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar funcionário: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ocorreu um erro inesperado: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void deletarFuncionario() {
        try {
            String matriculaFuncional = campoMatriculaFuncional.getText();
            if (matriculaFuncional.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Informe a Matrícula Funcional para deletar.", "Campo Vazio", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Tem certeza que deseja deletar o funcionário com Matrícula Funcional " + matriculaFuncional + "?", "Confirmar Deleção",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                if (fachada.deletarFuncionario(matriculaFuncional)) {
                    JOptionPane.showMessageDialog(this, "Funcionário deletado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    limparCampos();
                    listarTodosFuncionarios();
                } else {
                    JOptionPane.showMessageDialog(this, "Falha ao deletar funcionário. Verifique a Matrícula Funcional.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (EntidadeNaoEncontradaException | DadoInvalidoException e) {
            JOptionPane.showMessageDialog(this, "Erro ao deletar funcionário: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ocorreu um erro inesperado: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void limparCampos() {
        campoCPF.setText("");
        campoNome.setText("");
        campoTelefone.setText("");
        campoEmail.setText("");
        campoMatriculaFuncional.setText("");
        campoCargo.setText("");
        campoSalario.setText("");
        areaResultados.setText("");
    }

    private void listarTodosFuncionarios() {
        try {
            List<Funcionario> funcionarios = fachada.listarTodosFuncionarios();
            areaResultados.setText("");
            if (funcionarios.isEmpty()) {
                areaResultados.append("Nenhum funcionário cadastrado.\n");
            } else {
                areaResultados.append("--- Lista de Funcionários ---\n");
                for (Funcionario f : funcionarios) {
                    areaResultados.append("Matrícula Funcional: " + f.getMatriculaFuncional() +
                                          ", Nome: " + f.getNome() +
                                          ", CPF: " + f.getCpf() +
                                          ", Telefone: " + f.getTelefone() +
                                          ", Email: " + f.getEmail() +
                                          ", Cargo: " + f.getCargo() +
                                          ", Salário: " + String.format("%.2f", f.getSalario()) + "\n");
                }
                areaResultados.append("-----------------------------\n");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao listar funcionários: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void exibirFuncionario(Funcionario funcionario) {
        if (funcionario != null) {
            campoCPF.setText(funcionario.getCpf());
            campoNome.setText(funcionario.getNome());
            campoTelefone.setText(funcionario.getTelefone());
            campoEmail.setText(funcionario.getEmail());
            campoMatriculaFuncional.setText(funcionario.getMatriculaFuncional());
            campoCargo.setText(funcionario.getCargo());
            campoSalario.setText(String.format("%.2f", funcionario.getSalario()));

            areaResultados.setText("Funcionário encontrado:\n" +
                                   "Matrícula Funcional: " + funcionario.getMatriculaFuncional() + "\n" +
                                   "Nome: " + funcionario.getNome() + "\n" +
                                   "CPF: " + funcionario.getCpf() + "\n" +
                                   "Telefone: " + funcionario.getTelefone() + "\n" +
                                   "Email: " + funcionario.getEmail() + "\n" +
                                   "Cargo: " + funcionario.getCargo() + "\n" +
                                   "Salário: " + String.format("%.2f", funcionario.getSalario()) + "\n"
            );
        } else {
            limparCampos();
            areaResultados.setText("Funcionário não encontrado.");
        }
    }
}