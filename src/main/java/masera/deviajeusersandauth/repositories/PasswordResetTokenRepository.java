package masera.deviajeusersandauth.repositories;

import masera.deviajeusersandauth.entities.PasswordResetTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetTokenEntity, Integer> {
  Optional<PasswordResetTokenEntity> findByToken(String token);
  void deleteByUserId(Integer userId);
}