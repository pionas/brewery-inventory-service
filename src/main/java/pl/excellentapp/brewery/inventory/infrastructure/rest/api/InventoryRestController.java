package pl.excellentapp.brewery.inventory.infrastructure.rest.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.excellentapp.brewery.inventory.application.InventoryService;
import pl.excellentapp.brewery.inventory.domain.exception.InventoryNotFoundException;
import pl.excellentapp.brewery.inventory.infrastructure.rest.api.dto.InventoryRequest;
import pl.excellentapp.brewery.inventory.infrastructure.rest.api.dto.InventoryResponse;
import pl.excellentapp.brewery.inventory.infrastructure.rest.api.dto.InventorysResponse;
import pl.excellentapp.brewery.inventory.infrastructure.rest.api.mapper.InventoryRestMapper;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/inventorys")
class InventoryRestController {

    private final InventoryService inventoryService;
    private final InventoryRestMapper inventoryRestMapper;

    @GetMapping({"", "/"})
    public ResponseEntity<InventorysResponse> getInventorys() {
        return new ResponseEntity<>(inventoryRestMapper.map(inventoryService.findAll()), HttpStatus.OK);
    }

    @GetMapping("/{inventoryId}")
    public ResponseEntity<InventoryResponse> getInventory(@PathVariable("inventoryId") UUID inventoryId) {
        return inventoryService.findById(inventoryId)
                .map(inventoryRestMapper::map)
                .map(inventoryResponse -> new ResponseEntity<>(inventoryResponse, HttpStatus.OK))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<InventoryResponse> createInventory(@Valid @RequestBody InventoryRequest inventoryRequest) {
        final var inventoryResponse = inventoryRestMapper.map(inventoryService.create(inventoryRestMapper.map(inventoryRequest)));

        return new ResponseEntity<>(inventoryResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{inventoryId}")
    public ResponseEntity<InventoryResponse> updateInventory(@PathVariable("inventoryId") UUID inventoryId, @Valid @RequestBody InventoryRequest inventoryRequest) {
        final var inventoryResponse = inventoryRestMapper.map(inventoryService.update(inventoryId, inventoryRestMapper.map(inventoryRequest)));

        return new ResponseEntity<>(inventoryResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{inventoryId}")
    public ResponseEntity<HttpStatus> deleteInventory(@PathVariable("inventoryId") UUID inventoryId) {
        inventoryService.delete(inventoryId);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @ExceptionHandler(InventoryNotFoundException.class)
    public ResponseEntity<Object> handle(InventoryNotFoundException ex) {
        return handleError(HttpStatus.NOT_FOUND, List.of(ex.getMessage()));
    }

    private ResponseEntity<Object> handleError(HttpStatus status, List<String> errors) {
        final var body = new LinkedHashMap<String, Object>();
        body.put("timestamp", new Date());
        body.put("status", status.value());
        body.put("errors", errors);
        return new ResponseEntity<>(body, status);
    }

}
