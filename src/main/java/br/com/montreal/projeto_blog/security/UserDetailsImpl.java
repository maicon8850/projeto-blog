package br.com.montreal.projeto_blog.security;


import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.montreal.projeto_blog.model.Usuario;

/**
 * ✅ Requisito 5: Classe responsável por fornecer os dados do usuário para o Spring Security.
 * Implementa a interface UserDetails, usada para autenticação e autorização.
 */
public class UserDetailsImpl implements UserDetails {

    private static final long serialVersionUID = 1L;

    // Armazena o e-mail (login) do usuário
    private String userName;

    // Armazena a senha do usuário
    private String password;

    // Autoridades (perfis, papéis) - ainda não utilizado no projeto
    private List<GrantedAuthority> authorities;

    /**
     * ✅ Construtor que recebe o objeto Usuario e extrai os dados relevantes para autenticação
     */
    public UserDetailsImpl(Usuario user) {
        this.userName = user.getUsuario(); // Define o login (e-mail)
        this.password = user.getSenha();   // Define a senha
    }

    // Construtor vazio necessário para o Spring
    public UserDetailsImpl() { }

    // Retorna a lista de permissões (ainda vazia neste projeto)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password; // Retorna a senha
    }

    @Override
    public String getUsername() {
        return userName; // Retorna o login (e-mail)
    }

    // A conta não está expirada
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // A conta não está bloqueada
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // As credenciais (senha) não estão expiradas
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // A conta está ativa
    @Override
    public boolean isEnabled() {
        return true;
    }
}