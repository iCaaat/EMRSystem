package usc.emrsytem.springboot.controller.request;

import lombok.Data;

@Data
public class PasswordRequest {
    private String password;
    private String phoneNumber;
    private String newPassword;
}
