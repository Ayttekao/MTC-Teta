package repository;

import io.ayttekao.model.EnrichmentType;
import io.ayttekao.model.Message;
import io.ayttekao.repository.MessageRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class MessageRepositoryTest {
    private MessageRepositoryImpl messageRepository;

    @BeforeEach
    public void setUp() {
        messageRepository = new MessageRepositoryImpl(new ConcurrentLinkedQueue<>());
    }

    @Test
    public void ShouldAddMessageToRepository() {
        var message = new Message("Hello World!", EnrichmentType.MSISDN);

        messageRepository.add(message);

        var allMessages = messageRepository.getAll();
        assertTrue(allMessages.contains(message));
    }

    @Test
    public void ShouldRemoveMessageFromRepository() {
        var message = new Message("Hello World!", EnrichmentType.MSISDN);
        messageRepository.add(message);

        messageRepository.remove(message);

        var allMessages = messageRepository.getAll();
        assertTrue(allMessages.isEmpty());
    }

    @Test
    public void ShouldQueryMessagesFromRepositoryWithGivenFilter() {
        var helloMessage = new Message("Hello", EnrichmentType.MSISDN);
        var hiMessage = new Message("Hi", EnrichmentType.MSISDN);
        messageRepository.add(helloMessage);
        messageRepository.add(hiMessage);
        Predicate<Message> filter = message -> message.getContent().equals("Hi");

        var result = messageRepository.query(filter);

        assertTrue(result.contains(hiMessage));
    }

    @Test
    public void ShouldGetAllMessageFromRepository() {
        var helloMessage = new Message("Hello", EnrichmentType.MSISDN);
        var hiMessage = new Message("Hi", EnrichmentType.MSISDN);

        messageRepository.add(helloMessage);
        messageRepository.add(hiMessage);

        var allMessages = messageRepository.getAll();

        assertTrue(allMessages.contains(helloMessage));
        assertTrue(allMessages.contains(hiMessage));
    }
}
