package io.ayttekao.mapper;

import io.ayttekao.model.Client;
import io.ayttekao.model.EnrichRequest;
import io.ayttekao.model.EnrichResponse;

public class EnrichMapper {

    private EnrichMapper() {
    }

    public static EnrichResponse toEnrichResponse(EnrichRequest request, Client client) {
        var response = new EnrichResponse();
        response.setAction(request.getAction());
        response.setPage(request.getPage());
        response.setMsisdn(request.getMsisdn());
        response.setClient(client);

        return response;
    }
}
