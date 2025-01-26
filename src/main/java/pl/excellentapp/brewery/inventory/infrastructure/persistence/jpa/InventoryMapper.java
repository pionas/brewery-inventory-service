package pl.excellentapp.brewery.inventory.infrastructure.persistence.jpa;

import org.mapstruct.Mapper;
import pl.excellentapp.brewery.inventory.domain.beerinventory.BeerInventory;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.List;

@Mapper
interface InventoryMapper {

    List<BeerInventory> map(Iterable<BeerInventoryEntity> inventoryEntities);

    BeerInventory map(BeerInventoryEntity beerInventoryEntity);

    BeerInventoryEntity map(BeerInventory beerInventory);

    default Timestamp map(OffsetDateTime value) {
        return value == null ? null : Timestamp.from(value.toInstant());
    }

    default OffsetDateTime map(Timestamp value) {
        return value == null ? null : value.toInstant()
                .atOffset(OffsetDateTime.now().getOffset());
    }
}
