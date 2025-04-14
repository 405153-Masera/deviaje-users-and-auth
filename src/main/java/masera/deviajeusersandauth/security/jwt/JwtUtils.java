package masera.deviajeusersandauth.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;


/**
 * Clase para el manejo de JWT(JSON Web Tokens).
 * Se utiliza para generar y validar tokens JWT.
 */
@Component
public class JwtUtils {

  @Value("${deviaje.app.jwtSecret}")
  private String secret;

  @Value("${deviaje.app.jwtExpirationMs}")
  private long expirationMs;

  @Value("${deviaje.app.jwtRefreshExpirationMs}")
  private long refreshExpirationMs;


  /**
   * Metodo que genera la clave de firma (signing key) a partir de la clave secreta.
   *
   * @return la clave de firma (signing key).
   */
  private Key getSignigKey() {
    byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  /**
   * Metodo que extrae el nombre de usuario del token JWT.
   *
   * @param token el token JWT.
   * @return el nombre de usuario.
   */
  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  /**
   * Metodo que extrae la fecha de expiración del token JWT.
   *
   * @param token el token JWT.
   * @return la fecha de expiración.
   */
  public Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  /**
   * Metodo para extraer una reclamación especifíca del token JWT.
   *
   * @param token el token JWT.
   * @param claimsResolver la función que extrae la reclamación.
   * @param <T> el tipo de la reclamación.
   * @return la reclamación extraída.
   */
  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  /**
   * Metodo que extrae todas las reclamaciones(claims) del token JWT.
   *
   * @param token el token JWT.
   * @return las reclamaciones extraídas.
   */
  private Claims extractAllClaims(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(getSignigKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
  }

  /**
   * Metodo para validar si el token ha expirado.
   *
   * @param token el token JWT.
   * @return true si el token ha expirado, false en caso contrario.
   */
  private Boolean isTokenExpired(String token) {
    final Date expiration = extractExpiration(token);
    return expiration.before(new Date());
  }


  /**
   * Metodo que genera un token de acceso para un usuario.
   *
   * @param userDetails detalles del usuario.
   * @return el token generado.
   */
  public String generateToken(UserDetails userDetails) {
    Map<String, Object> claims = new HashMap<>();
    return createToken(claims, userDetails.getUsername(),
            expirationMs);
  }

  /**
   * Metodo que genera un token de acceso para un usuario con reclamaciones(claims) adicionales.
   *
   * @param extraClaims reclamaciones adicionales.
   * @param userDetails detalles del usuario.
   * @return el token generado.
   */
  public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
    return createToken(extraClaims, userDetails.getUsername(),
            expirationMs);
  }

  /**
   * Metodo que genera un token de refresco para un usuario.
   *
   * @param userDetails detalles del usuario.
   * @return el token de refresco generado.
   */
  public String generateRefreshToken(UserDetails userDetails) {
    Map<String, Object> claims = new HashMap<>();
    return createToken(claims, userDetails.getUsername(),
            refreshExpirationMs);
  }

  /**
   * Metodo que crea el token JWT.
   *
   * @param claims claims del token.
   * @param subject el nombre de usuario.
   * @param expiration tiempo de expiración del token en milisegundos.
   * @return el token generado.
   */
  private String createToken(Map<String, Object> claims, String subject, long expiration) {
    return Jwts.builder()
            .setClaims(claims)
            .setSubject(subject)
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + expiration))
            .signWith(getSignigKey(), SignatureAlgorithm.HS256)
            .compact();
  }

  /**
   * Metodo para validar el token JWT.
   *
   * @param token el token JWT.
   * @param userDetails detalles del usuario.
   * @return true si el token es válido, false en caso contrario.
   */
  public Boolean validateToken(String token, UserDetails userDetails) {
    final String username = extractUsername(token);
    return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
  }

  /**
   * Metodo que valida el token JWT sin necesidad de los detalles del usuario
   * solo verifica firma y expiración.
   *
   * @param token el token JWT.
   * @return true si el token es válido, false en caso contrario.
   */
  public boolean validateToken(String token) {
    try {
      Jwts.parserBuilder().setSigningKey(getSignigKey()).build().parseClaimsJws(token);
      return true;
    } catch (JwtException | IllegalArgumentException e) {
      return false;
    }
  }
}
