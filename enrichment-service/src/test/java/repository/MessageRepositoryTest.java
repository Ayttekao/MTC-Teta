package repository;

import io.ayttekao.model.EnrichmentType;
import io.ayttekao.model.Message;
import io.ayttekao.repository.MessageRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
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
        Message message = new Message("Hello World!", EnrichmentType.MSISDN);

        messageRepository.add(message);

        List<Message> allMessages = messageRepository.getAll();
        assertTrue(allMessages.contains(message));
    }

    @Test
    public void ShouldRemoveMessageFromRepository() {
        Message message = new Message("Hello World!", EnrichmentType.MSISDN);
        messageRepository.add(message);

        messageRepository.remove(message);

        List<Message> allMessages = messageRepository.getAll();
        assertTrue(allMessages.isEmpty());
    }

    @Test
    public void ShouldQueryMessagesFromRepositoryWithGivenFilter() {
        MessageRepositoryImpl repository = new MessageRepositoryImpl(new ConcurrentLinkedQueue<>());
        Message message1 = new Message("Hello World!", EnrichmentType.MSISDN);
        Message message2 = new Message("Hi there!", EnrichmentType.MSISDN);
        repository.add(message1);
        repository.add(message2);
        Predicate<Message> filter = m -> m.getContent().equals("Hi there!");

        List<Message> result = repository.query(filter);

        assertTrue(result.contains(message2));
    }

    @Test
    public void ShouldGetAllMessageFromRepository() {
        var helloMessage = new Message("Hello", EnrichmentType.MSISDN);
        var hiMessage = new Message("Hi", EnrichmentType.MSISDN);

        messageRepository.add(helloMessage);
        messageRepository.add(hiMessage);

        List<Message> allMessages = messageRepository.getAll();

        assertTrue(allMessages.contains(helloMessage));
        assertTrue(allMessages.contains(hiMessage));
    }
}
