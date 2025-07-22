package br.com.escola.gui;

import br.com.escola.negocio.Turma;
import br.com.escola.negocio.Disciplina;
import br.com.escola.negocio.Aluno;
import br.com.escola.negocio.Fachada;
import br.com.escola.negocio.IFachada;
import br.com.escola.excecoes.DadoInvalidoException;
import br.com.escola.excecoes.EntidadeNaoEncontradaException;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Vector; // Não está sendo usado, pode ser removido

public class TelaCadastroTurma extends JDialog {

    private IFachada fachada;

    private JTextField campoCodigo;
    private JTextField campoNome;
    private JTextField campoAnoLetivo;
    private JTextArea areaResultados;

    private JComboBox<Disciplina> comboDisciplinasDisponiveis;
    private JComboBox<Disciplina> comboDisciplinasNaTurma;
    private JComboBox<Aluno> comboAlunosDisponiveis;
    private JComboBox<Aluno> comboAlunosNaTurma;

    public TelaCadastroTurma(JFrame parent, boolean modal) {
        super(parent, "Cadastro de Turmas", modal);
        this.fachada = Fachada.getInstance();

        setSize(800, 700);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(painelPrincipal, BorderLayout.CENTER);

        JPanel painelDadosTurma = new JPanel(new GridLayout(3, 2, 10, 10));
        painelDadosTurma.setBorder(BorderFactory.createTitledBorder("Dados da Turma"));

        painelDadosTurma.add(new JLabel("Código:"));
        campoCodigo = new JTextField();
        painelDadosTurma.add(campoCodigo);

        painelDadosTurma.add(new JLabel("Nome:"));
        campoNome = new JTextField();
        painelDadosTurma.add(campoNome);

        painelDadosTurma.add(new JLabel("Ano Letivo:"));
        campoAnoLetivo = new JTextField();
        painelDadosTurma.add(campoAnoLetivo);

        painelPrincipal.add(painelDadosTurma, BorderLayout.NORTH);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnAdicionar = new JButton("Adicionar Turma");
        JButton btnBuscar = new JButton("Buscar Turma");
        JButton btnAtualizar = new JButton("Atualizar Turma");
        JButton btnDeletar = new JButton("Deletar Turma");
        JButton btnLimpar = new JButton("Limpar Campos");
        JButton btnListarTodos = new JButton("Listar Todas");

        painelBotoes.add(btnAdicionar);
        painelBotoes.add(btnBuscar);
        painelBotoes.add(btnAtualizar);
        painelBotoes.add(btnDeletar);
        painelBotoes.add(btnLimpar);
        painelBotoes.add(btnListarTodos);

        painelPrincipal.add(painelBotoes, BorderLayout.CENTER);

        JPanel painelAssociacoes = new JPanel(new GridLayout(1, 2, 10, 10));
        painelAssociacoes.setBorder(BorderFactory.createTitledBorder("Associações da Turma"));

        JPanel painelDisciplinas = new JPanel(new BorderLayout(5, 5));
        painelDisciplinas.setBorder(BorderFactory.createTitledBorder("Disciplinas da Turma"));
        comboDisciplinasDisponiveis = new JComboBox<>();
        JButton btnAddDisciplina = new JButton("Adicionar Disciplina");
        comboDisciplinasNaTurma = new JComboBox<>();
        JButton btnRemoverDisciplina = new JButton("Remover Disciplina");

        JPanel pnlAddDisc = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlAddDisc.add(new JLabel("Disponíveis:"));
        pnlAddDisc.add(comboDisciplinasDisponiveis);
        pnlAddDisc.add(btnAddDisciplina);
        painelDisciplinas.add(pnlAddDisc, BorderLayout.NORTH);

        JPanel pnlRemovDisc = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlRemovDisc.add(new JLabel("Na Turma:"));
        pnlRemovDisc.add(comboDisciplinasNaTurma);
        pnlRemovDisc.add(btnRemoverDisciplina);
        painelDisciplinas.add(pnlRemovDisc, BorderLayout.SOUTH);

        painelAssociacoes.add(painelDisciplinas);

        JPanel painelAlunos = new JPanel(new BorderLayout(5, 5));
        painelAlunos.setBorder(BorderFactory.createTitledBorder("Alunos da Turma"));
        comboAlunosDisponiveis = new JComboBox<>();
        JButton btnMatricularAluno = new JButton("Matricular Aluno");
        comboAlunosNaTurma = new JComboBox<>();
        JButton btnDesmatricularAluno = new JButton("Desmatricular Aluno");

        JPanel pnlMatrAluno = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlMatrAluno.add(new JLabel("Disponíveis:"));
        pnlMatrAluno.add(comboAlunosDisponiveis);
        pnlMatrAluno.add(btnMatricularAluno);
        painelAlunos.add(pnlMatrAluno, BorderLayout.NORTH);

        JPanel pnlDesmatrAluno = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlDesmatrAluno.add(new JLabel("Na Turma:"));
        pnlDesmatrAluno.add(comboAlunosNaTurma);
        pnlDesmatrAluno.add(btnDesmatricularAluno);
        painelAlunos.add(pnlDesmatrAluno, BorderLayout.SOUTH);

        painelAssociacoes.add(painelAlunos);

        painelPrincipal.add(painelAssociacoes, BorderLayout.SOUTH);

        areaResultados = new JTextArea();
        areaResultados.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(areaResultados);
        add(scrollPane, BorderLayout.SOUTH);

        btnAdicionar.addActionListener(e -> adicionarTurma());
        btnBuscar.addActionListener(e -> buscarTurma());
        btnAtualizar.addActionListener(e -> atualizarTurma());
        btnDeletar.addActionListener(e -> deletarTurma());
        btnLimpar.addActionListener(e -> limparCampos());
        btnListarTodos.addActionListener(e -> listarTodasTurmas());

        btnAddDisciplina.addActionListener(e -> adicionarDisciplinaNaTurma());
        btnRemoverDisciplina.addActionListener(e -> removerDisciplinaDaTurma());
        btnMatricularAluno.addActionListener(e -> matricularAlunoNaTurma());
        btnDesmatricularAluno.addActionListener(e -> desmatricularAlunoDaTurma());

        popularCombos();
        limparCampos();
        listarTodasTurmas();
    }

