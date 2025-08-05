package br.com.escola.gui;

import br.com.escola.negocio.Funcionario;
import br.com.escola.negocio.IFachada;
import br.com.escola.negocio.Fachada;
import br.com.escola.excecoes.DadoInvalidoException;
import br.com.escola.excecoes.EntidadeNaoEncontradaException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.io.IOException;

public class TelaCadastroFuncionario extends JDialog {

    private final IFachada fachada;
    private JTextField campoNome, campoCPF, campoTelefone, campoEmail, campoMatriculaFuncional, campoCargo, campoSalario;
    private JButton btnAdicionar, btnAtualizar, btnBuscar, btnDeletar, btnLimpar;
    private JTable tabelaFuncionarios;
    private DefaultTableModel modeloTabela;

    public TelaCadastroFuncionario(Frame owner, boolean modal) {
        super(owner, "Cadastro de Funcionários", modal);
        this.fachada = Fachada.getInstance();
        initComponents();
        carregarFuncionariosNaTabela();
        configurarListeners();
    }

    private void initComponents() {
        setSize(800, 600);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());

        JPanel painelFormulario = new JPanel(new GridLayout(7, 2, 10, 10));
        painelFormulario.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        painelFormulario.add(new JLabel("Nome:"));
        campoNome = new JTextField();
        painelFormulario.add(campoNome);

        painelFormulario.add(new JLabel("CPF:"));
        campoCPF = new JTextField();
        painelFormulario.add(campoCPF);

        painelFormulario.add(new JLabel("Telefone:"));
        campoTelefone = new JTextField();
        painelFormulario.add(campoTelefone);

        painelFormulario.add(new JLabel("Email:"));
        campoEmail = new JTextField();
        painelFormulario.add(campoEmail);

        painelFormulario.add(new JLabel("Matrícula Funcional:"));
        campoMatriculaFuncional = new JTextField();
        painelFormulario.add(campoMatriculaFuncional);

        painelFormulario.add(new JLabel("Cargo:"));
        campoCargo = new JTextField();
        painelFormulario.add(campoCargo);

        painelFormulario.add(new JLabel("Salário:"));
        campoSalario = new JTextField();
        painelFormulario.add(campoSalario);

        add(painelFormulario, BorderLayout.NORTH);

