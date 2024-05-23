package com.securityModel.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.securityModel.Serializer.PatientSerializeObject;
import jakarta.persistence.*;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Entity

public class RendezVous {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JsonIncludeProperties({"id","username","photo"})
    private Patient patientName;
    private String appointmentDate;

    private String appointmentReason;
    private Boolean confirmed=false;
    @OneToOne(mappedBy = "rendezvous")
    @JsonIncludeProperties("id")
    private MedicalBill medicalBill;

    public MedicalBill getMedicalBill() {
        return medicalBill;
    }

    public void setMedicalBill(MedicalBill medicalBill) {
        this.medicalBill = medicalBill;
    }

    private LocalTime time;
    @ManyToOne
    @JsonIncludeProperties({"id","username","photo"})
    private Doctor doctor;
    private Boolean done=false;
    private String formattedTime;



    public RendezVous(Long id, Patient patientName, String appointmentDate, String appointmentReason, Boolean confirmed) {
        this.id = id;
        this.patientName = patientName;
        this.appointmentDate = appointmentDate;
        this.appointmentReason = appointmentReason;
        this.confirmed=confirmed;
    }

    public RendezVous() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Patient getPatientName() {
        return patientName;
    }

    public void setPatientName(Patient patientName) {
        this.patientName = patientName;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getAppointmentReason() {
        return appointmentReason;
    }

    public void setAppointmentReason(String appointmentReason) {
        this.appointmentReason = appointmentReason;
    }

    public Boolean getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(Boolean confirmed) {
        this.confirmed = confirmed;
    }
    public Boolean getDone() {
        return done;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm");
        this.formattedTime = time.format(formatter);
    }
    public String getFormattedTime() {
        return formattedTime;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }
}
