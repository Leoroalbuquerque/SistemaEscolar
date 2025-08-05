package br.com.escola.dados;

import br.com.escola.negocio.Funcionario;
import br.com.escola.excecoes.DadoInvalidoException;
import br.com.escola.excecoes.EntidadeNaoEncontradaException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FuncionarioRepositorioJson implements IRepositorio<Funcionario, String> {

    private static final String NOME_ARQUIVO = "funcionarios.json";
    private final ObjectMapper objectMapper;
    private List<Funcionario> funcionarios;

    public FuncionarioRepositorioJson() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.funcionarios = new ArrayList<>();
        carregarFuncionariosDoArquivo();
    }

    private void carregarFuncionariosDoArquivo() {
        File arquivo = new File(NOME_ARQUIVO);
        if (arquivo.exists() && arquivo.length() > 0) {
            try {
                this.funcionarios = objectMapper.readValue(arquivo, new TypeReference<List<Funcionario>>() {});
            } catch (IOException e) {
                System.err.println("Erro ao carregar funcionários do arquivo JSON. Detalhes: " + e.getMessage());
                this.funcionarios = new ArrayList<>();
            }
        }
    }

    private void salvarFuncionariosNoArquivo() throws IOException {
        try {
            objectMapper.writeValue(new File(NOME_ARQUIVO), this.funcionarios);
        } catch (IOException e) {
            System.err.println("Erro ao salvar funcionários no arquivo JSON: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void salvar(Funcionario entidade) throws DadoInvalidoException, IOException {
        if (entidade == null || entidade.getMatriculaFuncional() == null || entidade.getMatriculaFuncional().trim().isEmpty()) {
            throw new DadoInvalidoException("Erro: Tentativa de adicionar funcionário nulo ou com matrícula funcional vazia/nula.");
        }
        if (entidade.getNome() == null || entidade.getMatriculaFuncional().trim().isEmpty()) {
            throw new DadoInvalidoException("Erro: Nome do funcionário é obrigatório para adição.");
        }

        boolean jaExiste = this.funcionarios.stream()
                .anyMatch(f -> Objects.equals(f.getMatriculaFuncional(), entidade.getMatriculaFuncional()));

        if (jaExiste) {
            throw new DadoInvalidoException("Já existe um funcionário cadastrado com a matrícula: " + entidade.getMatriculaFuncional());
        } else {
            this.funcionarios.add(entidade);
            salvarFuncionariosNoArquivo();
        }
    }

    @Override
    public Funcionario buscarPorId(String chave) throws IOException, EntidadeNaoEncontradaException, DadoInvalidoException {
        if (chave == null || chave.trim().isEmpty()) {
            throw new DadoInvalidoException("Matrícula funcional para busca não pode ser nula ou vazia.");
        }
        return this.funcionarios.stream()
                .filter(f -> Objects.equals(f.getMatriculaFuncional(), chave))
                .findFirst()
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Funcionário com matrícula " + chave + " não encontrado."));
    }

    @Override
    public void atualizar(Funcionario entidade) throws IOException, EntidadeNaoEncontradaException, DadoInvalidoException {
        if (entidade == null || entidade.getMatriculaFuncional() == null || entidade.getMatriculaFuncional().trim().isEmpty()) {
            throw new DadoInvalidoException("Não é possível atualizar: Funcionário ou matrícula funcional inválida.");
        }
        if (entidade.getNome() == null || entidade.getNome().trim().isEmpty()) {
            throw new DadoInvalidoException("Nome do funcionário é obrigatório para atualização.");
        }

        boolean encontrada = false;
        for (int i = 0; i < funcionarios.size(); i++) {
            if (Objects.equals(funcionarios.get(i).getMatriculaFuncional(), entidade.getMatriculaFuncional())) {
                funcionarios.set(i, entidade);
                encontrada = true;
                break;
            }
        }

        if (!encontrada) {
            throw new EntidadeNaoEncontradaException("Funcionário com matrícula " + entidade.getMatriculaFuncional() + " não encontrado para atualização.");
        }
        salvarFuncionariosNoArquivo();
    }

    @Override
    public void deletar(String chave) throws IOException, EntidadeNaoEncontradaException, DadoInvalidoException {
        if (chave == null || chave.trim().isEmpty()) {
            throw new DadoInvalidoException("Matrícula funcional para deleção não pode ser nula ou vazia.");
        }
        boolean removido = this.funcionarios.removeIf(f -> Objects.equals(f.getMatriculaFuncional(), chave));
        if (!removido) {
            throw new EntidadeNaoEncontradaException("Funcionário com matrícula " + chave + " não encontrado para exclusão.");
        }
        salvarFuncionariosNoArquivo();
    }

    @Override
    public List<Funcionario> listarTodos() throws IOException {
        return new ArrayList<>(this.funcionarios);
    }

    @Override
    public void limpar() throws IOException {
        this.funcionarios.clear();
        salvarFuncionariosNoArquivo();
    }
}