package io.ayttekao.validator;

import io.ayttekao.model.Message;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MessageValidatorImpl implements MessageValidator {
    private final Middleware middleware;

    @Override
    public boolean isValid(Message message) {
        return middleware.check(message);
    }

}
