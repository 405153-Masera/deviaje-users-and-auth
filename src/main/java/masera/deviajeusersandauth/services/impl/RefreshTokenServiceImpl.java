package masera.deviajeusersandauth.services.impl;


import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import masera.deviajeusersandauth.entities.RefreshTokenEntity;
import masera.deviajeusersandauth.entities.UserEntity;
import masera.deviajeusersandauth.exceptions.ResourceNotFoundException;
import masera.deviajeusersandauth.exceptions.TokenRefreshException;
import masera.deviajeusersandauth.repositories.RefreshTokenRepository;
import masera.deviajeusersandauth.repositories.UserRepository;
import masera.deviajeusersandauth.services.interfaces.RefreshTokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

  @Value("${app.jwt.expiration.minutes}")
  private Long refreshTokenDurationMs;

  private final RefreshTokenRepository refreshTokenRepository;

  private final UserRepository userRepository;

  @Override
  public Optional<RefreshTokenEntity> findByToken(String token) {
    return refreshTokenRepository.findByToken(token);
  }

  @Override
  @Transactional
  public RefreshTokenEntity createRefreshToken(Integer userId) {
    RefreshTokenEntity refreshToken = new RefreshTokenEntity();

    UserEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

    // Delete any existing refresh tokens for this user
    refreshTokenRepository.deleteByUser(user);

    refreshToken.setUser(user);
    refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
    refreshToken.setToken(UUID.randomUUID().toString());

    refreshToken = refreshTokenRepository.save(refreshToken);
    return refreshToken;
  }

  @Override
  public RefreshTokenEntity verifyExpiration(RefreshTokenEntity token) {
    if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
      refreshTokenRepository.delete(token);
      throw new TokenRefreshException(token.getToken(),
              "Refresh token was expired. Please make a new sign in request");
    }

    return token;
  }

  @Override
  @Transactional
  public int deleteByUserId(Integer userId) {
    UserEntity userEntity = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

    return refreshTokenRepository.deleteByUser(userEntity);
  }
}
