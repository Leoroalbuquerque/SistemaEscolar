package br.com.escola.main;

import br.com.escola.negocio.Aluno;
import br.com.escola.negocio.AlunoServico;
import br.com.escola.negocio.Pessoa;
import br.com.escola.negocio.PessoaFactory;
import br.com.escola.negocio.TipoPessoa;
import java.util.List;

public class SistemaEscolarTeste {

    public static void main(String[] args) {
        System.out.println("### INICIANDO TESTES CRUD DO SISTEMA ESCOLAR COM NOVA ESTRUTURA E FACTORY ###\n");

        AlunoServico alunoServico = new AlunoServico();

        System.out.println("--- Teste de Criação de Aluno (via Factory) ---");
        Pessoa pessoaAluno = PessoaFactory.criarPessoa(TipoPessoa.ALUNO);

        if (pessoaAluno instanceof Aluno aluno1) {
            aluno1.setNome("Maria Silva");
            aluno1.setCpf("111.222.333-44");
            aluno1.setMatricula("2025001");
            aluno1.setAnoLetivo(2025);

            alunoServico.criarAluno(aluno1);
            System.out.println("Aluno criado: " + aluno1.getNome() + " (Matrícula: " + aluno1.getMatricula() + ")\n");
        } else {
            System.out.println("Erro: A factory não criou um objeto Aluno.\n");
        }


        System.out.println("--- Teste de Leitura de Aluno por Matrícula ---");
        Aluno alunoConsultado = alunoServico.consultarAlunoPorMatricula("2025001");

        if (alunoConsultado != null) {
            System.out.println("Aluno encontrado:");
            System.out.println("Nome: " + alunoConsultado.getNome());
            System.out.println("Matrícula: " + alunoConsultado.getMatricula() + "\n");
        } else {
            System.out.println("Aluno com matrícula 2025001 não encontrado.\n");
        }


        System.out.println("--- Teste de Listagem de Todos os Alunos ---");
        
        Pessoa pessoaAluno2 = PessoaFactory.criarPessoa(TipoPessoa.ALUNO);
        if (pessoaAluno2 instanceof Aluno) {
            Aluno aluno2 = (Aluno) pessoaAluno2;
            aluno2.setNome("João Santos");
            aluno2.setCpf("555.666.777-88");
            aluno2.setMatricula("2025002");
            aluno2.setAnoLetivo(2025);
            alunoServico.criarAluno(aluno2);
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


        System.out.println("--- Teste de Atualização de Aluno ---");
        if (alunoConsultado != null) {
            String novoNome = "Maria Silva Atualizada";
            alunoConsultado.setNome(novoNome);
            alunoServico.atualizarAluno(alunoConsultado);
            System.out.println("Aluno atualizado para: " + alunoConsultado.getNome() + "\n");

            Aluno alunoVerificado = alunoServico.consultarAlunoPorMatricula("2025001");
            if (alunoVerificado != null && alunoVerificado.getNome().equals(novoNome)) {
                System.out.println("Verificação: Nome do aluno é agora '" + alunoVerificado.getNome() + "'\n");
            } else {
                System.out.println("Verificação: A atualização pode não ter funcionado como esperado.\n");
            }
        } else {
            System.out.println("Não foi possível atualizar, aluno inicial não encontrado.\n");
        }


        System.out.println("--- Teste de Deleção de Aluno ---");
        boolean deletado = alunoServico.excluirAluno("2025001");

        if (deletado) {
            System.out.println("Aluno com matrícula 2025001 deletado com sucesso.\n");
            Aluno alunoDeletadoConsultado = alunoServico.consultarAlunoPorMatricula("2025001");
            if (alunoDeletadoConsultado == null) {
                System.out.println("Confirmação: Aluno 2025001 não encontrado após deleção (esperado).\n");
            }
        } else {
            System.out.println("Falha ao deletar aluno com matrícula 2025001.\n");
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