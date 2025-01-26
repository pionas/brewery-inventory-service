package pl.excellentapp.brewery.inventory.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InventoryRepository {

    List<Inventory> findAll();

    Inventory save(Inventory inventory);

    Optional<Inventory> findById(UUID id);

    void deleteById(UUID id);
}
