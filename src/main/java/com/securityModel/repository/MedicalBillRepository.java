package com.securityModel.repository;

import com.securityModel.models.MedicalBill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface MedicalBillRepository extends JpaRepository<MedicalBill,Long> {
    public List<MedicalBill>findMedicalBillsByPatientUsername(String Username);
    public List<MedicalBill>findMedicalBillsByPatientId(Long id);

}
