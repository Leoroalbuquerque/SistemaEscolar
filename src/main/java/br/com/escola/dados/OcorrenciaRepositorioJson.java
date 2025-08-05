package br.com.escola.dados;

import br.com.escola.negocio.Ocorrencia;
import br.com.escola.excecoes.EntidadeNaoEncontradaException;
import br.com.escola.excecoes.DadoInvalidoException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OcorrenciaRepositorioJson implements IRepositorio<Ocorrencia, String> {

    private static final String NOME_ARQUIVO = "ocorrencias.json";
    private final File arquivo;
    private final ObjectMapper objectMapper;
    private List<Ocorrencia> ocorrencias;

    public OcorrenciaRepositorioJson() {
        this.arquivo = new File(NOME_ARQUIVO);
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.ocorrencias = carregarOcorrencias();
    }

    private List<Ocorrencia> carregarOcorrencias() {
        if (!arquivo.exists() || arquivo.length() == 0) {
            return new ArrayList<>();
        }
        try {
            return objectMapper.readValue(arquivo, new TypeReference<List<Ocorrencia>>() {});
        } catch (IOException e) {
            System.err.println("Erro ao carregar ocorrências do arquivo JSON: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private void salvarOcorrencias() throws IOException {
        try {
            objectMapper.writeValue(arquivo, ocorrencias);
        } catch (IOException e) {
            System.err.println("Erro ao salvar ocorrências no arquivo JSON: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void salvar(Ocorrencia ocorrencia) throws IOException, DadoInvalidoException {
        if (ocorrencia == null) {
            throw new DadoInvalidoException("Ocorrência não pode ser nula.");
        }
        if (ocorrencia.getId() == null || ocorrencia.getId().trim().isEmpty()) {
            throw new DadoInvalidoException("ID da ocorrência não pode ser nulo ou vazio.");
        }

        try {
            buscarPorId(ocorrencia.getId());
            throw new DadoInvalidoException("Ocorrência com ID " + ocorrencia.getId() + " já existe.");
        } catch (EntidadeNaoEncontradaException e) {
            ocorrencias.add(ocorrencia);
            salvarOcorrencias();
        }
    }

    @Override
    public Ocorrencia buscarPorId(String id) throws IOException, EntidadeNaoEncontradaException, DadoInvalidoException {
        if (id == null || id.trim().isEmpty()) {
            throw new DadoInvalidoException("ID da ocorrência para busca não pode ser nulo ou vazio.");
        }
        return ocorrencias.stream()
                .filter(o -> Objects.equals(o.getId(), id))
                .findFirst()
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Ocorrência com ID " + id + " não encontrada."));
    }

    @Override
    public List<Ocorrencia> listarTodos() throws IOException {
        return new ArrayList<>(ocorrencias);
    }

    @Override
    public void atualizar(Ocorrencia ocorrencia) throws IOException, EntidadeNaoEncontradaException, DadoInvalidoException {
        if (ocorrencia == null) {
            throw new DadoInvalidoException("Ocorrência não pode ser nula para atualização.");
        }
        if (ocorrencia.getId() == null || ocorrencia.getId().trim().isEmpty()) {
            throw new DadoInvalidoException("ID da ocorrência não pode ser nulo ou vazio para atualização.");
        }

        boolean encontrada = false;
        for (int i = 0; i < ocorrencias.size(); i++) {
            if (Objects.equals(ocorrencias.get(i).getId(), ocorrencia.getId())) {
                ocorrencias.set(i, ocorrencia);
                encontrada = true;
                break;
            }
        }
        if (!encontrada) {
            throw new EntidadeNaoEncontradaException("Ocorrência com ID " + ocorrencia.getId() + " não encontrada para atualização.");
        }
        salvarOcorrencias();
    }

    @Override
    public void deletar(String id) throws IOException, EntidadeNaoEncontradaException, DadoInvalidoException {
        if (id == null || id.trim().isEmpty()) {
            throw new DadoInvalidoException("ID da ocorrência para exclusão não pode ser nulo ou vazio.");
        }
        boolean removido = ocorrencias.removeIf(o -> Objects.equals(o.getId(), id));
        if (!removido) {
            throw new EntidadeNaoEncontradaException("Ocorrência com ID " + id + " não encontrada para exclusão.");
        }
        salvarOcorrencias();
    }

    @Override
    public void limpar() throws IOException {
        this.ocorrencias.clear();
        salvarOcorrencias();
    }
}