package br.com.escola.gui;

import br.com.escola.negocio.Usuario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import br.com.escola.gui.TelaCadastroFuncionario;
import br.com.escola.gui.TelaCadastroProfessor;
import br.com.escola.gui.TelaCadastroResponsavel;
import br.com.escola.gui.TelaCadastroDisciplina;
import br.com.escola.gui.TelaCadastroTurma;
import br.com.escola.gui.TelaCadastroAvaliacao;
import br.com.escola.gui.TelaRegistroOcorrencia;
import br.com.escola.gui.TelaCalendarioEscolar;
import br.com.escola.gui.TelaNota;
import br.com.escola.gui.TelaFrequencia;

public class TelaPrincipal extends JFrame {

    private Usuario usuarioLogado;
    private JLabel labelStatusUsuario;

    public TelaPrincipal(Usuario usuario) {
        this.usuarioLogado = usuario;

        setTitle("Sistema Escolar - Principal");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu menuCadastros = new JMenu("Cadastros");
        menuBar.add(menuCadastros);

        JMenuItem itemCadastrarAluno = new JMenuItem("Alunos");
        JMenuItem itemCadastrarProfessor = new JMenuItem("Professores");
        JMenuItem itemCadastrarDisciplina = new JMenuItem("Disciplinas");
        JMenuItem itemCadastrarTurma = new JMenuItem("Turmas");
        JMenuItem itemCadastrarFuncionario = new JMenuItem("Funcionários");
        JMenuItem itemCadastrarResponsavel = new JMenuItem("Responsáveis");

        menuCadastros.add(itemCadastrarAluno);
        menuCadastros.add(itemCadastrarProfessor);
        menuCadastros.add(itemCadastrarDisciplina);
        menuCadastros.add(itemCadastrarTurma);
        menuCadastros.add(itemCadastrarFuncionario);
        menuCadastros.add(itemCadastrarResponsavel);

        JMenu menuOperacoes = new JMenu("Operações");
        menuBar.add(menuOperacoes);
        JMenuItem itemLancamentoNotas = new JMenuItem("Lançamento de Notas");
        JMenuItem itemRegistroFrequencia = new JMenuItem("Registro de Frequência");
        JMenuItem itemOcorrencias = new JMenuItem("Ocorrências");
        JMenuItem itemCalendario = new JMenuItem("Calendário Escolar");
        JMenuItem itemAvaliacoes = new JMenuItem("Avaliações");
        menuOperacoes.add(itemLancamentoNotas);
        menuOperacoes.add(itemRegistroFrequencia);
        menuOperacoes.add(itemOcorrencias);
        menuOperacoes.add(itemCalendario);
        menuOperacoes.add(itemAvaliacoes);

        JMenu menuAjuda = new JMenu("Ajuda");
        menuBar.add(menuAjuda);
        JMenuItem itemSobre = new JMenuItem("Sobre");
        menuAjuda.add(itemSobre);

        JMenu menuSair = new JMenu("Sair");
        menuBar.add(menuSair);
        JMenuItem itemSairSistema = new JMenuItem("Sair do Sistema");
        menuSair.add(itemSairSistema);

        JPanel mainPanel = new JPanel(new BorderLayout());
        add(mainPanel);

        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.setBorder(BorderFactory.createEtchedBorder());
        labelStatusUsuario = new JLabel("Usuário: " + usuario.getNomeUsuario() + " | Perfil: " + usuario.getPerfil());
        statusPanel.add(labelStatusUsuario);
        mainPanel.add(statusPanel, BorderLayout.SOUTH);

        itemSairSistema.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirmarSaida();
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                confirmarSaida();
            }
        });

        itemCadastrarAluno.addActionListener(e -> {
            TelaCadastroAluno telaAluno = new TelaCadastroAluno();
            telaAluno.setVisible(true);
        });

        itemCadastrarFuncionario.addActionListener(e -> {
            TelaCadastroFuncionario telaFuncionario = new TelaCadastroFuncionario(this, true);
            telaFuncionario.setVisible(true);
        });

        itemCadastrarProfessor.addActionListener(e -> {
            TelaCadastroProfessor telaProfessor = new TelaCadastroProfessor(this, true);
            telaProfessor.setVisible(true);
        });

        itemCadastrarDisciplina.addActionListener(e -> {
            TelaCadastroDisciplina telaDisciplina = new TelaCadastroDisciplina(this, true);
            telaDisciplina.setVisible(true);
        });

        itemCadastrarTurma.addActionListener(e -> {
            TelaCadastroTurma telaTurma = new TelaCadastroTurma(this, true);
            telaTurma.setVisible(true);
        });

        itemCadastrarResponsavel.addActionListener(e -> {
            TelaCadastroResponsavel telaResponsavel = new TelaCadastroResponsavel(this, true);
            telaResponsavel.setVisible(true);
        });

        itemLancamentoNotas.addActionListener(e -> {
            TelaNota telaNota = new TelaNota();
            telaNota.setVisible(true);
        });
        
        itemRegistroFrequencia.addActionListener(e -> {
            TelaFrequencia telaFrequencia = new TelaFrequencia();
            telaFrequencia.setVisible(true);
        });

        itemOcorrencias.addActionListener(e -> {
            TelaRegistroOcorrencia telaOcorrencia = new TelaRegistroOcorrencia(this, true);
            telaOcorrencia.setVisible(true);
        });

        itemCalendario.addActionListener(e -> {
            TelaCalendarioEscolar telaCalendario = new TelaCalendarioEscolar(this, true);
            telaCalendario.setVisible(true);
        });

        itemAvaliacoes.addActionListener(e -> {
            TelaCadastroAvaliacao telaAvaliacao = new TelaCadastroAvaliacao(this, true);
            telaAvaliacao.setVisible(true);
        });

        itemSobre.addActionListener(e -> JOptionPane.showMessageDialog(this, "Sistema Escolar v1.0\nDesenvolvido por: [Seu Nome/Grupo]", "Sobre", JOptionPane.INFORMATION_MESSAGE));

        configurarPermissoes();
    }

    private void confirmarSaida() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja sair do sistema?", "Confirmar Saída",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            System.exit(0);
        }
    }

    private void configurarPermissoes() {
        if (usuarioLogado == null) {
            return;
        }

        JMenuBar menuBar = getJMenuBar();
        for (int i = 0; i < menuBar.getMenuCount(); i++) {
            JMenu menu = menuBar.getMenu(i);
            if (menu != null) {

                if (usuarioLogado.getPerfil().equals("PROFESSOR")) {
                    if (menu.getText().equals("Cadastros")) {
                        menu.setEnabled(false);
                    }
                } else if (usuarioLogado.getPerfil().equals("ALUNO")) {
                    if (menu.getText().equals("Cadastros") || menu.getText().equals("Operações")) {
                        menu.setEnabled(false);
                    }
                } else if (!usuarioLogado.getPerfil().equals("ADMINISTRADOR")) {
                    if (!menu.getText().equals("Sair") && !menu.getText().equals("Ajuda")) {
                        menu.setEnabled(false);
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Usuario usuarioAdmin = new Usuario("admin", "admin123", "ADMINISTRADOR");
            new TelaPrincipal(usuarioAdmin).setVisible(true);
        });
    }
}