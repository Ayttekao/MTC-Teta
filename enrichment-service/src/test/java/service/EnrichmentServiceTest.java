package service;

import io.ayttekao.marshaller.MessageMarshaller;
import io.ayttekao.model.*;
import io.ayttekao.repository.ClientRepository;
import io.ayttekao.repository.EnrichRepository;
import io.ayttekao.service.EnrichmentServiceImpl;
import io.ayttekao.validator.MessageValidator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import utils.JsonReader;

import java.io.IOException;
import java.util.Optional;

import static io.ayttekao.mapper.EnrichMapper.toEnrichResponse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static utils.ConverterTestUtils.buildPojo;

@ExtendWith(MockitoExtension.class)
class EnrichmentServiceTest {
    private static final String MSISDN_VALUE = "88005553535";
    private static final Client CLIENT = new Client("Vasya", "Ivanov");
    private static final String INVALID_JSON = "Invalid_Json";
    private static String validJson;
    private static String enrichmentJson;
    private static String enrichmentJsonWithDifferentClient;
    private static String jsonWithUnknownClient;

    @Mock
    private MessageMarshaller messageMarshaller;
    @Mock
    private MessageValidator messageValidator;
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private EnrichRepository<EnrichResponse> enrichedRepository;
    @Mock
    private EnrichRepository<EnrichRequest> nonEnrichedRepository;
    @InjectMocks
    private EnrichmentServiceImpl enrichmentService;

    @BeforeAll
    static void setUp() throws IOException {
        var reader = new JsonReader();
        validJson = reader.readJsonAsString("json/validEnrichRequest.json");
        enrichmentJson = reader.readJsonAsString("json/validEnrichResponse.json");
        enrichmentJsonWithDifferentClient = reader.readJsonAsString("json/enrichResponseDifferentClient.json");
        jsonWithUnknownClient = reader.readJsonAsString("json/enrichRequestUnknownClient.json");
    }

    @Test
    void shouldReturnEnrichmentMessageWhenValidMessage() {
        var message = new Message(validJson, EnrichmentType.MSISDN);
        var request = buildPojo(EnrichRequest.class);
        request.setMsisdn(MSISDN_VALUE);
        var response = toEnrichResponse(request, CLIENT);

        when(messageValidator.isValid(message)).thenReturn(true);
        when(messageMarshaller.marshall(message.getContent())).thenReturn(request);
        when(clientRepository.findByMsisdn(MSISDN_VALUE)).thenReturn(Optional.of(CLIENT));
        when(messageMarshaller.unmarshall(response)).thenReturn(enrichmentJson);

        var result = enrichmentService.enrich(message);

        assertEquals(enrichmentJson, result);
    }

    @Test
    void shouldReturnSameResultWhenInvalidMessage() {
        var message = new Message(INVALID_JSON, EnrichmentType.MSISDN);

        when(messageValidator.isValid(message)).thenReturn(false);
        var result = enrichmentService.enrich(message);

        assertEquals(INVALID_JSON, result);
    }

    @Test
    void shouldReplaceEnrichmentFieldWhenClientDifferent() {
        var message = new Message(enrichmentJsonWithDifferentClient, EnrichmentType.MSISDN);
        var request = buildPojo(EnrichRequest.class);
        request.setMsisdn(MSISDN_VALUE);
        var response = toEnrichResponse(request, CLIENT);

        when(messageValidator.isValid(message)).thenReturn(true);
        when(messageMarshaller.marshall(message.getContent())).thenReturn(request);
        when(clientRepository.findByMsisdn(MSISDN_VALUE)).thenReturn(Optional.of(CLIENT));
        when(messageMarshaller.unmarshall(response)).thenReturn(enrichmentJson);

        var result = enrichmentService.enrich(message);

        assertEquals(enrichmentJson, result);
    }

    @Test
    void shouldReturnSameMessageContentWhenClientNotFound() {
        var message = new Message(jsonWithUnknownClient, EnrichmentType.MSISDN);
        var request = buildPojo(EnrichRequest.class);
        request.setMsisdn(MSISDN_VALUE);

        when(messageValidator.isValid(message)).thenReturn(true);
        when(messageMarshaller.marshall(message.getContent())).thenReturn(request);
        when(clientRepository.findByMsisdn(MSISDN_VALUE)).thenReturn(Optional.empty());

        var result = enrichmentService.enrich(message);

        assertEquals(jsonWithUnknownClient, result);
    }
}
