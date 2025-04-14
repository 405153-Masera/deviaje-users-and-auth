package masera.deviajeusersandauth.services.interfaces;

import masera.deviajeusersandauth.entities.RefreshTokenEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface RefreshTokenService {

  Optional<RefreshTokenEntity> findByToken(String token);
  RefreshTokenEntity createRefreshToken(Integer userId);
  RefreshTokenEntity verifyExpiration(RefreshTokenEntity token);
  int deleteByUserId(Integer userId);
}
