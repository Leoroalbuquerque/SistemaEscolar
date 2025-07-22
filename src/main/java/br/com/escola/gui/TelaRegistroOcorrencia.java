package br.com.escola.gui;

import br.com.escola.negocio.Ocorrencia;
import br.com.escola.negocio.Aluno;
import br.com.escola.negocio.Fachada;
import br.com.escola.negocio.IFachada;
import br.com.escola.excecoes.DadoInvalidoException;
import br.com.escola.excecoes.EntidadeNaoEncontradaException;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

public class TelaRegistroOcorrencia extends JDialog {

    private IFachada fachada;

    private JTextField campoId;
    private JTextField campoDataHora;
    private JTextField campoRegistradorId;
    private JComboBox<Aluno> comboAlunos;
    private JTextArea campoDescricao;
    private JTextArea campoMedidas;
    private JTextArea areaResultados;

    public TelaRegistroOcorrencia(JFrame parent, boolean modal) {
        super(parent, "Registro de Ocorrências", modal);
        this.fachada = Fachada.getInstance();

        setSize(700, 600);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        JPanel painelEntrada = new JPanel(new GridLayout(6, 2, 10, 10));
        painelEntrada.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        painelEntrada.add(new JLabel("ID da Ocorrência:"));
        campoId = new JTextField();
        painelEntrada.add(campoId);

        painelEntrada.add(new JLabel("Data/Hora (AAAA-MM-DDTHH:MM:SS):"));
        campoDataHora = new JTextField();
        painelEntrada.add(campoDataHora);

        painelEntrada.add(new JLabel("Registrador (CPF/RF):"));
        campoRegistradorId = new JTextField();
        painelEntrada.add(campoRegistradorId);

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

        add(painelEntrada, BorderLayout.NORTH);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnAdicionar = new JButton("Adicionar");
        JButton btnBuscar = new JButton("Buscar");
        JButton btnAtualizar = new JButton("Atualizar");
        JButton btnDeletar = new JButton("Deletar");
        JButton btnLimpar = new JButton("Limpar Campos");
        JButton btnListarTodos = new JButton("Listar Todos");
        JButton btnRegistrarMedidas = new JButton("Registrar Medidas");
        JButton btnEncerrarOcorrencia = new JButton("Encerrar Ocorrência");

        painelBotoes.add(btnAdicionar);
        painelBotoes.add(btnBuscar);
        painelBotoes.add(btnAtualizar);
        painelBotoes.add(btnDeletar);
        painelBotoes.add(btnLimpar);
        painelBotoes.add(btnListarTodos);
        painelBotoes.add(btnRegistrarMedidas);
        painelBotoes.add(btnEncerrarOcorrencia);

        add(painelBotoes, BorderLayout.CENTER);

        areaResultados = new JTextArea();
        areaResultados.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(areaResultados);
        add(scrollPane, BorderLayout.SOUTH);

        btnAdicionar.addActionListener(e -> adicionarOcorrencia());
        btnBuscar.addActionListener(e -> buscarOcorrencia());
        btnAtualizar.addActionListener(e -> atualizarOcorrencia());
        btnDeletar.addActionListener(e -> deletarOcorrencia());
        btnLimpar.addActionListener(e -> limparCampos());
        btnListarTodos.addActionListener(e -> listarTodasOcorrencias());
        btnRegistrarMedidas.addActionListener(e -> registrarMedidasOcorrencia());
        btnEncerrarOcorrencia.addActionListener(e -> encerrarOcorrencia());

        popularAlunos();
        limparCampos();
        listarTodasOcorrencias();
    }

