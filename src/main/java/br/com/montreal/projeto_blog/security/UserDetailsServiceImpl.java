package br.com.montreal.projeto_blog.security;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.com.montreal.projeto_blog.model.Usuario;
import br.com.montreal.projeto_blog.repository.UsuarioRepository;

/**
 * ✅ Requisito 5 (PDF): Autenticação com JWT
 * Classe que implementa UserDetailsService, usada pelo Spring Security para buscar os dados do usuário no banco.
 * Essa classe é acionada automaticamente durante o login (autenticação).
 */

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository; // Injeta o repositório de usuários

    /**
     * ✅ Método obrigatório da interface UserDetailsService.
     * Chamado automaticamente pelo Spring Security ao tentar autenticar um usuário.
     * Busca o usuário pelo nome (e-mail) e retorna um objeto UserDetailsImpl se encontrado.
     */
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

        // Busca o usuário no banco pelo nome (e-mail)
        Optional<Usuario> usuario = usuarioRepository.findByUsuario(userName);

        // Se encontrar, retorna um UserDetailsImpl com os dados
        if (usuario.isPresent())
            return new UserDetailsImpl(usuario.get());

        // Caso contrário, lança exceção 403 (Acesso negado)
        throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    }
}