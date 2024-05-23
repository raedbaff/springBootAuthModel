package com.securityModel.models;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import jakarta.persistence.*;

@Entity

public class MedicalFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JsonIncludeProperties({"id","username","photo","age"})
    private Patient patient;
    @ManyToOne
    @JsonIncludeProperties({"id","username","photo","age"})
    private Doctor doctor;
    private String MedicalHistory;
    private String Allergies;
    private String Diagnoses;
    private String DiagnosesResults;
    private String ProgressNotes;
    private String NextSteps;

    public MedicalFile(Long id, Patient patient, String medicalHistory, String allergies, String diagnoses, String diagnosesResults, String progressNotes, String nextSteps) {
        this.id = id;
        this.patient = patient;
        MedicalHistory = medicalHistory;
        Allergies = allergies;
        Diagnoses = diagnoses;
        DiagnosesResults = diagnosesResults;
        ProgressNotes = progressNotes;
        NextSteps = nextSteps;

    }

    public MedicalFile() {
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public String getMedicalHistory() {
        return MedicalHistory;
    }

    public void setMedicalHistory(String medicalHistory) {
        MedicalHistory = medicalHistory;
    }

    public String getAllergies() {
        return Allergies;
    }

    public void setAllergies(String allergies) {
        Allergies = allergies;
    }

    public String getDiagnoses() {
        return Diagnoses;
    }

    public void setDiagnoses(String diagnoses) {
        Diagnoses = diagnoses;
    }

    public String getDiagnosesResults() {
        return DiagnosesResults;
    }

    public void setDiagnosesResults(String diagnosesResults) {
        DiagnosesResults = diagnosesResults;
    }

    public String getProgressNotes() {
        return ProgressNotes;
    }

    public void setProgressNotes(String progressNotes) {
        ProgressNotes = progressNotes;
    }

    public String getNextSteps() {
        return NextSteps;
    }

    public void setNextSteps(String nextSteps) {
        NextSteps = nextSteps;
    }
}
