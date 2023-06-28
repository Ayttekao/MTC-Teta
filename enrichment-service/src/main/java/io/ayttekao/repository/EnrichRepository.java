package io.ayttekao.repository;

import java.util.List;
import java.util.function.Predicate;

public interface EnrichRepository<T> {
    void add(T message);

    void remove(T message);

    List<T> query(Predicate<T> filter);

    List<T> getAll();
}
