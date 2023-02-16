package io.ayttekao.service;

import io.ayttekao.dao.ClientDao;
import io.ayttekao.marshaller.MessageMarshaller;
import io.ayttekao.model.Message;
import io.ayttekao.validator.MessageValidator;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EnrichmentServiceImpl implements EnrichmentService {
    private static final String ENRICHMENT_KEY = "enrichment";
    private final MessageMarshaller messageMarshaller;
    private final MessageValidator validator;
    private final ClientDao clientDao;

    @Override
    public String enrich(Message message) {
        String response;

        if (validator.isValid(message)) {
            var marshalledMessageMap = messageMarshaller.marshall(message.getContent());
            marshalledMessageMap.remove(ENRICHMENT_KEY);
            var msisdn = marshalledMessageMap.get(message.getEnrichmentType().toString());
            clientDao.findByMsisdn(Long.valueOf(msisdn.toString())).ifPresent(theUser -> {
                marshalledMessageMap.put(ENRICHMENT_KEY, theUser);
            });

            response = messageMarshaller.unmarshall(marshalledMessageMap);
        } else {
            response = message.getContent();
        }

        return response;
    }
}
