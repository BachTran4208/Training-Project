package project.training.com.example.demo.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    
    @NotBlank(message = "Email is not blank")
    @Email
    private String email;

    @NotBlank(message = "Password is not blank")
    private String password;
    
}
