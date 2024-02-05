package ru.kata.spring.boot_security.demo.service;

import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;

public interface UserService {
    String getUserEmail(String username);
    List<User> getAllUsers();

    void createUser(User user);

    @Transactional
    void createRole(Role role);

    @Transactional
    Role getRoleByName(String roleName);

    User getUserById(Long id);

    void editUser(Long id, User user);

    void deleteUser(long id);

    User getUserByUsername(String username);
}