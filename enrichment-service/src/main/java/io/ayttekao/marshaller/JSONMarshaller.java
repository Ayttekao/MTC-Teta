package io.ayttekao.marshaller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class JSONMarshaller implements MessageMarshaller {
    private final ObjectMapper mapper;

    @Override
    public HashMap<String, Object> marshall(String message) {
        TypeReference<HashMap<String, Object>> typeRef = new TypeReference<>() {};

        try {
            return mapper.readValue(message, typeRef);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public String unmarshall(Map<String, Object> marshalledMessageMap) {
        try {
            return mapper.writeValueAsString(marshalledMessageMap);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
