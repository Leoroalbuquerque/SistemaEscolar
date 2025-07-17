package br.com.escola.negocio;

import java.util.Objects;

public class Usuario {
    private String nomeUsuario;
    private String senhaCriptografada;
    private String perfil;

    public Usuario(String nomeUsuario, String senhaCriptografada, String perfil) {
        this.nomeUsuario = nomeUsuario;
        this.senhaCriptografada = senhaCriptografada;
        this.perfil = perfil;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public String getSenhaCriptografada() {
        return senhaCriptografada;
    }

    public String getPerfil() {
        return perfil;
    }

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
        return Objects.equals(nomeUsuario, usuario.nomeUsuario);
    }

    @Override
    public int hashCode() {
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
