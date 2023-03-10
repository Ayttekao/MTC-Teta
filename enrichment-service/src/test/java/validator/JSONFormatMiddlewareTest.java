package validator;

import io.ayttekao.model.EnrichmentType;
import io.ayttekao.model.Message;
import io.ayttekao.validator.Middleware;
import io.ayttekao.validator.json.JSONFormatMiddleware;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JSONFormatMiddlewareTest {
    private static final String VALID_JSON =
            "{\n" +
                    "\"action\":\"button_click\",\n" +
                    "\"page\":\"book_card\",\n" +
                    "\"msisdn\":\"88005553535\"\n" +
                    "}";
    private static final String INVALID_JSON = "Invalid_Json";
    private static Middleware jsonFormatMiddleware;

    @BeforeAll
    static void init() {
        jsonFormatMiddleware = Middleware.link(
                new JSONFormatMiddleware()
        );
    }

    @Test
    public void shouldTruedWhenValidJson() {
        var enrichment = EnrichmentType.MSISDN;
        var validMessage = new Message(VALID_JSON, enrichment);

        assertTrue(jsonFormatMiddleware.check(validMessage));
    }

    @Test
    public void shouldFalseWhenInvalidJson() {
        var enrichment = EnrichmentType.MSISDN;
        var validMessage = new Message(INVALID_JSON, enrichment);

        assertFalse(jsonFormatMiddleware.check(validMessage));
    }
}
