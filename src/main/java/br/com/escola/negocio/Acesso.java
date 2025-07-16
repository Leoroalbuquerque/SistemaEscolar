package br.com.escola.negocio;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Acesso {
    private Map<String, Usuario> usuarios; // Armazena usuários por nome de usuário

    public Acesso() {
        this.usuarios = new HashMap<>();
        // Opcional: Adicionar um usuário admin padrão ao iniciar o sistema
        // Para testes, o AcessoTeste remove este admin para ter um estado limpo.
        criarUsuario("admin", "admin123", "ADMINISTRADOR");
    }

    /**
     * Cria um novo usuário no sistema.
     * @param nomeUsuario Nome de usuário (deve ser único).
     * @param senha Senha do usuário.
     * @param perfil Perfil do usuário (Ex: ALUNO, PROFESSOR, ADMINISTRADOR).
     * @return true se o usuário foi criado com sucesso, false se o nome de usuário já existe.
     */
    public boolean criarUsuario(String nomeUsuario, String senha, String perfil) {
        if (usuarios.containsKey(nomeUsuario)) {
            System.out.println("Erro: Usuário '" + nomeUsuario + "' já existe.");
            return false;
        }
        // Em um sistema real, a senha seria hashada antes de ser armazenada
        Usuario novoUsuario = new Usuario(nomeUsuario, senha, perfil);
        usuarios.put(nomeUsuario, novoUsuario);
        System.out.println("Usuário '" + nomeUsuario + "' criado com perfil '" + perfil + "'.");
        return true;
    }

    /**
     * Autentica um usuário no sistema.
     * @param nomeUsuario Nome de usuário.
     * @param senha Senha.
     * @return O objeto Usuario se a autenticação for bem-sucedida, null caso contrário.
     */
    public Usuario autenticar(String nomeUsuario, String senha) {
        Usuario usuario = usuarios.get(nomeUsuario);
        if (usuario != null && usuario.getSenhaCriptografada().equals(senha)) {
            System.out.println("Usuário '" + nomeUsuario + "' autenticado com sucesso.");
            return usuario;
        }
        System.out.println("Falha na autenticação para o usuário '" + nomeUsuario + "'.");
        return null;
    }

    /**
     * Redefine a senha de um usuário existente.
     * @param nomeUsuario Nome de usuário.
     * @param novaSenha Nova senha.
     * @return true se a senha foi redefinida com sucesso, false caso contrário (usuário não encontrado).
     */
    public boolean redefinirSenha(String nomeUsuario, String novaSenha) {
        Usuario usuario = usuarios.get(nomeUsuario);
        if (usuario != null) {
            // Em um sistema real, a nova senha também seria hashada
            usuario.setSenhaCriptografada(novaSenha);
            System.out.println("Senha do usuário '" + nomeUsuario + "' redefinida com sucesso.");
            return true;
        }
        System.out.println("Erro: Usuário '" + nomeUsuario + "' não encontrado para redefinir senha.");
        return false;
    }

    /**
     * Remove um usuário do sistema.
     * ESTE MÉTODO FOI ADICIONADO/AJUSTADO PARA OS TESTES FUNCIONAREM CORRETAMENTE.
     * @param nomeUsuario O nome do usuário a ser removido.
     * @return true se o usuário foi removido com sucesso, false caso contrário.
     */
    public boolean removerUsuario(String nomeUsuario) {
        if (usuarios.containsKey(nomeUsuario)) {
            usuarios.remove(nomeUsuario);
            System.out.println("Usuário '" + nomeUsuario + "' removido com sucesso.");
            return true;
        }
        System.out.println("Usuário '" + nomeUsuario + "' não encontrado para remoção.");
        return false;
    }

    /**
     * Checa se um usuário tem permissão para uma determinada funcionalidade.
     * @param usuario O objeto Usuario.
     * @param funcionalidade A funcionalidade a ser checada (Ex: "GERENCIAR_USUARIOS", "LANÇAR_NOTAS").
     * @return true se o usuário tem permissão, false caso contrário.
     */
    public boolean checarPermissao(Usuario usuario, String funcionalidade) {
        if (usuario == null || funcionalidade == null || funcionalidade.isEmpty()) {
            return false;
        }

        Set<String> permissoes = new HashSet<>();
        switch (usuario.getPerfil()) {
            case "ADMINISTRADOR":
                // Administrador tem todas as permissões
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
                // Perfis desconhecidos não têm permissões
                break;
        }
        return permissoes.contains(funcionalidade);
    }
}