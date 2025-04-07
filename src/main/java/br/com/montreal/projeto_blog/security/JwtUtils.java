package br.com.montreal.projeto_blog.security;

import java.security.Key;
import java.util.Date;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import org.springframework.stereotype.Component;

import br.com.montreal.projeto_blog.model.Usuario;

/**
 * ✅ Requisito 5 - Utilitário de manipulação de JWT (criação, validação e leitura do token)
 */
@Component
public class JwtUtils {

    // 🔐 Chave secreta para assinar o token JWT (deve ter pelo menos 32 caracteres)
    private static final String SECRET = "blogpessoalblogpessoalblogpessoal123";

    // ⏰ Tempo de expiração do token (1 dia = 86400000 ms)
    private static final long EXPIRATION_TIME = 86400000;

    // 🔑 Gera uma chave segura a partir da constante SECRET
    private static final Key KEY = Keys.hmacShaKeyFor(SECRET.getBytes());

    /**
     * ✅ Gera um token JWT com os dados do usuário (id, nome e e-mail)
     * Esse token é devolvido ao cliente após o login com sucesso.
     */
    public String generateToken(Usuario usuario) {
        return Jwts.builder()
                .setSubject(usuario.getUsuario())                        // Define o assunto (e-mail do usuário)
                .claim("id", usuario.getId())                            // Adiciona o ID como uma informação extra (claim)
                .claim("nome", usuario.getNome())                        // Adiciona o nome como claim
                .setIssuedAt(new Date())                                 // Define a data de emissão
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // Define a expiração
                .signWith(KEY)                                           // Assina com a chave segura
                .compact();                                              // Compacta o token para string final
    }

    /**
     * ✅ Extrai o nome do usuário (e-mail) de dentro do token JWT.
     * É chamado durante a autenticação para identificar o usuário.
     */
    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject(); // Retorna o "subject" definido na criação do token
    }

    /**
     * ✅ Valida se o token é autêntico, não expirou e está bem formado.
     * Se qualquer erro ocorrer, o token será considerado inválido.
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(KEY)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false; // ❌ Token inválido, expirado ou mal formado
        }
    }
}