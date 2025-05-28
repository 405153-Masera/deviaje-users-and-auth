package masera.deviajeusersandauth.services.interfaces;

import java.util.List;
import masera.deviajeusersandauth.dtos.get.UserDto;
import masera.deviajeusersandauth.dtos.post.users.SignupRequest;
import masera.deviajeusersandauth.dtos.post.users.UserCreateRequest;
import masera.deviajeusersandauth.dtos.put.UserPut;
import masera.deviajeusersandauth.dtos.responses.MessageResponse;
import org.springframework.stereotype.Service;

/**
 * Interfaz del servicio de usuarios que define las operaciones
 * disponibles para la gestión de usuarios en la aplicación.
 */
@Service
public interface UserService {

  /**
   * Crea un nuevo usuario desde el rol SuperAdmin y Gerente.
   *
   * @param userCreateRequest datos del usuario a crear.
   * @return el usuario creado.
   */
  UserDto createUser(UserCreateRequest userCreateRequest);

  /**
   * Metodo para registrar un nuevo usuario.
   *
   * @param signupRequest clase que contiene los datos del
   *     nuevo usuario (username, email, password, etc.).
   * @return un {@link MessageResponse} que indica el resultado de la operación.
   */
  MessageResponse registerUser(SignupRequest signupRequest);

  /**
   * Actualiza un usuario existente.
   *
   * @param id el ID del usuario a actualizar.
   * @param userCreateRequest los nuevos datos del usuario.
   * @return el usuario actualizado.
   */
  UserDto updateUser(Integer id, UserPut userCreateRequest);

  /**
   * Obtiene un usuario por su ID.
   *
   * @param id id del usuario.
   * @return el usuario encontrado.
   */
  UserDto getUserById(Integer id);

  /**
   * Obtiene todos los usuarios registrados en la aplicación.
   *
   * @return la lista de usuarios.
   */
  List<UserDto> getAllUsers();

  /**
   * Obtiene una lista de usuarios por su rol.
   *
   * @param role el rol de los usuarios a buscar.
   * @return la lista de usuarios con el rol especificado.
   */
  List<UserDto> getUsersByRole(String role);

  /**
   * Activa un usuario por su ID.
   *
   * @param id del usuario a activar.
   */
  void activateUser(Integer id);

  /**
   * Desactiva un usuario por su ID.
   *
   * @param id del usuario a desactivar.
   */
  void deactivateUser(Integer id);
}
