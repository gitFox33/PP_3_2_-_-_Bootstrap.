package ru.kata.spring.boot_security.demo.configs;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@Component
public class SuccessUserHandler implements AuthenticationSuccessHandler {
    // Spring Security использует объект Authentication, пользователя авторизованной сессии.
    private final UserServiceImpl userService;

    public SuccessUserHandler(UserServiceImpl userService) {
        this.userService = userService;
    }
    /**
     * Метод onAuthenticationSuccess выполняет перенаправление пользователя на разные URL
     * в зависимости от их роли после успешной аутентификации.
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException {
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        if (roles.contains("ROLE_ADMIN")) {
            httpServletResponse.sendRedirect("/admin");
        } else if (roles.contains("ROLE_USER")) {
            httpServletResponse.sendRedirect("/user?id=" + userId(authentication));
        } else {
            httpServletResponse.sendRedirect("/");
        }

    }

    /**
     * Метод userId извлекает идентификатор пользователя из объекта Authentication с использованием сервиса userService.
     */
    public String userId(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return String.valueOf(userService.getUserByUsername(userDetails.getUsername()).getId());
    }
}