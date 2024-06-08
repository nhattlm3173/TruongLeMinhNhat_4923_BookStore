package com.bookstore.controller;

import com.bookstore.entity.Role;
import com.bookstore.entity.User;
import com.bookstore.services.RoleServices;
import com.bookstore.services.UserServices;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {
    @Autowired
    private UserServices userServices;
    @Autowired
    private RoleServices roleServices;
    @GetMapping("/login")
    public String login() {
        return "user/login";
    }
    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new User());
        return "user/register";
    }
    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") User user, BindingResult bindingResult,Model model) {
        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                model.addAttribute(error.getField()+"_error", error.getDefaultMessage());
            }
            return "user/register";
        }
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userServices.save(user);
        return "redirect:/login";
    }
    @GetMapping("/users")
    public String users(Model model) {
        List<User> users = userServices.getAllUsers();
        model.addAttribute("users", users);
        return "user/listUsers";
    }
    @GetMapping("/users/edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model) {
        User user = userServices.GetUserById(id);
        if(user != null) {
            List<Role> roles = roleServices.getAllRoles();
            model.addAttribute("user", user);
            model.addAttribute("roles", roles);
            return "user/edit";
        }
        return "redirect:/users";
    }
    @PostMapping("/users/edit/{id}")
    public String edit(@PathVariable("id") Long id, @RequestParam("roleIds") List<Long> roleIds) {
        User existingUser = userServices.GetUserById(id);
        if (existingUser != null) {
            userServices.removeRolesFromUser(existingUser.getId());
            for (Long roleId : roleIds) {
                userServices.addRoleToUser(existingUser.getId(), roleId);
            }
        }
        return "redirect:/users";
    }
}
