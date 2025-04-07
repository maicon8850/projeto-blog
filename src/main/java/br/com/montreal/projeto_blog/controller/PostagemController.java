package br.com.montreal.projeto_blog.controller;

import br.com.montreal.projeto_blog.dto.PostagemDTO;
import br.com.montreal.projeto_blog.dto.PostagemResponseDTO;
import br.com.montreal.projeto_blog.model.Postagem;
import br.com.montreal.projeto_blog.model.Tema;
import br.com.montreal.projeto_blog.model.Usuario;
import br.com.montreal.projeto_blog.repository.PostagemRepository;
import br.com.montreal.projeto_blog.repository.TemaRepository;
import br.com.montreal.projeto_blog.repository.UsuarioRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/postagens")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@SecurityRequirement(name = "bearerAuth")
public class PostagemController {

    @Autowired
    private PostagemRepository postagemRepository;

    @Autowired
    private TemaRepository temaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // ✅ GET /postagens
    @GetMapping
    public ResponseEntity<List<PostagemResponseDTO>> getAll() {
        List<PostagemResponseDTO> lista = postagemRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(lista);
    }

    // ✅ GET /postagens/{id}
    @GetMapping("/{id}")
    public ResponseEntity<PostagemResponseDTO> getById(@PathVariable Long id) {
        return postagemRepository.findById(id)
                .map(post -> ResponseEntity.ok(toResponseDTO(post)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // ✅ GET /postagens/titulo/{titulo}
    @GetMapping("/titulo/{titulo}")
    public ResponseEntity<List<PostagemResponseDTO>> getByTitulo(@PathVariable String titulo) {
        List<PostagemResponseDTO> lista = postagemRepository
                .findAllByTituloContainingIgnoreCase(titulo)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(lista);
    }

    // ✅ GET /postagens/autor/{nome}
    @GetMapping("/autor/{nome}")
    public ResponseEntity<List<PostagemResponseDTO>> getByAutor(@PathVariable String nome) {
        List<PostagemResponseDTO> lista = postagemRepository
                .findAllByUsuario_NomeContainingIgnoreCase(nome)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(lista);
    }

    // ✅ POST /postagens
    @PostMapping
    public ResponseEntity<PostagemResponseDTO> post(@Valid @RequestBody PostagemDTO dto) {

        Tema tema = temaRepository.findById(dto.getTemaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tema não encontrado"));

        // Pegando o usuário logado (no seu sistema real, substitua pela autenticação JWT se quiser)
        Usuario usuario = usuarioRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário não encontrado"));

        Postagem postagem = new Postagem();
        postagem.setTitulo(dto.getTitulo());
        postagem.setTexto(dto.getTexto());
        postagem.setTema(tema);
        postagem.setUsuario(usuario);

        Postagem salva = postagemRepository.save(postagem);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponseDTO(salva));
    }

    // ✅ PUT /postagens/{id}
    @PutMapping("/{id}")
    public ResponseEntity<PostagemResponseDTO> put(@PathVariable Long id, @Valid @RequestBody PostagemDTO dto) {
        Postagem postagem = postagemRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Tema tema = temaRepository.findById(dto.getTemaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tema não encontrado"));

        postagem.setTitulo(dto.getTitulo());
        postagem.setTexto(dto.getTexto());
        postagem.setTema(tema);

        Postagem atualizada = postagemRepository.save(postagem);
        return ResponseEntity.ok(toResponseDTO(atualizada));
    }

    // ✅ DELETE /postagens/{id}
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        if (!postagemRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        postagemRepository.deleteById(id);
    }

    // ✅ GET /postagens/filtro?autor=...&tema=...
    @GetMapping("/filtro")
    public ResponseEntity<List<PostagemResponseDTO>> getByAutorAndTema(
            @RequestParam(required = false) String autor,
            @RequestParam(required = false) Long tema) {

        List<PostagemResponseDTO> lista = postagemRepository
                .findByAutorAndTema(autor, tema)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(lista);
    }

    // ✅ Conversão de entidade para DTO de resposta
    private PostagemResponseDTO toResponseDTO(Postagem postagem) {
        return new PostagemResponseDTO(
                postagem.getId(),
                postagem.getTitulo(),
                postagem.getTexto(),
                postagem.getData(),
                postagem.getTema().getDescricao(),
                postagem.getUsuario().getNome()
        );
    }
}
