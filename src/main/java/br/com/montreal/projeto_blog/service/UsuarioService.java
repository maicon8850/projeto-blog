package br.com.montreal.projeto_blog.service;

import java.nio.charset.Charset;
import java.util.Optional;

import br.com.montreal.projeto_blog.model.Usuario;
import br.com.montreal.projeto_blog.model.UsuarioLogin;
import br.com.montreal.projeto_blog.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.apache.commons.codec.binary.Base64;



/**
 * ✅ Requisito 1 do projeto (PDF): /api/usuarios
 * Classe responsável pelas regras de negócio relacionadas ao usuário.
 * Atua como uma camada de serviço entre o Controller e o Repository.
 */
@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * ✅ POST /usuarios/cadastrar
     * Cadastra um novo usuário com senha criptografada.
     * Retorna Optional.empty() se o usuário já existir.
     */
    public Optional<Usuario> cadastrarUsuario(Usuario usuario) {

        if (usuarioRepository.findByUsuario(usuario.getUsuario()).isPresent())
            return Optional.empty(); // Usuário já existe

        usuario.setSenha(criptografarSenha(usuario.getSenha())); // Criptografa a senha

        return Optional.of(usuarioRepository.save(usuario)); // Salva o usuário
    }

    /**
     * ✅ PUT /usuarios/{id}
     * Atualiza os dados de um usuário existente, com verificação de duplicidade de e-mail.
     */
    public Optional<Usuario> atualizarUsuario(Usuario usuario) {

        if (usuarioRepository.findById(usuario.getId()).isPresent()) {

            Optional<Usuario> buscaUsuario = usuarioRepository.findByUsuario(usuario.getUsuario());

            // Verifica se o e-mail já está sendo usado por outro usuário
            if ((buscaUsuario.isPresent()) && (buscaUsuario.get().getId() != usuario.getId()))
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário já existe!", null);

            usuario.setSenha(criptografarSenha(usuario.getSenha())); // Criptografa nova senha

            return Optional.ofNullable(usuarioRepository.save(usuario));
        }

        return Optional.empty(); // Usuário não encontrado
    }

    /**
     * ✅ POST /usuarios/logar
     * Autentica um usuário validando a senha digitada com a do banco.
     */
    public Optional<Usuario> autenticarUsuario(UsuarioLogin usuarioLogin) {
        Optional<Usuario> usuario = usuarioRepository.findByUsuario(usuarioLogin.getUsuario());

        if (usuario.isPresent()) {
            if (compararSenhas(usuarioLogin.getSenha(), usuario.get().getSenha())) {
                return usuario; // Login OK
            }
        }

        return Optional.empty(); // Login falhou
    }

    // 🔐 Criptografa a senha com BCrypt
    private String criptografarSenha(String senha) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(senha);
    }

    // 🔐 Compara senha digitada com a criptografada
    private boolean compararSenhas(String senhaDigitada, String senhaBanco) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(senhaDigitada, senhaBanco);
    }

    // (⚠️ não utilizado com JWT) Gera token básico com Base64 - exemplo didático
    //private String gerarBasicToken(String usuario, String senha) {
    // String token = usuario + ":" + senha;
    // byte[] tokenBase64 = Base64.encodeBase64(token.getBytes(Charset.forName("US-ASCII")));
    //  return "Basic " + new String(tokenBase64);}

}
