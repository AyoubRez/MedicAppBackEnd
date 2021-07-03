package com.mdc.medic.apimedic.controllers;

import com.mdc.medic.apimedic.config.BearerTokenWrapper;
import com.mdc.medic.apimedic.models.User;
import com.mdc.medic.apimedic.repository.UserRepository;
import com.mdc.medic.apimedic.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
public class BaseController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    BearerTokenWrapper tokenWrapper;

    @Autowired
    JwtUtils jwtUtils;

    public User getUserFromRequest(){

        String username= jwtUtils.getUserNameFromJwtToken(tokenWrapper.getToken());

        Optional<User> user = userRepository.findByUsername(username);

        return user.get();
    }

}
