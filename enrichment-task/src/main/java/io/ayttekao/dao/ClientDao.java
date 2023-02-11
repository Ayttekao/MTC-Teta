package io.ayttekao.dao;

import io.ayttekao.model.Client;

import java.util.List;

public interface ClientDao {
    Client findByMsisdn(Long msisdn);

    List<Client> getAll();

    void save(Long msisdn, Client client);

    void update(Long msisdn, Client client);

    void deleteByMsisdn(Long msisdn);
}
