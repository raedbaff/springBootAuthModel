package com.securityModel.repository;

import com.securityModel.models.DoctorAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface DoctorAvailabilityRepository extends JpaRepository<DoctorAvailability,Long> {
    public List<DoctorAvailability>findAllByDoctorId(long id);
}
