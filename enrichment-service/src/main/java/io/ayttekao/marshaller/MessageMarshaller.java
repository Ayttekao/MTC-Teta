package io.ayttekao.marshaller;

import java.util.Map;

public interface MessageMarshaller {
    Map<String, Object> marshall(String message);

    String unmarshall(Map<String, Object> map);
}
