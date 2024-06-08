package com.bookstore.services;

import com.bookstore.entity.User;
import com.bookstore.repository.IRoleRepository;
import com.bookstore.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServices {
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IRoleRepository roleRepository;

    public void save(User user) {
        userRepository.save(user);
        Long userId = userRepository.GetUserIdByUsername(user.getUsername());
        Long roleId = roleRepository.findRoleIdByName("user");
        if (roleId != 0 && userId != 0) {
            userRepository.addRoleToUser(userId, roleId);
        }
    }
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    public User GetUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }
    public void removeRolesFromUser(Long userId) {
        userRepository.removeRolesFromUser(userId);
    }

    public void addRoleToUser(Long userId, Long roleId) {
        userRepository.addRoleToUser(userId, roleId);
    }
}
