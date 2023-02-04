package io.ayttekao.validator;

import io.ayttekao.model.Message;

public class MessageValidatorImpl implements MessageValidator {
    private Middleware middleware;

    public MessageValidatorImpl(Middleware middleware) {
        this.middleware = middleware;
    }

    @Override
    public Boolean isValid(Message message) {
        return middleware.check(message);
    }

}
