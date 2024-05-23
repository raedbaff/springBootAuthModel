package com.securityModel.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import com.securityModel.Serializer.RendezVousSerializeList;

import jakarta.persistence.*;

import javax.print.Doc;
import java.util.List;

@Entity
@Table(	name = "patients")

public class Patient extends User{
    private String Adresse;
    private Integer Age;
    private String SocialAccount;
    private String photo;
    private String Gender="male";
    @OneToMany(mappedBy = "patient")
    private List<MedicalBill>myBills;

    private String profession;
    @ManyToMany
    @JsonIncludeProperties({"id","username","photo"})
    private List<Doctor> mydocs;
    @OneToMany(mappedBy = "author",cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIncludeProperties("id")
    private List<Post>posts;

    @OneToMany(mappedBy = "patient",cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIncludeProperties("id")
    private List<Comment>comments;
    @OneToMany(mappedBy = "patient",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<LikePost>likes;
    @OneToMany(mappedBy = "patient",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Complaint>complaints;

    public List<Complaint> getComplaints() {
        return complaints;
    }

    public void setComplaints(List<Complaint> complaints) {
        this.complaints = complaints;
    }

    public List<LikePost> getLikes() {
        return likes;
    }

    public void setLikes(List<LikePost> likes) {
        this.likes = likes;
    }

    public List<Post> getPosts() {
        return posts;
    }


    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    @OneToMany(mappedBy = "patient")
    private List<DoctorRating>patientRatings;

    public List<DoctorRating> getPatientRatings() {
        return patientRatings;
    }

    public void setPatientRatings(List<DoctorRating> patientRatings) {
        this.patientRatings = patientRatings;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    @OneToMany(mappedBy = "patientName")
    @JsonIncludeProperties({"id","appointmentReason","doctor"})
    private List<RendezVous> myRendezVous;
    @OneToMany(mappedBy = "patient",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<MedicalFile> medicalFiles;

    public List<MedicalFile> getMedicalFiles() {
        return medicalFiles;
    }

    public void setMedicalFiles(List<MedicalFile> medicalFiles) {
        this.medicalFiles = medicalFiles;
    }

    public Patient() {

    }

    public List<MedicalBill> getMyBills() {
        return myBills;
    }

    public void setMyBills(List<MedicalBill> myBills) {
        this.myBills = myBills;
    }

    public List<Doctor> getMydocs() {
        return mydocs;
    }

    public void setMydocs(List<Doctor> mydocs) {
        this.mydocs = mydocs;
    }

    public String getAdresse() {
        return Adresse;
    }

    public void setAdresse(String adresse) {
        Adresse = adresse;
    }

    public Integer getAge() {
        return Age;
    }

    public void setAge(Integer age) {
        Age = age;
    }

    public String getSocialAccount() {
        return SocialAccount;
    }

    public void setSocialAccount(String socialAccount) {
        SocialAccount = socialAccount;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Patient(String adresse, Integer age, String socialAccount, String photo) {
        Adresse = adresse;
        Age = age;
        SocialAccount = socialAccount;
        this.photo = photo;

    }

    public Patient(String username, String email, String password, String adresse, Integer age, String socialAccount, String photo,String gender) {
        super(username, email, password);
        Adresse = adresse;
        Age = age;
        SocialAccount = socialAccount;
        this.photo = photo;
        this.Gender=gender;
    }
    //@JsonIgnore

    public List<RendezVous> getMyRendezVous() {
        return myRendezVous;
    }

    public void setMyRendezVous(List<RendezVous> myRendezVous) {
        this.myRendezVous = myRendezVous;
    }
}
