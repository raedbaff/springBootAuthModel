package com.securityModel.controllers;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import com.securityModel.Services.RendezVousService;
import com.securityModel.models.*;
import com.securityModel.payload.response.MessageResponse;
import com.securityModel.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/RendezVous")

public class RendezVousController {
    @Autowired
    RendezVousService rd;
    @Autowired
    RendezVousRepository rendezVousRepository;
    @Autowired
    PatientRepository patientRepository;
    @Autowired
    DoctorRepository doctorRepository;
    @Autowired
    NotificationsRepository notificationsRepository;
    @Autowired
    DoctorAvailabilityRepository doctorAvailabilityRepository;



    @PostMapping("/save")
    public ResponseEntity<?> saveRendezVous(Principal principal, RendezVous rv) {
        String username = principal.getName();
        Optional<Patient> patientOpt = patientRepository.findByUsername(username);
        if (patientOpt.isPresent()) {
            Patient patient = patientOpt.get();
            rv.setPatientName(patient);
            Long id = rv.getDoctor().getId();
            Doctor doctorname = doctorRepository.findById(id).get();
            List<Patient> patients = doctorname.getMyPatients();
            if (!patients.contains(patient)){
                patients.add(patient);
                doctorname.setMyPatients(patients);
                doctorRepository.save(doctorname);

            }

            List<Doctor> doctors = patient.getMydocs();
            if (!doctors.contains(doctorname)){
                doctors.add(doctorname);
                patient.setMydocs(doctors);
                patientRepository.save(patient);

            }

            rv.setPatientName(patient);
            rv.setDoctor(doctorname);
            rd.AddRendezVous(rv);
            DoctorAvailability av=new DoctorAvailability();
            av.setDoctor(doctorname);
            av.setAppointmentDate(rv.getAppointmentDate());
            av.setTime(rv.getFormattedTime());
            doctorAvailabilityRepository.save(av);

            return ResponseEntity.ok(new MessageResponse("Rendez-vous saved successfully!"));
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("Patient not found."));
        }
    }
    @GetMapping("/getAv")
    public List<DoctorAvailability> getAv(@RequestParam Long id){
        return doctorAvailabilityRepository.findAllByDoctorId(id);

    }


    @DeleteMapping("/delete/{id}")
    public void DeleteRendezVous(@PathVariable Long id){
        rd.DeleteRendezVous(id);

    }
    @GetMapping("/{id}")
    public RendezVous GetRendezVous(@PathVariable Long id){
        return rd.GetRendezVous(id);
    }
    @GetMapping("/all")

    public List<RendezVous> getAllRendezVous(){
        return rd.GetAllRendezVous();
    }
    @PostMapping("/confirm/{id}")
    public RendezVous ConfirmRendezVous(@PathVariable Long id){
        RendezVous rendezvous=rd.GetRendezVous(id);
        rendezvous.setConfirmed(true);
        rd.AddRendezVous(rendezvous);
        Notifications notifications=new Notifications();
        notifications.setUser(rendezvous.getPatientName());
        notifications.setContent("Your Rendezvous of " +rendezvous.getAppointmentDate()+ " has been confirmed");
        notifications.setTimestamp(LocalDateTime.now());
        notificationsRepository.save(notifications);
        Notifications nots=new Notifications();
        nots.setUser(rendezvous.getDoctor());
        nots.setTimestamp(LocalDateTime.now());
        nots.setContent("you have a new rendezvous request");
        notificationsRepository.save(nots);

        return rendezvous;

    }
    @GetMapping("/allConfirmed")

    public List<RendezVous> GetConfirmedRendezVous(){
        return rd.GetConfirmedRendezVous();
    }
    @GetMapping("/userRendezVous")
    public ResponseEntity<List<RendezVous>> getAllRendezVousByPatientUsername(Principal principal) {
        String username = principal.getName();
        List<RendezVous> rendezVousList = rendezVousRepository.getAllRendezVousByPatientNameUsername(username);
        return ResponseEntity.ok(rendezVousList);
    }
    @GetMapping("/doctorRendezvous")
    public ResponseEntity<List<RendezVous>>getallconfirmedDoctorRendezvous(Principal principal){
        String username=principal.getName();
        List<RendezVous>list=rendezVousRepository.findAllByDoctorUsernameAndConfirmedTrue(username);
        return ResponseEntity.ok(list);
    }
    @PutMapping("/{id}")
    public RendezVous finishRendezvous(@PathVariable("id")Long id){
        RendezVous rd=rendezVousRepository.findById(id).get();
        rd.setDone(true);
        rendezVousRepository.save(rd);
        return rd;
    }


}
