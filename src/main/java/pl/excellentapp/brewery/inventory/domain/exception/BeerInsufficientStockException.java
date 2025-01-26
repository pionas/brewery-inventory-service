package pl.excellentapp.brewery.inventory.domain.exception;

public class BeerInsufficientStockException extends RuntimeException {

    public BeerInsufficientStockException(String message) {
        super(message);
    }
}
