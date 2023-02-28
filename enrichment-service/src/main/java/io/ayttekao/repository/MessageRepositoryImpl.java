package io.ayttekao.repository;

import io.ayttekao.model.Message;
import lombok.RequiredArgsConstructor;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

@RequiredArgsConstructor
public class MessageRepositoryImpl implements MessageRepository {
    private final CopyOnWriteArrayList<Message> copyOnWriteArrayList;

    @Override
    public void save(Message message) {
        copyOnWriteArrayList.add(message);
    }

    @Override
    public Iterator<Message> getAll() {
        return copyOnWriteArrayList.iterator();
    }
}
