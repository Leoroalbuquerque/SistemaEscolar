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

    private void salvarDados() throws IOException {
        try {
            objectMapper.writeValue(new File(CAMINHO_ARQUIVO), alunos);
        } catch (IOException e) {
            System.err.println("Erro ao salvar dados de alunos: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void salvar(Aluno aluno) throws DadoInvalidoException, IOException {
        if (aluno == null) {
            throw new DadoInvalidoException("Aluno não pode ser nulo.");
        }
        try {
            if (buscarPorId(aluno.getMatricula()) != null) {
                throw new DadoInvalidoException("Aluno com matrícula " + aluno.getMatricula() + " já existe.");
            }
        } catch (EntidadeNaoEncontradaException e) {
        }
        alunos.add(aluno);
        salvarDados();
    }

    @Override
    public Aluno buscarPorId(String matricula) throws IOException, EntidadeNaoEncontradaException {
        return alunos.stream()
                .filter(a -> a.getMatricula().equals(matricula))
                .findFirst()
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Aluno com matrícula " + matricula + " não encontrado."));
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
            if (alunos.get(i).getMatricula().equals(aluno.getMatricula())) {
                alunos.set(i, aluno);
                encontrada = true;
                break;
            }
        }
        if (!encontrada) {
            throw new EntidadeNaoEncontradaException("Aluno com matrícula " + aluno.getMatricula() + " não encontrado para atualização.");
        }
        salvarDados();
    }

    @Override
    public void deletar(String matricula) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        if (matricula == null || matricula.isEmpty()) {
            throw new DadoInvalidoException("Matrícula do aluno não pode ser nula ou vazia.");
        }
        boolean removido = alunos.removeIf(a -> a.getMatricula().equals(matricula));
        if (!removido) {
            throw new EntidadeNaoEncontradaException("Aluno com matrícula " + matricula + " não encontrado para exclusão.");
        }
        salvarDados();
    }

    @Override
    public void limpar() throws IOException {
        alunos.clear();
        salvarDados();
    }
}