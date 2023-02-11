package io.ayttekao.validator;

import io.ayttekao.model.Message;

public interface MessageValidator {
    Boolean isValid(Message message);
}
