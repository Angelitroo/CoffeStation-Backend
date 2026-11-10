package es.safareyes.coffestation.security;

import es.safareyes.coffestation.exceptions.RecursoNoEncontradoException;
import es.safareyes.coffestation.model.Usuario;
import es.safareyes.coffestation.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class JwtService {

    private final JwtEncoder jwtEncoder;
    private final long expiracionMinutos;

    private final UsuarioRepository usuarioRepository;

    public JwtService(JwtEncoder jwtEncoder,
                      UsuarioRepository usuarioRepository,
                      @Value("${jwt.expiracion-minutos}") long expiracionMinutos) {
        this.jwtEncoder = jwtEncoder;
        this.usuarioRepository = usuarioRepository;
        this.expiracionMinutos = expiracionMinutos;
    }

    public String generarToken(Authentication authentication) {
        Instant ahora = Instant.now();

        // "ROLE_ADMIN" -> "ADMIN" (se ignoran otras authorities como FACTOR_PASSWORD)
        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(a -> a.startsWith("ROLE_"))
                .map(a -> a.substring("ROLE_".length()))
                .toList();


        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("coffestation")
                .issuedAt(ahora)
                .expiresAt(ahora.plus(expiracionMinutos, ChronoUnit.MINUTES))
                .subject(authentication.getName())
                .claim("roles", roles)
                .build();

        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
        return jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
    }


    public Usuario getUsuarioActual() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String nombre = auth.getName(); // es el "sub" del token
        return usuarioRepository.findByNombre(nombre)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado: " + nombre));
    }
}
