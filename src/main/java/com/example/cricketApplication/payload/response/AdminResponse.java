package com.example.cricketApplication.payload.response;

import java.util.Date;

@lombok.Getter
@lombok.Setter
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
public class AdminResponse {

    private Long adminId;
    private String name;
    private String contactNo;
    private String email;
    private String password;
    private String username;
    private String createdBy;
    private Date createdOn;
    private String updatedBy;
    private Date updatedOn;
}
