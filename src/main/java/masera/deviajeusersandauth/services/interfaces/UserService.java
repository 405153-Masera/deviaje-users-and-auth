package masera.deviajeusersandauth.services.interfaces;

import masera.deviajeusersandauth.dtos.get.UserDto;
import masera.deviajeusersandauth.dtos.post.users.SignupRequest;
import masera.deviajeusersandauth.dtos.post.users.UserCreateRequest;
import masera.deviajeusersandauth.dtos.put.UserPut;
import masera.deviajeusersandauth.dtos.responses.MessageResponse;
import org.springframework.stereotype.Service;

import java.util.List;

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

  UserDto updateUser(Integer id, UserPut userCreateRequest);
  UserDto getUserById(Integer id);
  List<UserDto> getAllUsers();
  List<UserDto> getUsersByRole(String role);
  void activateUser(Integer id);
  void deactivateUser(Integer id);
}
