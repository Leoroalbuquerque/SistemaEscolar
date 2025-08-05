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
import java.util.Objects;

public class ResponsavelRepositorioJson implements IRepositorio<Responsavel, String> {

    private static final String NOME_ARQUIVO = "responsaveis.json";
    private List<Responsavel> responsaveis;
    private final Gson gson;

    public ResponsavelRepositorioJson() {
        gson = new GsonBuilder().setPrettyPrinting().create();
        responsaveis = new ArrayList<>();
        carregarDados();
    }

    private void carregarDados() {
        try (FileReader reader = new FileReader(NOME_ARQUIVO)) {
            Type tipoListaResponsavel = new TypeToken<ArrayList<Responsavel>>() {}.getType();
            responsaveis = gson.fromJson(reader, tipoListaResponsavel);
            if (responsaveis == null) {
                responsaveis = new ArrayList<>();
            }
        } catch (IOException e) {
            responsaveis = new ArrayList<>();
        }
    }

    private void salvarDados() throws IOException {
        try (FileWriter writer = new FileWriter(NOME_ARQUIVO)) {
            gson.toJson(responsaveis, writer);
        }
    }

    @Override
    public void salvar(Responsavel responsavel) throws DadoInvalidoException, IOException {
        if (responsavel == null || responsavel.getCpfResponsavel() == null || responsavel.getCpfResponsavel().trim().isEmpty()) {
            throw new DadoInvalidoException("Responsável ou CPF não pode ser nulo/vazio para salvar.");
        }
        try {
            buscarPorId(responsavel.getCpfResponsavel());
            throw new DadoInvalidoException("Responsável com CPF " + responsavel.getCpfResponsavel() + " já existe.");
        } catch (EntidadeNaoEncontradaException e) {
            this.responsaveis.add(responsavel);
            salvarDados();
        }
    }

    @Override
    public Responsavel buscarPorId(String cpf) throws IOException, EntidadeNaoEncontradaException, DadoInvalidoException {
        if (cpf == null || cpf.trim().isEmpty()) {
            throw new DadoInvalidoException("CPF para busca não pode ser nulo ou vazio.");
        }
        return responsaveis.stream()
                .filter(r -> Objects.equals(r.getCpfResponsavel(), cpf))
                .findFirst()
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Responsável com CPF " + cpf + " não encontrado."));
    }

    @Override
    public void atualizar(Responsavel responsavelAtualizado) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        if (responsavelAtualizado == null || responsavelAtualizado.getCpfResponsavel() == null || responsavelAtualizado.getCpfResponsavel().trim().isEmpty()) {
            throw new DadoInvalidoException("Responsável ou CPF não pode ser nulo/vazio para atualização.");
        }

        boolean encontrada = false;
        for (int i = 0; i < responsaveis.size(); i++) {
            if (Objects.equals(responsaveis.get(i).getCpfResponsavel(), responsavelAtualizado.getCpfResponsavel())) {
                responsaveis.set(i, responsavelAtualizado);
                encontrada = true;
                break;
            }
        }

        if (!encontrada) {
            throw new EntidadeNaoEncontradaException("Responsável com CPF " + responsavelAtualizado.getCpfResponsavel() + " não encontrado para atualização.");
        }
        salvarDados();
    }

    @Override
    public void deletar(String cpf) throws EntidadeNaoEncontradaException, DadoInvalidoException, IOException {
        if (cpf == null || cpf.trim().isEmpty()) {
            throw new DadoInvalidoException("CPF para deleção não pode ser nulo ou vazio.");
        }
        boolean removido = responsaveis.removeIf(r -> Objects.equals(r.getCpfResponsavel(), cpf));
        if (!removido) {
            throw new EntidadeNaoEncontradaException("Responsável com CPF " + cpf + " não encontrado para exclusão.");
        }
        salvarDados();
    }

    @Override
    public List<Responsavel> listarTodos() throws IOException {
        return new ArrayList<>(responsaveis);
    }

    @Override
    public void limpar() throws IOException {
        this.responsaveis.clear();
        salvarDados();
    }
}