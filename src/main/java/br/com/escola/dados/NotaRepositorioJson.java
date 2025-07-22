package br.com.escola.dados;

import br.com.escola.negocio.Nota;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class NotaRepositorioJson {

    private static final String NOME_ARQUIVO = "notas.json";
    private final File arquivo;
    private final ObjectMapper objectMapper;
    private List<Nota> notas;

    public NotaRepositorioJson() {
        this.arquivo = new File(NOME_ARQUIVO);
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.notas = carregarNotasDoArquivo();
    }

    private List<Nota> carregarNotasDoArquivo() {
        if (!arquivo.exists() || arquivo.length() == 0) {
            return new ArrayList<>();
        }
        try {
            return objectMapper.readValue(arquivo, objectMapper.getTypeFactory().constructCollectionType(List.class, Nota.class));
        } catch (IOException e) {
            System.err.println("Erro ao carregar notas do arquivo: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private void salvarNotasNoArquivo() {
        try {
            objectMapper.writeValue(arquivo, notas);
        } catch (IOException e) {
            System.err.println("Erro ao salvar notas no arquivo: " + e.getMessage());
        }
    }

    public void adicionar(Nota nota) {
        if (nota == null || buscar(nota.getAluno().getMatricula(), nota.getDisciplina().getCodigo(), nota.getTipoAvaliacao(), nota.getDataLancamento()) != null) {
            return;
        }
        this.notas.add(nota);
        salvarNotasNoArquivo();
    }

    public Nota buscar(String matriculaAluno, String codigoDisciplina, String tipoAvaliacao, java.util.Date dataLancamento) {
        return this.notas.stream()
                .filter(n -> Objects.equals(n.getAluno().getMatricula(), matriculaAluno) &&
                              Objects.equals(n.getDisciplina().getCodigo(), codigoDisciplina) &&
                              Objects.equals(n.getTipoAvaliacao(), tipoAvaliacao) &&
                              Objects.equals(n.getDataLancamento(), dataLancamento))
                .findFirst()
                .orElse(null);
    }

    public void atualizar(Nota notaAtualizada) {
        if (notaAtualizada == null) {
            return;
        }
        for (int i = 0; i < notas.size(); i++) {
            if (notas.get(i).equals(notaAtualizada)) {
                notas.set(i, notaAtualizada);
                salvarNotasNoArquivo();
                return;
            }
        }
    }

    public boolean deletar(String matriculaAluno, String codigoDisciplina, String tipoAvaliacao, java.util.Date dataLancamento) {
        boolean removido = this.notas.removeIf(n -> Objects.equals(n.getAluno().getMatricula(), matriculaAluno) &&
                                                     Objects.equals(n.getDisciplina().getCodigo(), codigoDisciplina) &&
                                                     Objects.equals(n.getTipoAvaliacao(), tipoAvaliacao) &&
                                                     Objects.equals(n.getDataLancamento(), dataLancamento));
        if (removido) {
            salvarNotasNoArquivo();
        }
        return removido;
    }

    public List<Nota> listarTodos() {
        return Collections.unmodifiableList(this.notas);
    }

    public void limpar() {
        this.notas.clear();
        salvarNotasNoArquivo();
    }

    public List<Nota> buscarNotasPorAluno(String matriculaAluno) {
        return this.notas.stream()
                .filter(n -> Objects.equals(n.getAluno().getMatricula(), matriculaAluno))
                .collect(Collectors.toList());
    }

    public List<Nota> buscarNotasPorDisciplina(String codigoDisciplina) {
        return this.notas.stream()
                .filter(n -> Objects.equals(n.getDisciplina().getCodigo(), codigoDisciplina))
                .collect(Collectors.toList());
    }

    public List<Nota> buscarNotasPorAlunoEDisciplina(String matriculaAluno, String codigoDisciplina) {
        return this.notas.stream()
                .filter(n -> Objects.equals(n.getAluno().getMatricula(), matriculaAluno) &&
                              Objects.equals(n.getDisciplina().getCodigo(), codigoDisciplina))
                .collect(Collectors.toList());
    }
}