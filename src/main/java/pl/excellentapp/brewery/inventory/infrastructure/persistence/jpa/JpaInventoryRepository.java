package pl.excellentapp.brewery.inventory.infrastructure.persistence.jpa;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.excellentapp.brewery.inventory.domain.Inventory;
import pl.excellentapp.brewery.inventory.domain.InventoryRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@AllArgsConstructor
class JpaInventoryRepository implements InventoryRepository {

    private final SpringJpaInventoryRepository springJpaInventoryRepository;
    private final InventoryMapper inventoryMapper;

    @Override
    public List<Inventory> findAll() {
        return inventoryMapper.map(springJpaInventoryRepository.findAll());
    }

    @Override
    public Inventory save(Inventory inventory) {
        return inventoryMapper.map(springJpaInventoryRepository.save(inventoryMapper.map(inventory)));
    }

    @Override
    public Optional<Inventory> findById(UUID id) {
        return springJpaInventoryRepository.findById(id)
                .map(inventoryMapper::map);
    }

    @Override
    public void deleteById(UUID id) {
        springJpaInventoryRepository.deleteById(id);
    }
}
