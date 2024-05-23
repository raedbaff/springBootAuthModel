package com.securityModel.controllers;


import com.securityModel.models.LikePost;
import com.securityModel.models.Patient;
import com.securityModel.models.Post;
import com.securityModel.repository.LikeRepository;
import com.securityModel.repository.PatientRepository;
import com.securityModel.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/like")


public class LikeController {
    @Autowired
    PatientRepository patientRepository;
    @Autowired
    PostRepository postRepository;
    @Autowired
    LikeRepository likeRepository;
    @PostMapping("/{id}")
    public LikePost like(@PathVariable("id")Long id, LikePost like, Principal principal){
        String patientname= principal.getName();
        Patient patient=patientRepository.findByUsername(patientname).get();
        LikePost likepost=new LikePost();
        likepost.setPatient(patient);
        Post post=postRepository.findById(id).get();
        likepost.setLikepost(post);
        likepost.setActive(true);
        return likeRepository.save(likepost);
    }
    @DeleteMapping("/{id}")
    public void deleteLike(@PathVariable("id") Long id){
        likeRepository.deleteById(id);
    }
    @GetMapping("/{id}")
    public List<LikePost> getlikes(@PathVariable("id")Long id){
        return likeRepository.findAllByLikepostId(id);

    }
}
