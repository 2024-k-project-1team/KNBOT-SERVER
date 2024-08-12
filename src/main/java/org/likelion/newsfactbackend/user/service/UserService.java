package org.likelion.newsfactbackend.user.service;

import org.likelion.newsfactbackend.user.domain.User;
import org.springframework.http.ResponseEntity;

public interface UserService {
    User findByUser(String email);
}
