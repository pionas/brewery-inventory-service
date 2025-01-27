package pl.excellentapp.brewery.inventory.infrastructure.rest.api;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.jdbc.JdbcTestUtils;
import pl.excellentapp.brewery.inventory.infrastructure.rest.api.dto.InventoriesResponse;
import pl.excellentapp.brewery.inventory.infrastructure.rest.api.dto.InventoryRequest;
import pl.excellentapp.brewery.inventory.infrastructure.rest.api.dto.InventoryResponse;
import pl.excellentapp.brewery.inventory.infrastructure.rest.api.dto.InventoryStockRequest;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class BeerInventoryRestControllerIT extends AbstractIT {

    private final String MODEL_API_URL = "/api/v1/inventories";
    private final String INVENTORY_DETAILS_API_URL = "/api/v1/inventories/{inventoryId}";

    @AfterEach
    void clearDatabase(@Autowired JdbcTemplate jdbcTemplate) {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "beer_inventory_history", "beer_inventory");
    }

    @Test
    void shouldReturnEmptyListOfInventories() {
        // given

        // when
        final var response = restTemplate.getForEntity(MODEL_API_URL, InventoriesResponse.class);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        final var responseBody = response.getBody();
        assertNotNull(responseBody);
        final var inventoriesResponse = responseBody.getInventories();
        assertNotNull(inventoriesResponse);
        assertTrue(inventoriesResponse.isEmpty());
    }

    @Test
    @Sql({"/db/inventories.sql"})
    void shouldReturnListOfInventories() {
        // given

        // when
        final var response = restTemplate.getForEntity(MODEL_API_URL, InventoriesResponse.class);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        final var responseBody = response.getBody();
        assertNotNull(responseBody);
        final var inventoriesResponse = responseBody.getInventories();
        assertNotNull(inventoriesResponse);
        assertFalse(inventoriesResponse.isEmpty());
    }

    @Test
    @Sql({"/db/inventories.sql"})
    void shouldReturnInventory() {
        // given
        final var inventoryId = UUID.fromString("b1d1a20e-fb93-4c38-b0f7-9ac1f28e03c1");

        // when
        final var response = restTemplate.getForEntity(INVENTORY_DETAILS_API_URL, InventoryResponse.class, inventoryId);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        final var responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(inventoryId, responseBody.getId());
        assertEquals(20, responseBody.getAvailableStock());
    }

    @Test
    void shouldThrowNotFoundWhenTryGetInventoryButInventoryByIdNotExists() {
        // given
        final var inventoryId = UUID.fromString("b1d1a20e-fb93-4c38-b0f7-9ac1f28e03c1");

        // when
        final var response = restTemplate.getForEntity(INVENTORY_DETAILS_API_URL, InventoryResponse.class, inventoryId);

        // then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void shouldCreateInventory() {
        // given
        final var inventoryId = UUID.fromString("1337b641-30b2-4dcd-b4d7-e64dbedece61");
        final var inventoryRequest = InventoryRequest.builder()
                .beerId(inventoryId)
                .availableStock(20)
                .build();

        // when
        final var response = restTemplate.postForEntity(MODEL_API_URL, inventoryRequest, InventoryResponse.class);

        // then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        final var responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(inventoryId, responseBody.getId());
        assertEquals(20, responseBody.getAvailableStock());
    }

    @Test
    @Sql({"/db/inventories.sql"})
    void shouldAddStockInventory() {
        // given
        final var inventoryId = UUID.fromString("b1d1a20e-fb93-4c38-b0f7-9ac1f28e03c1");
        final var stockRequest = InventoryStockRequest.builder()
                .stock(20)
                .build();

        // when
        final var response = restTemplate.postForEntity(INVENTORY_DETAILS_API_URL + "/add-stock", stockRequest, InventoryResponse.class, inventoryId);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        final var responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(inventoryId, responseBody.getId());
        assertEquals(40, responseBody.getAvailableStock());
    }

    @Test
    @Sql({"/db/inventories.sql"})
    void shouldReserveStockInventory() {
        // given
        final var inventoryId = UUID.fromString("f395b3c2-6468-4d81-a87a-2db89c23ae14");
        final var stockRequest = InventoryStockRequest.builder()
                .stock(15)
                .build();

        // when
        final var response = restTemplate.postForEntity(INVENTORY_DETAILS_API_URL + "/reserve-stock", stockRequest, InventoryResponse.class, inventoryId);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        final var responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(inventoryId, responseBody.getId());
        assertEquals(85, responseBody.getAvailableStock());
    }

    @Test
    @Sql({"/db/inventories.sql"})
    void shouldReleaseStockInventory() {
        // given
        final var inventoryId = UUID.fromString("2e9f64d8-4bf2-4e5e-beb8-8a0e2f5d3a7a");
        final var stockRequest = InventoryStockRequest.builder()
                .stock(20)
                .build();

        // when
        final var response = restTemplate.postForEntity(INVENTORY_DETAILS_API_URL + "/release-stock", stockRequest, InventoryResponse.class, inventoryId);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        final var responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(inventoryId, responseBody.getId());
        assertEquals(20, responseBody.getAvailableStock());
    }

    @Test
    void shouldThrowNotFoundWhenTryDeleteInventoryButInventoryByIdNotExists() {
        // given
        final var inventoryId = UUID.fromString("1b4e28ba-2fa1-4d3b-a3f5-ef19b5a7633b");

        // when
        final var response = restTemplate.exchange(
                INVENTORY_DETAILS_API_URL,
                HttpMethod.DELETE,
                null,
                Map.class,
                inventoryId
        );

        // then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @Sql({"/db/inventories.sql"})
    void shouldDeleteById() {
        // given
        final var inventoryId = UUID.fromString("b1d1a20e-fb93-4c38-b0f7-9ac1f28e03c1");

        // when
        final var response = restTemplate.exchange(
                INVENTORY_DETAILS_API_URL,
                HttpMethod.DELETE,
                null,
                Void.class,
                inventoryId
        );

        // then
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        final var responseVerify = restTemplate.getForEntity(INVENTORY_DETAILS_API_URL, InventoryResponse.class, inventoryId);
        assertEquals(HttpStatus.NOT_FOUND, responseVerify.getStatusCode());
    }

}