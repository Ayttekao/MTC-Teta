package marshaller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.ayttekao.exception.MarshallException;
import io.ayttekao.marshaller.JSONMarshaller;
import io.ayttekao.marshaller.MessageMarshaller;
import io.ayttekao.model.Client;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import java.util.Arrays;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class JSONMarshallerTest {
    private static final String ACTION_KEY = "action";
    private static final String PAGE_KEY = "page";
    private static final String MSISDN_KEY = "msisdn";
    private static final String ACTION_VALUE = "button_click";
    private static final String PAGE_VALUE = "book_card";
    private static final String MSISDN_VALUE = "88005553535";
    private static final String ENRICHMENT_KEY = "enrichment";
    private static final String VALID_JSON = """
            {
            "action":"button_click",
            "page":"book_card",
            "msisdn":"88005553535"
            }""";
    private static final String ENRICHMENT_JSON = """
            {
            "action": "button_click",
            "page": "book_card",
            "msisdn": "88005553535",
            "enrichment": {
            "firstName": "Vasya",
            "lastName": "Ivanov"
            }
            }""";
    private static final String INVALID_JSON = "Invalid_Json";
    private static MessageMarshaller jsonMarshaller;

    @BeforeAll
    static void beforeAll() {
        jsonMarshaller = new JSONMarshaller(new ObjectMapper());
    }

    @Test
    void shouldMarshallToMapWhenValidJson() {
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
    void shouldMarshallToMapWhenNestedJson() {
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
    void shouldThrowExceptionWhenInvalidJson() {

        var thrown = assertThrows(
                RuntimeException.class, () -> jsonMarshaller.marshall(INVALID_JSON), "RuntimeException was expected"
        );

        assertEquals(MarshallException.class, thrown.getClass());
    }

    @Test
    void shouldUnmarshallMapToValidJson() throws JSONException {
        var marshalledMessageMap = new HashMap<String, Object>();
        marshalledMessageMap.put(ACTION_KEY, ACTION_VALUE);
        marshalledMessageMap.put(PAGE_KEY, PAGE_VALUE);
        marshalledMessageMap.put(MSISDN_KEY, MSISDN_VALUE);

        var json = jsonMarshaller.unmarshall(marshalledMessageMap);

        JSONAssert.assertEquals(VALID_JSON, json, JSONCompareMode.STRICT);
        JSONAssert.assertEquals(VALID_JSON, json, JSONCompareMode.LENIENT);
    }

    @Test
    void shouldUnmarshallMapToNestedJson() throws JSONException {
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
