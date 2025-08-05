package br.com.escola.gui;

import br.com.escola.negocio.Fachada;
import br.com.escola.negocio.Usuario;
import javax.swing.JOptionPane;
import javax.swing.GroupLayout;
import javax.swing.LayoutStyle;
import java.awt.Dimension;
import javax.swing.JLabel;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class TelaLogin extends javax.swing.JFrame {

    private Fachada fachada;
    private javax.swing.JTextField campoUsuario;
    private javax.swing.JPasswordField campoSenha;
    private javax.swing.JButton botaoLogin;
    private JLabel labelUsuario;
    private JLabel labelSenha;

    public TelaLogin() {
        fachada = Fachada.getInstance();
        initComponents();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        campoUsuario = new javax.swing.JTextField();
        campoSenha = new javax.swing.JPasswordField();
        botaoLogin = new javax.swing.JButton("Login");
        labelUsuario = new JLabel("Usuário:");
        labelSenha = new JLabel("Senha:");

        setTitle("Sistema Escolar - Login");
        setPreferredSize(new Dimension(350, 200));
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);

        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.CENTER)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                    .addComponent(labelUsuario)
                    .addComponent(labelSenha))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                    .addComponent(campoUsuario, GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                    .addComponent(campoSenha, GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE))
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(botaoLogin)
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(labelUsuario)
                    .addComponent(campoUsuario, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(labelSenha)
                    .addComponent(campoSenha, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addComponent(botaoLogin)
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();

        botaoLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoLoginActionPerformed(evt);
            }
        });

        KeyListener enterKeyListener = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    realizarLogin();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {}
        };

        campoUsuario.addKeyListener(enterKeyListener);
        campoSenha.addKeyListener(enterKeyListener);
        botaoLogin.addKeyListener(enterKeyListener);
    }

    private void botaoLoginActionPerformed(java.awt.event.ActionEvent evt) {
        realizarLogin();
    }

    private void realizarLogin() {
        String nomeUsuario = campoUsuario.getText();
        String senha = new String(campoSenha.getPassword());

        try {
            Usuario usuarioAutenticado = fachada.autenticarUsuario(nomeUsuario, senha);

            if (usuarioAutenticado != null) {
                JOptionPane.showMessageDialog(this, "Login bem-sucedido! Bem-vindo(a), " + usuarioAutenticado.getNomeUsuario() + ".", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                this.dispose();
                new TelaPrincipal(usuarioAutenticado).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Usuário ou senha inválidos.", "Erro de Login", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Ocorreu um erro inesperado durante o login: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } finally {
            campoSenha.setText("");
        }
    }

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TelaLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TelaLogin().setVisible(true);
            }
        });
    }
}