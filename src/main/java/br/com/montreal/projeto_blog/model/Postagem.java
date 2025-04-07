package br.com.montreal.projeto_blog.model;

import java.time.LocalDateTime;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity // Indica que essa classe é uma entidade JPA, representando uma tabela no banco
@Table(name= "tb_postagens") // Define o nome da tabela
public class Postagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Chave primária auto-incrementada
    private Long id;

    @NotBlank(message = "O atributo é obrigatório!")
    @Size(min = 5, max = 100, message = "O atributo título deve conter no mínimo 05 e no máximo 100 caracteres")
    private String titulo;

    @NotBlank(message = "O atributo é obrigatório!")
    @Size(min = 10, max = 1000, message = "O atributo título deve conter no mínimo 10 e no máximo 1000 caracteres")
    private String texto;

    @UpdateTimestamp // Atualiza automaticamente a data quando a postagem é modificada
    private LocalDateTime data;

    @ManyToOne // Associação: Muitas postagens podem pertencer a um tema (requisito de relacionamento)
    @JsonIgnoreProperties("postagem") // Evita loop infinito na serialização JSON
    private Tema tema;

    @ManyToOne // Associação: Muitas postagens pertencem a um único usuário
    @JsonIgnoreProperties("postagem")
    private Usuario usuario;

    // Getters e Setters (Encapsulamento: acesso controlado aos atributos)
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