        String[] colunas = {"Matrícula", "Nome", "Cargo", "Salário"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelaFuncionarios = new JTable(modeloTabela);
        JScrollPane scrollPane = new JScrollPane(tabelaFuncionarios);
        add(scrollPane, BorderLayout.CENTER);

        tabelaFuncionarios.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabelaFuncionarios.getSelectedRow() != -1) {
                preencherCamposComDadosDaTabela();
            }
        });

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnAdicionar = new JButton("Adicionar");
        btnAtualizar = new JButton("Atualizar");
        btnBuscar = new JButton("Buscar");
        btnDeletar = new JButton("Deletar");
        btnLimpar = new JButton("Limpar");

        painelBotoes.add(btnAdicionar);
        painelBotoes.add(btnAtualizar);
        painelBotoes.add(btnBuscar);
        painelBotoes.add(btnDeletar);
        painelBotoes.add(btnLimpar);

        add(painelBotoes, BorderLayout.SOUTH);
    }

    private void configurarListeners() {
        btnAdicionar.addActionListener(e -> adicionarFuncionario());
        btnAtualizar.addActionListener(e -> atualizarFuncionario());
        btnBuscar.addActionListener(e -> buscarFuncionario());
        btnDeletar.addActionListener(e -> deletarFuncionario());
        btnLimpar.addActionListener(e -> limparCampos());
    }

    private void preencherCamposComDadosDaTabela() {
        int linhaSelecionada = tabelaFuncionarios.getSelectedRow();
        if (linhaSelecionada >= 0) {
            String matricula = (String) modeloTabela.getValueAt(linhaSelecionada, 0);
            try {
                Funcionario funcionario = fachada.buscarFuncionario(matricula);
                campoNome.setText(funcionario.getNome());
                campoCPF.setText(funcionario.getCpf());
                campoTelefone.setText(funcionario.getTelefone());
                campoEmail.setText(funcionario.getEmail());
                campoMatriculaFuncional.setText(funcionario.getMatriculaFuncional());
                campoCargo.setText(funcionario.getCargo());
                campoSalario.setText(String.valueOf(funcionario.getSalario()));
            } catch (EntidadeNaoEncontradaException | DadoInvalidoException | IOException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao buscar funcionário: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void adicionarFuncionario() {
        try {
            String nome = campoNome.getText().trim();
            String cpf = campoCPF.getText().trim();
            String telefone = campoTelefone.getText().trim();
            String email = campoEmail.getText().trim();
            String matriculaFuncional = campoMatriculaFuncional.getText().trim();
            String cargo = campoCargo.getText().trim();

            if (nome.isEmpty() || cpf.isEmpty() || matriculaFuncional.isEmpty() || cargo.isEmpty() || campoSalario.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nome, CPF, Matrícula, Cargo e Salário são obrigatórios.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double salario = 0.0;
            try {
                salario = Double.parseDouble(campoSalario.getText().trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Salário inválido. Insira um valor numérico.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Funcionario funcionario = new Funcionario(nome, cpf, telefone, email, matriculaFuncional, cargo, salario);
            fachada.adicionarFuncionario(funcionario);
            JOptionPane.showMessageDialog(this, "Funcionário adicionado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparCampos();
            carregarFuncionariosNaTabela();
        } catch (DadoInvalidoException | IOException e) {
            JOptionPane.showMessageDialog(this, "Erro ao adicionar funcionário: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void atualizarFuncionario() {
        try {
            String nome = campoNome.getText().trim();
            String cpf = campoCPF.getText().trim();
            String telefone = campoTelefone.getText().trim();
            String email = campoEmail.getText().trim();
            String matriculaFuncional = campoMatriculaFuncional.getText().trim();
            String cargo = campoCargo.getText().trim();

            if (nome.isEmpty() || cpf.isEmpty() || matriculaFuncional.isEmpty() || cargo.isEmpty() || campoSalario.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos os campos são obrigatórios para atualização.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double salario = 0.0;
            try {
                salario = Double.parseDouble(campoSalario.getText().trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Salário inválido. Insira um valor numérico.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Funcionario funcionario = new Funcionario(nome, cpf, telefone, email, matriculaFuncional, cargo, salario);
            fachada.atualizarFuncionario(funcionario);
            JOptionPane.showMessageDialog(this, "Funcionário atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparCampos();
            carregarFuncionariosNaTabela();
        } catch (DadoInvalidoException | EntidadeNaoEncontradaException | IOException e) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar funcionário: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarFuncionario() {
        String matricula = campoMatriculaFuncional.getText().trim();
        if (matricula.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Insira uma matrícula para buscar.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Funcionario funcionario = fachada.buscarFuncionario(matricula);
            limparCampos();
            campoNome.setText(funcionario.getNome());
            campoCPF.setText(funcionario.getCpf());
            campoTelefone.setText(funcionario.getTelefone());
            campoEmail.setText(funcionario.getEmail());
            campoMatriculaFuncional.setText(funcionario.getMatriculaFuncional());
            campoCargo.setText(funcionario.getCargo());
            campoSalario.setText(String.valueOf(funcionario.getSalario()));
        } catch (EntidadeNaoEncontradaException | DadoInvalidoException | IOException e) {
            JOptionPane.showMessageDialog(this, "Erro ao buscar funcionário: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deletarFuncionario() {
        String matricula = campoMatriculaFuncional.getText().trim();
        if (matricula.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Insira uma matrícula para deletar.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja deletar este funcionário?", "Confirmar Deleção", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                fachada.deletarFuncionario(matricula);
                JOptionPane.showMessageDialog(this, "Funcionário deletado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                limparCampos();
                carregarFuncionariosNaTabela();
            } catch (EntidadeNaoEncontradaException | DadoInvalidoException | IOException e) {
                JOptionPane.showMessageDialog(this, "Erro ao deletar funcionário: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void limparCampos() {
        campoNome.setText("");
        campoCPF.setText("");
        campoTelefone.setText("");
        campoEmail.setText("");
        campoMatriculaFuncional.setText("");
        campoCargo.setText("");
        campoSalario.setText("");
    }

    private void carregarFuncionariosNaTabela() {
        modeloTabela.setRowCount(0);
        try {
            List<Funcionario> funcionarios = fachada.listarTodosFuncionarios();
            for (Funcionario funcionario : funcionarios) {
                modeloTabela.addRow(new Object[]{
                    funcionario.getMatriculaFuncional(),
                    funcionario.getNome(),
                    funcionario.getCargo(),
                    funcionario.getSalario()
                });
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar a lista de funcionários: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}