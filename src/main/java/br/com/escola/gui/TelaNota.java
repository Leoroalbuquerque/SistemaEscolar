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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TelaNota extends JFrame {

    private Fachada fachada;

    private JTextField txtMatriculaAluno;
    private JTextField txtCodigoDisciplina;
    private JTextField txtTipoAvaliacao;
    private JTextField txtValorNota;
    private JTextField txtDataLancamento; // Formato dd/MM/yyyy

    private JButton btnLancar;
    private JButton btnAtualizar;
    private JButton btnBuscar;
    private JButton btnDeletar;
    private JButton btnListarTodas;
    private JButton btnListarPorAluno;
    private JButton btnListarPorDisciplina;

    private JTable tabelaNotas;
    private DefaultTableModel tableModel;

    public TelaNota() {
        super("Lançamento de Notas");
        this.fachada = Fachada.getInstance();
        initComponents();
        carregarTodasNotas();
    }

    private void initComponents() {
        // Configurações da janela
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        // Não defina um layout aqui se for usar os add(..., BorderLayout.XYZ) depois
        // setLayout(new BorderLayout(10, 10)); // Comentado para usar o padrão que permite add com BorderLayout constants

        // Painel de entrada de dados
        JPanel panelForm = new JPanel(new GridLayout(6, 2, 5, 5));
        panelForm.setBorder(BorderFactory.createTitledBorder("Dados da Nota"));

        txtMatriculaAluno = new JTextField();
        txtCodigoDisciplina = new JTextField();
        txtTipoAvaliacao = new JTextField();
        txtValorNota = new JTextField();
        txtDataLancamento = new JTextField(); // Formato dd/MM/yyyy

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

        // Painel de botões de ação
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

        // --- NOVO PAINEL SUPERIOR PARA FORMULÁRIO E BOTÕES ---
        JPanel panelSuperior = new JPanel(new BorderLayout(10, 10)); // Espaçamento entre componentes
        panelSuperior.add(panelForm, BorderLayout.NORTH);
        panelSuperior.add(panelButtons, BorderLayout.CENTER);
        // ----------------------------------------------------

        // Tabela para exibir notas
        tableModel = new DefaultTableModel(new Object[]{"Matrícula Aluno", "Nome Aluno", "Código Disciplina", "Nome Disciplina", "Tipo Avaliação", "Valor", "Data Lançamento"}, 0);
        tabelaNotas = new JTable(tableModel);
        // Configurações para redimensionamento de colunas (opcional, mas bom para tabelas)
        tabelaNotas.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        JScrollPane scrollPane = new JScrollPane(tabelaNotas);

        // Adicionar painéis à janela principal (JFrame)
        this.add(panelSuperior, BorderLayout.NORTH); // Adiciona o painel superior (formulário + botões) ao NORTE
        this.add(scrollPane, BorderLayout.CENTER);   // Adiciona a tabela ao CENTRO (ela se expandirá)

        // Adicionar listeners (mantidos os mesmos)
        btnLancar.addActionListener(this::lancarNota);
        btnAtualizar.addActionListener(this::atualizarNota);
        btnBuscar.addActionListener(this::buscarNota);
        btnDeletar.addActionListener(this::deletarNota);
        btnListarTodas.addActionListener(e -> carregarTodasNotas());
        btnListarPorAluno.addActionListener(this::listarNotasPorAluno);
        btnListarPorDisciplina.addActionListener(this::listarNotasPorDisciplina);

        // Listener para preencher campos ao selecionar linha na tabela
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
            String matriculaAluno = txtMatriculaAluno.getText();
            String codigoDisciplina = txtCodigoDisciplina.getText();
            String tipoAvaliacao = txtTipoAvaliacao.getText();
            double valorNota = Double.parseDouble(txtValorNota.getText());
            Date dataLancamento = new SimpleDateFormat("dd/MM/yyyy").parse(txtDataLancamento.getText());

            if (matriculaAluno.isEmpty() || codigoDisciplina.isEmpty() || tipoAvaliacao.isEmpty() || txtValorNota.getText().isEmpty() || txtDataLancamento.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos os campos devem ser preenchidos.", "Erro de Validação", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (valorNota < 0 || valorNota > 100) {
                JOptionPane.showMessageDialog(this, "O valor da nota deve estar entre 0 e 100.", "Erro de Validação", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // AQUI É ONDE VOCÊ PRECISA GARANTIR QUE OS OBJETOS ALUNO E DISCIPLINA SÃO ENCONTRADOS
            // E ATRIBUÍDOS À NOTA ANTES DE PASSAR PARA A FACHADA.
            // Atualmente, sua NotaServico faz isso, mas se o Fachada.adicionarNota receber Nota com null
            // para Aluno/Disciplina, a validação no NotaServico fará as buscas.
            // Se você quiser que a GUI busque e atribua, o código abaixo é um exemplo:
            Aluno alunoAssociado = fachada.buscarAluno(matriculaAluno);
            Disciplina disciplinaAssociada = fachada.buscarDisciplina(codigoDisciplina);

            // Cria a nota com os objetos Aluno e Disciplina encontrados
            Nota novaNota = new Nota(valorNota, tipoAvaliacao, dataLancamento, disciplinaAssociada, alunoAssociado);

            fachada.adicionarNota(novaNota);
            JOptionPane.showMessageDialog(this, "Nota lançada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparCampos();
            carregarTodasNotas();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Valor da nota inválido. Use apenas números.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(this, "Formato de data inválido. Use dd/MM/yyyy.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (DadoInvalidoException | EntidadeNaoEncontradaException | IOException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao lançar nota: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void atualizarNota(ActionEvent e) {
        try {
            String matriculaAluno = txtMatriculaAluno.getText();
            String codigoDisciplina = txtCodigoDisciplina.getText();
            String tipoAvaliacao = txtTipoAvaliacao.getText();
            double valorNota = Double.parseDouble(txtValorNota.getText());
            Date dataLancamento = new SimpleDateFormat("dd/MM/yyyy").parse(txtDataLancamento.getText());

            if (matriculaAluno.isEmpty() || codigoDisciplina.isEmpty() || tipoAvaliacao.isEmpty() || txtValorNota.getText().isEmpty() || txtDataLancamento.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos os campos devem ser preenchidos para atualização.", "Erro de Validação", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (valorNota < 0 || valorNota > 100) {
                JOptionPane.showMessageDialog(this, "O valor da nota deve estar entre 0 e 100.", "Erro de Validação", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Busca os objetos completos Aluno e Disciplina
            Aluno alunoAssociado = fachada.buscarAluno(matriculaAluno);
            Disciplina disciplinaAssociada = fachada.buscarDisciplina(codigoDisciplina);

            // Cria uma nova Nota com os objetos completos e o valor atualizado
            Nota notaParaAtualizar = new Nota(valorNota, tipoAvaliacao, dataLancamento, disciplinaAssociada, alunoAssociado);

            fachada.atualizarNota(notaParaAtualizar);
            JOptionPane.showMessageDialog(this, "Nota atualizada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparCampos();
            carregarTodasNotas();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Valor da nota inválido. Use apenas números.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(this, "Formato de data inválido. Use dd/MM/yyyy.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (DadoInvalidoException | EntidadeNaoEncontradaException | IOException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar nota: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void buscarNota(ActionEvent e) {
        try {
            String matriculaAluno = txtMatriculaAluno.getText();
            String codigoDisciplina = txtCodigoDisciplina.getText();
            String tipoAvaliacao = txtTipoAvaliacao.getText();
            String dataStr = txtDataLancamento.getText();

            if (matriculaAluno.isEmpty() || codigoDisciplina.isEmpty() || tipoAvaliacao.isEmpty() || dataStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Matrícula, Código da Disciplina, Tipo de Avaliação e Data são necessários para buscar.", "Erro de Validação", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Date dataLancamento = new SimpleDateFormat("dd/MM/yyyy").parse(dataStr);

            Nota nota = fachada.buscarNota(matriculaAluno, codigoDisciplina, tipoAvaliacao, dataLancamento);

            tableModel.setRowCount(0);
            adicionarNotaATabela(nota); // Adiciona a nota encontrada à tabela

            // Preenche os campos com os dados da nota encontrada
            txtValorNota.setText(String.valueOf(nota.getValor()));
            // Os outros campos (matrícula, código, tipo, data) já foram usados para a busca
            // e provavelmente já estão preenchidos na interface.

            JOptionPane.showMessageDialog(this, "Nota encontrada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(this, "Formato de data inválido. Use dd/MM/yyyy.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (DadoInvalidoException | EntidadeNaoEncontradaException | IOException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao buscar nota: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            tableModel.setRowCount(0); // Limpa a tabela se a busca falhar
        }
    }


    private void deletarNota(ActionEvent e) {
        try {
            String matriculaAluno = txtMatriculaAluno.getText();
            String codigoDisciplina = txtCodigoDisciplina.getText();
            String tipoAvaliacao = txtTipoAvaliacao.getText();
            String dataStr = txtDataLancamento.getText();

            if (matriculaAluno.isEmpty() || codigoDisciplina.isEmpty() || tipoAvaliacao.isEmpty() || dataStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Matrícula, Código da Disciplina, Tipo de Avaliação e Data são necessários para deletar.", "Erro de Validação", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Date dataLancamento = new SimpleDateFormat("dd/MM/yyyy").parse(dataStr);

            int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja deletar esta nota?", "Confirmar Deleção", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                fachada.deletarNota(matriculaAluno, codigoDisciplina, tipoAvaliacao, dataLancamento);
                JOptionPane.showMessageDialog(this, "Nota deletada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                limparCampos();
                carregarTodasNotas();
            }
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(this, "Formato de data inválido. Use dd/MM/yyyy.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
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
        } catch (IOException ex) { // Adicionado catch para IOException aqui, pois listarTodasNotas pode lançar
            JOptionPane.showMessageDialog(this, "Erro ao carregar notas: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void listarNotasPorAluno(ActionEvent e) {
        try {
            String matriculaAluno = txtMatriculaAluno.getText();
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
        } catch (DadoInvalidoException | IOException ex) { // IOException adicionado
            JOptionPane.showMessageDialog(this, "Erro ao listar notas por aluno: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void listarNotasPorDisciplina(ActionEvent e) {
        try {
            String codigoDisciplina = txtCodigoDisciplina.getText();
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
        } catch (DadoInvalidoException | IOException ex) { // IOException adicionado
            JOptionPane.showMessageDialog(this, "Erro ao listar notas por disciplina: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void adicionarNotaATabela(Nota nota) {
        String nomeAluno = "N/A";
        String nomeDisciplina = "N/A";
        try {
            // É mais eficiente se a Nota já viesse com os objetos Aluno e Disciplina completos.
            // No entanto, para a estrutura atual, buscar novamente aqui pode ser necessário.
            // Certifique-se de que nota.getAluno() e nota.getDisciplina() não retornem null
            // antes de tentar acessar seus métodos (getMatricula(), getCodigo()).
            if (nota.getAluno() != null && nota.getAluno().getMatricula() != null) {
                Aluno aluno = fachada.buscarAluno(nota.getAluno().getMatricula());
                nomeAluno = aluno.getNome();
            }
        } catch (EntidadeNaoEncontradaException | DadoInvalidoException | IOException e) {
            System.err.println("Erro ao buscar aluno para exibição na tabela: " + e.getMessage());
        }
        try {
            if (nota.getDisciplina() != null && nota.getDisciplina().getCodigo() != null) {
                Disciplina disciplina = fachada.buscarDisciplina(nota.getDisciplina().getCodigo());
                nomeDisciplina = disciplina.getNome();
            }
        } catch (EntidadeNaoEncontradaException | DadoInvalidoException | IOException e) {
            System.err.println("Erro ao buscar disciplina para exibição na tabela: " + e.getMessage());
        }

        tableModel.addRow(new Object[]{
                nota.getAluno() != null ? nota.getAluno().getMatricula() : "N/A", // Evita NullPointerException
                nomeAluno,
                nota.getDisciplina() != null ? nota.getDisciplina().getCodigo() : "N/A", // Evita NullPointerException
                nomeDisciplina,
                nota.getTipoAvaliacao(),
                nota.getValor(),
                new SimpleDateFormat("dd/MM/yyyy").format(nota.getDataLancamento())
        });
    }


    private void limparCampos() {
        txtMatriculaAluno.setText("");
        txtCodigoDisciplina.setText("");
        txtTipoAvaliacao.setText("");
        txtValorNota.setText("");
        txtDataLancamento.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new TelaNota().setVisible(true);
        });
    }
}