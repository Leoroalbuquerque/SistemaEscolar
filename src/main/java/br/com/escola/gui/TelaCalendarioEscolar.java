package br.com.escola.gui;

import br.com.escola.negocio.CalendarioEscolar;
import br.com.escola.negocio.Avaliacao;
import br.com.escola.negocio.Fachada;
import br.com.escola.negocio.IFachada;
import br.com.escola.excecoes.DadoInvalidoException;
import br.com.escola.excecoes.EntidadeNaoEncontradaException;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Set; // Não está sendo usado diretamente, mas pode ser útil para Sets no futuro.
import java.util.TreeSet; // Não está sendo usado diretamente, mas pode ser útil para Sets no futuro.

public class TelaCalendarioEscolar extends JDialog {

    private IFachada fachada;

    private JTextField campoAnoLetivo;
    private JTextArea areaResultados;

    // Painel de Datas Letivas
    private JTextField campoDataLetiva;
    private JList<LocalDate> listaDatasLetivas;
    private DefaultListModel<LocalDate> modeloDatasLetivas;

    // Painel de Feriados
    private JTextField campoFeriado;
    private JList<LocalDate> listaFeriados;
    private DefaultListModel<LocalDate> modeloFeriados;

    // Painel de Avaliações
    private JComboBox<Avaliacao> comboAvaliacoesDisponiveis;
    private JList<Avaliacao> listaAvaliacoesNoCalendario;
    private DefaultListModel<Avaliacao> modeloAvaliacoesNoCalendario;


