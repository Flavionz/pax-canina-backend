package com.flavio.paxcanina.dao;

import com.flavio.paxcanina.model.TokenPurpose;
import com.flavio.paxcanina.model.ValidationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ValidationTokenDao extends JpaRepository<ValidationToken, Long> {

    Optional<ValidationToken> findByToken(@NonNull String token);

    Optional<ValidationToken> findByTokenAndPurpose(@NonNull String token, @NonNull TokenPurpose purpose);

    Optional<ValidationToken> findByTokenAndPurposeAndConsumedAtIsNull(@NonNull String token,
                                                                       @NonNull TokenPurpose purpose);

    void deleteByToken(@NonNull String token);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
           update ValidationToken v
              set v.consumedAt = :now
            where v.id = :id and v.consumedAt is null
           """)
    int markConsumedById(long id, @NonNull LocalDateTime now);

    // Plus RGPD: consume all outstanding tokens for a user (use in anonymize)
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
           update ValidationToken v
              set v.consumedAt = :now
            where v.user.idUser = :userId and v.consumedAt is null
           """)
    int consumeAllForUser(int userId, @NonNull LocalDateTime now);

    // Optional hygiene: cleanup
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
           delete from ValidationToken v
            where v.expiryDate < :now or v.consumedAt is not null
           """)
    int cleanupExpiredOrConsumed(@NonNull LocalDateTime now);
}
