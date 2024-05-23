package com.securityModel.controllers;

import com.securityModel.Services.CabinetService;
import com.securityModel.models.Cabinet;
import com.securityModel.models.Doctor;
import com.securityModel.models.MedicalFile;
import com.securityModel.models.Secretary;
import com.securityModel.repository.CabinetRepository;
import com.securityModel.repository.DoctorRepository;
import com.securityModel.repository.SecretaryRepository;
import com.securityModel.utils.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/Cabinet")
public class CabinetController {
    @Autowired
    CabinetService cabinetService;
    @Autowired
    DoctorRepository doctorRepository;
    @Autowired
    CabinetRepository cabinetRepository;
    @Autowired
    StorageService storage;
    @Autowired
    SecretaryRepository secretaryRepository;

    @PostMapping("/save")
    public ResponseEntity<String> AddCabinet(@RequestParam("file") MultipartFile file,Authentication authentication, Principal principal, Cabinet cabinet) {
        String roleName = "";
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            roleName = authority.getAuthority();
        }

        if (roleName.equals("ROLE_DOCTOR")) {
            String doctorname=principal.getName();
            Doctor doctor=doctorRepository.findDoctorByUsername(doctorname);
            cabinet.setDoctor(doctor);
            String fileName = storage.store(file);
            cabinet.setPhoto(fileName);
            Long secretaireid=cabinet.getSecretary().getId();
            Secretary secretaire=secretaryRepository.findById(secretaireid).get();
            cabinet.setSecretary(secretaire);

            cabinetRepository.save(cabinet);
            return ResponseEntity.ok().body("success");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have permission to add the entity");
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> DeleteCabinet(Authentication authentication,@PathVariable Long id) {
        String roleName = "";
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            roleName = authority.getAuthority();
        }

        if (roleName.equals("ROLE_DOCTOR")) {
            cabinetService.DeleteCabinet(id);
            return ResponseEntity.ok("cabinet Deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have permission to Delete the entity");
        }
    }
    @GetMapping("/{id}")
    public Cabinet GetCabinet(@PathVariable Long id){
        return cabinetService.GetCabinet(id);
    }
    @PutMapping("/{id}")
    public Cabinet UpdateCabinet(@PathVariable Long id,@RequestBody Cabinet cabinet){
        Cabinet cab=cabinetService.GetCabinet(id);
        cab.setAdresseCabinet(cabinet.getAdresseCabinet());
        cab.setNomCabinet(cabinet.getNomCabinet());
        cab.setSecretary(cabinet.getSecretary());
        return cabinetService.AddCabinet(cab);
    }
    @PutMapping("/update/{id}")
    public Cabinet update(@PathVariable("id")Long id,@RequestParam("file") MultipartFile file,Principal principal,Cabinet cabinet){
        Cabinet cab=cabinetRepository.findById(id).get();
        String doc=principal.getName();
        Doctor doctor=doctorRepository.findDoctorByUsername(doc);
        cab.setDoctor(doctor);
        String fileName = storage.store(file);
        cab.setPhoto(fileName);
        Long secretaireid=cabinet.getSecretary().getId();
        Secretary secretaire=secretaryRepository.findById(secretaireid).get();
        cab.setSecretary(secretaire);
        cab.setNomCabinet(cabinet.getNomCabinet());
        cab.setAdresseCabinet(cabinet.getAdresseCabinet());
        cab.setDescription(cabinet.getDescription());
        return cabinetRepository.save(cab);

    }
    @GetMapping("/all")
    public List<Cabinet>getall(){
        return cabinetRepository.findAll();
    }
}
