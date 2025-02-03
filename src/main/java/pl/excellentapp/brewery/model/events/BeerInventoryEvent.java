package pl.excellentapp.brewery.model.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BeerInventoryEvent {

    private UUID orderId;
    private UUID beerId;
    private Integer stock;
}
