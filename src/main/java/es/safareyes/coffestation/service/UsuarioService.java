package es.safareyes.coffestation.service;

import es.safareyes.coffestation.enums.Rol;
import es.safareyes.coffestation.model.Usuario;
import es.safareyes.coffestation.repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@AllArgsConstructor
public class UsuarioService {

    private UsuarioRepository usuarioRepository;
    private PasswordEncoder passwordEncoder;

    public Usuario registrarCliente(String nombre, String password) {
        if (usuarioRepository.existsByNombre(nombre)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El usuario ya existe");
        }
        Usuario usuario = new Usuario(null, nombre, passwordEncoder.encode(password), Rol.CLIENTE);
        return usuarioRepository.save(usuario);
    }
}
