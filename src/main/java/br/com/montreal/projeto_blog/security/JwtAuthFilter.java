package br.com.montreal.projeto_blog.security;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * ✅ Requisito 5 - Autenticação com JWT
 * Filtro que intercepta cada requisição, extrai o token e autentica o usuário no contexto de segurança do Spring.
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    // 🔐 Utilitário que manipula e valida tokens JWT
    @Autowired
    private JwtUtils jwtUtils;

    // 🔐 Serviço que carrega os dados do usuário pelo nome de usuário (e-mail)
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    /**
     * 🔎 Intercepta a requisição HTTP para verificar se há um token JWT válido.
     * Caso exista e seja válido, autentica o usuário no contexto de segurança.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 🔍 Busca o cabeçalho Authorization da requisição (esperado: "Bearer <token>")
        String authHeader = request.getHeader("Authorization");

        // ✅ Verifica se o token começa com "Bearer " e está presente
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // 🔪 Remove "Bearer " para extrair o token real
            String username = jwtUtils.getUsernameFromToken(token); // 🧠 Extrai o nome do usuário contido no token

            // 🔒 Verifica se o usuário está presente e ainda não autenticado
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // ✅ Valida o token antes de seguir com a autenticação
                if (jwtUtils.validateToken(token)) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    // 🧾 Adiciona detalhes da requisição ao token
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // 🔐 Registra o usuário autenticado no contexto do Spring Security
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        }

        // 🔁 Continua o fluxo de filtros (importante para que a aplicação funcione normalmente)
        filterChain.doFilter(request, response);
    }
}