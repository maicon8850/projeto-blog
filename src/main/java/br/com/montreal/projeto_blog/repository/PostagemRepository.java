package br.com.montreal.projeto_blog.repository;

import java.util.List;

import br.com.montreal.projeto_blog.model.Postagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository // Indica que essa interface é um componente de persistência (Repository)
public interface PostagemRepository extends JpaRepository<Postagem, Long> {

    // Requisito complementar: Permite buscar postagens pelo título (case-insensitive)
    public List<Postagem> findAllByTituloContainingIgnoreCase(@Param("titulo") String titulo);

    // Requisito complementar: Permite buscar postagens pelo nome do autor (case-insensitive)
    public List<Postagem> findAllByUsuario_NomeContainingIgnoreCase(@Param("nome") String nome);

    // Requisito: GET /filtro?autor={id}&tema={id} - Método customizado usando JPQL para filtrar por autor e/ou tema
    @Query("SELECT p FROM Postagem p WHERE " +
            "(:autor IS NULL OR LOWER(p.usuario.nome) LIKE LOWER(CONCAT('%', :autor, '%'))) AND " +
            "(:tema IS NULL OR p.tema.id = :tema)")
    List<Postagem> findByAutorAndTema(@Param("autor") String autor, @Param("tema") Long tema);
}