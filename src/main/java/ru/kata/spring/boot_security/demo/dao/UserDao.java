package ru.kata.spring.boot_security.demo.dao;

import ru.kata.spring.boot_security.demo.entity.User;

import java.util.List;

public interface UserDao {
    List<User> showAllUsers();
    void add(User user);
    void delete(User user);
    void edit(User user);
    User getById(long id);
}
