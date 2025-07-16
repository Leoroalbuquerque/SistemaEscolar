package br.com.escola.dados;

import br.com.escola.negocio.Funcionario;
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

public class FuncionarioRepositorioJson implements IRepositorio<Funcionario, String> {

    private static final String NOME_ARQUIVO = "funcionarios.json";
    private final ObjectMapper objectMapper;
    private List<Funcionario> funcionarios;

    public FuncionarioRepositorioJson() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        this.funcionarios = new ArrayList<>(); // Inicializa para garantir que não seja nulo antes de carregar
        carregarFuncionariosDoArquivo();
    }

    private void carregarFuncionariosDoArquivo() {
        File arquivo = new File(NOME_ARQUIVO);
        if (arquivo.exists() && arquivo.length() > 0) {
            try {
                this.funcionarios = objectMapper.readValue(arquivo, new TypeReference<List<Funcionario>>() {});
            } catch (IOException e) {
                System.err.println("Erro ao carregar funcionários do arquivo JSON. Detalhes: " + e.getMessage());
                this.funcionarios = new ArrayList<>(); // Inicia lista vazia em caso de erro de leitura
            }
        } else {
            System.out.println("Arquivo " + NOME_ARQUIVO + " não encontrado ou vazio. Iniciando com lista vazia de funcionários.");
        }
    }

    private void salvarFuncionariosNoArquivo() {
        try {
            objectMapper.writeValue(new File(NOME_ARQUIVO), this.funcionarios);
            System.out.println("Funcionários salvos no arquivo: " + NOME_ARQUIVO);
        } catch (IOException e) {
            System.err.println("Erro ao salvar funcionários no arquivo JSON: " + e.getMessage());
        }
    }

    @Override
    public void adicionar(Funcionario entidade) throws DadoInvalidoException {
        if (entidade == null || entidade.getMatriculaFuncional() == null || entidade.getMatriculaFuncional().trim().isEmpty()) {
            throw new DadoInvalidoException("Erro: Tentativa de adicionar funcionário nulo ou com matrícula funcional vazia/nula.");
        }
        if (entidade.getNome() == null || entidade.getNome().trim().isEmpty()) {
            throw new DadoInvalidoException("Erro: Nome do funcionário é obrigatório para adição.");
        }

        boolean existe = this.funcionarios.stream()
                .anyMatch(f -> f != null && f.getMatriculaFuncional() != null && f.getMatriculaFuncional().equals(entidade.getMatriculaFuncional()));
        if (existe) {
            throw new DadoInvalidoException("Já existe um funcionário cadastrado com a matrícula: " + entidade.getMatriculaFuncional());
        }

        this.funcionarios.add(entidade);
        salvarFuncionariosNoArquivo();
    }

    @Override
    public Optional<Funcionario> buscarPorId(String chave) {
        if (chave == null || chave.trim().isEmpty()) {
            return Optional.empty();
        }
        return this.funcionarios.stream()
                .filter(f -> f != null && f.getMatriculaFuncional() != null && f.getMatriculaFuncional().equals(chave))
                .findFirst();
    }

    @Override
    public void atualizar(Funcionario entidade) throws DadoInvalidoException, EntidadeNaoEncontradaException {
        if (entidade == null || entidade.getMatriculaFuncional() == null || entidade.getMatriculaFuncional().trim().isEmpty()) {
            throw new DadoInvalidoException("Não é possível atualizar: Funcionário ou matrícula funcional inválida.");
        }
        if (entidade.getNome() == null || entidade.getNome().trim().isEmpty()) {
            throw new DadoInvalidoException("Nome do funcionário é obrigatório para atualização.");
        }

        Optional<Funcionario> funcionarioExistenteOpt = buscarPorId(entidade.getMatriculaFuncional());
        if (funcionarioExistenteOpt.isEmpty()) {
            throw new EntidadeNaoEncontradaException("Funcionário com matrícula " + entidade.getMatriculaFuncional() + " não encontrado para atualização.");
        }

        this.funcionarios.removeIf(f -> f != null && f.getMatriculaFuncional() != null && f.getMatriculaFuncional().equals(entidade.getMatriculaFuncional()));
        this.funcionarios.add(entidade);
        salvarFuncionariosNoArquivo();
    }

    @Override
    public boolean deletar(String chave) throws EntidadeNaoEncontradaException, DadoInvalidoException {
        if (chave == null || chave.trim().isEmpty()) {
            throw new DadoInvalidoException("Matrícula funcional para deleção não pode ser nula ou vazia.");
        }
        boolean removido = this.funcionarios.removeIf(f -> f != null && f.getMatriculaFuncional() != null && f.getMatriculaFuncional().equals(chave));
        if (removido) {
            salvarFuncionariosNoArquivo();
            return true;
        } else {
            throw new EntidadeNaoEncontradaException("Funcionário com matrícula " + chave + " não encontrado para deleção no JSON.");
        }
    }

    @Override
    public List<Funcionario> listarTodos() {
        return new ArrayList<>(this.funcionarios);
    }

    /**
     * Limpa todos os dados de funcionários do repositório e persiste a lista vazia no arquivo JSON.
     */
    public void limpar() {
        this.funcionarios.clear(); // Limpa a lista em memória
        salvarFuncionariosNoArquivo(); // Salva a lista vazia no arquivo
        System.out.println("DEBUG: Arquivo " + NOME_ARQUIVO + " limpo.");
    }
}