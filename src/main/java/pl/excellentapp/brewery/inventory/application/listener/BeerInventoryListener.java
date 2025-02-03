package pl.excellentapp.brewery.inventory.application.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import pl.excellentapp.brewery.inventory.application.BeerInventoryService;
import pl.excellentapp.brewery.model.events.BeerInventoryEvent;
import pl.excellentapp.brewery.model.events.BeerInventoryEventResponse;

import java.util.UUID;
import java.util.function.BiConsumer;

@Component
@Slf4j
class BeerInventoryListener {

    private final JmsTemplate jmsTemplate;
    private final BeerInventoryService beerInventoryService;
    private final String allocateOrderResponseQueueName;

    public BeerInventoryListener(BeerInventoryService beerInventoryService, JmsTemplate jmsTemplate, @Value("${queue.order.allocate-response}") String allocateOrderResponseQueueName) {
        this.beerInventoryService = beerInventoryService;
        this.jmsTemplate = jmsTemplate;
        this.allocateOrderResponseQueueName = allocateOrderResponseQueueName;
    }


    @JmsListener(destination = "${queue.inventory.add-stock}")
    public void addStock(@Payload BeerInventoryEvent beerInventoryEvent) {
        processStockOperation(
                beerInventoryEvent,
                beerInventoryService::addStock
        );
    }

    @JmsListener(destination = "${queue.inventory.reserve-stock}")
    public void reserveStock(@Payload BeerInventoryEvent beerInventoryEvent) {
        processStockOperation(
                beerInventoryEvent,
                beerInventoryService::reserveStock
        );
    }

    @JmsListener(destination = "${queue.inventory.release-stock}")
    public void releaseStock(@Payload BeerInventoryEvent beerInventoryEvent) {
        processStockOperation(
                beerInventoryEvent,
                beerInventoryService::releaseStock
        );
    }

    private void processStockOperation(BeerInventoryEvent beerInventoryEvent,
                                       BiConsumer<UUID, Integer> stockOperation) {
        final var responseEvent = BeerInventoryEventResponse.builder()
                .orderId(beerInventoryEvent.getOrderId())
                .beerId(beerInventoryEvent.getBeerId())
                .stock(beerInventoryEvent.getStock());
        try {
            stockOperation.accept(beerInventoryEvent.getBeerId(), beerInventoryEvent.getStock());
            responseEvent.success(true);
        } catch (Exception e) {
            responseEvent.success(false);
            responseEvent.message(e.getMessage());
        }
        jmsTemplate.convertAndSend(allocateOrderResponseQueueName, responseEvent.build());
    }
}
