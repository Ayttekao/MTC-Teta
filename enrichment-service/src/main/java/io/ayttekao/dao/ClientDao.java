package io.ayttekao.dao;

import io.ayttekao.model.Client;

import java.util.List;
import java.util.Optional;

public interface ClientDao {
    Optional<Client> findByMsisdn(String msisdn);

    List<Client> getAll();

    void save(String msisdn, Client client);

    void update(String msisdn, Client client);

    void deleteByMsisdn(String msisdn);
}
