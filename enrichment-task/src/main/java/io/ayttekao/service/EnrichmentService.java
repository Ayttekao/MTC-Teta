package io.ayttekao.service;

import io.ayttekao.dao.ClientDao;
import io.ayttekao.model.Message;
import io.ayttekao.validator.MessageValidator;

public class EnrichmentService {
    private final MessageValidator validator;
    private final ClientDao clientDao;

    public EnrichmentService(MessageValidator validator, ClientDao clientDao) {
        this.validator = validator;
        this.clientDao = clientDao;
    }

    public String enrich(Message message) {
        if (validator.isValid(message)) {
            return "Not implemented yet";
        } else {
            return message.getContent();
        }
    }
}
