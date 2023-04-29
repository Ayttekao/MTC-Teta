package service;

import io.ayttekao.dao.ClientDao;
import io.ayttekao.marshaller.MessageMarshaller;
import io.ayttekao.model.Client;
import io.ayttekao.model.EnrichmentType;
import io.ayttekao.model.Message;
import io.ayttekao.repository.MessageRepository;
import io.ayttekao.repository.MessageRepositoryImpl;
import io.ayttekao.service.EnrichmentService;
import io.ayttekao.service.EnrichmentServiceImpl;
import io.ayttekao.validator.MessageValidator;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EnrichmentServiceTest {
    private static final String ACTION_KEY = "action";
    private static final String PAGE_KEY = "page";
    private static final String MSISDN_KEY = "msisdn";
    private static final String ACTION_VALUE = "button_click";
    private static final String PAGE_VALUE = "book_card";
    private static final String ENRICHMENT_KEY = "enrichment";
    private static final String MSISDN_VALUE = "88005553535";
    private static final Client CLIENT = new Client("Vasya", "Ivanov");
    private static final Client UNKNOWN_CLIENT = new Client("Darya", "Zayceva");
    private static final String VALID_JSON =
            """
            {
                "action":"button_click",
                "page":"book_card",
                "msisdn":"88005553535"
            }
            """;
    private static final String ENRICHMENT_JSON =
            """
            {
                "action": "button_click",
                "page": "book_card",
                "msisdn": "88005553535",
                "enrichment": {
                    "firstName": "Vasya",
                    "lastName": "Ivanov"
                }
            }
            """;
    private static final String ENRICHMENT_JSON_WITH_DIFFERENT_CLIENT =
            """
            {
                "action": "button_click",
                "page": "book_card",
                "msisdn": "88005553535",
                "enrichment": {
                    "firstName": "Darya",
                    "lastName": "Zayceva"
                }
            }
            """;
    private static final String JSON_WITH_UNKNOWN_CLIENT =
            """
            {
                "action":"button_click",
                "page":"book_card",
                "msisdn":"84952313645"
            }
            """;
    private static final String INVALID_JSON = "Invalid_Json";
    private static final MessageMarshaller messageMarshaller = mock(MessageMarshaller.class);
    private static final MessageValidator messageValidator = mock(MessageValidator.class);
    private static final ClientDao clientDao = mock(ClientDao.class);
    private static final MessageRepository messageRepository = mock(MessageRepositoryImpl.class);
    private static final EnrichmentService enrichmentService = new EnrichmentServiceImpl(
            messageMarshaller,
            messageValidator,
            clientDao,
            messageRepository,
            messageRepository
    );

    @Test
    public void shouldReturnEnrichmentMessageWhenValidMessage() {
        var message = new Message(VALID_JSON, EnrichmentType.MSISDN);
        var marshalledMessageMap = new HashMap<String, Object>();
        marshalledMessageMap.put(ACTION_KEY, ACTION_VALUE);
        marshalledMessageMap.put(PAGE_KEY, PAGE_VALUE);
        marshalledMessageMap.put(MSISDN_KEY, MSISDN_VALUE);

        when(messageValidator.isValid(message)).thenReturn(true);
        when(messageMarshaller.marshall(message.getContent())).thenReturn(marshalledMessageMap);
        when(clientDao.findByMsisdn(MSISDN_VALUE)).thenReturn(Optional.of(CLIENT));
        when(messageMarshaller.unmarshall(marshalledMessageMap)).thenReturn(ENRICHMENT_JSON);

        var result = enrichmentService.enrich(message);

        assertEquals(ENRICHMENT_JSON, result);
    }

    @Test
    public void shouldReturnSameResultWhenInvalidMessage() {
        var message = new Message(INVALID_JSON, EnrichmentType.MSISDN);

        when(messageValidator.isValid(message)).thenReturn(false);
        var result = enrichmentService.enrich(message);

        assertEquals(INVALID_JSON, result);
    }

    @Test
    public void shouldReplaceEnrichmentFieldWhenClientDifferent() {
        var message = new Message(ENRICHMENT_JSON_WITH_DIFFERENT_CLIENT, EnrichmentType.MSISDN);
        var marshalledMessageMap = new HashMap<String, Object>();
        marshalledMessageMap.put(ACTION_KEY, ACTION_VALUE);
        marshalledMessageMap.put(PAGE_KEY, PAGE_VALUE);
        marshalledMessageMap.put(MSISDN_KEY, MSISDN_VALUE);
        marshalledMessageMap.put(ENRICHMENT_KEY, UNKNOWN_CLIENT);

        when(messageValidator.isValid(message)).thenReturn(true);
        when(messageMarshaller.marshall(message.getContent())).thenReturn(marshalledMessageMap);
        when(clientDao.findByMsisdn(MSISDN_VALUE)).thenReturn(Optional.of(CLIENT));
        when(messageMarshaller.unmarshall(marshalledMessageMap)).thenReturn(ENRICHMENT_JSON);

        var result = enrichmentService.enrich(message);

        assertEquals(ENRICHMENT_JSON, result);
    }

    @Test
    public void shouldReturnSameMessageContentWhenClientNotFound() {
        var message = new Message(JSON_WITH_UNKNOWN_CLIENT, EnrichmentType.MSISDN);
        var marshalledMessageMap = new HashMap<String, Object>();
        marshalledMessageMap.put(ACTION_KEY, ACTION_VALUE);
        marshalledMessageMap.put(PAGE_KEY, PAGE_VALUE);
        marshalledMessageMap.put(MSISDN_KEY, MSISDN_VALUE);

        when(messageValidator.isValid(message)).thenReturn(true);
        when(messageMarshaller.marshall(message.getContent())).thenReturn(marshalledMessageMap);
        when(clientDao.findByMsisdn(MSISDN_VALUE)).thenReturn(Optional.empty());
        when(messageMarshaller.unmarshall(marshalledMessageMap)).thenReturn(JSON_WITH_UNKNOWN_CLIENT);

        var result = enrichmentService.enrich(message);

        assertEquals(JSON_WITH_UNKNOWN_CLIENT, result);
    }
}
