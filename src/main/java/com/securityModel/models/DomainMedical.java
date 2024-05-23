package com.securityModel.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class DomainMedical {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String photo;


    @OneToMany(mappedBy = "domainMedical",cascade=CascadeType.ALL)
    @JsonIncludeProperties({"id","username","photo"})
    private List<Doctor> list;

    public DomainMedical(Long id, String name, String photo, List<Doctor> list) {
        this.id = id;
        this.name = name;
        this.photo = photo;
        this.list = list;
    }

    public DomainMedical() {
        this.list=new ArrayList<>();

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public List<Doctor> getList() {
        return list;
    }

    public void setList(List<Doctor> list) {
        this.list = list;
    }
}
