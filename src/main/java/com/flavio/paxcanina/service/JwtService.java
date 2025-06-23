package com.flavio.paxcanina.service;

import com.flavio.paxcanina.security.AppUserDetails;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
    String generateToken(AppUserDetails userDetails, String role);
    String getSubjectFromJwt(String jwt);
    String getRole(AppUserDetails userDetails);
    boolean isTokenValid(String jwt, UserDetails userDetails);
}
