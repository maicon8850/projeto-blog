package br.com.montreal.projeto_blog.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import br.com.montreal.projeto_blog.model.Tema;
import br.com.montreal.projeto_blog.repository.TemaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;


/**
 * 3. Tema (/api/temas)
 * POST /: Criar um novo tema.
 * PUT /{id}: Atualizar um tema existente.
 * DELETE /{id}: Excluir um tema.
 * GET /: Listar todos os temas.
 */

@RestController
@RequestMapping("/temas")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TemaController {

    @Autowired
    private TemaRepository temaRepository;

    //http://localhost:8080/temas
    @GetMapping
    public ResponseEntity<List<Tema>> getAll(){
        return ResponseEntity.ok(temaRepository.findAll());
    }

    //http://localhost:8080/temas/1
    @GetMapping("/{id}")
    public ResponseEntity<Tema> getById(@PathVariable Long id){
        return temaRepository.findById(id)
                .map(resposta -> ResponseEntity.ok(resposta))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    //http://localhost:8080/temas/descricao/tema 01
    @GetMapping("/descricao/{descricao}")
    public ResponseEntity<List<Tema>> getByTitle(@PathVariable
                                                 String descricao){
        return ResponseEntity.ok(temaRepository
                .findAllByDescricaoContainingIgnoreCase(descricao));
    }

    //http://localhost:8080/temas
    @PostMapping
    public ResponseEntity<Tema> post(@Valid @RequestBody Tema tema){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(temaRepository.save(tema));
    }

    //http://localhost:8080/temas
    @PutMapping("/{id}")
    public ResponseEntity<Tema> put(@PathVariable Long id, @Valid @RequestBody Tema tema){
        return temaRepository.findById(id)
                .map(resposta -> {
                    tema.setId(id);
                    return ResponseEntity.status(HttpStatus.OK).body(temaRepository.save(tema));
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }


    //http://localhost:8080/temas/1
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        Optional<Tema> tema = temaRepository.findById(id);

        if(tema.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        temaRepository.deleteById(id);
    }

}