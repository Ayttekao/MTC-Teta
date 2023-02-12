package marshaller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.ayttekao.marshaller.JSONMarshaller;
import io.ayttekao.marshaller.MessageMarshaller;
import org.json.JSONException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import java.util.HashMap;

public class JSONMarshallerTest {
    private static final String ACTION_KEY = "action";
    private static final String PAGE_KEY = "page";
    private static final String MSISDN_KEY = "msisdn";
    private static final String ACTION_VALUE = "button_click";
    private static final String PAGE_VALUE = "book_card";
    private static final String MSISDN_VALUE = "88005553535";
    private static final String VALID_JSON =
            "{\n" +
                    "\"action\":\"button_click\",\n" +
                    "\"page\":\"book_card\",\n" +
                    "\"msisdn\":\"88005553535\"\n" +
                    "}";
    private static final String INVALID_JSON = "Invalid_Json";
    private static MessageMarshaller jsonMarshaller;

    @BeforeAll
    static void init() {
        jsonMarshaller = new JSONMarshaller(new ObjectMapper());
    }

    @Test
    @DisplayName("marshall method test - positive")
    public void positiveTestMarshall() {
        var map = jsonMarshaller.marshall(VALID_JSON);

        Assertions.assertTrue(map.containsKey(ACTION_KEY));
        Assertions.assertTrue(map.containsKey(PAGE_KEY));
        Assertions.assertTrue(map.containsKey(MSISDN_KEY));
        Assertions.assertTrue(map.containsValue(ACTION_VALUE));
        Assertions.assertTrue(map.containsValue(PAGE_VALUE));
        Assertions.assertTrue(map.containsValue(MSISDN_VALUE));
    }

    @Test
    @DisplayName("marshall method test - negative")
    public void negativeTestMarshall() {

        var thrown = Assertions.assertThrows(
                RuntimeException.class, () -> jsonMarshaller.marshall(INVALID_JSON), "RuntimeException was expected"
        );

        Assertions.assertEquals(RuntimeException.class, thrown.getClass());
    }

    @Test
    @DisplayName("unmarshall method test - positive")
    public void positiveTestUnmarshall() throws JSONException {
        var map = new HashMap<String, String>();
        map.put(ACTION_KEY, ACTION_VALUE);
        map.put(PAGE_KEY, PAGE_VALUE);
        map.put(MSISDN_KEY, MSISDN_VALUE);

        var json = jsonMarshaller.unmarshall(map);

        JSONAssert.assertEquals(VALID_JSON, json, JSONCompareMode.STRICT);
        JSONAssert.assertEquals(VALID_JSON, json, JSONCompareMode.LENIENT);
    }
}
