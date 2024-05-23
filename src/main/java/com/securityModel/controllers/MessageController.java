package com.securityModel.controllers;

import com.securityModel.Services.MessageService;
import com.securityModel.Services.imp.UserNotFoundException;
import com.securityModel.models.Message;
import com.securityModel.models.User;
import com.securityModel.repository.MessageRepository;
import com.securityModel.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/Messages")

public class MessageController {
    @Autowired
    private MessageService messageService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MessageRepository messageRepository;
    @PostMapping("/save/")
    public ResponseEntity<?> sendMessage(@RequestParam String sender, @RequestParam String receiver, @RequestParam String content) {
        messageService.sendMessage(sender, receiver, content);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/saveMessage")
    public Message send(@RequestParam Long sender, @RequestParam Long receiver, @RequestParam String content){
        User senderman=userRepository.findById(sender).get();
        User recieverman=userRepository.findById(receiver).get();
        Message message=new Message();
        message.setReceiver(recieverman);
        message.setSender(senderman);
        message.setContent(content);
        message.setTimestamp(LocalDateTime.now());
        return messageRepository.save(message);

    }
    @PostMapping("/addmsg/{sender}/{receiver}")
    public Message sendit(@PathVariable Long sender,@PathVariable Long receiver,@RequestParam String content){
        User senderman=userRepository.findById(sender).get();
        User recieverman=userRepository.findById(receiver).get();
        Message message=new Message();
        message.setReceiver(recieverman);
        message.setSender(senderman);
        message.setContent(content);
        message.setTimestamp(LocalDateTime.now());
        return messageRepository.save(message);

    }
    @GetMapping("/{userid}")
    public List<Map<String, Object>> getUserMessages(@PathVariable Long userid) {
        User user = userRepository.findById(userid)
                .orElseThrow(() -> new UserNotFoundException2(userid));
        List<Message> messages = messageRepository.findByReceiverOrSenderOrderByTimestampDesc(user, user);
        List<Map<String, Object>> messageList = new ArrayList<>();
        for (Message message : messages) {
            Map<String, Object> messageInfo = new HashMap<>();
            messageInfo.put("id", message.getId());
            messageInfo.put("sender", message.getSender());
            messageInfo.put("receiver", message.getReceiver());
            messageInfo.put("content", message.getContent());
            messageInfo.put("timestamp", message.getTimestamp());
            messageList.add(messageInfo);
        }
        return messageList;
    }
    @GetMapping("/messages/{id}/{id2}")
    public ResponseEntity<List<Message>> getMessages(@PathVariable Long id, @PathVariable Long id2) {
        List<Message> messages = messageService.getMessagesBetweenUsers(id,id2);

        return ResponseEntity.ok().body(messages);
    }

}
