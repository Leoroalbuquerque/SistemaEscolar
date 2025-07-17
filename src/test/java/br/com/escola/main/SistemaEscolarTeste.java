package br.com.escola.main;

import br.com.escola.negocio.Fachada;
import br.com.escola.negocio.Aluno;
import br.com.escola.negocio.Professor;
import br.com.escola.negocio.Disciplina;
import br.com.escola.negocio.Turma;
import br.com.escola.negocio.Funcionario;
import br.com.escola.negocio.Responsavel;
import br.com.escola.excecoes.DadoInvalidoException;
import br.com.escola.excecoes.EntidadeNaoEncontradaException;
import java.util.Arrays;
import java.util.List;

public class SistemaEscolarTeste {

    public static void main(String[] args) {
        Fachada fachada = Fachada.getInstance();

        System.out.println("\n--- Testando CRUD de Alunos ---");
        try {
            fachada.limparTodosOsDados();

            Aluno aluno1 = new Aluno("Joao", "123.456.789-00", "99999-8888", "joao@email.com", "2023001", 2023);
            Aluno aluno2 = new Aluno("Maria", "987.654.321-11", "98888-7777", "maria@email.com", "2023002", 2023);

            fachada.adicionarAluno(aluno1);
            fachada.adicionarAluno(aluno2);
            System.out.println("Alunos adicionados com sucesso.");

            System.out.println("\n--- Lista de Alunos ---");
            List<Aluno> alunos = fachada.listarTodosAlunos();
            alunos.forEach(System.out::println);

            Aluno alunoEncontrado = fachada.buscarAluno("2023001");
            System.out.println("Aluno encontrado (2023001): " + alunoEncontrado.getNome());

            alunoEncontrado.setEmail("joao.novo@email.com");
            fachada.atualizarAluno(alunoEncontrado);
            System.out.println("Aluno 2023001 atualizado para: " + fachada.buscarAluno("2023001").getEmail());

            fachada.deletarAluno("2023002");
            System.out.println("Aluno 2023002 deletado com sucesso.");

            System.out.println("\n--- Lista de Alunos após deleção ---");
            alunos = fachada.listarTodosAlunos();
            if (alunos.isEmpty()) {
                System.out.println("Nenhum aluno restante.");
            } else {
                alunos.forEach(System.out::println);
            }

            try {
                fachada.adicionarAluno(new Aluno("Joao Duplicado", "123.456.789-00", "99999-8888", "joao@email.com", "2023001", 2023));
            } catch (DadoInvalidoException e) {
                System.out.println("Erro esperado ao adicionar aluno duplicado: " + e.getMessage());
            }

            try {
                fachada.buscarAluno("INEXISTENTE");
            } catch (EntidadeNaoEncontradaException e) {
                System.out.println("Erro esperado ao buscar aluno inexistente: " + e.getMessage());
            }

        } catch (DadoInvalidoException | EntidadeNaoEncontradaException e) {
            System.err.println("Erro durante o teste de Aluno: " + e.getMessage());
        }

        System.out.println("\n--- Testando CRUD de Professores ---");
        try {
            fachada.limparTodosOsDados();

            Professor prof1 = new Professor("Carlos Lima", "111.222.333-44", "97777-6666", "carlos.l@escola.com", "PRO101", "Matemática", 5000.00);
            Professor prof2 = new Professor("Ana Souza", "222.333.444-55", "96666-5555", "ana.s@escola.com", "PRO102", "Português", 5200.00);

            fachada.adicionarProfessor(prof1);
            fachada.adicionarProfessor(prof2);
            System.out.println("Professores adicionados com sucesso.");

            System.out.println("\n--- Lista de Professores ---");
            List<Professor> professores = fachada.listarTodosProfessores();
            professores.forEach(System.out::println);

            Professor profEncontrado = fachada.buscarProfessor("PRO101");
            System.out.println("Professor encontrado (PRO101): " + profEncontrado.getNome());

            profEncontrado.setEspecialidade("Matemática Avançada");
            fachada.atualizarProfessor(profEncontrado);
            System.out.println("Professor PRO101 atualizado para: " + fachada.buscarProfessor("PRO101").getEspecialidade());

            System.out.println("\n--- Buscando Professores por Especialidade (Português) ---");
            List<Professor> profsPortugues = fachada.buscarProfessoresPorEspecialidade("Português");
            profsPortugues.forEach(System.out::println);

            fachada.deletarProfessor("PRO102");
            System.out.println("Professor PRO102 deletado com sucesso.");

            System.out.println("\n--- Lista de Professores após deleção ---");
            professores = fachada.listarTodosProfessores();
            if (professores.isEmpty()) {
                System.out.println("Nenhum professor restante.");
            } else {
                professores.forEach(System.out::println);
            }

            try {
                fachada.adicionarProfessor(new Professor("Carlos Duplicado", "111.222.333-44", "97777-6666", "carlos.l@escola.com", "PRO101", "Física", 5100.00));
            } catch (DadoInvalidoException e) {
                System.out.println("Erro esperado ao adicionar professor duplicado: " + e.getMessage());
            }

            try {
                fachada.buscarProfessor("INEXISTENTE");
            } catch (EntidadeNaoEncontradaException e) {
                System.out.println("Erro esperado ao buscar professor inexistente: " + e.getMessage());
            }

        } catch (DadoInvalidoException | EntidadeNaoEncontradaException e) {
            System.err.println("Erro durante o teste de Professor: " + e.getMessage());
        }

        System.out.println("\n--- Testando CRUD de Disciplinas ---");
        try {
            fachada.limparTodosOsDados();

            Disciplina disc1 = new Disciplina("MAT001", "Matemática Fund.", 60);
            Disciplina disc2 = new Disciplina("POR001", "Português Avançado", 80);

            fachada.adicionarDisciplina(disc1);
            fachada.adicionarDisciplina(disc2);
            System.out.println("Disciplinas adicionadas com sucesso.");

            System.out.println("\n--- Lista de Disciplinas ---");
            List<Disciplina> disciplinas = fachada.listarTodasDisciplinas();
            disciplinas.forEach(System.out::println);

            Disciplina discEncontrada = fachada.buscarDisciplina("MAT001");
            System.out.println("Disciplina encontrada (MAT001): " + discEncontrada.getNome());

            discEncontrada.setCargaHoraria(70);
            fachada.atualizarDisciplina(discEncontrada);
            System.out.println("Disciplina MAT001 atualizada para carga horária: " + fachada.buscarDisciplina("MAT001").getCargaHoraria());

            fachada.deletarDisciplina("POR001");
            System.out.println("Disciplina POR001 deletada com sucesso.");

            System.out.println("\n--- Lista de Disciplinas após deleção ---");
            disciplinas = fachada.listarTodasDisciplinas();
            if (disciplinas.isEmpty()) {
                System.out.println("Nenhuma disciplina restante.");
            } else {
                disciplinas.forEach(System.out::println);
            }

            try {
                fachada.adicionarDisciplina(new Disciplina("MAT001", "Matemática Duplicada", 65));
            } catch (DadoInvalidoException e) {
                System.out.println("Erro esperado ao adicionar disciplina duplicada: " + e.getMessage());
            }

            try {
                fachada.buscarDisciplina("INEXISTENTE");
            } catch (EntidadeNaoEncontradaException e) {
                System.out.println("Erro esperado ao buscar disciplina inexistente: " + e.getMessage());
            }

        } catch (DadoInvalidoException | EntidadeNaoEncontradaException e) {
            System.err.println("Erro durante o teste de Disciplina: " + e.getMessage());
        }

        System.out.println("\n--- Testando CRUD de Turmas ---");
        try {
            fachada.limparTodosOsDados();

            Professor profParaTurma = new Professor("Carlos Lima", "111.222.333-44", "97777-6666", "carlos.l@escola.com", "PRO101", "Matemática", 5000.00);
            fachada.adicionarProfessor(profParaTurma);
            
            Disciplina discParaTurma = new Disciplina("MAT001", "Matemática Fund.", 60);
            fachada.adicionarDisciplina(discParaTurma);
            
            Aluno alunoParaTurma = new Aluno("Joao", "123.456.789-00", "99999-8888", "joao@email.com", "2023001", 2023);
            fachada.adicionarAluno(alunoParaTurma);
            
            Turma turma1 = new Turma("T2024A", "Turma A - 2024", 2024, profParaTurma);

            fachada.adicionarTurma(turma1);
            System.out.println("Turma adicionada com sucesso.");

            System.out.println("\n--- Lista de Turmas ---");
            List<Turma> turmas = fachada.listarTodasTurmas();
            turmas.forEach(System.out::println);

            Turma turmaEncontrada = fachada.buscarTurma("T2024A");
            System.out.println("Turma encontrada (T2024A): " + turmaEncontrada.getNomeTurma());

            fachada.adicionarDisciplinaNaTurma("T2024A", discParaTurma.getCodigo());
            System.out.println("Disciplina MAT001 adicionada à Turma T2024A.");

            fachada.matricularAlunoNaTurma("T2024A", alunoParaTurma.getMatricula());
            System.out.println("Aluno 2023001 matriculado na Turma T2024A.");
            
            System.out.println("Turma T2024A após associações: " + fachada.buscarTurma("T2024A"));

            turmaEncontrada.setAnoLetivo(2025);
            fachada.atualizarTurma(turmaEncontrada);
            System.out.println("Turma T2024A atualizada para ano letivo: " + fachada.buscarTurma("T2024A").getAnoLetivo());

            fachada.removerDisciplinaDaTurma("T2024A", discParaTurma.getCodigo());
            System.out.println("Disciplina MAT001 removida da Turma T2024A.");

            fachada.desmatricularAlunoDaTurma("T2024A", alunoParaTurma.getMatricula());
            System.out.println("Aluno 2023001 desmatriculado da Turma T2024A.");
            
            System.out.println("Turma T2024A após desassociações: " + fachada.buscarTurma("T2024A"));

            fachada.deletarTurma("T2024A");
            System.out.println("Turma T2024A deletada com sucesso.");

            System.out.println("\n--- Lista de Turmas após deleção ---");
            turmas = fachada.listarTodasTurmas();
            if (turmas.isEmpty()) {
                System.out.println("Nenhuma turma restante.");
            } else {
                turmas.forEach(System.out::println);
            }

            try {
                fachada.adicionarTurma(new Turma("T2024A", "Turma Duplicada", 2024, profParaTurma));
            } catch (DadoInvalidoException e) {
                System.out.println("Erro esperado ao adicionar turma duplicada: " + e.getMessage());
            }

            try {
                fachada.buscarTurma("INEXISTENTE");
            } catch (EntidadeNaoEncontradaException e) {
                System.out.println("Erro esperado ao buscar turma inexistente: " + e.getMessage());
            }

        } catch (DadoInvalidoException | EntidadeNaoEncontradaException e) {
            System.err.println("Erro durante o teste de Turma: " + e.getMessage());
        }

        System.out.println("\n--- Testando CRUD de Funcionários ---");
        try {
            fachada.limparTodosOsDados();

            Funcionario func1 = new Funcionario("Maria Silva", "789.012.345-67", "98765-4321", "maria.s@escola.com", "Secretária", "SEC0001", 3000.00);
            Funcionario func2 = new Funcionario("João Santos", "123.456.789-01", "12345-6789", "joao.s@escola.com", "Administrador", "ADM0001", 4500.00);
            Funcionario func3 = new Funcionario("Pedro Mendes", "456.789.012-34", "99887-7665", "pedro.m@escola.com", "Zelador", "ZEL0001", 2000.00);

            fachada.adicionarFuncionario(func1);
            fachada.adicionarFuncionario(func2);
            fachada.adicionarFuncionario(func3);
            System.out.println("Funcionários adicionados com sucesso.");

            System.out.println("\n--- Lista de Funcionários ---");
            List<Funcionario> funcionarios = fachada.listarTodosFuncionarios();
            funcionarios.forEach(System.out::println);

            Funcionario funcEncontrado = fachada.buscarFuncionario("SEC0001");
            System.out.println("Funcionário encontrado (SEC0001): " + funcEncontrado.getNome());

            funcEncontrado.setSalario(3200.00);
            funcEncontrado.setEmail("maria.silva@escola.com");
            fachada.atualizarFuncionario(funcEncontrado);
            System.out.println("Funcionário SEC0001 atualizado para: " + fachada.buscarFuncionario("SEC0001").getSalario() + " e " + fachada.buscarFuncionario("SEC0001").getEmail());

            System.out.println("\n--- Buscando Funcionários por Cargo (Administrador) ---");
            List<Funcionario> admins = fachada.buscarFuncionariosPorCargo("Administrador");
            admins.forEach(System.out::println);

            boolean deletado = fachada.deletarFuncionario("ZEL0001");
            if (deletado) {
                System.out.println("Funcionário ZEL0001 deletado com sucesso.");
            } else {
                System.out.println("Falha ao deletar Funcionário ZEL0001.");
            }

            System.out.println("\n--- Lista de Funcionários após deleção ---");
            funcionarios = fachada.listarTodosFuncionarios();
            if (funcionarios.isEmpty()) {
                System.out.println("Nenhum funcionário restante.");
            } else {
                funcionarios.forEach(System.out::println);
            }

            try {
                fachada.adicionarFuncionario(new Funcionario("Outra Maria", "111.222.333-44", "11111-2222", "outra@escola.com", "Secretária", "SEC0001", 2900.00));
            } catch (DadoInvalidoException e) {
                System.out.println("Erro esperado ao adicionar funcionário duplicado: " + e.getMessage());
            }

            try {
                fachada.buscarFuncionario("INEXISTENTE");
            } catch (EntidadeNaoEncontradaException e) {
                System.out.println("Erro esperado ao buscar funcionário inexistente: " + e.getMessage());
            }

        } catch (DadoInvalidoException | EntidadeNaoEncontradaException e) {
            System.err.println("Erro durante o teste de Funcionário: " + e.getMessage());
        }
        System.out.println("--- Fim do Teste de Funcionários ---\n");

        System.out.println("\n--- Testando CRUD de Responsáveis e Associação com Alunos ---");
        try {
            fachada.limparTodosOsDados();

            Responsavel resp1 = new Responsavel("Carlos Pai", "111.111.111-11", "91111-1111", "carlos.pai@email.com", "Pai", "111.111.111-11", true);
            Responsavel resp2 = new Responsavel("Ana Mae", "222.222.222-22", "92222-2222", "ana.mae@email.com", "Mãe", "222.222.222-22", true);
            Responsavel resp3 = new Responsavel("Joana Tia", "333.333.333-33", "93333-3333", "joana.tia@email.com", "Tia", "333.333.333-33", false);

            fachada.adicionarResponsavel(resp1);
            fachada.adicionarResponsavel(resp2);
            fachada.adicionarResponsavel(resp3);
            System.out.println("Responsáveis adicionados com sucesso.");

            System.out.println("\n--- Lista de Responsáveis ---");
            List<Responsavel> responsaveis = fachada.listarTodosResponsaveis();
            responsaveis.forEach(System.out::println);

            System.out.println("\n--- Buscando Responsável ---");
            Responsavel respEncontrado = fachada.buscarResponsavel("111.111.111-11");
            System.out.println("Responsável encontrado (111.111.111-11): " + respEncontrado.getNome());

            System.out.println("\n--- Atualizando Responsável ---");
            respEncontrado.setTelefone("91111-0000");
            fachada.atualizarResponsavel(respEncontrado);
            System.out.println("Responsável 111.111.111-11 atualizado para telefone: " + fachada.buscarResponsavel("111.111.111-11").getTelefone());

            System.out.println("\n--- Buscando Responsáveis Principais ---");
            List<Responsavel> responsaveisPrincipais = fachada.buscarResponsaveisPrincipais();
            responsaveisPrincipais.forEach(System.out::println);

            System.out.println("\n--- Associando Responsáveis a Aluno ---");
            Aluno alunoJoaoParaResp = new Aluno("Joao Responsavel", "111.111.111-11", "99999-8888", "joao.resp@email.com", "2023001", 2023);
            fachada.adicionarAluno(alunoJoaoParaResp);

            System.out.println("Aluno antes da associação: " + alunoJoaoParaResp);
            
            fachada.adicionarResponsavelAoAluno(alunoJoaoParaResp.getMatricula(), "111.111.111-11");
            fachada.adicionarResponsavelAoAluno(alunoJoaoParaResp.getMatricula(), "222.222.222-22");
            System.out.println("Responsáveis 111.111.111-11 e 222.222.222-22 associados ao aluno 2023001.");
            
            Aluno alunoJoaoAtualizado = fachada.buscarAluno("2023001");
            System.out.println("Aluno após associação: " + alunoJoaoAtualizado);

            System.out.println("\n--- Removendo Responsável de Aluno ---");
            fachada.removerResponsavelDoAluno(alunoJoaoAtualizado.getMatricula(), "111.111.111-11");
            System.out.println("Responsável 111.111.111-11 removido do aluno 2023001.");

            Aluno alunoJoaoFinal = fachada.buscarAluno("2023001");
            System.out.println("Aluno após remoção: " + alunoJoaoFinal);

            System.out.println("\n--- Deletando Responsável ---");
            boolean respDeletado = fachada.deletarResponsavel("333.333.333-33");
            if (respDeletado) {
                System.out.println("Responsável 333.333.333-33 deletado com sucesso.");
            } else {
                System.out.println("Falha ao deletar Responsável 333.333.333-33.");
            }

            System.out.println("\n--- Lista de Responsáveis após deleção ---");
            responsaveis = fachada.listarTodosResponsaveis();
            if (responsaveis.isEmpty()) {
                System.out.println("Nenhum responsável restante.");
            } else {
                responsaveis.forEach(System.out::println);
            }

            System.out.println("\n--- Tentando adicionar responsável com CPF duplicado ---");
            try {
                fachada.adicionarResponsavel(new Responsavel("Carlos Duplicado", "111.111.111-11", "91111-1111", "carlos.duplicado@email.com", "Pai", "111.111.111-11", true));
            } catch (DadoInvalidoException e) {
                System.out.println("Erro esperado ao adicionar responsável duplicado: " + e.getMessage());
            }

            System.out.println("\n--- Tentando associar responsável inexistente a aluno ---");
            try {
                fachada.adicionarResponsavelAoAluno(alunoJoaoAtualizado.getMatricula(), "999.999.999-99");
            } catch (EntidadeNaoEncontradaException e) {
                System.out.println("Erro esperado ao associar responsável inexistente: " + e.getMessage());
            }

            System.out.println("\n--- Tentando remover responsável não associado de aluno ---");
            try {
                fachada.removerResponsavelDoAluno(alunoJoaoAtualizado.getMatricula(), "111.111.111-11");
            } catch (EntidadeNaoEncontradaException e) {
                System.out.println("Erro esperado ao remover responsável não associado: " + e.getMessage());
            }


        } catch (DadoInvalidoException | EntidadeNaoEncontradaException e) {
            System.err.println("Erro durante o teste de Responsável: " + e.getMessage());
        }
        System.out.println("--- Fim do Teste de Responsáveis ---\n");
    }
}