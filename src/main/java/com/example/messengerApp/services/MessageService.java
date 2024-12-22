package com.example.messengerApp.services;

import com.example.messengerApp.entity.Message;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MessageService {
    private List<Message> messages = new ArrayList<>();

    public void addProduct(Message m) {
        messages.add(m);
    }

    public List<Message> findAll() {
        return messages;
    }
}
