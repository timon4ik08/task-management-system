package com.task_management_system.task_management_system.service.impl;

import com.task_management_system.task_management_system.model.ERole;
import com.task_management_system.task_management_system.model.Role;
import com.task_management_system.task_management_system.model.User;
import com.task_management_system.task_management_system.security.exception.AuthorizationException;
import com.task_management_system.task_management_system.security.exception.RegistrationException;
import com.task_management_system.task_management_system.security.model.JwtResponse;
import com.task_management_system.task_management_system.security.model.LoginRequest;
import com.task_management_system.task_management_system.security.model.MessageResponse;
import com.task_management_system.task_management_system.security.model.SignupRequest;
import com.task_management_system.task_management_system.repository.RoleRepository;
import com.task_management_system.task_management_system.repository.UserRepository;
import com.task_management_system.task_management_system.security.JwtUtils;
import com.task_management_system.task_management_system.security.service.UserDetailsImpl;
import com.task_management_system.task_management_system.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;

    @Autowired
    public AuthServiceImpl(AuthenticationManager authenticationManager, UserRepository userRepository,
                           RoleRepository roleRepository, PasswordEncoder encoder, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
    }


    @Override
    public JwtResponse authUser(LoginRequest loginRequest) {
        String jwt = "";
        UserDetailsImpl userDetails = null;
        List<String> roles = new ArrayList<>();

        if(!userRepository.existsByEmail(loginRequest.getEmail())){
            throw new AuthorizationException("Email not found.");
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            jwt = jwtUtils.generateJwtToken(authentication);
            userDetails = (UserDetailsImpl) authentication.getPrincipal();
            roles = userDetails.getAuthorities().stream()
                    .map(item -> item.getAuthority())
                    .collect(Collectors.toList());
        }catch (Exception e){
            throw new AuthorizationException("Password is incorrect!");
        }

        return new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(), //email
                roles);
    }

    @Override
    public MessageResponse registerUser(SignupRequest signUpRequest) {

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new RegistrationException("Email is already in use!");
        }

        User user = new User(
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Role defaultRole = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));

        user.setRoles(Set.of(defaultRole));
        userRepository.save(user);

        return new MessageResponse("User: " + user.getEmail() + " - registered successfully!");
    }
}
