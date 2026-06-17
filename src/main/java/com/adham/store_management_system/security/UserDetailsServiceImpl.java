package com.adham.store_management_system.security;

import com.adham.store_management_system.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.debug("Loading user details for email {}", email);
        return userRepository.findByEmail(email)
                .orElseThrow( () -> {
                    log.warn("User not found for email {}", email);
                    return new UsernameNotFoundException("User not found with username" + email);
                });
    }
}
