package es.safareyes.coffestation.security;

import es.safareyes.coffestation.model.Usuario;
import es.safareyes.coffestation.repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private UsuarioRepository usuarioRepository;

    //Busca usuario en bbdd para ver su contraseña/rol
    @Override
    public UserDetails loadUserByUsername(String nombre) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByNombre(nombre)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + nombre));

        return User.builder()
                .username(usuario.getNombre())
                .password(usuario.getPassword())
                .roles(usuario.getRol().name()) // ADMIN/BARISTA/CLIENTE
                .build();
    }
}

