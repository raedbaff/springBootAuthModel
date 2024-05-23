package com.securityModel.repository;

import com.securityModel.models.Patient;
import com.securityModel.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository

public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByUsername(String username);
    User findByEmail(String email);
    User findByPasswordResetToken(String passwordResetToken);


    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}
