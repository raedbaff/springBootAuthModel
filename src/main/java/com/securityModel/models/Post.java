package com.securityModel.models;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Post {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    private String title;
    private String photo;
    private String Description;

    @ManyToOne
    @JsonIncludeProperties({"username","photo"})
    private Patient author;
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonIncludeProperties({"id", "content","patient"})
    private List<Comment>comments;


    private LocalDateTime timestamp;
    @OneToMany(mappedBy = "likepost",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<LikePost>likes;
    private boolean accepted;

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public List<LikePost> getLikes() {
        return likes;
    }

    public void setLikes(List<LikePost> likes) {
        this.likes = likes;
    }

    public Post() {
    }

    public Post(Long id, String title, String photo, String description, Patient author, List<Comment> comments, LocalDateTime timestamp) {
        this.id = id;
        this.title = title;
        this.photo = photo;
        this.Description = description;
        this.author = author;
        this.comments = comments;
        this.timestamp = timestamp;
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public Patient getAuthor() {
        return author;
    }

    public void setAuthor(Patient author) {
        this.author = author;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

}
