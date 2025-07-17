package br.com.escola.negocio;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Acesso {
    private Map<String, Usuario> usuarios;

    public Acesso() {
        this.usuarios = new HashMap<>();
        criarUsuario("admin", "admin123", "ADMINISTRADOR");
    }

    public boolean criarUsuario(String nomeUsuario, String senha, String perfil) {
        if (usuarios.containsKey(nomeUsuario)) {
            System.out.println("Erro: Usuário '" + nomeUsuario + "' já existe.");
            return false;
        }
        Usuario novoUsuario = new Usuario(nomeUsuario, senha, perfil);
        usuarios.put(nomeUsuario, novoUsuario);
        System.out.println("Usuário '" + nomeUsuario + "' criado com perfil '" + perfil + "'.");
        return true;
    }

    public Usuario autenticar(String nomeUsuario, String senha) {
        Usuario usuario = usuarios.get(nomeUsuario);
        if (usuario != null && usuario.getSenhaCriptografada().equals(senha)) {
            System.out.println("Usuário '" + nomeUsuario + "' autenticado com sucesso.");
            return usuario;
        }
        System.out.println("Falha na autenticação para o usuário '" + nomeUsuario + "'.");
        return null;
    }

    public boolean redefinirSenha(String nomeUsuario, String novaSenha) {
        Usuario usuario = usuarios.get(nomeUsuario);
        if (usuario != null) {
            usuario.setSenhaCriptografada(novaSenha);
            System.out.println("Senha do usuário '" + nomeUsuario + "' redefinida com sucesso.");
            return true;
        }
        System.out.println("Erro: Usuário '" + nomeUsuario + "' não encontrado para redefinir senha.");
        return false;
    }

    public boolean removerUsuario(String nomeUsuario) {
        if (usuarios.containsKey(nomeUsuario)) {
            usuarios.remove(nomeUsuario);
            System.out.println("Usuário '" + nomeUsuario + "' removido com sucesso.");
            return true;
        }
        System.out.println("Usuário '" + nomeUsuario + "' não encontrado para remoção.");
        return false;
    }

    public boolean checarPermissao(Usuario usuario, String funcionalidade) {
        if (usuario == null || funcionalidade == null || funcionalidade.isEmpty()) {
            return false;
        }

        Set<String> permissoes = new HashSet<>();
        switch (usuario.getPerfil()) {
            case "ADMINISTRADOR":
                return true;
            case "PROFESSOR":
                permissoes.add("LANÇAR_NOTAS");
                permissoes.add("LANÇAR_FREQUENCIA");
                permissoes.add("VISUALIZAR_ALUNOS");
                break;
            case "ALUNO":
                permissoes.add("VISUALIZAR_NOTAS");
                permissoes.add("VISUALIZAR_FREQUENCIA");
                permissoes.add("VISUALIZAR_PROFESSORES");
                break;
            default:
                break;
        }
        return permissoes.contains(funcionalidade);
    }
}
