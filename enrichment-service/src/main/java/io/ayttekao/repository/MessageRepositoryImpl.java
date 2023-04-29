package io.ayttekao.repository;

import io.ayttekao.model.Message;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Predicate;

@RequiredArgsConstructor
public class MessageRepositoryImpl implements MessageRepository {
    private final ConcurrentLinkedQueue<Message> messages;

    public void add(Message message) {
        messages.add(message);
    }

    public Boolean remove(Message message) {
        return messages.remove(message);
    }

    public List<Message> query(Predicate<Message> filter) {
        List<Message> result = new ArrayList<>();
        for (Message message : messages) {
            if (filter.test(message)) {
                result.add(message);
            }
        }
        return result;
    }

    @Override
    public List<Message> getAll() {
        return new ArrayList<>(messages);
    }
}
