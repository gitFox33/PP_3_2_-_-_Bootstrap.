package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }


    /**
     *   Метод loadUserByUsername является частью Spring Security и используется для
     *   загрузки информации о пользователе по его имени пользователя. Он получает пользователя
     *   из базы данных по его имени пользователя, проверяет его существование, и затем
     *   создает объект UserDetails для этого пользователя, учитывая различные параметры,
     *   такие как активность учетной записи и сроки действия учетных данных.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.getUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("No user found with username: " + username);
        }
        boolean enabled = true;
        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(), enabled, accountNonExpired,
                credentialsNonExpired, accountNonLocked, getAuthorities(user.getRoles()));
    }

    /**
     *   Метод getAuthorities принимает набор ролей пользователя и преобразует их в
     *   набор разрешений (GrantedAuthority). Он проходит по каждой роли пользователя и
     *   создает объект SimpleGrantedAuthority для каждой роли, используя имя роли в
     *   качестве имени разрешения. Метод возвращает набор разрешений.
     */

    private static Set<GrantedAuthority> getAuthorities (Set<Role> roles) {
        Set<GrantedAuthority> authorities = new HashSet<>();
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        return authorities;
    }
    @Override
    public String getUserEmail(String username) {
        User user = userDao.getUserByUsername(username);
        return user.getEmail();
    }

    @Override
    public List<User> getAllUsers(){return userDao.getAllUsers();}
    @Override
    @Transactional
    public void createUser(User user) {userDao.createUser(user);}

    @Override
    @Transactional
    public void createRole(Role role) {
        userDao.createRole(role);
    }
    @Override
    @Transactional
    public Role getRoleByName(String roleName) {
        return userDao.getRoleByName(roleName);
    }
    @Override
    public User getUserById(Long id) {return userDao.getUserById(id);}
    @Override
    @Transactional
    public void editUser(Long id, User user) {userDao.editUser(id, user);}
    @Override
    @Transactional
    public void deleteUser(long id) {userDao.deleteUser(id);}

    @Override
    public User getUserByUsername(String username) {
        return userDao.getUserByUsername(username);
    }
}