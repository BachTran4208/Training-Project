package project.training.com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Role {
    SCRUM_MASTER,
    PROJECT_OWNER,
    MEMBER,
    OTHER;

    @JsonCreator
    public static Role fromString(String value) {
        if (value == null) {
            return OTHER;
        }

        for (Role role : Role.values()) {
            if (role.name().equalsIgnoreCase(value)) {
                return role;
            }
        }

        return OTHER;
    }
}
