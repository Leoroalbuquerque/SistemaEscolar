package br.com.escola.negocio;

import br.com.escola.dados.UsuarioRepositorioJson;
import java.io.IOException;
import java.util.Optional;

public class Acesso {

    private UsuarioRepositorioJson usuarioRepositorio;

    public Acesso() {
        this.usuarioRepositorio = new UsuarioRepositorioJson();
        if (usuarioRepositorio.buscarPorNomeUsuario("admin").isEmpty()) {
            criarUsuarioInterno("admin", "admin123", "ADMINISTRADOR");
        }
    }

    public boolean criarUsuario(String nomeUsuario, String senha, String perfil) throws IOException {
        usuarioRepositorio.salvar(new Usuario(nomeUsuario, senha, perfil));
        return true;
    }

    private boolean criarUsuarioInterno(String nomeUsuario, String senha, String perfil) {
        try {
            usuarioRepositorio.salvar(new Usuario(nomeUsuario, senha, perfil));
            return true;
        } catch (IOException e) {
            System.err.println("Erro ao criar usuário interno (admin): " + e.getMessage());
            return false;
        }
    }

    public Usuario autenticar(String nomeUsuario, String senha) {
        Optional<Usuario> usuarioOpt = usuarioRepositorio.buscarPorNomeUsuario(nomeUsuario);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            if (usuario.getSenhaCriptografada().equals(senha)) {
                System.out.println("Usuário '" + nomeUsuario + "' autenticado com sucesso.");
                return usuario;
            }
        }
        System.out.println("Falha na autenticação para o usuário '" + nomeUsuario + "'.");
        return null;
    }

    public boolean redefinirSenha(String nomeUsuario, String novaSenha) throws IOException {
        Optional<Usuario> usuarioOpt = usuarioRepositorio.buscarPorNomeUsuario(nomeUsuario);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            usuario.setSenhaCriptografada(novaSenha);
            usuarioRepositorio.atualizar(usuario);
            System.out.println("Senha do usuário '" + nomeUsuario + "' redefinida com sucesso.");
            return true;
        }
        System.out.println("Erro: Usuário '" + nomeUsuario + "' não encontrado para redefinir senha.");
        return false;
    }

    public boolean removerUsuario(String nomeUsuario) throws IOException {
        boolean removido = usuarioRepositorio.deletar(nomeUsuario);
        if (removido) {
            System.out.println("Usuário '" + nomeUsuario + "' removido com sucesso.");
        } else {
            System.out.println("Usuário '" + nomeUsuario + "' não encontrado para remoção.");
        }
        return removido;
    }

    public boolean checarPermissao(Usuario usuario, String funcionalidade) {
        if (usuario == null || funcionalidade == null || funcionalidade.isEmpty()) {
            return false;
        }

        switch (usuario.getPerfil()) {
            case "ADMINISTRADOR":
                return true;
            case "PROFESSOR":
                return funcionalidade.equals("LANÇAR_NOTAS") ||
                       funcionalidade.equals("LANÇAR_FREQUENCIA") ||
                       funcionalidade.equals("VISUALIZAR_ALUNOS");
            case "ALUNO":
                return funcionalidade.equals("VISUALIZAR_NOTAS") ||
                       funcionalidade.equals("VISUALIZAR_FREQUENCIA") ||
                       funcionalidade.equals("VISUALIZAR_PROFESSORES");
            default:
                return false;
        }
    }
}