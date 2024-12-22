package com.example.messengerApp.services;

import com.example.messengerApp.entity.User;
import com.example.messengerApp.repos.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.UUID;

@Service
@AllArgsConstructor
public class AppService {
    private UserRepository userRepo;
    private PasswordEncoder encoder;
    private MailService mailService;

    public User addUser(User user) {
        var userFromDB = userRepo.findByUsername(user.getUsername()).
                orElseGet(() -> {
                    user.setPassword(encoder.encode(user.getPassword()));
                    user.setActivationCode(UUID.randomUUID().toString());
                    userRepo.save(user);
                    return null;
                });
        if (!StringUtils.isEmpty(user.getEmail())) {
            var message = String.format("Приветствую, %s\n" +
                    "Перейдите по следующей ссылке для подтверждения аккаунта:\n" +
                    "http://localhost:8080/activate/%s", user.getUsername(), user.getActivationCode());
            mailService.send(user.getEmail(),
                    "Activation code: ",
                    message);
        }
        return userFromDB;
    }

    public boolean activateUser(String code) {
        var user = userRepo.findByActivationCode(code);
        if (user == null) {
            return false;
        }

        user.setActivationCode(null);

        userRepo.save(user);

        return true;
    }
}
