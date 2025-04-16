package masera.deviajeusersandauth.configs;

import lombok.RequiredArgsConstructor;
import masera.deviajeusersandauth.security.jwt.AuthEntryPointJwt;
import masera.deviajeusersandauth.security.jwt.JwtAuthenticationFilter;
import masera.deviajeusersandauth.security.jwt.JwtUtils;
import masera.deviajeusersandauth.security.services.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Esta clase establece las reglas de seguridad para toda la aplicación, incluyendo qué endpoints
 * son públicos, cómo se validan los tokens JWT, y cómo se codifican las contraseñas.
 */
@Configuration
@EnableWebSecurity
//para habilitar la seguridad de métodos @PreAuthorize y @PostAuthorize
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final UserDetailsServiceImpl userDetailsService;
  private final AuthEntryPointJwt unauthorizedHandler;
  private final JwtUtils jwtUtils;

  /**
   * Crea una instancia de JwtAuthenticationFilter, el filtro personalizado
   * que valida los tokens JWT en cada solicitud.
   *
   * @return JwtAuthenticationFilter
   */
  @Bean
  public JwtAuthenticationFilter authenticationJwtTokenFilter() {
    return new JwtAuthenticationFilter(jwtUtils, userDetailsService);
  }

  /**
   * Configura el proveedor de autenticación para la aplicación.
   * Utiliza DaoAuthenticationProvider para autenticar usuarios
   * a través de un UserDetailsService y un PasswordEncoder.
   *
   * @return DaoAuthenticationProvider
   */
  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

    authProvider.setUserDetailsService(userDetailsService);
    authProvider.setPasswordEncoder(passwordEncoder());

    return authProvider;
  }

  /**
   * Configura el AuthenticationManager para la aplicación.
   * Este bean es necesario para la autenticación de usuarios. Gestiona las autenticaciones
   *
   * @param authConfig Configuración de autenticación
   * @return AuthenticationManager
   * @throws Exception si ocurre un error al configurar el AuthenticationManager
   */
  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig)
          throws Exception {
    return authConfig.getAuthenticationManager();
  }

  /**
   * Crea un PasswordEncoder que utiliza BCrypt para codificar contraseñas
   * y compararlas de forma segura.
   *
   * @return PasswordEncoder
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  /**
   * Configura la cadena de filtros de seguridad para la aplicación.
   * Define cómo se manejan las solicitudes HTTP, la gestión de sesiones,
   * y los puntos de entrada no autorizados.
   *
   * @param http Configuración de seguridad HTTP
   * @return SecurityFilterChain
   * @throws Exception si ocurre un error al configurar la cadena de filtros
   */
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
            .csrf(AbstractHttpConfigurer::disable) // Deshabilita la protección CSRF
            .cors(cors -> cors.configure(http)) // habilita CORS
            .exceptionHandling(exception -> exception
                    .authenticationEntryPoint(unauthorizedHandler))
            .sessionManagement(session -> session
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //sin estado
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/api/auth/**").permitAll()
                    .requestMatchers("/api/public/**").permitAll()
                    .requestMatchers("/swagger-ui.html").permitAll()
                    .requestMatchers("/swagger-ui/**").permitAll()
                    .requestMatchers("/v3/api-docs/**").permitAll()
                    .requestMatchers("/v3/api-docs").permitAll()
                    .requestMatchers("/swagger-resources/**").permitAll()
                    .anyRequest().authenticated());

    http.authenticationProvider(authenticationProvider());
    http.addFilterBefore(authenticationJwtTokenFilter(),
            UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}