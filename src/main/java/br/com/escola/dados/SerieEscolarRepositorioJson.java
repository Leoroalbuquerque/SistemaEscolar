package br.com.escola.dados;

import br.com.escola.negocio.SerieEscolar;
import br.com.escola.excecoes.EntidadeNaoEncontradaException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SerieEscolarRepositorioJson implements IRepositorio<SerieEscolar, String> {

    private static final String NOME_ARQUIVO = "seriesEscolares.json";
    private List<SerieEscolar> seriesEscolares;
    private final Gson gson;

    public SerieEscolarRepositorioJson() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        carregarDados();
        if (seriesEscolares.isEmpty()) {
            inicializarSeriesPadrao();
        }
    }

    private void carregarDados() {
        try (Reader reader = new FileReader(NOME_ARQUIVO)) {
            Type tipoListaSerieEscolar = new TypeToken<ArrayList<SerieEscolar>>() {}.getType();
            seriesEscolares = gson.fromJson(reader, tipoListaSerieEscolar);
            if (seriesEscolares == null) {
                seriesEscolares = new ArrayList<>();
            }
        } catch (FileNotFoundException e) {
            seriesEscolares = new ArrayList<>();
        } catch (IOException e) {
            System.err.println("Erro ao carregar dados do arquivo JSON: " + e.getMessage());
            seriesEscolares = new ArrayList<>();
        }
    }

    private void salvarDados() throws IOException {
        try (Writer writer = new FileWriter(NOME_ARQUIVO)) {
            gson.toJson(seriesEscolares, writer);
        }
    }

    private void inicializarSeriesPadrao() {
        List<String> nomesSeries = Arrays.asList(
            "INFANTIL 1", "INFANTIL 2", "INFANTIL 3", "INFANTIL 4", "INFANTIL 5",
            "1º ANO FUNDAMENTAL", "2º ANO FUNDAMENTAL", "3º ANO FUNDAMENTAL", "4º ANO FUNDAMENTAL", "5º ANO FUNDAMENTAL",
            "6º ANO FUNDAMENTAL", "7º ANO FUNDAMENTAL", "8º ANO FUNDAMENTAL", "9º ANO FUNDAMENTAL",
            "1º ANO MÉDIO", "2º ANO MÉDIO", "3º ANO MÉDIO"
        );

        int codigoCounter = 1;
        for (String nome : nomesSeries) {
            String codigo = "S" + String.format("%02d", codigoCounter++);
            SerieEscolar.NivelEscolar nivel;
            int ordem;

            if (nome.startsWith("INFANTIL")) {
                nivel = SerieEscolar.NivelEscolar.INFANTIL;
                ordem = Integer.parseInt(nome.substring(9).trim());
            } else if (nome.contains("FUNDAMENTAL")) {
                int ano = Integer.parseInt(nome.substring(0, 1));
                if (ano >= 1 && ano <= 5) {
                    nivel = SerieEscolar.NivelEscolar.FUNDAMENTAL_1;
                } else {
                    nivel = SerieEscolar.NivelEscolar.FUNDAMENTAL_2;
                }
                ordem = ano;
            } else if (nome.contains("MÉDIO")) {
                nivel = SerieEscolar.NivelEscolar.MEDIO;
                ordem = Integer.parseInt(nome.substring(0, 1));
            } else {
                nivel = null;
                ordem = 0;
            }

            SerieEscolar serie = new SerieEscolar(codigo, nome, nivel, ordem);
            this.seriesEscolares.add(serie);
        }
        try {
            salvarDados();
        } catch (IOException e) {
            System.err.println("Erro ao salvar dados iniciais: " + e.getMessage());
        }
    }

    @Override
    public void salvar(SerieEscolar serieEscolar) throws IOException {
        this.seriesEscolares.add(serieEscolar);
        salvarDados();
    }

    @Override
    public SerieEscolar buscarPorId(String codigoSerie) throws IOException, EntidadeNaoEncontradaException {
        return seriesEscolares.stream()
                .filter(s -> s.getCodigoSerie().equals(codigoSerie))
                .findFirst()
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Série escolar não encontrada: " + codigoSerie));
    }

    @Override
    public List<SerieEscolar> listarTodos() throws IOException {
        return new ArrayList<>(seriesEscolares);
    }

    @Override
    public void atualizar(SerieEscolar serieAtualizada) throws IOException, EntidadeNaoEncontradaException {
        boolean encontrada = false;
        for (int i = 0; i < seriesEscolares.size(); i++) {
            if (seriesEscolares.get(i).getCodigoSerie().equals(serieAtualizada.getCodigoSerie())) {
                seriesEscolares.set(i, serieAtualizada);
                encontrada = true;
                break;
            }
        }
        if (!encontrada) {
            throw new EntidadeNaoEncontradaException("Série escolar não encontrada para atualização: " + serieAtualizada.getCodigoSerie());
        }
        salvarDados();
    }

    @Override
    public void deletar(String codigoSerie) throws IOException, EntidadeNaoEncontradaException {
        boolean removido = seriesEscolares.removeIf(s -> s.getCodigoSerie().equals(codigoSerie));
        if (!removido) {
            throw new EntidadeNaoEncontradaException("Série escolar não encontrada para exclusão: " + codigoSerie);
        }
        salvarDados();
    }

    @Override
    public void limpar() throws IOException {
        seriesEscolares.clear();
        salvarDados();
    }
}