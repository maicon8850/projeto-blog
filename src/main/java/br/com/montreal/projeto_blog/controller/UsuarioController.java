package br.com.montreal.projeto_blog.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;


import br.com.montreal.projeto_blog.model.JwtResponse;
import br.com.montreal.projeto_blog.model.Usuario;
import br.com.montreal.projeto_blog.model.UsuarioLogin;
import br.com.montreal.projeto_blog.repository.UsuarioRepository;
import br.com.montreal.projeto_blog.security.JwtUtils;
import br.com.montreal.projeto_blog.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * ✅ Requisitos do Projeto - Usuário (/api/usuarios):
 * - POST /cadastrar: Criar novo usuário.
 * - PUT /{id}: Atualizar um usuário existente.
 * - DELETE /{id}: Excluir um usuário.
 * - GET /: Listar todos os usuários.
 * - GET /{id}: Buscar usuário por ID.
 * - POST /logar: Autenticar usuário e gerar token JWT.
 */

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UsuarioController {

    // 🔹 Injeção de dependência: aplicando o princípio da inversão de controle (IoC)
    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JwtUtils jwtUtils;

    // ✅ GET /usuarios - Lista todos os usuários cadastrados
    @GetMapping
    public ResponseEntity<List<Usuario>> getAll() {
        return ResponseEntity.ok(usuarioRepository.findAll());
    }

    // ✅ GET /usuarios/{id} - Retorna um usuário pelo ID
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getById(@PathVariable Long id) {
        return usuarioRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ POST /usuarios/logar - Autenticação de usuário e geração de JWT
    @PostMapping("/logar")
    public ResponseEntity<JwtResponse> logar(@RequestBody UsuarioLogin usuarioLogin) {
        Optional<Usuario> usuario = usuarioService.autenticarUsuario(usuarioLogin);

        if (usuario.isPresent()) {
            String token = jwtUtils.generateToken(usuario.get());
            return ResponseEntity.ok(
                    new JwtResponse(token, usuario.get().getNome(), usuario.get().getUsuario()));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    // ✅ POST /usuarios/cadastrar - Criação de novo usuário
    @PostMapping("/cadastrar")
    public ResponseEntity<Usuario> postUsuario(@Valid @RequestBody Usuario usuario) {
        return usuarioService.cadastrarUsuario(usuario)
                .map(resposta -> ResponseEntity.status(HttpStatus.CREATED).body(resposta))
                .orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    // ✅ PUT /usuarios/{id} - Atualização de usuário existente
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> putUsuario(@PathVariable Long id, @Valid @RequestBody Usuario usuario) {
        usuario.setId(id); // Garante que estamos atualizando o usuário correto
        return usuarioService.atualizarUsuario(usuario)
                .map(resposta -> ResponseEntity.ok(resposta))
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ DELETE /usuarios/{id} - Exclusão de usuário
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable Long id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        if (usuario.isEmpty())
            return ResponseEntity.notFound().build();

        usuarioRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}