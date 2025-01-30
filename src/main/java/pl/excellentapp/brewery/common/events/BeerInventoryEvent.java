package pl.excellentapp.brewery.common.events;

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

    private UUID beerId;
    private Integer stock;
}
