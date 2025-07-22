package br.com.escola.dados;

import br.com.escola.negocio.Ocorrencia;
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
            return objectMapper.readValue(arquivo, objectMapper.getTypeFactory().constructCollectionType(List.class, Ocorrencia.class));
        } catch (IOException e) {
            System.err.println("Erro ao carregar ocorrências do arquivo JSON: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private void salvarOcorrencias() {
        try {
            objectMapper.writeValue(arquivo, ocorrencias);
        } catch (IOException e) {
            System.err.println("Erro ao salvar ocorrências no arquivo JSON: " + e.getMessage());
        }
    }

    @Override
    public void salvar(Ocorrencia ocorrencia) {
        if (buscarPorId(ocorrencia.getId()).isEmpty()) {
            ocorrencias.add(ocorrencia);
            salvarOcorrencias();
        } else {
            System.out.println("Ocorrência com ID " + ocorrencia.getId() + " já existe e não foi salva.");
        }
    }

    @Override
    public Optional<Ocorrencia> buscarPorId(String id) {
        return ocorrencias.stream()
                .filter(o -> o.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Ocorrencia> listarTodos() {
        return new ArrayList<>(ocorrencias);
    }

    @Override
    public void atualizar(Ocorrencia ocorrencia) {
        Optional<Ocorrencia> existingOcorrenciaOpt = buscarPorId(ocorrencia.getId());
        if (existingOcorrenciaOpt.isPresent()) {
            Ocorrencia existingOcorrencia = existingOcorrenciaOpt.get();
            int index = ocorrencias.indexOf(existingOcorrencia);
            if (index != -1) {
                ocorrencias.set(index, ocorrencia);
                salvarOcorrencias();
            }
        } else {
            System.out.println("Ocorrência com ID " + ocorrencia.getId() + " não encontrada para atualização.");
        }
    }

    @Override
    public boolean deletar(String id) {
        boolean removido = ocorrencias.removeIf(o -> o.getId().equals(id));
        if (removido) {
            salvarOcorrencias();
        }
        return removido;
    }

    @Override
    public void limpar() {
        this.ocorrencias.clear();
        salvarOcorrencias();
    }
}