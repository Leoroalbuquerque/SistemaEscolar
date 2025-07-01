package br.com.escola.negocio;

import java.util.Map;

public class Acesso {
    private Map<String, Usuario> usuarios;

    public void criarUsuario(String nome, String senha, String perfil) {}
    public Usuario autenticar(String usuario, String senha) { return null; }
    public boolean redefinirSenha(String usuario, String novaSenha) { return false; }
    public boolean checarPermissao(String usuario, String funcionalidade) { return false; }
}