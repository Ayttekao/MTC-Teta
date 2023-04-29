package service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.ayttekao.repository.ClientRepository;
import io.ayttekao.repository.ClientRepositoryImpl;
import io.ayttekao.marshaller.JSONMarshaller;
import io.ayttekao.marshaller.MessageMarshaller;
import io.ayttekao.model.Client;
import io.ayttekao.model.EnrichmentType;
import io.ayttekao.model.Message;
import io.ayttekao.repository.MessageRepository;
import io.ayttekao.repository.MessageRepositoryImpl;
import io.ayttekao.service.EnrichmentService;
import io.ayttekao.service.EnrichmentServiceImpl;
import io.ayttekao.validator.MessageValidator;
import io.ayttekao.validator.MessageValidatorImpl;
import io.ayttekao.validator.Middleware;
import io.ayttekao.validator.json.JSONEnrichmentFieldMiddleware;
import io.ayttekao.validator.json.JSONFormatMiddleware;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MultiThreadEnrichmentServiceTest {
    private static final Random random = new Random();
    private static final Middleware middleware = Middleware.link(
            new JSONFormatMiddleware(),
            new JSONEnrichmentFieldMiddleware()
    );
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final MessageMarshaller messageMarshaller = new JSONMarshaller(mapper);
    private static final MessageValidator validator = new MessageValidatorImpl(middleware);
    private static final ClientRepository CLIENT_REPOSITORY = new ClientRepositoryImpl();
    private static final MessageRepository enrichedMessages = new MessageRepositoryImpl(new ConcurrentLinkedQueue<>());
    private static final MessageRepository nonEnrichedMessages = new MessageRepositoryImpl(new ConcurrentLinkedQueue<>());

    @Test
    public void shouldEnrichMessageInMultiThreadedEnvironment() throws InterruptedException, ExecutionException {
        var services = IntStream.range(0, 10)
                .mapToObj(i -> createEnrichmentService())
                .toList();

        var executorService = Executors.newFixedThreadPool(10);
        var futures = new ArrayList<Future<String>>();

        for (var i = 0; i < 1000; i++) {
            int index = i % 10;
            var msisdn = randomDigitString(11);
            var message = generateMessage(msisdn, EnrichmentType.MSISDN);
            var client = new Client(randomDigitString(8), randomDigitString(8));
            CLIENT_REPOSITORY.save(msisdn, client);
            futures.add(executorService.submit(() -> services.get(index).enrich(message)));
        }

        for (Future<String> future : futures) {
            future.get();
        }

        executorService.shutdown();

        assertEquals(1000, enrichedMessages.getAll().size() + nonEnrichedMessages.getAll().size());

        for (Message message : enrichedMessages.getAll()) {
            assertTrue(message.getContent().contains("enrichment"));
        }
    }

    private EnrichmentService createEnrichmentService() {
        return new EnrichmentServiceImpl(
                messageMarshaller,
                validator,
                CLIENT_REPOSITORY,
                enrichedMessages,
                nonEnrichedMessages
        );
    }

    private static String randomDigitString(Integer targetStringLength) {
        return random.ints(targetStringLength, 0, 10)
                .mapToObj(Integer::toString)
                .collect(Collectors.joining());
    }

    private Message generateMessage(String msisdn, EnrichmentType enrichmentType) {
        if (random.nextInt(2) == 0) {
            return createValidMessage(msisdn, enrichmentType);
        } else {
            return createInvalidMessage(msisdn, enrichmentType);
        }
    }

    private Message createValidMessage(String msisdn, EnrichmentType enrichmentType) {
        var context = "{\n"
                + "\"action\":\"button_click\",\n"
                + "\"page\":\"book_card\",\n"
                + String.format("\"msisdn\":\"%s\"\n", msisdn)
                + "}";

        return new Message(context, enrichmentType);
    }

    private Message createInvalidMessage(String msisdn, EnrichmentType enrichmentType) {
        return new Message(msisdn, enrichmentType);
    }
}