package io.ayttekao.repository;

import io.ayttekao.model.Message;

import java.util.Iterator;

public interface MessageRepository {
    void save(Message message);

    Iterator<Message> getAll();
}
