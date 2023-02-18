package marshaller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.ayttekao.marshaller.JSONMarshaller;
import io.ayttekao.marshaller.MessageMarshaller;
import io.ayttekao.model.Client;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import java.util.Arrays;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class JSONMarshallerTest {
    private static final String ACTION_KEY = "action";
    private static final String PAGE_KEY = "page";
    private static final String MSISDN_KEY = "msisdn";
    private static final String ACTION_VALUE = "button_click";
    private static final String PAGE_VALUE = "book_card";
    private static final String MSISDN_VALUE = "88005553535";
    private static final String ENRICHMENT_KEY = "enrichment";
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
    private static final String INVALID_JSON = "Invalid_Json";
    private static MessageMarshaller jsonMarshaller;

    @BeforeAll
    static void init() {
        jsonMarshaller = new JSONMarshaller(new ObjectMapper());
    }

    @Test
    public void shouldMarshallToMapWhenValidJson() {
        var marshalledMessageMap = jsonMarshaller.marshall(VALID_JSON);
        var keyList = Arrays.asList(ACTION_KEY, PAGE_KEY, MSISDN_KEY);
        var valueList = Arrays.asList(ACTION_VALUE, PAGE_VALUE, MSISDN_VALUE);

        for (String key : keyList) {
            assertTrue(marshalledMessageMap.containsKey(key));
        }
        for (String value : valueList) {
            assertTrue(marshalledMessageMap.containsValue(value));
        }
    }

    @Test
    public void shouldMarshallToMapWhenNestedJson() {
        var marshalledMessageMap = jsonMarshaller.marshall(ENRICHMENT_JSON);
        var keyList = Arrays.asList(ACTION_KEY, PAGE_KEY, MSISDN_KEY, ENRICHMENT_KEY);
        var valueList = Arrays.asList(ACTION_VALUE, PAGE_VALUE, MSISDN_VALUE);

        for (String key : keyList) {
            assertTrue(marshalledMessageMap.containsKey(key));
        }
        for (String value : valueList) {
            assertTrue(marshalledMessageMap.containsValue(value));
        }
    }

    @Test
    public void shouldThrowExceptionWhenInvalidJson() {

        var thrown = assertThrows(
                RuntimeException.class, () -> jsonMarshaller.marshall(INVALID_JSON), "RuntimeException was expected"
        );

        assertEquals(RuntimeException.class, thrown.getClass());
    }

    @Test
    public void shouldUnmarshallMapToValidJson() throws JSONException {
        var marshalledMessageMap = new HashMap<String, Object>();
        marshalledMessageMap.put(ACTION_KEY, ACTION_VALUE);
        marshalledMessageMap.put(PAGE_KEY, PAGE_VALUE);
        marshalledMessageMap.put(MSISDN_KEY, MSISDN_VALUE);

        var json = jsonMarshaller.unmarshall(marshalledMessageMap);

        JSONAssert.assertEquals(VALID_JSON, json, JSONCompareMode.STRICT);
        JSONAssert.assertEquals(VALID_JSON, json, JSONCompareMode.LENIENT);
    }

    @Test
    public void shouldUnmarshallMapToNestedJson() throws JSONException {
        var marshalledMessageMap = new HashMap<String, Object>();
        marshalledMessageMap.put(ACTION_KEY, ACTION_VALUE);
        marshalledMessageMap.put(PAGE_KEY, PAGE_VALUE);
        marshalledMessageMap.put(MSISDN_KEY, MSISDN_VALUE);
        marshalledMessageMap.put(ENRICHMENT_KEY, new Client("Vasya", "Ivanov"));

        var json = jsonMarshaller.unmarshall(marshalledMessageMap);

        JSONAssert.assertEquals(ENRICHMENT_JSON, json, JSONCompareMode.STRICT);
        JSONAssert.assertEquals(ENRICHMENT_JSON, json, JSONCompareMode.LENIENT);
    }
}