    public TelaCalendarioEscolar(JFrame parent, boolean modal) {
        super(parent, "Gerenciamento de Calendário Escolar", modal);
        this.fachada = Fachada.getInstance();

        setSize(800, 700);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        JPanel painelSuperior = new JPanel(new GridLayout(1, 2, 10, 10));
        painelSuperior.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        painelSuperior.add(new JLabel("Ano Letivo do Calendário:"));
        campoAnoLetivo = new JTextField();
        painelSuperior.add(campoAnoLetivo);
        add(painelSuperior, BorderLayout.NORTH);

        JPanel painelBotoesCRUD = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnAdicionarCalendario = new JButton("Adicionar Calendário");
        JButton btnBuscarCalendario = new JButton("Buscar Calendário");
        JButton btnAtualizarCalendario = new JButton("Atualizar Calendário");
        JButton btnDeletarCalendario = new JButton("Deletar Calendário");
        JButton btnLimpar = new JButton("Limpar Campos");
        JButton btnListarTodos = new JButton("Listar Todos");

        painelBotoesCRUD.add(btnAdicionarCalendario);
        painelBotoesCRUD.add(btnBuscarCalendario);
        painelBotoesCRUD.add(btnAtualizarCalendario);
        painelBotoesCRUD.add(btnDeletarCalendario);
        painelBotoesCRUD.add(btnLimpar);
        painelBotoesCRUD.add(btnListarTodos);

        // Adiciona o painel de botões CRUD abaixo do campo de ano letivo
        JPanel painelCentralSuperior = new JPanel(new BorderLayout());
        painelCentralSuperior.add(painelSuperior, BorderLayout.NORTH);
        painelCentralSuperior.add(painelBotoesCRUD, BorderLayout.CENTER);
        add(painelCentralSuperior, BorderLayout.NORTH);


        JTabbedPane abasGerenciamento = new JTabbedPane();

        // Aba 1: Datas Letivas
        JPanel painelDatasLetivas = new JPanel(new BorderLayout(5, 5));
        painelDatasLetivas.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel pnlInputDataLetiva = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlInputDataLetiva.add(new JLabel("Data Letiva (AAAA-MM-DD):"));
        campoDataLetiva = new JTextField(15);
        pnlInputDataLetiva.add(campoDataLetiva);
        JButton btnAddDataLetiva = new JButton("Adicionar Data");
        JButton btnRemoverDataLetiva = new JButton("Remover Data");
        pnlInputDataLetiva.add(btnAddDataLetiva);
        pnlInputDataLetiva.add(btnRemoverDataLetiva);
        painelDatasLetivas.add(pnlInputDataLetiva, BorderLayout.NORTH);

        modeloDatasLetivas = new DefaultListModel<>();
        listaDatasLetivas = new JList<>(modeloDatasLetivas);
        painelDatasLetivas.add(new JScrollPane(listaDatasLetivas), BorderLayout.CENTER);
        
        abasGerenciamento.addTab("Datas Letivas", painelDatasLetivas);

        // Aba 2: Feriados
        JPanel painelFeriados = new JPanel(new BorderLayout(5, 5));
        painelFeriados.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel pnlInputFeriado = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlInputFeriado.add(new JLabel("Feriado (AAAA-MM-DD):"));
        campoFeriado = new JTextField(15);
        pnlInputFeriado.add(campoFeriado);
        JButton btnAddFeriado = new JButton("Adicionar Feriado");
        JButton btnRemoverFeriado = new JButton("Remover Feriado");
        pnlInputFeriado.add(btnAddFeriado);
        pnlInputFeriado.add(btnRemoverFeriado);
        painelFeriados.add(pnlInputFeriado, BorderLayout.NORTH);

        modeloFeriados = new DefaultListModel<>();
        listaFeriados = new JList<>(modeloFeriados);
        painelFeriados.add(new JScrollPane(listaFeriados), BorderLayout.CENTER);

        abasGerenciamento.addTab("Feriados", painelFeriados);

        // Aba 3: Avaliações
        JPanel painelAvaliacoes = new JPanel(new BorderLayout(5, 5));
        painelAvaliacoes.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel pnlInputAvaliacao = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlInputAvaliacao.add(new JLabel("Avaliação Disponível:"));
        comboAvaliacoesDisponiveis = new JComboBox<>();
        pnlInputAvaliacao.add(comboAvaliacoesDisponiveis);
        JButton btnAddAvaliacao = new JButton("Registrar Avaliação");
        JButton btnRemoverAvaliacao = new JButton("Remover Avaliação");
        pnlInputAvaliacao.add(btnAddAvaliacao);
        pnlInputAvaliacao.add(btnRemoverAvaliacao);
        painelAvaliacoes.add(pnlInputAvaliacao, BorderLayout.NORTH);

        modeloAvaliacoesNoCalendario = new DefaultListModel<>();
        listaAvaliacoesNoCalendario = new JList<>(modeloAvaliacoesNoCalendario);
        painelAvaliacoes.add(new JScrollPane(listaAvaliacoesNoCalendario), BorderLayout.CENTER);
        
        abasGerenciamento.addTab("Avaliações", painelAvaliacoes);

        add(abasGerenciamento, BorderLayout.CENTER);

        areaResultados = new JTextArea();
        areaResultados.setEditable(false);
        JScrollPane scrollPaneResultados = new JScrollPane(areaResultados);
        add(scrollPaneResultados, BorderLayout.SOUTH);

        btnAdicionarCalendario.addActionListener(e -> adicionarCalendario());
        btnBuscarCalendario.addActionListener(e -> buscarCalendario());
        btnAtualizarCalendario.addActionListener(e -> atualizarCalendario());
        btnDeletarCalendario.addActionListener(e -> deletarCalendario());
        btnLimpar.addActionListener(e -> limparCampos());
        btnListarTodos.addActionListener(e -> listarTodosCalendarios());

        btnAddDataLetiva.addActionListener(e -> adicionarDataLetivaCalendario());
        btnRemoverDataLetiva.addActionListener(e -> removerDataLetivaCalendario());
        btnAddFeriado.addActionListener(e -> adicionarFeriadoCalendario());
        btnRemoverFeriado.addActionListener(e -> removerFeriadoCalendario());
        btnAddAvaliacao.addActionListener(e -> registrarAvaliacaoNoCalendario());
        btnRemoverAvaliacao.addActionListener(e -> removerAvaliacaoDoCalendario());

        popularAvaliacoes();
        limparCampos();
        listarTodosCalendarios();
    }

