package io.ayttekao.marshaller;

import java.util.Map;

public interface MessageMarshaller {
    Map<String, String> marshall(String message);

    String unmarshall(Map<String, String> map);
}
