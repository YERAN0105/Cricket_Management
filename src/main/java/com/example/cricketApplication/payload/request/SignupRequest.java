package com.example.cricketApplication.payload.request;

import com.example.cricketApplication.models.Membership;
import com.example.cricketApplication.models.Role;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Date;
import java.util.Set;

@lombok.Getter
@lombok.Setter
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
public class SignupRequest {
  @NotBlank
  @Size(min = 3, max = 20)
  private String username;

  @NotBlank
  @Size(max = 50)
  @Email
  private String email;

  private Set<String> roles;

  @NotBlank
  @Size(min = 6, max = 40)
  private String password;

//after modifications

  //@NotBlank
  private String name;

  @Temporal(TemporalType.DATE)
  private Date dateOfBirth;

  private String contactNo;

  private String battingStyle;
  private String bowlingStyle;

  private String status;

  private String image;

  private String playerRole;

  private Membership membership;
  private String address;
  private String description;
  private String position;





//
//  public String getUsername() {
//    return username;
//  }
//
//  public void setUsername(String username) {
//    this.username = username;
//  }
//
//  public String getEmail() {
//    return email;
//  }
//
//  public void setEmail(String email) {
//    this.email = email;
//  }
//
//  public String getPassword() {
//    return password;
//  }
//
//  public void setPassword(String password) {
//    this.password = password;
//  }
//
//  public Set<String> getRoles() {
//    return this.roles;
//  }
//
//  public void setRole(Set<String> roles) {
//    this.roles = roles;
//  }
}
