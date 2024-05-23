package com.securityModel.Services;

import com.securityModel.models.Doctor;
import com.securityModel.models.RendezVous;

import java.util.List;


public interface RendezVousService {
    public void AddRendezVous(RendezVous rd);
    public List<RendezVous> GetAllRendezVous();
    RendezVous GetRendezVous(Long id);
    public void DeleteRendezVous(Long id);
    public List<RendezVous> GetConfirmedRendezVous();
}
