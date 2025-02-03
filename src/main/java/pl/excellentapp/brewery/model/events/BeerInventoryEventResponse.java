package pl.excellentapp.brewery.model.events;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BeerInventoryEventResponse implements Serializable {

    private final UUID orderId;
    private final UUID beerId;
    private final Integer stock;
    private Boolean success;
    private String message;

    public void success() {
        this.success = true;
    }

    public void failed(String message) {
        this.success = false;
        this.message = message;
    }
}