    private void popularCombos() {
        try {
            List<Disciplina> disciplinas = fachada.listarTodasDisciplinas();
            comboDisciplinasDisponiveis.removeAllItems();
            for (Disciplina d : disciplinas) {
                comboDisciplinasDisponiveis.addItem(d);
            }

            List<Aluno> alunos = fachada.listarTodosAlunos();
            comboAlunosDisponiveis.removeAllItems();
            for (Aluno a : alunos) {
                comboAlunosDisponiveis.addItem(a);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar dados para os combos: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void atualizarCombosTurma(Turma turma) {
        comboDisciplinasNaTurma.removeAllItems();
        if (turma != null && turma.getDisciplinas() != null) {
            for (Disciplina d : turma.getDisciplinas()) {
                comboDisciplinasNaTurma.addItem(d);
            }
        }

        comboAlunosNaTurma.removeAllItems();
        if (turma != null && turma.getAlunosMatriculados() != null) {
            for (Aluno a : turma.getAlunosMatriculados()) {
                comboAlunosNaTurma.addItem(a);
            }
        }
    }

    private void adicionarTurma() {
        try {
            String codigo = campoCodigo.getText();
            String nome = campoNome.getText();
            int anoLetivo = Integer.parseInt(campoAnoLetivo.getText());

            Turma novaTurma = new Turma(codigo, nome, anoLetivo);
            fachada.adicionarTurma(novaTurma);
            JOptionPane.showMessageDialog(this, "Turma adicionada com sucesso!");
            limparCampos();
            listarTodasTurmas();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ano Letivo deve ser um número válido.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (DadoInvalidoException | EntidadeNaoEncontradaException ex) {
            JOptionPane.showMessageDialog(this, "Erro de Validação: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao adicionar turma: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void buscarTurma() {
        try {
            String codigo = campoCodigo.getText();
            if (codigo.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, digite o código da turma para buscar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Turma turma = fachada.buscarTurma(codigo);
            exibirTurma(turma);
            atualizarCombosTurma(turma);
            JOptionPane.showMessageDialog(this, "Turma encontrada!");
        } catch (EntidadeNaoEncontradaException ex) {
            JOptionPane.showMessageDialog(this, "Turma não encontrada: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            limparCampos();
        } catch (DadoInvalidoException ex) {
            JOptionPane.showMessageDialog(this, "Erro de Validação: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao buscar turma: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void atualizarTurma() {
        try {
            String codigo = campoCodigo.getText();
            String nome = campoNome.getText();
            int anoLetivo = Integer.parseInt(campoAnoLetivo.getText());

            Turma turmaAtualizada = new Turma(codigo, nome, anoLetivo);
            fachada.atualizarTurma(turmaAtualizada);
            JOptionPane.showMessageDialog(this, "Turma atualizada com sucesso!");
            limparCampos();
            listarTodasTurmas();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ano Letivo deve ser um número válido.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (EntidadeNaoEncontradaException ex) {
            JOptionPane.showMessageDialog(this, "Erro: Turma não encontrada para atualização. " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (DadoInvalidoException ex) {
            JOptionPane.showMessageDialog(this, "Erro de Validação: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar turma: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void deletarTurma() {
        try {
            String codigo = campoCodigo.getText();
            if (codigo.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, digite o código da turma para deletar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja deletar a turma " + codigo + "?", "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                boolean deletado = fachada.deletarTurma(codigo);
                if (deletado) {
                    JOptionPane.showMessageDialog(this, "Turma deletada com sucesso!");
                    limparCampos();
                    listarTodasTurmas();
                } else {
                    JOptionPane.showMessageDialog(this, "Não foi possível deletar a turma.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (EntidadeNaoEncontradaException ex) {
            JOptionPane.showMessageDialog(this, "Erro: Turma não encontrada para exclusão. " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (DadoInvalidoException ex) {
            JOptionPane.showMessageDialog(this, "Erro de Validação: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao deletar turma: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void adicionarDisciplinaNaTurma() {
        String codigoTurma = campoCodigo.getText();
        Disciplina disciplinaSelecionada = (Disciplina) comboDisciplinasDisponiveis.getSelectedItem();
        if (codigoTurma.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, busque ou adicione uma turma primeiro.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (disciplinaSelecionada == null) {
            JOptionPane.showMessageDialog(this, "Selecione uma disciplina para adicionar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Supondo que você quer atribuir a disciplina à turma atual com o professor default (null)
            // Você precisaria de um combo para selecionar o professor se a lógica permitir
            // OU, que o método na Fachada será ajustado para receber disciplina e turma para atribuição automática
            fachada.adicionarDisciplinaNaTurma(codigoTurma, disciplinaSelecionada.getCodigo());
            JOptionPane.showMessageDialog(this, "Disciplina adicionada à turma com sucesso!");
            buscarTurma(); // Atualiza a exibição e os combos
        } catch (DadoInvalidoException | EntidadeNaoEncontradaException ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao adicionar disciplina à turma: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void removerDisciplinaDaTurma() {
        String codigoTurma = campoCodigo.getText();
        Disciplina disciplinaSelecionada = (Disciplina) comboDisciplinasNaTurma.getSelectedItem();
        if (codigoTurma.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, busque ou adicione uma turma primeiro.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (disciplinaSelecionada == null) {
            JOptionPane.showMessageDialog(this, "Selecione uma disciplina para remover.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            fachada.removerDisciplinaDaTurma(codigoTurma, disciplinaSelecionada.getCodigo());
            JOptionPane.showMessageDialog(this, "Disciplina removida da turma com sucesso!");
            buscarTurma(); // Atualiza a exibição e os combos
        } catch (DadoInvalidoException | EntidadeNaoEncontradaException ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao remover disciplina da turma: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void matricularAlunoNaTurma() {
        String codigoTurma = campoCodigo.getText();
        Aluno alunoSelecionado = (Aluno) comboAlunosDisponiveis.getSelectedItem();
        if (codigoTurma.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, busque ou adicione uma turma primeiro.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (alunoSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um aluno para matricular.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            fachada.matricularAlunoNaTurma(codigoTurma, alunoSelecionado.getMatricula());
            JOptionPane.showMessageDialog(this, "Aluno matriculado na turma com sucesso!");
            buscarTurma(); // Atualiza a exibição e os combos
        } catch (DadoInvalidoException | EntidadeNaoEncontradaException ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao matricular aluno na turma: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void desmatricularAlunoDaTurma() {
        String codigoTurma = campoCodigo.getText();
        Aluno alunoSelecionado = (Aluno) comboAlunosNaTurma.getSelectedItem();
        if (codigoTurma.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, busque ou adicione uma turma primeiro.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (alunoSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um aluno para desmatricular.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            fachada.desmatricularAlunoDaTurma(codigoTurma, alunoSelecionado.getMatricula());
            JOptionPane.showMessageDialog(this, "Aluno desmatriculado da turma com sucesso!");
            buscarTurma(); // Atualiza a exibição e os combos
        } catch (DadoInvalidoException | EntidadeNaoEncontradaException ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao desmatricular aluno da turma: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void limparCampos() {
        campoCodigo.setText("");
        campoNome.setText("");
        campoAnoLetivo.setText("");
        areaResultados.setText("");
        popularCombos(); // Repopula os combos de disponíveis
        comboDisciplinasNaTurma.removeAllItems(); // Limpa disciplinas da turma
        comboAlunosNaTurma.removeAllItems(); // Limpa alunos da turma
    }

    private void listarTodasTurmas() {
        try {
            List<Turma> turmas = fachada.listarTodasTurmas();
            if (turmas.isEmpty()) {
                areaResultados.setText("Nenhuma turma cadastrada.");
            } else {
                StringBuilder sb = new StringBuilder("--- Lista de Turmas ---\n");
                for (Turma t : turmas) {
                    sb.append("Código: ").append(t.getCodigo())
                      .append(", Nome: ").append(t.getNomeTurma())
                      .append(", Ano Letivo: ").append(t.getAnoLetivo());
                    if (t.getDisciplinas() != null && !t.getDisciplinas().isEmpty()) {
                        sb.append("\n  Disciplinas: ");
                        for (Disciplina d : t.getDisciplinas()) {
                            sb.append(d.getNome()).append(" (").append(d.getCodigo()).append("); ");
                        }
                    }
                    if (t.getAlunosMatriculados() != null && !t.getAlunosMatriculados().isEmpty()) {
                        sb.append("\n  Alunos Matriculados: ");
                        for (Aluno a : t.getAlunosMatriculados()) {
                            sb.append(a.getNome()).append(" (").append(a.getMatricula()).append("); ");
                        }
                    }
                    sb.append("\n------------------------\n");
                }
                areaResultados.setText(sb.toString());
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao listar turmas: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void exibirTurma(Turma turma) {
        if (turma != null) {
            campoCodigo.setText(turma.getCodigo());
            campoNome.setText(turma.getNomeTurma());
            campoAnoLetivo.setText(String.valueOf(turma.getAnoLetivo()));
            StringBuilder sb = new StringBuilder();
            sb.append("--- Detalhes da Turma ---\n")
              .append("Código: ").append(turma.getCodigo()).append("\n")
              .append("Nome: ").append(turma.getNomeTurma()).append("\n")
              .append("Ano Letivo: ").append(turma.getAnoLetivo()).append("\n");

            if (turma.getDisciplinas() != null && !turma.getDisciplinas().isEmpty()) {
                sb.append("Disciplinas:\n");
                for (Disciplina d : turma.getDisciplinas()) {
                    sb.append("  - ").append(d.getNome()).append(" (").append(d.getCodigo()).append(")\n");
                }
            } else {
                sb.append("Nenhuma disciplina associada.\n");
            }

            if (turma.getAlunosMatriculados() != null && !turma.getAlunosMatriculados().isEmpty()) {
                sb.append("Alunos Matriculados:\n");
                for (Aluno a : turma.getAlunosMatriculados()) {
                    sb.append("  - ").append(a.getNome()).append(" (").append(a.getMatricula()).append(")\n");
                }
            } else {
                sb.append("Nenhum aluno matriculado.\n");
            }
            areaResultados.setText(sb.toString());
        } else {
            limparCampos();
            areaResultados.setText("Turma não encontrada.");
        }
    }
}