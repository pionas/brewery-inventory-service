package pl.excellentapp.brewery.inventory.application.listener;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class BeerInventoryEventResponse implements Serializable {

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
