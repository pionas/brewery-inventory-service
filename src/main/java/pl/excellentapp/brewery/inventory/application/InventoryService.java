package pl.excellentapp.brewery.inventory.application;

import pl.excellentapp.brewery.inventory.domain.Inventory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InventoryService {

    List<Inventory> findAll();

    Optional<Inventory> findById(UUID id);

    Inventory create(Inventory inventory);

    Inventory update(UUID inventoryId, Inventory inventory);

    void delete(UUID inventoryId);
}
