package pl.excellentapp.brewery.inventory.domain.beerinventory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BeerInventoryEvent {

    private BeerHistoryType eventType;
    private int quantity;
    private LocalDateTime createdDate;
}
