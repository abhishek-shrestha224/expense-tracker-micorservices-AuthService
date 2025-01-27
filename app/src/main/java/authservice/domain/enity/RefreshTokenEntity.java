package authservice.domain.enity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity(name = "refresh_tokens")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefreshTokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refresh_token_id", nullable = false, updatable = false)
    private Long id;

    @Column(nullable = false)
    private String token;

    private Instant expiresAt;

    @OneToOne
    @JoinColumn(name = "id", referencedColumnName = "user_id")
    private UserEntity user;


}
