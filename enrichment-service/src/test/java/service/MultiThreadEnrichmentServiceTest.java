package service;

import io.ayttekao.marshaller.JSONMarshaller;
import io.ayttekao.marshaller.MessageMarshaller;
import io.ayttekao.model.Client;
import io.ayttekao.model.EnrichmentType;
import io.ayttekao.model.Message;
import io.ayttekao.repository.ClientRepository;
import io.ayttekao.repository.ClientRepositoryImpl;
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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;

import static Utils.RandomTestUtils.randomString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MultiThreadEnrichmentServiceTest {
    private static final Random random = new Random();
    private static final Middleware middleware = Middleware.link(
            new JSONFormatMiddleware(),
            new JSONEnrichmentFieldMiddleware()
    );
    private static final MessageMarshaller messageMarshaller = new JSONMarshaller();
    private static final MessageValidator validator = new MessageValidatorImpl(middleware);
    private static final ClientRepository clientRepository = new ClientRepositoryImpl();
    private static final MessageRepository enrichedMessages = new MessageRepositoryImpl(new ConcurrentLinkedQueue<>());
    private static final MessageRepository nonEnrichedMessages = new MessageRepositoryImpl(new ConcurrentLinkedQueue<>());

    @Test
    void shouldEnrichMessageInMultiThreadedEnvironment() {
        var service = createEnrichmentService();

        var executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        var completableFutures = new ArrayList<CompletableFuture<String>>();

        for (var i = 0; i < 1000; i++) {
            var msisdn = randomString(11, false, true);
            var message = generateMessage(msisdn, EnrichmentType.MSISDN);
            var client = new Client(randomString(), randomString());
            clientRepository.save(msisdn, client);

            CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> service.enrich(message), executorService);
            completableFutures.add(future);
        }

        CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture[0])).join();

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
                clientRepository,
                enrichedMessages,
                nonEnrichedMessages
        );
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