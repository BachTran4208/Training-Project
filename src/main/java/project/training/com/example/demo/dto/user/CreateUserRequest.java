package project.training.com.example.demo.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import project.training.com.example.demo.entity.Role;
import project.training.com.example.demo.validation.annotation.ValidDob;
import project.training.com.example.demo.validation.annotation.ValidPhone;

@Data
public class CreateUserRequest {
    @NotBlank(message = "Name is required")
    private String name;

    @ValidDob
    private String dob;

    @Email
    private String email;

    @ValidPhone
    private String phone;

    @NotBlank
    private String office;

    private Role role;
}