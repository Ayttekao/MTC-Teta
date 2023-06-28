package repository;

import io.ayttekao.model.EnrichRequest;
import io.ayttekao.repository.EnrichRepository;
import io.ayttekao.repository.EnrichRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static utils.ConverterTestUtils.buildPojo;

class EnrichRepositoryTest {
    private EnrichRepository<EnrichRequest> enrichRepository;

    @BeforeEach
    public void beforeEach() {
        enrichRepository = new EnrichRepositoryImpl<>(new ConcurrentLinkedQueue<>());
    }

    @Test
    void ShouldAddEnrichToRepository() {
        var request = buildPojo(EnrichRequest.class);

        enrichRepository.add(request);

        var allMessages = enrichRepository.getAll();
        assertTrue(allMessages.contains(request));
    }

    @Test
    void ShouldRemoveEnrichFromRepository() {
        var request = buildPojo(EnrichRequest.class);
        enrichRepository.add(request);

        enrichRepository.remove(request);

        var allMessages = enrichRepository.getAll();
        assertTrue(allMessages.isEmpty());
    }

    @Test
    void ShouldQueryEnrichFromRepositoryWithGivenFilter() {
        var firstRequest = buildPojo(EnrichRequest.class);
        var secondRequest = buildPojo(EnrichRequest.class);

        enrichRepository.add(firstRequest);
        enrichRepository.add(secondRequest);
        Predicate<EnrichRequest> filter = message -> message.getPage().equals(firstRequest.getPage());

        var result = enrichRepository.query(filter);

        assertTrue(result.contains(firstRequest));
    }

    @Test
    void ShouldGetAllMessageFromRepository() {
        var firstRequest = buildPojo(EnrichRequest.class);
        var secondRequest = buildPojo(EnrichRequest.class);

        enrichRepository.add(firstRequest);
        enrichRepository.add(secondRequest);

        var allMessages = enrichRepository.getAll();

        assertTrue(allMessages.contains(firstRequest));
        assertTrue(allMessages.contains(secondRequest));
    }
}
