package br.com.montreal.projeto_blog.model;


// ✅ Requisito Funcional 4: Modelo de resposta para autenticação com JWT
public class JwtResponse {

    // ✅ Token JWT gerado ao autenticar o usuário
    private String token;

    //
    private Long id;


    // ✅ Nome do usuário autenticado
    private String nome;

    // ✅ E-mail (ou username) do usuário autenticado
    private String usuario;

    // ✅ Construtor com todos os atributos
    public JwtResponse(String token, Long id, String nome, String usuario) {
      this.token = token;
      this.id = id;
      this.nome = nome;
      this.usuario = usuario;
    }

    // ✅ Getter para acessar o token na resposta
    public String getToken() {
        return token;
    }
  public Long getId() {
    return id;
  }

    // ✅ Getter para acessar o nome do usuário
    public String getNome() {
        return nome;
    }

    // ✅ Getter para acessar o e-mail (ou login) do usuário
    public String getUsuario() {
        return usuario;
    }
}
