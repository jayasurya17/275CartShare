package com.cartshare.Message.dao;

import java.util.List;

import com.cartshare.models.Message;
import com.cartshare.repositories.MessageRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageDAO {
    @Autowired
    MessageRepository messageRepository;

    public Message save(Message m){
        return messageRepository.save(m);
    }

    public Message findById(Long id){
        return messageRepository.findById(id).orElse(null);
    }

    public void deleteMessage(Long id){
        messageRepository.deleteById(id);
    }

    public List<Message> findByFromAndTo(Long fromId, Long toId){
        return messageRepository.findByFromIdAndToIdOrderByDateDesc(fromId, toId);
    }
}