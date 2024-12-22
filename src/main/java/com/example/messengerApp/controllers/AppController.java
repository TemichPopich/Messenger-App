package com.example.messengerApp.controllers;

import com.example.messengerApp.config.UserDetailsConfig;
import com.example.messengerApp.entity.Message;
import com.example.messengerApp.entity.Role;
import com.example.messengerApp.entity.User;
import com.example.messengerApp.repos.MessageRepository;
import com.example.messengerApp.services.AppService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Collections;

@Controller
@AllArgsConstructor
public class AppController {
    private MessageRepository messageRepo;
    private AppService service;

    @GetMapping("/")
    public String viewHome(Model model) {
        return "home.html";
    }

    @GetMapping("/main")
    public String viewProducts(Model model, @AuthenticationPrincipal UserDetailsConfig user) {
        findMessage(model, user);
        model.addAttribute("currentUser", user.getUser());
        return "main.html";
    }

    private void findMessage(Model model, @AuthenticationPrincipal UserDetailsConfig user) {
        var messages = messageRepo.findAll();
        if (user.getUser().getRoles().contains(Role.ADMIN)) {
            model.addAttribute("messages", messages);
            return;
        }
        var resMessages = new ArrayList<Message>();
        for (var m : messages) {
            if (!m.getRecipient().equals(user.getUsername())) continue;
            resMessages.add(m);
        }
        model.addAttribute("messages", resMessages);
    }

    @PostMapping("/main")
    public String addMessage(
            @AuthenticationPrincipal UserDetailsConfig user,
            @RequestParam String text,
            @RequestParam String recipient,
            Model model
    ) {
        Message m = new Message(text, user.getUser(), recipient);
        messageRepo.save(m);
        findMessage(model, user);
        return "main.html";
    }

    @GetMapping("/reg")
    public String registration() {
        return "reg.html";
    }

    @PostMapping("/reg")
    public String registration(User user, Model model) {
        user.setRoles(Collections.singleton(Role.USER));
        var userResult = service.addUser(user);
        if (userResult != null) {
            model.addAttribute("message", "This user are already exist");
            return "reg.html";
        }
        model.addAttribute("message", "Вам на почту отправлен код для активации аккаунта");
        return "reg.html";
    }

    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable String code) {
        boolean isActive = service.activateUser(code);

        if (isActive) {
            model.addAttribute("message", "Пользователь успешно активирован");
        } else {
            model.addAttribute("message", "Код активации не найден");
        }

        return "login.html";
    }

    @GetMapping("/account")
    public String account(Model model, @AuthenticationPrincipal UserDetailsConfig user) {
        var name = user.getUsername();
        model.addAttribute("name", name);
        return "account.html";
    }
}
