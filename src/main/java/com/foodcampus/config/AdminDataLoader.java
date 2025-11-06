package com.foodcampus.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.foodcampus.model.UserDtls;
import com.foodcampus.service.UserService;

@Component
public class AdminDataLoader implements CommandLineRunner {

    @Autowired
    private UserService userService;

    @Override
    public void run(String... args) {
        try {
            List<UserDtls> admins = userService.getUsers("ROLE_ADMIN");
            if (admins == null || admins.isEmpty()) {
                UserDtls admin = new UserDtls();
                admin.setName("Administrator");
                admin.setEmail("admin@local.test");
                // Do NOT pre-encode the password here. UserServiceImpl.saveAdmin encodes it.
                admin.setPassword("Admin@123");
                admin.setRole("ROLE_ADMIN");
                admin.setIsEnable(true);
                admin.setAccountNonLocked(true);
                admin.setProfileImage("default.jpg");

                userService.saveAdmin(admin);
                System.out.println("[AdminDataLoader] Default admin created: admin@local.test / Admin@123");
            } else {
                System.out.println("[AdminDataLoader] Admin user(s) already present, skipping creation.");
            }
        } catch (Exception e) {
            System.err.println("[AdminDataLoader] Error while creating default admin: " + e.getMessage());
        }
    }
}
