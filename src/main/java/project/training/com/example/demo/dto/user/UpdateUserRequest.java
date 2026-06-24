package project.training.com.example.demo.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import project.training.com.example.demo.entity.Role;
import project.training.com.example.demo.validation.annotation.ValidDob;
import project.training.com.example.demo.validation.annotation.ValidPhone;

@Data
public class UpdateUserRequest {

    private String name;

    @NotBlank(message = "Dob is not blank")
    @ValidDob
    private String dob;

    @NotBlank(message = "Email is not blank")
    @Email
    private String email;

    @NotBlank(message = "Phone is not blank")
    @ValidPhone
    private String phone;

    private String office;

    private Role role;
}
