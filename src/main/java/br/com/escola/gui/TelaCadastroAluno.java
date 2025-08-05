package br.com.escola.gui;

import br.com.escola.negocio.Aluno;
import br.com.escola.negocio.Responsavel;
import br.com.escola.negocio.Fachada;
import br.com.escola.negocio.IFachada;
import br.com.escola.excecoes.DadoInvalidoException;
import br.com.escola.excecoes.EntidadeNaoEncontradaException;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.time.ZoneId;

public class TelaCadastroAluno extends JFrame {

    private JTextField campoMatricula;
    private JTextField campoNomeAluno;
    private JFormattedTextField campoDataNascimentoAluno;
    private JTextField campoAnoLetivo;
    private JTextField campoCPFResponsavel;
    private JTextField campoNomeResponsavelBusca;

    private JButton btnAdicionar;
    private JButton btnBuscar;
    private JButton btnAtualizar;
    private JButton btnDeletar;
    private JButton btnLimpar;
    private JButton btnListarTodos;

    private JTextArea areaResultados;

    private IFachada fachada;

    public TelaCadastroAluno() {
        this.fachada = Fachada.getInstance();

        setTitle("Cadastro de Alunos");
        setSize(800, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panelPrincipal = new JPanel(new BorderLayout());
        JPanel panelForm = new JPanel(new GridLayout(6, 2, 10, 10));
        panelForm.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        panelForm.add(new JLabel("Matrícula:"));
        campoMatricula = new JTextField(20);
        panelForm.add(campoMatricula);

        panelForm.add(new JLabel("Nome Aluno:"));
        campoNomeAluno = new JTextField(20);
        panelForm.add(campoNomeAluno);

        panelForm.add(new JLabel("Data Nasc. (DD/MM/AAAA):"));
        try {
            MaskFormatter dateFormatter = new MaskFormatter("##/##/####");
            dateFormatter.setPlaceholderCharacter('_');
            campoDataNascimentoAluno = new JFormattedTextField(dateFormatter);
        } catch (ParseException e) {
            campoDataNascimentoAluno = new JFormattedTextField();
            e.printStackTrace();
        }
        panelForm.add(campoDataNascimentoAluno);

        panelForm.add(new JLabel("Ano Letivo:"));
        campoAnoLetivo = new JTextField(20);
        panelForm.add(campoAnoLetivo);

        panelForm.add(new JLabel("CPF Responsável:"));
        campoCPFResponsavel = new JTextField(20);
        panelForm.add(campoCPFResponsavel);

        panelForm.add(new JLabel("Nome Responsável:"));
        campoNomeResponsavelBusca = new JTextField(20);
        campoNomeResponsavelBusca.setEditable(false);
        panelForm.add(campoNomeResponsavelBusca);

        JPanel panelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnAdicionar = new JButton("Adicionar");
        btnBuscar = new JButton("Buscar");
        btnAtualizar = new JButton("Atualizar");
        btnDeletar = new JButton("Deletar");
        btnLimpar = new JButton("Limpar Campos");
        JButton btnListarTodos = new JButton("Listar Todos");

        panelBotoes.add(btnAdicionar);
        panelBotoes.add(btnBuscar);
        panelBotoes.add(btnAtualizar);
        panelBotoes.add(btnDeletar);
        panelBotoes.add(btnLimpar);
        panelBotoes.add(btnListarTodos);

        areaResultados = new JTextArea(10, 60);
        areaResultados.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(areaResultados);

        panelPrincipal.add(panelForm, BorderLayout.NORTH);
        panelPrincipal.add(panelBotoes, BorderLayout.CENTER);
        panelPrincipal.add(scrollPane, BorderLayout.SOUTH);

        add(panelPrincipal);

        btnAdicionar.addActionListener(e -> adicionarAluno());
        btnBuscar.addActionListener(e -> buscarAluno());
        btnAtualizar.addActionListener(e -> atualizarAluno());
        btnDeletar.addActionListener(e -> deletarAluno());
        btnLimpar.addActionListener(e -> limparCampos());
        btnListarTodos.addActionListener(e -> listarTodosAlunos());
    }

    private void adicionarAluno() {
        try {
            String matricula = campoMatricula.getText();
            String nomeAluno = campoNomeAluno.getText();
            
            LocalDate dataNascimentoAluno = null;
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                dataNascimentoAluno = LocalDate.parse(campoDataNascimentoAluno.getText(), formatter);
            } catch (DateTimeParseException e) {
                JOptionPane.showMessageDialog(this, "Data de nascimento inválida. Use o formato DD/MM/AAAA.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            int anoLetivo = 0;
            try {
                anoLetivo = Integer.parseInt(campoAnoLetivo.getText());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Ano Letivo deve ser um número válido.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String cpfResponsavelInput = campoCPFResponsavel.getText();
            List<String> cpfsResponsaveis = new ArrayList<>();

            if (!cpfResponsavelInput.isEmpty()) {
                try {
                    fachada.buscarResponsavel(cpfResponsavelInput);
                    cpfsResponsaveis.add(cpfResponsavelInput);
                } catch (EntidadeNaoEncontradaException ex) {
                    int option = JOptionPane.showConfirmDialog(this,
                                    "O CPF do responsável (" + cpfResponsavelInput + ") não está cadastrado. Deseja cadastrá-lo agora?",
                                    "Responsável Não Encontrado", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

                    if (option == JOptionPane.YES_OPTION) {
                        TelaCadastroResponsavel telaResponsavel = new TelaCadastroResponsavel(this, true);
                        telaResponsavel.campoCPF.setText(cpfResponsavelInput);
                        telaResponsavel.setVisible(true);

                        if (telaResponsavel.isResponsavelManipulado()) {
                            try {
                                fachada.buscarResponsavel(cpfResponsavelInput);
                                cpfsResponsaveis.add(cpfResponsavelInput);
                            } catch (EntidadeNaoEncontradaException | DadoInvalidoException | IOException e2) {
                                JOptionPane.showMessageDialog(this, "Erro ao confirmar cadastro do responsável. Por favor, tente novamente.", "Erro", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                        } else {
                            JOptionPane.showMessageDialog(this, "Cadastro do responsável cancelado ou falhou. Não foi possível adicionar o aluno.", "Ação Cancelada", JOptionPane.WARNING_MESSAGE);
                            return;
                        }
                    } else {
                        return;
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Erro de I/O ao buscar responsável: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } else {
                    JOptionPane.showMessageDialog(this, "Por favor, informe o CPF de um responsável para o aluno.", "Campo Vazio", JOptionPane.WARNING_MESSAGE);
                    return;
            }
            
            Aluno alunoParaAdicionar = new Aluno(nomeAluno, matricula, "sem_telefone_aluno", "sem_email_aluno",
                                                 matricula, dataNascimentoAluno, anoLetivo, cpfsResponsaveis);

            fachada.adicionarAluno(alunoParaAdicionar);
            JOptionPane.showMessageDialog(this, "Aluno adicionado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparCampos();
            listarTodosAlunos();
        } catch (DadoInvalidoException | EntidadeNaoEncontradaException | IOException e) {
            JOptionPane.showMessageDialog(this, "Erro ao adicionar aluno: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ocorreu um erro inesperado: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void buscarAluno() {
        try {
            String matricula = campoMatricula.getText();
            if (matricula.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Informe a matrícula para buscar.", "Campo Vazio", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Aluno aluno = fachada.buscarAluno(matricula);
            exibirAluno(aluno);
        } catch (EntidadeNaoEncontradaException e) {
            JOptionPane.showMessageDialog(this, "Aluno não encontrado: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            limparCampos();
        } catch (DadoInvalidoException e) {
            JOptionPane.showMessageDialog(this, "Matrícula inválida: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erro de I/O ao buscar aluno: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ocorreu um erro inesperado: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void atualizarAluno() {
        try {
            String matricula = campoMatricula.getText();
            String nomeAluno = campoNomeAluno.getText();
            
            LocalDate dataNascimentoAluno = null;
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                dataNascimentoAluno = LocalDate.parse(campoDataNascimentoAluno.getText(), formatter);
            } catch (DateTimeParseException e) {
                JOptionPane.showMessageDialog(this, "Data de nascimento inválida. Use o formato DD/MM/AAAA.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int anoLetivo = 0;
            try {
                anoLetivo = Integer.parseInt(campoAnoLetivo.getText());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Ano Letivo deve ser um número válido.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String cpfResponsavelInput = campoCPFResponsavel.getText();
            List<String> cpfsResponsaveis = new ArrayList<>();

            if (!cpfResponsavelInput.isEmpty()) {
                try {
                    fachada.buscarResponsavel(cpfResponsavelInput);
                    cpfsResponsaveis.add(cpfResponsavelInput);
                } catch (EntidadeNaoEncontradaException ex) {
                    int option = JOptionPane.showConfirmDialog(this,
                                    "O CPF do responsável (" + cpfResponsavelInput + ") não está cadastrado. Deseja cadastrá-lo agora?",
                                    "Responsável Não Encontrado", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

                    if (option == JOptionPane.YES_OPTION) {
                        TelaCadastroResponsavel telaResponsavel = new TelaCadastroResponsavel(this, true);
                        telaResponsavel.campoCPF.setText(cpfResponsavelInput);
                        telaResponsavel.setVisible(true);

                        if (telaResponsavel.isResponsavelManipulado()) {
                            try {
                                fachada.buscarResponsavel(cpfResponsavelInput);
                                cpfsResponsaveis.add(cpfResponsavelInput);
                            } catch (EntidadeNaoEncontradaException | DadoInvalidoException | IOException e2) {
                                JOptionPane.showMessageDialog(this, "Erro ao confirmar cadastro do responsável. Por favor, tente novamente.", "Erro", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                        } else {
                            JOptionPane.showMessageDialog(this, "Cadastro do responsável cancelado ou falhou. Não foi possível atualizar o aluno.", "Ação Cancelada", JOptionPane.WARNING_MESSAGE);
                            return;
                        }
                    } else {
                        return;
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Erro de I/O ao buscar responsável: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } else {
                    JOptionPane.showMessageDialog(this, "Por favor, informe o CPF de um responsável para o aluno.", "Campo Vazio", JOptionPane.WARNING_MESSAGE);
                    return;
            }

            Aluno alunoAtualizado = new Aluno(nomeAluno, matricula, "sem_telefone_aluno", "sem_email_aluno",
                                                 matricula, dataNascimentoAluno, anoLetivo, cpfsResponsaveis);
            
            fachada.atualizarAluno(alunoAtualizado);
            JOptionPane.showMessageDialog(this, "Aluno atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparCampos();
            listarTodosAlunos();
        } catch (DadoInvalidoException | EntidadeNaoEncontradaException | IOException e) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar aluno: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ocorreu um erro inesperado: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void deletarAluno() {
        try {
            String matricula = campoMatricula.getText();
            if (matricula.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Informe a matrícula para deletar.", "Campo Vazio", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this,
                            "Tem certeza que deseja deletar o aluno " + matricula + "?", "Confirmar Deleção",
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                fachada.deletarAluno(matricula);
                JOptionPane.showMessageDialog(this, "Aluno deletado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                limparCampos();
                listarTodosAlunos();
            }
        } catch (EntidadeNaoEncontradaException | DadoInvalidoException | IOException e) {
            JOptionPane.showMessageDialog(this, "Erro ao deletar aluno: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ocorreu um erro inesperado: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void limparCampos() {
        campoMatricula.setText("");
        campoNomeAluno.setText("");
        campoDataNascimentoAluno.setText("");
        campoAnoLetivo.setText("");
        campoCPFResponsavel.setText("");
        campoNomeResponsavelBusca.setText("");
        areaResultados.setText("");
    }

    private void listarTodosAlunos() {
        try {
            List<Aluno> alunos = fachada.listarTodosAlunos();
            areaResultados.setText("");
            if (alunos.isEmpty()) {
                areaResultados.append("Nenhum aluno cadastrado.\n");
            } else {
                areaResultados.append("--- Lista de Alunos ---\n");
                for (Aluno aluno : alunos) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Matrícula: ").append(aluno.getMatricula())
                      .append(", Nome: ").append(aluno.getNome())
                      .append(", Data Nasc.: ").append(aluno.getDataNascimento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                      .append(", Ano Letivo: ").append(aluno.getAnoLetivo());
                    
                    List<String> cpfsResponsaveis = aluno.getCpfsResponsaveis();
                    if (cpfsResponsaveis != null && !cpfsResponsaveis.isEmpty()) {
                        sb.append(", Responsáveis: ");
                        for (String cpfResp : cpfsResponsaveis) {
                            try {
                                Responsavel resp = fachada.buscarResponsavel(cpfResp);
                                sb.append(resp.getNome()).append(" (CPF: ").append(resp.getCpfResponsavel()).append("); ");
                            } catch (EntidadeNaoEncontradaException | DadoInvalidoException | IOException e) {
                                sb.append("CPF ").append(cpfResp).append(" (Não Encontrado ou Erro de I/O); ");
                            }
                        }
                    } else {
                        sb.append(", Responsáveis: N/A");
                    }
                    sb.append("\n");
                    areaResultados.append(sb.toString());
                }
                areaResultados.append("-----------------------\n");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erro de I/O ao listar alunos: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ocorreu um erro inesperado: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void exibirAluno(Aluno aluno) {
        if (aluno != null) {
            campoMatricula.setText(aluno.getMatricula());
            campoNomeAluno.setText(aluno.getNome());
            campoDataNascimentoAluno.setText(aluno.getDataNascimento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            campoAnoLetivo.setText(String.valueOf(aluno.getAnoLetivo()));

            List<String> cpfsResponsaveis = aluno.getCpfsResponsaveis();
            if (cpfsResponsaveis != null && !cpfsResponsaveis.isEmpty()) {
                String primeiroCpfResp = cpfsResponsaveis.get(0);
                campoCPFResponsavel.setText(primeiroCpfResp);
                try {
                    Responsavel resp = fachada.buscarResponsavel(primeiroCpfResp);
                    campoNomeResponsavelBusca.setText(resp.getNome());
                } catch (EntidadeNaoEncontradaException | DadoInvalidoException | IOException e) {
                    campoCPFResponsavel.setText("CPF Resp. Inválido");
                    campoNomeResponsavelBusca.setText("Responsável Não Encontrado ou Erro de I/O");
                }
            } else {
                campoCPFResponsavel.setText("");
                campoNomeResponsavelBusca.setText("");
            }

            areaResultados.setText("Aluno encontrado:\n" +
                                   "Matrícula: " + aluno.getMatricula() + "\n" +
                                   "Nome: " + aluno.getNome() + "\n" +
                                   "Data Nasc.: " + aluno.getDataNascimento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "\n" +
                                   "Ano Letivo: " + aluno.getAnoLetivo() + "\n" +
                                   "Responsáveis (CPFs): " + (cpfsResponsaveis != null && !cpfsResponsaveis.isEmpty() ? String.join(", ", cpfsResponsaveis) : "N/A") + "\n"
            );
        } else {
            limparCampos();
            areaResultados.setText("Aluno não encontrado.");
        }
    }
}