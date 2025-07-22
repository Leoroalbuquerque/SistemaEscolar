package br.com.escola.gui;

import br.com.escola.negocio.Disciplina;
import br.com.escola.negocio.Fachada;
import br.com.escola.negocio.IFachada;
import br.com.escola.excecoes.DadoInvalidoException;
import br.com.escola.excecoes.EntidadeNaoEncontradaException;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TelaCadastroDisciplina extends JDialog {

    private IFachada fachada;

    private JTextField campoCodigo;
    private JTextField campoNome;
    private JTextArea areaResultados;

    public TelaCadastroDisciplina(JFrame parent, boolean modal) {
        super(parent, "Cadastro de Disciplinas", modal);
        this.fachada = Fachada.getInstance();

        setSize(600, 500);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JPanel painelEntrada = new JPanel(new GridLayout(3, 2, 10, 10));
        painelEntrada.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        painelEntrada.add(new JLabel("Código:"));
        campoCodigo = new JTextField();
        painelEntrada.add(campoCodigo);

        painelEntrada.add(new JLabel("Nome:"));
        campoNome = new JTextField();
        painelEntrada.add(campoNome);

        add(painelEntrada, BorderLayout.NORTH);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnAdicionar = new JButton("Adicionar");
        JButton btnBuscar = new JButton("Buscar");
        JButton btnAtualizar = new JButton("Atualizar");
        JButton btnDeletar = new JButton("Deletar");
        JButton btnLimpar = new JButton("Limpar Campos");
        JButton btnListarTodos = new JButton("Listar Todos");

        painelBotoes.add(btnAdicionar);
        painelBotoes.add(btnBuscar);
        painelBotoes.add(btnAtualizar);
        painelBotoes.add(btnDeletar);
        painelBotoes.add(btnLimpar);
        painelBotoes.add(btnListarTodos);

        add(painelBotoes, BorderLayout.CENTER);

        areaResultados = new JTextArea();
        areaResultados.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(areaResultados);
        add(scrollPane, BorderLayout.SOUTH);

        btnAdicionar.addActionListener(e -> adicionarDisciplina());
        btnBuscar.addActionListener(e -> buscarDisciplina());
        btnAtualizar.addActionListener(e -> atualizarDisciplina());
        btnDeletar.addActionListener(e -> deletarDisciplina());
        btnLimpar.addActionListener(e -> limparCampos());
        btnListarTodos.addActionListener(e -> listarTodasDisciplinas());

        limparCampos();
        listarTodasDisciplinas();
    }

    private void adicionarDisciplina() {
        try {
            String codigo = campoCodigo.getText();
            String nome = campoNome.getText();

            Disciplina novaDisciplina = new Disciplina(codigo, nome);
            fachada.adicionarDisciplina(novaDisciplina);
            JOptionPane.showMessageDialog(this, "Disciplina adicionada com sucesso!");
            limparCampos();
            listarTodasDisciplinas();
        } catch (DadoInvalidoException ex) {
            JOptionPane.showMessageDialog(this, "Erro de Validação: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao adicionar disciplina: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void buscarDisciplina() {
        try {
            String codigo = campoCodigo.getText();
            if (codigo.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, digite o código da disciplina para buscar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Disciplina disciplina = fachada.buscarDisciplina(codigo);
            exibirDisciplina(disciplina);
            JOptionPane.showMessageDialog(this, "Disciplina encontrada!");
        } catch (EntidadeNaoEncontradaException ex) {
            JOptionPane.showMessageDialog(this, "Disciplina não encontrada: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            limparCampos();
        } catch (DadoInvalidoException ex) {
            JOptionPane.showMessageDialog(this, "Erro de Validação: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao buscar disciplina: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void atualizarDisciplina() {
        try {
            String codigo = campoCodigo.getText();
            String nome = campoNome.getText();

            Disciplina disciplinaAtualizada = new Disciplina(codigo, nome);
            fachada.atualizarDisciplina(disciplinaAtualizada);
            JOptionPane.showMessageDialog(this, "Disciplina atualizada com sucesso!");
            limparCampos();
            listarTodasDisciplinas();
        } catch (EntidadeNaoEncontradaException ex) {
            JOptionPane.showMessageDialog(this, "Erro: Disciplina não encontrada para atualização. " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (DadoInvalidoException ex) {
            JOptionPane.showMessageDialog(this, "Erro de Validação: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar disciplina: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void deletarDisciplina() {
        try {
            String codigo = campoCodigo.getText();
            if (codigo.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, digite o código da disciplina para deletar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja deletar a disciplina " + codigo + "?", "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                boolean deletado = fachada.deletarDisciplina(codigo);
                if (deletado) {
                    JOptionPane.showMessageDialog(this, "Disciplina deletada com sucesso!");
                    limparCampos();
                    listarTodasDisciplinas();
                } else {
                    JOptionPane.showMessageDialog(this, "Não foi possível deletar a disciplina.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (EntidadeNaoEncontradaException ex) {
            JOptionPane.showMessageDialog(this, "Erro: Disciplina não encontrada para exclusão. " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (DadoInvalidoException ex) {
            JOptionPane.showMessageDialog(this, "Erro de Validação: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao deletar disciplina: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void limparCampos() {
        campoCodigo.setText("");
        campoNome.setText("");
        areaResultados.setText("");
    }

    private void listarTodasDisciplinas() {
        try {
            List<Disciplina> disciplinas = fachada.listarTodasDisciplinas();
            if (disciplinas.isEmpty()) {
                areaResultados.setText("Nenhuma disciplina cadastrada.");
            } else {
                StringBuilder sb = new StringBuilder("--- Lista de Disciplinas ---\n");
                for (Disciplina d : disciplinas) {
                    sb.append("Código: ").append(d.getCodigo())
                      .append(", Nome: ").append(d.getNome())
                      .append("\n");
                }
                areaResultados.setText(sb.toString());
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao listar disciplinas: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void exibirDisciplina(Disciplina disciplina) {
        if (disciplina != null) {
            campoCodigo.setText(disciplina.getCodigo());
            campoNome.setText(disciplina.getNome());
            areaResultados.setText(
                "--- Detalhes da Disciplina ---\n" +
                "Código: " + disciplina.getCodigo() + "\n" +
                "Nome: " + disciplina.getNome() + "\n"
            );
        } else {
            limparCampos();
            areaResultados.setText("Disciplina não encontrada.");
        }
    }
}