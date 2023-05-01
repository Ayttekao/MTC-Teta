package repository;

import io.ayttekao.model.EnrichmentType;
import io.ayttekao.model.Message;
import io.ayttekao.repository.MessageRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertTrue;

class MessageRepositoryTest {
    private MessageRepositoryImpl messageRepository;

    @BeforeEach
    public void beforeEach() {
        messageRepository = new MessageRepositoryImpl(new ConcurrentLinkedQueue<>());
    }

    @Test
    void ShouldAddMessageToRepository() {
        var message = new Message("Hello World!", EnrichmentType.MSISDN);

        messageRepository.add(message);

        var allMessages = messageRepository.getAll();
        assertTrue(allMessages.contains(message));
    }

    @Test
    void ShouldRemoveMessageFromRepository() {
        var message = new Message("Hello World!", EnrichmentType.MSISDN);
        messageRepository.add(message);

        messageRepository.remove(message);

        var allMessages = messageRepository.getAll();
        assertTrue(allMessages.isEmpty());
    }

    @Test
    void ShouldQueryMessagesFromRepositoryWithGivenFilter() {
        var helloMessage = new Message("Hello", EnrichmentType.MSISDN);
        var hiMessage = new Message("Hi", EnrichmentType.MSISDN);
        messageRepository.add(helloMessage);
        messageRepository.add(hiMessage);
        Predicate<Message> filter = message -> message.getContent().equals("Hi");

        var result = messageRepository.query(filter);

        assertTrue(result.contains(hiMessage));
    }

    @Test
    void ShouldGetAllMessageFromRepository() {
        var helloMessage = new Message("Hello", EnrichmentType.MSISDN);
        var hiMessage = new Message("Hi", EnrichmentType.MSISDN);

        messageRepository.add(helloMessage);
        messageRepository.add(hiMessage);

        var allMessages = messageRepository.getAll();

        assertTrue(allMessages.contains(helloMessage));
        assertTrue(allMessages.contains(hiMessage));
    }
}
