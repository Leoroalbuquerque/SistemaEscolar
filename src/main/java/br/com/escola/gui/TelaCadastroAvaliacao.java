package br.com.escola.gui;

import br.com.escola.negocio.Avaliacao;
import br.com.escola.negocio.Disciplina;
import br.com.escola.negocio.Fachada;
import br.com.escola.negocio.IFachada;
import br.com.escola.excecoes.DadoInvalidoException;
import br.com.escola.excecoes.EntidadeNaoEncontradaException;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.io.IOException;

public class TelaCadastroAvaliacao extends JDialog {

    private IFachada fachada;

    private JTextField campoId;
    private JTextField campoNomeAvaliacao;
    private JTextField campoDataInicio;
    private JTextField campoDataFim;
    private JComboBox<Disciplina> comboDisciplinas;
    private JTextArea areaResultados;

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public TelaCadastroAvaliacao(JFrame parent, boolean modal) {
        super(parent, "Cadastro de Avaliações", modal);
        this.fachada = Fachada.getInstance();

        setSize(600, 500);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JPanel painelEntrada = new JPanel(new GridLayout(5, 2, 10, 10));
        painelEntrada.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        painelEntrada.add(new JLabel("ID da Avaliação:"));
        campoId = new JTextField();
        painelEntrada.add(campoId);

        painelEntrada.add(new JLabel("Nome da Avaliação:"));
        campoNomeAvaliacao = new JTextField();
        painelEntrada.add(campoNomeAvaliacao);

        painelEntrada.add(new JLabel("Data Início (DD/MM/AAAA):"));
        campoDataInicio = new JTextField();
        painelEntrada.add(campoDataInicio);

        painelEntrada.add(new JLabel("Data Fim (DD/MM/AAAA):"));
        campoDataFim = new JTextField();
        painelEntrada.add(campoDataFim);

        painelEntrada.add(new JLabel("Disciplina:"));
        comboDisciplinas = new JComboBox<>();
        painelEntrada.add(comboDisciplinas);

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

        btnAdicionar.addActionListener(e -> adicionarAvaliacao());
        btnBuscar.addActionListener(e -> buscarAvaliacao());
        btnAtualizar.addActionListener(e -> atualizarAvaliacao());
        btnDeletar.addActionListener(e -> deletarAvaliacao());
        btnLimpar.addActionListener(e -> limparCampos());
        btnListarTodos.addActionListener(e -> listarTodasAvaliacoes());

        popularDisciplinas();
        limparCampos();
        listarTodasAvaliacoes();
    }

