package io.ayttekao.service;

import io.ayttekao.model.Message;

public interface EnrichmentService {
    String enrich(Message message);
}
