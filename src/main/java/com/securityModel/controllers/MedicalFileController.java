package com.securityModel.controllers;

import com.securityModel.Services.MedicalFileService;
import com.securityModel.models.Doctor;
import com.securityModel.models.MedicalFile;
import com.securityModel.repository.DoctorRepository;
import com.securityModel.repository.MedicalFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/MedicalFile")

public class MedicalFileController {
    @Autowired
    MedicalFileRepository medicalFileRepository;
    @Autowired
    MedicalFileService medicalFileService;
    @Autowired
    DoctorRepository doctorRepository;
    @PostMapping("/save")
    public MedicalFile AddMedicalFile(Principal principal, MedicalFile medicalFile) {
        String doc=principal.getName();
        Doctor doctor=doctorRepository.findByUsername(doc).get();
        medicalFile.setDoctor(doctor);


        return medicalFileService.AddMedicalFile(medicalFile);
    }
    @GetMapping("/all")
    public List<MedicalFile> GetAllMedicalFiles(){
        return medicalFileService.GetAllMedicalFiles();
    }
    @GetMapping("/myfiles/{id}")
    public List<MedicalFile>getmyfiles(@PathVariable("id")Long id){
        return medicalFileRepository.findAllByPatientId(id);
    }

    @GetMapping("/{id}")
    public MedicalFile GetMedicalFile(@PathVariable Long id){
        return medicalFileRepository.findMedicalFileByPatientId(id);
    }

    @DeleteMapping("/{id}")
    public void DeleteMedicalFile(@PathVariable Long id){
        medicalFileService.DeleteMedicalFile(id);
    }
    @PutMapping("/{id}")
    public MedicalFile UpdateMedicalFile(@PathVariable Long id,@RequestBody MedicalFile medicalfile){
        MedicalFile medicalFile=medicalFileService.GetMedicalFile(id);
        medicalFile.setMedicalHistory(medicalfile.getMedicalHistory());
        medicalFile.setAllergies(medicalfile.getAllergies());
        medicalfile.setDiagnoses(medicalfile.getDiagnoses());
        medicalFile.setDiagnosesResults(medicalfile.getDiagnosesResults());
        medicalFile.setNextSteps(medicalfile.getNextSteps());
        medicalfile.setProgressNotes(medicalfile.getProgressNotes());
        return medicalFileService.AddMedicalFile(medicalFile);

    }
}
