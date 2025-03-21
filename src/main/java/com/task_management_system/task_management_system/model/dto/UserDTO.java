package com.task_management_system.task_management_system.model.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.task_management_system.task_management_system.model.ERole;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class UserDTO {
    private Long id;
    private String email;
    private Set<String> roles = new HashSet<>();

    @JsonFormat(pattern = "dd.MM.yyyy - HH:mm")
    private LocalDateTime regAt;

    public boolean isAdmin() {
        return roles.stream().anyMatch(r -> r.equals(ERole.ROLE_ADMIN.name()));
    }
}
