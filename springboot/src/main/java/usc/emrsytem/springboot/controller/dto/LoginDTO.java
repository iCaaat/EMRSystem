package usc.emrsytem.springboot.controller.dto;

import lombok.Data;

@Data
public class LoginDTO {
    private Integer userId;
    private String username;
    private String email;
    private String phoneNumber;
    private String role;
    private String token;
}
