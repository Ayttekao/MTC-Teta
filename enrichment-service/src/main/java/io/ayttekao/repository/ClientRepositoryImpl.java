package io.ayttekao.repository;

import io.ayttekao.model.Client;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
public class ClientRepositoryImpl implements ClientRepository {
    private final ConcurrentHashMap<String, Client> clients;

    @Override
    public Optional<Client> findByMsisdn(String msisdn) {
        return Optional.ofNullable(clients.get(msisdn));
    }

    @Override
    public List<Client> getAll() {
        return new ArrayList<>(clients.values());
    }

    @Override
    public void save(String msisdn, Client client) {
        if (clients.containsKey(msisdn)) {
            throw new IllegalArgumentException("Client with the same MSISDN already exists");
        }

        clients.put(msisdn, client);
    }

    @Override
    public void update(String msisdn, Client client) {
        if (!clients.containsKey(msisdn)) {
            throw new NoSuchElementException("Client not found");
        }

        clients.replace(msisdn, client);
    }

    @Override
    public void deleteByMsisdn(String msisdn) {
        if (!clients.containsKey(msisdn)) {
            throw new NoSuchElementException("Client not found");
        }

        clients.remove(msisdn);
    }
}
