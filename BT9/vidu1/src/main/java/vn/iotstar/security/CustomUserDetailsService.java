package vn.iotstar.security;

import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import vn.iotstar.entity.User;
import vn.iotstar.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository users;

    public CustomUserDetailsService(UserRepository users) {
        this.users = users;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User u = users.findByEmailWithRole(username)
                .orElseThrow(() -> new UsernameNotFoundException("Khong tim thay tai khoan."));

        return org.springframework.security.core.userdetails.User.withUsername(u.getEmail())
                .password(u.getPassword())
                .roles(u.getRole().getName())
                .disabled(!u.isEnabled())
                .build();
    }
}
