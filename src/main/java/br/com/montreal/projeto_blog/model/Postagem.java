package br.com.montreal.projeto_blog.model;
import java.time.LocalDateTime;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity  // diz ao JPA que essa classe é uma entidade
@Table(name= "tb_postagens") // nome da tabela do banco de dados

public class Postagem {

    @Id  // chave or
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Anotação que
    @NotBlank(message = "O atributo é obrigatório!")
    @Size(min = 5, max = 100, message = "O atributo título deve conter no mínimo 05 e no máximo 100 caracteres")
    private String titulo;

    @NotBlank(message = "O atributo é obrigatório!")
    @Size(min = 10, max = 1000, message = "O atributo título deve conter no mínimo 10 e no máximo 1000 caracteres")
    private String texto;

    @UpdateTimestamp
    private LocalDateTime data;

    @ManyToOne
    @JsonIgnoreProperties("postagem")
    private Tema tema;

    @ManyToOne
    @JsonIgnoreProperties("postagem")
    private Usuario usuario;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public Tema getTema() {
        return tema;
    }

    public void setTema(Tema tema) {
        this.tema = tema;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

}
