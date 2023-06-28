package io.ayttekao.marshaller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.ayttekao.exception.MarshallException;
import io.ayttekao.model.EnrichRequest;
import io.ayttekao.model.EnrichResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JSONMarshaller implements MessageMarshaller {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public EnrichRequest marshall(String message) {
        try {
            return MAPPER.readValue(message, EnrichRequest.class);
        } catch (JsonProcessingException e) {
            throw new MarshallException("Marshall process exception", e.getCause());
        }
    }

    @Override
    public String unmarshall(EnrichResponse response) {
        try {
            return MAPPER.writeValueAsString(response);
        } catch (JsonProcessingException e) {
            throw new MarshallException("Unmarshall process exception", e.getCause());
        }
    }
}
