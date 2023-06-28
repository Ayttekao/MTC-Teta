package io.ayttekao.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class EnrichResponse {
    @JsonProperty("action")
    private String action;
    @JsonProperty("page")
    private String page;
    @JsonProperty("msisdn")
    private String msisdn;
    @JsonProperty("enrichment")
    private Client client;
}
