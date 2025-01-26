package pl.excellentapp.brewery.inventory.infrastructure.rest.api;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.jdbc.JdbcTestUtils;
import pl.excellentapp.brewery.inventory.infrastructure.rest.api.dto.InventoryResponse;
import pl.excellentapp.brewery.inventory.infrastructure.rest.api.dto.InventorysResponse;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class BeerInventoryRestControllerIT extends AbstractIT {

    private final String MODEL_API_URL = "/api/v1/inventorys";

    @AfterEach
    void clearDatabase(@Autowired JdbcTemplate jdbcTemplate) {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "inventorys");
    }

    @Test
    void getInventorys_ShouldReturnEmptyListOfInventorys() {
        // given

        // when
        final var response = restTemplate.getForEntity(MODEL_API_URL, InventorysResponse.class);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        final var responseBody = response.getBody();
        assertNotNull(responseBody);
        final var inventorysResponse = responseBody.getInventorys();
        assertNotNull(inventorysResponse);
        assertTrue(inventorysResponse.isEmpty());
    }

    @Test
    @Sql({"/db/inventorys.sql"})
    void getInventorys_ShouldReturnListOfInventorys() {
        // given

        // when
        final var response = restTemplate.getForEntity(MODEL_API_URL, InventorysResponse.class);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        final var responseBody = response.getBody();
        assertNotNull(responseBody);
        final var inventorysResponse = responseBody.getInventorys();
        assertNotNull(inventorysResponse);
        assertFalse(inventorysResponse.isEmpty());
    }

    @Test
    @Sql({"/db/inventorys.sql"})
    void getInventory_ShouldReturnInventory() {
        // given
        final var inventoryId = UUID.fromString("1b4e28ba-2fa1-4d3b-a3f5-ef19b5a7633b");

        // when
        final var response = restTemplate.getForEntity(MODEL_API_URL + "/{inventoryId}", InventoryResponse.class, inventoryId);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        final var responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(inventoryId, responseBody.getId());
    }

    @Test
    void deleteInventory_ShouldThrowException() {
        // given
        final var inventoryId = UUID.fromString("1b4e28ba-2fa1-4d3b-a3f5-ef19b5a7633b");

        // when
        final var response = restTemplate.exchange(
                MODEL_API_URL + "/{inventoryId}",
                HttpMethod.DELETE,
                null,
                Map.class,
                inventoryId
        );

        // then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @Sql({"/db/inventorys.sql"})
    void deleteInventory_ShouldDelete() {
        // given
        final var inventoryId = UUID.fromString("1b4e28ba-2fa1-4d3b-a3f5-ef19b5a7633b");

        // when
        final var response = restTemplate.exchange(
                MODEL_API_URL + "/{inventoryId}",
                HttpMethod.DELETE,
                null,
                Void.class,
                inventoryId
        );

        // then
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        final var responseVerify = restTemplate.getForEntity(MODEL_API_URL + "/{inventoryId}", InventoryResponse.class, inventoryId);
        assertEquals(HttpStatus.OK, responseVerify.getStatusCode());
    }

}