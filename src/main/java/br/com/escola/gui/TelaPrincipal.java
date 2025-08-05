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
import br.com.escola.gui.TelaCadastroUsuario;

public class TelaPrincipal extends JFrame {

    private Usuario usuarioLogado;
    private JLabel labelStatusUsuario;
    private JMenuItem itemCadastrarAluno;
    private JMenuItem itemCadastrarProfessor;
    private JMenuItem itemCadastrarDisciplina;
    private JMenuItem itemCadastrarTurma;
    private JMenuItem itemCadastrarFuncionario;
    private JMenuItem itemCadastrarResponsavel;
    private JMenuItem itemCadastrarUsuario;
    private JMenuItem itemLancamentoNotas;
    private JMenuItem itemRegistroFrequencia;
    private JMenuItem itemOcorrencias;
    private JMenuItem itemCalendario;
    private JMenuItem itemAvaliacoes;
    private JMenu menuCadastros;
    private JMenu menuOperacoes;
    private JMenu menuAjuda;
    private JMenu menuSair;
    private JMenuItem itemSairSistema;
    private JMenuItem itemSobre;

    public TelaPrincipal(Usuario usuario) {
        this.usuarioLogado = usuario;

        setTitle("Sistema Escolar - Principal");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        menuCadastros = new JMenu("Cadastros");
        menuBar.add(menuCadastros);

        itemCadastrarAluno = new JMenuItem("Alunos");
        itemCadastrarProfessor = new JMenuItem("Professores");
        itemCadastrarDisciplina = new JMenuItem("Disciplinas");
        itemCadastrarTurma = new JMenuItem("Turmas");
        itemCadastrarFuncionario = new JMenuItem("Funcionários");
        itemCadastrarResponsavel = new JMenuItem("Responsáveis");
        itemCadastrarUsuario = new JMenuItem("Usuários");

        menuCadastros.add(itemCadastrarAluno);
        menuCadastros.add(itemCadastrarProfessor);
        menuCadastros.add(itemCadastrarDisciplina);
        menuCadastros.add(itemCadastrarTurma);
        menuCadastros.add(itemCadastrarFuncionario);
        menuCadastros.add(itemCadastrarResponsavel);
        menuCadastros.add(itemCadastrarUsuario);

        menuOperacoes = new JMenu("Operações");
        menuBar.add(menuOperacoes);
        itemLancamentoNotas = new JMenuItem("Lançamento de Notas");
        itemRegistroFrequencia = new JMenuItem("Registro de Frequência");
        itemOcorrencias = new JMenuItem("Ocorrências");
        itemCalendario = new JMenuItem("Calendário Escolar");
        itemAvaliacoes = new JMenuItem("Avaliações");
        menuOperacoes.add(itemLancamentoNotas);
        menuOperacoes.add(itemRegistroFrequencia);
        menuOperacoes.add(itemOcorrencias);
        menuOperacoes.add(itemCalendario);
        menuOperacoes.add(itemAvaliacoes);

        menuAjuda = new JMenu("Ajuda");
        menuBar.add(menuAjuda);
        itemSobre = new JMenuItem("Sobre");
        menuAjuda.add(itemSobre);

        menuSair = new JMenu("Sair");
        menuBar.add(menuSair);
        itemSairSistema = new JMenuItem("Sair do Sistema");
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
            try {
                TelaRegistroOcorrencia telaOcorrencia = new TelaRegistroOcorrencia(this, true);
                telaOcorrencia.setVisible(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao abrir a tela de Ocorrências: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        itemCalendario.addActionListener(e -> {
            TelaCalendarioEscolar telaCalendario = new TelaCalendarioEscolar(this, true);
            telaCalendario.setVisible(true);
        });

        itemAvaliacoes.addActionListener(e -> {
            TelaCadastroAvaliacao telaAvaliacao = new TelaCadastroAvaliacao(this, true);
            telaAvaliacao.setVisible(true);
        });

        itemCadastrarUsuario.addActionListener(e -> {
            TelaCadastroUsuario telaCadastro = new TelaCadastroUsuario();
            telaCadastro.setVisible(true);
        });

        itemSobre.addActionListener(e -> JOptionPane.showMessageDialog(this, "Sistema Escolar v1.0\nDesenvolvido por: [Leonardo Rodrigues, José Albérico, Rodrigo Albuquerque]", "Sobre", JOptionPane.INFORMATION_MESSAGE));

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

        String perfil = usuarioLogado.getPerfil();

        setAllMenuItemsEnabled(false);
        menuAjuda.setEnabled(true);
        itemSobre.setEnabled(true);
        menuSair.setEnabled(true);
        itemSairSistema.setEnabled(true);


        if (perfil.equals("ADMINISTRADOR")) {
            setAllMenuItemsEnabled(true);
        } else if (perfil.equals("PROFESSOR")) {
            menuOperacoes.setEnabled(true);
            itemLancamentoNotas.setEnabled(true);
            itemRegistroFrequencia.setEnabled(true);
            itemOcorrencias.setEnabled(true);
            itemCalendario.setEnabled(true);
            itemAvaliacoes.setEnabled(true);
        } else if (perfil.equals("FUNCIONARIO")) {
            menuCadastros.setEnabled(true);
            itemCadastrarAluno.setEnabled(true);
            itemCadastrarProfessor.setEnabled(true);
            itemCadastrarDisciplina.setEnabled(true);
            itemCadastrarTurma.setEnabled(true);
            itemCadastrarFuncionario.setEnabled(true);
            itemCadastrarResponsavel.setEnabled(true);
            itemCadastrarUsuario.setEnabled(false);

            menuOperacoes.setEnabled(true);
            itemLancamentoNotas.setEnabled(false);
            itemRegistroFrequencia.setEnabled(false);
            itemOcorrencias.setEnabled(true);
            itemCalendario.setEnabled(true);
            itemAvaliacoes.setEnabled(false);
        } else {

        }
    }
    
    private void setAllMenuItemsEnabled(boolean enabled) {
        JMenuBar menuBar = getJMenuBar();
        for (int i = 0; i < menuBar.getMenuCount(); i++) {
            JMenu menu = menuBar.getMenu(i);
            if (menu != null) {
                menu.setEnabled(enabled);
                for (int j = 0; j < menu.getItemCount(); j++) {
                    JMenuItem item = menu.getItem(j);
                    if (item != null) {
                        item.setEnabled(enabled);
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