package dao;

import io.ayttekao.dao.ClientDao;
import io.ayttekao.dao.ClientDaoImpl;
import io.ayttekao.model.Client;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;

public class ClientDaoTest {
    private static final String testMsisdn = randomDigitString(11);
    private static final Client testClient = new Client("Elliot", "Alderson");
    private ClientDao clientDao;

    @BeforeEach
    void initEach() {
        clientDao = new ClientDaoImpl();
    }

    @Test
    public void shouldFindClientWhenAdding() {
        clientDao.save(testMsisdn, testClient);

        var clientFromDao = clientDao.findByMsisdn(testMsisdn);

        assertTrue(clientFromDao.isPresent());
        assertEquals(testClient.getFirstName(), clientFromDao.get().getFirstName());
        assertEquals(testClient.getLastName(), clientFromDao.get().getLastName());
    }

    @Test
    public void shouldGetAll() {
        var countClient = 20;
        var random = new Random();
        var list = random.ints(countClient, 0, 10)
                .mapToObj(i -> random.ints(11, 0, 10)
                        .mapToObj(Integer::toString)
                        .collect(Collectors.joining()))
                .collect(Collectors.toCollection(ArrayList::new));

        list.forEach(id -> clientDao.save(id, testClient));

        var listClient = clientDao.getAll();

        assertThat(listClient, hasSize(list.size()));
    }

    @Test
    public void shouldChangeClientWhenUpdate() {
        var newFirstName = "Mr";
        var newLastName = "Robot";

        clientDao.save(testMsisdn, testClient);
        clientDao.update(testMsisdn, new Client(newFirstName, newLastName));

        var clientFromDao = clientDao.findByMsisdn(testMsisdn);

        assertTrue(clientFromDao.isPresent());
        assertEquals(newFirstName, clientFromDao.get().getFirstName());
        assertEquals(newLastName, clientFromDao.get().getLastName());
    }

    @Test
    public void shouldReturnEmptyClientWhenDeleted() {
        clientDao.save(testMsisdn, testClient);
        clientDao.deleteByMsisdn(testMsisdn);

        var clientFromDao = clientDao.findByMsisdn(testMsisdn);

        assertTrue(clientFromDao.isEmpty());
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenDuplicateId() {
        clientDao.save(testMsisdn, testClient);
        assertThrows(IllegalArgumentException.class, () -> clientDao.save(testMsisdn, testClient));
    }

    @Test
    void shouldThrowNoSuchElementExceptionWhenNonexistentClientUpdate() {
        assertThrows(NoSuchElementException.class, () -> clientDao.update(testMsisdn, testClient));
    }

    @Test
    void shouldThrowNoSuchElementExceptionWhenNonexistentClientDelete() {
        assertThrows(NoSuchElementException.class, () -> clientDao.deleteByMsisdn(testMsisdn));
    }

    private static String randomDigitString(Integer targetStringLength) {
        int leftLimit = '0';
        int rightLimit = '9';
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}

