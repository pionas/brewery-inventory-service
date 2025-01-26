package pl.excellentapp.brewery.inventory.infrastructure.rest.api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryStockRequest {

    @NotNull(message = "Stock cannot be null")
    private Integer stock;
}
