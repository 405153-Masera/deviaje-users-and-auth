package masera.deviajeusersandauth.controllers;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import masera.deviajeusersandauth.dtos.get.UserDto;
import masera.deviajeusersandauth.dtos.post.users.UserCreateRequest;
import masera.deviajeusersandauth.dtos.put.UserPut;
import masera.deviajeusersandauth.dtos.responses.MessageResponse;
import masera.deviajeusersandauth.services.interfaces.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST para la gestión de usuarios.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  /**
   * Endpoint para obtener todos los usuarios.
   *
   * @return Lista de usuarios.
   */
  @GetMapping
  @PreAuthorize("hasAnyAuthority('SUPERADMIN', 'GERENTE')")
  public ResponseEntity<List<UserDto>> getAllUsers() {
    List<UserDto> users = userService.getAllUsers();
    return ResponseEntity.ok(users);
  }

  /**
   * Endpoint para obtener un usuario por su ID.
   *
   * @param role Rol del usuario a buscar.
   * @return Lista de usuarios con el rol especificado.
   */
  @GetMapping("/role/{role}")
  @PreAuthorize("hasAnyAuthority('SUPERADMIN', 'GERENTE')")
  public ResponseEntity<List<UserDto>> getUsersByRole(@PathVariable String role) {
    List<UserDto> users = userService.getUsersByRole(role);
    return ResponseEntity.ok(users);
  }

  /**
   * Endpoint para obtener un usuario por su ID.
   *
   * @param id ID del usuario a buscar.
   * @return Usuario encontrado.
   */
  @GetMapping("/{id}")
  @PreAuthorize("hasAnyAuthority('SUPERADMIN', 'GERENTE', 'AGENTE')"
          + " or #id == authentication.principal.id")
  public ResponseEntity<UserDto> getUserById(@PathVariable Integer id) {
    UserDto user = userService.getUserById(id);
    return ResponseEntity.ok(user);
  }

  /**
   * Endpoint para crear un nuevo usuario.
   *
   * @param request Datos del nuevo usuario a crear.
   * @return Usuario creado.
   */
  @PostMapping
  @PreAuthorize("hasAnyAuthority('SUPERADMIN', 'GERENTE')")
  public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserCreateRequest request) {
    UserDto createdUser = userService.createUser(request);
    return ResponseEntity.ok(createdUser);
  }

  /**
   * Endpoint para actualizar un usuario existente.
   *
   * @param id id del usuario a actualizar.
   * @param request Datos del usuario a actualizar.
   * @return Usuario actualizado.
   */
  @PutMapping("/{id}")
  @PreAuthorize("hasAnyAuthority('SUPERADMIN', 'GERENTE') "
          + "or #id == authentication.principal.id")
  public ResponseEntity<UserDto> updateUser(@PathVariable Integer id,
                                            @Valid @RequestBody UserPut request) {
    UserDto updatedUser = userService.updateUser(id, request);
    return ResponseEntity.ok(updatedUser);
  }

  /**
   * Endpoint para dar de baja un usuario.
   *
   * @param id id del usuario a dar de baja.
   * @return Mensaje de respuesta.
   */
  @DeleteMapping("/{id}")
  @PreAuthorize("hasAuthority('SUPERADMIN')")
  public ResponseEntity<MessageResponse> deleteUser(@PathVariable Integer id) {
    userService.deactivateUser(id);
    return ResponseEntity.ok(new MessageResponse("User deactivated successfully"));
  }

  /**
   * Endpoint para activar un usuario.
   *
   * @param id id del usuario a activar.
   * @return Mensaje de respuesta.
   */
  @PostMapping("/{id}/activate")
  @PreAuthorize("hasAuthority('SUPERADMIN')")
  public ResponseEntity<MessageResponse> activateUser(@PathVariable Integer id) {
    userService.activateUser(id);
    return ResponseEntity.ok(new MessageResponse("User activated successfully"));
  }
}
