package br.com.escola.dados;

import br.com.escola.negocio.Disciplina;
import br.com.escola.excecoes.DadoInvalidoException;
import br.com.escola.excecoes.EntidadeNaoEncontradaException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DisciplinaRepositorioJson implements IRepositorio<Disciplina, String> {

    private static final String NOME_ARQUIVO = "disciplinas.json";
    private final ObjectMapper objectMapper;
    private List<Disciplina> disciplinas;

    public DisciplinaRepositorioJson() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        this.disciplinas = new ArrayList<>();
        carregarDisciplinasDoArquivo();
    }

    private void carregarDisciplinasDoArquivo() {
        File arquivo = new File(NOME_ARQUIVO);
        if (arquivo.exists() && arquivo.length() > 0) {
            try {
                this.disciplinas = objectMapper.readValue(arquivo, new TypeReference<List<Disciplina>>() {});
                System.out.println("Disciplinas carregadas do arquivo: " + NOME_ARQUIVO);
            } catch (IOException e) {
                System.err.println("Erro ao carregar disciplinas do arquivo JSON. Criando um novo arquivo se o conteúdo estiver inválido. Detalhes: " + e.getMessage());
                this.disciplinas = new ArrayList<>();
            }
        } else {
            System.out.println("Arquivo " + NOME_ARQUIVO + " não encontrado ou vazio. Iniciando com lista vazia de disciplinas.");
        }
    }

    private void salvarDisciplinasNoArquivo() {
        try {
            objectMapper.writeValue(new File(NOME_ARQUIVO), this.disciplinas);
            System.out.println("Disciplinas salvas no arquivo: " + NOME_ARQUIVO);
        } catch (IOException e) {
            System.err.println("Erro ao salvar disciplinas no arquivo JSON: " + e.getMessage());
        }
    }

    @Override
    public void adicionar(Disciplina entidade) throws DadoInvalidoException {
        if (entidade == null || entidade.getCodigo() == null || entidade.getCodigo().trim().isEmpty()) {
            throw new DadoInvalidoException("Erro: Tentativa de adicionar disciplina nula ou com código vazio/nulo.");
        }
        if (entidade.getNome() == null || entidade.getNome().trim().isEmpty()) {
            throw new DadoInvalidoException("Erro: Nome da disciplina é obrigatório para adição.");
        }

        boolean existe = this.disciplinas.stream()
                .anyMatch(d -> d != null && d.getCodigo() != null && d.getCodigo().equals(entidade.getCodigo()));
        if (existe) {
            throw new DadoInvalidoException("Já existe uma disciplina cadastrada com o código: " + entidade.getCodigo());
        }

        this.disciplinas.add(entidade);
        salvarDisciplinasNoArquivo();
    }

    @Override
    public Optional<Disciplina> buscarPorId(String chave) {
        if (chave == null || chave.trim().isEmpty()) {
            return Optional.empty();
        }
        return this.disciplinas.stream()
                .filter(d -> d != null && d.getCodigo() != null && d.getCodigo().equals(chave))
                .findFirst();
    }

    @Override
    public void atualizar(Disciplina entidade) throws DadoInvalidoException, EntidadeNaoEncontradaException {
        if (entidade == null || entidade.getCodigo() == null || entidade.getCodigo().trim().isEmpty()) {
            throw new DadoInvalidoException("Não é possível atualizar: Disciplina ou código inválido.");
        }
        if (entidade.getNome() == null || entidade.getNome().trim().isEmpty()) {
            throw new DadoInvalidoException("Nome da disciplina é obrigatório para atualização.");
        }

        Optional<Disciplina> disciplinaExistenteOpt = buscarPorId(entidade.getCodigo());
        if (disciplinaExistenteOpt.isEmpty()) {
            throw new EntidadeNaoEncontradaException("Disciplina com código " + entidade.getCodigo() + " não encontrada para atualização.");
        }

        this.disciplinas.removeIf(d -> d != null && d.getCodigo() != null && d.getCodigo().equals(entidade.getCodigo()));
        this.disciplinas.add(entidade);
        salvarDisciplinasNoArquivo();
    }

    @Override
    public boolean deletar(String chave) throws EntidadeNaoEncontradaException, DadoInvalidoException {
        if (chave == null || chave.trim().isEmpty()) {
            throw new DadoInvalidoException("Código para deleção não pode ser nulo ou vazio.");
        }
        boolean removido = this.disciplinas.removeIf(d -> d != null && d.getCodigo() != null && d.getCodigo().equals(chave));
        if (removido) {
            salvarDisciplinasNoArquivo();
            return true;
        } else {
            throw new EntidadeNaoEncontradaException("Disciplina com código " + chave + " não encontrada para deleção no JSON.");
        }
    }

    @Override
    public List<Disciplina> listarTodos() {
        return new ArrayList<>(this.disciplinas);
    }

    public void limpar() {
        this.disciplinas.clear();
        salvarDisciplinasNoArquivo();
        System.out.println("DEBUG: Arquivo " + NOME_ARQUIVO + " limpo.");
    }
}
