package com.securityModel.repository;

import com.securityModel.models.Doctor;
import com.securityModel.models.RendezVous;
import com.securityModel.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository

public interface DoctorRepository extends JpaRepository<Doctor,Long> {
    Optional<Doctor> findByUsername(String username);
    Doctor findDoctorByUsername(String username);
    User findByEmail(String email);
    User findByPasswordResetToken(String passwordResetToken);

    Boolean existsByUsername(String username);
    List<RendezVous> findRendezvousById(Long id);

    Boolean existsByEmail(String email);


}
