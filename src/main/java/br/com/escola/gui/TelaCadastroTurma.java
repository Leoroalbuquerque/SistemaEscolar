package br.com.escola.gui;

import br.com.escola.negocio.Aluno;
import br.com.escola.negocio.Disciplina;
import br.com.escola.negocio.Fachada;
import br.com.escola.negocio.Professor;
import br.com.escola.negocio.SerieEscolar;
import br.com.escola.negocio.SerieEscolar.NivelEscolar;
import br.com.escola.negocio.Turma;
import br.com.escola.excecoes.DadoInvalidoException;
import br.com.escola.excecoes.EntidadeNaoEncontradaException;
import br.com.escola.negocio.DisciplinaTurma;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TelaCadastroTurma extends JDialog {

    private final Fachada fachada;
    private Turma turmaAtual;

    private JComboBox<SerieEscolar.NivelEscolar> cbNivelEscolar;
    private JSpinner spnAnoLetivo;
    private JComboBox<SerieEscolar> cbSerieEscolar;
    private JComboBox<String> cbTurno;

    private JList<Aluno> listaAlunosMatriculados;
    private DefaultListModel<Aluno> modeloListaAlunosMatriculados;
    private JComboBox<Aluno> cbAlunosDisponiveis;
    private JButton btnMatricularAluno;
    private JButton btnDesmatricularAluno;

    private JList<DisciplinaTurma> listaAtribuicoes;
    private DefaultListModel<DisciplinaTurma> modeloListaAtribuicoes;
    private JComboBox<Disciplina> cbDisciplinasDisponiveis;
    private JComboBox<Professor> cbProfessoresDisponiveis;
    private JButton btnAdicionarAtribuicao;
    private JButton btnRemoverAtribuicao;

    public TelaCadastroTurma(Frame parent, boolean modal) {
        super(parent, "Cadastro de Turma", modal);
        this.fachada = Fachada.getInstance();
        setSize(800, 700);
        setLocationRelativeTo(parent);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initComponents();
        carregarNiveisEscolares();
        carregarSeriesEscolares();
        carregarAlunosDisponiveis();
        carregarDisciplinasDisponiveis();
        carregarProfessoresDisponiveis();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        add(mainPanel);

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Dados da Turma"));

        formPanel.add(new JLabel("Nível Escolar:"));
        cbNivelEscolar = new JComboBox<>();
        formPanel.add(cbNivelEscolar);

        formPanel.add(new JLabel("Turno:"));
        String[] turnos = {"Manhã", "Tarde"};
        cbTurno = new JComboBox<>(turnos);
        formPanel.add(cbTurno);

        formPanel.add(new JLabel("Ano Letivo:"));
        spnAnoLetivo = new JSpinner(new SpinnerNumberModel(LocalDate.now().getYear(), 1900, 2100, 1));
        formPanel.add(spnAnoLetivo);

        formPanel.add(new JLabel("Série Escolar:"));
        cbSerieEscolar = new JComboBox<>();
        formPanel.add(cbSerieEscolar);

        mainPanel.add(formPanel, BorderLayout.NORTH);

        JTabbedPane tabbedPane = new JTabbedPane();
        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        tabbedPane.addTab("Alunos", criarPainelAlunos());
        tabbedPane.addTab("Disciplinas e Professores", criarPainelDisciplinasEProfessores());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.addActionListener(e -> salvarTurma());
        buttonPanel.add(btnSalvar);

        JButton btnLimpar = new JButton("Limpar");
        btnLimpar.addActionListener(e -> limparCampos());
        buttonPanel.add(btnLimpar);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    private JPanel criarPainelAlunos() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        modeloListaAlunosMatriculados = new DefaultListModel<>();
        listaAlunosMatriculados = new JList<>(modeloListaAlunosMatriculados);
        panel.add(new JScrollPane(listaAlunosMatriculados), BorderLayout.CENTER);

        JPanel controlPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        cbAlunosDisponiveis = new JComboBox<>();
        controlPanel.add(cbAlunosDisponiveis);

        btnMatricularAluno = new JButton("Matricular Aluno");
        btnMatricularAluno.addActionListener(e -> matricularAluno());
        controlPanel.add(btnMatricularAluno);

        btnDesmatricularAluno = new JButton("Desmatricular Aluno");
        btnDesmatricularAluno.addActionListener(e -> desmatricularAluno());
        controlPanel.add(btnDesmatricularAluno);

        panel.add(controlPanel, BorderLayout.EAST);
        return panel;
    }

    private JPanel criarPainelDisciplinasEProfessores() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        modeloListaAtribuicoes = new DefaultListModel<>();
        listaAtribuicoes = new JList<>(modeloListaAtribuicoes);
        panel.add(new JScrollPane(listaAtribuicoes), BorderLayout.CENTER);

        JPanel controlPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        cbDisciplinasDisponiveis = new JComboBox<>();
        controlPanel.add(cbDisciplinasDisponiveis);
        
        cbProfessoresDisponiveis = new JComboBox<>();
        controlPanel.add(cbProfessoresDisponiveis);

        btnAdicionarAtribuicao = new JButton("Adicionar Disciplina e Professor");
        btnAdicionarAtribuicao.addActionListener(e -> adicionarDisciplinaEProfessor());
        controlPanel.add(btnAdicionarAtribuicao);

        btnRemoverAtribuicao = new JButton("Remover Atribuição");
        btnRemoverAtribuicao.addActionListener(e -> removerAtribuicao());
        controlPanel.add(btnRemoverAtribuicao);

        panel.add(controlPanel, BorderLayout.EAST);
        return panel;
    }

    private void carregarNiveisEscolares() {
        cbNivelEscolar.removeAllItems();
        for (SerieEscolar.NivelEscolar nivel : SerieEscolar.NivelEscolar.values()) {
            cbNivelEscolar.addItem(nivel);
        }
        if (cbNivelEscolar.getItemCount() > 0) {
            cbNivelEscolar.setSelectedIndex(0);
        }
    }

    private void carregarSeriesEscolares() {
        cbSerieEscolar.removeAllItems();
        try {
            List<SerieEscolar> series = fachada.listarTodasSeriesEscolares();
            for (SerieEscolar serie : series) {
                cbSerieEscolar.addItem(serie);
            }
            if (cbSerieEscolar.getItemCount() > 0) {
                cbSerieEscolar.setSelectedIndex(0);
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar séries escolares: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void carregarTurmaParaEdicao(String codigoTurma) {
        try {
            this.turmaAtual = fachada.buscarTurma(codigoTurma);
            
            if (turmaAtual.getSerieEscolar() != null && turmaAtual.getSerieEscolar().getNivel() != null) {
                cbNivelEscolar.setSelectedItem(turmaAtual.getSerieEscolar().getNivel());
            }

            spnAnoLetivo.setValue(turmaAtual.getAnoLetivo());
            
            SerieEscolar serieDaTurma = turmaAtual.getSerieEscolar();
            if (serieDaTurma != null) {
                for (int i = 0; i < cbSerieEscolar.getItemCount(); i++) {
                    if (cbSerieEscolar.getItemAt(i).equals(serieDaTurma)) {
                        cbSerieEscolar.setSelectedIndex(i);
                        break;
                    }
                }
            }

            cbTurno.setSelectedItem(turmaAtual.getTurno());

            carregarAlunosMatriculados();
            carregarAlunosDisponiveis();
            carregarAtribuicoes();
            carregarDisciplinasDisponiveis();
            carregarProfessoresDisponiveis();
            setVisible(true);
        } catch (EntidadeNaoEncontradaException | DadoInvalidoException | IOException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar turma: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void salvarTurma() {
        SerieEscolar.NivelEscolar nivelSelecionado = (SerieEscolar.NivelEscolar) cbNivelEscolar.getSelectedItem();
        int anoLetivo = (int) spnAnoLetivo.getValue();
        SerieEscolar serieSelecionada = (SerieEscolar) cbSerieEscolar.getSelectedItem();
        String turnoSelecionado = (String) cbTurno.getSelectedItem();

        if (nivelSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um Nível Escolar.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (serieSelecionada == null) {
            JOptionPane.showMessageDialog(this, "Selecione uma Série Escolar.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (turnoSelecionado == null || turnoSelecionado.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione um Turno.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            String codigoGerado = nivelSelecionado.toString() + "-" +
                    serieSelecionada.getCodigoSerie() + "-" +
                    turnoSelecionado.substring(0,1).toUpperCase() + "-" +
                    anoLetivo;

            String nomeTurmaGerado = serieSelecionada.getNomeSerie() + " - " + turnoSelecionado + " - " + anoLetivo;

            if (turmaAtual == null) {
                Turma novaTurma = new Turma(codigoGerado, nomeTurmaGerado, anoLetivo, serieSelecionada, turnoSelecionado);
                fachada.adicionarTurma(novaTurma);
                JOptionPane.showMessageDialog(this, "Turma adicionada com sucesso! Código: " + codigoGerado, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                this.turmaAtual = novaTurma;
            } else {
                turmaAtual.setNomeTurma(nomeTurmaGerado);
                turmaAtual.setAnoLetivo(anoLetivo);
                turmaAtual.setSerieEscolar(serieSelecionada);
                turmaAtual.setTurno(turnoSelecionado);
                fachada.atualizarTurma(turmaAtual);
                JOptionPane.showMessageDialog(this, "Turma atualizada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            }
            carregarAlunosDisponiveis();
            carregarDisciplinasDisponiveis();
            carregarProfessoresDisponiveis();
            carregarAtribuicoes();
        } catch (DadoInvalidoException | EntidadeNaoEncontradaException | IOException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar turma: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limparCampos() {
        turmaAtual = null;
        cbNivelEscolar.setSelectedIndex(0);
        spnAnoLetivo.setValue(LocalDate.now().getYear());
        if (cbSerieEscolar.getItemCount() > 0) {
            cbSerieEscolar.setSelectedIndex(0);
        } else {
            cbSerieEscolar.removeAllItems();
        }
        cbTurno.setSelectedIndex(0);
        modeloListaAlunosMatriculados.clear();
        cbAlunosDisponiveis.removeAllItems();
        modeloListaAtribuicoes.clear();
        cbDisciplinasDisponiveis.removeAllItems();
        cbProfessoresDisponiveis.removeAllItems();
    }

    private void carregarAlunosMatriculados() {
        modeloListaAlunosMatriculados.clear();
        if (turmaAtual != null) {
            try {
                fachada.buscarTurma(turmaAtual.getCodigo()).getAlunosMatriculados().forEach(modeloListaAlunosMatriculados::addElement);
            } catch (EntidadeNaoEncontradaException | DadoInvalidoException | IOException e) {
                JOptionPane.showMessageDialog(this, "Erro ao carregar alunos matriculados: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void carregarAlunosDisponiveis() {
        cbAlunosDisponiveis.removeAllItems();
        try {
            List<Aluno> todosAlunos = fachada.listarTodosAlunos();
            List<Aluno> alunosMatriculados = new ArrayList<>();
            if (turmaAtual != null) {
                alunosMatriculados = fachada.buscarTurma(turmaAtual.getCodigo()).getAlunosMatriculados();
            }
            
            for (Aluno aluno : todosAlunos) {
                boolean jaMatriculado = alunosMatriculados.stream().anyMatch(matriculado -> matriculado.getMatricula().equals(aluno.getMatricula()));
                if (!jaMatriculado) {
                    cbAlunosDisponiveis.addItem(aluno);
                }
            }
        } catch (IOException | EntidadeNaoEncontradaException | DadoInvalidoException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao listar alunos: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void matricularAluno() {
        if (turmaAtual == null || turmaAtual.getCodigo() == null) {
            JOptionPane.showMessageDialog(this, "Crie ou selecione uma turma primeiro.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Aluno alunoSelecionado = (Aluno) cbAlunosDisponiveis.getSelectedItem();
        if (alunoSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um aluno para matricular.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            fachada.matricularAlunoNaTurma(turmaAtual.getCodigo(), alunoSelecionado.getMatricula());
            JOptionPane.showMessageDialog(this, "Aluno matriculado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            turmaAtual = fachada.buscarTurma(turmaAtual.getCodigo());
            carregarAlunosMatriculados();
            carregarAlunosDisponiveis();
        } catch (DadoInvalidoException | EntidadeNaoEncontradaException | IOException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao matricular aluno: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void desmatricularAluno() {
        if (turmaAtual == null || turmaAtual.getCodigo() == null) {
            JOptionPane.showMessageDialog(this, "Crie ou selecione uma turma primeiro.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Aluno alunoSelecionado = listaAlunosMatriculados.getSelectedValue();
        if (alunoSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um aluno para desmatricular.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            fachada.desmatricularAlunoDaTurma(turmaAtual.getCodigo(), alunoSelecionado.getMatricula());
            JOptionPane.showMessageDialog(this, "Aluno desmatriculado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            turmaAtual = fachada.buscarTurma(turmaAtual.getCodigo());
            carregarAlunosMatriculados();
            carregarAlunosDisponiveis();
        } catch (DadoInvalidoException | EntidadeNaoEncontradaException | IOException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao desmatricular aluno: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarAtribuicoes() {
        modeloListaAtribuicoes.clear();
        if (turmaAtual != null) {
            try {
                List<DisciplinaTurma> atribuicoes = fachada.buscarAtribuicoesPorTurma(turmaAtual.getCodigo());
                atribuicoes.forEach(modeloListaAtribuicoes::addElement);
            } catch (EntidadeNaoEncontradaException | DadoInvalidoException | IOException e) {
                JOptionPane.showMessageDialog(this, "Erro ao carregar atribuições: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void carregarDisciplinasDisponiveis() {
        cbDisciplinasDisponiveis.removeAllItems();
        try {
            List<Disciplina> todasDisciplinas = fachada.listarTodasDisciplinas();
            List<Disciplina> disciplinasAssociadas = new ArrayList<>();
            if (turmaAtual != null) {
                disciplinasAssociadas = fachada.buscarDisciplinasPorTurma(turmaAtual.getCodigo());
            }

            for (Disciplina disciplina : todasDisciplinas) {
                if (!disciplinasAssociadas.contains(disciplina)) {
                    cbDisciplinasDisponiveis.addItem(disciplina);
                }
            }
        } catch (IOException | EntidadeNaoEncontradaException | DadoInvalidoException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao listar disciplinas: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarProfessoresDisponiveis() {
        cbProfessoresDisponiveis.removeAllItems();
        try {
            List<Professor> todosProfessores = fachada.listarTodosProfessores();
            List<Professor> professoresAssociados = new ArrayList<>();
            if (turmaAtual != null) {
                professoresAssociados = fachada.buscarProfessoresPorTurma(turmaAtual.getCodigo());
            }
            
            for (Professor professor : todosProfessores) {
                if (!professoresAssociados.contains(professor)) {
                    cbProfessoresDisponiveis.addItem(professor);
                }
            }
        } catch (IOException | EntidadeNaoEncontradaException | DadoInvalidoException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao listar professores: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void adicionarDisciplinaEProfessor() {
        if (turmaAtual == null || turmaAtual.getCodigo() == null) {
            JOptionPane.showMessageDialog(this, "Crie ou selecione uma turma primeiro.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Disciplina disciplinaSelecionada = (Disciplina) cbDisciplinasDisponiveis.getSelectedItem();
        Professor professorSelecionado = (Professor) cbProfessoresDisponiveis.getSelectedItem();

        if (disciplinaSelecionada == null || professorSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione uma disciplina e um professor.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            fachada.adicionarDisciplinaComProfessorNaTurma(turmaAtual.getCodigo(), disciplinaSelecionada.getCodigo(), professorSelecionado.getRegistroFuncional());
            JOptionPane.showMessageDialog(this, "Disciplina e professor atribuídos com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            carregarAtribuicoes();
            carregarDisciplinasDisponiveis();
            carregarProfessoresDisponiveis();
        } catch (DadoInvalidoException | EntidadeNaoEncontradaException | IOException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao atribuir professor à disciplina: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removerAtribuicao() {
        if (turmaAtual == null || turmaAtual.getCodigo() == null) {
            JOptionPane.showMessageDialog(this, "Crie ou selecione uma turma primeiro.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        DisciplinaTurma atribuicaoSelecionada = listaAtribuicoes.getSelectedValue();
        if (atribuicaoSelecionada == null) {
            JOptionPane.showMessageDialog(this, "Selecione uma atribuição para remover.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            if (atribuicaoSelecionada.getProfessor() != null) {
                fachada.removerAtribuicaoDisciplinaProfessorTurma(
                    atribuicaoSelecionada.getDisciplina().getCodigo(),
                    atribuicaoSelecionada.getProfessor().getCpf(),
                    turmaAtual.getCodigo()
                );
                JOptionPane.showMessageDialog(this, "Atribuição removida com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } else {
                fachada.removerDisciplinaDaTurma(turmaAtual.getCodigo(), atribuicaoSelecionada.getDisciplina().getCodigo());
                JOptionPane.showMessageDialog(this, "Disciplina removida da turma com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            }
            
            carregarAtribuicoes();
            carregarDisciplinasDisponiveis();
            carregarProfessoresDisponiveis();
        } catch (DadoInvalidoException | EntidadeNaoEncontradaException | IOException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao remover a atribuição: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}