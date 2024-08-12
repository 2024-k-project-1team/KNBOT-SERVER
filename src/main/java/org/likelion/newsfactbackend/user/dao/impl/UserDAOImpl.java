package org.likelion.newsfactbackend.user.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.likelion.newsfactbackend.user.dao.UserDAO;
import org.likelion.newsfactbackend.user.domain.User;
import org.likelion.newsfactbackend.user.repository.UserRepository;
import org.springframework.stereotype.Component;
@Slf4j
@RequiredArgsConstructor
@Component
public class UserDAOImpl implements UserDAO {
    private final UserRepository userRepository;
    @Override
    public User findUser(String email) {
        User user = userRepository.findByEmail(email);
        return user;
    }
}
