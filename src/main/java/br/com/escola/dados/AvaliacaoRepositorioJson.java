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
import java.util.Optional;
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

    private void salvarDados() {
        try {
            objectMapper.writeValue(new File(CAMINHO_ARQUIVO), avaliacoes);
        } catch (IOException e) {
            System.err.println("Erro ao salvar dados de avaliações: " + e.getMessage());
        }
    }

    @Override
    public void salvar(Avaliacao avaliacao) throws DadoInvalidoException {
        if (avaliacao == null) {
            throw new DadoInvalidoException("Avaliação não pode ser nula.");
        }
        if (buscarPorId(avaliacao.getId()).isPresent()) {
            throw new DadoInvalidoException("Avaliação com ID " + avaliacao.getId() + " já existe.");
        }
        avaliacoes.add(avaliacao);
        salvarDados();
    }

    @Override
    public Optional<Avaliacao> buscarPorId(String id) {
        return avaliacoes.stream()
                .filter(a -> a.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Avaliacao> listarTodos() {
        return new ArrayList<>(avaliacoes);
    }

    @Override
    public void atualizar(Avaliacao avaliacao) throws EntidadeNaoEncontradaException, DadoInvalidoException {
        if (avaliacao == null) {
            throw new DadoInvalidoException("Avaliação não pode ser nula.");
        }
        Optional<Avaliacao> existenteOpt = buscarPorId(avaliacao.getId());
        if (existenteOpt.isEmpty()) {
            throw new EntidadeNaoEncontradaException("Avaliação com ID " + avaliacao.getId() + " não encontrada para atualização.");
        }
        avaliacoes = avaliacoes.stream()
                .map(a -> a.getId().equals(avaliacao.getId()) ? avaliacao : a)
                .collect(Collectors.toList());
        salvarDados();
    }

    @Override
    public boolean deletar(String id) throws EntidadeNaoEncontradaException, DadoInvalidoException {
        if (id == null || id.isEmpty()) {
            throw new DadoInvalidoException("ID da avaliação não pode ser nulo ou vazio.");
        }
        boolean removido = avaliacoes.removeIf(a -> a.getId().equals(id));
        if (!removido) {
            throw new EntidadeNaoEncontradaException("Avaliação com ID " + id + " não encontrada para exclusão.");
        }
        salvarDados();
        return removido;
    }

    @Override
    public void limpar() {
        avaliacoes.clear();
        salvarDados();
    }
}