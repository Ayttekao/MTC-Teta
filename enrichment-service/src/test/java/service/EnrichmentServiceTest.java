package service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.ayttekao.dao.ClientDao;
import io.ayttekao.dao.ClientDaoImpl;
import io.ayttekao.marshaller.JSONMarshaller;
import io.ayttekao.model.Client;
import io.ayttekao.model.EnrichmentType;
import io.ayttekao.model.Message;
import io.ayttekao.service.EnrichmentService;
import io.ayttekao.validator.MessageValidatorImpl;
import io.ayttekao.validator.Middleware;
import io.ayttekao.validator.json.JSONEnrichmentFieldMiddleware;
import io.ayttekao.validator.json.JSONFormatMiddleware;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

public class EnrichmentServiceTest {
    private static final String VALID_JSON =
            "{\n" +
                    "\"action\":\"button_click\",\n" +
                    "\"page\":\"book_card\",\n" +
                    "\"msisdn\":\"88005553535\"\n" +
                    "}";
    private static final String ENRICHMENT_JSON =
            "{\n" +
                    "\"action\": \"button_click\",\n" +
                    "\"page\": \"book_card\",\n" +
                    "\"msisdn\": \"88005553535\",\n" +
                    "\"enrichment\": {\n" +
                    "\"firstName\": \"Vasya\",\n" +
                    "\"lastName\": \"Ivanov\"\n" +
                    "}\n" +
                    "}";
    private static final String ENRICHMENT_JSON_WITH_DIFFERENT_CLIENT =
            "{\n" +
                    "\"action\": \"button_click\",\n" +
                    "\"page\": \"book_card\",\n" +
                    "\"msisdn\": \"88005553535\",\n" +
                    "\"enrichment\": {\n" +
                    "\"firstName\": \"Darya\",\n" +
                    "\"lastName\": \"Zayceva\"\n" +
                    "}\n" +
                    "}";
    private static final String JSON_WITH_UNKNOWN_CLIENT =
            "{\n" +
                    "\"action\":\"button_click\",\n" +
                    "\"page\":\"book_card\",\n" +
                    "\"msisdn\":\"84952313645\"\n" +
                    "}";
    private static final String INVALID_JSON = "Invalid_Json";
    private static final String JSON_WITHOUT_MSISDN =
            "{\n" +
                    "\"action\":\"button_click\",\n" +
                    "\"page\":\"book_card\",\n" +
                    "}";
    private static EnrichmentService enrichmentService;
    private static ClientDao clientDao;

    @BeforeAll
    static void init() {
        var middleware = Middleware.link(
                new JSONFormatMiddleware(),
                new JSONEnrichmentFieldMiddleware()
        );
        var messageValidator = new MessageValidatorImpl(middleware);
        var mapper = new ObjectMapper();
        var messageMarshaller = new JSONMarshaller(mapper);

        clientDao = new ClientDaoImpl();
        clientDao.save(88005553535L, new Client("Vasya", "Ivanov"));
        enrichmentService = new EnrichmentService(
                messageValidator,
                clientDao,
                messageMarshaller
        );
    }

    @Test
    public void shouldReturnEnrichmentJsonWhenValidJson() throws JSONException {
        var message = new Message(VALID_JSON, EnrichmentType.MSISDN);

        var result = enrichmentService.enrich(message);

        JSONAssert.assertEquals(ENRICHMENT_JSON, result, JSONCompareMode.STRICT);
        JSONAssert.assertEquals(ENRICHMENT_JSON, result, JSONCompareMode.LENIENT);
    }

    @Test
    public void shouldReturnSameResultWhenInvalidJson() throws JSONException {
        var message = new Message(INVALID_JSON, EnrichmentType.MSISDN);

        var result = enrichmentService.enrich(message);

        JSONAssert.assertEquals(INVALID_JSON, result, JSONCompareMode.STRICT);
        JSONAssert.assertEquals(INVALID_JSON, result, JSONCompareMode.LENIENT);
    }

    @Test
    public void shouldReturnSameResultWhenJsonWithoutMsisdn() throws JSONException {
        var message = new Message(JSON_WITHOUT_MSISDN, EnrichmentType.MSISDN);

        var result = enrichmentService.enrich(message);

        JSONAssert.assertEquals(JSON_WITHOUT_MSISDN, result, JSONCompareMode.STRICT);
        JSONAssert.assertEquals(JSON_WITHOUT_MSISDN, result, JSONCompareMode.LENIENT);
    }

    @Test
    public void shouldReplaceEnrichmentFieldIfClientDifferent() throws JSONException {
        var message = new Message(ENRICHMENT_JSON_WITH_DIFFERENT_CLIENT, EnrichmentType.MSISDN);

        var result = enrichmentService.enrich(message);

        JSONAssert.assertEquals(ENRICHMENT_JSON, result, JSONCompareMode.STRICT);
        JSONAssert.assertEquals(ENRICHMENT_JSON, result, JSONCompareMode.LENIENT);
    }

    @Test
    public void test() throws JSONException {
        var message = new Message(JSON_WITH_UNKNOWN_CLIENT, EnrichmentType.MSISDN);

        var result = enrichmentService.enrich(message);

        JSONAssert.assertEquals(JSON_WITH_UNKNOWN_CLIENT, result, JSONCompareMode.STRICT);
        JSONAssert.assertEquals(JSON_WITH_UNKNOWN_CLIENT, result, JSONCompareMode.LENIENT);
    }
}
