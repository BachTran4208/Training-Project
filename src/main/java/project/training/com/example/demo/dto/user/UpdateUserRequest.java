package project.training.com.example.demo.dto.user;

import jakarta.validation.constraints.Email;
import lombok.Data;
import project.training.com.example.demo.entity.Role;
import project.training.com.example.demo.validation.annotation.ValidDob;
import project.training.com.example.demo.validation.annotation.ValidPhone;

@Data
public class UpdateUserRequest {

    private String name;

    @ValidDob
    private String dob;

    @Email
    private String email;

    @ValidPhone
    private String phone;

    private String office;

    private Role role;
}
