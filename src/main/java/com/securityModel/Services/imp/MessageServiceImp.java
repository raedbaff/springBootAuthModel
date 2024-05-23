package com.securityModel.Services.imp;

import com.securityModel.Services.MessageService;
import com.securityModel.models.Message;
import com.securityModel.models.User;
import com.securityModel.repository.MessageRepository;
import com.securityModel.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
@Service

public class MessageServiceImp implements MessageService {
    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;
    @Override
    public void sendMessage(String sender, String receiver, String content) {
        User senderr = userRepository.findByUsername(sender).orElseThrow(() -> new UserNotFoundException(sender));
        User receiverr = userRepository.findByUsername(receiver).orElseThrow(() -> new UserNotFoundException(receiver));
        Message message = new Message();
        message.setSender(senderr);
        message.setReceiver(receiverr);
        message.setContent(content);
        message.setTimestamp(LocalDateTime.now());
        messageRepository.save(message);


    }

    @Override
    public List<Message> getMessagesForUser(String user) {

        User userr = userRepository.findByUsername(user).orElseThrow(() -> new UserNotFoundException(user));
        List<Message> messagesReceived = userr.getMessagesReceived();
        List<Message> messagesSent = userr.getMessagesSent();
        List<Message> allMessages = new ArrayList<>(messagesReceived);
        allMessages.addAll(messagesSent);
        allMessages.sort(Comparator.comparing(Message::getTimestamp));
        return allMessages;
    }

    @Override
    public List<Message> getMessagesBetweenUsers(Long id, Long id2) {
        return messageRepository.findBySender_IdAndReceiver_IdOrSender_IdAndReceiver_IdOrderByTimestampAsc(id,id2,id2,id);
    }
}

