package masera.deviajeusersandauth.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Clase JwtAuthenticationFilter intercepta las solicitudes HTTP
 * y verifica la presencia y validez del token JWT en la cabecera Authorization.
 * OncePerRequestFilter asegura que el filtro se aplique una vez por solicitud.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  /**
   * Servicio de utilidades JWT que permiten generar, validar y extraer
   * información de los tokens JWT (en mi caso también refresh token).
   */
  private final JwtUtils jwtUtils;

  /**
   * Servicio que carga los detalles del usuario desde la base de datos
   * para la autenticación de Spring Security.
   */
  private final UserDetailsService userDetailsService;

  /**
   * Metodo que filtra las solicitudes HTTP y verifica la autenticación del usuario.
   *
   * @param request la solicitud HTTP
   * @param response la respuesta HTTP
   * @param filterChain la cadena de filtros
   * @throws ServletException excepción de servlet
   * @throws IOException excepción de entrada/salida
   */
  @Override
  protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain filterChain) throws ServletException, IOException {

    //Aca se obtiene el token de la cabecera Authorization
    final String authHeader = request.getHeader("Authorization");

    String username = null;
    String jwt = null;

    // Validar si existe el token y cumple el formato de token.
    if (authHeader != null && authHeader.startsWith("Bearer ")) {
      // Extraer el token JWT de la cabecera Authorization
      jwt = authHeader.substring(7);
      try {
        username = jwtUtils.extractUsername(jwt);
      } catch (Exception e) {
        logger.error("Error al extraer el nombre de usuario del token JWT", e);
      }
    }

    // Si encontramos un username y no hay autenticación en el contexto de Spring Security,
    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      //Cargar los detalles del usuario desde la base de datos
      UserDetails userDetails = userDetailsService.loadUserByUsername(username);

      // Si el token es válido, configura la autenticación de Spring Security.
      if (jwtUtils.validateToken(jwt, userDetails)) {

        // Crea un objeto de autenticación con los detalles del usuario
        // credenciales(contraseña) en null porque JWT ya valído el usuario
        // las authorities (roles/permisos) del usuario
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());

        //detalles adicionales sobre la solicitud
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        // Establece la autenticación en el contexto de seguridad de Spring.
        // SecurityContextHolder: es un contenedor de contexto de seguridad.
        // Esto marca al usuario como autenticado y permite verificar permisos @PreAuthorize.
        SecurityContextHolder.getContext().setAuthentication(authToken);
      }
    }

    // Pasa la solicitud y la respuesta al siguiente filtro en la cadena
    // o al controlador si no hay más filtros
    filterChain.doFilter(request, response);
  }
}
