package io.ayttekao.dao;

import io.ayttekao.model.Client;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class ClientDao implements Dao<Client> {
    private ConcurrentHashMap<Long, Client> clients = new ConcurrentHashMap<>();

    @Override
    public Optional<Client> getById(Long id) {
        return Optional.ofNullable(clients.get(id));
    }

    @Override
    public List<Client> getAll() {
        return new ArrayList<>(clients.values());
    }

    @Override
    public void save(Long id, Client client) {
        clients.put(id, client);
    }

    @Override
    public void update(Long id, Client client) {
        clients.replace(id, client);
    }

    @Override
    public void delete(Long id) {
        clients.remove(id);
    }

}
