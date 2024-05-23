package com.securityModel.repository;

import com.securityModel.models.Admin;
import com.securityModel.models.Doctor;
import com.securityModel.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository

public interface AdminRepository extends JpaRepository<Admin,Long> {
    Optional<Admin> findByUsername(String username);
    User findByEmail(String email);
    User findByPasswordResetToken(String passwordResetToken);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}
