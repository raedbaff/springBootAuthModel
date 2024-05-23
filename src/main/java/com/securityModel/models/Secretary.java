package com.securityModel.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(	name = "secretaries",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")
        })
public class Secretary extends User{
    private String photo;
    @OneToOne(mappedBy = "secretary")
    private Cabinet cabinet;
    private Integer experience;
    private Integer age;


    public Integer getExperience() {
        return experience;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Secretary(String photo) {
        this.photo = photo;
    }

    public Secretary(String username, String email, String password, String photo) {
        super(username, email, password);
        this.photo = photo;
    }

    public Secretary() {

    }

    public Cabinet getCabinet() {
        return cabinet;
    }


    public void setCabinet(Cabinet cabinet) {
        this.cabinet = cabinet;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }


}
