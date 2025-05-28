package com.flavio.paxcanina.service;

import com.flavio.paxcanina.security.AppUserDetails;

public interface JwtService {
    String generateToken(AppUserDetails userDetails);
    String getSubjectFromJwt(String jwt);
    String getRole(AppUserDetails userDetails);
}
