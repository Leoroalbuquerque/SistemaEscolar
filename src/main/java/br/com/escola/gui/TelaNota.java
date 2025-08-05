package br.com.escola.gui;

import br.com.escola.negocio.Fachada;
import br.com.escola.negocio.Nota;
import br.com.escola.negocio.Aluno;
import br.com.escola.negocio.Disciplina;
import br.com.escola.excecoes.DadoInvalidoException;
import br.com.escola.excecoes.EntidadeNaoEncontradaException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class TelaNota extends JFrame {

    private Fachada fachada;

    private JTextField txtMatriculaAluno;
    private JTextField txtCodigoDisciplina;
    private JTextField txtTipoAvaliacao;
    private JTextField txtValorNota;
    private JTextField txtDataLancamento;

    private JButton btnLancar;
    private JButton btnAtualizar;
    private JButton btnBuscar;
    private JButton btnDeletar;
    private JButton btnListarTodas;
    private JButton btnListarPorAluno;
    private JButton btnListarPorDisciplina;

    private JTable tabelaNotas;
    private DefaultTableModel tableModel;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public TelaNota() {
        super("Lançamento de Notas");
        this.fachada = Fachada.getInstance();
        initComponents();
        carregarTodasNotas();
    }

    private void initComponents() {
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel panelForm = new JPanel(new GridLayout(6, 2, 5, 5));
        panelForm.setBorder(BorderFactory.createTitledBorder("Dados da Nota"));

        txtMatriculaAluno = new JTextField();
        txtCodigoDisciplina = new JTextField();
        txtTipoAvaliacao = new JTextField();
        txtValorNota = new JTextField();
        txtDataLancamento = new JTextField();

        panelForm.add(new JLabel("Matrícula do Aluno:"));
        panelForm.add(txtMatriculaAluno);
        panelForm.add(new JLabel("Código da Disciplina:"));
        panelForm.add(txtCodigoDisciplina);
        panelForm.add(new JLabel("Tipo de Avaliação:"));
        panelForm.add(txtTipoAvaliacao);
        panelForm.add(new JLabel("Valor da Nota (0-100):"));
        panelForm.add(txtValorNota);
        panelForm.add(new JLabel("Data Lançamento (dd/MM/yyyy):"));
        panelForm.add(txtDataLancamento);

        JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnLancar = new JButton("Lançar Nota");
        btnAtualizar = new JButton("Atualizar Nota");
        btnBuscar = new JButton("Buscar Nota");
        btnDeletar = new JButton("Deletar Nota");
        btnListarTodas = new JButton("Listar Todas");
        btnListarPorAluno = new JButton("Listar Por Aluno");
        btnListarPorDisciplina = new JButton("Listar Por Disciplina");

        panelButtons.add(btnLancar);
        panelButtons.add(btnAtualizar);
        panelButtons.add(btnBuscar);
        panelButtons.add(btnDeletar);
        panelButtons.add(btnListarTodas);
        panelButtons.add(btnListarPorAluno);
        panelButtons.add(btnListarPorDisciplina);

        JPanel panelSuperior = new JPanel(new BorderLayout(10, 10));
        panelSuperior.add(panelForm, BorderLayout.NORTH);
        panelSuperior.add(panelButtons, BorderLayout.CENTER);

        tableModel = new DefaultTableModel(new Object[]{"Matrícula Aluno", "Nome Aluno", "Código Disciplina", "Nome Disciplina", "Tipo Avaliação", "Valor", "Data Lançamento"}, 0);
        tabelaNotas = new JTable(tableModel);
        tabelaNotas.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        JScrollPane scrollPane = new JScrollPane(tabelaNotas);

        this.add(panelSuperior, BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);

        btnLancar.addActionListener(this::lancarNota);
        btnAtualizar.addActionListener(this::atualizarNota);
        btnBuscar.addActionListener(this::buscarNota);
        btnDeletar.addActionListener(this::deletarNota);
        btnListarTodas.addActionListener(e -> carregarTodasNotas());
        btnListarPorAluno.addActionListener(this::listarNotasPorAluno);
        btnListarPorDisciplina.addActionListener(this::listarNotasPorDisciplina);

        tabelaNotas.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabelaNotas.getSelectedRow() != -1) {
                int selectedRow = tabelaNotas.getSelectedRow();
                txtMatriculaAluno.setText(tableModel.getValueAt(selectedRow, 0).toString());
                txtCodigoDisciplina.setText(tableModel.getValueAt(selectedRow, 2).toString());
                txtTipoAvaliacao.setText(tableModel.getValueAt(selectedRow, 4).toString());
                txtValorNota.setText(tableModel.getValueAt(selectedRow, 5).toString());
                txtDataLancamento.setText(tableModel.getValueAt(selectedRow, 6).toString());
            }
        });
    }

    private void lancarNota(ActionEvent e) {
        try {
            String matriculaAluno = txtMatriculaAluno.getText().trim();
            String codigoDisciplina = txtCodigoDisciplina.getText().trim();
            String tipoAvaliacao = txtTipoAvaliacao.getText().trim();
            String valorNotaStr = txtValorNota.getText().trim();
            String dataLancamentoStr = txtDataLancamento.getText().trim();

            if (matriculaAluno.isEmpty() || codigoDisciplina.isEmpty() || tipoAvaliacao.isEmpty() || valorNotaStr.isEmpty() || dataLancamentoStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos os campos devem ser preenchidos.", "Erro de Validação", JOptionPane.WARNING_MESSAGE);
                return;
            }

            double valorNota = Double.parseDouble(valorNotaStr);
            if (valorNota < 0 || valorNota > 100) {
                JOptionPane.showMessageDialog(this, "O valor da nota deve estar entre 0 e 100.", "Erro de Validação", JOptionPane.WARNING_MESSAGE);
                return;
            }

            LocalDate dataLancamento = LocalDate.parse(dataLancamentoStr, DATE_FORMATTER);

            Aluno alunoAssociado = fachada.buscarAluno(matriculaAluno);
            Disciplina disciplinaAssociada = fachada.buscarDisciplina(codigoDisciplina);

            Nota novaNota = new Nota(valorNota, tipoAvaliacao, dataLancamento, disciplinaAssociada, alunoAssociado);

            fachada.adicionarNota(novaNota);
            JOptionPane.showMessageDialog(this, "Nota lançada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparCampos();
            carregarTodasNotas();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Valor da nota inválido. Use apenas números (ex: 75.5).", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Formato de data inválido. Use dd/MM/yyyy.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (DadoInvalidoException | EntidadeNaoEncontradaException | IOException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao lançar nota: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void atualizarNota(ActionEvent e) {
        try {
            String matriculaAluno = txtMatriculaAluno.getText().trim();
            String codigoDisciplina = txtCodigoDisciplina.getText().trim();
            String tipoAvaliacao = txtTipoAvaliacao.getText().trim();
            String valorNotaStr = txtValorNota.getText().trim();
            String dataLancamentoStr = txtDataLancamento.getText().trim();

            if (matriculaAluno.isEmpty() || codigoDisciplina.isEmpty() || tipoAvaliacao.isEmpty() || valorNotaStr.isEmpty() || dataLancamentoStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos os campos devem ser preenchidos para atualização.", "Erro de Validação", JOptionPane.WARNING_MESSAGE);
                return;
            }

            double valorNota = Double.parseDouble(valorNotaStr);
            if (valorNota < 0 || valorNota > 100) {
                JOptionPane.showMessageDialog(this, "O valor da nota deve estar entre 0 e 100.", "Erro de Validação", JOptionPane.WARNING_MESSAGE);
                return;
            }

            LocalDate dataLancamento = LocalDate.parse(dataLancamentoStr, DATE_FORMATTER);

            Aluno alunoAssociado = fachada.buscarAluno(matriculaAluno);
            Disciplina disciplinaAssociada = fachada.buscarDisciplina(codigoDisciplina);

            Nota notaParaAtualizar = new Nota(valorNota, tipoAvaliacao, dataLancamento, disciplinaAssociada, alunoAssociado);

            fachada.atualizarNota(notaParaAtualizar);
            JOptionPane.showMessageDialog(this, "Nota atualizada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparCampos();
            carregarTodasNotas();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Valor da nota inválido. Use apenas números (ex: 75.5).", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Formato de data inválido. Use dd/MM/yyyy.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (DadoInvalidoException | EntidadeNaoEncontradaException | IOException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar nota: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarNota(ActionEvent e) {
        try {
            String matriculaAluno = txtMatriculaAluno.getText().trim();
            String codigoDisciplina = txtCodigoDisciplina.getText().trim();
            String tipoAvaliacao = txtTipoAvaliacao.getText().trim();
            String dataStr = txtDataLancamento.getText().trim();

            if (matriculaAluno.isEmpty() || codigoDisciplina.isEmpty() || tipoAvaliacao.isEmpty() || dataStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Matrícula, Código da Disciplina, Tipo de Avaliação e Data são necessários para buscar.", "Erro de Validação", JOptionPane.WARNING_MESSAGE);
                return;
            }

            LocalDate dataLancamento = LocalDate.parse(dataStr, DATE_FORMATTER);

            Nota nota = fachada.buscarNota(matriculaAluno, codigoDisciplina, tipoAvaliacao, dataLancamento);

            tableModel.setRowCount(0);
            adicionarNotaATabela(nota);

            txtValorNota.setText(String.valueOf(nota.getValor()));

            JOptionPane.showMessageDialog(this, "Nota encontrada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Formato de data inválido. Use dd/MM/yyyy.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (DadoInvalidoException | EntidadeNaoEncontradaException | IOException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao buscar nota: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            tableModel.setRowCount(0);
        }
    }

    private void deletarNota(ActionEvent e) {
        int selectedRow = tabelaNotas.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma nota na tabela para deletar.", "Erro", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String matriculaAluno = tableModel.getValueAt(selectedRow, 0).toString();
            String codigoDisciplina = tableModel.getValueAt(selectedRow, 2).toString();
            String tipoAvaliacao = tableModel.getValueAt(selectedRow, 4).toString();
            String dataStr = tableModel.getValueAt(selectedRow, 6).toString();
            
            LocalDate dataLancamento = LocalDate.parse(dataStr, DATE_FORMATTER);

            int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja deletar a nota selecionada?", "Confirmar Deleção", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                fachada.deletarNota(matriculaAluno, codigoDisciplina, tipoAvaliacao, dataLancamento);
                JOptionPane.showMessageDialog(this, "Nota deletada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                limparCampos();
                carregarTodasNotas();
            }
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Erro na conversão da data da tabela. Formato esperado dd/MM/yyyy.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (DadoInvalidoException | EntidadeNaoEncontradaException | IOException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao deletar nota: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarTodasNotas() {
        try {
            tableModel.setRowCount(0);
            List<Nota> notas = fachada.listarTodasNotas();
            if (notas.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nenhuma nota cadastrada.", "Informação", JOptionPane.INFORMATION_MESSAGE);
            } else {
                for (Nota nota : notas) {
                    adicionarNotaATabela(nota);
                }
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar notas: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void listarNotasPorAluno(ActionEvent e) {
        try {
            String matriculaAluno = txtMatriculaAluno.getText().trim();
            if (matriculaAluno.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Informe a matrícula do aluno para listar as notas.", "Erro de Validação", JOptionPane.WARNING_MESSAGE);
                return;
            }
            tableModel.setRowCount(0);
            List<Nota> notas = fachada.buscarNotasPorAluno(matriculaAluno);
            if (notas.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nenhuma nota encontrada para o aluno " + matriculaAluno + ".", "Consulta", JOptionPane.INFORMATION_MESSAGE);
            } else {
                for (Nota nota : notas) {
                    adicionarNotaATabela(nota);
                }
            }
        } catch (DadoInvalidoException | IOException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao listar notas por aluno: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void listarNotasPorDisciplina(ActionEvent e) {
        try {
            String codigoDisciplina = txtCodigoDisciplina.getText().trim();
            if (codigoDisciplina.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Informe o código da disciplina para listar as notas.", "Erro de Validação", JOptionPane.WARNING_MESSAGE);
                return;
            }
            tableModel.setRowCount(0);
            List<Nota> notas = fachada.buscarNotasPorDisciplina(codigoDisciplina);
            if (notas.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nenhuma nota encontrada para a disciplina " + codigoDisciplina + ".", "Consulta", JOptionPane.INFORMATION_MESSAGE);
            } else {
                for (Nota nota : notas) {
                    adicionarNotaATabela(nota);
                }
            }
        } catch (DadoInvalidoException | IOException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao listar notas por disciplina: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void adicionarNotaATabela(Nota nota) {
        String matriculaAluno = (nota.getAluno() != null) ? nota.getAluno().getMatricula() : "N/A";
        String nomeAluno = (nota.getAluno() != null) ? nota.getAluno().getNome() : "N/A";
        String codigoDisciplina = (nota.getDisciplina() != null) ? nota.getDisciplina().getCodigo() : "N/A";
        String nomeDisciplina = (nota.getDisciplina() != null) ? nota.getDisciplina().getNome() : "N/A";

        String dataFormatada = (nota.getDataLancamento() != null) ? nota.getDataLancamento().format(DATE_FORMATTER) : "N/A";

        tableModel.addRow(new Object[]{
            matriculaAluno,
            nomeAluno,
            codigoDisciplina,
            nomeDisciplina,
            nota.getTipoAvaliacao(),
            nota.getValor(),
            dataFormatada
        });
    }

    private void limparCampos() {
        txtMatriculaAluno.setText("");
        txtCodigoDisciplina.setText("");
        txtTipoAvaliacao.setText("");
        txtValorNota.setText("");
        txtDataLancamento.setText("");
        tabelaNotas.clearSelection();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new TelaNota().setVisible(true);
        });
    }
}