package br.com.escola.gui;

import br.com.escola.negocio.Professor;
import br.com.escola.negocio.Fachada;
import br.com.escola.negocio.IFachada;
import br.com.escola.excecoes.DadoInvalidoException;
import br.com.escola.excecoes.EntidadeNaoEncontradaException;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TelaCadastroProfessor extends JDialog {

    private JTextField campoRegistroFuncional;
    private JTextField campoNome;
    private JTextField campoCPF; // Adicionado para CPF do Professor
    private JTextField campoTelefone;
    private JTextField campoEmail;
    private JTextField campoEspecialidade; // Corrigido para Especialidade
    private JTextField campoSalario;

    private JButton btnAdicionar;
    private JButton btnBuscar;
    private JButton btnAtualizar;
    private JButton btnDeletar;
    private JButton btnLimpar;
    private JButton btnListarTodos;

    private JTextArea areaResultados;

    private IFachada fachada;

    public TelaCadastroProfessor(JFrame parent, boolean modal) {
        super(parent, "Cadastro de Professores", modal);

        this.fachada = Fachada.getInstance();

        setSize(700, 650); // Ajustei a altura para o novo campo CPF
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parent);

        JPanel panelPrincipal = new JPanel(new BorderLayout());
        JPanel panelForm = new JPanel(new GridLayout(7, 2, 10, 10)); // Ajustado para 7 linhas
        panelForm.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        panelForm.add(new JLabel("Registro Funcional:"));
        campoRegistroFuncional = new JTextField(20);
        panelForm.add(campoRegistroFuncional);

        panelForm.add(new JLabel("Nome:"));
        campoNome = new JTextField(20);
        panelForm.add(campoNome);

        panelForm.add(new JLabel("CPF:")); // Novo campo CPF
        campoCPF = new JTextField(20);
        panelForm.add(campoCPF);

        panelForm.add(new JLabel("Telefone:"));
        campoTelefone = new JTextField(20);
        panelForm.add(campoTelefone);

        panelForm.add(new JLabel("Email:"));
        campoEmail = new JTextField(20);
        panelForm.add(campoEmail);

        panelForm.add(new JLabel("Especialidade:")); // Corrigido para Especialidade
        campoEspecialidade = new JTextField(20);
        panelForm.add(campoEspecialidade);

        panelForm.add(new JLabel("Salário:"));
        campoSalario = new JTextField(20);
        panelForm.add(campoSalario);

        JPanel panelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnAdicionar = new JButton("Adicionar");
        btnBuscar = new JButton("Buscar");
        btnAtualizar = new JButton("Atualizar");
        btnDeletar = new JButton("Deletar");
        btnLimpar = new JButton("Limpar Campos");
        btnListarTodos = new JButton("Listar Todos");

        panelBotoes.add(btnAdicionar);
        panelBotoes.add(btnBuscar);
        panelBotoes.add(btnAtualizar);
        panelBotoes.add(btnDeletar);
        panelBotoes.add(btnLimpar);
        panelBotoes.add(btnListarTodos);

        areaResultados = new JTextArea(10, 50);
        areaResultados.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(areaResultados);

        panelPrincipal.add(panelForm, BorderLayout.NORTH);
        panelPrincipal.add(panelBotoes, BorderLayout.CENTER);
        panelPrincipal.add(scrollPane, BorderLayout.SOUTH);

        add(panelPrincipal);

        btnAdicionar.addActionListener(e -> adicionarProfessor());
        btnBuscar.addActionListener(e -> buscarProfessor());
        btnAtualizar.addActionListener(e -> atualizarProfessor());
        btnDeletar.addActionListener(e -> deletarProfessor());
        btnLimpar.addActionListener(e -> limparCampos());
        btnListarTodos.addActionListener(e -> listarTodosProfessores());
    }

    private void adicionarProfessor() {
        try {
            String registroFuncional = campoRegistroFuncional.getText();
            String nome = campoNome.getText();
            String cpf = campoCPF.getText(); // Novo campo
            String telefonePessoa = campoTelefone.getText();
            String emailPessoa = campoEmail.getText();
            String especialidade = campoEspecialidade.getText(); // Corrigido

            double salario = 0.0;
            try {
                salario = Double.parseDouble(campoSalario.getText());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Salário inválido. Use um número.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Professor professor = new Professor(nome, cpf, telefonePessoa,
                                                emailPessoa, registroFuncional, especialidade, salario);

            fachada.adicionarProfessor(professor);
            JOptionPane.showMessageDialog(this, "Professor adicionado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparCampos();
            listarTodosProfessores();
        } catch (DadoInvalidoException e) {
            JOptionPane.showMessageDialog(this, "Erro ao adicionar professor: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ocorreu um erro inesperado: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void buscarProfessor() {
        try {
            String registroFuncional = campoRegistroFuncional.getText();
            if (registroFuncional.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Informe o Registro Funcional para buscar.", "Campo Vazio", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Professor professor = fachada.buscarProfessor(registroFuncional);
            exibirProfessor(professor);
        } catch (EntidadeNaoEncontradaException e) {
            JOptionPane.showMessageDialog(this, "Professor não encontrado: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            limparCampos();
        } catch (DadoInvalidoException e) {
            JOptionPane.showMessageDialog(this, "Registro Funcional inválido: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ocorreu um erro inesperado: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void atualizarProfessor() {
        try {
            String registroFuncional = campoRegistroFuncional.getText();
            String nome = campoNome.getText();
            String cpf = campoCPF.getText(); // Novo campo
            String telefonePessoa = campoTelefone.getText();
            String emailPessoa = campoEmail.getText();
            String especialidade = campoEspecialidade.getText(); // Corrigido

            double salario = 0.0;
            try {
                salario = Double.parseDouble(campoSalario.getText());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Salário inválido. Use um número.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Professor professorAtualizado = new Professor(nome, cpf, telefonePessoa,
                                                          emailPessoa, registroFuncional, especialidade, salario);

            fachada.atualizarProfessor(professorAtualizado);
            JOptionPane.showMessageDialog(this, "Professor atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparCampos();
            listarTodosProfessores();
        } catch (DadoInvalidoException | EntidadeNaoEncontradaException e) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar professor: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ocorreu um erro inesperado: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void deletarProfessor() {
        try {
            String registroFuncional = campoRegistroFuncional.getText();
            if (registroFuncional.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Informe o Registro Funcional para deletar.", "Campo Vazio", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Tem certeza que deseja deletar o professor com Registro Funcional " + registroFuncional + "?", "Confirmar Deleção",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                if (fachada.deletarProfessor(registroFuncional)) {
                    JOptionPane.showMessageDialog(this, "Professor deletado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    limparCampos();
                    listarTodosProfessores();
                } else {
                    JOptionPane.showMessageDialog(this, "Falha ao deletar professor. Verifique o Registro Funcional.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (EntidadeNaoEncontradaException | DadoInvalidoException e) {
            JOptionPane.showMessageDialog(this, "Erro ao deletar professor: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ocorreu um erro inesperado: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void limparCampos() {
        campoRegistroFuncional.setText("");
        campoNome.setText("");
        campoCPF.setText(""); // Limpa o novo campo
        campoTelefone.setText("");
        campoEmail.setText("");
        campoEspecialidade.setText("");
        campoSalario.setText("");
        areaResultados.setText("");
    }

    private void listarTodosProfessores() {
        try {
            List<Professor> professores = fachada.listarTodosProfessores();
            areaResultados.setText("");
            if (professores.isEmpty()) {
                areaResultados.append("Nenhum professor cadastrado.\n");
            } else {
                areaResultados.append("--- Lista de Professores ---\n");
                for (Professor p : professores) {
                    areaResultados.append("Registro Funcional: " + p.getRegistroFuncional() +
                                          ", Nome: " + p.getNome() +
                                          ", CPF: " + p.getCpf() +
                                          ", Telefone: " + p.getTelefone() +
                                          ", Email: " + p.getEmail() +
                                          ", Especialidade: " + p.getEspecialidade() +
                                          ", Salário: " + String.format("%.2f", p.getSalario()) + "\n");
                }
                areaResultados.append("-----------------------------\n");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao listar professores: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void exibirProfessor(Professor professor) {
        if (professor != null) {
            campoRegistroFuncional.setText(professor.getRegistroFuncional());
            campoNome.setText(professor.getNome());
            campoCPF.setText(professor.getCpf()); // Exibe o CPF
            campoTelefone.setText(professor.getTelefone());
            campoEmail.setText(professor.getEmail());
            campoEspecialidade.setText(professor.getEspecialidade());
            campoSalario.setText(String.format("%.2f", professor.getSalario()));

            areaResultados.setText("Professor encontrado:\n" +
                                   "Registro Funcional: " + professor.getRegistroFuncional() + "\n" +
                                   "Nome: " + professor.getNome() + "\n" +
                                   "CPF: " + professor.getCpf() + "\n" +
                                   "Telefone: " + professor.getTelefone() + "\n" +
                                   "Email: " + professor.getEmail() + "\n" +
                                   "Especialidade: " + professor.getEspecialidade() + "\n" +
                                   "Salário: " + String.format("%.2f", professor.getSalario()) + "\n"
            );
        } else {
            limparCampos();
            areaResultados.setText("Professor não encontrado.");
        }
    }
}