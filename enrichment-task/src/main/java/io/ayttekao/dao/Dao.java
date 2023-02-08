package io.ayttekao.dao;

import java.util.List;
import java.util.Optional;

public interface Dao<T> {

    Optional<T> getById(Long id);

    List<T> getAll();

    void save(Long id, T t);

    void update(Long id, T t);

    void delete(Long id);
}
