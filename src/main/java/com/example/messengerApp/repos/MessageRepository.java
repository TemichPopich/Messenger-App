package com.example.messengerApp.repos;

import com.example.messengerApp.entity.Message;
import org.springframework.data.repository.CrudRepository;

public interface MessageRepository extends CrudRepository<Message, Long> {
}