    private void popularAvaliacoes() {
        try {
            List<Avaliacao> avaliacoes = fachada.listarTodasAvaliacoes();
            comboAvaliacoesDisponiveis.removeAllItems();
            for (Avaliacao a : avaliacoes) {
                comboAvaliacoesDisponiveis.addItem(a);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar avaliações: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void atualizarListasCalendario(CalendarioEscolar calendario) {
        modeloDatasLetivas.clear();
        if (calendario != null && calendario.getDatasLetivas() != null) {
            calendario.getDatasLetivas().stream().sorted().forEach(modeloDatasLetivas::addElement);
        }

        modeloFeriados.clear();
        if (calendario != null && calendario.getFeriados() != null) {
            calendario.getFeriados().stream().sorted().forEach(modeloFeriados::addElement);
        }

        modeloAvaliacoesNoCalendario.clear();
        if (calendario != null && calendario.getAvaliacoes() != null) {
            calendario.getAvaliacoes().stream().sorted((a1, a2) -> a1.getId().compareTo(a2.getId())).forEach(modeloAvaliacoesNoCalendario::addElement);
        }
    }

    private void adicionarCalendario() {
        try {
            int anoLetivo = Integer.parseInt(campoAnoLetivo.getText());
            fachada.adicionarCalendario(anoLetivo);
            JOptionPane.showMessageDialog(this, "Calendário adicionado com sucesso para o ano " + anoLetivo + "!");
            limparCampos();
            listarTodosCalendarios();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ano Letivo deve ser um número inteiro.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (DadoInvalidoException ex) {
            JOptionPane.showMessageDialog(this, "Erro de Validação: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao adicionar calendário: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void buscarCalendario() {
        try {
            int anoLetivo = Integer.parseInt(campoAnoLetivo.getText());
            CalendarioEscolar calendario = fachada.buscarCalendario(anoLetivo);
            exibirCalendario(calendario);
            atualizarListasCalendario(calendario);
            JOptionPane.showMessageDialog(this, "Calendário encontrado para o ano " + anoLetivo + "!");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ano Letivo deve ser um número inteiro para buscar.", "Aviso", JOptionPane.WARNING_MESSAGE);
        } catch (EntidadeNaoEncontradaException ex) {
            JOptionPane.showMessageDialog(this, "Calendário não encontrado: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            limparCampos();
        } catch (DadoInvalidoException ex) {
            JOptionPane.showMessageDialog(this, "Erro de Validação: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao buscar calendário: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void atualizarCalendario() {
        try {
            int anoLetivo = Integer.parseInt(campoAnoLetivo.getText());
            CalendarioEscolar calendarioAtualizado = new CalendarioEscolar(anoLetivo); // A atualização aqui é por ano letivo, datas e avaliações são gerenciadas em métodos separados.
            fachada.atualizarCalendario(calendarioAtualizado);
            JOptionPane.showMessageDialog(this, "Calendário atualizado com sucesso para o ano " + anoLetivo + "!");
            limparCampos();
            listarTodosCalendarios();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ano Letivo deve ser um número inteiro.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (EntidadeNaoEncontradaException ex) {
            JOptionPane.showMessageDialog(this, "Erro: Calendário não encontrado para atualização. " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (DadoInvalidoException ex) {
            JOptionPane.showMessageDialog(this, "Erro de Validação: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar calendário: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void deletarCalendario() {
        try {
            int anoLetivo = Integer.parseInt(campoAnoLetivo.getText());
            int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja deletar o calendário do ano " + anoLetivo + "?", "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                boolean deletado = fachada.deletarCalendario(anoLetivo);
                if (deletado) {
                    JOptionPane.showMessageDialog(this, "Calendário do ano " + anoLetivo + " deletado com sucesso!");
                    limparCampos();
                    listarTodosCalendarios();
                } else {
                    JOptionPane.showMessageDialog(this, "Não foi possível deletar o calendário.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ano Letivo deve ser um número inteiro para deletar.", "Aviso", JOptionPane.WARNING_MESSAGE);
        } catch (EntidadeNaoEncontradaException ex) {
            JOptionPane.showMessageDialog(this, "Erro: Calendário não encontrado para exclusão. " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (DadoInvalidoException ex) {
            JOptionPane.showMessageDialog(this, "Erro de Validação: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao deletar calendário: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void adicionarDataLetivaCalendario() {
        try {
            int anoLetivo = Integer.parseInt(campoAnoLetivo.getText());
            LocalDate data = LocalDate.parse(campoDataLetiva.getText());
            fachada.adicionarDataLetivaCalendario(anoLetivo, data);
            JOptionPane.showMessageDialog(this, "Data letiva adicionada com sucesso!");
            buscarCalendario();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ano Letivo deve ser um número inteiro.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Formato de data inválido. Use AAAA-MM-DD.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (EntidadeNaoEncontradaException | DadoInvalidoException ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao adicionar data letiva: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void removerDataLetivaCalendario() {
        try {
            int anoLetivo = Integer.parseInt(campoAnoLetivo.getText());
            LocalDate dataSelecionada = listaDatasLetivas.getSelectedValue();
            if (dataSelecionada == null) {
                JOptionPane.showMessageDialog(this, "Selecione uma data para remover.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            fachada.removerDataLetivaCalendario(anoLetivo, dataSelecionada);
            JOptionPane.showMessageDialog(this, "Data letiva removida com sucesso!");
            buscarCalendario();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ano Letivo deve ser um número inteiro.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (EntidadeNaoEncontradaException | DadoInvalidoException ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao remover data letiva: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void adicionarFeriadoCalendario() {
        try {
            int anoLetivo = Integer.parseInt(campoAnoLetivo.getText());
            LocalDate data = LocalDate.parse(campoFeriado.getText());
            fachada.adicionarFeriadoCalendario(anoLetivo, data);
            JOptionPane.showMessageDialog(this, "Feriado adicionado com sucesso!");
            buscarCalendario();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ano Letivo deve ser um número inteiro.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Formato de data inválido. Use AAAA-MM-DD.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (EntidadeNaoEncontradaException | DadoInvalidoException ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao adicionar feriado: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void removerFeriadoCalendario() {
        try {
            int anoLetivo = Integer.parseInt(campoAnoLetivo.getText());
            LocalDate dataSelecionada = listaFeriados.getSelectedValue();
            if (dataSelecionada == null) {
                JOptionPane.showMessageDialog(this, "Selecione um feriado para remover.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            fachada.removerFeriadoCalendario(anoLetivo, dataSelecionada);
            JOptionPane.showMessageDialog(this, "Feriado removido com sucesso!");
            buscarCalendario();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ano Letivo deve ser um número inteiro.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (EntidadeNaoEncontradaException | DadoInvalidoException ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao remover feriado: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void registrarAvaliacaoNoCalendario() {
        try {
            int anoLetivo = Integer.parseInt(campoAnoLetivo.getText());
            Avaliacao avaliacaoSelecionada = (Avaliacao) comboAvaliacoesDisponiveis.getSelectedItem();
            if (avaliacaoSelecionada == null) {
                JOptionPane.showMessageDialog(this, "Selecione uma avaliação para registrar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            fachada.registrarAvaliacaoNoCalendario(anoLetivo, avaliacaoSelecionada.getId());
            JOptionPane.showMessageDialog(this, "Avaliação registrada no calendário com sucesso!");
            buscarCalendario();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ano Letivo deve ser um número inteiro.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (EntidadeNaoEncontradaException | DadoInvalidoException ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao registrar avaliação: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void removerAvaliacaoDoCalendario() {
        try {
            int anoLetivo = Integer.parseInt(campoAnoLetivo.getText());
            Avaliacao avaliacaoSelecionada = listaAvaliacoesNoCalendario.getSelectedValue();
            if (avaliacaoSelecionada == null) {
                JOptionPane.showMessageDialog(this, "Selecione uma avaliação para remover.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            fachada.removerAvaliacaoDoCalendario(anoLetivo, avaliacaoSelecionada.getId());
            JOptionPane.showMessageDialog(this, "Avaliação removida do calendário com sucesso!");
            buscarCalendario();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ano Letivo deve ser um número inteiro.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (EntidadeNaoEncontradaException | DadoInvalidoException ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao remover avaliação: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void limparCampos() {
        campoAnoLetivo.setText("");
        campoDataLetiva.setText("");
        campoFeriado.setText("");
        areaResultados.setText("");
        modeloDatasLetivas.clear();
        modeloFeriados.clear();
        modeloAvaliacoesNoCalendario.clear();
        popularAvaliacoes();
    }

    private void listarTodosCalendarios() {
        try {
            List<CalendarioEscolar> calendarios = fachada.listarTodosCalendarios();
            if (calendarios.isEmpty()) {
                areaResultados.setText("Nenhum calendário cadastrado.");
            } else {
                StringBuilder sb = new StringBuilder("--- Lista de Calendários ---\n");
                for (CalendarioEscolar c : calendarios) {
                    sb.append("Ano Letivo: ").append(c.getAnoLetivo()).append("\n");
                    if (c.getDatasLetivas() != null && !c.getDatasLetivas().isEmpty()) {
                        sb.append("  Datas Letivas: ").append(c.getDatasLetivas().stream().sorted().map(LocalDate::toString).reduce((a, b) -> a + ", " + b).orElse("")).append("\n");
                    }
                    if (c.getFeriados() != null && !c.getFeriados().isEmpty()) {
                        sb.append("  Feriados: ").append(c.getFeriados().stream().sorted().map(LocalDate::toString).reduce((a, b) -> a + ", " + b).orElse("")).append("\n");
                    }
                    if (c.getAvaliacoes() != null && !c.getAvaliacoes().isEmpty()) {
                        sb.append("  Avaliações Registradas: ");
                        c.getAvaliacoes().stream().sorted((a1, a2) -> a1.getId().compareTo(a2.getId())).forEach(a -> sb.append(a.getNomeAvaliacao()).append(" (").append(a.getId()).append("); "));
                        sb.append("\n");
                    }
                    sb.append("---------------------------------------\n");
                }
                areaResultados.setText(sb.toString());
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao listar calendários: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void exibirCalendario(CalendarioEscolar calendario) {
        if (calendario != null) {
            campoAnoLetivo.setText(String.valueOf(calendario.getAnoLetivo()));
            StringBuilder sb = new StringBuilder();
            sb.append("--- Detalhes do Calendário ---\n")
              .append("Ano Letivo: ").append(calendario.getAnoLetivo()).append("\n");

            if (calendario.getDatasLetivas() != null && !calendario.getDatasLetivas().isEmpty()) {
                sb.append("Datas Letivas:\n");
                calendario.getDatasLetivas().stream().sorted().forEach(data -> sb.append("  - ").append(data).append("\n"));
            } else {
                sb.append("Nenhuma data letiva registrada.\n");
            }

            if (calendario.getFeriados() != null && !calendario.getFeriados().isEmpty()) {
                sb.append("Feriados:\n");
                calendario.getFeriados().stream().sorted().forEach(feriado -> sb.append("  - ").append(feriado).append("\n"));
            } else {
                sb.append("Nenhum feriado registrado.\n");
            }

            if (calendario.getAvaliacoes() != null && !calendario.getAvaliacoes().isEmpty()) {
                sb.append("Avaliações Registradas:\n");
                calendario.getAvaliacoes().stream().sorted((a1, a2) -> a1.getId().compareTo(a2.getId())).forEach(aval -> sb.append("  - ").append(aval.getNomeAvaliacao()).append(" (").append(aval.getId()).append(")\n"));
            } else {
                sb.append("Nenhuma avaliação registrada neste calendário.\n");
            }
            areaResultados.setText(sb.toString());
        } else {
            limparCampos();
            areaResultados.setText("Calendário não encontrado.");
        }
    }
}