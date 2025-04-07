package br.com.montreal.projeto_blog.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * ✅ Requisito 5 - Autenticação e autorização com Spring Security + JWT
 * Esta classe configura a segurança do sistema, permitindo acesso às rotas públicas e protegendo as demais.
 */

@Configuration
@EnableWebSecurity
public class BasicSecurityConfig {

    // 🔐 Injeção do filtro personalizado de autenticação JWT
    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    // 🔐 BCrypt é utilizado para criptografar senhas ao cadastrar/atualizar usuários
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 🔐 Fornece o gerenciador de autenticação com suporte ao JWT
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration auth) throws Exception {
        return auth.getAuthenticationManager();
    }

    /**
     * 🔐 Configura a cadeia de filtros de segurança:
     * - Desativa CSRF (proteção desnecessária para APIs REST)
     * - Ativa CORS (permite requisições de outros domínios)
     * - Define política sem sessão (stateless)
     * - Libera acesso a /usuarios/cadastrar e /usuarios/logar
     * - Bloqueia o restante das rotas, exigindo autenticação
     * - Adiciona o filtro JWT antes do filtro de autenticação padrão
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .cors().and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/usuarios/logar", "/usuarios/cadastrar").permitAll()
                .antMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                .antMatchers(HttpMethod.OPTIONS.name()).permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}