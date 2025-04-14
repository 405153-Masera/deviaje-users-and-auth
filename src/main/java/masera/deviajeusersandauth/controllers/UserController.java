package masera.deviajeusersandauth.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import masera.deviajeusersandauth.dtos.get.UserDto;
import masera.deviajeusersandauth.dtos.post.users.UserCreateRequest;
import masera.deviajeusersandauth.dtos.put.UserPut;
import masera.deviajeusersandauth.dtos.responses.MessageResponse;
import masera.deviajeusersandauth.services.interfaces.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @GetMapping
  @PreAuthorize("hasAnyAuthority('SUPERADMIN', 'GERENTE')")
  public ResponseEntity<List<UserDto>> getAllUsers() {
    List<UserDto> users = userService.getAllUsers();
    return ResponseEntity.ok(users);
  }

  @GetMapping("/role/{role}")
  @PreAuthorize("hasAnyAuthority('SUPERADMIN', 'GERENTE')")
  public ResponseEntity<List<UserDto>> getUsersByRole(@PathVariable String role) {
    List<UserDto> users = userService.getUsersByRole(role);
    return ResponseEntity.ok(users);
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasAnyAuthority('SUPERADMIN', 'GERENTE', 'AGENTE') or #id == authentication.principal.id")
  public ResponseEntity<UserDto> getUserById(@PathVariable Integer id) {
    UserDto user = userService.getUserById(id);
    return ResponseEntity.ok(user);
  }

  @PostMapping
  @PreAuthorize("hasAnyAuthority('SUPERADMIN', 'GERENTE')")
  public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserCreateRequest request) {
    UserDto createdUser = userService.createUser(request);
    return ResponseEntity.ok(createdUser);
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasAnyAuthority('SUPERADMIN', 'GERENTE') or #id == authentication.principal.id")
  public ResponseEntity<UserDto> updateUser(@PathVariable Integer id, @Valid @RequestBody UserPut request) {
    UserDto updatedUser = userService.updateUser(id, request);
    return ResponseEntity.ok(updatedUser);
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasAuthority('SUPERADMIN')")
  public ResponseEntity<MessageResponse> deleteUser(@PathVariable Integer id) {
    userService.deactivateUser(id);
    return ResponseEntity.ok(new MessageResponse("User deactivated successfully"));
  }

  @PostMapping("/{id}/activate")
  @PreAuthorize("hasAuthority('SUPERADMIN')")
  public ResponseEntity<MessageResponse> activateUser(@PathVariable Integer id) {
    userService.activateUser(id);
    return ResponseEntity.ok(new MessageResponse("User activated successfully"));
  }

  @PostMapping("/{id}/deactivate")
  @PreAuthorize("hasAuthority('SUPERADMIN')")
  public ResponseEntity<MessageResponse> deactivateUser(@PathVariable Integer id) {
    userService.deactivateUser(id);
    return ResponseEntity.ok(new MessageResponse("User deactivated successfully"));
  }
}
