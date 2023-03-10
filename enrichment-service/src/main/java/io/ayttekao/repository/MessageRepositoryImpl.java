package io.ayttekao.repository;

import io.ayttekao.model.Message;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class MessageRepositoryImpl implements MessageRepository {
    private final List<Message> messages;

    @Override
    public synchronized void save(Message message) {
        messages.add(message);
    }

    @Override
    public synchronized List<Message> getAll() {
        return new ArrayList<>(messages);
    }
}
