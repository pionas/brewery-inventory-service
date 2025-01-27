package pl.excellentapp.brewery.inventory.infrastructure.rest.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.excellentapp.brewery.inventory.domain.beerinventory.BeerInventory;
import pl.excellentapp.brewery.inventory.infrastructure.rest.api.dto.InventoryRequest;
import pl.excellentapp.brewery.inventory.infrastructure.rest.api.dto.InventoryResponse;
import pl.excellentapp.brewery.inventory.infrastructure.rest.api.dto.InventoriesResponse;

import java.util.List;

@Mapper
public interface InventoryRestMapper {

    BeerInventory map(InventoryRequest inventoryRequest);

    @Mapping(target = "id", source = "beerId")
    InventoryResponse map(BeerInventory beerInventory);

    List<InventoryResponse> mapInventories(List<BeerInventory> all);

    default InventoriesResponse map(List<BeerInventory> all) {
        return InventoriesResponse.builder()
                .inventories(mapInventories(all))
                .build();
    }
}
