package es.safareyes.coffestation.service;

import es.safareyes.coffestation.dto.RegistroDTO;
import es.safareyes.coffestation.enums.Rol;
import es.safareyes.coffestation.model.Cliente;
import es.safareyes.coffestation.model.Usuario;
import es.safareyes.coffestation.repository.ClienteRepository;
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
    private ClienteRepository clienteRepository;
    private PasswordEncoder passwordEncoder;

    public Usuario registrarCliente(RegistroDTO dto) {
        if (usuarioRepository.existsByNombre(dto.nombre())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El usuario ya existe");
        }
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre(dto.nombre());
        nuevoUsuario.setPassword(passwordEncoder.encode(dto.password()));
        nuevoUsuario.setRol(Rol.CLIENTE);
        usuarioRepository.save(nuevoUsuario);

        Cliente nuevoCliente = new Cliente();
        nuevoCliente.setNombre(dto.nombre());
        nuevoCliente.setEmail(dto.email());
        nuevoCliente.setDni(dto.dni());
        nuevoCliente.setUsuario(nuevoUsuario);

        clienteRepository.save(nuevoCliente);

        return nuevoUsuario;
    }
}
