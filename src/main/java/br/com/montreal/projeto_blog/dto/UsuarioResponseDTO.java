package br.com.montreal.projeto_blog.dto;

public class UsuarioResponseDTO {

    private Long id;
    private String nome;
    private String usuario;
    private String foto;

    public UsuarioResponseDTO() { }

    public UsuarioResponseDTO(Long id, String nome, String usuario, String foto) {
        this.id = id;
        this.nome = nome;
        this.usuario = usuario;
        this.foto = foto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
