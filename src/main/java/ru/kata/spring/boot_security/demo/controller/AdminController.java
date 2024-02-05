package ru.kata.spring.boot_security.demo.controller;

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
import java.security.Principal;
import java.util.List;

@Controller
public class AdminController {
    private final UserService userService;


    public AdminController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/admin")
    public String adminPage(Model model, Model model2, Principal principal) {

        String name = principal.getName();
        String email = userService.getUserEmail(name);
        User user = userService.getUserByUsername(name);
        Role role = userService.getRoleByName("ROLE_ADMIN");
        model2.addAttribute("currentUser", user);
        model.addAttribute("users", userService.getAllUsers());
        return "admin";
    }


    @PostMapping("/create")
    public String create(@ModelAttribute("user") @Valid User user,
                         BindingResult bindingResult,
                         @RequestParam("selectedRoles") List<String> selectedRoles) {
        if (bindingResult.hasErrors()) {
            System.out.println("Incorrect create input");
            return "redirect:/admin";
        }

        for (String roleName : selectedRoles) {
            user.addRole(userService.getRoleByName(roleName));
        }
        userService.createUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/create")
    public String newPage(Model model) {
        model.addAttribute("user", new User());
        return "redirect:/admin";
    }

    @GetMapping("/edit")
    public String editPage(@RequestParam(value = "id") long id, Model model) {
        model.addAttribute("user", userService.getUserById(id));
        for (Role role : userService.getUserById(id).getRoles()) {
            if ("ROLE_USER".equals(role.getName())) {
                model.addAttribute("userRole", true);
            }
            if ("ROLE_ADMIN".equals(role.getName())) {
                model.addAttribute("adminRole", true);
            }
        }
        return "redirect:/admin";
    }

    @PostMapping("/edit")
    public String update(@ModelAttribute("user") @Valid User user,
                         BindingResult bindingResult,
                         @RequestParam(value = "id") long id,
                         @RequestParam(name = "ROLE_USER", defaultValue = "false") boolean userRole,
                         @RequestParam(name = "ROLE_ADMIN", defaultValue = "false") boolean adminRole) {
        if (bindingResult.hasErrors()) {
            return "redirect:/admin";
        }

        if (userRole) {
            user.addRole(userService.getRoleByName("ROLE_USER"));
        }
        if (adminRole) {
            user.addRole(userService.getRoleByName("ROLE_ADMIN"));
        }
        userService.editUser(id, user);
        return "redirect:/admin";
    }

    @GetMapping("/delete")
    public String deleteUser(@RequestParam(value = "id") long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}
