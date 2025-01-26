package pl.excellentapp.brewery.inventory.domain.beerinventory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BeerInventoryRepository {

    List<BeerInventory> findAll();

    BeerInventory save(BeerInventory beerInventory);

    Optional<BeerInventory> findById(UUID id);

    void deleteById(UUID id);
}
