package io.ayttekao.marshaller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public class JSONMarshaller implements MessageMarshaller {
    private final ObjectMapper mapper;

    @Override
    public Map<String, String> marshall(String message) {
        TypeReference<Map<String, String>> typeRef = new TypeReference<>() {};

        try {
            return mapper.readValue(message, typeRef);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public String unmarshall(Map<String, String> map) {
        try {
            return mapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
