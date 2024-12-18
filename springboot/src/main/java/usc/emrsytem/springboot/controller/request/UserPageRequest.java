package usc.emrsytem.springboot.controller.request;

import lombok.Data;

@Data
public class UserPageRequest extends BaseRequest{
    private String username;
    private String phoneNumber;
    private String role;
    private String email;
}
