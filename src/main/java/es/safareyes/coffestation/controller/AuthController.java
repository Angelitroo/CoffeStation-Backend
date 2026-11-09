package es.safareyes.coffestation.controller;

import es.safareyes.coffestation.dto.LoginDTO;
import es.safareyes.coffestation.dto.RegistroDTO;
import es.safareyes.coffestation.dto.TokenDTO;
import es.safareyes.coffestation.security.JwtService;
import es.safareyes.coffestation.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private AuthenticationManager authenticationManager;
    private JwtService jwtService;
    private UsuarioService usuarioService;

    @PostMapping("/login")
    public TokenDTO login(@Valid @RequestBody LoginDTO dto) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.nombre(), dto.password()));
        return new TokenDTO(jwtService.generarToken(auth));
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void registro(@Valid @RequestBody RegistroDTO dto) {
        usuarioService.registrarCliente(dto);
    }
}
