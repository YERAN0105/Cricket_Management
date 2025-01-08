package com.example.cricketApplication.payload.response;

import java.util.List;

public class JwtResponse {
  private String token;
  private String type = "Bearer";
  private Long id;
  private String username;
  private String email;
  private List<String> roles;
  private Long playerId;
  private Long coachId;
  private Long officialId;
//  private Long adminId;

  public JwtResponse(String accessToken, Long id, String username, String email, List<String> roles, Long playerId, Long coachId, Long officialId) {
    this.token = accessToken;
    this.id = id;
    this.username = username;
    this.email = email;
    this.roles = roles;
    this.playerId = playerId;
    this.coachId = coachId;
    this.officialId = officialId;
//    this.adminId = adminId;
  }

  public String getAccessToken() {
    return token;
  }

  public void setAccessToken(String accessToken) {
    this.token = accessToken;
  }

  public String getTokenType() {
    return type;
  }

  public void setTokenType(String tokenType) {
    this.type = tokenType;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public List<String> getRoles() {
    return roles;
  }

  public void setPlayerId(Long playerId) {
    this.playerId = playerId;
  }

  public Long getPlayerId() {return playerId;}

  public void setCoachId(Long coachId) {this.coachId = coachId;}
  public Long getCoachId() {return coachId;}
  public void setOfficialId(Long officialId) {this.officialId = officialId;}
  public Long getOfficialId() {return officialId;}

//  public void setAdminId(Long adminId) {this.adminId = adminId;}
//  public Long getAdminId() {return adminId;}
}
