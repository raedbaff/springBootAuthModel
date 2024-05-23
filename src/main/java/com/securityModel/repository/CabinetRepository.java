package com.securityModel.repository;

import com.securityModel.models.Cabinet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface CabinetRepository extends JpaRepository<Cabinet,Long> {
    public Cabinet findByDoctorUsername(String username);
}
