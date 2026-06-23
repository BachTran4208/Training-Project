package project.training.com.example.demo.mapper;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

import project.training.com.example.demo.dto.user.CreateUserRequest;
import project.training.com.example.demo.dto.user.UserResponse;
import project.training.com.example.demo.entity.User;

@Component
public class UserMapper {

    private static final DateTimeFormatter REQUEST_DOB_FORMAT =
            DateTimeFormatter.ofPattern("MM-dd-yyyy");

    private static final DateTimeFormatter RESPONSE_DOB_FORMAT =
            DateTimeFormatter.ofPattern("MMddyy");

    public UserResponse toUserResponse(User user) {

        if (user == null) {
            return null;
        }

        UserResponse response = new UserResponse();

        response.setId(user.getId());
        response.setName(user.getName());
        response.setDob(formatDob(user.getDob()));
        response.setEmail(user.getEmail());
        response.setPhone(formatPhone(user.getPhone()));
        response.setOffice(user.getOffice());
        response.setRole(user.getRole().name());

        return response;
    }

    public User toUser(CreateUserRequest request) {

        if (request == null) {
            return null;
        }

        User user = new User();

        user.setName(request.getName());
        user.setDob(convertDob(request.getDob()));
        user.setEmail(request.getEmail().toLowerCase());
        user.setPhone(request.getPhone());
        user.setOffice(request.getOffice());

        user.setRole(request.getRole());

        return user;
    }

    public Instant convertDob(String dob) {

        LocalDate date =
                LocalDate.parse(dob, REQUEST_DOB_FORMAT);

        return date.atStartOfDay()
                .toInstant(ZoneOffset.UTC);
    }

    private String formatDob(Instant dob) {

        if (dob == null) {
            return null;
        }

        return dob.atZone(ZoneOffset.UTC)
                .toLocalDate()
                .format(RESPONSE_DOB_FORMAT);
    }

    private String formatPhone(String phone) {

        if (phone == null || phone.length() != 10) {
            return phone;
        }

        return phone.replaceAll(
                "(\\d{4})(\\d{3})(\\d{3})",
                "$1 $2 $3");
    }

}
