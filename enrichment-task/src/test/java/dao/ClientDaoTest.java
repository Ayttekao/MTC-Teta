package dao;

import io.ayttekao.dao.ClientDao;
import io.ayttekao.dao.Dao;
import io.ayttekao.model.Client;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Random;

public class ClientDaoTest {
    private static Dao<Client> clientDao;

    @BeforeAll
    static void init() {
        clientDao = new ClientDao();
    }

    @Test
    @DisplayName("get method test - positive")
    public void testGetById() {
        var id = new Random().nextLong();
        var firstName = "Elliot";
        var lastName = "Alderson";
        var client = new Client(firstName, lastName);

        clientDao.save(id, client);

        var clientFromDao = clientDao.getById(id).get();

        Assertions.assertEquals(firstName, clientFromDao.getFirstName());
        Assertions.assertEquals(lastName, clientFromDao.getLastName());
        clientDao.delete(id);
    }

    @Test
    @DisplayName("getAll method test - positive")
    public void testGetAll() {
        var countClient = 5;
        var idArray = new Random().longs(countClient, Long.MIN_VALUE, Long.MAX_VALUE).toArray();
        var client = new Client("Elliot", "Alderson");

        Arrays.stream(idArray).forEach(id -> clientDao.save(id, client));

        var listClient = clientDao.getAll();

        Assertions.assertNotNull(listClient);
        Assertions.assertEquals(idArray.length, listClient.size());
        Arrays.stream(idArray).forEach(id -> clientDao.delete(id));
    }

    @Test
    @DisplayName("update method test - positive")
    public void testUpdate() {
        var id = new Random().nextLong();
        var client = new Client("Elliot", "Alderson");
        var newFirstName = "Mr";
        var newLastName = "Robot";

        clientDao.save(id, client);
        clientDao.update(id, new Client(newFirstName, newLastName));

        var clientFromDao = clientDao.getById(id).get();

        Assertions.assertEquals(newFirstName, clientFromDao.getFirstName());
        Assertions.assertEquals(newLastName, clientFromDao.getLastName());
        clientDao.delete(id);
    }

    @Test
    @DisplayName("delete method test - positive")
    public void testDelete() {
        var id = new Random().nextLong();
        var client = new Client("Elliot", "Alderson");

        clientDao.save(id, client);
        clientDao.delete(id);

        Assertions.assertFalse(clientDao.getById(id).isPresent());
    }

}
