package pl.excellentapp.brewery.inventory.infrastructure.persistence.jpa;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
interface SpringJpaInventoryRepository extends CrudRepository<InventoryEntity, UUID> {
}
