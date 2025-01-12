package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;

import java.util.List;

public interface UserService extends UserDetailsService {
    UserDetails loadUserByUsername(String username);

    User findUserById(Long userId);

    List<User> showAllUsers();

    boolean deleteUser(Long userId);

    boolean saveUser(User user);

    Role getRoleById(long id);

    List<Role> getAllRoles();
}
