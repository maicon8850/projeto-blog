package br.com.montreal.projeto_blog.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import br.com.montreal.projeto_blog.dto.UsuarioDTO;
import br.com.montreal.projeto_blog.dto.UsuarioResponseDTO;
import br.com.montreal.projeto_blog.model.JwtResponse;
import br.com.montreal.projeto_blog.model.Usuario;
import br.com.montreal.projeto_blog.model.UsuarioLogin;
import br.com.montreal.projeto_blog.repository.UsuarioRepository;
import br.com.montreal.projeto_blog.security.JwtUtils;
import br.com.montreal.projeto_blog.service.UsuarioService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "usuario")
@SecurityRequirement(name = "bearerAuth")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JwtUtils jwtUtils;

    // ✅ GET /usuarios/all - Lista todos os usuários como ResponseDTO
    @GetMapping("/all")
    public ResponseEntity<List<UsuarioResponseDTO>> getAll() {
        List<UsuarioResponseDTO> usuarios = usuarioRepository.findAll()
                .stream()
                .map(u -> new UsuarioResponseDTO(u.getId(), u.getNome(), u.getUsuario(), u.getFoto()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(usuarios);
    }

    // ✅ GET /usuarios/id/{id} - Retorna um usuário pelo ID como ResponseDTO
    @GetMapping("/id/{id}")
    public ResponseEntity<UsuarioResponseDTO> getById(@PathVariable Long id) {
        return usuarioRepository.findById(id)
                .map(u -> ResponseEntity.ok(new UsuarioResponseDTO(u.getId(), u.getNome(), u.getUsuario(), u.getFoto())))
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

    // ✅ POST /usuarios/cadastrar - Recebe DTO e retorna ResponseDTO
    @PostMapping("/cadastrar")
    public ResponseEntity<UsuarioResponseDTO> postUsuario(@Valid @RequestBody UsuarioDTO dto) {
        Usuario usuario = new Usuario(null, dto.getNome(), dto.getUsuario(), dto.getSenha(), dto.getFoto(), null);

        return usuarioService.cadastrarUsuario(usuario)
                .map(u -> ResponseEntity.status(HttpStatus.CREATED)
                        .body(new UsuarioResponseDTO(u.getId(), u.getNome(), u.getUsuario(), u.getFoto())))
                .orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    // ✅ PUT /usuarios/atualizar/{id} - Atualiza com DTO e retorna ResponseDTO
    @PutMapping("/atualizar/{id}")
    public ResponseEntity<UsuarioResponseDTO> putUsuario(@PathVariable Long id,
                                                         @Valid @RequestBody UsuarioDTO dto) {
        Usuario usuario = new Usuario(id, dto.getNome(), dto.getUsuario(), dto.getSenha(), dto.getFoto(), null);

        return usuarioService.atualizarUsuario(usuario)
                .map(u -> ResponseEntity.ok(new UsuarioResponseDTO(u.getId(), u.getNome(), u.getUsuario(), u.getFoto())))
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ DELETE /usuarios/deletar/{id}
    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable Long id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        if (usuario.isEmpty())
            return ResponseEntity.notFound().build();

        usuarioRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
