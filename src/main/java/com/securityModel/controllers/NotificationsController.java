package com.securityModel.controllers;

import com.securityModel.models.Notifications;
import com.securityModel.models.Patient;
import com.securityModel.repository.NotificationsRepository;
import com.securityModel.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.management.Notification;
import java.security.Principal;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/Notifications")

public class NotificationsController {
    @Autowired
    PatientRepository patientRepository;
    @Autowired
    NotificationsRepository notificationsRepository;
    @GetMapping("/")
    public List<Notifications>getallusernotifications(Principal principal){
        String username=principal.getName();
        return notificationsRepository.findNotificationsByUserUsername(username);
    }
    @DeleteMapping("/{id}")
    public void deleteNot(@PathVariable("id") Long id){

        notificationsRepository.deleteById(id);
    }
}
