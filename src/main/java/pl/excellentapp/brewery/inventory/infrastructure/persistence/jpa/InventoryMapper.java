package pl.excellentapp.brewery.inventory.infrastructure.persistence.jpa;

import org.mapstruct.Mapper;
import pl.excellentapp.brewery.inventory.domain.Inventory;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.List;

@Mapper
interface InventoryMapper {

    List<Inventory> map(Iterable<InventoryEntity> inventoryEntities);

    Inventory map(InventoryEntity inventoryEntity);

    InventoryEntity map(Inventory inventory);

    default Timestamp map(OffsetDateTime value) {
        return value == null ? null : Timestamp.from(value.toInstant());
    }

    default OffsetDateTime map(Timestamp value) {
        return value == null ? null : value.toInstant()
                .atOffset(OffsetDateTime.now().getOffset());
    }
}
