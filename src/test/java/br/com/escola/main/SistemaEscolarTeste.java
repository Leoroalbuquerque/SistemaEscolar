package br.com.escola.main;

import br.com.escola.excecoes.DadoInvalidoException;
import br.com.escola.excecoes.EntidadeNaoEncontradaException;
import br.com.escola.negocio.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SistemaEscolarTeste {

    private static final Logger LOGGER = Logger.getLogger(SistemaEscolarTeste.class.getName());

    public static void main(String[] args) {
        Fachada fachada = Fachada.getInstance();

        try {
            LOGGER.info("Disciplinas carregadas do arquivo: disciplinas.json");

            LOGGER.info("Iniciando testes do Sistema Escolar...");

            LOGGER.info("--- Limpando repositórios ---");
            fachada.limparTodosRepositorios();
            LOGGER.log(Level.FINE, "Arquivo professores.json limpo.");
            LOGGER.info("Disciplinas salvas no arquivo: disciplinas.json");
            LOGGER.log(Level.FINE, "Arquivo disciplinas.json limpo.");
            LOGGER.log(Level.FINE, "Funcionários salvos no arquivo: funcionarios.json");
            LOGGER.log(Level.FINE, "Arquivo funcionarios.json limpo.");
            LOGGER.log(Level.FINE, "Arquivo responsaveis.json limpo.");
            LOGGER.log(Level.FINE, "Arquivo turmas.json limpo.");

            LOGGER.info("--- Adicionando Responsáveis ---");
            Responsavel resp1 = new Responsavel("Joana Silva", "111.111.111-11", "9999-0000", "joana@email.com", "Mãe", "111.111.111-11", true);
            Responsavel resp2 = new Responsavel("Carlos Souza", "222.222.222-22", "8888-1111", "carlos@email.com", "Pai", "222.222.222-22", false);
            fachada.adicionarResponsavel(resp1);
            fachada.adicionarResponsavel(resp2);
            LOGGER.info("Responsáveis adicionados.");
            
            LOGGER.info("--- Adicionando Alunos ---");
            Aluno aluno1 = new Aluno("Maria Eduarda", "555.555.555-55", "9999-0001", "maria.eduarda@email.com", 
                                     "2025001", LocalDate.of(2010, 3, 15), 5, Arrays.asList(resp1.getCpfResponsavel()));
            
            Aluno aluno2 = new Aluno("João Pedro", "666.666.666-66", "9999-0002", "joao.pedro@email.com", 
                                     "2025002", LocalDate.of(2009, 7, 20), 6, Arrays.asList(resp2.getCpfResponsavel()));
            
            fachada.adicionarAluno(aluno1);
            fachada.adicionarAluno(aluno2);
            LOGGER.info("Alunos adicionados e responsáveis associados.");

            LOGGER.info("--- Adicionando Professores ---");
            Professor prof1 = new Professor("Prof. Ricardo Lima", "123.456.789-01", "9876-5432", "ricardo.lima@escola.com", "POR001", "Matemática", 4000.00);
            Professor prof2 = new Professor("Profa. Laura Mendes", "987.654.321-09", "1234-5678", "laura.mendes@escola.com", "MAT002", "Português", 4200.00);
            fachada.adicionarProfessor(prof1);
            fachada.adicionarProfessor(prof2);
            LOGGER.info("Professores adicionados.");
            
            LOGGER.info("--- Adicionando Disciplinas ---");
            Disciplina disc1 = new Disciplina("MAT01", "Matemática Avançada", 60);
            Disciplina disc2 = new Disciplina("POR01", "Português e Redação", 60);
            fachada.adicionarDisciplina(disc1);
            fachada.adicionarDisciplina(disc2);
            LOGGER.info("Disciplinas adicionadas.");

            LOGGER.info("--- Adicionando Funcionários ---");
            Funcionario func1 = new Funcionario("Pedro Álvares", "555.555.555-55", "5555-4444", "pedro@escola.com", "Secretário", "SEC0001", 3000.00);
            fachada.adicionarFuncionario(func1);
            LOGGER.info("Funcionários adicionados.");
            
            LOGGER.info("--- Adicionando Turmas ---");
            Turma turma1 = new Turma("TMA2025-01", "Manhã 2025", 2025, prof1);
            Turma turma2 = new Turma("TMA2025-02", "Tarde 2025", 2025, prof2);
            fachada.adicionarTurma(turma1);
            fachada.adicionarTurma(turma2);
            LOGGER.info("Turmas adicionadas.");

            LOGGER.info("--- Matrículas e Associações na Turma ---");
            fachada.matricularAlunoNaTurma(turma1.getCodigo(), aluno1.getMatricula());
            fachada.matricularAlunoNaTurma(turma1.getCodigo(), aluno2.getMatricula());
            fachada.adicionarDisciplinaNaTurma(turma1.getCodigo(), disc1.getCodigo());
            fachada.adicionarDisciplinaNaTurma(turma1.getCodigo(), disc2.getCodigo());
            LOGGER.info("Alunos matriculados e disciplinas adicionadas às turmas.");

            LOGGER.info("--- Testando operações de busca ---");
            Aluno alunoEncontrado = fachada.buscarAluno(aluno1.getMatricula());
            LOGGER.info("Aluno encontrado: " + alunoEncontrado.getNome() + ", Data Nasc: " + alunoEncontrado.getDataNascimento());
            Professor professorEncontrado = fachada.buscarProfessor(prof1.getRegistroFuncional());
            LOGGER.info("Professor encontrado: " + professorEncontrado.getNome());
            Disciplina disciplinaEncontrada = fachada.buscarDisciplina(disc1.getCodigo());
            LOGGER.info("Disciplina encontrada: " + disciplinaEncontrada.getNome());
            Turma turmaEncontrada = fachada.buscarTurma(turma1.getCodigo());
            LOGGER.info("Turma encontrada: " + turmaEncontrada.getCodigo());
            Funcionario funcionarioEncontrado = fachada.buscarFuncionario(func1.getMatriculaFuncional());
            LOGGER.info("Funcionário encontrado: " + funcionarioEncontrado.getNome());
            Responsavel responsavelEncontrado = fachada.buscarResponsavel(resp1.getCpfResponsavel());
            LOGGER.info("Responsável encontrado: " + responsavelEncontrado.getNome());

            LOGGER.info("--- Testando operações de atualização ---");
            aluno1.setAnoLetivo(6);
            aluno1.setDataNascimento(LocalDate.of(2010, 3, 20));
            fachada.atualizarAluno(aluno1);
            LOGGER.info("Aluno " + aluno1.getNome() + " atualizado para o " + aluno1.getAnoLetivo() + "º ano e nova data de nascimento: " + aluno1.getDataNascimento());

            prof1.setSalario(4500.00);
            fachada.atualizarProfessor(prof1);
            LOGGER.info("Salário do professor " + prof1.getNome() + " atualizado para R$ " + prof1.getSalario());

            LOGGER.info("--- Testando Avaliações ---");
            fachada.criarAvaliacao("AVA001", "Prova de Matemática", LocalDate.of(2025, 11, 10), LocalDate.of(2025, 11, 10), disc1);
            Avaliacao avaliacao = fachada.consultarAvaliacao("AVA001");
            LOGGER.info("Avaliação criada: " + avaliacao.getNomeAvaliacao());

            LOGGER.info("--- Testando Ocorrências ---");
            fachada.adicionarOcorrencia("OCOR001", LocalDateTime.now(), "Atraso na entrada", func1.getMatriculaFuncional(), aluno1);
            Ocorrencia ocorrencia = fachada.buscarOcorrencia("OCOR001");
            fachada.registrarMedidasOcorrencia("OCOR001", "Advertência verbal");
            LOGGER.info("Ocorrência registrada para " + ocorrencia.getAluno().getNome() + ": " + ocorrencia.getDescricao());

            LOGGER.info("--- Testando Calendário Escolar ---");
            fachada.adicionarCalendario(2025);
            fachada.adicionarDataLetivaCalendario(2025, LocalDate.of(2025, 8, 1));
            fachada.registrarAvaliacaoNoCalendario(2025, avaliacao.getId());
            CalendarioEscolar calendario = fachada.buscarCalendario(2025);
            LOGGER.info("Calendário 2025 criado e data letiva adicionada. Avaliação registrada.");

            LOGGER.info("--- Testando exclusão ---");
            fachada.desmatricularAlunoDaTurma(turma1.getCodigo(), aluno2.getMatricula());
            LOGGER.info("Aluno " + aluno2.getNome() + " desmatriculado da turma " + turma1.getCodigo());
            fachada.deletarAluno(aluno2.getMatricula());
            LOGGER.info("Aluno " + aluno2.getNome() + " excluído.");

        } catch (DadoInvalidoException | EntidadeNaoEncontradaException | IOException e) {
            LOGGER.log(Level.SEVERE, "Erro durante o teste: " + e.getMessage(), e);
        } finally {
            LOGGER.info("Testes do Sistema Escolar concluídos.");
        }
    }
}