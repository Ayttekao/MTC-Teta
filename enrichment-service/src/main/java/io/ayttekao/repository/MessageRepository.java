package io.ayttekao.repository;

import io.ayttekao.model.Message;

import java.util.List;

public interface MessageRepository {
    void save(Message message);

    List<Message> getAll();
}
