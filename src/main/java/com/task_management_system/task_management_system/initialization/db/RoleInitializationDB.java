//package com.task_management_system.task_management_system.initialization.db;
//
//import com.task_management_system.task_management_system.model.ERole;
//import com.task_management_system.task_management_system.model.Role;
//import com.task_management_system.task_management_system.repository.RoleRepository;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.core.Ordered;
//import org.springframework.stereotype.Component;
//
//@Component
//public class RoleInitializationDB implements CommandLineRunner, Ordered {
//    private final RoleRepository roleRepository;
//    private static final Logger logger = LoggerFactory.getLogger(RoleInitializationDB.class);
//
//    public RoleInitializationDB(RoleRepository roleRepository) {
//        this.roleRepository = roleRepository;
//    }
//
//    @Override
//    public void run(String... args) {
//        for (ERole role : ERole.values()) {
//            if (!roleRepository.existsByName(role)) {
//                Role roleSave = roleRepository.save(new Role(role));
//                logger.info("Add new role in db: {}", roleSave.getName());
//            }
//        }
//    }
//
//    @Override
//    public int getOrder() {
//        return 1;
//    }
//}
