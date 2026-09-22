package vn.iotstar.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import vn.iotstar.entity.Role;
import vn.iotstar.entity.User;
import vn.iotstar.repository.RoleRepository;
import vn.iotstar.repository.UserRepository;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initData(RoleRepository roles, UserRepository users, PasswordEncoder encoder,
                                @Value("${ADMIN_EMAIL:admin@hcmute.edu.vn}") String adminEmail,
                                @Value("${ADMIN_PASSWORD:123456}") String adminPassword) {
        return args -> {
            Role userRole = roles.findByNameIgnoreCase("USER").orElseGet(() -> roles.save(new Role("USER")));
            Role adminRole = roles.findByNameIgnoreCase("ADMIN").orElseGet(() -> roles.save(new Role("ADMIN")));

            if (!users.existsByEmailIgnoreCase(adminEmail)) {
                User admin = new User();
                admin.setEmail(adminEmail.toLowerCase());
                admin.setFullName("System Administrator");
                admin.setPassword(encoder.encode(adminPassword));
                admin.setRole(adminRole);
                admin.setEnabled(true);
                users.save(admin);
            }

            if (!users.existsByEmailIgnoreCase("user01@gmail.com")) {
                User user = new User();
                user.setEmail("user01@gmail.com");
                user.setFullName("Nguyen Van A");
                user.setPassword(encoder.encode("123456"));
                user.setRole(userRole);
                user.setEnabled(true);
                users.save(user);
            }
        };
    }
}
