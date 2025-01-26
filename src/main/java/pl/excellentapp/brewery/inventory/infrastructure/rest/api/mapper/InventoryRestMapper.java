package pl.excellentapp.brewery.inventory.infrastructure.rest.api.mapper;

import org.mapstruct.Mapper;
import pl.excellentapp.brewery.inventory.domain.Inventory;
import pl.excellentapp.brewery.inventory.infrastructure.rest.api.dto.InventoryRequest;
import pl.excellentapp.brewery.inventory.infrastructure.rest.api.dto.InventoryResponse;
import pl.excellentapp.brewery.inventory.infrastructure.rest.api.dto.InventorysResponse;

import java.util.List;

@Mapper
public interface InventoryRestMapper {

    Inventory map(InventoryRequest inventoryRequest);

    InventoryResponse map(Inventory inventory);

    List<InventoryResponse> mapInventorys(List<Inventory> all);

    default InventorysResponse map(List<Inventory> all) {
        return InventorysResponse.builder()
                .inventorys(mapInventorys(all))
                .build();
    }
}
