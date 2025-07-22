package br.com.escola.gui;

import br.com.escola.negocio.Aluno;
import br.com.escola.negocio.Disciplina;
import br.com.escola.negocio.Fachada;
import br.com.escola.negocio.Frequencia;
import br.com.escola.excecoes.DadoInvalidoException;
import br.com.escola.excecoes.EntidadeNaoEncontradaException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Vector;

public class TelaFrequencia extends JFrame {

    private final Fachada fachada;
    private JTextField txtMatriculaAluno;
    private JTextField txtCodigoDisciplina;
    private JTextField txtDataAula;
    private JRadioButton rbPresente;
    private JRadioButton rbAusente;
    private ButtonGroup bgPresenca;
    private JTextField txtJustificativa;
    private JTable tabelaFrequencias;
    private DefaultTableModel modeloTabela;

    public TelaFrequencia() {
        super("Registro de Frequência");
        this.fachada = Fachada.getInstance();
        initComponents();
        carregarTabelaFrequencias();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        JPanel painelFormulario = new JPanel(new GridLayout(7, 2, 5, 5));
        painelFormulario.setBorder(BorderFactory.createTitledBorder("Dados da Frequência"));

        painelFormulario.add(new JLabel("Matrícula do Aluno:"));
        txtMatriculaAluno = new JTextField();
        painelFormulario.add(txtMatriculaAluno);

        painelFormulario.add(new JLabel("Código da Disciplina:"));
        txtCodigoDisciplina = new JTextField();
        painelFormulario.add(txtCodigoDisciplina);

        painelFormulario.add(new JLabel("Data da Aula (DD/MM/AAAA):"));
        txtDataAula = new JTextField();
        painelFormulario.add(txtDataAula);

        painelFormulario.add(new JLabel("Presença:"));
        JPanel painelPresenca = new JPanel(new FlowLayout(FlowLayout.LEFT));
        rbPresente = new JRadioButton("Presente");
        rbAusente = new JRadioButton("Ausente");
        bgPresenca = new ButtonGroup();
        bgPresenca.add(rbPresente);
        bgPresenca.add(rbAusente);
        painelPresenca.add(rbPresente);
        painelPresenca.add(rbAusente);
        painelFormulario.add(painelPresenca);

        painelFormulario.add(new JLabel("Justificativa (se ausente):"));
        txtJustificativa = new JTextField();
        painelFormulario.add(txtJustificativa);

        JButton btnRegistrar = new JButton("Registrar/Atualizar Frequência");
        btnRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrarOuAtualizarFrequencia();
            }
        });
        painelFormulario.add(btnRegistrar);

        JButton btnJustificar = new JButton("Justificar Falta");
        btnJustificar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                justificarFalta();
            }
        });
        painelFormulario.add(btnJustificar);

        JButton btnLimparCampos = new JButton("Limpar Campos");
        btnLimparCampos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limparCampos();
            }
        });
        painelFormulario.add(btnLimparCampos);

        JButton btnDeletar = new JButton("Deletar Frequência");
        btnDeletar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deletarFrequenciaSelecionada();
            }
        });
        painelFormulario.add(btnDeletar);

        add(painelFormulario, BorderLayout.NORTH);

        modeloTabela = new DefaultTableModel(new Object[]{"Matrícula", "Aluno", "Disciplina", "Data", "Presença", "Justificativa"}, 0);
        tabelaFrequencias = new JTable(modeloTabela);
        JScrollPane scrollPane = new JScrollPane(tabelaFrequencias);
        add(scrollPane, BorderLayout.CENTER);

        tabelaFrequencias.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabelaFrequencias.getSelectedRow() != -1) {
                carregarFrequenciaParaEdicao();
            }
        });

        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void carregarTabelaFrequencias() {
        modeloTabela.setRowCount(0);
        try {
            List<Frequencia> frequencias = fachada.listarTodasFrequencias();
            if (frequencias.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nenhuma frequência registrada.", "Informação", JOptionPane.INFORMATION_MESSAGE);
            }
            for (Frequencia f : frequencias) {
                String nomeAluno = (f.getAluno() != null) ? f.getAluno().getNome() : "N/A";
                String nomeDisciplina = (f.getDisciplina() != null) ? f.getDisciplina().getNome() : "N/A";
                modeloTabela.addRow(new Object[]{
                        f.getAluno().getMatricula(),
                        nomeAluno,
                        f.getDisciplina().getCodigo(),
                        nomeDisciplina,
                        f.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                        f.isPresenca() ? "Presente" : "Ausente",
                        f.getJustificativa()
                });
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar frequências: " + e.getMessage(), "Erro de I/O", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void registrarOuAtualizarFrequencia() {
        String matriculaAluno = txtMatriculaAluno.getText().trim();
        String codigoDisciplina = txtCodigoDisciplina.getText().trim();
        String dataStr = txtDataAula.getText().trim();
        boolean presenca = rbPresente.isSelected();
        LocalDate dataAula;

        try {
            if (matriculaAluno.isEmpty() || codigoDisciplina.isEmpty() || dataStr.isEmpty()) {
                throw new DadoInvalidoException("Todos os campos de matrícula, disciplina e data são obrigatórios.");
            }

            dataAula = LocalDate.parse(dataStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

            fachada.registrarFrequencia(matriculaAluno, codigoDisciplina, dataAula, presenca);
            JOptionPane.showMessageDialog(this, "Frequência registrada/atualizada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparCampos();
            carregarTabelaFrequencias();
        } catch (DadoInvalidoException | EntidadeNaoEncontradaException e) {
            JOptionPane.showMessageDialog(this, "Erro ao registrar frequência: " + e.getMessage(), "Erro de Validação/Entidade", JOptionPane.WARNING_MESSAGE);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Formato de data inválido. Use DD/MM/AAAA.", "Erro de Formato", JOptionPane.WARNING_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erro de I/O ao registrar frequência: " + e.getMessage(), "Erro de I/O", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ocorreu um erro inesperado: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void justificarFalta() {
        String matriculaAluno = txtMatriculaAluno.getText().trim();
        String codigoDisciplina = txtCodigoDisciplina.getText().trim();
        String dataStr = txtDataAula.getText().trim();
        String justificativa = txtJustificativa.getText().trim();
        LocalDate dataAula;

        try {
            if (matriculaAluno.isEmpty() || codigoDisciplina.isEmpty() || dataStr.isEmpty() || justificativa.isEmpty()) {
                throw new DadoInvalidoException("Todos os campos de matrícula, disciplina, data e justificativa são obrigatórios para justificar.");
            }

            dataAula = LocalDate.parse(dataStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

            fachada.justificarFalta(matriculaAluno, codigoDisciplina, dataAula, justificativa);
            JOptionPane.showMessageDialog(this, "Falta justificada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparCampos();
            carregarTabelaFrequencias();
        } catch (DadoInvalidoException | EntidadeNaoEncontradaException e) {
            JOptionPane.showMessageDialog(this, "Erro ao justificar falta: " + e.getMessage(), "Erro de Validação/Entidade", JOptionPane.WARNING_MESSAGE);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Formato de data inválido. Use DD/MM/AAAA.", "Erro de Formato", JOptionPane.WARNING_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erro de I/O ao justificar falta: " + e.getMessage(), "Erro de I/O", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ocorreu um erro inesperado: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void deletarFrequenciaSelecionada() {
        int linhaSelecionada = tabelaFrequencias.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma frequência na tabela para deletar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String matriculaAluno = (String) modeloTabela.getValueAt(linhaSelecionada, 0);
        String codigoDisciplina = (String) modeloTabela.getValueAt(linhaSelecionada, 2);
        String dataStr = (String) modeloTabela.getValueAt(linhaSelecionada, 4);
        LocalDate dataAula;

        int confirmacao = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja deletar esta frequência?", "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
        if (confirmacao == JOptionPane.YES_OPTION) {
            try {
                dataAula = LocalDate.parse(dataStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                fachada.deletarFrequencia(matriculaAluno, codigoDisciplina, dataAula);
                JOptionPane.showMessageDialog(this, "Frequência deletada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                limparCampos();
                carregarTabelaFrequencias();
            } catch (DadoInvalidoException | EntidadeNaoEncontradaException e) {
                JOptionPane.showMessageDialog(this, "Erro ao deletar frequência: " + e.getMessage(), "Erro de Validação/Entidade", JOptionPane.WARNING_MESSAGE);
            } catch (DateTimeParseException e) {
                JOptionPane.showMessageDialog(this, "Erro ao converter data para exclusão. Formato inválido na tabela.", "Erro Interno", JOptionPane.ERROR_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Erro de I/O ao deletar frequência: " + e.getMessage(), "Erro de I/O", JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Ocorreu um erro inesperado: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    private void carregarFrequenciaParaEdicao() {
        int linhaSelecionada = tabelaFrequencias.getSelectedRow();
        if (linhaSelecionada != -1) {
            txtMatriculaAluno.setText((String) modeloTabela.getValueAt(linhaSelecionada, 0));
            txtCodigoDisciplina.setText((String) modeloTabela.getValueAt(linhaSelecionada, 2));
            txtDataAula.setText((String) modeloTabela.getValueAt(linhaSelecionada, 4));

            String presencaStr = (String) modeloTabela.getValueAt(linhaSelecionada, 5);
            if ("Presente".equals(presencaStr)) {
                rbPresente.setSelected(true);
            } else {
                rbAusente.setSelected(true);
            }

            String justificativa = (String) modeloTabela.getValueAt(linhaSelecionada, 6);
            txtJustificativa.setText(justificativa != null ? justificativa : "");
        }
    }

    private void limparCampos() {
        txtMatriculaAluno.setText("");
        txtCodigoDisciplina.setText("");
        txtDataAula.setText("");
        bgPresenca.clearSelection();
        txtJustificativa.setText("");
        tabelaFrequencias.clearSelection();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new TelaFrequencia().setVisible(true);
        });
    }
}