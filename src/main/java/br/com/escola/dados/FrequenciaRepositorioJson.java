package br.com.escola.dados;

import br.com.escola.negocio.Frequencia;
import br.com.escola.util.LocalDateAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FrequenciaRepositorioJson {

    private static final String NOME_ARQUIVO = "frequencias.json";
    private List<Frequencia> frequencias;
    private final Gson gson;

    public FrequenciaRepositorioJson() {
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .setPrettyPrinting()
                .create();
        carregarDados();
    }

    private void carregarDados() {
        File arquivo = new File(NOME_ARQUIVO);
        if (arquivo.exists() && arquivo.length() > 0) {
            try (FileReader reader = new FileReader(NOME_ARQUIVO)) {
                Type tipoListaFrequencia = new TypeToken<ArrayList<Frequencia>>() {}.getType();
                frequencias = gson.fromJson(reader, tipoListaFrequencia);
                if (frequencias == null) {
                    frequencias = new ArrayList<>();
                }
            } catch (IOException e) {
                frequencias = new ArrayList<>();
                System.err.println("Erro ao carregar frequências do arquivo JSON (" + NOME_ARQUIVO + "). Iniciando com lista vazia.");
            }
        } else {
            frequencias = new ArrayList<>();
            salvarDadosSeguro(); // Tenta criar o arquivo vazio se não existir
        }
    }

    private void salvarDados() throws IOException {
        try (FileWriter writer = new FileWriter(NOME_ARQUIVO)) {
            gson.toJson(frequencias, writer);
        } catch (IOException e) {
            throw e; // Relança a exceção para que a camada superior seja notificada
        }
    }

    private void salvarDadosSeguro() {
        try (FileWriter writer = new FileWriter(NOME_ARQUIVO)) {
            gson.toJson(frequencias, writer);
        } catch (IOException e) {
            System.err.println("Erro ao salvar dados inicialmente no arquivo " + NOME_ARQUIVO + ": " + e.getMessage());
        }
    }

    public void salvar(Frequencia frequencia) throws IOException {
        frequencias.add(frequencia);
        salvarDados();
    }

    public Frequencia buscar(String matriculaAluno, String codigoDisciplina, LocalDate data) {
        return frequencias.stream()
                .filter(f -> f.getAluno() != null && f.getAluno().getMatricula() != null &&
                        f.getAluno().getMatricula().equals(matriculaAluno) &&
                        f.getDisciplina() != null && f.getDisciplina().getCodigo() != null &&
                        f.getDisciplina().getCodigo().equals(codigoDisciplina) &&
                        f.getData().equals(data))
                .findFirst()
                .orElse(null);
    }

    public List<Frequencia> listarTodos() {
        return new ArrayList<>(frequencias);
    }

    public List<Frequencia> buscarPorAluno(String matriculaAluno) {
        return frequencias.stream()
                .filter(f -> f.getAluno() != null && f.getAluno().getMatricula() != null &&
                        f.getAluno().getMatricula().equals(matriculaAluno))
                .collect(Collectors.toList());
    }

    public List<Frequencia> buscarPorDisciplina(String codigoDisciplina) {
        return frequencias.stream()
                .filter(f -> f.getDisciplina() != null && f.getDisciplina().getCodigo() != null &&
                        f.getDisciplina().getCodigo().equals(codigoDisciplina))
                .collect(Collectors.toList());
    }

    public List<Frequencia> buscarPorAlunoEDisciplina(String matriculaAluno, String codigoDisciplina) {
        return frequencias.stream()
                .filter(f -> f.getAluno() != null && f.getAluno().getMatricula() != null &&
                        f.getAluno().getMatricula().equals(matriculaAluno) &&
                        f.getDisciplina() != null && f.getDisciplina().getCodigo() != null &&
                        f.getDisciplina().getCodigo().equals(codigoDisciplina))
                .collect(Collectors.toList());
    }

    public void atualizar(Frequencia frequenciaAtualizada) throws IOException {
        boolean encontrada = false;
        for (int i = 0; i < frequencias.size(); i++) {
            Frequencia f = frequencias.get(i);
            if (f.getAluno().getMatricula().equals(frequenciaAtualizada.getAluno().getMatricula()) &&
                f.getDisciplina().getCodigo().equals(frequenciaAtualizada.getDisciplina().getCodigo()) &&
                f.getData().equals(frequenciaAtualizada.getData())) {
                frequencias.set(i, frequenciaAtualizada);
                encontrada = true;
                break;
            }
        }
        if (encontrada) {
            salvarDados();
        } else {
            // Se não encontrou para atualizar, você pode lançar uma exceção ou adicionar
            // a nova frequência, dependendo da sua regra de negócio.
            // Por enquanto, vamos apenas não fazer nada e assumir que o serviço superior irá lidar.
        }
    }

    public boolean deletar(String matriculaAluno, String codigoDisciplina, LocalDate data) throws IOException {
        boolean removido = frequencias.removeIf(f -> f.getAluno().getMatricula().equals(matriculaAluno) &&
                                                     f.getDisciplina().getCodigo().equals(codigoDisciplina) &&
                                                     f.getData().equals(data));
        if (removido) {
            salvarDados();
        }
        return removido;
    }

    public void limpar() throws IOException {
        frequencias.clear();
        salvarDados();
    }
}