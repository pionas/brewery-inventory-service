package pl.excellentapp.brewery.inventory.infrastructure.persistence.jpa;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.excellentapp.brewery.inventory.domain.beerinventory.BeerInventory;
import pl.excellentapp.brewery.inventory.domain.beerinventory.BeerInventoryRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@AllArgsConstructor
class JpaBeerInventoryRepository implements BeerInventoryRepository {

    private final SpringJpaInventoryRepository springJpaInventoryRepository;
    private final InventoryMapper inventoryMapper;

    @Override
    public List<BeerInventory> findAll() {
        return inventoryMapper.map(springJpaInventoryRepository.findAll());
    }

    @Override
    public BeerInventory save(BeerInventory beerInventory) {
        return Optional.of(beerInventory)
                .map(inventoryMapper::map)
                .map(springJpaInventoryRepository::save)
                .map(inventoryMapper::map)
                .orElseThrow(IllegalStateException::new);
    }

    @Override
    public Optional<BeerInventory> findById(UUID id) {
        return springJpaInventoryRepository.findById(id)
                .map(inventoryMapper::map);
    }

    @Override
    public void deleteById(UUID id) {
        springJpaInventoryRepository.deleteById(id);
    }
}
