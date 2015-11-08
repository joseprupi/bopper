package com.backofficer.message;

import java.util.List;

public interface MessageDAO {
    public List<Message> getAllMessages();
    public Message getMessage(int id);
    public void updateMessage();
    public void deleteMessage(Message student);
}
