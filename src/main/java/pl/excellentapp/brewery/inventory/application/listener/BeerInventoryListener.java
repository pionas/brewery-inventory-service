package pl.excellentapp.brewery.inventory.application.listener;

import jakarta.jms.JMSException;
import jakarta.jms.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import pl.excellentapp.brewery.model.events.BeerInventoryEvent;
import pl.excellentapp.brewery.inventory.application.BeerInventoryService;

import java.util.UUID;
import java.util.function.BiConsumer;

@RequiredArgsConstructor
@Component
@Slf4j
class BeerInventoryListener {

    private final JmsTemplate jmsTemplate;
    private final BeerInventoryService beerInventoryService;

    @JmsListener(destination = "${queue.inventory.add-stock}")
    public void addStock(@Payload BeerInventoryEvent beerInventoryEvent,
                         Message jmsMessage) throws JMSException {
        processStockOperation(
                beerInventoryEvent,
                jmsMessage,
                beerInventoryService::addStock
        );
    }

    @JmsListener(destination = "${queue.inventory.reserve-stock}")
    public void reserveStock(@Payload BeerInventoryEvent beerInventoryEvent,
                             Message jmsMessage) throws JMSException {
        processStockOperation(
                beerInventoryEvent,
                jmsMessage,
                beerInventoryService::reserveStock
        );
    }

    @JmsListener(destination = "${queue.inventory.release-stock}")
    public void releaseStock(@Payload BeerInventoryEvent beerInventoryEvent,
                             Message jmsMessage) throws JMSException {
        processStockOperation(
                beerInventoryEvent,
                jmsMessage,
                beerInventoryService::releaseStock
        );
    }

    private void processStockOperation(BeerInventoryEvent beerInventoryEvent,
                                       Message jmsMessage,
                                       BiConsumer<UUID, Integer> stockOperation) throws JMSException {
        final var responseEvent = new BeerInventoryEventResponse();
        try {
            stockOperation.accept(beerInventoryEvent.getBeerId(), beerInventoryEvent.getStock());
            responseEvent.success();
        } catch (Exception e) {
            responseEvent.failed(e.getMessage());
        }
        jmsTemplate.convertAndSend(jmsMessage.getJMSReplyTo(), responseEvent);
    }
}
