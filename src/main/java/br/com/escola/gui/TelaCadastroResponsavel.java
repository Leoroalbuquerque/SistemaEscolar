package br.com.escola.gui;

import br.com.escola.negocio.Responsavel;
import br.com.escola.negocio.Fachada;
import br.com.escola.negocio.IFachada;
import br.com.escola.excecoes.DadoInvalidoException;
import br.com.escola.excecoes.EntidadeNaoEncontradaException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public class TelaCadastroResponsavel extends JDialog {

    public JTextField campoCPF; // Tornando público para acesso de TelaCadastroAluno
    private JTextField campoNome;
    private JTextField campoTelefone;
    private JTextField campoEmail;
    private JTextField campoParentesco;
    private JCheckBox checkPrincipal;

    private JButton btnAdicionar;
    private JButton btnBuscar;
    private JButton btnAtualizar;
    private JButton btnDeletar;
    private JButton btnLimpar;
    private JButton btnListarTodos;

    private JTextArea areaResultados;

    private IFachada fachada;

    private boolean responsavelManipulado = false;

    public TelaCadastroResponsavel() {
        this(null, false);
    }

    public TelaCadastroResponsavel(JFrame parent, boolean modal) {
        super(parent, "Cadastro de Responsáveis", modal);

        this.fachada = Fachada.getInstance();

        setSize(700, 600);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parent);

        JPanel panelPrincipal = new JPanel(new BorderLayout());
        JPanel panelForm = new JPanel(new GridLayout(6, 2, 10, 10));
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

        panelForm.add(new JLabel("Parentesco:"));
        campoParentesco = new JTextField(20);
        panelForm.add(campoParentesco);

        panelForm.add(new JLabel("Responsável Principal:"));
        checkPrincipal = new JCheckBox();
        panelForm.add(checkPrincipal);

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

        btnAdicionar.addActionListener(e -> adicionarResponsavel());
        btnBuscar.addActionListener(e -> buscarResponsavel());
        btnAtualizar.addActionListener(e -> atualizarResponsavel());
        btnDeletar.addActionListener(e -> deletarResponsavel());
        btnLimpar.addActionListener(e -> limparCampos());
        btnListarTodos.addActionListener(e -> listarTodosResponsaveis());

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
            }
        });
    }

    private void adicionarResponsavel() {
        try {
            String cpfPessoa = campoCPF.getText();
            String nome = campoNome.getText();
            String telefonePessoa = campoTelefone.getText();
            String emailPessoa = campoEmail.getText();
            String parentesco = campoParentesco.getText();
            String cpfResponsavel = campoCPF.getText(); // Assumindo que o CPF da pessoa é o CPF do responsável
            boolean principal = checkPrincipal.isSelected();

            Responsavel responsavel = new Responsavel(nome, cpfPessoa, telefonePessoa,
                                                      emailPessoa, parentesco, cpfResponsavel, principal);

            fachada.adicionarResponsavel(responsavel);
            JOptionPane.showMessageDialog(this, "Responsável adicionado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparCampos();
            listarTodosResponsaveis();
            responsavelManipulado = true;
            this.dispose();
        } catch (DadoInvalidoException e) {
            JOptionPane.showMessageDialog(this, "Erro ao adicionar responsável: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            responsavelManipulado = false;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ocorreu um erro inesperado: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            responsavelManipulado = false;
        }
    }

    private void buscarResponsavel() {
        try {
            String cpf = campoCPF.getText();
            if (cpf.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Informe o CPF para buscar.", "Campo Vazio", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Responsavel responsavel = fachada.buscarResponsavel(cpf);
            exibirResponsavel(responsavel);
            responsavelManipulado = true;
        } catch (EntidadeNaoEncontradaException e) {
            JOptionPane.showMessageDialog(this, "Responsável não encontrado: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            limparCampos();
            responsavelManipulado = false;
        } catch (DadoInvalidoException e) {
            JOptionPane.showMessageDialog(this, "CPF inválido: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            responsavelManipulado = false;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ocorreu um erro inesperado: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            responsavelManipulado = false;
        }
    }

    private void atualizarResponsavel() {
        try {
            String cpfPessoa = campoCPF.getText();
            String nome = campoNome.getText();
            String telefonePessoa = campoTelefone.getText();
            String emailPessoa = campoEmail.getText();
            String parentesco = campoParentesco.getText();
            String cpfResponsavel = campoCPF.getText(); // Assumindo que o CPF da pessoa é o CPF do responsável
            boolean principal = checkPrincipal.isSelected();

            Responsavel responsavelAtualizado = new Responsavel(nome, cpfPessoa, telefonePessoa,
                                                                emailPessoa, parentesco, cpfResponsavel, principal);

            fachada.atualizarResponsavel(responsavelAtualizado);
            JOptionPane.showMessageDialog(this, "Responsável atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparCampos();
            listarTodosResponsaveis();
            responsavelManipulado = true;
            this.dispose();
        } catch (DadoInvalidoException | EntidadeNaoEncontradaException e) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar responsável: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            responsavelManipulado = false;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ocorreu um erro inesperado: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            responsavelManipulado = false;
        }
    }

    private void deletarResponsavel() {
        try {
            String cpf = campoCPF.getText();
            if (cpf.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Informe o CPF para deletar.", "Campo Vazio", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Tem certeza que deseja deletar o responsável com CPF " + cpf + "?", "Confirmar Deleção",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                if (fachada.deletarResponsavel(cpf)) {
                    JOptionPane.showMessageDialog(this, "Responsável deletado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    limparCampos();
                    listarTodosResponsaveis();
                } else {
                    JOptionPane.showMessageDialog(this, "Falha ao deletar responsável. Verifique o CPF.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (EntidadeNaoEncontradaException | DadoInvalidoException e) {
            JOptionPane.showMessageDialog(this, "Erro ao deletar responsável: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
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
        campoParentesco.setText("");
        checkPrincipal.setSelected(false);
        areaResultados.setText("");
    }

    private void listarTodosResponsaveis() {
        try {
            List<Responsavel> responsaveis = fachada.listarTodosResponsaveis();
            areaResultados.setText("");
            if (responsaveis.isEmpty()) {
                areaResultados.append("Nenhum responsável cadastrado.\n");
            } else {
                areaResultados.append("--- Lista de Responsáveis ---\n");
                for (Responsavel r : responsaveis) {
                    areaResultados.append("CPF Pessoa: " + r.getCpf() +
                                          ", Nome: " + r.getNome() +
                                          ", Telefone: " + r.getTelefone() +
                                          ", Email: " + r.getEmail() +
                                          ", Parentesco: " + r.getParentesco() +
                                          ", Principal: " + r.isPrincipal() +
                                          ", CPF Responsável: " + r.getCpfResponsavel() + "\n");
                }
                areaResultados.append("-----------------------------\n");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao listar responsáveis: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void exibirResponsavel(Responsavel responsavel) {
        if (responsavel != null) {
            campoCPF.setText(responsavel.getCpf());
            campoNome.setText(responsavel.getNome());
            campoTelefone.setText(responsavel.getTelefone());
            campoEmail.setText(responsavel.getEmail());
            campoParentesco.setText(responsavel.getParentesco());
            checkPrincipal.setSelected(responsavel.isPrincipal());

            areaResultados.setText("Responsável encontrado:\n" +
                                   "CPF Pessoa: " + responsavel.getCpf() + "\n" +
                                   "Nome: " + responsavel.getNome() + "\n" +
                                   "Telefone: " + responsavel.getTelefone() + "\n" +
                                   "Email: " + responsavel.getEmail() + "\n" +
                                   "Parentesco: " + responsavel.getParentesco() + "\n" +
                                   "Principal: " + responsavel.isPrincipal() + "\n" +
                                   "CPF Responsável: " + responsavel.getCpfResponsavel() + "\n"
            );
        } else {
            limparCampos();
            areaResultados.setText("Responsável não encontrado.");
        }
    }

    public boolean isResponsavelManipulado() {
        return responsavelManipulado;
    }
}