package br.com.escola.main;

import br.com.escola.negocio.Acesso;
import br.com.escola.negocio.Usuario;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AcessoTeste {
    public static void main(String[] args) {
        System.out.println("Teste");
    }

    private Acesso sistemaAcesso;

    @BeforeEach
    void setUp() {
        sistemaAcesso = new Acesso();
        sistemaAcesso.removerUsuario("admin");
    }

    @Test
    @DisplayName("Deve criar um novo usuário com sucesso")
    void deveCriarUsuarioComSucesso() {
        sistemaAcesso.criarUsuario("usuarioTeste", "senha123", "ALUNO");
        Usuario usuarioCriado = sistemaAcesso.autenticar("usuarioTeste", "senha123");
        assertNotNull(usuarioCriado);
        assertEquals("usuarioTeste", usuarioCriado.getNomeUsuario());
        assertEquals("ALUNO", usuarioCriado.getPerfil());
    }

    @Test
    @DisplayName("Não deve criar usuário com nome de usuário já existente")
    void naoDeveCriarUsuarioDuplicado() {
        sistemaAcesso.criarUsuario("usuarioExistente", "senhaOriginal", "PROFESSOR");
        sistemaAcesso.criarUsuario("usuarioExistente", "novaSenha", "ADMINISTRADOR");
        Usuario usuario = sistemaAcesso.autenticar("usuarioExistente", "senhaOriginal");
        assertNotNull(usuario);
        assertEquals("PROFESSOR", usuario.getPerfil());
        assertNull(sistemaAcesso.autenticar("usuarioExistente", "novaSenha"));
    }

    @Test
    @DisplayName("Deve autenticar um usuário com credenciais corretas")
    void deveAutenticarComCredenciaisCorretas() {
        sistemaAcesso.criarUsuario("aluno1", "senhaAluno", "ALUNO");
        Usuario usuarioAutenticado = sistemaAcesso.autenticar("aluno1", "senhaAluno");
        assertNotNull(usuarioAutenticado);
        assertEquals("aluno1", usuarioAutenticado.getNomeUsuario());
    }

    @Test
    @DisplayName("Não deve autenticar com senha incorreta")
    void naoDeveAutenticarComSenhaIncorreta() {
        sistemaAcesso.criarUsuario("prof1", "senhaProf", "PROFESSOR");
        Usuario usuarioAutenticado = sistemaAcesso.autenticar("prof1", "senhaIncorreta");
        assertNull(usuarioAutenticado);
    }

    @Test
    @DisplayName("Não deve autenticar usuário inexistente")
    void naoDeveAutenticarUsuarioInexistente() {
        Usuario usuarioAutenticado = sistemaAcesso.autenticar("usuarioInexistente", "qualquerSenha");
        assertNull(usuarioAutenticado);
    }

    @Test
    @DisplayName("Deve redefinir a senha de um usuário com sucesso")
    void deveRedefinirSenhaComSucesso() {
        sistemaAcesso.criarUsuario("adminTeste", "senhaAntiga", "ADMINISTRADOR");
        boolean redefinido = sistemaAcesso.redefinirSenha("adminTeste", "novaSenhaForte");
        assertTrue(redefinido);
        Usuario usuario = sistemaAcesso.autenticar("adminTeste", "novaSenhaForte");
        assertNotNull(usuario);
        assertNull(sistemaAcesso.autenticar("adminTeste", "senhaAntiga"));
    }

    @Test
    @DisplayName("Não deve redefinir senha para usuário inexistente")
    void naoDeveRedefinirSenhaUsuarioInexistente() {
        boolean redefinido = sistemaAcesso.redefinirSenha("naoExiste", "novaSenha");
        assertFalse(redefinido);
    }

    @Test
    @DisplayName("Administrador deve ter todas as permissões")
    void adminDeveTerTodasAsPermissoes() {
        sistemaAcesso.criarUsuario("superadmin", "supersegura", "ADMINISTRADOR");
        Usuario admin = sistemaAcesso.autenticar("superadmin", "supersegura");
        assertNotNull(admin);
        assertTrue(sistemaAcesso.checarPermissao(admin, "GERENCIAR_USUARIOS"));
        assertTrue(sistemaAcesso.checarPermissao(admin, "LANÇAR_NOTAS"));
        assertTrue(sistemaAcesso.checarPermissao(admin, "QUALQUER_FUNCIONALIDADE"));
    }

    @Test
    @DisplayName("Professor deve ter permissões específicas")
    void professorDeveTerPermissoesEspecificas() {
        sistemaAcesso.criarUsuario("professorJoao", "joao123", "PROFESSOR");
        Usuario professor = sistemaAcesso.autenticar("professorJoao", "joao123");
        assertNotNull(professor);
        assertTrue(sistemaAcesso.checarPermissao(professor, "LANÇAR_NOTAS"));
        assertTrue(sistemaAcesso.checarPermissao(professor, "LANÇAR_FREQUENCIA"));
        assertFalse(sistemaAcesso.checarPermissao(professor, "GERENCIAR_USUARIOS"));
        assertFalse(sistemaAcesso.checarPermissao(professor, "GERAR_BOLETIM"));
    }

    @Test
    @DisplayName("Perfil não reconhecido não deve ter permissão")
    void perfilNaoReconhecidoNaoDeveTerPermissao() {
        sistemaAcesso.criarUsuario("usuarioGenerico", "senhaGenerica", "ALUNO");
        Usuario generico = sistemaAcesso.autenticar("usuarioGenerico", "senhaGenerica");
        assertNotNull(generico);
        assertFalse(sistemaAcesso.checarPermissao(generico, "QUALQUER_COISA"));
    }
}
