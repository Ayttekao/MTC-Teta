package io.ayttekao.service;

import io.ayttekao.model.Message;
import io.ayttekao.validator.MessageValidator;

public class EnrichmentService {
    private final MessageValidator validator;

    public EnrichmentService(MessageValidator validator) {
        this.validator = validator;
    }

    public String enrich(Message message) {
        return "Not implemented yet";
    }
}
