package usc.emrsytem.springboot.controller.request;

import lombok.Data;

@Data
public class LoginRequest {
    private String phoneNumber;
    private String password;
}
