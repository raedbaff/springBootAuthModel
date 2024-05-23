package com.securityModel.repository;

import com.securityModel.models.RendezVous;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface RendezVousRepository extends JpaRepository<RendezVous,Long> {
    List<RendezVous> findByConfirmedTrue();
    List<RendezVous> getAllRendezVousByPatientNameUsername(String username);
    List<RendezVous>findAllByDoctorUsernameAndConfirmedTrue(String username);
    List<RendezVous> findByDoctorId(long id);
    List<RendezVous>findAllByDoctorId(Long id);

}
