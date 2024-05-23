package com.securityModel.controllers;

import com.securityModel.models.*;
import com.securityModel.payload.request.LoginRequest;
import com.securityModel.payload.request.SignupRequest;
import com.securityModel.payload.response.JwtResponse;
import com.securityModel.payload.response.MessageResponse;
import com.securityModel.repository.*;
import com.securityModel.security.jwt.JwtUtils;
import com.securityModel.security.services.RefreshTokenService;
import com.securityModel.security.services.UserDetailsImpl;
import com.securityModel.utils.StorageService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth/Admin")
public class AdminController {
    @Autowired
    DoctorRepository doctorRepository;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    AdminRepository adminRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    private JavaMailSender emailsender;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    RefreshTokenService refreshTokenService;
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    StorageService storage;
    @Autowired
    PostRepository postRepository;
    @Autowired
    ComplaintRepository complaintRepository;

    @PostMapping("/signup")
    public ResponseEntity<?> registerDoctor(@Valid @RequestBody SignupRequest signUpRequest, HttpServletRequest request) throws MessagingException, MessagingException {
        if (adminRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

        if (adminRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        Admin admin = new Admin(signUpRequest.getUsername(), signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));
        Set<Role> roles = new HashSet<>();
        boolean confirm;


        Role AdminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        roles.add(AdminRole);
        admin.setConfirm(true);


        admin.setRoles(roles);
        adminRepository.save(admin);
        return ResponseEntity.ok(new MessageResponse("admin registered successfully!"));

    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        Optional<Admin> u = adminRepository.findByUsername(loginRequest.getUsername());
        if (u.get().isConfirm() == true) {

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
            throw new RuntimeException("admin account not confirmed");
        }

    }

    @GetMapping("/allDoctors")
    public List<Doctor> GetAllDoctors() {
        return doctorRepository.findAll();
    }

    @GetMapping("/{id}")
    public Doctor GetDoctor(@PathVariable("id") Long id) {
        return doctorRepository.findById(id).get();
    }

    @PutMapping("/confirm/{id}")
    public void ConfirmDoctor(@PathVariable Long id) {
        Doctor doctor = doctorRepository.findById(id).get();
        doctor.setConfirm(true);
        doctorRepository.save(doctor);
    }

    @PutMapping("/editDoctor/{id}")
    public Doctor EditDoctor(@PathVariable Long id, Doctor DoctorDetails, @RequestParam("file") MultipartFile file) {
        Doctor doctor = doctorRepository.findById(id).get();
        doctor.setUsername(DoctorDetails.getUsername());
        doctor.setEmail(DoctorDetails.getEmail());
        doctor.setPassword(DoctorDetails.getPassword());
        doctor.setAge(DoctorDetails.getAge());
        doctor.setDomainMedical(DoctorDetails.getDomainMedical());
        doctor.setPoints(DoctorDetails.getPoints());
        doctor.setPatients(DoctorDetails.getPatients());
        doctor.setAdresse(DoctorDetails.getAdresse());
        String fileName = storage.store(file);
        doctor.setPhoto(fileName);
        return doctorRepository.save(doctor);


    }
    @PutMapping("/{id}")
    public Post acceptPost(@PathVariable("id")Long id){
        Post post=postRepository.findById(id).get();
        post.setAccepted(true);
        return postRepository.save(post);

    }
    @GetMapping("/complaints")
    public List<Complaint>GetAllComplaints(){
        return complaintRepository.findAll();
    }





}
