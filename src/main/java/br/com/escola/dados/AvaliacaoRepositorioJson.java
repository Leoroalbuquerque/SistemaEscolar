package br.com.escola.dados;

import br.com.escola.excecoes.DadoInvalidoException;
import br.com.escola.excecoes.EntidadeNaoEncontradaException;
import br.com.escola.negocio.Avaliacao;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AvaliacaoRepositorioJson implements IRepositorio<Avaliacao, String> {

    private final String CAMINHO_ARQUIVO = "avaliacoes.json";
    private ObjectMapper objectMapper;
    private List<Avaliacao> avaliacoes;

    public AvaliacaoRepositorioJson() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        avaliacoes = carregarDados();
    }

    private List<Avaliacao> carregarDados() {
        File arquivo = new File(CAMINHO_ARQUIVO);
        if (arquivo.exists() && arquivo.length() > 0) {
            try {
                return objectMapper.readValue(arquivo, objectMapper.getTypeFactory().constructCollectionType(List.class, Avaliacao.class));
            } catch (IOException e) {
                System.err.println("Erro ao carregar dados de avaliações: " + e.getMessage());
                return new ArrayList<>();
            }
        }
        return new ArrayList<>();
    }

    private void salvarDados() throws IOException {
        try {
            objectMapper.writeValue(new File(CAMINHO_ARQUIVO), avaliacoes);
        } catch (IOException e) {
            System.err.println("Erro ao salvar dados de avaliações: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void salvar(Avaliacao avaliacao) throws DadoInvalidoException, IOException {
        if (avaliacao == null) {
            throw new DadoInvalidoException("Avaliação não pode ser nula.");
        }
        try {
            if (buscarPorId(avaliacao.getId()) != null) {
                throw new DadoInvalidoException("Avaliação com ID " + avaliacao.getId() + " já existe.");
            }
        } catch (EntidadeNaoEncontradaException e) {
        }
        avaliacoes.add(avaliacao);
        salvarDados();
    }

    @Override
    public Avaliacao buscarPorId(String id) throws IOException, EntidadeNaoEncontradaException {
        return avaliacoes.stream()
                .filter(a -> a.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Avaliação com ID " + id + " não encontrada."));
    }

    @Override
    public List<Avaliacao> listarTodos() throws IOException {
        return new ArrayList<>(avaliacoes);
    }

    @Override
    public void atualizar(Avaliacao avaliacao) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        if (avaliacao == null) {
            throw new DadoInvalidoException("Avaliação não pode ser nula para atualização.");
        }
        boolean encontrada = false;
        for (int i = 0; i < avaliacoes.size(); i++) {
            if (avaliacoes.get(i).getId().equals(avaliacao.getId())) {
                avaliacoes.set(i, avaliacao);
                encontrada = true;
                break;
            }
        }
        if (!encontrada) {
            throw new EntidadeNaoEncontradaException("Avaliação com ID " + avaliacao.getId() + " não encontrada para atualização.");
        }
        salvarDados();
    }

    @Override
    public void deletar(String id) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        if (id == null || id.isEmpty()) {
            throw new DadoInvalidoException("ID da avaliação não pode ser nulo ou vazio.");
        }
        boolean removido = avaliacoes.removeIf(a -> a.getId().equals(id));
        if (!removido) {
            throw new EntidadeNaoEncontradaException("Avaliação com ID " + id + " não encontrada para exclusão.");
        }
        salvarDados();
    }

    @Override
    public void limpar() throws IOException {
        avaliacoes.clear();
        salvarDados();
    }
}