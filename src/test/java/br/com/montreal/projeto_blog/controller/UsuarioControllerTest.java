package br.com.montreal.projeto_blog.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import br.com.montreal.projeto_blog.model.TipoUsuario;
import br.com.montreal.projeto_blog.model.Usuario;
import br.com.montreal.projeto_blog.model.UsuarioLogin;
import br.com.montreal.projeto_blog.repository.UsuarioRepository;
import br.com.montreal.projeto_blog.service.UsuarioService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UsuarioControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private TipoUsuario tipo = TipoUsuario.NORMAL;

    @BeforeAll
    void start() {
        usuarioRepository.deleteAll();

        usuarioService.cadastrarUsuario(
                new Usuario(0L, "Root", "root@root.com", "rootroot", " ", tipo)
        );
    }

    private String gerarToken() {
        UsuarioLogin login = new UsuarioLogin();
        login.setUsuario("root@root.com");
        login.setSenha("rootroot");

        HttpEntity<UsuarioLogin> request = new HttpEntity<>(login);
        ResponseEntity<UsuarioLogin> response = testRestTemplate
                .exchange("/usuarios/logar", HttpMethod.POST, request, UsuarioLogin.class);

        return response.getBody().getToken();
    }

    private HttpHeaders criarHeadersComJWT() {
        String token = gerarToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    @Test
    @DisplayName("Cadastrar Um Usuário")
    public void deveCriarUmUsuario() {
        HttpEntity<Usuario> corpoRequisicao = new HttpEntity<>(
                new Usuario(0L, "Paulo Antunes", "paulo_antunes@email.com.br", "13465278",
                        "https://i.imgur.com/JR7kUFU.jpg", tipo));

        ResponseEntity<Usuario> corpoResposta = testRestTemplate.exchange(
                "/usuarios/cadastrar", HttpMethod.POST, corpoRequisicao, Usuario.class);

        assertEquals(HttpStatus.CREATED, corpoResposta.getStatusCode());
        assertEquals(corpoRequisicao.getBody().getNome(), corpoResposta.getBody().getNome());
        assertEquals(corpoRequisicao.getBody().getUsuario(), corpoResposta.getBody().getUsuario());
    }

    @Test
    @DisplayName("Não deve permitir duplicação do Usuário")
    public void naoDeveDuplicarUsuario() {
        usuarioService.cadastrarUsuario(new Usuario(0L, "Maria da Silva", "maria_silva@email.com.br", "13465278",
                "https://i.imgur.com/T12NIp9.jpg", tipo));

        HttpEntity<Usuario> corpoRequisicao = new HttpEntity<>(
                new Usuario(0L, "Maria da Silva", "maria_silva@email.com.br", "13465278",
                        "https://i.imgur.com/T12NIp9.jpg", tipo));

        ResponseEntity<Usuario> corpoResposta = testRestTemplate.exchange(
                "/usuarios/cadastrar", HttpMethod.POST, corpoRequisicao, Usuario.class);

        assertEquals(HttpStatus.BAD_REQUEST, corpoResposta.getStatusCode());
    }

    @Test
    @DisplayName("Atualizar um Usuário")
    public void deveAtualizarUmUsuario() {
        Optional<Usuario> usuarioCadastrado = usuarioService.cadastrarUsuario(
                new Usuario(0L, "Juliana Andrews", "juliana_andrews@email.com.br", "juliana123",
                        "https://i.imgur.com/yDRVeK7.jpg", tipo));

        Usuario usuarioUpdate = new Usuario(usuarioCadastrado.get().getId(),
                "Juliana Andrews Ramos", "juliana_ramos@email.com.br", "juliana123",
                "https://i.imgur.com/yDRVeK7.jpg", tipo);

        HttpHeaders headers = criarHeadersComJWT();
        HttpEntity<Usuario> corpoRequisicao = new HttpEntity<>(usuarioUpdate, headers);

        ResponseEntity<Usuario> corpoResposta = testRestTemplate.exchange(
                "/usuarios/atualizar/" + usuarioUpdate.getId(), HttpMethod.PUT, corpoRequisicao, Usuario.class);

        assertEquals(HttpStatus.OK, corpoResposta.getStatusCode());
        assertEquals(usuarioUpdate.getNome(), corpoResposta.getBody().getNome());
        assertEquals(usuarioUpdate.getUsuario(), corpoResposta.getBody().getUsuario());
    }

    @Test
    @DisplayName("Listar todos os Usuários")
    public void deveMostrarTodosUsuarios() {
        usuarioService.cadastrarUsuario(new Usuario(0L, "Sabrina Sanches",
                "sabrina_sanches@email.com.br", "sabrina123",
                "https://i.imgur.com/5M2p5Wb.jpg", tipo));

        usuarioService.cadastrarUsuario(new Usuario(0L, "Ricardo Marques",
                "ricardo_marques@email.com.br", "ricardo123",
                "https://i.imgur.com/Sk5SjWE.jpg", tipo));

        HttpHeaders headers = criarHeadersComJWT();
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<String> resposta = testRestTemplate.exchange(
                "/usuarios/all", HttpMethod.GET, entity, String.class);

        assertEquals(HttpStatus.OK, resposta.getStatusCode());
    }
}
