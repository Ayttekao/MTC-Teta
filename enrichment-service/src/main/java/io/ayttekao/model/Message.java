package io.ayttekao.model;

import lombok.Data;

@Data
public class Message {
    private final String content;
    private final EnrichmentType enrichmentType;
}