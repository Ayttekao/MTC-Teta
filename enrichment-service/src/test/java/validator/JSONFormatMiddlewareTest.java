package validator;

import io.ayttekao.model.EnrichmentType;
import io.ayttekao.model.Message;
import io.ayttekao.validator.Middleware;
import io.ayttekao.validator.json.JSONFormatMiddleware;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import utils.JsonReader;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JSONFormatMiddlewareTest {
    private static String validJson;
    private static String invalidJson;
    private static Middleware jsonFormatMiddleware;

    @BeforeAll
    static void setUp() {
        var reader = new JsonReader();
        validJson = reader.readJsonAsString("json/validEnrichRequest.json");
        invalidJson = reader.readJsonAsString("json/invalidFormatEnrichRequest.json");
        jsonFormatMiddleware = Middleware.link(new JSONFormatMiddleware());
    }

    @Test
    void shouldTruedWhenValidJson() {
        var enrichment = EnrichmentType.MSISDN;
        var validMessage = new Message(validJson, enrichment);

        assertTrue(jsonFormatMiddleware.check(validMessage));
    }

    @Test
    void shouldFalseWhenInvalidJson() {
        var enrichment = EnrichmentType.MSISDN;
        var invalidMessage = new Message(invalidJson, enrichment);

        assertFalse(jsonFormatMiddleware.check(invalidMessage));
    }
}
