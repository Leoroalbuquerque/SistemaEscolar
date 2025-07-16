package br.com.escola.main; // Onde o arquivo de teste está localizado

import br.com.escola.negocio.Acesso;   // Importa a classe Acesso do pacote de negócio
import br.com.escola.negocio.Usuario;  // Importa a classe Usuario do pacote de negócio

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*; // Importa as asserções estáticas do JUnit 5

public class AcessoTeste {

    private Acesso sistemaAcesso;

    // Este método é executado ANTES de cada método de teste (@Test)
    @BeforeEach
    void setUp() {
        // Inicializa uma nova instância de Acesso para cada teste, garantindo isolamento
        sistemaAcesso = new Acesso();
        // Remove o admin padrão que o construtor de Acesso cria,
        // para que cada teste comece de um estado limpo.
        sistemaAcesso.removerUsuario("admin"); // Agora este método existe em Acesso.java
    }

    @Test
    @DisplayName("Deve criar um novo usuário com sucesso")
    void deveCriarUsuarioComSucesso() {
        sistemaAcesso.criarUsuario("usuarioTeste", "senha123", "ALUNO");
        // Tenta autenticar para verificar se o usuário foi criado
        Usuario usuarioCriado = sistemaAcesso.autenticar("usuarioTeste", "senha123");
        assertNotNull(usuarioCriado, "O usuário criado não deveria ser nulo após autenticação.");
        assertEquals("usuarioTeste", usuarioCriado.getNomeUsuario(), "O nome de usuário não corresponde.");
        assertEquals("ALUNO", usuarioCriado.getPerfil(), "O perfil do usuário não corresponde.");
    }

    @Test
    @DisplayName("Não deve criar usuário com nome de usuário já existente")
    void naoDeveCriarUsuarioDuplicado() {
        sistemaAcesso.criarUsuario("usuarioExistente", "senhaOriginal", "PROFESSOR");
        // Tenta criar o mesmo usuário novamente
        sistemaAcesso.criarUsuario("usuarioExistente", "novaSenha", "ADMINISTRADOR"); // Isso deve falhar

        // Verifica se a senha e o perfil do usuário original não foram alterados
        Usuario usuario = sistemaAcesso.autenticar("usuarioExistente", "senhaOriginal");
        assertNotNull(usuario);
        assertEquals("PROFESSOR", usuario.getPerfil(), "O perfil não deveria ter sido alterado.");
        // Tenta autenticar com a "novaSenha" (que não deveria ter sido aplicada)
        assertNull(sistemaAcesso.autenticar("usuarioExistente", "novaSenha"), "Não deveria ser possível autenticar com a nova senha.");
    }

    @Test
    @DisplayName("Deve autenticar um usuário com credenciais corretas")
    void deveAutenticarComCredenciaisCorretas() {
        sistemaAcesso.criarUsuario("aluno1", "senhaAluno", "ALUNO");
        Usuario usuarioAutenticado = sistemaAcesso.autenticar("aluno1", "senhaAluno");
        assertNotNull(usuarioAutenticado, "A autenticação deveria ser bem-sucedida.");
        assertEquals("aluno1", usuarioAutenticado.getNomeUsuario());
    }

    @Test
    @DisplayName("Não deve autenticar com senha incorreta")
    void naoDeveAutenticarComSenhaIncorreta() {
        sistemaAcesso.criarUsuario("prof1", "senhaProf", "PROFESSOR");
        Usuario usuarioAutenticado = sistemaAcesso.autenticar("prof1", "senhaIncorreta");
        assertNull(usuarioAutenticado, "A autenticação deveria falhar com senha incorreta.");
    }

    @Test
    @DisplayName("Não deve autenticar usuário inexistente")
    void naoDeveAutenticarUsuarioInexistente() {
        Usuario usuarioAutenticado = sistemaAcesso.autenticar("usuarioInexistente", "qualquerSenha");
        assertNull(usuarioAutenticado, "A autenticação deveria falhar para usuário inexistente.");
    }

