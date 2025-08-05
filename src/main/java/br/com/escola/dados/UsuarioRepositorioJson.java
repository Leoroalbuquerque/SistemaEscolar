package br.com.escola.dados;

import br.com.escola.negocio.Usuario;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UsuarioRepositorioJson {
    private static final String NOME_ARQUIVO = "usuarios.json";
    private List<Usuario> usuarios;
    private final Gson gson;

    public UsuarioRepositorioJson() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.usuarios = carregarDados();
    }

    private List<Usuario> carregarDados() {
        File file = new File(NOME_ARQUIVO);
        if (!file.exists() || file.length() == 0) {
            return new ArrayList<>();
        }
        try (FileReader reader = new FileReader(file)) {
            Type tipoListaUsuario = new TypeToken<List<Usuario>>() {}.getType();
            List<Usuario> carregados = gson.fromJson(reader, tipoListaUsuario);
            return carregados != null ? carregados : new ArrayList<>();
        } catch (IOException e) {
            System.err.println("Erro ao carregar dados de usuários: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private void salvarDados() throws IOException {
        try (FileWriter writer = new FileWriter(NOME_ARQUIVO)) {
            gson.toJson(usuarios, writer);
        }
    }

    public void salvar(Usuario usuario) throws IOException {
        if (buscarPorNomeUsuario(usuario.getNomeUsuario()).isPresent()) {
            throw new IOException("Usuário com nome de usuário '" + usuario.getNomeUsuario() + "' já existe.");
        }
        this.usuarios.add(usuario);
        salvarDados();
    }

    public Optional<Usuario> buscarPorNomeUsuario(String nomeUsuario) {
        try {
            this.usuarios = carregarDados();
            return usuarios.stream()
                    .filter(u -> u.getNomeUsuario().equals(nomeUsuario))
                    .findFirst();
        } catch (Exception e) {
            System.err.println("Erro ao buscar usuário no repositório: " + e.getMessage());
            return Optional.empty();
        }
    }

    public List<Usuario> listarTodos() {
        this.usuarios = carregarDados();
        return new ArrayList<>(usuarios);
    }

    public void atualizar(Usuario usuarioAtualizado) throws IOException {
        boolean encontrado = false;
        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getNomeUsuario().equals(usuarioAtualizado.getNomeUsuario())) {
                usuarios.set(i, usuarioAtualizado);
                encontrado = true;
                break;
            }
        }
        if (encontrado) {
            salvarDados();
        } else {
            throw new IOException("Usuário com nome de usuário '" + usuarioAtualizado.getNomeUsuario() + "' não encontrado para atualização.");
        }
    }

    public boolean deletar(String nomeUsuario) throws IOException {
        boolean removido = usuarios.removeIf(u -> u.getNomeUsuario().equals(nomeUsuario));
        if (removido) {
            salvarDados();
        }
        return removido;
    }

    public void limpar() throws IOException {
        this.usuarios.clear();
        salvarDados();
    }
}