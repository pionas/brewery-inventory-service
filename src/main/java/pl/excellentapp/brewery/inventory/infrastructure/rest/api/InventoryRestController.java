package pl.excellentapp.brewery.inventory.infrastructure.rest.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.excellentapp.brewery.inventory.application.BeerInventoryService;
import pl.excellentapp.brewery.inventory.infrastructure.rest.api.dto.InventoryRequest;
import pl.excellentapp.brewery.inventory.infrastructure.rest.api.dto.InventoryResponse;
import pl.excellentapp.brewery.inventory.infrastructure.rest.api.dto.InventoryStockRequest;
import pl.excellentapp.brewery.inventory.infrastructure.rest.api.dto.InventoriesResponse;
import pl.excellentapp.brewery.inventory.infrastructure.rest.api.mapper.InventoryRestMapper;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/inventories")
class InventoryRestController {

    private final BeerInventoryService beerInventoryService;
    private final InventoryRestMapper inventoryRestMapper;

    @GetMapping({"", "/"})
    public ResponseEntity<InventoriesResponse> getInventories() {
        return new ResponseEntity<>(inventoryRestMapper.map(beerInventoryService.findAll()), HttpStatus.OK);
    }

    @GetMapping("/{beerId}")
    public ResponseEntity<InventoryResponse> getInventory(@PathVariable("beerId") UUID beerId) {
        return beerInventoryService.findById(beerId)
                .map(inventoryRestMapper::map)
                .map(inventoryResponse -> new ResponseEntity<>(inventoryResponse, HttpStatus.OK))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<InventoryResponse> createInventory(@Valid @RequestBody InventoryRequest inventoryRequest) {
        final var inventoryResponse = inventoryRestMapper.map(beerInventoryService.create(inventoryRequest.getBeerId(), inventoryRequest.getAvailableStock()));

        return new ResponseEntity<>(inventoryResponse, HttpStatus.CREATED);
    }

    @PostMapping("/{beerId}/add-stock")
    public ResponseEntity<InventoryResponse> addStock(@PathVariable("beerId") UUID beerId, @Valid @RequestBody InventoryStockRequest inventoryStockRequest) {
        final var inventoryResponse = inventoryRestMapper.map(beerInventoryService.addStock(beerId, inventoryStockRequest.getStock()));
        return new ResponseEntity<>(inventoryResponse, HttpStatus.OK);
    }

    @PostMapping("/{beerId}/reserve-stock")
    public ResponseEntity<InventoryResponse> reserveStock(@PathVariable("beerId") UUID beerId, @Valid @RequestBody InventoryStockRequest inventoryStockRequest) {
        final var inventoryResponse = inventoryRestMapper.map(beerInventoryService.reserveStock(beerId, inventoryStockRequest.getStock()));
        return new ResponseEntity<>(inventoryResponse, HttpStatus.OK);
    }

    @PostMapping("/{beerId}/release-stock")
    public ResponseEntity<InventoryResponse> releaseStock(@PathVariable("beerId") UUID beerId, @Valid @RequestBody InventoryStockRequest inventoryStockRequest) {
        final var inventoryResponse = inventoryRestMapper.map(beerInventoryService.releaseStock(beerId, inventoryStockRequest.getStock()));
        return new ResponseEntity<>(inventoryResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{beerId}")
    public ResponseEntity<HttpStatus> deleteInventory(@PathVariable("beerId") UUID beerId) {
        beerInventoryService.delete(beerId);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

}
