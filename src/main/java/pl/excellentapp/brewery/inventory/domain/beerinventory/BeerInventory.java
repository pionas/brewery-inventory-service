package pl.excellentapp.brewery.inventory.domain.beerinventory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.excellentapp.brewery.inventory.domain.exception.BeerInsufficientStockException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BeerInventory {

    private UUID id;
    private UUID beerId;
    private int availableStock;
    private List<BeerInventoryEvent> history;

    public void addStock(int quantity, LocalDateTime now) {
        this.availableStock += quantity;
        history.add(new BeerInventoryEvent(BeerHistoryType.BREWED, quantity, now));
    }

    public void reserveStock(int quantity, LocalDateTime now) throws BeerInsufficientStockException {
        if (this.availableStock < quantity) {
            throw new BeerInsufficientStockException("Not enough stock to reserve.");
        }
        this.availableStock -= quantity;
        history.add(new BeerInventoryEvent(BeerHistoryType.RESERVED, quantity, now));
    }

    public void releaseStock(int quantity, LocalDateTime now) {
        this.availableStock += quantity;
        history.add(new BeerInventoryEvent(BeerHistoryType.CANCELLED, quantity, now));
    }
}
