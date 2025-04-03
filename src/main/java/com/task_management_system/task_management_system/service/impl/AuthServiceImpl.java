package com.task_management_system.task_management_system.service.impl;

import com.task_management_system.task_management_system.model.ERole;
import com.task_management_system.task_management_system.model.Role;
import com.task_management_system.task_management_system.model.User;
import com.task_management_system.task_management_system.exception.AuthorizationException;
import com.task_management_system.task_management_system.exception.RegistrationException;
import com.task_management_system.task_management_system.security.model.JwtResponse;
import com.task_management_system.task_management_system.security.model.LoginRequest;
import com.task_management_system.task_management_system.security.model.MessageResponse;
import com.task_management_system.task_management_system.security.model.SignupRequest;
import com.task_management_system.task_management_system.repository.RoleRepository;
import com.task_management_system.task_management_system.repository.UserRepository;
import com.task_management_system.task_management_system.security.JwtUtils;
import com.task_management_system.task_management_system.security.service.UserDetailsImpl;
import com.task_management_system.task_management_system.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;

    public AuthServiceImpl(AuthenticationManager authenticationManager, UserRepository userRepository,
                           RoleRepository roleRepository, PasswordEncoder encoder, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
        log.info("AuthServiceImpl initialized with dependencies");
    }

    @Override
    public JwtResponse authUser(LoginRequest loginRequest) {
        log.info("Attempting to authenticate user with email: {}", loginRequest.getEmail());
        String jwt = "";
        UserDetailsImpl userDetails = null;
        Set<Role> roles = new HashSet<>();

        if (!userRepository.existsByEmail(loginRequest.getEmail())) {
            log.error("Email not found: {}", loginRequest.getEmail());
            throw new AuthorizationException("Email not found.");
        }

        try {
            log.debug("Authenticating user with email: {}", loginRequest.getEmail());
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.debug("User authenticated successfully: {}", loginRequest.getEmail());

            jwt = jwtUtils.generateJwtToken(authentication);
            log.debug("JWT token generated for user: {}", loginRequest.getEmail());

            userDetails = (UserDetailsImpl) authentication.getPrincipal();
            log.debug("User details retrieved: {}", userDetails.getUsername());

            List<String> roleList = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();
            log.debug("User roles retrieved: {}", roleList);

            roles = roleList.stream()
                    .map(roleName -> {
                        log.debug("Looking up role: {}", roleName);
                        return roleRepository.findByName(ERole.valueOf(roleName))
                                .orElseThrow(() -> {
                                    log.error("Role not found: {}", roleName);
                                    return new RuntimeException("Role not found: " + roleName);
                                });
                    })
                    .collect(Collectors.toSet());
            log.debug("Roles mapped successfully: {}", roles);

        } catch (Exception e) {
            log.error("Authentication failed for user: {}", loginRequest.getEmail(), e);
            throw new AuthorizationException("Password is incorrect!");
        }

        log.info("User authenticated successfully: {}", loginRequest.getEmail());
        return new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(), //email
                roles);
    }

    @Override
    public MessageResponse registerUser(SignupRequest signUpRequest) {
        log.info("Attempting to register user with email: {}", signUpRequest.getEmail());

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            log.error("Email already in use: {}", signUpRequest.getEmail());
            throw new RegistrationException("Email is already in use!");
        }

        User user = new User(
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));
        log.debug("User object created for email: {}", signUpRequest.getEmail());

        Role defaultRole = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> {
                    log.error("Default role ROLE_USER not found");
                    return new RuntimeException("Error: Role is not found.");
                });
        log.debug("Default role ROLE_USER assigned to user: {}", signUpRequest.getEmail());

        user.setRoles(Set.of(defaultRole));
        userRepository.save(user);
        log.info("User registered successfully: {}", signUpRequest.getEmail());

        return new MessageResponse("User: " + user.getEmail() + " - registered successfully!");
    }
}