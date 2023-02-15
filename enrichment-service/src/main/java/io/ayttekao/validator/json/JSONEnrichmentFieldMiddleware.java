package io.ayttekao.validator.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.ayttekao.model.Message;
import io.ayttekao.validator.Middleware;

public class JSONEnrichmentFieldMiddleware extends Middleware {
    @Override
    public Boolean check(Message message) {
        var node = tryGetNodeOrEmpty(message.getContent());

        if (node.has(message.getEnrichmentType().toString())) {
            return checkNext(message);
        }

        return false;
    }

    private ObjectNode tryGetNodeOrEmpty(String message) {
        try {
            return new ObjectMapper().readValue(message, ObjectNode.class);
        } catch (JsonProcessingException e) {
            return new ObjectMapper().createObjectNode();
        }

    }
}
