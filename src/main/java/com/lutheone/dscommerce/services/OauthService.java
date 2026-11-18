package com.lutheone.dscommerce.services;

import com.lutheone.dscommerce.entities.User;
import com.lutheone.dscommerce.services.exceptions.ForbiddenExeception;
import org.springframework.stereotype.Service;

@Service
public class OauthService {

    private final UserService userService;

    public OauthService(UserService userService) {
        this.userService = userService;
    }

    public void validateSelfOrAdmin(Long userId) {
        User me = userService.authenticated();
       if (!me.hasRole("ROLE_ADMIN") && !me.getId().equals(userId)) {
            throw new ForbiddenExeception("Access denied.");
       }
    }

}
