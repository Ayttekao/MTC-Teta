package validator;

import io.ayttekao.model.EnrichmentType;
import io.ayttekao.model.Message;
import io.ayttekao.validator.Middleware;
import io.ayttekao.validator.json.JSONEnrichmentFieldMiddleware;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import utils.JsonReader;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JSONEnrichmentFieldMiddlewareTest {
    private static String validJson;
    private static String invalidJson;
    private static Middleware jsonFieldMiddleware;

    @BeforeAll
    static void setUp() {
        var reader = new JsonReader();
        validJson = reader.readJsonAsString("json/validEnrichRequest.json");
        invalidJson = reader.readJsonAsString("json/invalidEnrichRequest.json");
        jsonFieldMiddleware = Middleware.link(new JSONEnrichmentFieldMiddleware());
    }

    @Test
    void shouldTrueWhenValidJson() {
        var enrichment = EnrichmentType.MSISDN;
        var validMessage = new Message(validJson, enrichment);

        assertTrue(jsonFieldMiddleware.check(validMessage));
    }

    @Test
    void shouldFalseWhenInvalidJson() {
        var enrichment = EnrichmentType.MSISDN;
        var invalidMessage = new Message(invalidJson, enrichment);

        assertFalse(jsonFieldMiddleware.check(invalidMessage));
    }
}
