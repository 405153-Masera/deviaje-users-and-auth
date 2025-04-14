package masera.deviajeusersandauth.security.services;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serial;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.Getter;
import masera.deviajeusersandauth.entities.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Clase que representa los detalles de un usuario.
 */
@Getter
public class UserDetailsImpl implements UserDetails {

  @Serial
  private static final long serialVersionUID = 1L;

  private final Integer id;
  private final String username;
  private final String email;
  @JsonIgnore
  private final String password;
  private final String firstName;
  private final String lastName;
  private final String phone;
  private final LocalDate birthDate;
  private final String dni;
  private final Boolean active;
  private final String avatarUrl;

  private final Collection<? extends GrantedAuthority> authorities;

  /**
   * Constructor de la clase.
   *
   * @param id  el ID del usuario.
   * @param username es el nombre de usuario.
   * @param email es el email del usuario.
   * @param password es la contraseña del usuario.
   * @param firstName es el nombre del usuario.
   * @param lastName es el apellido del usuario.
   * @param phone es el número de teléfono del usuario.
   * @param birthDate es la fecha de nacimiento del usuario.
   * @param dni es el DNI del usuario.
   * @param active indica si el usuario está activo.
   * @param avatarUrl es la URL del avatar del usuario.
   * @param authorities son los roles del usuario.
   */
  public UserDetailsImpl(Integer id, String username, String email,
                         String password, String firstName,
                         String lastName, String phone,
                         LocalDate birthDate, String dni, Boolean active,
                         String avatarUrl, Collection<? extends GrantedAuthority> authorities) {
    this.id = id;
    this.username = username;
    this.email = email;
    this.password = password;
    this.firstName = firstName;
    this.lastName = lastName;
    this.phone = phone;
    this.birthDate = birthDate;
    this.dni = dni;
    this.active = active;
    this.avatarUrl = avatarUrl;
    this.authorities = authorities;
  }

  /**
   * Metodo estático para construir un objeto UserDetailsImpl a partir de un UserEntity.
   *
   * @param user el objeto UserEntity.
   * @return un objeto UserDetailsImpl.
   */
  public static UserDetailsImpl build(UserEntity user) {
    List<GrantedAuthority> authorities = user.getUserRoles().stream()
            .map(userRole -> new SimpleGrantedAuthority(userRole.getRole().getDescription()))
            .collect(Collectors.toList());

    return new UserDetailsImpl(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getPassword(),
            user.getFirstName(),
            user.getLastName(),
            user.getPhone(),
            user.getBirthDate(),
            user.getDni(),
            user.getActive(),
            user.getAvatarUrl(),
            authorities);
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return active;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserDetailsImpl user = (UserDetailsImpl) o;
    return Objects.equals(id, user.id);
  }
}
