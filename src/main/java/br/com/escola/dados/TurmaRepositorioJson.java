package br.com.escola.dados;

import br.com.escola.excecoes.DadoInvalidoException;
import br.com.escola.excecoes.EntidadeNaoEncontradaException;
import br.com.escola.negocio.Turma;
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

public class TurmaRepositorioJson implements IRepositorio<Turma, String> {

    private final String NOME_ARQUIVO = "turmas.json";
    private List<Turma> turmas;
    private final ObjectMapper objectMapper;

    public TurmaRepositorioJson() {
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
                this.turmas = objectMapper.readValue(jsonContent, new TypeReference<List<Turma>>() {});
            } catch (IOException e) {
                System.err.println("Erro ao carregar dados do arquivo " + NOME_ARQUIVO + ": " + e.getMessage());
                this.turmas = new ArrayList<>();
            }
        } else {
            this.turmas = new ArrayList<>();
        }
    }

    private void salvarDados() throws IOException {
        try {
            objectMapper.writeValue(new File(NOME_ARQUIVO), turmas);
        } catch (IOException e) {
            System.err.println("Erro ao salvar dados no arquivo " + NOME_ARQUIVO + ": " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void salvar(Turma entidade) throws DadoInvalidoException, IOException {
        if (entidade == null || entidade.getCodigo() == null || entidade.getCodigo().trim().isEmpty()) {
            throw new DadoInvalidoException("Turma ou código da turma não pode ser nulo ou vazio.");
        }
        try {
            buscarPorId(entidade.getCodigo());
            throw new DadoInvalidoException("Já existe uma turma com o código: " + entidade.getCodigo());
        } catch (EntidadeNaoEncontradaException e) {
        }
        
        if (entidade.getNomeTurma() == null || entidade.getNomeTurma().trim().isEmpty()) {
            throw new DadoInvalidoException("Nome da turma é obrigatório.");
        }
        if (entidade.getSerieEscolar() == null || entidade.getSerieEscolar().getCodigoSerie() == null || entidade.getSerieEscolar().getCodigoSerie().trim().isEmpty()) {
            throw new DadoInvalidoException("Série escolar é obrigatória.");
        }
        if (entidade.getTurno() == null || entidade.getTurno().trim().isEmpty()) {
            throw new DadoInvalidoException("Turno da turma é obrigatório.");
        }

        this.turmas.add(entidade);
        salvarDados();
    }

    @Override
    public Turma buscarPorId(String id) throws IOException, EntidadeNaoEncontradaException, DadoInvalidoException {
        if (id == null || id.trim().isEmpty()) {
            throw new DadoInvalidoException("ID para busca não pode ser nulo ou vazio.");
        }
        return turmas.stream()
                .filter(t -> t.getCodigo().equals(id))
                .findFirst()
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Turma com código " + id + " não encontrada."));
    }

    @Override
    public void atualizar(Turma entidade) throws IOException, EntidadeNaoEncontradaException, DadoInvalidoException {
        if (entidade == null || entidade.getCodigo() == null || entidade.getCodigo().trim().isEmpty()) {
            throw new DadoInvalidoException("Turma ou código da turma não pode ser nulo para atualização.");
        }
        if (entidade.getNomeTurma() == null || entidade.getNomeTurma().trim().isEmpty()) {
            throw new DadoInvalidoException("Nome da turma é obrigatório para atualização.");
        }
        if (entidade.getSerieEscolar() == null || entidade.getSerieEscolar().getCodigoSerie() == null || entidade.getSerieEscolar().getCodigoSerie().trim().isEmpty()) {
            throw new DadoInvalidoException("Série escolar é obrigatória para atualização.");
        }
        if (entidade.getTurno() == null || entidade.getTurno().trim().isEmpty()) {
            throw new DadoInvalidoException("Turno da turma é obrigatório para atualização.");
        }

        boolean encontrada = false;
        for (int i = 0; i < turmas.size(); i++) {
            if (turmas.get(i).getCodigo().equals(entidade.getCodigo())) {
                turmas.set(i, entidade);
                encontrada = true;
                break;
            }
        }
        
        if (!encontrada) {
            throw new EntidadeNaoEncontradaException("Turma com código " + entidade.getCodigo() + " não encontrada para atualização.");
        }
        salvarDados();
    }

    @Override
    public void deletar(String id) throws IOException, EntidadeNaoEncontradaException, DadoInvalidoException {
        if (id == null || id.trim().isEmpty()) {
            throw new DadoInvalidoException("ID para deleção não pode ser nulo ou vazio.");
        }
        boolean removido = turmas.removeIf(t -> t.getCodigo().equals(id));
        if (!removido) {
            throw new EntidadeNaoEncontradaException("Turma com código " + id + " não encontrada para exclusão.");
        }
        salvarDados();
    }

    @Override
    public List<Turma> listarTodos() throws IOException {
        return new ArrayList<>(turmas);
    }

    @Override
    public void limpar() throws IOException {
        this.turmas.clear();
        salvarDados();
    }
}