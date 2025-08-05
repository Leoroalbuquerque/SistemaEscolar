package br.com.escola.dados;

import br.com.escola.excecoes.DadoInvalidoException;
import br.com.escola.excecoes.EntidadeNaoEncontradaException;
import br.com.escola.negocio.Aluno;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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

    private void salvarDados() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CAMINHO_ARQUIVO))) {
            for (Aluno aluno : alunos) {
                writer.write(aluno.toLine());
                writer.newLine();
            }
        }
    }

    @Override
    public void salvar(Aluno aluno) throws DadoInvalidoException, IOException {
        if (aluno == null) {
            throw new DadoInvalidoException("Aluno não pode ser nulo.");
        }
        try {
            if (buscarPorId(aluno.getCpf()) != null) {
                throw new DadoInvalidoException("Aluno com CPF " + aluno.getCpf() + " já existe.");
            }
        } catch (EntidadeNaoEncontradaException e) {
        }
        alunos.add(aluno);
        salvarDados();
    }

    @Override
    public Aluno buscarPorId(String cpf) throws IOException, EntidadeNaoEncontradaException {
        return alunos.stream()
                .filter(a -> a.getCpf().equals(cpf))
                .findFirst()
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Aluno com CPF " + cpf + " não encontrado."));
    }

    @Override
    public List<Aluno> listarTodos() throws IOException {
        return new ArrayList<>(alunos);
    }

    @Override
    public void atualizar(Aluno aluno) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        if (aluno == null) {
            throw new DadoInvalidoException("Aluno não pode ser nulo para atualização.");
        }
        boolean encontrada = false;
        for (int i = 0; i < alunos.size(); i++) {
            if (alunos.get(i).getCpf().equals(aluno.getCpf())) {
                alunos.set(i, aluno);
                encontrada = true;
                break;
            }
        }
        if (!encontrada) {
            throw new EntidadeNaoEncontradaException("Aluno com CPF " + aluno.getCpf() + " não encontrado para atualização.");
        }
        salvarDados();
    }

    @Override
    public void deletar(String cpf) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        if (cpf == null || cpf.isEmpty()) {
            throw new DadoInvalidoException("CPF do aluno não pode ser nulo ou vazio.");
        }
        boolean removido = alunos.removeIf(a -> a.getCpf().equals(cpf));
        if (!removido) {
            throw new EntidadeNaoEncontradaException("Aluno com CPF " + cpf + " não encontrado para exclusão.");
        }
        salvarDados();
    }

    @Override
    public void limpar() throws IOException {
        alunos.clear();
        salvarDados();
    }
}