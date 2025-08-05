package br.com.escola.gui;

import br.com.escola.negocio.CalendarioEscolar;
import br.com.escola.negocio.Evento;
import br.com.escola.negocio.Avaliacao;
import br.com.escola.negocio.Fachada;
import br.com.escola.negocio.IFachada;
import br.com.escola.excecoes.DadoInvalidoException;
import br.com.escola.excecoes.EntidadeNaoEncontradaException;
import br.com.escola.util.EventoCalendario;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

public class TelaCalendarioEscolar extends JDialog {
    private IFachada fachada;
    private int anoAtual;
    private int mesAtual;
    private JLabel labelMesAno;
    private JPanel painelCalendario;
    private CalendarioEscolar calendarioAtual;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final Locale LOCALE_PT_BR = new Locale("pt", "BR");

    public TelaCalendarioEscolar(JFrame parent, boolean modal) {
        super(parent, "Gerenciamento de Calendário Escolar", modal);
        this.fachada = Fachada.getInstance();
        setSize(800, 700);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));
        this.anoAtual = LocalDate.now().getYear();
        this.mesAtual = LocalDate.now().getMonthValue();
        JPanel painelControle = new JPanel(new BorderLayout());
        JPanel painelNavegacao = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        JButton btnAnterior = new JButton("<");
        JButton btnProximo = new JButton(">");
        labelMesAno = new JLabel("", SwingConstants.CENTER);
        labelMesAno.setFont(new Font("Arial", Font.BOLD, 18));
        painelNavegacao.add(btnAnterior);
        painelNavegacao.add(labelMesAno);
        painelNavegacao.add(btnProximo);
        painelControle.add(painelNavegacao, BorderLayout.CENTER);
        JPanel painelBotoesAcao = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        JButton btnAdicionarCalendario = new JButton("Adicionar Ano Letivo");
        JButton btnDeletarCalendario = new JButton("Remover Ano Letivo");
        painelBotoesAcao.add(btnAdicionarCalendario);
        painelBotoesAcao.add(btnDeletarCalendario);
        painelControle.add(painelBotoesAcao, BorderLayout.SOUTH);
        add(painelControle, BorderLayout.NORTH);
        painelCalendario = new JPanel(new GridLayout(0, 7, 5, 5));
        painelCalendario.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        adicionarNomesDosDiasDaSemana();
        add(new JScrollPane(painelCalendario), BorderLayout.CENTER);
        btnAnterior.addActionListener(e -> navegarMesAnterior());
        btnProximo.addActionListener(e -> navegarProximoMes());
        btnAdicionarCalendario.addActionListener(e -> adicionarCalendario());
        btnDeletarCalendario.addActionListener(e -> deletarCalendario());
        carregarCalendarioDoAnoAtual();
        popularCalendario();
    }

    private void adicionarNomesDosDiasDaSemana() {
        String[] diasDaSemana = {"Dom", "Seg", "Ter", "Qua", "Qui", "Sex", "Sáb"};
        for (String dia : diasDaSemana) {
            JLabel label = new JLabel(dia, SwingConstants.CENTER);
            label.setFont(new Font("Arial", Font.BOLD, 12));
            painelCalendario.add(label);
        }
    }

    private void carregarCalendarioDoAnoAtual() {
        try {
            this.calendarioAtual = fachada.buscarCalendario(anoAtual);
        } catch (EntidadeNaoEncontradaException | DadoInvalidoException ex) {
            this.calendarioAtual = null;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar o calendário do ano " + anoAtual + ": " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void popularCalendario() {
        painelCalendario.removeAll();
        adicionarNomesDosDiasDaSemana();
        YearMonth anoMes = YearMonth.of(anoAtual, mesAtual);
        labelMesAno.setText(anoMes.getMonth().getDisplayName(TextStyle.FULL, LOCALE_PT_BR) + " de " + anoAtual);
        LocalDate primeiroDiaDoMes = anoMes.atDay(1);
        int diaDaSemanaDoPrimeiroDia = primeiroDiaDoMes.getDayOfWeek().getValue();
        int espacosVazios = (diaDaSemanaDoPrimeiroDia == 7) ? 0 : diaDaSemanaDoPrimeiroDia;
        for (int i = 0; i < espacosVazios; i++) {
            painelCalendario.add(new JLabel(""));
        }
        for (int dia = 1; dia <= anoMes.lengthOfMonth(); dia++) {
            LocalDate data = anoMes.atDay(dia);
            JButton botaoDia = new JButton(String.valueOf(dia));
            botaoDia.setOpaque(true);
            botaoDia.setBorderPainted(false);
            botaoDia.setBackground(Color.WHITE);
            if (calendarioAtual != null) {
                if (calendarioAtual.getFeriados().contains(data)) {
                    botaoDia.setBackground(Color.RED);
                    botaoDia.setForeground(Color.WHITE);
                } else if (calendarioAtual.getDatasLetivas().contains(data)) {
                    botaoDia.setBackground(Color.LIGHT_GRAY);
                }
                boolean temAvaliacao = calendarioAtual.getAvaliacoes().stream().anyMatch(a -> a.getDataInicio().equals(data));
                boolean temEventoGenerico = calendarioAtual.getEventos().stream().anyMatch(e -> e.getData().equals(data));
                if (temAvaliacao || temEventoGenerico) {
                    botaoDia.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
                    botaoDia.setToolTipText("Eventos neste dia");
                }
            }
            botaoDia.addActionListener(e -> new JDialogDetalhesDia(TelaCalendarioEscolar.this, data, calendarioAtual).setVisible(true));
            painelCalendario.add(botaoDia);
        }
        painelCalendario.revalidate();
        painelCalendario.repaint();
    }

    private void navegarMesAnterior() {
        if (mesAtual == 1) {
            mesAtual = 12;
            anoAtual--;
            carregarCalendarioDoAnoAtual();
        } else {
            mesAtual--;
        }
        popularCalendario();
    }

    private void navegarProximoMes() {
        if (mesAtual == 12) {
            mesAtual = 1;
            anoAtual++;
            carregarCalendarioDoAnoAtual();
        } else {
            mesAtual++;
        }
        popularCalendario();
    }

    private void adicionarCalendario() {
        try {
            String inputAno = JOptionPane.showInputDialog(this, "Digite o ano letivo do novo calendário:");
            if (inputAno != null && !inputAno.trim().isEmpty()) {
                int novoAno = Integer.parseInt(inputAno);
                fachada.adicionarCalendario(novoAno);
                JOptionPane.showMessageDialog(this, "Calendário adicionado com sucesso para o ano " + novoAno + "!");
                this.anoAtual = novoAno;
                this.mesAtual = 1;
                carregarCalendarioDoAnoAtual();
                popularCalendario();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ano Letivo deve ser um número inteiro.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (DadoInvalidoException ex) {
            JOptionPane.showMessageDialog(this, "Erro de Validação: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao adicionar calendário: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deletarCalendario() {
        try {
            String inputAno = JOptionPane.showInputDialog(this, "Digite o ano letivo do calendário que deseja remover:");
            if (inputAno != null && !inputAno.trim().isEmpty()) {
                int anoParaRemover = Integer.parseInt(inputAno);
                int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja deletar o calendário do ano " + anoParaRemover + "?", "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    fachada.deletarCalendario(anoParaRemover);
                    JOptionPane.showMessageDialog(this, "Calendário do ano " + anoParaRemover + " deletado com sucesso!");
                    if (this.anoAtual == anoParaRemover) {
                        this.calendarioAtual = null;
                        this.anoAtual = LocalDate.now().getYear();
                        this.mesAtual = LocalDate.now().getMonthValue();
                        carregarCalendarioDoAnoAtual();
                    }
                    popularCalendario();
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ano Letivo deve ser um número inteiro.", "Aviso", JOptionPane.WARNING_MESSAGE);
        } catch (EntidadeNaoEncontradaException ex) {
            JOptionPane.showMessageDialog(this, "Erro: Calendário não encontrado para exclusão. " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao deletar calendário: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private class JDialogDetalhesDia extends JDialog {
        private CalendarioEscolar calendario;
        private LocalDate data;
        private JList<Object> listaEventos;
        private DefaultListModel<Object> modeloEventos;
        private JTextField campoNovoEvento;

        public JDialogDetalhesDia(Dialog parent, LocalDate data, CalendarioEscolar calendario) {
            super(parent, "Eventos do Dia " + data.format(DATE_FORMATTER), true);
            this.calendario = calendario;
            this.data = data;
            if (this.calendario == null) {
                setLayout(new BorderLayout());
                add(new JLabel("Nenhum calendário encontrado para este ano.", SwingConstants.CENTER), BorderLayout.CENTER);
                pack();
                setLocationRelativeTo(parent);
                return;
            }

            setLayout(new BorderLayout(10, 10));

            JPanel painelControlesSuperiores = new JPanel();
            painelControlesSuperiores.setLayout(new BoxLayout(painelControlesSuperiores, BoxLayout.Y_AXIS));

            JPanel painelStatus = new JPanel(new FlowLayout(FlowLayout.LEFT));
            painelStatus.setBorder(BorderFactory.createTitledBorder("Status do Dia"));
            if (calendario.getFeriados().contains(data)) {
                painelStatus.add(new JLabel("<html><font color='red'><b>Feriado</b></font></html>"));
            } else if (calendario.getDatasLetivas().contains(data)) {
                painelStatus.add(new JLabel("<html><font color='blue'><b>Dia Letivo</b></font></html>"));
            } else {
                painelStatus.add(new JLabel("<html><font color='gray'><b>Dia comum</b></font></html>"));
            }

            JPanel painelControleDia = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
            JButton btnAlternarDiaLetivo = new JButton(calendario.getDatasLetivas().contains(data) ? "Remover de Dia Letivo" : "Marcar como Dia Letivo");
            JButton btnAlternarFeriado = new JButton(calendario.getFeriados().contains(data) ? "Remover de Feriado" : "Marcar como Feriado");
            btnAlternarDiaLetivo.addActionListener(e -> {
                try {
                    if (calendario.getDatasLetivas().contains(data)) {
                        fachada.removerDataLetivaCalendario(calendario.getAnoLetivo(), data);
                    } else {
                        fachada.adicionarDataLetivaCalendario(calendario.getAnoLetivo(), data);
                    }
                    JOptionPane.showMessageDialog(this, "Configuração de dia letivo atualizada!");
                    ((TelaCalendarioEscolar) getOwner()).popularCalendario();
                    dispose();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erro ao atualizar dia letivo: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            });
            btnAlternarFeriado.addActionListener(e -> {
                try {
                    if (calendario.getFeriados().contains(data)) {
                        fachada.removerFeriadoCalendario(calendario.getAnoLetivo(), data);
                    } else {
                        fachada.adicionarFeriadoCalendario(calendario.getAnoLetivo(), data);
                    }
                    JOptionPane.showMessageDialog(this, "Configuração de feriado atualizada!");
                    ((TelaCalendarioEscolar) getOwner()).popularCalendario();
                    dispose();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erro ao atualizar feriado: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            });
            painelControleDia.add(btnAlternarDiaLetivo);
            painelControleDia.add(btnAlternarFeriado);

            painelControlesSuperiores.add(painelStatus);
            painelControlesSuperiores.add(painelControleDia);

            JPanel painelEventosDia = new JPanel(new BorderLayout(5,5));
            painelEventosDia.setBorder(BorderFactory.createTitledBorder("Eventos e Avaliações Agendados"));

            modeloEventos = new DefaultListModel<>();
            listaEventos = new JList<>(modeloEventos);
            listaEventos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            painelEventosDia.add(new JScrollPane(listaEventos), BorderLayout.CENTER);

            JPanel painelEventoControle = new JPanel(new FlowLayout(FlowLayout.LEFT));
            campoNovoEvento = new JTextField(20);
            JButton btnAddEvento = new JButton("Adicionar");
            btnAddEvento.addActionListener(e -> adicionarEvento());

            JButton btnRemoverEvento = new JButton("Remover");
            btnRemoverEvento.addActionListener(e -> removerEvento());

            painelEventoControle.add(new JLabel("Novo Evento:"));
            painelEventoControle.add(campoNovoEvento);
            painelEventoControle.add(btnAddEvento);
            painelEventoControle.add(btnRemoverEvento);

            painelEventosDia.add(painelEventoControle, BorderLayout.SOUTH);

            add(painelControlesSuperiores, BorderLayout.NORTH);
            add(painelEventosDia, BorderLayout.CENTER);

            carregarDetalhesDoDia();
            pack();
            setLocationRelativeTo(parent);
        }

        private void carregarDetalhesDoDia() {
            modeloEventos.clear();
            if (calendario != null) {
                calendario.getAvaliacoes().stream()
                    .filter(a -> a.getDataInicio().equals(data))
                    .sorted((a1, a2) -> a1.getId().compareTo(a2.getId()))
                    .forEach(a -> modeloEventos.addElement("Avaliação: " + a.getNomeAvaliacao() + " (" + a.getId() + ")"));

                calendario.getEventos().stream()
                    .filter(e -> e.getData().equals(data))
                    .sorted((e1, e2) -> e1.getDescricao().compareTo(e2.getDescricao()))
                    .forEach(e -> modeloEventos.addElement("Evento: " + e.getDescricao() + " (ID:" + e.getId() + ")"));
            }
        }

        private void adicionarEvento() {
            try {
                String descricaoEvento = campoNovoEvento.getText().trim();
                if (descricaoEvento.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Digite a descrição do evento para adicionar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                String idEvento = UUID.randomUUID().toString();
                EventoCalendario novoEvento = new EventoCalendario(idEvento, data, descricaoEvento, "GENERICO");
                fachada.adicionarEventoNoCalendario(calendario.getAnoLetivo(), novoEvento);
                
                JOptionPane.showMessageDialog(this, "Evento adicionado com sucesso!");
                campoNovoEvento.setText("");
                carregarDetalhesDoDia();
                ((TelaCalendarioEscolar) getOwner()).popularCalendario();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao adicionar evento: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void removerEvento() {
            try {
                Object itemSelecionado = listaEventos.getSelectedValue();
                if (itemSelecionado == null) {
                    JOptionPane.showMessageDialog(this, "Selecione um evento para remover.", "Aviso", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (itemSelecionado.toString().startsWith("Avaliação:")) {
                    JOptionPane.showMessageDialog(this, "A remoção de avaliações deve ser feita na tela de Avaliações.", "Aviso", JOptionPane.WARNING_MESSAGE);
                } else if (itemSelecionado.toString().startsWith("Evento:")) {
                    String textoItem = itemSelecionado.toString();
                    String idEvento = textoItem.substring(textoItem.indexOf("(ID:") + 4, textoItem.indexOf(")"));
                    fachada.removerEventoDoCalendario(calendario.getAnoLetivo(), idEvento);
                    
                    JOptionPane.showMessageDialog(this, "Evento removido com sucesso!");
                    carregarDetalhesDoDia();
                    ((TelaCalendarioEscolar) getOwner()).popularCalendario();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao remover evento: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}