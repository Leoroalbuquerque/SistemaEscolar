package br.com.escola.dados;

import br.com.escola.negocio.Aluno;
import br.com.escola.excecoes.DadoInvalidoException;
import br.com.escola.excecoes.EntidadeNaoEncontradaException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AlunoRepositorioJson implements IRepositorio<Aluno, String> {

    private static final String NOME_ARQUIVO = "alunos.json";
    private final ObjectMapper objectMapper;
    private List<Aluno> alunosCarregados;

    public AlunoRepositorioJson() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        this.alunosCarregados = carregarAlunosDoArquivo();
    }

    private List<Aluno> carregarAlunosDoArquivo() {
        File arquivo = new File(NOME_ARQUIVO);
        if (!arquivo.exists() || arquivo.length() == 0) {
            System.out.println("Arquivo " + NOME_ARQUIVO + " não encontrado ou vazio. Iniciando com lista vazia.");
            return new ArrayList<>();
        }
        try {
            List<Aluno> alunos = objectMapper.readValue(arquivo, new TypeReference<List<Aluno>>() {});
            System.out.println("Alunos carregados do arquivo: " + NOME_ARQUIVO);
            return alunos;
        } catch (IOException e) {
            System.err.println("Erro ao carregar alunos do arquivo " + NOME_ARQUIVO + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private void salvarAlunosNoArquivo() {
        try {
            objectMapper.writeValue(new File(NOME_ARQUIVO), alunosCarregados);
            System.out.println("Alunos salvos no arquivo: " + NOME_ARQUIVO);
        } catch (IOException e) {
            System.err.println("Erro ao salvar alunos no arquivo " + NOME_ARQUIVO + ": " + e.getMessage());
        }
    }

    @Override
    public void adicionar(Aluno aluno) throws DadoInvalidoException {
        if (aluno == null || aluno.getMatricula() == null || aluno.getMatricula().trim().isEmpty()) {
            throw new DadoInvalidoException("Matrícula do aluno é obrigatória.");
        }
        if (aluno.getNome() == null || aluno.getNome().trim().isEmpty()) {
            throw new DadoInvalidoException("Nome do aluno é obrigatório.");
        }

        boolean jaExiste = alunosCarregados.stream()
                .anyMatch(a -> a.getMatricula().equalsIgnoreCase(aluno.getMatricula()));

        if (jaExiste) {
            throw new DadoInvalidoException("Já existe um aluno cadastrado com a matrícula: " + aluno.getMatricula());
        }

        alunosCarregados.add(aluno);
        salvarAlunosNoArquivo();
    }

    @Override
    public Optional<Aluno> buscarPorId(String matricula) {
        System.out.println("Buscando aluno com matrícula: " + matricula + " no JSON.");
        return alunosCarregados.stream()
                .filter(a -> a.getMatricula().equalsIgnoreCase(matricula))
                .findFirst();
    }

    @Override
    public void atualizar(Aluno aluno) throws DadoInvalidoException, EntidadeNaoEncontradaException {
        if (aluno == null || aluno.getMatricula() == null || aluno.getMatricula().trim().isEmpty()) {
            throw new DadoInvalidoException("Matrícula do aluno para atualização é obrigatória.");
        }
        if (aluno.getNome() == null || aluno.getNome().trim().isEmpty()) {
            throw new DadoInvalidoException("Nome do aluno é obrigatório para atualização.");
        }

        Optional<Aluno> alunoExistenteOpt = alunosCarregados.stream()
                .filter(a -> a.getMatricula().equalsIgnoreCase(aluno.getMatricula()))
                .findFirst();

        if (alunoExistenteOpt.isEmpty()) {
            throw new EntidadeNaoEncontradaException("Aluno com matrícula " + aluno.getMatricula() + " não encontrado para atualização no JSON.");
        }

        alunosCarregados.removeIf(a -> a.getMatricula().equalsIgnoreCase(aluno.getMatricula()));
        alunosCarregados.add(aluno);
        System.out.println("Atualizando aluno com matrícula: " + aluno.getMatricula() + " no JSON.");
        salvarAlunosNoArquivo();
    }

    @Override
    public boolean deletar(String matricula) throws EntidadeNaoEncontradaException, DadoInvalidoException {
        System.out.println("Deletando aluno com matrícula: " + matricula + " do JSON.");
        if (matricula == null || matricula.trim().isEmpty()) {
            throw new DadoInvalidoException("A matrícula para exclusão não pode ser vazia.");
        }
        boolean removido = alunosCarregados.removeIf(a -> a.getMatricula().equalsIgnoreCase(matricula));
        if (!removido) {
            throw new EntidadeNaoEncontradaException("Aluno com matrícula " + matricula + " não encontrado para deleção no JSON.");
        }
        salvarAlunosNoArquivo();
        return true;
    }

    @Override
    public List<Aluno> listarTodos() {
        System.out.println("Listando todos os alunos do JSON.");
        return new ArrayList<>(alunosCarregados);
    }

    /**
     * Limpa todos os dados do repositório e persiste a lista vazia no arquivo JSON.
     */
    public void limpar() {
        this.alunosCarregados.clear(); // Limpa a lista em memória
        salvarAlunosNoArquivo();       // Salva a lista vazia no arquivo
        System.out.println("DEBUG: Arquivo " + NOME_ARQUIVO + " limpo.");
    }
}