    private void popularDisciplinas() {
        try {
            List<Disciplina> disciplinas = fachada.listarTodasDisciplinas();
            comboDisciplinas.removeAllItems();
            for (Disciplina d : disciplinas) {
                comboDisciplinas.addItem(d);
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar disciplinas: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void adicionarAvaliacao() {
        try {
            String id = campoId.getText();
            String nome = campoNomeAvaliacao.getText();
            LocalDate dataInicio = LocalDate.parse(campoDataInicio.getText(), dateFormatter);
            LocalDate dataFim = LocalDate.parse(campoDataFim.getText(), dateFormatter);
            Disciplina disciplinaSelecionada = (Disciplina) comboDisciplinas.getSelectedItem();

            if (disciplinaSelecionada == null) {
                JOptionPane.showMessageDialog(this, "Selecione uma disciplina para a avaliação.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            fachada.criarAvaliacao(id, nome, dataInicio, dataFim, disciplinaSelecionada);
            JOptionPane.showMessageDialog(this, "Avaliação adicionada com sucesso!");
            limparCampos();
            listarTodasAvaliacoes();
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Formato de data inválido. Use DD/MM/AAAA.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (DadoInvalidoException ex) {
            JOptionPane.showMessageDialog(this, "Erro de Validação: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao adicionar avaliação: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void buscarAvaliacao() {
        try {
            String id = campoId.getText();
            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, digite o ID da avaliação para buscar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Avaliacao avaliacao = fachada.consultarAvaliacao(id);
            exibirAvaliacao(avaliacao);
            JOptionPane.showMessageDialog(this, "Avaliação encontrada!");
        } catch (EntidadeNaoEncontradaException ex) {
            JOptionPane.showMessageDialog(this, "Avaliação não encontrada: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            limparCampos();
        } catch (DadoInvalidoException ex) {
            JOptionPane.showMessageDialog(this, "Erro de Validação: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao buscar avaliação: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void atualizarAvaliacao() {
        try {
            String id = campoId.getText();
            String nome = campoNomeAvaliacao.getText();
            LocalDate dataInicio = LocalDate.parse(campoDataInicio.getText(), dateFormatter);
            LocalDate dataFim = LocalDate.parse(campoDataFim.getText(), dateFormatter);
            Disciplina disciplinaSelecionada = (Disciplina) comboDisciplinas.getSelectedItem();

            if (disciplinaSelecionada == null) {
                JOptionPane.showMessageDialog(this, "Selecione uma disciplina para a avaliação.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Avaliacao avaliacaoAtualizada = new Avaliacao(id, nome, dataInicio, dataFim, disciplinaSelecionada);
            fachada.atualizarAvaliacao(avaliacaoAtualizada);
            JOptionPane.showMessageDialog(this, "Avaliação atualizada com sucesso!");
            limparCampos();
            listarTodasAvaliacoes();
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Formato de data inválido. Use DD/MM/AAAA.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (EntidadeNaoEncontradaException ex) {
            JOptionPane.showMessageDialog(this, "Erro: Avaliação não encontrada para atualização. " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (DadoInvalidoException ex) {
            JOptionPane.showMessageDialog(this, "Erro de Validação: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar avaliação: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void deletarAvaliacao() {
        try {
            String id = campoId.getText();
            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, digite o ID da avaliação para deletar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja deletar a avaliação " + id + "?", "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                fachada.excluirAvaliacao(id);
                JOptionPane.showMessageDialog(this, "Avaliação deletada com sucesso!");
                limparCampos();
                listarTodasAvaliacoes();
            }
        } catch (EntidadeNaoEncontradaException ex) {
            JOptionPane.showMessageDialog(this, "Erro: Avaliação não encontrada para exclusão. " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (DadoInvalidoException ex) {
            JOptionPane.showMessageDialog(this, "Erro de Validação: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao deletar avaliação: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void limparCampos() {
        campoId.setText("");
        campoNomeAvaliacao.setText("");
        campoDataInicio.setText("");
        campoDataFim.setText("");
        popularDisciplinas();
        areaResultados.setText("");
    }

    private void listarTodasAvaliacoes() {
        try {
            List<Avaliacao> avaliacoes = fachada.listarTodasAvaliacoes();
            if (avaliacoes.isEmpty()) {
                areaResultados.setText("Nenhuma avaliação cadastrada.");
            } else {
                StringBuilder sb = new StringBuilder("--- Lista de Avaliações ---\n");
                for (Avaliacao a : avaliacoes) {
                    sb.append("ID: ").append(a.getId())
                      .append(", Nome: ").append(a.getNomeAvaliacao())
                      .append(", Início: ").append(a.getDataInicio().format(dateFormatter))
                      .append(", Fim: ").append(a.getDataFim().format(dateFormatter));
                    if (a.getDisciplina() != null) {
                        sb.append(", Disciplina: ").append(a.getDisciplina().getNome());
                    }
                    sb.append("\n");
                }
                areaResultados.setText(sb.toString());
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao listar avaliações: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void exibirAvaliacao(Avaliacao avaliacao) {
        if (avaliacao != null) {
            campoId.setText(avaliacao.getId());
            campoNomeAvaliacao.setText(avaliacao.getNomeAvaliacao());
            campoDataInicio.setText(avaliacao.getDataInicio().format(dateFormatter));
            campoDataFim.setText(avaliacao.getDataFim().format(dateFormatter));
            popularDisciplinas();
            if (avaliacao.getDisciplina() != null) {
                for (int i = 0; i < comboDisciplinas.getItemCount(); i++) {
                    if (comboDisciplinas.getItemAt(i).getCodigo().equals(avaliacao.getDisciplina().getCodigo())) {
                        comboDisciplinas.setSelectedIndex(i);
                        break;
                    }
                }
            }
            areaResultados.setText(
                "--- Detalhes da Avaliação ---\n" +
                "ID: " + avaliacao.getId() + "\n" +
                "Nome: " + avaliacao.getNomeAvaliacao() + "\n" +
                "Data Início: " + avaliacao.getDataInicio().format(dateFormatter) + "\n" +
                "Data Fim: " + avaliacao.getDataFim().format(dateFormatter) + "\n" +
                "Disciplina: " + (avaliacao.getDisciplina() != null ? avaliacao.getDisciplina().getNome() : "N/A") + "\n"
            );
        } else {
            limparCampos();
            areaResultados.setText("Avaliação não encontrada.");
        }
    }
}