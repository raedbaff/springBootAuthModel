package com.securityModel.repository;

import com.securityModel.models.Secretary;
import com.securityModel.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository

public interface SecretaryRepository extends JpaRepository<Secretary,Long> {
    Optional<Secretary> findByUsername(String username);
    User findByEmail(String email);
    User findByPasswordResetToken(String passwordResetToken);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}
