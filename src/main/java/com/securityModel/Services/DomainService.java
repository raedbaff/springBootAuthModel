package com.securityModel.Services;

import com.securityModel.models.Doctor;
import com.securityModel.models.DomainMedical;

import java.util.List;

public interface DomainService {
    public void AddDomain(DomainMedical dom);
    public List<DomainMedical> GetAllDomains();
    DomainMedical GetDomain(Long id);
    public void DeleteDomain(Long id);
    public List<Doctor> getDoctorByDomainMedical(DomainMedical dom);
}
