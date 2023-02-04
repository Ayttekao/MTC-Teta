package io.ayttekao.model;

public class Message {
    private String content;
    private EnrichmentType enrichmentType;

    public Message(String content, EnrichmentType enrichmentType) {
        this.content = content;
        this.enrichmentType = enrichmentType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public EnrichmentType getEnrichmentType() {
        return enrichmentType;
    }

    public void setEnrichmentType(EnrichmentType enrichmentType) {
        this.enrichmentType = enrichmentType;
    }
}