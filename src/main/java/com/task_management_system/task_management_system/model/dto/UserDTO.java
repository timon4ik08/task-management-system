package com.task_management_system.task_management_system.model.dto;


import com.task_management_system.task_management_system.model.ERole;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class UserDTO {
    private Long id;
    private String email;
    private Set<String> roles = new HashSet<>();

    public boolean isAdmin() {
        return roles.stream().anyMatch(r -> r.equals(ERole.ROLE_ADMIN.name()));
    }
}
