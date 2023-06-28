package io.ayttekao.validator;

import io.ayttekao.model.Message;

public interface MessageValidator {
    boolean isValid(Message message);
}
