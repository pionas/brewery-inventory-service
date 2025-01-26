package pl.excellentapp.brewery.inventory.infrastructure.rest.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventorysResponse {

    private List<InventoryResponse> inventorys;
}
