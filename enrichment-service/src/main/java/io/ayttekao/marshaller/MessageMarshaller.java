package io.ayttekao.marshaller;

import io.ayttekao.model.EnrichRequest;
import io.ayttekao.model.EnrichResponse;

public interface MessageMarshaller {
    EnrichRequest marshall(String message);

    String unmarshall(EnrichResponse response);
}
