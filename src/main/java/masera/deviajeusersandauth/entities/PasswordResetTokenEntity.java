package masera.deviajeusersandauth.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "password_reset_tokens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PasswordResetTokenEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(nullable = false, unique = true)
  private String token;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private UserEntity user;

  @Column(name = "expiry_date", nullable = false)
  private LocalDateTime expiryDate;

  @Column(name = "created_datetime")
  private LocalDateTime createdDatetime;

  @Column(name = "used")
  private Boolean used;

  @PrePersist
  protected void onCreate() {
    createdDatetime = LocalDateTime.now();
    used = false;

    if (token == null) {
      token = UUID.randomUUID().toString();
    }

    if (expiryDate == null) {
      // Por defecto, el token expira en 24 horas
      expiryDate = LocalDateTime.now().plusHours(24);
    }
  }

  public boolean isExpired() {
    return LocalDateTime.now().isAfter(expiryDate);
  }
}
