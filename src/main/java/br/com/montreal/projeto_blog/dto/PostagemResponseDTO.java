package br.com.montreal.projeto_blog.dto;

import java.time.LocalDateTime;

public class PostagemResponseDTO {

  private Long id;
  private String titulo;
  private String texto;
  private LocalDateTime data;
  private String temaDescricao;
  private String autorNome;

  public PostagemResponseDTO() {}

  public PostagemResponseDTO(Long id, String titulo, String texto, LocalDateTime data, String temaDescricao, String autorNome) {
    this.id = id;
    this.titulo = titulo;
    this.texto = texto;
    this.data = data;
    this.temaDescricao = temaDescricao;
    this.autorNome = autorNome;
  }

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

  public String getTemaDescricao() {
    return temaDescricao;
  }

  public void setTemaDescricao(String temaDescricao) {
    this.temaDescricao = temaDescricao;
  }

  public String getAutorNome() {
    return autorNome;
  }

  public void setAutorNome(String autorNome) {
    this.autorNome = autorNome;
  }
}
