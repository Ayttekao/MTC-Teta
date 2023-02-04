package io.ayttekao.validator.json;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.ayttekao.model.Message;
import io.ayttekao.validator.Middleware;

public class JSONFormatMiddleware extends Middleware {
    private final ObjectMapper mapper = new ObjectMapper()
            .enable(DeserializationFeature.FAIL_ON_TRAILING_TOKENS);

    @Override
    public Boolean check(Message message) {
        try {
            mapper.readTree(message.getContent());
        } catch (JacksonException e) {
            return false;
        }

        return checkNext(message);
    }
}
