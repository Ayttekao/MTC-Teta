package io.ayttekao.service;

import io.ayttekao.marshaller.MessageMarshaller;
import io.ayttekao.model.Client;
import io.ayttekao.model.EnrichRequest;
import io.ayttekao.model.EnrichResponse;
import io.ayttekao.model.Message;
import io.ayttekao.repository.ClientRepository;
import io.ayttekao.repository.EnrichRepository;
import io.ayttekao.validator.MessageValidator;
import lombok.RequiredArgsConstructor;

import static io.ayttekao.mapper.EnrichMapper.toEnrichResponse;

@RequiredArgsConstructor
public class EnrichmentServiceImpl implements EnrichmentService {
    private final MessageMarshaller messageMarshaller;
    private final MessageValidator validator;
    private final ClientRepository clientRepository;
    private final EnrichRepository<Message> enrichedRepository;
    private final EnrichRepository<Message> nonEnrichedRepository;

    @Override
    public String enrich(Message message) {
        var messageContent = message.getContent();

        if (!validator.isValid(message)) {
            handleNonValidMessage(message);
            return messageContent;
        }

        return processValidMessage(message);
    }

    private void handleNonValidMessage(Message message) {
        nonEnrichedRepository.add(message);
    }

    private String processValidMessage(Message message) {
        var request = marshallMessageContent(message.getContent());
        var msisdn = request.getMsisdn();
        var clientOptional = clientRepository.findByMsisdn(msisdn);

        if (clientOptional.isEmpty()) {
            handleNonEnrichableMessage(message);
            return message.getContent();
        }

        return processEnrichableMessage(message, request, clientOptional.get());
    }

    private void handleNonEnrichableMessage(Message message) {
        nonEnrichedRepository.add(message);
    }

    private String processEnrichableMessage(Message message, EnrichRequest request, Client client) {
        var response = toEnrichResponse(request, client);
        var enrichResponse = unmarshallResponse(response);
        var enrichMessage = new Message(enrichResponse, message.getEnrichmentType());
        enrichedRepository.add(enrichMessage);

        return enrichResponse;
    }

    private EnrichRequest marshallMessageContent(String messageContent) {
        return messageMarshaller.marshall(messageContent);
    }

    private String unmarshallResponse(EnrichResponse response) {
        return messageMarshaller.unmarshall(response);
    }
}
