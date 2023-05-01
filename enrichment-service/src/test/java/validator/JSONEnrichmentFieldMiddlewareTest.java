package validator;

import io.ayttekao.model.EnrichmentType;
import io.ayttekao.model.Message;
import io.ayttekao.validator.Middleware;
import io.ayttekao.validator.json.JSONEnrichmentFieldMiddleware;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JSONEnrichmentFieldMiddlewareTest {
    private static final String VALID_JSON = """
            {
            "action":"button_click",
            "page":"book_card",
            "msisdn":"88005553535"
            }""";
    private static final String INVALID_JSON = """
            {
            "action": "button_click",
            "page": "book_card",
            "msisdn1": "88005553535"
            }""";
    private static Middleware jsonFieldMiddleware;

    @BeforeAll
    static void beforeAll() {
        jsonFieldMiddleware = Middleware.link(
                new JSONEnrichmentFieldMiddleware()
        );
    }

    @Test
    void shouldTrueWhenValidJson() {
        var enrichment = EnrichmentType.MSISDN;
        var validMessage = new Message(VALID_JSON, enrichment);

        assertTrue(jsonFieldMiddleware.check(validMessage));
    }

    @Test
    void shouldFalseWhenInvalidJson() {
        var enrichment = EnrichmentType.MSISDN;
        var validMessage = new Message(INVALID_JSON, enrichment);

        assertFalse(jsonFieldMiddleware.check(validMessage));
    }
}
