package pl.excellentapp.brewery.inventory.application;

import pl.excellentapp.brewery.inventory.domain.beerinventory.BeerInventory;
import pl.excellentapp.brewery.inventory.infrastructure.rest.api.dto.InventoryRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BeerInventoryService {

    List<BeerInventory> findAll();

    Optional<BeerInventory> findById(UUID id);

    BeerInventory create(UUID beerId, int availableStock);

    BeerInventory addStock(UUID beerId, int availableStock);

    BeerInventory reserveStock(UUID beerId, int quantity);

    BeerInventory releaseStock(UUID beerId, int quantity);

    void delete(UUID inventoryId);
}
