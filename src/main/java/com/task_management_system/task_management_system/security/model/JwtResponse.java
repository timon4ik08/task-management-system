package com.task_management_system.task_management_system.security.model;

import com.task_management_system.task_management_system.model.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String email;
    private Set<Role> roles;

    public JwtResponse(String accessToken, Long id, String email, Set<Role> roles) {
        this.token = accessToken;
        this.id = id;
        this.email = email;
        this.roles = roles;
    }
}
