package br.com.escola.dados;

import br.com.escola.negocio.Disciplina;
import br.com.escola.excecoes.DadoInvalidoException;
import br.com.escola.excecoes.EntidadeNaoEncontradaException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DisciplinaRepositorioJson implements IRepositorio<Disciplina, String> {

    private static final String NOME_ARQUIVO = "disciplinas.json";
    private final ObjectMapper objectMapper;
    private List<Disciplina> disciplinas;

    public DisciplinaRepositorioJson() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.disciplinas = new ArrayList<>();
        carregarDisciplinasDoArquivo();
    }

    private void carregarDisciplinasDoArquivo() {
        File arquivo = new File(NOME_ARQUIVO);
        if (arquivo.exists() && arquivo.length() > 0) {
            try {
                this.disciplinas = objectMapper.readValue(arquivo, new TypeReference<List<Disciplina>>() {});
            } catch (IOException e) {
                System.err.println("Erro ao carregar disciplinas do arquivo JSON. Detalhes: " + e.getMessage());
                this.disciplinas = new ArrayList<>();
            }
        }
    }

    private void salvarDisciplinasNoArquivo() throws IOException {
        try {
            objectMapper.writeValue(new File(NOME_ARQUIVO), this.disciplinas);
        } catch (IOException e) {
            System.err.println("Erro ao salvar disciplinas no arquivo JSON: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void salvar(Disciplina entidade) throws DadoInvalidoException, IOException {
        if (entidade == null || entidade.getCodigo() == null || entidade.getCodigo().trim().isEmpty()) {
            throw new DadoInvalidoException("Erro: Tentativa de salvar disciplina nula ou com código vazio/nulo.");
        }
        if (entidade.getNome() == null || entidade.getNome().trim().isEmpty()) {
            throw new DadoInvalidoException("Erro: Nome da disciplina é obrigatório para salvar.");
        }

        try {
            buscarPorId(entidade.getCodigo());
            throw new DadoInvalidoException("Já existe uma disciplina cadastrada com o código: " + entidade.getCodigo() + ". Use o método 'atualizar' para modificar.");
        } catch (EntidadeNaoEncontradaException e) {
            this.disciplinas.add(entidade);
            salvarDisciplinasNoArquivo();
        }
    }

    @Override
    public Disciplina buscarPorId(String chave) throws IOException, EntidadeNaoEncontradaException, DadoInvalidoException {
        if (chave == null || chave.trim().isEmpty()) {
            throw new DadoInvalidoException("Código para busca não pode ser nulo ou vazio.");
        }
        return this.disciplinas.stream()
                .filter(d -> d.getCodigo().equals(chave))
                .findFirst()
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Disciplina com código " + chave + " não encontrada."));
    }

    @Override
    public void atualizar(Disciplina entidade) throws IOException, EntidadeNaoEncontradaException, DadoInvalidoException {
        if (entidade == null || entidade.getCodigo() == null || entidade.getCodigo().trim().isEmpty()) {
            throw new DadoInvalidoException("Não é possível atualizar: Disciplina ou código inválido.");
        }
        if (entidade.getNome() == null || entidade.getNome().trim().isEmpty()) {
            throw new DadoInvalidoException("Nome da disciplina é obrigatório para atualização.");
        }

        boolean encontrada = false;
        for (int i = 0; i < disciplinas.size(); i++) {
            if (disciplinas.get(i).getCodigo().equals(entidade.getCodigo())) {
                disciplinas.set(i, entidade);
                encontrada = true;
                break;
            }
        }

        if (!encontrada) {
            throw new EntidadeNaoEncontradaException("Disciplina com código " + entidade.getCodigo() + " não encontrada para atualização.");
        }
        salvarDisciplinasNoArquivo();
    }

    @Override
    public void deletar(String chave) throws IOException, EntidadeNaoEncontradaException, DadoInvalidoException {
        if (chave == null || chave.trim().isEmpty()) {
            throw new DadoInvalidoException("Código para deleção não pode ser nulo ou vazio.");
        }
        boolean removido = this.disciplinas.removeIf(d -> d.getCodigo().equals(chave));
        if (!removido) {
            throw new EntidadeNaoEncontradaException("Disciplina com código " + chave + " não encontrada para exclusão.");
        }
        salvarDisciplinasNoArquivo();
    }

    @Override
    public List<Disciplina> listarTodos() throws IOException {
        return new ArrayList<>(this.disciplinas);
    }

    @Override
    public void limpar() throws IOException {
        this.disciplinas.clear();
        salvarDisciplinasNoArquivo();
    }
}