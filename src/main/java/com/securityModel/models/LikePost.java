package com.securityModel.models;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import jakarta.persistence.*;

@Entity

public class LikePost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    @ManyToOne
    @JsonIncludeProperties("id")
    private Post likepost;
    @ManyToOne
    @JsonIncludeProperties("id")
    private Patient patient;
    private boolean active=false;

    public LikePost() {
    }

    public LikePost(Long id, Post likepost, Patient patient, boolean active) {
        this.id = id;
        this.likepost = likepost;
        this.patient = patient;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Post getLikepost() {
        return likepost;
    }

    public void setLikepost(Post likepost) {
        this.likepost = likepost;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
