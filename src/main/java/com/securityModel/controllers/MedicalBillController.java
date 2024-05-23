package com.securityModel.controllers;

import com.securityModel.models.*;
import com.securityModel.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/Bill")

public class MedicalBillController {
    @Autowired
    DoctorRepository doctorRepository;
    @Autowired
    PatientRepository patientRepository;
     @Autowired
    MedicalBillRepository medicalBillRepository;
     @Autowired
    NotificationsRepository notificationsRepository;
     @Autowired
    RendezVousRepository rendezVousRepository;
    @PostMapping("/save")
    public MedicalBill CreateMedicalBill(MedicalBill medicalbill){
        Long id=medicalbill.getRendezvous().getId();
        RendezVous rd=rendezVousRepository.findById(id).get();
        medicalbill.setRendezvous(rd);
        Patient pat=rd.getPatientName();
        medicalbill.setPatient(pat);
        Doctor doc=rd.getDoctor();
        medicalbill.setDoc(doc);
        medicalbill.setDate(rd.getAppointmentDate());

        Notifications not=new Notifications();
        not.setUser(pat);
        not.setTimestamp(LocalDateTime.now());
        not.setContent("a new bill has been created");
        notificationsRepository.save(not);
        Notifications not2=new Notifications();
        not2.setUser(doc);
        not2.setTimestamp(LocalDateTime.now());
        not2.setContent("you have a new Bill");
        notificationsRepository.save(not2);


        return medicalBillRepository.save(medicalbill);

    }
    @GetMapping("/all")
    public List<MedicalBill>getallbills(){
        return medicalBillRepository.findAll();
    }
    @GetMapping("/mybills")
    public List<MedicalBill>getmybills(Principal principal){
        String username = principal.getName();
        List<MedicalBill> medicalbills = medicalBillRepository.findMedicalBillsByPatientUsername(username);
        return medicalbills;
    }
    @GetMapping("/{id}")
    public List<MedicalBill>getmybillsbyid(@PathVariable("id") Long id){
        return medicalBillRepository.findMedicalBillsByPatientId(id);
    }

}
