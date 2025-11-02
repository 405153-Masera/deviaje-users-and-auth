package masera.deviajeusersandauth.security.services;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serial;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import masera.deviajeusersandauth.entities.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Clase que representa los detalles de un usuario.
 */
@Getter
@RequiredArgsConstructor
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
  private final Boolean active;
  private final String avatarUrl;

  /**
   * Lista de roles del usuario.
   */
  private final Collection<? extends GrantedAuthority> authorities;

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
            user.getActive(),
            user.getAvatarUrl(),
            authorities);
  }

  @Override
  @NonNull
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  @NonNull
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
