package com.securityModel.controllers;

import java.util.*;
import java.util.stream.Collectors;

import com.securityModel.models.*;
import com.securityModel.payload.request.LoginRequest;
import com.securityModel.payload.request.SignupRequest;
import com.securityModel.payload.request.TokenRefreshRequest;
import com.securityModel.payload.response.JwtResponse;
import com.securityModel.payload.response.MessageResponse;
import com.securityModel.payload.response.TokenRefreshResponse;
import com.securityModel.repository.RoleRepository;
import com.securityModel.repository.UserRepository;
import com.securityModel.security.jwt.JwtUtils;
import com.securityModel.security.services.RefreshTokenService;
import com.securityModel.security.services.UserDetailsImpl;
import com.securityModel.utils.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.securityModel.exception.TokenRefreshException;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
  @Autowired
  private JavaMailSender emailsender;
  @Autowired
  private EmailService emailService;
  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  UserRepository userRepository;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  PasswordEncoder encoder;

  @Autowired
  JwtUtils jwtUtils;

  @Autowired
  RefreshTokenService refreshTokenService;

  @PostMapping("/login")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

    Authentication authentication = authenticationManager
            .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

    Optional<User> u=userRepository.findByUsername(loginRequest.getUsername()) ;
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
      throw new RuntimeException("user not confirmed");
    }

  }




  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest, HttpServletRequest request) throws MessagingException, MessagingException {
    if (userRepository.existsByUsername(signUpRequest.getUsername())) {
      return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
    }

    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
      return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
    }

    // Create new user's account
    User user = new User(signUpRequest.getUsername(), signUpRequest.getEmail(),
            encoder.encode(signUpRequest.getPassword()));

    Set<String> strRoles = signUpRequest.getRole();
    Set<Role> roles = new HashSet<>();

    if (strRoles == null) {
      Role userRole = roleRepository.findByName(ERole.ROLE_USER)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
      roles.add(userRole);
    } else {
      strRoles.forEach(role -> {
        switch (role) {
          case "admin":
            Role providerRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(providerRole);

            break;
          case "patient":
            Role patientRole = roleRepository.findByName(ERole.ROLE_PATIENT)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(patientRole);
            break;
          case "secretary":
            Role secretaryRole = roleRepository.findByName(ERole.ROLE_SECRETARY)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(secretaryRole);
            break;
          case "doctor":
            Role doctorRole = roleRepository.findByName(ERole.ROLE_DOCTOR)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(doctorRole);
            break;
          default:
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        }
      });
    }

    user.setRoles(roles);
    userRepository.save(user);
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

    return ResponseEntity.ok(new MessageResponse("User registered successfully! check your email for confirmation"));
  }
  @GetMapping("/confirm")
  public ResponseApiObject confirmEmail(@RequestParam String email) {

    ResponseApiObject responseApiObject= new ResponseApiObject();

    try {
      User user = userRepository.findByEmail(email);
      if (user != null) {
        user.setConfirm(true);
        responseApiObject.setMessage("user confirmed ");
        responseApiObject.setData(userRepository.save(user));
        responseApiObject.setStatus(200);
      }
    }catch (Exception e){
      responseApiObject.setStatus(406);
      responseApiObject.setMessage("something wrong");
    }

    return responseApiObject;


  }

  @PutMapping("/confirrm")
  public ResponseEntity<?> confirmuser(@RequestParam String email){
    User user = userRepository.findByEmail(email);
    if (user != null) {
      user.setConfirm(true);
      userRepository.save(user);
      return ResponseEntity.ok(new MessageResponse("User confirmed"));
    }
    else{
      return ResponseEntity.ok(new MessageResponse("Error"));
    }
  }
  @GetMapping("/c")
  public HashMap<String,String> confirmu(@RequestParam String email){
    HashMap message = new HashMap();
    try {
      User user = userRepository.findByEmail(email);
      if (user != null)
        user.setConfirm(true);
      userRepository.save(user);
      message.put("etat", "user confirmed ");
      return message;

    } catch(Exception e){
      message.put("etat","Error");
      return  message;
    }
  }

  @PostMapping("/refreshtoken")
  public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequest request) {
    String requestRefreshToken = request.getRefreshToken();

    return refreshTokenService.findByToken(requestRefreshToken)
            .map(refreshTokenService::verifyExpiration)
            .map(RefreshToken::getUser)
            .map(user -> {
              String token = jwtUtils.generateTokenFromUsername(user.getUsername());
              return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
            })
            .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                    "Refresh token is not in database!"));
  }

  @PostMapping("/signout")
  public ResponseEntity<?> logoutUser() {
    UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Long userId = userDetails.getId();
    refreshTokenService.deleteByUserId(userId);
    return ResponseEntity.ok(new MessageResponse("Log out successful!"));
  }

  //forget password
  @PostMapping("/forgetpassword")
  public HashMap<String,String> resetPassword(String email) throws MessagingException {
    HashMap message = new HashMap();
    User userexisting = userRepository.findByEmail(email);
    if (userexisting == null) {
      message.put("user", "user not found");
      return message;
    }
    UUID token = UUID.randomUUID();
    userexisting.setPasswordResetToken(token.toString());
    userexisting.setId(userexisting.getId());
    Mail mail = new Mail();
    mail.setContent("votre nouveau token est : " +userexisting.getPasswordResetToken());
    mail.setFrom("itgate@gmail.com");
    mail.setTo(userexisting.getEmail());
    mail.setSubject("Reset password");
    emailService.sendSimpleMessage(mail);
    userRepository.saveAndFlush(userexisting);
    message.put("user", "user found , check your email");

    return message;

  }


  //reset password
  @PostMapping("/savePassword/{passwordResetToken}")
  public HashMap<String,String> savePassword(@PathVariable String passwordResetToken, String newPassword) {

    User userexisting = userRepository.findByPasswordResetToken(passwordResetToken);
    HashMap message = new HashMap();

    if (userexisting != null) {
      userexisting.setId(userexisting.getId());
      userexisting.setPassword(new BCryptPasswordEncoder().encode(newPassword));
      userexisting.setPasswordResetToken(null);
      userRepository.save(userexisting);
      message.put("resetpassword", "proccesed");
      return message;

    } else {
      message.put("resetpassword", "failed");
      return message;

    }



  }}