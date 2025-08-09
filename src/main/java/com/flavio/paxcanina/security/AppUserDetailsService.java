package com.flavio.paxcanina.security;

import com.flavio.paxcanina.dao.UserDao;
import com.flavio.paxcanina.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * AppUserDetailsService
 * ---------------------
 * Loads the domain user from the database and adapts it to Spring Security.
 * We also guard against disabled/anonymized users for clearer error semantics.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {

    private final UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userDao.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        // Early guard for business state.
        if (!user.isActive() || user.getAnonymizedAt() != null) {
            log.warn("Blocked login for disabled/anonymized user: {}", email);
            throw new UsernameNotFoundException("User disabled or anonymized: " + email);
        }

        return new AppUserDetails(user);

    }
}
