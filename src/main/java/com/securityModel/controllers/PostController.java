package com.securityModel.controllers;

import com.securityModel.models.Cabinet;
import com.securityModel.models.LikePost;
import com.securityModel.models.Patient;
import com.securityModel.models.Post;
import com.securityModel.repository.CommentRepository;
import com.securityModel.repository.LikeRepository;
import com.securityModel.repository.PatientRepository;
import com.securityModel.repository.PostRepository;
import com.securityModel.utils.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/Post")

public class PostController {
    @Autowired
    PostRepository postRepository;
    @Autowired
    StorageService storage;
    @Autowired
    PatientRepository patientRepository;
    @Autowired
    LikeRepository likeRepository;
    @Autowired
    CommentRepository commentRepository;
    @PostMapping("/savePost")
    public Post addPost(@RequestParam("file") MultipartFile file,Principal principal, Post post){
        String patient=principal.getName();
        Patient pat=patientRepository.findByUsername(patient).get();
        post.setAuthor(pat);
        String fileName = storage.store(file);
        post.setPhoto(fileName);
        post.setTimestamp(LocalDateTime.now());
        LikePost likepost=new LikePost();
        return postRepository.save(post);
    }
    @GetMapping("/{id}")
    public Post getPost(@PathVariable("id")Long id){
        Post post=postRepository.findById(id).get();
        return post;

    }
    @GetMapping("/all")
    public List<Post> getposts(){
        return postRepository.findAll();


    }
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id){
         Post post=postRepository.findById(id).get();
         postRepository.delete(post);

    }
}
