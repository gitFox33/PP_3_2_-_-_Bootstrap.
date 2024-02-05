package ru.kata.spring.boot_security.demo.dao;

import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;

public interface UserDao {
    List<User> getAllUsers();

    void createUser(User user);

    void createRole(Role role);

    Role getRoleByName(String roleName);

    User getUserById(Long id);

    User getUserByUsername(String username);

    void editUser(Long id, User user);

    void deleteUser(long id);

}