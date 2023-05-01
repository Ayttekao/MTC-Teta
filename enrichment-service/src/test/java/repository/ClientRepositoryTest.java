package repository;

import io.ayttekao.repository.ClientRepository;
import io.ayttekao.repository.ClientRepositoryImpl;
import io.ayttekao.model.Client;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.stream.Collectors;

import static Utils.RandomTestUtils.randomString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;

class ClientRepositoryTest {
    private static final String TEST_MSISDN = randomString(11, false, true);
    private static final Client TEST_CLIENT = new Client("Elliot", "Alderson");
    private ClientRepository clientRepository;

    @BeforeEach
    void beforeEach() {
        clientRepository = new ClientRepositoryImpl();
    }

    @Test
    void shouldFindClientWhenAdding() {
        clientRepository.save(TEST_MSISDN, TEST_CLIENT);

        var clientFromDao = clientRepository.findByMsisdn(TEST_MSISDN);

        assertTrue(clientFromDao.isPresent());
        assertEquals(TEST_CLIENT.getFirstName(), clientFromDao.get().getFirstName());
        assertEquals(TEST_CLIENT.getLastName(), clientFromDao.get().getLastName());
    }

    @Test
    void shouldGetAll() {
        var countClient = 20;
        var random = new Random();
        var list = random.ints(countClient, 0, 10)
                .mapToObj(i -> random.ints(11, 0, 10)
                        .mapToObj(Integer::toString)
                        .collect(Collectors.joining()))
                .collect(Collectors.toCollection(ArrayList::new));

        list.forEach(id -> clientRepository.save(id, TEST_CLIENT));

        var listClient = clientRepository.getAll();

        assertThat(listClient, hasSize(list.size()));
    }

    @Test
    void shouldChangeClientWhenUpdate() {
        var newFirstName = "Mr";
        var newLastName = "Robot";

        clientRepository.save(TEST_MSISDN, TEST_CLIENT);
        clientRepository.update(TEST_MSISDN, new Client(newFirstName, newLastName));

        var clientFromDao = clientRepository.findByMsisdn(TEST_MSISDN);

        assertTrue(clientFromDao.isPresent());
        assertEquals(newFirstName, clientFromDao.get().getFirstName());
        assertEquals(newLastName, clientFromDao.get().getLastName());
    }

    @Test
    void shouldReturnEmptyClientWhenDeleted() {
        clientRepository.save(TEST_MSISDN, TEST_CLIENT);
        clientRepository.deleteByMsisdn(TEST_MSISDN);

        var clientFromDao = clientRepository.findByMsisdn(TEST_MSISDN);

        assertTrue(clientFromDao.isEmpty());
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenDuplicateId() {
        clientRepository.save(TEST_MSISDN, TEST_CLIENT);
        assertThrows(IllegalArgumentException.class, () -> clientRepository.save(TEST_MSISDN, TEST_CLIENT));
    }

    @Test
    void shouldThrowNoSuchElementExceptionWhenNonexistentClientUpdate() {
        assertThrows(NoSuchElementException.class, () -> clientRepository.update(TEST_MSISDN, TEST_CLIENT));
    }

    @Test
    void shouldThrowNoSuchElementExceptionWhenNonexistentClientDelete() {
        assertThrows(NoSuchElementException.class, () -> clientRepository.deleteByMsisdn(TEST_MSISDN));
    }
}

