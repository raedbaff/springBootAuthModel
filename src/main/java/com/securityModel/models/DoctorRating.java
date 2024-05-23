package com.securityModel.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity

public class DoctorRating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private boolean happy;
    private int Rating;
    @ManyToOne
    @JsonIgnoreProperties(value = {"cabinetDoctor","rendezvousList","doctorBills","ratings"})
    private Doctor doctor;
    @ManyToOne
    @JsonIgnoreProperties(value = {"myBills","patientRatings","myRendezVous"})
    private Patient patient;
    private String patientNotes;

    public DoctorRating() {
    }

    public DoctorRating(Long id, boolean happy, int rating, Doctor doctor, Patient patient, String patientNotes) {
        this.id = id;
        this.happy = happy;
        Rating = rating;
        this.doctor = doctor;
        this.patient = patient;
        this.patientNotes = patientNotes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isHappy() {
        return happy;
    }

    public void setHappy(boolean happy) {
        this.happy = happy;
    }

    public int getRating() {
        return Rating;
    }

    public void setRating(int rating) {
        Rating = rating;
        this.happy=rating >=3;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public String getPatientNotes() {
        return patientNotes;
    }

    public void setPatientNotes(String patientNotes) {
        this.patientNotes = patientNotes;
    }


}