    private void popularAlunos() {
        try {
            List<Aluno> alunos = fachada.listarTodosAlunos();
            comboAlunos.removeAllItems();
            for (Aluno a : alunos) {
                comboAlunos.addItem(a);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar alunos: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void adicionarOcorrencia() {
        try {
            String id = campoId.getText();
            LocalDateTime dataHora = LocalDateTime.parse(campoDataHora.getText());
            String descricao = campoDescricao.getText();
            String registradorId = campoRegistradorId.getText();
            Aluno alunoSelecionado = (Aluno) comboAlunos.getSelectedItem();

            if (alunoSelecionado == null) {
                JOptionPane.showMessageDialog(this, "Selecione um aluno para a ocorrência.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            fachada.adicionarOcorrencia(id, dataHora, descricao, registradorId, alunoSelecionado);
            JOptionPane.showMessageDialog(this, "Ocorrência adicionada com sucesso!");
            limparCampos();
            listarTodasOcorrencias();
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Formato de data/hora inválido. Use AAAA-MM-DDTHH:MM:SS.", "Erro", JOptionPane.ERROR_MESSAGE);
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
            LocalDateTime dataHora = LocalDateTime.parse(campoDataHora.getText());
            String descricao = campoDescricao.getText();
            String registradorId = campoRegistradorId.getText();
            Aluno alunoSelecionado = (Aluno) comboAlunos.getSelectedItem();

            if (alunoSelecionado == null) {
                JOptionPane.showMessageDialog(this, "Selecione um aluno para a ocorrência.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Ocorrencia ocorrenciaAtualizada = new Ocorrencia(id, dataHora, descricao, registradorId, alunoSelecionado);
            fachada.atualizarOcorrencia(ocorrenciaAtualizada);
            JOptionPane.showMessageDialog(this, "Ocorrência atualizada com sucesso!");
            limparCampos();
            listarTodasOcorrencias();
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Formato de data/hora inválido. Use AAAA-MM-DDTHH:MM:SS.", "Erro", JOptionPane.ERROR_MESSAGE);
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
                boolean deletado = fachada.deletarOcorrencia(id);
                if (deletado) {
                    JOptionPane.showMessageDialog(this, "Ocorrência deletada com sucesso!");
                    limparCampos();
                    listarTodasOcorrencias();
                } else {
                    JOptionPane.showMessageDialog(this, "Não foi possível deletar a ocorrência.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
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

    private void registrarMedidasOcorrencia() {
        try {
            String id = campoId.getText();
            String medidas = campoMedidas.getText();
            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, digite o ID da ocorrência.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (medidas.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, digite as medidas a serem registradas.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            fachada.registrarMedidasOcorrencia(id, medidas);
            JOptionPane.showMessageDialog(this, "Medidas registradas com sucesso para a ocorrência " + id + "!");
            buscarOcorrencia();
            listarTodasOcorrencias();
        } catch (EntidadeNaoEncontradaException ex) {
            JOptionPane.showMessageDialog(this, "Erro: Ocorrência não encontrada para registrar medidas. " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (DadoInvalidoException ex) {
            JOptionPane.showMessageDialog(this, "Erro de Validação: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao registrar medidas: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void encerrarOcorrencia() {
        try {
            String id = campoId.getText();
            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, digite o ID da ocorrência para encerrar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja encerrar a ocorrência " + id + "?", "Confirmar Encerramento", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                fachada.encerrarOcorrencia(id);
                JOptionPane.showMessageDialog(this, "Ocorrência " + id + " encerrada com sucesso!");
                buscarOcorrencia();
                listarTodasOcorrencias();
            }
        } catch (EntidadeNaoEncontradaException ex) {
            JOptionPane.showMessageDialog(this, "Erro: Ocorrência não encontrada para encerrar. " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (DadoInvalidoException ex) {
            JOptionPane.showMessageDialog(this, "Erro de Validação: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao encerrar ocorrência: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void limparCampos() {
        campoId.setText("");
        campoDataHora.setText("");
        campoRegistradorId.setText("");
        campoDescricao.setText("");
        campoMedidas.setText("");
        popularAlunos();
        areaResultados.setText("");
    }

    private void listarTodasOcorrencias() {
        try {
            List<Ocorrencia> ocorrencias = fachada.listarTodasOcorrencias();
            if (ocorrencias.isEmpty()) {
                areaResultados.setText("Nenhuma ocorrência cadastrada.");
            } else {
                StringBuilder sb = new StringBuilder("--- Lista de Ocorrências ---\n");
                for (Ocorrencia o : ocorrencias) {
                    sb.append("ID: ").append(o.getId())
                      .append(", Data/Hora: ").append(o.getDataHora())
                      .append(", Aluno: ").append(o.getAluno() != null ? o.getAluno().getNome() : "N/A")
                      .append(", Registrador: ").append(o.getRegistradorId())
                      .append(", Status: ").append(o.isEncerrada() ? "Encerrada" : "Aberta")
                      .append("\n  Descrição: ").append(o.getDescricao())
                      .append("\n  Medidas: ").append(o.getMedidasTomadas() != null && !o.getMedidasTomadas().isEmpty() ? o.getMedidasTomadas() : "N/A")
                      .append("\n-----------------------------------\n");
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
            campoDataHora.setText(ocorrencia.getDataHora().toString());
            campoRegistradorId.setText(ocorrencia.getRegistradorId());
            campoDescricao.setText(ocorrencia.getDescricao());
            campoMedidas.setText(ocorrencia.getMedidasTomadas());
            popularAlunos();
            if (ocorrencia.getAluno() != null) {
                for (int i = 0; i < comboAlunos.getItemCount(); i++) {
                    if (comboAlunos.getItemAt(i).getMatricula().equals(ocorrencia.getAluno().getMatricula())) {
                        comboAlunos.setSelectedIndex(i);
                        break;
                    }
                }
            }
            areaResultados.setText(
                "--- Detalhes da Ocorrência ---\n" +
                "ID: " + ocorrencia.getId() + "\n" +
                "Data/Hora: " + ocorrencia.getDataHora() + "\n" +
                "Registrador: " + ocorrencia.getRegistradorId() + "\n" +
                "Aluno: " + (ocorrencia.getAluno() != null ? ocorrencia.getAluno().getNome() : "N/A") + "\n" +
                "Descrição: " + ocorrencia.getDescricao() + "\n" +
                "Medidas Tomadas: " + (ocorrencia.getMedidasTomadas() != null && !ocorrencia.getMedidasTomadas().isEmpty() ? ocorrencia.getMedidasTomadas() : "N/A") + "\n" +
                "Encerrada: " + (ocorrencia.isEncerrada() ? "Sim" : "Não") + "\n"
            );
        } else {
            limparCampos();
            areaResultados.setText("Ocorrência não encontrada.");
        }
    }
}