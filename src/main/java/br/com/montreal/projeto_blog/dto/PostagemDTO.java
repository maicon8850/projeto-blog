package br.com.montreal.projeto_blog.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class PostagemDTO {

    @NotBlank
    @Size(min = 5, max = 100)
    private String titulo;

    @NotBlank
    @Size(min = 10, max = 1000)
    private String texto;

    private Long temaId;

    public PostagemDTO() { }

    public PostagemDTO(String titulo, String texto, Long temaId) {
        this.titulo = titulo;
        this.texto = texto;
        this.temaId = temaId;
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

    public Long getTemaId() {
        return temaId;
    }

    public void setTemaId(Long temaId) {
        this.temaId = temaId;
    }
}
