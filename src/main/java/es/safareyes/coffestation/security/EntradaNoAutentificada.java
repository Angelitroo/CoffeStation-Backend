package es.safareyes.coffestation.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.time.LocalDateTime;

//Respuesta 401 (no autenticado) con el mismo formato ProblemDetail que el resto de errores
@Component
@AllArgsConstructor
public class EntradaNoAutentificada implements AuthenticationEntryPoint {

    private JsonMapper jsonMapper;

    @Override
    public void commence(HttpServletRequest peticion,
                         HttpServletResponse respuesta,
                         AuthenticationException excepcion) throws IOException {

        ProblemDetail problema = ProblemDetail.forStatusAndDetail(
                HttpStatus.UNAUTHORIZED,
                "Necesitas iniciar sesión para acceder a este recurso");
        problema.setTitle("No autenticado");
        problema.setProperty("ruta", peticion.getRequestURI());
        problema.setProperty("timestamp", LocalDateTime.now());

        respuesta.setStatus(HttpStatus.UNAUTHORIZED.value());
        respuesta.setContentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE);
        jsonMapper.writeValue(respuesta.getOutputStream(), problema);
    }
}
