package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
public class AdminController {
    private final String ADMIN_PAGE = "redirect:/admin";
    private final UserService userService;


    public AdminController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/admin")
    public String adminPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails ud = (UserDetails) authentication.getPrincipal();

        List<Role> allRoles = new ArrayList<>();
        allRoles.add(new Role("ROLE_ADMIN"));
        allRoles.add(new Role("ROLE_USER"));



        model.addAttribute("userModel", new User());
        model.addAttribute("allRoles", allRoles);
        model.addAttribute("currentUser", userService.getUserByUsername(ud.getUsername()));
        model.addAttribute("users", userService.getAllUsers());
        return "admin";
    }


    @PostMapping("/create")
    public String create(@ModelAttribute("user") @Valid User user,
                         BindingResult bindingResult,
                         @RequestParam("selectedRoles") List<String> selectedRoles) {
        if (bindingResult.hasErrors()) {
            System.out.println("Incorrect create input");
            return ADMIN_PAGE;
        }

        for (String roleName : selectedRoles) {
            user.addRole(userService.getRoleByName(roleName));
        }
        userService.createUser(user);
        return ADMIN_PAGE;
    }

    @GetMapping("/create")
    public String newPage(Model model) {
        model.addAttribute("user", new User());
        return ADMIN_PAGE;
    }


    @PostMapping("/edit")
    public String edit(@RequestParam("id") long id,
                       @RequestParam("edit_name") @Valid String name,
                       @RequestParam("edit_email") String lastName,
                       @RequestParam("edit_age") Integer age,
                       @RequestParam("edit_username") String username,
                       @RequestParam("edit_password") String password,
                       @RequestParam("selectedRoles") List<String> selectedRoles) {
        User user = new User(name, lastName, age, username, password);

        for (String roleName : selectedRoles) {
            user.addRole(userService.getRoleByName(roleName));
        }
        userService.editUser(id, user);
        return ADMIN_PAGE;
    }


    @PostMapping("/delete")
    public String deleteUser(@RequestParam(value = "id") long id) {
        userService.deleteUser(id);
        return ADMIN_PAGE;
    }
}
