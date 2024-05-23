package com.securityModel.Services.imp;

import com.securityModel.Services.RendezVousService;
import com.securityModel.models.RendezVous;
import com.securityModel.repository.RendezVousRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service


public class RendezVousImp implements RendezVousService {
    @Autowired
    RendezVousRepository rp;
    @Override
    public void AddRendezVous(RendezVous rd) {
        rp.save(rd);
    }

    @Override
    public List<RendezVous> GetAllRendezVous() {
        return rp.findAll();
    }

    @Override
    public RendezVous GetRendezVous(Long id) {
        return rp.findById(id).get();
    }

    @Override
    public void DeleteRendezVous(Long id) {
        rp.deleteById(id);

    }

    @Override
    public List<RendezVous> GetConfirmedRendezVous() {
        return rp.findByConfirmedTrue();
    }
}
