package com.securityModel.Services;

import com.securityModel.models.MedicalFile;
import com.securityModel.models.RendezVous;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository

public interface MedicalFileService {
    public MedicalFile AddMedicalFile(MedicalFile mf);
    public List<MedicalFile> GetAllMedicalFiles();
    MedicalFile GetMedicalFile(Long id);
    public void DeleteMedicalFile(Long id);
}
