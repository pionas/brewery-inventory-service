package pl.excellentapp.brewery.inventory.domain.beerinventory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BeerInventoryEvent {

    private UUID id;
    private BeerHistoryType eventType;
    private int quantity;
    private LocalDateTime createdDate;
    private BeerInventory beerInventory;

    public static BeerInventoryEvent brewed(int quantity, LocalDateTime now, BeerInventory beerInventory) {
        return new BeerInventoryEvent(BeerHistoryType.BREWED, quantity, now, beerInventory);
    }

    public static BeerInventoryEvent reserved(int quantity, LocalDateTime now, BeerInventory beerInventory) {
        return new BeerInventoryEvent(BeerHistoryType.RESERVED, quantity, now, beerInventory);
    }

    public static BeerInventoryEvent cancelled(int quantity, LocalDateTime now, BeerInventory beerInventory) {
        return new BeerInventoryEvent(BeerHistoryType.CANCELLED, quantity, now, beerInventory);
    }

    private BeerInventoryEvent(BeerHistoryType eventType, int quantity, LocalDateTime createdDate, BeerInventory beerInventory) {
        this.eventType = eventType;
        this.quantity = quantity;
        this.createdDate = createdDate;
        this.beerInventory = beerInventory;
    }
}
