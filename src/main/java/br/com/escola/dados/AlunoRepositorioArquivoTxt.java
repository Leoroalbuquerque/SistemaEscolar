package br.com.escola.dados;

import br.com.escola.excecoes.DadoInvalidoException;
import br.com.escola.excecoes.EntidadeNaoEncontradaException;
import br.com.escola.negocio.Aluno;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AlunoRepositorioArquivoTxt implements IRepositorio<Aluno, String> {

    private final String CAMINHO_ARQUIVO = "alunos.txt";
    private List<Aluno> alunos;

    public AlunoRepositorioArquivoTxt() {
        alunos = carregarDados();
    }

    private List<Aluno> carregarDados() {
        List<Aluno> lista = new ArrayList<>();
        File arquivo = new File(CAMINHO_ARQUIVO);
        if (arquivo.exists() && arquivo.length() > 0) {
            try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
                String linha;
                while ((linha = reader.readLine()) != null) {
                    Aluno aluno = Aluno.fromLine(linha);
                    if (aluno != null) {
                        lista.add(aluno);
                    }
                }
            } catch (IOException e) {
                System.err.println("Erro ao carregar dados de alunos do arquivo: " + e.getMessage());
            }
        }
        return lista;
    }

    private void salvarDados() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CAMINHO_ARQUIVO))) {
            for (Aluno aluno : alunos) {
                writer.write(aluno.toLine());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar dados de alunos no arquivo: " + e.getMessage());
        }
    }

    @Override
    public void salvar(Aluno aluno) throws DadoInvalidoException {
        if (aluno == null) {
            throw new DadoInvalidoException("Aluno não pode ser nulo.");
        }
        if (buscarPorId(aluno.getCpf()).isPresent()) {
            throw new DadoInvalidoException("Aluno com CPF " + aluno.getCpf() + " já existe.");
        }
        alunos.add(aluno);
        salvarDados();
    }

    @Override
    public Optional<Aluno> buscarPorId(String cpf) {
        return alunos.stream()
                .filter(a -> a.getCpf().equals(cpf))
                .findFirst();
    }

    @Override
    public void atualizar(Aluno aluno) throws EntidadeNaoEncontradaException, DadoInvalidoException {
        if (aluno == null) {
            throw new DadoInvalidoException("Aluno não pode ser nulo para atualização.");
        }
        Optional<Aluno> existenteOpt = buscarPorId(aluno.getCpf());
        if (existenteOpt.isEmpty()) {
            throw new EntidadeNaoEncontradaException("Aluno com CPF " + aluno.getCpf() + " não encontrado para atualização.");
        }
        alunos = alunos.stream()
                .map(a -> a.getCpf().equals(aluno.getCpf()) ? aluno : a)
                .collect(Collectors.toList());
        salvarDados();
    }

    @Override
    public boolean deletar(String cpf) throws EntidadeNaoEncontradaException, DadoInvalidoException {
        if (cpf == null || cpf.isEmpty()) {
            throw new DadoInvalidoException("CPF do aluno não pode ser nulo ou vazio.");
        }
        boolean removido = alunos.removeIf(a -> a.getCpf().equals(cpf));
        if (!removido) {
            throw new EntidadeNaoEncontradaException("Aluno com CPF " + cpf + " não encontrado para exclusão.");
        }
        salvarDados();
        return removido;
    }

    @Override
    public List<Aluno> listarTodos() {
        return new ArrayList<>(alunos);
    }

    @Override
    public void limpar() {
        alunos.clear();
        salvarDados();
    }
}