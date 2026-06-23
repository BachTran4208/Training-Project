package project.training.com.example.demo.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;

    private String name;

    // mmddyy
    private String dob;

    // XXXX XXX XXX
    private String phone;

    private String email;

    private String office;

    private String role;
}
