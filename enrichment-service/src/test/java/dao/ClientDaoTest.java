package dao;

import io.ayttekao.dao.ClientDao;
import io.ayttekao.dao.ClientDaoImpl;
import io.ayttekao.model.Client;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class ClientDaoTest {
    private static ClientDao clientDao;

    @BeforeAll
    static void init() {
        clientDao = new ClientDaoImpl();
    }

    @Test
    public void shouldFindClientWhenAdding() {
        var id = new Random().nextLong();
        var firstName = "Elliot";
        var lastName = "Alderson";
        var client = new Client(firstName, lastName);

        clientDao.save(id, client);

        var clientFromDao = clientDao.findByMsisdn(id);

        assertTrue(clientFromDao.isPresent());
        assertEquals(firstName, clientFromDao.get().getFirstName());
        assertEquals(lastName, clientFromDao.get().getLastName());
        clientDao.deleteByMsisdn(id);
    }

    @Test
    public void shouldGetAll() {
        var countClient = 5;
        var idArray = new Random().longs(countClient, Long.MIN_VALUE, Long.MAX_VALUE).toArray();
        var client = new Client("Elliot", "Alderson");

        Arrays.stream(idArray).forEach(id -> clientDao.save(id, client));

        var listClient = clientDao.getAll();

        assertNotNull(listClient);
        assertEquals(idArray.length, listClient.size());
        Arrays.stream(idArray).forEach(id -> clientDao.deleteByMsisdn(id));
    }

    @Test
    public void shouldChangeClientWhenUpdate() {
        var id = new Random().nextLong();
        var client = new Client("Elliot", "Alderson");
        var newFirstName = "Mr";
        var newLastName = "Robot";

        clientDao.save(id, client);
        clientDao.update(id, new Client(newFirstName, newLastName));

        var clientFromDao = clientDao.findByMsisdn(id);

        assertTrue(clientFromDao.isPresent());
        assertEquals(newFirstName, clientFromDao.get().getFirstName());
        assertEquals(newLastName, clientFromDao.get().getLastName());
        clientDao.deleteByMsisdn(id);
    }

    @Test
    public void shouldReturnEmptyClientWhenDeleted() {
        var id = new Random().nextLong();
        var client = new Client("Elliot", "Alderson");

        clientDao.save(id, client);
        clientDao.deleteByMsisdn(id);

        var clientFromDao = clientDao.findByMsisdn(id);

        assertTrue(clientFromDao.isEmpty());
    }

}

