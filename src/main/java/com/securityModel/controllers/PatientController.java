package com.securityModel.controllers;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.securityModel.Serializer.PatientSerializeObject;
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
import org.springframework.http.MediaType;
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
@RequestMapping(value="/api/auth/Patient",produces = MediaType.APPLICATION_JSON_VALUE)
public class PatientController {
    @Autowired
    PatientRepository patientRepository;
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
    DoctorRepository doctorRepository;
    @Autowired
    StorageService storage;
    @Autowired
    DoctorRatingRepository doctorRatingRepository;
    @Autowired
    ComplaintRepository complaintRepository;
    @PostMapping("/signup")
    public ResponseEntity<?> registerPatient(@RequestParam("file") MultipartFile file,SignupRequest signUpRequest, HttpServletRequest request) throws MessagingException, MessagingException, IOException {
        if (patientRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

        if (patientRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        Patient patient = new Patient(signUpRequest.getUsername(), signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()),signUpRequest.getAdresse(),signUpRequest.getAge(),signUpRequest.getSocialAccount(),signUpRequest.getPhoto(),signUpRequest.getGender());
        Set<Role> roles = new HashSet<>();


            Role patientRole = roleRepository.findByName(ERole.ROLE_PATIENT)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(patientRole);
        String fileName = storage.store(file);
        patient.setPhoto(fileName);


        patient.setRoles(roles);
        patientRepository.save(patient);
        String appUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        System.out.println(appUrl);

        //mail confirmation
        //user.setConfirm(false);
        String from ="admin@gmail.com" ;
        String to = signUpRequest.getEmail();
        MimeMessage message = emailsender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setSubject("Complete Registration!");
        helper.setFrom(from);
        helper.setTo(to);
        helper.setText("<HTML><body> <a href=\"http://localhost:8088/api/auth/confirm?email="+signUpRequest.getEmail()+"\">VERIFY</a></body></HTML>",true);
        emailsender.send(message);

        return ResponseEntity.ok(new MessageResponse("patient registered successfully! check your email for confirmation"));
    }
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        Optional<Patient> u=patientRepository.findByUsername(loginRequest.getUsername()) ;
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
            throw new RuntimeException("Patient account not confirmed");
        }

    }
    @GetMapping("/allPatients")
    public ResponseEntity<List<Patient>>GetAllPatients(){
        List<Patient>patients=patientRepository.findAll();
        return ResponseEntity.ok(patients);
    }
    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        Resource file = storage.loadFile(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }
    @GetMapping("/{id}")
    public Patient getpatient(@PathVariable("id")Long id){
        return patientRepository.findById(id).get();
    }
    @PostMapping("/rate/{doctorid}")
    public DoctorRating RateDoctor(Principal principal, @PathVariable("doctorid") Long doctorid, @RequestBody DoctorRating DoctorRating){
        String patient=principal.getName();
        Patient patient1=patientRepository.findByUsername(patient).get();
        Doctor doc=doctorRepository.findById(doctorid).get();
        DoctorRating rating=new DoctorRating();
        rating.setPatient(patient1);
        rating.setDoctor(doc);
        rating.setRating(DoctorRating.getRating());
        rating.setPatientNotes(DoctorRating.getPatientNotes());
        System.out.println("DoctorRating object: " + DoctorRating);
        System.out.println("Rating value: " + DoctorRating.getRating());

        return doctorRatingRepository.save(rating);

    }
    @GetMapping("/rate/all")
    public List<DoctorRating>getallratings(){
        return doctorRatingRepository.findAll();
    }
    @GetMapping("/rate/{id}")
    public List<DoctorRating> getDoctorRating(@PathVariable("id")Long id){
        Doctor doc=doctorRepository.findById(id).get();
        return doctorRatingRepository.findDoctorRatingsByDoctorId(doc.getId());
    }
    @PutMapping("/{id}")
    public Patient updateProfile(@RequestParam("file") MultipartFile file,@PathVariable("id") Long id,Patient pat){
        Patient patient=patientRepository.findById(id).get();
        String filename=storage.store(file);
        patient.setPhoto(filename);
        patient.setUsername(pat.getUsername());
        patient.setEmail(pat.getEmail());
        patient.setAge(pat.getAge());
        patient.setAdresse(pat.getAdresse());
        patient.setSocialAccount(pat.getSocialAccount());
        patient.setProfession(pat.getProfession());
        return patientRepository.save(patient);
    }
    @PostMapping("/complaint")
    public void FileComplaint(Principal principal,Complaint complaint){
        String name=principal.getName();
        Patient patient=patientRepository.findByUsername(name).get();
        complaint.setPatient(patient);
        complaintRepository.save(complaint);

    }
}

