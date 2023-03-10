package dao;

import io.ayttekao.dao.ClientDao;
import io.ayttekao.dao.ClientDaoImpl;
import io.ayttekao.model.Client;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Random;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;

public class ClientDaoTest {
    private static final Long testId = new Random().nextLong();
    private static final Client testClient = new Client("Elliot", "Alderson");
    private ClientDao clientDao;

    @BeforeEach
    void initEach() {
        clientDao = new ClientDaoImpl();
    }

    @Test
    public void shouldFindClientWhenAdding() {
        clientDao.save(testId, testClient);

        var clientFromDao = clientDao.findByMsisdn(testId);

        assertTrue(clientFromDao.isPresent());
        assertEquals(testClient.getFirstName(), clientFromDao.get().getFirstName());
        assertEquals(testClient.getLastName(), clientFromDao.get().getLastName());
    }

    @Test
    public void shouldGetAll() {
        var countClient = 5;
        var idArray = new Random().longs(countClient, Long.MIN_VALUE, Long.MAX_VALUE).toArray();
        Arrays.stream(idArray).forEach(id -> clientDao.save(id, testClient));

        var listClient = clientDao.getAll();

        assertThat(listClient, hasSize(idArray.length));
    }

    @Test
    public void shouldChangeClientWhenUpdate() {
        var newFirstName = "Mr";
        var newLastName = "Robot";

        clientDao.save(testId, testClient);
        clientDao.update(testId, new Client(newFirstName, newLastName));

        var clientFromDao = clientDao.findByMsisdn(testId);

        assertTrue(clientFromDao.isPresent());
        assertEquals(newFirstName, clientFromDao.get().getFirstName());
        assertEquals(newLastName, clientFromDao.get().getLastName());
    }

    @Test
    public void shouldReturnEmptyClientWhenDeleted() {
        clientDao.save(testId, testClient);
        clientDao.deleteByMsisdn(testId);

        var clientFromDao = clientDao.findByMsisdn(testId);

        assertTrue(clientFromDao.isEmpty());
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenDuplicateId() {
        clientDao.save(testId, testClient);
        assertThrows(IllegalArgumentException.class, () -> clientDao.save(testId, testClient));
    }

    @Test
    void shouldThrowNoSuchElementExceptionWhenNonexistentClientUpdate() {
        assertThrows(NoSuchElementException.class, () -> clientDao.update(testId, testClient));
    }

    @Test
    void shouldThrowNoSuchElementExceptionWhenNonexistentClientDelete() {
        assertThrows(NoSuchElementException.class, () -> clientDao.deleteByMsisdn(testId));
    }

}

