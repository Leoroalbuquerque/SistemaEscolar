package br.com.escola.main;

import br.com.escola.negocio.Aluno;
import br.com.escola.negocio.AlunoServico;
import br.com.escola.negocio.Pessoa;
import br.com.escola.negocio.PessoaFactory;
import br.com.escola.negocio.TipoPessoa;
import br.com.escola.excecoes.DadoInvalidoException;
import br.com.escola.excecoes.EntidadeNaoEncontradaException;
import br.com.escola.fachada.EscolaFachada;

import java.util.List;

public class SistemaEscolarTeste {

    public static void main(String[] args) {
        System.out.println("### INICIANDO TESTES CRUD DO SISTEMA ESCOLAR COM TRATAMENTO DE EXCEÇÕES E DUPLICIDADE ###\n");

        EscolaFachada fachada = EscolaFachada.getInstance();
        AlunoServico alunoServico = fachada.getAlunoServico();

        System.out.println("--- Teste de Criação de Aluno (Sucesso) ---");
        Pessoa pessoaAluno = PessoaFactory.criarPessoa(TipoPessoa.ALUNO);
        if (pessoaAluno instanceof Aluno aluno1) {
            aluno1.setNome("Maria Silva");
            aluno1.setCpf("111.222.333-44");
            aluno1.setMatricula("2025001");
            aluno1.setAnoLetivo(2025);

            try {
                alunoServico.criarAluno(aluno1);
                System.out.println("Aluno criado: " + aluno1.getNome() + " (Matrícula: " + aluno1.getMatricula() + ")\n");
            } catch (DadoInvalidoException e) {
                System.out.println("Erro inesperado ao criar aluno existente: " + e.getMessage() + "\n");
            }
        } else {
            System.out.println("Erro: A factory não criou um objeto Aluno.\n");
        }

        System.out.println("--- Teste de Criação de Aluno (Falha - Dado Inválido Esperado) ---");
        Pessoa pessoaAlunoInvalido = PessoaFactory.criarPessoa(TipoPessoa.ALUNO);
        if (pessoaAlunoInvalido instanceof Aluno alunoInvalido) {
            alunoInvalido.setNome("");
            alunoInvalido.setCpf("000.000.000-00");
            alunoInvalido.setMatricula("2025003");
            alunoInvalido.setAnoLetivo(2025);

            try {
                alunoServico.criarAluno(alunoInvalido);
                System.out.println("Erro: Aluno criado com dados inválidos.\n");
            } catch (DadoInvalidoException e) {
                System.out.println("Sucesso! Capturada a exceção esperada ao criar aluno: " + e.getMessage() + "\n");
            }
        }

        System.out.println("--- Teste de Criação de Aluno (Falha - Matrícula Duplicada Esperada) ---");
        Pessoa pessoaAlunoDuplicado = PessoaFactory.criarPessoa(TipoPessoa.ALUNO);
        if (pessoaAlunoDuplicado instanceof Aluno alunoDuplicado) {
            alunoDuplicado.setNome("Pedro Duplicado");
            alunoDuplicado.setCpf("999.888.777-66");
            alunoDuplicado.setMatricula("2025001");
            alunoDuplicado.setAnoLetivo(2025);

            try {
                alunoServico.criarAluno(alunoDuplicado);
                System.out.println("Erro: Aluno com matrícula duplicada criado.\n");
            } catch (DadoInvalidoException e) {
                System.out.println("Sucesso! Capturada a exceção esperada ao criar aluno: " + e.getMessage() + "\n");
            }
        }

        System.out.println("--- Teste de Leitura de Aluno por Matrícula (Sucesso) ---");
        try {
            Aluno alunoConsultado = alunoServico.consultarAlunoPorMatricula("2025001");
            System.out.println("Aluno encontrado:");
            System.out.println("Nome: " + alunoConsultado.getNome());
            System.out.println("Matrícula: " + alunoConsultado.getMatricula() + "\n");
        } catch (EntidadeNaoEncontradaException e) {
            System.out.println("Erro inesperado ao buscar aluno existente: " + e.getMessage() + "\n");
        }

        System.out.println("--- Teste de Leitura de Aluno por Matrícula (Falha - Exceção Esperada) ---");
        try {
            Aluno alunoInexistente = alunoServico.consultarAlunoPorMatricula("9999999");
            System.out.println("Aluno encontrado: " + alunoInexistente.getNome() + "\n");
        } catch (EntidadeNaoEncontradaException e) {
            System.out.println("Sucesso! Capturada a exceção: " + e.getMessage() + "\n");
        }

        System.out.println("--- Teste de Listagem de Todos os Alunos ---");
        Pessoa pessoaAluno2 = PessoaFactory.criarPessoa(TipoPessoa.ALUNO);
        if (pessoaAluno2 instanceof Aluno aluno2) {
            aluno2.setNome("João Santos");
            aluno2.setCpf("555.666.777-88");
            aluno2.setMatricula("2025002");
            aluno2.setAnoLetivo(2025);
            try {
                alunoServico.criarAluno(aluno2);
                System.out.println("Aluno criado: " + aluno2.getNome() + " (Matrícula: " + aluno2.getMatricula() + ")\n");
            } catch (DadoInvalidoException e) {
                System.out.println("Erro inesperado ao criar aluno: " + e.getMessage() + "\n");
            }
        }

        List<Aluno> todosAlunos = alunoServico.listarTodosAlunos();
        if (!todosAlunos.isEmpty()) {
            System.out.println("Alunos cadastrados:");
            for (Aluno a : todosAlunos) {
                System.out.println("- " + a.getNome() + " (Matrícula: " + a.getMatricula() + ")");
            }
        } else {
            System.out.println("Nenhum aluno cadastrado.\n");
        }
        System.out.println();

        System.out.println("--- Teste de Atualização de Aluno (Sucesso) ---");
        try {
            Aluno alunoParaAtualizar = alunoServico.consultarAlunoPorMatricula("2025001");
            String novoNome = "Maria Silva Atualizada";
            alunoParaAtualizar.setNome(novoNome);
            alunoServico.atualizarAluno(alunoParaAtualizar);
            System.out.println("Aluno atualizado para: " + alunoParaAtualizar.getNome() + "\n");

            Aluno alunoVerificado = alunoServico.consultarAlunoPorMatricula("2025001");
            System.out.println("Verificação: Nome do aluno é agora '" + alunoVerificado.getNome() + "'\n");
        } catch (EntidadeNaoEncontradaException | DadoInvalidoException e) {
            System.out.println("Não foi possível atualizar o aluno 2025001: " + e.getMessage() + "\n");
        }

        System.out.println("--- Teste de Atualização de Aluno (Falha - Dado Inválido Esperado) ---");
        try {
            Aluno alunoComDadoInvalido = alunoServico.consultarAlunoPorMatricula("2025002");
            alunoComDadoInvalido.setNome("");
            alunoServico.atualizarAluno(alunoComDadoInvalido);
            System.out.println("Erro: Aluno atualizado com dados inválidos.\n");
        } catch (DadoInvalidoException e) {
            System.out.println("Sucesso! Capturada a exceção esperada ao atualizar aluno: " + e.getMessage() + "\n");
        } catch (EntidadeNaoEncontradaException e) {
            System.out.println("Erro inesperado ao buscar aluno para atualização: " + e.getMessage() + "\n");
        }

        System.out.println("--- Teste de Deleção de Aluno (Sucesso) ---");
        try {
            boolean deletado = alunoServico.excluirAluno("2025001");
            if (deletado) {
                System.out.println("Aluno com matrícula 2025001 deletado com sucesso.\n");
                try {
                    Aluno alunoDeletadoConsultado = alunoServico.consultarAlunoPorMatricula("2025001");
                    System.out.println("Erro: Aluno 2025001 ainda encontrado após deleção.\n");
                } catch (EntidadeNaoEncontradaException e) {
                    System.out.println("Confirmação: " + e.getMessage() + "\n");
                }
            } else {
                System.out.println("Falha ao deletar aluno com matrícula 2025001.\n");
            }
        } catch (EntidadeNaoEncontradaException e) {
            System.out.println("Erro inesperado ao tentar deletar aluno existente: " + e.getMessage() + "\n");
        }

        System.out.println("--- Teste de Deleção de Aluno (Falha - Entidade Não Encontrada Esperada) ---");
        try {
            alunoServico.excluirAluno("9999998");
            System.out.println("Erro: Aluno inexistente deletado.\n");
        } catch (EntidadeNaoEncontradaException e) {
            System.out.println("Sucesso! Capturada a exceção esperada ao tentar deletar aluno inexistente: " + e.getMessage() + "\n");
        }

        System.out.println("--- Alunos restantes após todas as operações ---");
        List<Aluno> alunosRestantes = alunoServico.listarTodosAlunos();
        if (!alunosRestantes.isEmpty()) {
            System.out.println("Alunos restantes:");
            for (Aluno a : alunosRestantes) {
                System.out.println("- " + a.getNome() + " (Matrícula: " + a.getMatricula() + ")");
            }
        } else {
            System.out.println("Nenhum aluno restante.\n");
        }

        System.out.println("### TESTES CRUD FINALIZADOS ###");
    }
}