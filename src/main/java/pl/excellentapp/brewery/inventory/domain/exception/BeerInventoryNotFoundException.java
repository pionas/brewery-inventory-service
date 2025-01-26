package pl.excellentapp.brewery.inventory.domain.exception;

public class BeerInventoryNotFoundException extends RuntimeException {

    public BeerInventoryNotFoundException(String message) {
        super(message);
    }
}
