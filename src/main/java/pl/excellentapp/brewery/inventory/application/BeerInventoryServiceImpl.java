package pl.excellentapp.brewery.inventory.application;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.excellentapp.brewery.inventory.domain.beerinventory.BeerInventory;
import pl.excellentapp.brewery.inventory.domain.beerinventory.BeerInventoryRepository;
import pl.excellentapp.brewery.inventory.domain.exception.BeerInventoryNotFoundException;
import pl.excellentapp.brewery.inventory.utils.DateTimeProvider;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class BeerInventoryServiceImpl implements BeerInventoryService {

    private final BeerInventoryRepository beerInventoryRepository;
    private final DateTimeProvider dateTimeProvider;

    @Override
    public List<BeerInventory> findAll() {
        return beerInventoryRepository.findAll();
    }

    @Override
    public Optional<BeerInventory> findById(UUID id) {
        return beerInventoryRepository.findById(id);
    }

    @Override
    public BeerInventory create(UUID beerId, int availableStock) {
        if (beerInventoryRepository.findById(beerId).isPresent()) {
            throw new IllegalStateException("Beer inventory exists");
        }
        final var beerInventory = getBeerInventory(beerId);
        beerInventory.addStock(availableStock, dateTimeProvider.now());
        return beerInventoryRepository.save(beerInventory);
    }

    @Override
    @Transactional
    public BeerInventory addStock(UUID beerId, int quantity) {
        final var beerInventory = beerInventoryRepository.findById(beerId).orElseGet(() -> getBeerInventory(beerId));
        beerInventory.addStock(quantity, dateTimeProvider.now());
        return beerInventoryRepository.save(beerInventory);
    }

    @Override
    @Transactional
    public BeerInventory reserveStock(UUID beerId, int quantity) {
        final var beerInventory = getInventoryById(beerId);
        beerInventory.reserveStock(quantity, dateTimeProvider.now());
        return beerInventoryRepository.save(beerInventory);
    }

    @Override
    @Transactional
    public BeerInventory releaseStock(UUID beerId, int quantity) {
        final var beerInventory = getInventoryById(beerId);
        beerInventory.releaseStock(quantity, dateTimeProvider.now());
        return beerInventoryRepository.save(beerInventory);
    }

    @Override
    public void delete(UUID inventoryId) {
        getInventoryById(inventoryId);
        beerInventoryRepository.deleteById(inventoryId);
    }

    private BeerInventory getInventoryById(UUID inventoryId) {
        return beerInventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new BeerInventoryNotFoundException("Inventory Not Found. UUID: " + inventoryId));
    }

    private BeerInventory getBeerInventory(UUID beerId) {
        return BeerInventory.builder()
                .beerId(beerId)
                .build();
    }
}
