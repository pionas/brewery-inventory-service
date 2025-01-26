package pl.excellentapp.brewery.inventory.infrastructure.rest.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.excellentapp.brewery.inventory.application.InventoryService;
import pl.excellentapp.brewery.inventory.domain.Inventory;
import pl.excellentapp.brewery.inventory.domain.exception.InventoryNotFoundException;
import pl.excellentapp.brewery.inventory.infrastructure.rest.api.dto.InventoryRequest;
import pl.excellentapp.brewery.inventory.infrastructure.rest.api.dto.InventoryResponse;
import pl.excellentapp.brewery.inventory.infrastructure.rest.api.dto.InventorysResponse;
import pl.excellentapp.brewery.inventory.infrastructure.rest.api.mapper.InventoryRestMapper;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class InventoryRestControllerTest extends AbstractMvcTest {

    private static final OffsetDateTime OFFSET_DATE_TIME = OffsetDateTime.of(2025, 1, 23, 12, 7, 0, 0, ZoneOffset.UTC);

    @InjectMocks
    private InventoryRestController controller;

    @Mock
    private InventoryService inventoryService;

    @Spy
    private InventoryRestMapper inventoryRestMapper = Mappers.getMapper(InventoryRestMapper.class);

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setMessageConverters(new MappingJackson2HttpMessageConverter(getObjectMapper()))
                .build();
    }

    @Test
    void getInventorys_ShouldReturnEmptyListOfInventorys() throws Exception {
        // given

        // when
        mockMvc.perform(get("/api/v1/inventorys"))
                .andExpect(status().isOk());

        // then
        verify(inventoryService).findAll();
    }

    @Test
    void getInventorys_ShouldReturnListOfInventorys() throws Exception {
        // given
        final var inventory1 = createInventory(UUID.fromString("71737f0e-11eb-4775-b8b4-ce945fdee936"));
        final var inventory2 = createInventory(UUID.fromString("4a5b96de-684a-411b-9616-fddd0b06a382"));
        when(inventoryService.findAll())
                .thenReturn(List.of(inventory1, inventory2));

        // when
        final var response = mockMvc.perform(get("/api/v1/inventorys"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        // then
        assertNotNull(response);
        final var responseBody = response.getContentAsString();
        assertNotNull(responseBody);
        final var inventorysResponse = super.mapFromJson(responseBody, InventorysResponse.class);
        assertNotNull(inventorysResponse);
        final var inventorysResponseList = inventorysResponse.getInventorys();
        assertNotNull(inventorysResponseList);
        assertEquals(2, inventorysResponseList.size());
        final var inventoryResponse1 = inventorysResponseList.getFirst();
        assertNotNull(inventoryResponse1);
        assertEquals(inventoryResponse1.getId(), inventory1.getId());
        final var inventoryResponse2 = inventorysResponseList.getLast();
        assertNotNull(inventoryResponse2);
        assertEquals(inventoryResponse2.getId(), inventory2.getId());
        verify(inventoryService).findAll();
    }

    @Test
    void getInventory_ShouldReturnNotFound() throws Exception {
        // given
        final var inventoryId = UUID.fromString("71737f0e-11eb-4775-b8b4-ce945fdee936");
        // when
        final var response = mockMvc.perform(get("/api/v1/inventorys/" + inventoryId))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse();

        // then
        assertNotNull(response);
        final var responseBody = response.getContentAsString();
        assertNotNull(responseBody);
        verify(inventoryService).findById(inventoryId);
    }

    @Test
    void getInventory_ShouldReturn() throws Exception {
        // given
        final var inventoryId = UUID.fromString("71737f0e-11eb-4775-b8b4-ce945fdee936");
        final var inventory = createInventory(inventoryId);
        when(inventoryService.findById(inventoryId)).thenReturn(Optional.of(inventory));

        // when
        final var response = mockMvc.perform(get("/api/v1/inventorys/" + inventoryId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        // then
        assertNotNull(response);
        final var responseBody = response.getContentAsString();
        assertNotNull(responseBody);
        final var inventoryResponse = super.mapFromJson(responseBody, InventoryResponse.class);
        assertNotNull(inventoryResponse);
        assertEquals(inventoryResponse.getId(), inventory.getId());
    }

    @Test
    void createInventory_ShouldReturnCreatedInventory() throws Exception {
        // given
        final var inventory = createInventory(UUID.fromString("71737f0e-11eb-4775-b8b4-ce945fdee936"));
        final var inventoryRequest = InventoryRequest.builder()

                .build();
        when(inventoryService.create(any())).thenReturn(inventory);

        // when
        final var response = mockMvc.perform(post("/api/v1/inventorys")
                        .content(super.mapToJson(inventoryRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse();

        // then
        assertNotNull(response);
        final var responseBody = response.getContentAsString();
        assertNotNull(responseBody);
        final var inventoryResponse = super.mapFromJson(responseBody, InventoryResponse.class);
        assertNotNull(inventoryResponse);
        assertEquals(inventoryResponse.getId(), inventory.getId());
        verify(inventoryService).create(any());
    }

    @Test
    void updateInventory_ShouldReturnUpdatedInventory() throws Exception {
        // given
        final var offsetDateTime = OffsetDateTime.of(2025, 1, 23, 12, 7, 10, 0, ZoneOffset.UTC);
        final var originalInventory = createInventory(UUID.fromString("71737f0e-11eb-4775-b8b4-ce945fdee936"));
        final var updateRequest = getUpdateRequest(originalInventory);
        final var expectedInventory = getExpectedInventory(originalInventory, offsetDateTime);
        final var inventoryRequest = InventoryRequest.builder()

                .build();
        when(inventoryService.update(any(), any())).thenReturn(expectedInventory);

        // when
        final var response = mockMvc.perform(put("/api/v1/inventorys/" + originalInventory.getId())
                        .content(super.mapToJson(inventoryRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        // then
        assertNotNull(response);
        final var responseBody = response.getContentAsString();
        assertNotNull(responseBody);
        final var inventoryResponse = super.mapFromJson(responseBody, InventoryResponse.class);
        assertNotNull(inventoryResponse);
        assertEquals(inventoryResponse.getId(), expectedInventory.getId());
        verify(inventoryService).update(any(), any());
    }

    @Test
    void deleteInventory_ShouldDeletedInventory() throws Exception {
        // given
        final var inventoryId = UUID.fromString("71737f0e-11eb-4775-b8b4-ce945fdee936");

        // when
        mockMvc.perform(delete("/api/v1/inventorys/" + inventoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted());

        // then
        verify(inventoryService).delete(inventoryId);
    }

    @Test
    void deleteInventory_ShouldThrowExceptionWhenTryDeletedInventory() throws Exception {
        // given
        final var inventoryId = UUID.fromString("71737f0e-11eb-4775-b8b4-ce945fdee936");
        doThrow(new InventoryNotFoundException("Inventory Not Found. UUID: " + inventoryId)).when(inventoryService).delete(inventoryId);

        // when
        mockMvc.perform(delete("/api/v1/inventorys/" + inventoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        // then
        verify(inventoryService).delete(inventoryId);
    }

    private Inventory createInventory(UUID id) {
        return Inventory.builder()
                .id(id)
                .build();
    }

    private Inventory getUpdateRequest(Inventory originalInventory) {
        return createInventory(
                originalInventory.getId()
        );
    }

    private Inventory getExpectedInventory(Inventory originalInventory, OffsetDateTime offsetDateTime) {
        return createInventory(
                originalInventory.getId()
        );
    }
}