package pl.excellentapp.brewery.inventory.application;

import pl.excellentapp.brewery.inventory.domain.beerinventory.BeerInventory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BeerInventoryService {

    List<BeerInventory> findAll();

    Optional<BeerInventory> findById(UUID id);

    BeerInventory create(BeerInventory beerInventory);

    BeerInventory update(UUID inventoryId, BeerInventory beerInventory);

    void delete(UUID inventoryId);
}
