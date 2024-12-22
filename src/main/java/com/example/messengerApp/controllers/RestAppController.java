package com.example.messengerApp.controllers;

import com.example.messengerApp.entity.Role;
import com.example.messengerApp.entity.User;
import com.example.messengerApp.services.AppService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@AllArgsConstructor
public class RestAppController {
    private AppService service;

    @PostMapping("/new-user")
    public String addNewUser(@RequestBody User user) {
        user.setRoles(Collections.singleton(Role.USER));
        user.setActivationCode("22233443");
        service.addUser(user);
        return "user is saved";
    }
}
