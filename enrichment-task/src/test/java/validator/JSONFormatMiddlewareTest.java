package validator;

import io.ayttekao.model.EnrichmentType;
import io.ayttekao.model.Message;
import io.ayttekao.validator.*;
import io.ayttekao.validator.json.JSONFormatMiddleware;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class JSONFormatMiddlewareTest {
    private static Middleware jsonFormatMiddleware;

    @BeforeAll
    static void init() {
        jsonFormatMiddleware = Middleware.link(
                new JSONFormatMiddleware()
        );
    }

    @Test
    @DisplayName("check method test - positive")
    public void checkMethodPositiveTest() {
        var enrichment = EnrichmentType.MSISDN;
        var validJson = "{\n" +
                "    \"action\": \"button_click\",\n" +
                "    \"page\": \"book_card\",\n" +
                "    \"msisdn\": \"88005553535\"\n" +
                "}";
        var validMessage = new Message(validJson, enrichment);

        Assertions.assertEquals(true, jsonFormatMiddleware.check(validMessage));
    }

    @Test
    @DisplayName("check method test - negative")
    public void checkMethodNegativeTest() {
        var enrichment = EnrichmentType.MSISDN;
        var invalidJson = "Invalid_Json";
        var validMessage = new Message(invalidJson, enrichment);

        Assertions.assertEquals(false, jsonFormatMiddleware.check(validMessage));
    }
}
