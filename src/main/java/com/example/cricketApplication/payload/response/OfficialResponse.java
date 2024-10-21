package com.example.cricketApplication.payload.response;

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
}
