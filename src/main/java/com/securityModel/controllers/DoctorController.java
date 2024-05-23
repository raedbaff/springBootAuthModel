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
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth/Doctor")

public class DoctorController {

    @Autowired
    DoctorRepository doctorRepository;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    SecretaryRepository secretaryRepository;
    @Autowired
    private JavaMailSender emailsender;
    @Autowired
    private PatientRepository PatientRepository;
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
    @Autowired
    CabinetRepository cabinetRepository;
    @PostMapping("/signup")
    public ResponseEntity<?> registerDoctor(@RequestParam("file") MultipartFile file, SignupRequest signUpRequest, HttpServletRequest request) throws MessagingException, MessagingException, IOException {
        if (doctorRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

        if (doctorRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        Doctor doctor = new Doctor(signUpRequest.getUsername(), signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()), signUpRequest.getDomainMedical(), signUpRequest.getAdresse(), signUpRequest.getPhoto());
        Set<Role> roles = new HashSet<>();


        Role patientRole = roleRepository.findByName(ERole.ROLE_DOCTOR)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        roles.add(patientRole);
        String fileName = storage.store(file);
        doctor.setPhoto(fileName);


        doctor.setRoles(roles);

        doctorRepository.save(doctor);
        DomainMedical domain=doctor.getDomainMedical();
        List<Doctor>docs=domain.getList();
        docs.add(doctor);
        domain.setList(docs);


        //String appUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        //System.out.println(appUrl);

        //mail confirmation
        //user.setConfirm(false);
        //String from ="admin@gmail.com" ;
        //String to = signUpRequest.getEmail();
        //MimeMessage message = emailsender.createMimeMessage();
        //MimeMessageHelper helper = new MimeMessageHelper(message);
        //helper.setSubject("Complete Registration!");
        //helper.setFrom(from);
        //helper.setTo(to);
        //helper.setText("<HTML><body> <a href=\"http://localhost:8088/api/auth/confirm?email="+signUpRequest.getEmail()+"\">VERIFY</a></body></HTML>",true);
        //emailsender.send(message);

        return ResponseEntity.ok(new MessageResponse("doctor registered successfully! please wait for admin to confirm your account"));

    }
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        Optional<Doctor> u=doctorRepository.findByUsername(loginRequest.getUsername()) ;
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
            throw new RuntimeException("doctor account not confirmed");
        }

    }
    @PostMapping("/secretary/signup")
    public ResponseEntity<?> registerSecretary(@RequestParam("file") MultipartFile file,SignupRequest signUpRequest, HttpServletRequest request) throws MessagingException, MessagingException {
        if (secretaryRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

        if (secretaryRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        Secretary secretary = new Secretary(signUpRequest.getUsername(), signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()),signUpRequest.getPhoto());
        Set<Role> roles = new HashSet<>();



        Role SecretaryRole = roleRepository.findByName(ERole.ROLE_SECRETARY)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        roles.add(SecretaryRole);


        secretary.setRoles(roles);
        secretary.setConfirm(true);
        String filename=storage.store(file);
        secretary.setPhoto(filename);
        secretaryRepository.save(secretary);
        String appUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        System.out.println(appUrl);

        return ResponseEntity.ok(new MessageResponse("Secretary registered successfully! check your email for confirmation"));

    }
    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        Resource file = storage.loadFile(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }

    @GetMapping("/Predict")
    public String predict(){
        return null;

    }
    @GetMapping("/{id}")
    public List<RendezVous> getRendezvousByDoctorId(@PathVariable Long id) {
        return rendezVousRepository.findByDoctorId(id);
    }
    @GetMapping("/allSec")
    public List<Secretary>getall(){
        return secretaryRepository.findAll();
    }
    @GetMapping("/myCabinet")
    public Cabinet getMyCabinet(Principal principal){
        String doc=principal.getName();
        return cabinetRepository.findByDoctorUsername(doc);
    }
    @PutMapping("/")
    public Doctor editProfile(Doctor doctor,Principal principal){
        String doc=principal.getName();
        Doctor DOC=doctorRepository.findDoctorByUsername(doc);
        DOC.setUsername(doctor.getUsername());
        DOC.setAge(doctor.getAge());
        DOC.setEmail(doctor.getEmail());
        DOC.setAdresse(doctor.getAdresse());
        DOC.setDescription(doctor.getDescription());
        return doctorRepository.save(DOC);


    }






}
