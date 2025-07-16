package br.com.escola.dados;

import br.com.escola.excecoes.DadoInvalidoException;
import br.com.escola.excecoes.EntidadeNaoEncontradaException;
import br.com.escola.negocio.Turma;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TurmaRepositorioJson implements IRepositorio<Turma, String> {

    private final String NOME_ARQUIVO = "turmas.json";
    private List<Turma> turmas;
    private final ObjectMapper objectMapper;

    public TurmaRepositorioJson() {
        objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT); // Formata o JSON para ser legível
        carregarDados();
    }

    private void carregarDados() {
        File file = new File(NOME_ARQUIVO);
        if (file.exists() && file.length() > 0) {
            try {
                String jsonContent = new String(Files.readAllBytes(Paths.get(NOME_ARQUIVO)));
                this.turmas = objectMapper.readValue(jsonContent, new TypeReference<List<Turma>>() {});
            } catch (IOException e) {
                System.err.println("Erro ao carregar dados do arquivo " + NOME_ARQUIVO + ": " + e.getMessage());
                this.turmas = new ArrayList<>();
            }
        } else {
            this.turmas = new ArrayList<>();
        }
    }

    private void salvarDados() {
        try {
            objectMapper.writeValue(new File(NOME_ARQUIVO), turmas);
        } catch (IOException e) {
            System.err.println("Erro ao salvar dados no arquivo " + NOME_ARQUIVO + ": " + e.getMessage());
        }
    }

    @Override
    public void adicionar(Turma entidade) throws DadoInvalidoException {
        if (entidade == null || entidade.getCodigo() == null || entidade.getCodigo().trim().isEmpty()) {
            throw new DadoInvalidoException("Turma ou código da turma não pode ser nulo.");
        }
        boolean existe = turmas.stream()
                .anyMatch(t -> t.getCodigo().equals(entidade.getCodigo()));
        if (existe) {
            throw new DadoInvalidoException("Já existe uma turma com o código: " + entidade.getCodigo());
        }
        if (entidade.getNomeTurma() == null || entidade.getNomeTurma().trim().isEmpty()) {
            throw new DadoInvalidoException("Nome da turma é obrigatório.");
        }

        this.turmas.add(entidade);
        salvarDados();
    }

    @Override
    public Optional<Turma> buscarPorId(String id) {
        if (id == null || id.trim().isEmpty()) {
            return Optional.empty();
        }
        return turmas.stream()
                .filter(t -> t.getCodigo().equals(id))
                .findFirst();
    }

    @Override
    public void atualizar(Turma entidade) throws DadoInvalidoException, EntidadeNaoEncontradaException {
        if (entidade == null || entidade.getCodigo() == null || entidade.getCodigo().trim().isEmpty()) {
            throw new DadoInvalidoException("Turma ou código da turma não pode ser nulo para atualização.");
        }
        if (entidade.getNomeTurma() == null || entidade.getNomeTurma().trim().isEmpty()) {
            throw new DadoInvalidoException("Nome da turma é obrigatório para atualização.");
        }

        Optional<Turma> turmaExistenteOpt = buscarPorId(entidade.getCodigo());
        if (turmaExistenteOpt.isEmpty()) {
            throw new EntidadeNaoEncontradaException("Turma com código " + entidade.getCodigo() + " não encontrada para atualização.");
        }
        
        turmas.removeIf(t -> t.getCodigo().equals(entidade.getCodigo()));
        this.turmas.add(entidade);
        salvarDados();
    }

    @Override
    public boolean deletar(String id) throws EntidadeNaoEncontradaException, DadoInvalidoException {
        if (id == null || id.trim().isEmpty()) {
            throw new DadoInvalidoException("ID para deleção não pode ser nulo ou vazio.");
        }
        boolean removido = turmas.removeIf(t -> t.getCodigo().equals(id));
        if (!removido) {
            throw new EntidadeNaoEncontradaException("Turma com código " + id + " não encontrada para deleção.");
        }
        salvarDados();
        return true;
    }

    @Override
    public List<Turma> listarTodos() {
        return new ArrayList<>(turmas);
    }

    /**
     * Limpa todos os dados de turmas do repositório e persiste a lista vazia no arquivo JSON.
     */
    public void limpar() {
        this.turmas.clear(); // Limpa a lista em memória
        salvarDados();       // Salva a lista vazia no arquivo
        System.out.println("DEBUG: Arquivo " + NOME_ARQUIVO + " limpo.");
    }
}