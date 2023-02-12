package validator;

import io.ayttekao.model.EnrichmentType;
import io.ayttekao.model.Message;
import io.ayttekao.validator.Middleware;
import io.ayttekao.validator.json.JSONEnrichmentFieldMiddleware;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class JSONEnrichmentFieldMiddlewareTest {
    private static final String VALID_JSON =
            "{\n" +
                    "\"action\":\"button_click\",\n" +
                    "\"page\":\"book_card\",\n" +
                    "\"msisdn\":\"88005553535\"\n" +
                    "}";
    private static final String INVALID_JSON =
            "{\n" +
                    "\"action\": \"button_click\",\n" +
                    "\"page\": \"book_card\",\n" +
                    "\"msisdn1\": \"88005553535\"\n" +
                    "}";
    private static Middleware jsonFieldMiddleware;

    @BeforeAll
    static void init() {
        jsonFieldMiddleware = Middleware.link(
                new JSONEnrichmentFieldMiddleware()
        );
    }

    @Test
    @DisplayName("check method test - positive")
    public void positiveTestCheck() {
        var enrichment = EnrichmentType.MSISDN;
        var validMessage = new Message(VALID_JSON, enrichment);

        Assertions.assertTrue(jsonFieldMiddleware.check(validMessage));
    }

    @Test
    @DisplayName("check method test - negative")
    public void negativeTestCheck() {
        var enrichment = EnrichmentType.MSISDN;
        var validMessage = new Message(INVALID_JSON, enrichment);

        Assertions.assertFalse(jsonFieldMiddleware.check(validMessage));
    }
}
