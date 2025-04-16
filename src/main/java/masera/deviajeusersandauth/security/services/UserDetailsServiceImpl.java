package masera.deviajeusersandauth.security.services;

import lombok.RequiredArgsConstructor;
import masera.deviajeusersandauth.entities.UserEntity;
import masera.deviajeusersandauth.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * * Clase que implementa la interfaz UserDetailsService para cargar
 * los detalles del usuario desde la base de datos y proporcionar
 * esta información a Spring Security para la autenticación y autorización.
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    UserEntity user = userRepository.findByUsernameWithRoles(username)
            .orElseThrow(() ->
                    new UsernameNotFoundException("User not found with username: " + username));
    return UserDetailsImpl.build(user);
  }
}
