package io.usumu.api.mail;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MailStorage {
    private Map<String, Map<String, Message>> messages = new HashMap<>();

    public synchronized void deliver(Message message) {
        Map<String, Message> messageList = messages.getOrDefault(message.to, new LinkedHashMap<>());
        messageList.put(message.id, message);
        messages.put(message.to, messageList);
    }

    public synchronized void delete(String mailbox, String messageId) {
        Map<String, Message> messageList = messages.getOrDefault(mailbox, new LinkedHashMap<>());
        messageList.remove(messageId);
        messages.put(mailbox, messageList);
    }

    public synchronized void empty(String mailbox) {
        messages.put(mailbox, new TreeMap<>());
    }

    public synchronized List<Message> read(String mailbox) {
        return new ArrayList<>(messages.getOrDefault(mailbox, new TreeMap<>())
                                   .values());
    }
}
