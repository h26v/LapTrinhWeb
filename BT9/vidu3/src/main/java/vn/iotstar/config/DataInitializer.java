package vn.iotstar.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import vn.iotstar.entity.Role;
import vn.iotstar.entity.User;
import vn.iotstar.repository.RoleRepository;
import vn.iotstar.repository.UserRepository;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initData(RoleRepository roleRepository, UserRepository userRepository,
                                PasswordEncoder passwordEncoder,
                                @Value("${ADMIN_EMAIL:admin@hcmute.edu.vn}") String adminEmail,
                                @Value("${ADMIN_PASSWORD:123456}") String adminPassword) {
        return args -> {
            Role userRole = roleRepository.findByName("ROLE_USER")
                    .orElseGet(() -> roleRepository.save(Role.builder().name("ROLE_USER").build()));
            Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                    .orElseGet(() -> roleRepository.save(Role.builder().name("ROLE_ADMIN").build()));

            if (!userRepository.existsByUsername("admin")) {
                User admin = User.builder()
                        .username("admin")
                        .email(adminEmail)
                        .fullName("Administrator")
                        .password(passwordEncoder.encode(adminPassword))
                        .role(adminRole)
                        .enabled(true)
                        .build();
                userRepository.save(admin);
            }

            if (!userRepository.existsByUsername("user01")) {
                User user = User.builder()
                        .username("user01")
                        .email("user01@gmail.com")
                        .fullName("Nguyen Van A")
                        .password(passwordEncoder.encode("123456"))
                        .role(userRole)
                        .enabled(true)
                        .build();
                userRepository.save(user);
            }
        };
    }
}
