package com.securityModel.controllers;

import com.securityModel.models.Patient;
import com.securityModel.models.RefreshToken;
import com.securityModel.models.RendezVous;
import com.securityModel.models.Secretary;
import com.securityModel.payload.request.LoginRequest;
import com.securityModel.payload.response.JwtResponse;
import com.securityModel.repository.PatientRepository;
import com.securityModel.repository.RendezVousRepository;
import com.securityModel.repository.RoleRepository;
import com.securityModel.repository.SecretaryRepository;
import com.securityModel.security.jwt.JwtUtils;
import com.securityModel.security.services.RefreshTokenService;
import com.securityModel.security.services.UserDetailsImpl;
import com.securityModel.utils.StorageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth/Secretary")
public class SecretaryController {
    @Autowired
    SecretaryRepository secretaryRepository;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    private JavaMailSender emailsender;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    RefreshTokenService refreshTokenService;
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    RendezVousRepository rendezVousRepository;
    @Autowired
    StorageService storage;
    @GetMapping("/sec/{id}")
    public Secretary getsec(@PathVariable("id")Long id){
        return secretaryRepository.findById(id).get();
    }
    @GetMapping("/{id}")
    public List<RendezVous>getallrendezvous(@PathVariable("id")Long id){
        return rendezVousRepository.findAllByDoctorId(id);
    }
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        Optional<Secretary> u=secretaryRepository.findByUsername(loginRequest.getUsername()) ;
        if(u.get().isConfirm()==true) {

            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            String jwt = jwtUtils.generateJwtToken(userDetails);

            List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
                    .collect(Collectors.toList());

            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
            return ResponseEntity.ok(new JwtResponse(jwt, refreshToken.getToken(),
                    userDetails.getId(),
                    userDetails.getUsername(),
                    userDetails.getEmail(),
                    roles));

        } else {
            throw new RuntimeException("Secretary account not confirmed");
        }

    }
    @PutMapping("/edit/{id}")
    public void editSecretaryProfile(@PathVariable("id")Long id,Secretary secretary){

        Secretary sec=secretaryRepository.findById(id).get();
        sec.setUsername(secretary.getUsername());
        sec.setAge(secretary.getAge());
        sec.setExperience(secretary.getExperience());
        sec.setEmail(secretary.getEmail());
        secretaryRepository.save(sec);
    }
    @PutMapping("/editPhoto/{id}")
    public void editPhoto(@RequestParam("file") MultipartFile file,@PathVariable("id")Long id){

        Secretary sec=secretaryRepository.findById(id).get();
        String filename=storage.store(file);
        sec.setPhoto(filename);
        secretaryRepository.save(sec);
    }
    @GetMapping("/all")
    public List<Secretary>GetAll(){
        return secretaryRepository.findAll();
    }

}
