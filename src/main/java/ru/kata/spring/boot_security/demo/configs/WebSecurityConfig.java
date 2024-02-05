package ru.kata.spring.boot_security.demo.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;
/**
 * Этот класс WebSecurityConfig является конфигурационным классом для настройки
 * безопасности веб-приложения с использованием Spring Security. Он расширяет класс
 * WebSecurityConfigurerAdapter и использует аннотацию @EnableWebSecurity для включения
 * поддержки безопасности веб-приложения.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final SuccessUserHandler successUserHandler;
    private final UserServiceImpl userService;

    public WebSecurityConfig(SuccessUserHandler successUserHandler, UserServiceImpl userService) {
        this.successUserHandler = successUserHandler;
        this.userService = userService;
    }

/**
 * Метод configure определяет правила доступа к различным URL в приложении,
 * требующим определенных ролей для доступа. Например, определены правила доступа для URL,
 * таких как "/admin", "/create", "/edit/", "/delete/" и других.
 */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().and()
                .authorizeRequests()
                .antMatchers("/", "/index").permitAll()
                .antMatchers("/admin", "/create", "/edit", "/delete").hasRole("ADMIN")
                .and()
                .formLogin().successHandler(successUserHandler)
                .and()
                .logout().logoutUrl("/logout")
                .logoutSuccessUrl("/login")
                .permitAll();

    }
/**
 * Метод configureGlobal настраивает AuthenticationManagerBuilder для использования
 * userService в качестве сервиса пользовательских данных и NoOpPasswordEncoder в
 * качестве кодировщика паролей.
 */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(NoOpPasswordEncoder.getInstance());
    }


    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("*"); // Разрешаем запросы со всех доменов
        configuration.addAllowedMethod("*"); // Разрешаем все HTTP методы
        configuration.addAllowedHeader("*"); // Разрешаем все заголовки
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}



