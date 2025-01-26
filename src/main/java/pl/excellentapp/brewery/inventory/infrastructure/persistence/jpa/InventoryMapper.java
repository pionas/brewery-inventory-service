package pl.excellentapp.brewery.inventory.infrastructure.persistence.jpa;

import org.mapstruct.Mapper;
import pl.excellentapp.brewery.inventory.domain.beerinventory.BeerInventory;
import pl.excellentapp.brewery.inventory.domain.beerinventory.BeerInventoryEvent;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

@Mapper
interface InventoryMapper {

    List<BeerInventory> map(Iterable<BeerInventoryEntity> inventoryEntities);

    BeerInventory map(BeerInventoryEntity beerInventoryEntity);

    BeerInventoryEntity map(BeerInventory beerInventory);

    BeerInventoryEventEntity map(BeerInventoryEvent beerInventoryEvent);

    BeerInventoryEvent map(BeerInventoryEventEntity beerInventoryEventEntity);

    default Timestamp map(OffsetDateTime value) {
        return value == null ? null : Timestamp.from(value.toInstant());
    }

    default OffsetDateTime map(Timestamp value) {
        return value == null ? null : value.toInstant()
                .atOffset(OffsetDateTime.now().getOffset());
    }

    default Timestamp mapToTimestamp(LocalDateTime value) {
        return value == null ? null : Timestamp.valueOf(value);
    }

    default LocalDateTime mapToLocalDateTime(Timestamp value) {
        return value == null ? null : value.toLocalDateTime();
    }
}
