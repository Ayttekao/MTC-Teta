package io.ayttekao.marshaller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.ayttekao.exception.MarshallException;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class JSONMarshaller implements MessageMarshaller {
    private final ObjectMapper mapper;
    TypeReference<HashMap<String, Object>> typeRef = new TypeReference<>() {};

    @Override
    public HashMap<String, Object> marshall(String message) {
        try {
            return mapper.readValue(message, typeRef);
        } catch (JsonProcessingException e) {
            throw new MarshallException("Marshall process exception", e.getCause());
        }
    }

    @Override
    public String unmarshall(Map<String, Object> marshalledMessageMap) {
        try {
            return mapper.writeValueAsString(marshalledMessageMap);
        } catch (JsonProcessingException e) {
            throw new MarshallException("Unmarshall process exception", e.getCause());
        }
    }
}
