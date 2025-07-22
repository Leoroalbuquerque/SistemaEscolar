package br.com.escola.dados;

import br.com.escola.excecoes.DadoInvalidoException;
import br.com.escola.excecoes.EntidadeNaoEncontradaException;
import br.com.escola.negocio.Aluno;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AlunoRepositorioJson implements IRepositorio<Aluno, String> {

    private final String CAMINHO_ARQUIVO = "alunos.json";
    private ObjectMapper objectMapper;
    private List<Aluno> alunos;

    public AlunoRepositorioJson() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        alunos = carregarDados();
    }

    private List<Aluno> carregarDados() {
        File arquivo = new File(CAMINHO_ARQUIVO);
        if (arquivo.exists() && arquivo.length() > 0) {
            try {
                return objectMapper.readValue(arquivo, objectMapper.getTypeFactory().constructCollectionType(List.class, Aluno.class));
            } catch (IOException e) {
                System.err.println("Erro ao carregar dados de alunos: " + e.getMessage());
                return new ArrayList<>();
            }
        }
        return new ArrayList<>();
    }

    private void salvarDados() {
        try {
            objectMapper.writeValue(new File(CAMINHO_ARQUIVO), alunos);
        } catch (IOException e) {
            System.err.println("Erro ao salvar dados de alunos: " + e.getMessage());
        }
    }

    @Override
    public void salvar(Aluno aluno) throws DadoInvalidoException {
        if (aluno == null) {
            throw new DadoInvalidoException("Aluno não pode ser nulo.");
        }
        if (buscarPorId(aluno.getMatricula()).isPresent()) {
            throw new DadoInvalidoException("Aluno com matrícula " + aluno.getMatricula() + " já existe.");
        }
        alunos.add(aluno);
        salvarDados();
    }

    @Override
    public Optional<Aluno> buscarPorId(String matricula) {
        return alunos.stream()
                .filter(a -> a.getMatricula().equals(matricula))
                .findFirst();
    }

    @Override
    public List<Aluno> listarTodos() {
        return new ArrayList<>(alunos);
    }

    @Override
    public void atualizar(Aluno aluno) throws EntidadeNaoEncontradaException, DadoInvalidoException {
        if (aluno == null) {
            throw new DadoInvalidoException("Aluno não pode ser nulo.");
        }
        Optional<Aluno> existenteOpt = buscarPorId(aluno.getMatricula());
        if (existenteOpt.isEmpty()) {
            throw new EntidadeNaoEncontradaException("Aluno com matrícula " + aluno.getMatricula() + " não encontrado para atualização.");
        }
        alunos = alunos.stream()
                .map(a -> a.getMatricula().equals(aluno.getMatricula()) ? aluno : a)
                .collect(Collectors.toList());
        salvarDados();
    }

    @Override
    public boolean deletar(String matricula) throws EntidadeNaoEncontradaException, DadoInvalidoException {
        if (matricula == null || matricula.isEmpty()) {
            throw new DadoInvalidoException("Matrícula do aluno não pode ser nula ou vazia.");
        }
        boolean removido = alunos.removeIf(a -> a.getMatricula().equals(matricula));
        if (!removido) {
            throw new EntidadeNaoEncontradaException("Aluno com matrícula " + matricula + " não encontrado para exclusão.");
        }
        salvarDados();
        return removido;
    }

    @Override
    public void limpar() {
        alunos.clear();
        salvarDados();
    }
}