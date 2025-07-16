package br.com.escola.dados;

import br.com.escola.negocio.Aluno;
import br.com.escola.excecoes.DadoInvalidoException;
import br.com.escola.excecoes.EntidadeNaoEncontradaException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional; // Adicionado: Importação para Optional
import java.util.stream.Collectors;

public class AlunoRepositorioArquivoTxt implements IRepositorio<Aluno, String> {

    private static final String NOME_ARQUIVO = "alunos.txt";

    public AlunoRepositorioArquivoTxt() {
        File arquivo = new File(NOME_ARQUIVO);
        if (!arquivo.exists()) {
            try {
                arquivo.createNewFile();
                System.out.println("Arquivo " + NOME_ARQUIVO + " criado.");
            } catch (IOException e) {
                System.err.println("Erro ao criar o arquivo " + NOME_ARQUIVO + ": " + e.getMessage());
            }
        }
    }

    private void salvarNoArquivo(List<Aluno> alunos) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(NOME_ARQUIVO))) {
            for (Aluno aluno : alunos) {
                // Formato: nome;cpf;telefone;email;matricula;anoLetivo
                writer.write(aluno.getNome() + ";" +
                             aluno.getCpf() + ";" +
                             aluno.getTelefone() + ";" +
                             aluno.getEmail() + ";" +
                             aluno.getMatricula() + ";" +
                             aluno.getAnoLetivo());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar alunos no arquivo: " + e.getMessage());
        }
    }

    private List<Aluno> carregarDoArquivo() {
        List<Aluno> alunos = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(NOME_ARQUIVO))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split(";");
                if (dados.length == 6) {
                    Aluno aluno = new Aluno();
                    aluno.setNome(dados[0]);
                    aluno.setCpf(dados[1]);
                    aluno.setTelefone(dados[2]);
                    aluno.setEmail(dados[3]);
                    aluno.setMatricula(dados[4]);
                    aluno.setAnoLetivo(Integer.parseInt(dados[5]));
                    alunos.add(aluno);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo " + NOME_ARQUIVO + " não encontrado. Iniciando com lista vazia.");
        } catch (IOException e) {
            System.err.println("Erro ao carregar alunos do arquivo: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Erro de formato numérico ao carregar aluno: " + e.getMessage());
        }
        return alunos;
    }

    @Override
    public void adicionar(Aluno entidade) throws DadoInvalidoException {
        if (entidade == null || entidade.getMatricula() == null || entidade.getMatricula().trim().isEmpty()) {
            throw new DadoInvalidoException("Matrícula do aluno é obrigatória.");
        }
        List<Aluno> alunos = carregarDoArquivo();
        boolean existe = alunos.stream()
                .anyMatch(a -> a.getMatricula().equals(entidade.getMatricula()));
        if (existe) {
            throw new DadoInvalidoException("Aluno com matrícula " + entidade.getMatricula() + " já existe.");
        }
        alunos.add(entidade);
        salvarNoArquivo(alunos);
    }

    @Override // Alterado: De 'buscar' para 'buscarPorId' e retorno para Optional<Aluno>
    public Optional<Aluno> buscarPorId(String chave) { // Agora retorna Optional<Aluno>
        List<Aluno> alunos = carregarDoArquivo();
        return alunos.stream()
                .filter(a -> a.getMatricula().equals(chave))
                .findFirst(); // Retorna Optional<Aluno>
    }

    @Override
    public void atualizar(Aluno entidade) throws DadoInvalidoException, EntidadeNaoEncontradaException {
        if (entidade == null || entidade.getMatricula() == null || entidade.getMatricula().trim().isEmpty()) {
            throw new DadoInvalidoException("Matrícula do aluno para atualização é obrigatória.");
        }
        List<Aluno> alunos = carregarDoArquivo();
        boolean removido = alunos.removeIf(a -> a.getMatricula().equals(entidade.getMatricula()));
        if (removido) {
            alunos.add(entidade);
            salvarNoArquivo(alunos);
        } else {
            throw new EntidadeNaoEncontradaException("Aluno com matrícula " + entidade.getMatricula() + " não encontrado para atualização.");
        }
    }

    @Override
    public boolean deletar(String chave) throws EntidadeNaoEncontradaException, DadoInvalidoException {
        if (chave == null || chave.trim().isEmpty()) {
            throw new DadoInvalidoException("Matrícula para deleção não pode ser nula ou vazia.");
        }
        List<Aluno> alunos = carregarDoArquivo();
        boolean removido = alunos.removeIf(a -> a.getMatricula().equals(chave));
        if (removido) {
            salvarNoArquivo(alunos);
            return true;
        } else {
            throw new EntidadeNaoEncontradaException("Aluno com matrícula " + chave + " não encontrado para deleção.");
        }
    }

    @Override
    public List<Aluno> listarTodos() {
        return new ArrayList<>(carregarDoArquivo());
    }
}