package br.com.escola.dados;

import br.com.escola.excecoes.DadoInvalidoException;
import br.com.escola.excecoes.EntidadeNaoEncontradaException;
import br.com.escola.negocio.Professor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ProfessorRepositorioJson implements IRepositorio<Professor, String> {

    private final String NOME_ARQUIVO = "professores.json";
    private List<Professor> professores;
    private final ObjectMapper objectMapper;

    public ProfessorRepositorioJson() {
        objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.registerModule(new JavaTimeModule());
        carregarDados();
    }

    private void carregarDados() {
        File file = new File(NOME_ARQUIVO);
        if (file.exists() && file.length() > 0) {
            try {
                String jsonContent = new String(Files.readAllBytes(Paths.get(NOME_ARQUIVO)));
                this.professores = objectMapper.readValue(jsonContent, new TypeReference<List<Professor>>() {});
            } catch (IOException e) {
                System.err.println("Erro ao carregar dados do arquivo " + NOME_ARQUIVO + ": " + e.getMessage());
                this.professores = new ArrayList<>();
            }
        } else {
            this.professores = new ArrayList<>();
        }
    }

    private void salvarDados() {
        try {
            objectMapper.writeValue(new File(NOME_ARQUIVO), professores);
        } catch (IOException e) {
            System.err.println("Erro ao salvar dados no arquivo " + NOME_ARQUIVO + ": " + e.getMessage());
        }
    }

    @Override
    public void salvar(Professor entidade) throws DadoInvalidoException {
        if (entidade == null || entidade.getRegistroFuncional() == null || entidade.getRegistroFuncional().trim().isEmpty()) {
            throw new DadoInvalidoException("Professor ou registro funcional não pode ser nulo.");
        }
        if (entidade.getNome() == null || entidade.getNome().trim().isEmpty()) {
            throw new DadoInvalidoException("Nome do professor é obrigatório.");
        }

        boolean existe = professores.stream()
                .anyMatch(p -> p.getRegistroFuncional().equals(entidade.getRegistroFuncional()));
        if (existe) {
            throw new DadoInvalidoException("Já existe um professor com o registro funcional: " + entidade.getRegistroFuncional());
        }
        this.professores.add(entidade);
        salvarDados();
    }

    @Override
    public Optional<Professor> buscarPorId(String id) {
        if (id == null || id.trim().isEmpty()) {
            return Optional.empty();
        }
        return professores.stream()
                .filter(p -> p.getRegistroFuncional().equals(id))
                .findFirst();
    }

    @Override
    public void atualizar(Professor entidade) throws DadoInvalidoException, EntidadeNaoEncontradaException {
        if (entidade == null || entidade.getRegistroFuncional() == null || entidade.getRegistroFuncional().trim().isEmpty()) {
            throw new DadoInvalidoException("Professor ou registro funcional não pode ser nulo para atualização.");
        }
        if (entidade.getNome() == null || entidade.getNome().trim().isEmpty()) {
            throw new DadoInvalidoException("Nome do professor é obrigatório para atualização.");
        }

        Optional<Professor> professorExistenteOpt = buscarPorId(entidade.getRegistroFuncional());
        if (professorExistenteOpt.isEmpty()) {
            throw new EntidadeNaoEncontradaException("Professor com registro " + entidade.getRegistroFuncional() + " não encontrado para atualização.");
        }

        professores.removeIf(p -> p.getRegistroFuncional().equals(entidade.getRegistroFuncional()));
        this.professores.add(entidade);
        salvarDados();
    }

    @Override
    public boolean deletar(String id) throws EntidadeNaoEncontradaException, DadoInvalidoException {
        if (id == null || id.trim().isEmpty()) {
            throw new DadoInvalidoException("ID para deleção não pode ser nulo ou vazio.");
        }
        boolean removido = professores.removeIf(p -> p.getRegistroFuncional().equals(id));
        if (!removido) {
            throw new EntidadeNaoEncontradaException("Professor com registro " + id + " não encontrado para deleção.");
        }
        salvarDados();
        return true;
    }

    @Override
    public List<Professor> listarTodos() {
        return new ArrayList<>(professores);
    }

    @Override
    public void limpar() {
        this.professores.clear();
        salvarDados();
        System.out.println("DEBUG: Arquivo " + NOME_ARQUIVO + " limpo.");
    }
}