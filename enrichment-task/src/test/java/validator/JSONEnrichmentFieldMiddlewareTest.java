package validator;

import io.ayttekao.model.EnrichmentType;
import io.ayttekao.model.Message;
import io.ayttekao.validator.json.JSONEnrichmentFieldMiddleware;
import io.ayttekao.validator.Middleware;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class JSONEnrichmentFieldMiddlewareTest {
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
        var validJson =
                "{\n" +
                "    \"action\": \"button_click\",\n" +
                "    \"page\": \"book_card\",\n" +
                "    \"msisdn\": \"88005553535\"\n" +
                "}";
        var validMessage = new Message(validJson, enrichment);

        Assertions.assertTrue(jsonFieldMiddleware.check(validMessage));
    }

    @Test
    @DisplayName("check method test - negative")
    public void negativeTestCheck() {
        var enrichment = EnrichmentType.MSISDN;
        var invalidJson =
                "{\n" +
                "    \"action\": \"button_click\",\n" +
                "    \"page\": \"book_card\",\n" +
                "    \"msisdn1\": \"88005553535\"\n" +
                "}";
        var validMessage = new Message(invalidJson, enrichment);

        Assertions.assertFalse(jsonFieldMiddleware.check(validMessage));
    }
}
