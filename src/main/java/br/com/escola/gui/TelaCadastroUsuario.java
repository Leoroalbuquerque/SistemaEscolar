package br.com.escola.gui;

import br.com.escola.negocio.Fachada;
import br.com.escola.negocio.IFachada;
import br.com.escola.excecoes.DadoInvalidoException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class TelaCadastroUsuario extends JFrame {

    private JTextField campoNomeUsuario;
    private JPasswordField campoSenha;
    private JComboBox<String> comboPerfil;
    private JButton botaoCadastrar;
    private IFachada fachada;

    public TelaCadastroUsuario() {
        super("Cadastro de Usuário");
        this.fachada = new Fachada();

        configurarJanela();
        inicializarComponentes();
        organizarLayout();
        adicionarListeners();
    }

    private void configurarJanela() {
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void inicializarComponentes() {
        campoNomeUsuario = new JTextField(20);
        campoSenha = new JPasswordField(20);
        // AJUSTE CRUCIAL AQUI: Apenas perfis com acesso ao sistema
        comboPerfil = new JComboBox<>(new String[]{"ADMINISTRADOR", "PROFESSOR", "FUNCIONARIO"});
        botaoCadastrar = new JButton("Cadastrar");
    }

    private void organizarLayout() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Nome de Usuário:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        add(campoNomeUsuario, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Senha:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        add(campoSenha, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Perfil:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        add(comboPerfil, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(botaoCadastrar, gbc);
    }

    private void adicionarListeners() {
        botaoCadastrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cadastrarUsuario();
            }
        });
    }

    private void cadastrarUsuario() {
        String nomeUsuario = campoNomeUsuario.getText().trim();
        String senha = new String(campoSenha.getPassword());
        String perfil = (String) comboPerfil.getSelectedItem();

        if (nomeUsuario.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, preencha todos os campos.", "Erro de Validação", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            boolean sucesso = fachada.criarNovoUsuario(nomeUsuario, senha, perfil);
            if (sucesso) {
                JOptionPane.showMessageDialog(this, "Usuário " + nomeUsuario + " cadastrado com sucesso!", "Cadastro Realizado", JOptionPane.INFORMATION_MESSAGE);
                limparCampos();
            } else {
                JOptionPane.showMessageDialog(this, "Falha ao cadastrar usuário.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (DadoInvalidoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro de Validação", JOptionPane.WARNING_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Erro de I/O ao cadastrar usuário: " + ex.getMessage(), "Erro de Sistema", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Ocorreu um erro inesperado: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void limparCampos() {
        campoNomeUsuario.setText("");
        campoSenha.setText("");
        comboPerfil.setSelectedIndex(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new TelaCadastroUsuario().setVisible(true);
            }
        });
    }
}