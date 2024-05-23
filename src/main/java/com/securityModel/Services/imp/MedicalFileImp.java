package com.securityModel.Services.imp;

import com.securityModel.Services.MedicalFileService;
import com.securityModel.models.MedicalFile;
import com.securityModel.repository.MedicalFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service

public class MedicalFileImp implements MedicalFileService {
    @Autowired
    MedicalFileRepository medicalFileRepository;
    @Override
    public MedicalFile AddMedicalFile(MedicalFile mf) {
        return medicalFileRepository.save(mf);
    }

    @Override
    public List<MedicalFile> GetAllMedicalFiles() {
        return medicalFileRepository.findAll();
    }

    @Override
    public MedicalFile GetMedicalFile(Long id) {
        return medicalFileRepository.findById(id).get();
    }

    @Override
    public void DeleteMedicalFile(Long id) {
        medicalFileRepository.deleteById(id);

    }
}