    @Test
    @DisplayName("Deve redefinir a senha de um usuário com sucesso")
    void deveRedefinirSenhaComSucesso() {
        sistemaAcesso.criarUsuario("adminTeste", "senhaAntiga", "ADMINISTRADOR");
        boolean redefinido = sistemaAcesso.redefinirSenha("adminTeste", "novaSenhaForte");
        assertTrue(redefinido, "A redefinição de senha deveria ser bem-sucedida.");
        
        // Tenta autenticar com a nova senha
        Usuario usuario = sistemaAcesso.autenticar("adminTeste", "novaSenhaForte");
        assertNotNull(usuario, "Deveria ser possível autenticar com a nova senha.");
        
        // Tenta autenticar com a senha antiga (deve falhar)
        assertNull(sistemaAcesso.autenticar("adminTeste", "senhaAntiga"), "Não deveria ser possível autenticar com a senha antiga.");
    }

    @Test
    @DisplayName("Não deve redefinir senha para usuário inexistente")
    void naoDeveRedefinirSenhaUsuarioInexistente() {
        boolean redefinido = sistemaAcesso.redefinirSenha("naoExiste", "novaSenha");
        assertFalse(redefinido, "A redefinição de senha deveria falhar para usuário inexistente.");
    }

    @Test
    @DisplayName("Administrador deve ter todas as permissões")
    void adminDeveTerTodasAsPermissoes() {
        sistemaAcesso.criarUsuario("superadmin", "supersegura", "ADMINISTRADOR");
        Usuario admin = sistemaAcesso.autenticar("superadmin", "supersegura");
        assertNotNull(admin);

        assertTrue(sistemaAcesso.checarPermissao(admin, "GERENCIAR_USUARIOS"), "Admin deve ter permissão para gerenciar usuários.");
        assertTrue(sistemaAcesso.checarPermissao(admin, "LANÇAR_NOTAS"), "Admin deve ter permissão para lançar notas.");
        assertTrue(sistemaAcesso.checarPermissao(admin, "QUALQUER_FUNCIONALIDADE"), "Admin deve ter permissão para qualquer funcionalidade.");
    }

    @Test
    @DisplayName("Professor deve ter permissões específicas")
    void professorDeveTerPermissoesEspecificas() {
        sistemaAcesso.criarUsuario("professorJoao", "joao123", "PROFESSOR");
        Usuario professor = sistemaAcesso.autenticar("professorJoao", "joao123");
        assertNotNull(professor);

        assertTrue(sistemaAcesso.checarPermissao(professor, "LANÇAR_NOTAS"), "Professor deve ter permissão para lançar notas.");
        assertTrue(sistemaAcesso.checarPermissao(professor, "LANÇAR_FREQUENCIA"), "Professor deve ter permissão para lançar frequência.");
        assertFalse(sistemaAcesso.checarPermissao(professor, "GERENCIAR_USUARIOS"), "Professor NÃO deve ter permissão para gerenciar usuários.");
        assertFalse(sistemaAcesso.checarPermissao(professor, "GERAR_BOLETIM"), "Professor NÃO deve ter permissão para gerar boletim.");
    }

    @Test
    @DisplayName("Perfil não reconhecido não deve ter permissão")
    void perfilNaoReconhecidoNaoDeveTerPermissao() {
        // Exemplo: um ALUNO no nosso modelo atual não tem permissões explícitas no checarPermissao para "QUALQUER_COISA"
        sistemaAcesso.criarUsuario("usuarioGenerico", "senhaGenerica", "ALUNO"); 
        Usuario generico = sistemaAcesso.autenticar("usuarioGenerico", "senhaGenerica");
        assertNotNull(generico);

        assertFalse(sistemaAcesso.checarPermissao(generico, "QUALQUER_COISA"), "Usuário com perfil não mapeado não deve ter permissão.");
    }
}