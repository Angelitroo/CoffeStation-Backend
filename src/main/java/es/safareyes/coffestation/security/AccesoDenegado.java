package es.safareyes.coffestation.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.time.LocalDateTime;

//Respuesta 403 (sin permisos) con el mismo formato ProblemDetail que el resto de errores
@Component
@AllArgsConstructor
public class AccesoDenegado implements AccessDeniedHandler {

    private JsonMapper jsonMapper;

    @Override
    public void handle(HttpServletRequest peticion,
                       HttpServletResponse respuesta,
                       AccessDeniedException excepcion) throws IOException {

        ProblemDetail problema = ProblemDetail.forStatusAndDetail(
                HttpStatus.FORBIDDEN,
                "Tu cuenta no tiene permisos para esta operación");
        problema.setTitle("Acceso denegado");
        problema.setProperty("ruta", peticion.getRequestURI());
        problema.setProperty("timestamp", LocalDateTime.now());

        respuesta.setStatus(HttpStatus.FORBIDDEN.value());
        respuesta.setContentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE);
        jsonMapper.writeValue(respuesta.getOutputStream(), problema);
    }
}
