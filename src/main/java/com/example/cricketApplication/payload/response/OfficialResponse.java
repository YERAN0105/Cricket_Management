package com.example.cricketApplication.payload.response;

import java.util.Date;

@lombok.Getter
@lombok.Setter
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
public class OfficialResponse {

    private Long officialId;
    private String name;
    private String email;
    private String contactNo;
    private String position;
    private String password;
    private String username;
    private String createdBy;
    private Date createdOn;
    private String updatedBy;
    private Date updatedOn;
}
