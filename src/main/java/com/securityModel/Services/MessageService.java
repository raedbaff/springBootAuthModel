package com.securityModel.Services;

import com.securityModel.models.Message;

import java.util.List;

public interface MessageService {
    public void sendMessage(String sender, String receiver, String content);
    public List<Message> getMessagesForUser(String user);
    public List<Message> getMessagesBetweenUsers(Long id,Long id2);
}
