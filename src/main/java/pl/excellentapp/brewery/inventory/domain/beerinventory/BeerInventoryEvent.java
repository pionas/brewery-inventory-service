package pl.excellentapp.brewery.inventory.domain.beerinventory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode(exclude = "beerInventory")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BeerInventoryEvent {

    private UUID id;
    private BeerHistoryType eventType;
    private int quantity;
    private OffsetDateTime createdDate;
    private BeerInventory beerInventory;

    public static BeerInventoryEvent brewed(int quantity, OffsetDateTime now, BeerInventory beerInventory) {
        return new BeerInventoryEvent(BeerHistoryType.BREWED, quantity, now, beerInventory);
    }

    public static BeerInventoryEvent reserved(int quantity, OffsetDateTime now, BeerInventory beerInventory) {
        return new BeerInventoryEvent(BeerHistoryType.RESERVED, quantity, now, beerInventory);
    }

    public static BeerInventoryEvent cancelled(int quantity, OffsetDateTime now, BeerInventory beerInventory) {
        return new BeerInventoryEvent(BeerHistoryType.CANCELLED, quantity, now, beerInventory);
    }

    private BeerInventoryEvent(BeerHistoryType eventType, int quantity, OffsetDateTime createdDate, BeerInventory beerInventory) {
        this.eventType = eventType;
        this.quantity = quantity;
        this.createdDate = createdDate;
        this.beerInventory = beerInventory;
    }
}
