package com.bookstore.services;

import com.bookstore.entity.User;
import com.bookstore.repository.IRoleRepository;
import com.bookstore.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class UserServices {
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IRoleRepository roleRepository;
    @Autowired
    private JavaMailSender mailSender;
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
    @Transactional
    public boolean processForgotPassword(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return false;
        }

        String token = UUID.randomUUID().toString();
        user.setResetPassToken(token);
        userRepository.save(user);

        sendResetPasswordEmail(email, token);

        return true;
    }
    private void sendResetPasswordEmail(String email, String token) {
        String resetUrl = "http://localhost:8080/reset-password?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Đặt lại mật khẩu");
        message.setText("Nhấn vào liên kết sau để đặt lại mật khẩu của bạn: " + resetUrl +" Nếu bạn không gửi yêu cầu lấy lại mật khẩu nào vui lòng bỏ qua điều này hoặc liên hệ chúng tôi để được hỗ trợ.");

        mailSender.send(message);
    }
    public boolean isValidToken(String token) {
        User user = userRepository.findByResetPassToken(token);
        return user != null;
    }

    public boolean updatePassword(String token, String password) {
        User user = userRepository.findByResetPassToken(token);
        if (user == null) {
            return false;
        }
        user.setPassword(new BCryptPasswordEncoder().encode(password));
        user.setResetPassToken(null);
        userRepository.save(user);
        return true;
    }
    public User FindUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
