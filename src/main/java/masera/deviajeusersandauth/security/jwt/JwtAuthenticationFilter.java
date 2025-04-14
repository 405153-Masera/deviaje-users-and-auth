package masera.deviajeusersandauth.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Clase JwtAuthenticationFilter para filtrar las solicitudes HTTP
 * y valída si el token JWT es válido.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtUtils jwtUtils;

  private final UserDetailsService userDetailsService;

  /**
   * Constructor de la clase JwtAuthenticationFilter.
   *
   * @param jwtUtils clase JwtUtil para generar y validar tokens JWT.
   * @param userDetailsService servicio de detalles de usuario.
   */
  public JwtAuthenticationFilter(JwtUtils jwtUtils, UserDetailsService userDetailsService) {
    this.jwtUtils = jwtUtils;
    this.userDetailsService = userDetailsService;
  }

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

    // Si encontramos un username y no hay autenticación en el contexto.
    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      UserDetails userDetails = userDetailsService.loadUserByUsername(username);

      // Si el token es válido, configura la autenticación de Spring Security.
      if (jwtUtils.validateToken(jwt, userDetails)) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
            userDetails, null, userDetails.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
      }
    }

    filterChain.doFilter(request, response);
  }
}
