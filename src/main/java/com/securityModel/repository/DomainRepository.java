package com.securityModel.repository;

import com.securityModel.models.DomainMedical;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface DomainRepository extends JpaRepository<DomainMedical,Long> {


}
