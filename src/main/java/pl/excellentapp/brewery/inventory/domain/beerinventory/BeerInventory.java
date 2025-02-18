package pl.excellentapp.brewery.inventory.domain.beerinventory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.excellentapp.brewery.inventory.domain.exception.BeerInsufficientStockException;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode(exclude = "history")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BeerInventory {

    private UUID beerId;
    private int availableStock;
    @Builder.Default
    private List<BeerInventoryEvent> history = new ArrayList<>();

    public BeerInventory(UUID beerId) {
        this.beerId = beerId;
    }

    public void addStock(int quantity, OffsetDateTime now) {
        this.availableStock += quantity;
        history.add(BeerInventoryEvent.brewed(quantity, now, this));
    }

    public void reserveStock(int quantity, OffsetDateTime now) throws BeerInsufficientStockException {
        if (this.availableStock < quantity) {
            throw new BeerInsufficientStockException("Not enough stock to reserve.");
        }
        this.availableStock -= quantity;
        history.add(BeerInventoryEvent.reserved(quantity, now, this));
    }

    public void releaseStock(int quantity, OffsetDateTime now) {
        this.availableStock += quantity;
        history.add(BeerInventoryEvent.cancelled(quantity, now, this));
    }
}
