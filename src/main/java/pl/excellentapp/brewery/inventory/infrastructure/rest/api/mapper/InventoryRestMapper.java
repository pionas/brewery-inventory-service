package pl.excellentapp.brewery.inventory.infrastructure.rest.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.excellentapp.brewery.inventory.domain.beerinventory.BeerInventory;
import pl.excellentapp.brewery.inventory.infrastructure.rest.api.dto.InventoryRequest;
import pl.excellentapp.brewery.inventory.infrastructure.rest.api.dto.InventoryResponse;
import pl.excellentapp.brewery.inventory.infrastructure.rest.api.dto.InventorysResponse;

import java.util.List;

@Mapper
public interface InventoryRestMapper {

    BeerInventory map(InventoryRequest inventoryRequest);

    @Mapping(target = "id", source = "beerId")
    InventoryResponse map(BeerInventory beerInventory);

    List<InventoryResponse> mapInventories(List<BeerInventory> all);

    default InventorysResponse map(List<BeerInventory> all) {
        return InventorysResponse.builder()
                .inventorys(mapInventories(all))
                .build();
    }
}
