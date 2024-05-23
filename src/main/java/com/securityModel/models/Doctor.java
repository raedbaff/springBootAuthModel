package com.securityModel.models;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(	name = "doctors",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")
        })

public class Doctor extends User {
    @ManyToOne
    @JsonIgnoreProperties("list")
    private DomainMedical domainMedical;
    private String Adresse;
    private String photo;
    private Integer age=0;
    private Integer patients=0;
    private Integer points=0;
    private String Description;
    @OneToMany(mappedBy = "doctor",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<MedicalFile>myMedicalFiles;
   @ManyToMany(mappedBy = "mydocs")
   @JsonIncludeProperties({"id","username","photo","email","gender"})
    private List<Patient>myPatients;

    public List<Patient> getMyPatients() {
        return myPatients;
    }

    public void setMyPatients(List<Patient> myPatients) {
        this.myPatients = myPatients;
    }

    public String getDescription() {
        return Description;
    }



    @OneToOne(mappedBy = "doctor")
    @JsonIncludeProperties({"nomCabinet","secretary"})
    private Cabinet cabinetDoctor;
    @OneToMany(mappedBy = "doctor")
    @JsonIgnore
    private List<RendezVous> rendezvousList;
    @OneToMany(mappedBy = "doc")
    @JsonIgnoreProperties("doc")
    private List<MedicalBill>doctorBills;
    @OneToMany(mappedBy = "doctor")
    private List<DoctorRating> ratings;
    @OneToMany(mappedBy = "doctor")
    private List<DoctorAvailability> docAv;
    public void setDescription(String description) {
        Description = description;
    }
    public List<DoctorRating> getRatings() {
        return ratings;
    }

    public void setRatings(List<DoctorRating> ratings) {
        this.ratings = ratings;
    }

    public List<MedicalBill> getDoctorBills() {
        return doctorBills;
    }

    public void setDoctorBills(List<MedicalBill> doctorBills) {
        this.doctorBills = doctorBills;
    }

    public Cabinet getCabinet() {
        return cabinetDoctor;
    }

    public void setCabinet(Cabinet cabinet) {
        this.cabinetDoctor = cabinet;
    }

    public Doctor(String username, String email, String password, DomainMedical domainMedical, String adresse, String photo) {
        super(username, email, password);
        this.domainMedical = domainMedical;
        Adresse = adresse;
        this.photo = photo;
    }

    public Doctor() {

    }


    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getPatients() {
        return patients;
    }

    public void setPatients(Integer patients) {
        this.patients = patients;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public DomainMedical getDomainMedical() {
        return domainMedical;
    }

    public void setDomainMedical(DomainMedical domainMedical) {
        this.domainMedical = domainMedical;
    }

    public String getAdresse() {
        return Adresse;
    }

    public void setAdresse(String adresse) {
        Adresse = adresse;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String fileName) {
        this.photo = fileName;
    }

    public List<MedicalFile> getMyMedicalFiles() {
        return myMedicalFiles;
    }

    public void setMyMedicalFiles(List<MedicalFile> myMedicalFiles) {
        this.myMedicalFiles = myMedicalFiles;
    }

    public Cabinet getCabinetDoctor() {
        return cabinetDoctor;
    }

    public void setCabinetDoctor(Cabinet cabinetDoctor) {
        this.cabinetDoctor = cabinetDoctor;
    }

    public List<RendezVous> getRendezvousList() {
        return rendezvousList;
    }

    public void setRendezvousList(List<RendezVous> rendezvousList) {
        this.rendezvousList = rendezvousList;
    }

    public List<DoctorAvailability> getDocAv() {
        return docAv;
    }

    public void setDocAv(List<DoctorAvailability> docAv) {
        this.docAv = docAv;
    }
}