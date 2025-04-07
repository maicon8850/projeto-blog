package br.com.montreal.projeto_blog.model;

import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Table(name = "tb_usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O atributo Nome é obrigatório")
    private String nome;

    @Schema(example = "email@email.com.br")
    @NotBlank(message = "O atributo Usuário é obrigatório")
    @Email(message = "O atributo Usuário deve ser um email válido")
    private String usuario;

    @NotBlank(message = "O atributo Senha é obrigatório")
    @Size(min = 8, message = "A senha deve ter no mínimo 8 caracteres")
    private String senha;

    @Size(max = 5000, message = "O link da foto não pode ser maior que 5000 caracteres")
    private String foto;

    @Enumerated(EnumType.STRING)
    private TipoUsuario tipoUsuario; // ✅ Novo campo enum

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.REMOVE)
    @JsonIgnoreProperties("usuario") // ✅ Corrigido
    private List<Postagem> postagem;

    public Usuario() { }

    // ✅ Construtor completo com tipoUsuario
    public Usuario(Long id, String nome, String usuario, String senha, String foto, TipoUsuario tipoUsuario) {
        this.id = id;
        this.nome = nome;
        this.usuario = usuario;
        this.senha = senha;
        this.foto = foto;
        this.tipoUsuario = tipoUsuario;
    }

    // ✅ Getters e Setters
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

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public List<Postagem> getPostagem() {
        return postagem;
    }

    public void setPostagem(List<Postagem> postagem) {
        this.postagem = postagem;
    }
}