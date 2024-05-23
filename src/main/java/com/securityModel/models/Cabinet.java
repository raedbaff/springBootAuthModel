package com.securityModel.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

@Entity

public class Cabinet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nomCabinet;
    private String AdresseCabinet;
    private String photo;
    private String description;



    @OneToOne
    @JsonIncludeProperties({"username","email"})
    private Secretary secretary;

    @OneToOne
    @JsonIncludeProperties({"id","username","email"})
    private Doctor doctor;

    public Cabinet() {
    }

    public Cabinet(Long id, String nomCabinet, String adresseCabinet, String photo, String description, Secretary secretary, Doctor doctor) {
        this.id = id;
        this.nomCabinet = nomCabinet;
        AdresseCabinet = adresseCabinet;
        this.photo = photo;
        this.description = description;
        this.secretary = secretary;
        this.doctor = doctor;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomCabinet() {
        return nomCabinet;
    }

    public void setNomCabinet(String nomCabinet) {
        this.nomCabinet = nomCabinet;
    }

    public String getAdresseCabinet() {
        return AdresseCabinet;
    }

    public void setAdresseCabinet(String adresseCabinet) {
        AdresseCabinet = adresseCabinet;
    }

    public Secretary getSecretary() {
        return secretary;
    }

    public void setSecretary(Secretary secretary) {
        this.secretary = secretary;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }
}


