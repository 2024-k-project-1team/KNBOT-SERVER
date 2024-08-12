package org.likelion.newsfactbackend.user.dao;


import org.likelion.newsfactbackend.user.domain.User;

public interface UserDAO {
    User findUser(String email);
}
