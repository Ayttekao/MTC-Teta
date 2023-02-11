package io.ayttekao.dao;

import io.ayttekao.model.Client;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;

public class ClientDaoImpl implements ClientDao {
    private final ConcurrentHashMap<Long, Client> clients = new ConcurrentHashMap<>();

    @Override
    public Client findByMsisdn(Long msisdn) {
        var client = clients.get(msisdn);

        if (client == null) {
            throw new NoSuchElementException("Client not found");
        } else {
            return client;
        }
    }

    @Override
    public List<Client> getAll() {
        return new ArrayList<>(clients.values());
    }

    @Override
    public void save(Long msisdn, Client client) {
        clients.put(msisdn, client);
    }

    @Override
    public void update(Long msisdn, Client client) {
        var savedClient = clients.replace(msisdn, client);

        if (savedClient == null) {
            throw new NoSuchElementException("Client not found");
        }
    }

    @Override
    public void deleteByMsisdn(Long msisdn) {
        var removedClient = clients.remove(msisdn);

        if (removedClient == null) {
            throw new NoSuchElementException("Client not found");
        }
    }
}
