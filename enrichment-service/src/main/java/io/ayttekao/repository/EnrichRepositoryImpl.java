package io.ayttekao.repository;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Predicate;

@RequiredArgsConstructor
public class EnrichRepositoryImpl<T> implements EnrichRepository<T> {
    private final ConcurrentLinkedQueue<T> messages;

    public void add(T message) {
        messages.add(message);
    }

    public void remove(T message) {
        messages.remove(message);
    }

    public List<T> query(Predicate<T> filter) {
        List<T> result = new ArrayList<>();
        for (var message : messages) {
            if (filter.test(message)) {
                result.add(message);
            }
        }
        return result;
    }

    @Override
    public List<T> getAll() {
        return new ArrayList<>(messages);
    }
}
