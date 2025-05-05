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
  private Long usuarioId;

  public PostagemDTO() { }

  public PostagemDTO(String titulo, String texto, Long temaId, Long usuarioId) {
    this.titulo = titulo;
    this.texto = texto;
    this.temaId = temaId;
    this.usuarioId = usuarioId;
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

  public Long getUsuarioId() {
    return usuarioId;
  }

  public void setUsuarioId(Long usuarioId) {
    this.usuarioId = usuarioId;
  }
}
