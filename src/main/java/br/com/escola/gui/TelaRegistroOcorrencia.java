package br.com.escola.gui;

import br.com.escola.negocio.Ocorrencia;
import br.com.escola.negocio.Aluno;
import br.com.escola.negocio.Funcionario;
import br.com.escola.negocio.Professor;
import br.com.escola.negocio.IFachada;
import br.com.escola.negocio.Fachada;
import br.com.escola.excecoes.DadoInvalidoException;
import br.com.escola.excecoes.EntidadeNaoEncontradaException;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TelaRegistroOcorrencia extends JDialog {
    private IFachada fachada;
    private JTextField campoId;
    private JSpinner campoDataHora;
    private JComboBox<String> comboRegistrador;
    private JComboBox<Aluno> comboAlunos;
    private JTextArea campoDescricao;
    private JTextArea campoMedidas;
    private JTextArea areaResultados;
    private static final DateTimeFormatter LOCAL_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public TelaRegistroOcorrencia(JFrame parent, boolean modal) {
        super(parent, "Registro de Ocorrências", modal);
        this.fachada = Fachada.getInstance();
        setSize(700, 600);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        JPanel painelSuperior = new JPanel(new BorderLayout(10, 10));
        
        JPanel painelEntrada = new JPanel(new GridLayout(6, 2, 10, 10));
        painelEntrada.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        painelEntrada.add(new JLabel("ID da Ocorrência:"));
        campoId = new JTextField();
        painelEntrada.add(campoId);
        painelEntrada.add(new JLabel("Data/Hora:"));
        SpinnerDateModel model = new SpinnerDateModel();
        campoDataHora = new JSpinner(model);
        JSpinner.DateEditor editor = new JSpinner.DateEditor(campoDataHora, "dd/MM/yyyy HH:mm:ss");
        campoDataHora.setEditor(editor);
        campoDataHora.setValue(java.sql.Timestamp.valueOf(LocalDateTime.now()));
        painelEntrada.add(campoDataHora);
        painelEntrada.add(new JLabel("Registrador:"));
        comboRegistrador = new JComboBox<>();
        painelEntrada.add(comboRegistrador);
        painelEntrada.add(new JLabel("Aluno Envolvido:"));
        comboAlunos = new JComboBox<>();
        painelEntrada.add(comboAlunos);
        painelEntrada.add(new JLabel("Descrição:"));
        campoDescricao = new JTextArea(3, 20);
        campoDescricao.setLineWrap(true);
        campoDescricao.setWrapStyleWord(true);
        JScrollPane scrollDescricao = new JScrollPane(campoDescricao);
        painelEntrada.add(scrollDescricao);
        painelEntrada.add(new JLabel("Medidas Tomadas:"));
        campoMedidas = new JTextArea(3, 20);
        campoMedidas.setLineWrap(true);
        campoMedidas.setWrapStyleWord(true);
        JScrollPane scrollMedidas = new JScrollPane(campoMedidas);
        painelEntrada.add(scrollMedidas);
        
        painelSuperior.add(painelEntrada, BorderLayout.NORTH);

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
        
        painelSuperior.add(painelBotoes, BorderLayout.CENTER);
        
        add(painelSuperior, BorderLayout.NORTH);

        areaResultados = new JTextArea();
        areaResultados.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(areaResultados);
        add(scrollPane, BorderLayout.CENTER);

        btnAdicionar.addActionListener(e -> adicionarOcorrencia());
        btnBuscar.addActionListener(e -> buscarOcorrencia());
        btnAtualizar.addActionListener(e -> atualizarOcorrencia());
        btnDeletar.addActionListener(e -> deletarOcorrencia());
        btnLimpar.addActionListener(e -> limparCampos());
        btnListarTodos.addActionListener(e -> listarTodasOcorrencias());

        popularCombos();
        limparCampos();
        listarTodasOcorrencias();
    }

    private void popularCombos() {
        try {
            List<Aluno> alunos = fachada.listarTodosAlunos();
            if (alunos != null) {
                Collections.sort(alunos, Comparator.comparing(Aluno::getNome));
                comboAlunos.removeAllItems();
                for (Aluno a : alunos) {
                    comboAlunos.addItem(a);
                }
                comboAlunos.setRenderer(new DefaultListCellRenderer() {
                    @Override
                    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                        if (value instanceof Aluno) {
                            Aluno aluno = (Aluno) value;
                            setText(aluno.getNome() + " - Responsável: " + aluno.getResponsavel());
                        }
                        return this;
                    }
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar alunos: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
        try {
            List<String> registradores = new ArrayList<>();
            List<Professor> professores = fachada.listarTodosProfessores();
            if (professores != null) {
                for (Professor p : professores) {
                    registradores.add(p.getNome() + " - " + p.getRegistroFuncional());
                }
            }
            List<Funcionario> funcionarios = fachada.listarTodosFuncionarios();
            if (funcionarios != null) {
                for (Funcionario f : funcionarios) {
                    registradores.add(f.getNome() + " - " + f.getCpf());
                }
            }
            Collections.sort(registradores);
            comboRegistrador.removeAllItems();
            for (String r : registradores) {
                comboRegistrador.addItem(r);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar registradores: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void adicionarOcorrencia() {
        try {
            String id = campoId.getText();
            LocalDateTime dataHora = LocalDateTime.ofInstant(((java.util.Date) campoDataHora.getValue()).toInstant(), java.time.ZoneId.systemDefault());
            String descricao = campoDescricao.getText();
            String medidasTomadas = campoMedidas.getText();
            String registradorInfo = (String) comboRegistrador.getSelectedItem();
            String registradorId = registradorInfo != null ? registradorInfo.substring(registradorInfo.lastIndexOf(" - ") + 3) : null;
            Aluno alunoSelecionado = (Aluno) comboAlunos.getSelectedItem();
            if (alunoSelecionado == null) {
                JOptionPane.showMessageDialog(this, "Selecione um aluno para a ocorrência.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            fachada.adicionarOcorrencia(id, dataHora, registradorId, alunoSelecionado, descricao, medidasTomadas);
            JOptionPane.showMessageDialog(this, "Ocorrência adicionada com sucesso!");
            limparCampos();
            listarTodasOcorrencias();
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Formato de data/hora inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (DadoInvalidoException | EntidadeNaoEncontradaException ex) {
            JOptionPane.showMessageDialog(this, "Erro de Validação: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao adicionar ocorrência: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void buscarOcorrencia() {
        try {
            String id = campoId.getText();
            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, digite o ID da ocorrência para buscar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Ocorrencia ocorrencia = fachada.buscarOcorrencia(id);
            exibirOcorrencia(ocorrencia);
            JOptionPane.showMessageDialog(this, "Ocorrência encontrada!");
        } catch (EntidadeNaoEncontradaException ex) {
            JOptionPane.showMessageDialog(this, "Ocorrência não encontrada: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            limparCampos();
        } catch (DadoInvalidoException ex) {
            JOptionPane.showMessageDialog(this, "Erro de Validação: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao buscar ocorrência: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void atualizarOcorrencia() {
        try {
            String id = campoId.getText();
            LocalDateTime dataHora = LocalDateTime.ofInstant(((java.util.Date) campoDataHora.getValue()).toInstant(), java.time.ZoneId.systemDefault());
            String descricao = campoDescricao.getText();
            String medidasTomadas = campoMedidas.getText();
            String registradorInfo = (String) comboRegistrador.getSelectedItem();
            String registradorId = registradorInfo != null ? registradorInfo.substring(registradorInfo.lastIndexOf(" - ") + 3) : null;
            Aluno alunoSelecionado = (Aluno) comboAlunos.getSelectedItem();
            if (alunoSelecionado == null) {
                JOptionPane.showMessageDialog(this, "Selecione um aluno para a ocorrência.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Ocorrencia ocorrenciaAtualizada = new Ocorrencia(id, dataHora, registradorId, alunoSelecionado, descricao, medidasTomadas);
            fachada.atualizarOcorrencia(ocorrenciaAtualizada);
            JOptionPane.showMessageDialog(this, "Ocorrência atualizada com sucesso!");
            limparCampos();
            listarTodasOcorrencias();
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Formato de data/hora inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (EntidadeNaoEncontradaException ex) {
            JOptionPane.showMessageDialog(this, "Erro: Ocorrência não encontrada para atualização. " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (DadoInvalidoException ex) {
            JOptionPane.showMessageDialog(this, "Erro de Validação: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar ocorrência: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void deletarOcorrencia() {
        try {
            String id = campoId.getText();
            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, digite o ID da ocorrência para deletar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja deletar a ocorrência " + id + "?", "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                fachada.deletarOcorrencia(id);
                JOptionPane.showMessageDialog(this, "Ocorrência deletada com sucesso!");
                limparCampos();
                listarTodasOcorrencias();
            }
        } catch (EntidadeNaoEncontradaException ex) {
            JOptionPane.showMessageDialog(this, "Erro: Ocorrência não encontrada para exclusão. " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (DadoInvalidoException ex) {
            JOptionPane.showMessageDialog(this, "Erro de Validação: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao deletar ocorrência: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void limparCampos() {
        campoId.setText("");
        campoDataHora.setValue(java.sql.Timestamp.valueOf(LocalDateTime.now()));
        comboRegistrador.setSelectedIndex(-1);
        campoDescricao.setText("");
        campoMedidas.setText("");
        comboAlunos.setSelectedIndex(-1);
        areaResultados.setText("");
    }

    private void listarTodasOcorrencias() {
        try {
            List<Ocorrencia> ocorrencias = fachada.listarTodasOcorrencias();
            if (ocorrencias == null || ocorrencias.isEmpty()) {
                areaResultados.setText("Nenhuma ocorrência cadastrada.");
            } else {
                StringBuilder sb = new StringBuilder("--- Lista de Ocorrências ---\n");
                for (Ocorrencia o : ocorrencias) {
                    String nomeAluno = o.getAluno() != null ? o.getAluno().getNome() : "N/A";
                    String responsavelAluno = (o.getAluno() != null && o.getAluno().getResponsavel() != null) ? o.getAluno().getResponsavel() : "N/A";
                    sb.append("ID: ").append(o.getId()).append(", Aluno: ").append(nomeAluno).append(", Responsável: ").append(responsavelAluno).append("\nRegistrador: ").append(o.getRegistradorId()).append(", Data/Hora: ").append(o.getDataHora().format(LOCAL_DATE_TIME_FORMATTER)).append("\nDescrição: ").append(o.getDescricao()).append("\nMedidas Tomadas: ").append(o.getMedidasTomadas().trim().isEmpty() ? "N/A" : o.getMedidasTomadas()).append("\n-----------------------------------\n");
                }
                areaResultados.setText(sb.toString());
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao listar ocorrências: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void exibirOcorrencia(Ocorrencia ocorrencia) {
        if (ocorrencia != null) {
            campoId.setText(ocorrencia.getId());
            campoDataHora.setValue(java.sql.Timestamp.valueOf(ocorrencia.getDataHora()));
            campoDescricao.setText(ocorrencia.getDescricao());
            campoMedidas.setText(ocorrencia.getMedidasTomadas());
            for (int i = 0; i < comboRegistrador.getItemCount(); i++) {
                String item = comboRegistrador.getItemAt(i);
                if (item.contains(ocorrencia.getRegistradorId())) {
                    comboRegistrador.setSelectedIndex(i);
                    break;
                }
            }
            for (int i = 0; i < comboAlunos.getItemCount(); i++) {
                if (comboAlunos.getItemAt(i).getMatricula().equals(ocorrencia.getAluno().getMatricula())) {
                    comboAlunos.setSelectedIndex(i);
                    break;
                }
            }
            String responsavelAluno = (ocorrencia.getAluno() != null && ocorrencia.getAluno().getResponsavel() != null) ? ocorrencia.getAluno().getResponsavel() : "N/A";
            areaResultados.setText("--- Detalhes da Ocorrência ---\n" + "ID: " + ocorrencia.getId() + "\n" + "Data/Hora: " + ocorrencia.getDataHora().format(LOCAL_DATE_TIME_FORMATTER) + "\n" + "Registrador: " + ocorrencia.getRegistradorId() + "\n" + "Aluno: " + (ocorrencia.getAluno() != null ? ocorrencia.getAluno().getNome() : "N/A") + "\n" + "Responsável: " + responsavelAluno + "\n" + "Descrição: " + ocorrencia.getDescricao() + "\n" + "Medidas Tomadas: " + (ocorrencia.getMedidasTomadas().trim().isEmpty() ? "N/A" : ocorrencia.getMedidasTomadas()) + "\n" + "Encerrada: " + (ocorrencia.isEncerrada() ? "Sim" : "Não") + "\n");
        } else {
            limparCampos();
            areaResultados.setText("Ocorrência não encontrada.");
        }
    }
}