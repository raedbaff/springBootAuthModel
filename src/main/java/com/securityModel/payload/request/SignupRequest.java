package com.securityModel.payload.request;

import java.util.Set;

import com.securityModel.models.DomainMedical;
import jakarta.validation.constraints.*;
 
public class SignupRequest {
    @NotBlank
    @Size(min = 3, max = 20)
    private String username;
 
    @NotBlank
    @Size(max = 50)
    @Email
    private String email;
    
    private Set<String> role;
    
    @NotBlank
    @Size(min = 6, max = 40)
    private String password;
    private boolean confirm=false;
    private String passwordResetToken;

    public boolean isConfirm() {
        return confirm;
    }
    private String Adresse;
    private Integer Age;
    private String SocialAccount;
    private String photo;
    private String Gender;

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    private com.securityModel.models.DomainMedical DomainMedical;

    public void setConfirm(boolean confirm) {
        this.confirm = confirm;
    }

    public String getUsername() {
        return username;
    }
 
    public void setUsername(String username) {
        this.username = username;
    }
 
    public String getEmail() {
        return email;
    }
 
    public void setEmail(String email) {
        this.email = email;
    }
 
    public String getPassword() {
        return password;
    }
 
    public void setPassword(String password) {
        this.password = password;
    }
    
    public Set<String> getRole() {
      return this.role;
    }
    
    public void setRole(Set<String> role) {
      this.role = role;
    }

    public String getPasswordResetToken() {
        return passwordResetToken;
    }

    public void setPasswordResetToken(String passwordResetToken) {
        this.passwordResetToken = passwordResetToken;
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

    public com.securityModel.models.DomainMedical getDomainMedical() {
        return DomainMedical;
    }

    public void setDomainMedical(com.securityModel.models.DomainMedical domainMedical) {
        DomainMedical = domainMedical;
    }
}
