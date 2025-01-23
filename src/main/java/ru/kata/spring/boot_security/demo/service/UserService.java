package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.entity.User;

import java.util.List;

public interface UserService {

    User findUserById(Long userId);

    List<User> showAllUsers();

    boolean deleteUser(Long userId);

    void saveUser(User user, String roleSelect);

    void updateUser(User user, List<Long> roleIds, String password);

}
