package br.com.montreal.projeto_blog.controller;


import br.com.montreal.projeto_blog.model.Postagem;
import br.com.montreal.projeto_blog.repository.PostagemRepository;


import br.com.montreal.projeto_blog.repository.TemaRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController // Marca a classe como um Controller REST (Requisito: definir endpoints)
@RequestMapping("/postagens") // Endpoint base: /postagens
@CrossOrigin(origins = "*", allowedHeaders = "*") // Permite requisições de outras origens
@SecurityRequirement(name = "bearerAuth")
public class PostagemController {

    @Autowired // Injeção de dependência: abstrai a criação manual dos repositórios
    private PostagemRepository postagemRepository;

    @Autowired
    private TemaRepository temaRepository;

    // Requisito: GET / - Listar todas as postagens
    @GetMapping
    public ResponseEntity<List<Postagem>> getAll() {
        return ResponseEntity.ok(postagemRepository.findAll());
    }

    // Requisito: GET /{id} - Buscar postagem por ID
    @GetMapping("/{id}")
    public ResponseEntity<Postagem> getById(@PathVariable Long id) {
        return postagemRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Método complementar: GET /titulo/{titulo} - Filtrar postagens por título (opcional)
    @GetMapping("/titulo/{titulo}")
    public ResponseEntity<List<Postagem>> getByTitulo(@PathVariable String titulo) {
        return ResponseEntity.ok(postagemRepository.findAllByTituloContainingIgnoreCase(titulo));
    }

    // Método complementar: GET /autor/{nome} - Filtrar postagens por nome do autor (opcional)
    @GetMapping("/autor/{nome}")
    public ResponseEntity<List<Postagem>> getByAutor(@PathVariable String nome) {
        return ResponseEntity.ok(postagemRepository.findAllByUsuario_NomeContainingIgnoreCase(nome));
    }

    // Requisito: POST / - Criar uma nova postagem
    @PostMapping
    public ResponseEntity<Postagem> post(@Valid @RequestBody Postagem postagem) {
        // Verifica se o tema associado existe (requisito: manter integridade referencial)
        if (temaRepository.existsById(postagem.getTema().getId())) {
            // Cria a postagem e retorna status 201 (Created)
            return ResponseEntity.status(HttpStatus.CREATED).body(postagemRepository.save(postagem));
        }
        // Caso o tema não exista, retorna Bad Request
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    // Requisito: PUT /{id} - Atualizar uma postagem existente
    @PutMapping("/{id}")
    public ResponseEntity<Postagem> put(@PathVariable Long id, @Valid @RequestBody Postagem postagem) {
        // Verifica se a postagem existe; se não, retorna 404 (Not Found)
        if (!postagemRepository.existsById(id))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        // Verifica se o tema associado existe; se não, retorna 400 (Bad Request)
        if (!temaRepository.existsById(postagem.getTema().getId()))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        // Define o ID para atualização e salva a postagem atualizada
        postagem.setId(id);
        return ResponseEntity.ok(postagemRepository.save(postagem));
    }

    // Requisito: DELETE /{id} - Excluir uma postagem existente
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        Optional<Postagem> postagem = postagemRepository.findById(id);
        if (postagem.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        postagemRepository.deleteById(id);
    }

    // Requisito: GET /filtro?autor={id}&tema={id} - Filtrar postagens por autor e/ou tema
    @GetMapping("/filtro")
    public ResponseEntity<List<Postagem>> getByAutorAndTema(
            @RequestParam(required = false) String autor,
            @RequestParam(required = false) Long tema) {

        // A lógica de filtro (por autor e/ou tema) está encapsulada no método do repositório
        return ResponseEntity.ok(postagemRepository.findByAutorAndTema(autor, tema));
    }
}