package masera.deviajeusersandauth.services.impl;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import masera.deviajeusersandauth.dtos.post.LoginRequest;
import masera.deviajeusersandauth.dtos.post.RefreshTokenRequest;
import masera.deviajeusersandauth.dtos.responses.JwtResponse;
import masera.deviajeusersandauth.dtos.responses.MessageResponse;
import masera.deviajeusersandauth.entities.RefreshTokenEntity;
import masera.deviajeusersandauth.entities.UserEntity;
import masera.deviajeusersandauth.exceptions.TokenRefreshException;
import masera.deviajeusersandauth.repositories.UserRepository;
import masera.deviajeusersandauth.security.jwt.JwtUtils;
import masera.deviajeusersandauth.security.services.UserDetailsImpl;
import masera.deviajeusersandauth.services.interfaces.AuthService;
import masera.deviajeusersandauth.services.interfaces.RefreshTokenService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final AuthenticationManager authenticationManager;

  private final UserRepository userRepository;

  private final PasswordEncoder passwordEncoder;

  private final JwtUtils jwtUtils;

  private final RefreshTokenService refreshTokenService;

  @Override
  public JwtResponse authenticateUser(LoginRequest loginRequest) {
    Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);

    // Usar el nuevo método del JwtUtils actualizado
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    String jwt = jwtUtils.generateToken(userDetails);

    List<String> roles = userDetails.getAuthorities().stream()
            .map(item -> item.getAuthority())
            .collect(Collectors.toList());

    RefreshTokenEntity refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

    return JwtResponse.builder()
            .token(jwt)
            .refreshToken(refreshToken.getToken())
            .id(userDetails.getId())
            .username(userDetails.getUsername())
            .email(userDetails.getEmail())
            .roles(roles)
            .build();
  }


  @Override
  public JwtResponse refreshToken(RefreshTokenRequest request) {
    String requestRefreshToken = request.getRefreshToken();

    return refreshTokenService.findByToken(requestRefreshToken)
            .map(refreshTokenService::verifyExpiration)
            .map(RefreshTokenEntity::getUser)
            .map(user -> {
              // Usar el nuevo método del JwtUtils actualizado para generar token
              UserDetailsImpl userDetails = UserDetailsImpl.build(user);
              String token = jwtUtils.generateToken(userDetails);

              List<String> roles = user.getUserRoles().stream()
                      .map(ur -> ur.getRole().getDescription())
                      .collect(Collectors.toList());

              return JwtResponse.builder()
                      .token(token)
                      .refreshToken(requestRefreshToken)
                      .id(user.getId())
                      .username(user.getUsername())
                      .email(user.getEmail())
                      .roles(roles)
                      .build();
            })
            .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                    "Refresh token is not in database!"));
  }

  @Override
  public MessageResponse logoutUser(Integer userId) {
    refreshTokenService.deleteByUserId(userId);
    return new MessageResponse("Log out successful!");
  }

  @Override
  public UserEntity getCurrentUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()) {
      return null;
    }

    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    return userRepository.findById(userDetails.getId())
            .orElse(null);
  }
}
