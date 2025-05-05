package br.com.montreal.projeto_blog.security;

import java.util.Collection;
import java.util.Collections;

import br.com.montreal.projeto_blog.model.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * ✅ Representa o usuário autenticado no sistema.
 * Implementa UserDetails para que o Spring Security reconheça e use.
 */
public class UserDetailsImpl implements UserDetails {

  private final Usuario usuario;

  public UserDetailsImpl(Usuario usuario) {
    this.usuario = usuario;
  }

  public Usuario getUsuario() {
    return usuario;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    // Você pode retornar roles aqui se usar permissões por perfil
    return Collections.emptyList();
  }

  @Override
  public String getPassword() {
    return usuario.getSenha();
  }

  @Override
  public String getUsername() {
    return usuario.getUsuario();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
