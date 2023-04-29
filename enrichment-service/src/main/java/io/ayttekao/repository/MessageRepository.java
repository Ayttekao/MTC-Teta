package io.ayttekao.repository;

import io.ayttekao.model.Message;

import java.util.List;
import java.util.function.Predicate;

public interface MessageRepository {
    void add(Message message);

    Boolean remove(Message message);

    List<Message> query(Predicate<Message> filter);

    List<Message> getAll();
}
