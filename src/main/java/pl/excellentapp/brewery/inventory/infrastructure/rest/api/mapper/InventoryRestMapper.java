package pl.excellentapp.brewery.inventory.infrastructure.rest.api.mapper;

import org.mapstruct.Mapper;
import pl.excellentapp.brewery.inventory.domain.beerinventory.BeerInventory;
import pl.excellentapp.brewery.inventory.infrastructure.rest.api.dto.InventoryRequest;
import pl.excellentapp.brewery.inventory.infrastructure.rest.api.dto.InventoryResponse;
import pl.excellentapp.brewery.inventory.infrastructure.rest.api.dto.InventorysResponse;

import java.util.List;

@Mapper
public interface InventoryRestMapper {

    BeerInventory map(InventoryRequest inventoryRequest);

    InventoryResponse map(BeerInventory beerInventory);

    List<InventoryResponse> mapInventorys(List<BeerInventory> all);

    default InventorysResponse map(List<BeerInventory> all) {
        return InventorysResponse.builder()
                .inventorys(mapInventorys(all))
                .build();
    }
}
