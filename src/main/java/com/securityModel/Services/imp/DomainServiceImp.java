package com.securityModel.Services.imp;

import com.securityModel.Services.DomainService;
import com.securityModel.models.Doctor;
import com.securityModel.models.DomainMedical;
import com.securityModel.repository.DomainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service

public class DomainServiceImp implements DomainService {
    @Autowired
    DomainRepository dr;


    @Override
    public void AddDomain(DomainMedical dom) {
        dr.save(dom);

    }

    @Override
    public List<DomainMedical> GetAllDomains() {
        return dr.findAll();
    }

    @Override
    public DomainMedical GetDomain(Long id) {
        return dr.findById(id).get();
    }

    @Override
    public void DeleteDomain(Long id) {
        dr.deleteById(id);

    }

    @Override
    public List<Doctor> getDoctorByDomainMedical(DomainMedical dom) {
        return null;
    }
}
