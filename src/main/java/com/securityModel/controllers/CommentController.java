package com.securityModel.controllers;

import com.securityModel.models.Comment;
import com.securityModel.models.Patient;
import com.securityModel.models.Post;
import com.securityModel.repository.CommentRepository;
import com.securityModel.repository.PatientRepository;
import com.securityModel.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/Comment")

public class CommentController {
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    PatientRepository patientRepository;
    @Autowired
    PostRepository postRepository;
    @PostMapping("/add/{id}")
    public Comment addComment(@PathVariable("id")Long id, Principal principal, Comment comment){
        String patientname=principal.getName();
        Patient pat=patientRepository.findByUsername(patientname).get();
        Post post=postRepository.findById(id).get();
        Comment newComment = new Comment(); // create a new Comment entity
        newComment.setPatient(pat);
        newComment.setPost(post);
        newComment.setContent(comment.getContent()); // set the new comment text
        newComment.setTimestamp(LocalDateTime.now());
        return commentRepository.save(newComment);
    }
    @GetMapping("/{id}")
    public List<Comment> getcommentOfAPost(@PathVariable("id") Long id){
       return  commentRepository.findCommentsByPostId(id);
    }
}
