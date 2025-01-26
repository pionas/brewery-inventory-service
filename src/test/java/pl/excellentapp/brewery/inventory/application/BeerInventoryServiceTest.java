package pl.excellentapp.brewery.inventory.application;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import pl.excellentapp.brewery.inventory.domain.beerinventory.BeerInventory;
import pl.excellentapp.brewery.inventory.domain.beerinventory.BeerInventoryRepository;
import pl.excellentapp.brewery.inventory.domain.exception.BeerInventoryNotFoundException;
import pl.excellentapp.brewery.inventory.utils.DateTimeProvider;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BeerInventoryServiceTest {

    private static final OffsetDateTime OFFSET_DATE_TIME = OffsetDateTime.of(2025, 1, 23, 12, 7, 0, 0, ZoneOffset.UTC);

    private final BeerInventoryRepository beerInventoryRepository = Mockito.mock(BeerInventoryRepository.class);
    private final DateTimeProvider dateTimeProvider = Mockito.mock(DateTimeProvider.class);

    private final BeerInventoryService beerInventoryService = new BeerInventoryServiceImpl(beerInventoryRepository, dateTimeProvider);


    @Test
    void findAll_ShouldReturnListOfInventories() {
        // given
        final var inventories = List.of(
                createInventory(UUID.fromString("1b4e28ba-2fa1-4d3b-a3f5-ef19b5a7633b"), OFFSET_DATE_TIME),
                createInventory(UUID.fromString("2c4f2ed6-bd1d-4f9d-82c6-6b975b5cf5b3"), OFFSET_DATE_TIME),
                createInventory(UUID.fromString("3a8e0e2f-587d-4b3c-b1c9-27f5d6c3627a"), OFFSET_DATE_TIME),
                createInventory(UUID.fromString("4c9e7a3b-84e7-4f8e-95e2-cd2f1d56e6b7"), OFFSET_DATE_TIME),
                createInventory(UUID.fromString("5d3f8e7c-9f2b-42e1-908d-cf3d1e678e9b"), OFFSET_DATE_TIME),
                createInventory(UUID.fromString("6e8f9d4c-7c8a-45d1-8b4c-ed3f5a7b6e9d"), OFFSET_DATE_TIME),
                createInventory(UUID.fromString("7f1b3c2d-8e9f-41b2-94c8-ef3d7a6b5c9f"), OFFSET_DATE_TIME),
                createInventory(UUID.fromString("8a2d4e6f-9b3c-4e2f-b7d1-2c3f5a8e7b6d"), OFFSET_DATE_TIME),
                createInventory(UUID.fromString("9c3e5d7a-b8f2-41c3-82e9-f2b1d6e5c4f7"), OFFSET_DATE_TIME),
                createInventory(UUID.fromString("0d1e2f3b-5a7c-4d1f-8e9b-2f3d6a8b7c5f"), OFFSET_DATE_TIME)
        );
        when(beerInventoryRepository.findAll()).thenReturn(inventories);

        // when
        final var result = beerInventoryService.findAll();

        // then
        assertEquals(10, result.size());
        assertEquals(inventories, result);
        verify(beerInventoryRepository, times(1)).findAll();
    }

    @Test
    void findById_ShouldReturnInventory_WhenInventoryExists() {
        // given
        final var inventory = createInventory(UUID.fromString("71737f0e-11eb-4775-b8b4-ce945fdee936"), OFFSET_DATE_TIME);
        when(beerInventoryRepository.findById(inventory.getBeerId())).thenReturn(Optional.of(inventory));

        // when
        final var result = beerInventoryService.findById(inventory.getBeerId());

        // then
        assertTrue(result.isPresent());
        assertEquals(inventory, result.get());
        verify(beerInventoryRepository, times(1)).findById(inventory.getBeerId());
    }

    @Test
    void findById_ShouldThrowException_WhenInventoryNotFound() {
        // given
        final var inventory = createInventory(UUID.fromString("71737f0e-11eb-4775-b8b4-ce945fdee936"), OFFSET_DATE_TIME);
        when(beerInventoryRepository.findById(inventory.getBeerId())).thenReturn(Optional.empty());

        // when
        final var inventoryOptional = beerInventoryService.findById(inventory.getBeerId());

        // then
        assertTrue(inventoryOptional.isEmpty());
        verify(beerInventoryRepository, times(1)).findById(inventory.getBeerId());
    }

    @Test
    void create_ShouldSaveAndReturnInventory() {
        // given
        final var inventory = createInventory(UUID.fromString("71737f0e-11eb-4775-b8b4-ce945fdee936"), OFFSET_DATE_TIME);
        when(beerInventoryRepository.save(inventory)).thenReturn(inventory);

        // when
        final var result = beerInventoryService.create(inventory.getBeerId(), inventory.getAvailableStock());

        // then
        assertEquals(inventory, result);
        verify(beerInventoryRepository, times(1)).save(inventory);
    }

//    @Test
//    void update_ShouldUpdateAndReturnInventory() {
//        // given
//        OffsetDateTime offsetDateTime = OffsetDateTime.of(2025, 1, 23, 12, 7, 10, 0, ZoneOffset.UTC);
//        final var originalInventory = createInventory(UUID.fromString("71737f0e-11eb-4775-b8b4-ce945fdee936"), OFFSET_DATE_TIME);
//        final var updateRequest = getUpdateRequest(originalInventory);
//        final var expectedInventory = getExpectedInventory(updateRequest, offsetDateTime);
//
//        when(beerInventoryRepository.findById(originalInventory.getBeerId())).thenReturn(Optional.of(originalInventory));
//        when(beerInventoryRepository.save(any(BeerInventory.class))).thenAnswer(invocation -> invocation.getArgument(0));
//        when(dateTimeProvider.now()).thenReturn(offsetDateTime);
//
//        // when
//        final var result = beerInventoryService.update(originalInventory.getBeerId(), updateRequest);
//
//        // then
//        assertEquals(expectedInventory, result);
//        assertEquals(expectedInventory.getBeerId(), result.getBeerId());
//        verify(beerInventoryRepository, times(1)).findById(originalInventory.getBeerId());
//        verify(beerInventoryRepository, times(1)).save(originalInventory);
//    }


    @Test
    void delete_ShouldDeleteInventory_WhenInventoryExists() {
        // given
        final var inventory = createInventory(UUID.fromString("71737f0e-11eb-4775-b8b4-ce945fdee936"), OFFSET_DATE_TIME);
        when(beerInventoryRepository.findById(inventory.getBeerId())).thenReturn(Optional.of(inventory));
        doNothing().when(beerInventoryRepository).deleteById(inventory.getBeerId());

        // when
        assertDoesNotThrow(() -> beerInventoryService.delete(inventory.getBeerId()));

        // then
        verify(beerInventoryRepository, times(1)).findById(inventory.getBeerId());
        verify(beerInventoryRepository, times(1)).deleteById(inventory.getBeerId());
    }

    @Test
    void delete_ShouldThrowException_WhenInventoryNotFound() {
        // given
        final var inventory = createInventory(UUID.fromString("71737f0e-11eb-4775-b8b4-ce945fdee936"), OFFSET_DATE_TIME);
        when(beerInventoryRepository.findById(inventory.getBeerId())).thenReturn(Optional.empty());

        // when
        assertThrows(BeerInventoryNotFoundException.class, () -> beerInventoryService.delete(inventory.getBeerId()));

        // then
        verify(beerInventoryRepository, times(1)).findById(inventory.getBeerId());
        verify(beerInventoryRepository, never()).deleteById(any());
    }

    private BeerInventory getUpdateRequest(BeerInventory originalBeerInventory) {
        return createInventory(
                originalBeerInventory.getBeerId(),
                OFFSET_DATE_TIME
        );
    }

    private BeerInventory getExpectedInventory(BeerInventory originalBeerInventory, OffsetDateTime offsetDateTime) {
        return createInventory(
                originalBeerInventory.getBeerId(),
                offsetDateTime
        );
    }

    private BeerInventory createInventory(UUID id, OffsetDateTime offsetDateTime) {
        return BeerInventory.builder()
                .beerId(id)
                .build();
    }
}