package pl.excellentapp.brewery.inventory.application;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.excellentapp.brewery.inventory.domain.Inventory;
import pl.excellentapp.brewery.inventory.domain.InventoryRepository;
import pl.excellentapp.brewery.inventory.domain.exception.InventoryNotFoundException;
import pl.excellentapp.brewery.inventory.utils.DateTimeProvider;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final DateTimeProvider dateTimeProvider;

    @Override
    public List<Inventory> findAll() {
        return inventoryRepository.findAll();
    }

    @Override
    public Optional<Inventory> findById(UUID id) {
        return inventoryRepository.findById(id);
    }

    @Override
    public Inventory create(Inventory inventory) {
        return inventoryRepository.save(inventory);
    }

    @Override
    public Inventory update(UUID inventoryId, Inventory inventory) {
        final var currentInventory = getInventoryById(inventoryId);
        // TODO
        return inventoryRepository.save(currentInventory);
    }

    @Override
    public void delete(UUID inventoryId) {
        getInventoryById(inventoryId);
        inventoryRepository.deleteById(inventoryId);
    }

    private Inventory getInventoryById(UUID inventoryId) {
        return inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new InventoryNotFoundException("Inventory Not Found. UUID: " + inventoryId));
    }
}
