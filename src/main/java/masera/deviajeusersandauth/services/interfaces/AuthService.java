package masera.deviajeusersandauth.services.interfaces;

import masera.deviajeusersandauth.dtos.post.LoginRequest;
import masera.deviajeusersandauth.dtos.post.RefreshTokenRequest;
import masera.deviajeusersandauth.dtos.post.users.SignupRequest;
import masera.deviajeusersandauth.dtos.responses.JwtResponse;
import masera.deviajeusersandauth.dtos.responses.MessageResponse;
import masera.deviajeusersandauth.entities.UserEntity;

/**
 * Interfaz que define los métodos para la autenticación y gestión de usuarios.
 */
public interface AuthService {

  /**
   * Metodo para autenticar un usuario.
   *
   * @param loginRequest clase que contiene los datos de inicio de sesión (username y password).
   * @return un {@link JwtResponse} que contiene el token JWT y los datos del usuario.
   */
  JwtResponse authenticateUser(LoginRequest loginRequest);


  /**
   * Metodo para refrescar el token JWT.
   *
   * @param refreshTokenRequest clase que contiene el token de refresco.
   * @return un {@link JwtResponse} que contiene el nuevo token JWT.
   */
  JwtResponse refreshToken(RefreshTokenRequest refreshTokenRequest);

  /**
   * Metodo para cerrar sesión de un usuario.
   *
   * @param userId el ID del usuario que desea cerrar sesión.
   * @return un {@link MessageResponse} que indica el resultado de la operación.
   */
  MessageResponse logoutUser(Integer userId);

  /**
   * Metodo para obtener el usuario actual.
   *
   * @return un {@link UserEntity} que representa al usuario actual.
   */
  UserEntity getCurrentUser();
}
