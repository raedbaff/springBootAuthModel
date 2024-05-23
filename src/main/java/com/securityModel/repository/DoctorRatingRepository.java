package com.securityModel.repository;

import com.securityModel.models.DoctorRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository

public interface DoctorRatingRepository extends JpaRepository<DoctorRating,Long> {
    public List<DoctorRating>findDoctorRatingsByDoctorId(Long id);
}
