package io.usumu.api.smtp;

import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
public class MailStorage {
    private Map<String, Map<String, Message>> messages = new HashMap<>();

    public synchronized void deliver(Message message) {
        Map<String, Message> messageList = messages.getOrDefault(message.to, new HashMap<>());
        messageList.put(message.id, message);
        messages.put(message.to, messageList);
    }

    public synchronized void delete(String mailbox, String messageId) {
        Map<String, Message> messageList = messages.getOrDefault(mailbox, new HashMap<>());
        messageList.remove(messageId);
        messages.put(mailbox, messageList);
    }

    public synchronized void empty(String mailbox) {
        messages.put(mailbox, new HashMap<>());
    }

    public synchronized Collection<Message> read(String mailbox) {
        return messages.getOrDefault(mailbox, new HashMap<>()).values();
    }
}
