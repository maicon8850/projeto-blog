package br.com.montreal.projeto_blog.controller;

import br.com.montreal.projeto_blog.model.Postagem;
import br.com.montreal.projeto_blog.model.Tema;
import br.com.montreal.projeto_blog.model.Usuario;
import br.com.montreal.projeto_blog.repository.PostagemRepository;
import br.com.montreal.projeto_blog.repository.TemaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/postagens")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PostagemController {

  @Autowired
  private PostagemRepository postagemRepository;

  @Autowired
  private TemaRepository temaRepository;

  // ✅ GET /postagens - Retorna todas as postagens
  //@GetMapping
  //public ResponseEntity<List<Postagem>> getAll() {
   // return ResponseEntity.ok(postagemRepository.findAll());
 // }
  // ✅ GET /postagens - Retorna todas as postagens
  @GetMapping
  public ResponseEntity<List<Postagem>> getAll() {
    List<Postagem> lista = postagemRepository.findAll();

    for (Postagem p : lista) {
      if (p.getUsuario() == null) p.setUsuario(new Usuario());
      if (p.getTema() == null) p.setTema(new Tema());
    }

    return ResponseEntity.ok(lista);
  }



  // ✅ GET /postagens/{id} - Busca postagem pelo ID
  @GetMapping("/{id}")
  public ResponseEntity<Postagem> getById(@PathVariable Long id) {
    return postagemRepository.findById(id)
      .map(ResponseEntity::ok)
      .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
  }

  // ✅ GET /postagens/titulo/{titulo} - Busca postagem pelo título
  @GetMapping("/titulo/{titulo}")
  public ResponseEntity<List<Postagem>> getByTitulo(@PathVariable String titulo) {
    return ResponseEntity.ok(postagemRepository.findAllByTituloContainingIgnoreCase(titulo));
  }

  // ✅ POST /postagens - Cadastra uma postagem
  @PostMapping
  public ResponseEntity<Postagem> post(@Valid @RequestBody Postagem postagem) {
    if (temaRepository.existsById(postagem.getTema().getId())) {
      return ResponseEntity.status(HttpStatus.CREATED).body(postagemRepository.save(postagem));
    }

    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tema não encontrado!");
  }

  // ✅ PUT /postagens - Atualiza uma postagem existente
  @PutMapping
  public ResponseEntity<Postagem> put(@Valid @RequestBody Postagem postagem) {
    if (postagemRepository.existsById(postagem.getId())) {
      if (temaRepository.existsById(postagem.getTema().getId())) {
        return ResponseEntity.status(HttpStatus.OK).body(postagemRepository.save(postagem));
      }
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tema não encontrado!");
    }
    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
  }

  // ✅ DELETE /postagens/{id} - Deleta uma postagem pelo ID
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @DeleteMapping("/{id}")
  public void delete(@PathVariable Long id) {
    Optional<Postagem> postagem = postagemRepository.findById(id);

    if (postagem.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Postagem não encontrada!");
    }

    postagemRepository.deleteById(id);
  }
}
