package masera.deviajeusersandauth.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * Maneja las excepciones de autenticación que ocurren cuando un usuario intenta
 * acceder a un recurso protegido sin estar autenticado correctamente.
 * Implementa la interfaz AuthenticationEntryPoint que invoca Spring Security
 * para cuando ocurre una excepción de autenticación.
 */
@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

  private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);

  /**
   * Este metodo se invoca cuando un usuario no autenticado intenta acceder a un
   * recurso protegido. En este caso, se devuelve una respuesta JSON con el
   * estado de error y el mensaje de error.
   *
   * @param request la solicitud HTTP
   * @param response la respuesta HTTP
   * @param authException la excepción de autenticación
   * @throws IOException si ocurre un error al escribir la respuesta
   */
  @Override
  public void commence(HttpServletRequest request,
                       HttpServletResponse response,
                       AuthenticationException authException) throws IOException {
    logger.error("Unauthorized error: {}", authException.getMessage());

    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

    final Map<String, Object> body = new HashMap<>();
    body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
    body.put("error", "Unauthorized");
    body.put("message", authException.getMessage());
    body.put("path", request.getServletPath());

    final ObjectMapper mapper = new ObjectMapper();
    mapper.writeValue(response.getOutputStream(), body);

  }
}
