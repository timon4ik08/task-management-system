package com.task_management_system.task_management_system.initialization.db;

import com.task_management_system.task_management_system.model.Role;
import com.task_management_system.task_management_system.model.User;
import com.task_management_system.task_management_system.repository.RoleRepository;
import com.task_management_system.task_management_system.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;

@Component
public class UserInitializationDB implements CommandLineRunner, Ordered {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final RoleRepository roleRepository;
    private static final Logger logger = LoggerFactory.getLogger(UserInitializationDB.class);

    public UserInitializationDB(UserRepository userRepository, PasswordEncoder encoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            User user = new User(
                    "admin@admin.admin",
                    encoder.encode("admin"));

            List<Role> roles = roleRepository.findAll();
            user.setRoles(new HashSet<>(roles));
            userRepository.save(user);
            logger.info("Created ADMIN user - {} and password - admin", user.getEmail());
            logger.info("Change the administrator password!!!");
        }
    }

    @Override
    public int getOrder() {
        return 2;
    }
}
