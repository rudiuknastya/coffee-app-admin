package project.serviceImpl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import project.entity.Admin;
import project.repository.AdminRepository;
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final AdminRepository adminRepository;

    public UserDetailsServiceImpl(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username){
        Admin admin = adminRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not exists by email"));
        UserDetails userInDetails = org.springframework.security.core.userdetails.User.withUsername(admin.getEmail())
                .password(admin.getPassword())
                .roles(admin.getRole().toString())
                .build();
        return userInDetails;
    }
}
