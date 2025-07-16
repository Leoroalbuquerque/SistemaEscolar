package br.com.escola.negocio;

import java.util.Objects; // ESSENCIAL: Adicionado para usar Objects.equals e Objects.hash

public class Usuario {
    private String nomeUsuario;
    private String senhaCriptografada; // Idealmente, armazenaríamos um hash da senha, não a senha em texto puro
    private String perfil; // Ex: ALUNO, PROFESSOR, ADMINISTRADOR

    // Construtor
    public Usuario(String nomeUsuario, String senhaCriptografada, String perfil) {
        this.nomeUsuario = nomeUsuario;
        this.senhaCriptografada = senhaCriptografada; // Em um sistema real, aqui seria feito o hash da senha
        this.perfil = perfil;
    }

    // Getters
    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public String getSenhaCriptografada() {
        return senhaCriptografada;
    }

    public String getPerfil() {
        return perfil;
    }

    // Setters (para permitir modificações, como redefinição de senha ou mudança de perfil)
    public void setSenhaCriptografada(String senhaCriptografada) {
        this.senhaCriptografada = senhaCriptografada;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        // Compara usuários apenas pelo nome de usuário (que deve ser único)
        return Objects.equals(nomeUsuario, usuario.nomeUsuario);
    }

    @Override
    public int hashCode() {
        // Gera o hash baseado apenas no nome de usuário
        return Objects.hash(nomeUsuario);
    }

    @Override
    public String toString() {
        return "Usuario{" +
               "nomeUsuario='" + nomeUsuario + '\'' +
               ", perfil='" + perfil + '\'' +
               '}';
    }
}