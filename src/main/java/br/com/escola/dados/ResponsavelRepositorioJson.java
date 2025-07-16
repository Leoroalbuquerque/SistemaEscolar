package br.com.escola.dados;

import br.com.escola.negocio.Responsavel;
import br.com.escola.excecoes.DadoInvalidoException;
import br.com.escola.excecoes.EntidadeNaoEncontradaException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// CORREÇÃO AQUI: Implementando IRepositorio (com 'I' maiúsculo)
public class ResponsavelRepositorioJson implements IRepositorio<Responsavel, String> {

    private static final String NOME_ARQUIVO = "responsaveis.json";
    private List<Responsavel> responsaveis; // Variável correta é "responsaveis" (plural)
    private final Gson gson;

    public ResponsavelRepositorioJson() {
        gson = new GsonBuilder().setPrettyPrinting().create();
        // CORREÇÃO AQUI: Usando a variável "responsaveis" (plural)
        responsaveis = new ArrayList<>();
        carregarDados(); // Carrega os dados ao inicializar
    }

    private void carregarDados() {
        try (FileReader reader = new FileReader(NOME_ARQUIVO)) {
            Type tipoListaResponsavel = new TypeToken<ArrayList<Responsavel>>() {}.getType();
            responsaveis = gson.fromJson(reader, tipoListaResponsavel);
            if (responsaveis == null) {
                responsaveis = new ArrayList<>();
            }
        } catch (IOException e) {
            // Se o arquivo não existe ou há um erro de leitura, inicia com uma lista vazia.
            // Isso é o comportamento desejado para um novo início.
            responsaveis = new ArrayList<>();
        }
    }

    private void salvarDados() throws IOException {
        try (FileWriter writer = new FileWriter(NOME_ARQUIVO)) {
            gson.toJson(responsaveis, writer);
        }
    }

    @Override
    public void adicionar(Responsavel responsavel) throws DadoInvalidoException {
        if (buscarPorId(responsavel.getCpfResponsavel()).isPresent()) {
            throw new DadoInvalidoException("Responsável com CPF " + responsavel.getCpfResponsavel() + " já existe.");
        }
        this.responsaveis.add(responsavel);
        try {
            salvarDados();
        } catch (IOException e) {
            throw new DadoInvalidoException("Erro ao salvar o responsável: " + e.getMessage());
        }
    }

    @Override
    public Optional<Responsavel> buscarPorId(String cpf) {
        return responsaveis.stream()
                .filter(r -> r.getCpfResponsavel().equals(cpf))
                .findFirst();
    }

    @Override
    public void atualizar(Responsavel responsavelAtualizado) throws EntidadeNaoEncontradaException, DadoInvalidoException {
        Optional<Responsavel> responsavelExistenteOpt = buscarPorId(responsavelAtualizado.getCpfResponsavel());
        if (responsavelExistenteOpt.isEmpty()) {
            throw new EntidadeNaoEncontradaException("Responsável com CPF " + responsavelAtualizado.getCpfResponsavel() + " não encontrado para atualização.");
        }
        
        Responsavel responsavelExistente = responsavelExistenteOpt.get();
        int index = responsaveis.indexOf(responsavelExistente);
        if (index != -1) {
            responsaveis.set(index, responsavelAtualizado);
        }
        
        try {
            salvarDados();
        } catch (IOException e) {
            throw new DadoInvalidoException("Erro ao salvar o responsável atualizado: " + e.getMessage());
        }
    }

    @Override
    public boolean deletar(String cpf) throws EntidadeNaoEncontradaException, DadoInvalidoException {
        Optional<Responsavel> responsavelParaDeletarOpt = buscarPorId(cpf);
        if (responsavelParaDeletarOpt.isEmpty()) {
            throw new EntidadeNaoEncontradaException("Responsável com CPF " + cpf + " não encontrado para exclusão.");
        }
        boolean removido = responsaveis.remove(responsavelParaDeletarOpt.get());
        if (removido) {
            try {
                salvarDados();
            } catch (IOException e) {
                throw new DadoInvalidoException("Erro ao salvar após exclusão do responsável: " + e.getMessage());
            }
        }
        return removido;
    }

    @Override
    public List<Responsavel> listarTodos() {
        return new ArrayList<>(responsaveis);
    }

    /**
     * Limpa todos os dados de responsáveis do repositório e persiste a lista vazia no arquivo JSON.
     */
    public void limpar() {
        this.responsaveis.clear(); // Limpa a lista em memória
        try {
            salvarDados(); // Salva a lista vazia no arquivo
            System.out.println("DEBUG: Arquivo " + NOME_ARQUIVO + " limpo.");
        } catch (IOException e) {
            System.err.println("Erro ao limpar o arquivo " + NOME_ARQUIVO + ": " + e.getMessage());
        }
    }
}