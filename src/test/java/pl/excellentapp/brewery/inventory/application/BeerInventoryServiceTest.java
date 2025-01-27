package pl.excellentapp.brewery.inventory.application;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import pl.excellentapp.brewery.inventory.domain.beerinventory.BeerHistoryType;
import pl.excellentapp.brewery.inventory.domain.beerinventory.BeerInventory;
import pl.excellentapp.brewery.inventory.domain.beerinventory.BeerInventoryRepository;
import pl.excellentapp.brewery.inventory.domain.exception.BeerInsufficientStockException;
import pl.excellentapp.brewery.inventory.domain.exception.BeerInventoryNotFoundException;
import pl.excellentapp.brewery.inventory.utils.DateTimeProvider;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BeerInventoryServiceTest {

    private static final LocalDateTime LOCAL_DATE_TIME = LocalDateTime.of(2025, 1, 23, 12, 7, 0, 0);

    private final BeerInventoryRepository beerInventoryRepository = Mockito.mock(BeerInventoryRepository.class);
    private final DateTimeProvider dateTimeProvider = Mockito.mock(DateTimeProvider.class);

    private final BeerInventoryService beerInventoryService = new BeerInventoryServiceImpl(beerInventoryRepository, dateTimeProvider);

    @Test
    void shouldReturnListOfInventories() {
        // given
        final var inventories = List.of(
                createBeerInventory(UUID.fromString("1b4e28ba-2fa1-4d3b-a3f5-ef19b5a7633b"), 1, LOCAL_DATE_TIME),
                createBeerInventory(UUID.fromString("2c4f2ed6-bd1d-4f9d-82c6-6b975b5cf5b3"), 1, LOCAL_DATE_TIME),
                createBeerInventory(UUID.fromString("3a8e0e2f-587d-4b3c-b1c9-27f5d6c3627a"), 1, LOCAL_DATE_TIME),
                createBeerInventory(UUID.fromString("4c9e7a3b-84e7-4f8e-95e2-cd2f1d56e6b7"), 1, LOCAL_DATE_TIME),
                createBeerInventory(UUID.fromString("5d3f8e7c-9f2b-42e1-908d-cf3d1e678e9b"), 1, LOCAL_DATE_TIME),
                createBeerInventory(UUID.fromString("6e8f9d4c-7c8a-45d1-8b4c-ed3f5a7b6e9d"), 1, LOCAL_DATE_TIME),
                createBeerInventory(UUID.fromString("7f1b3c2d-8e9f-41b2-94c8-ef3d7a6b5c9f"), 1, LOCAL_DATE_TIME),
                createBeerInventory(UUID.fromString("8a2d4e6f-9b3c-4e2f-b7d1-2c3f5a8e7b6d"), 1, LOCAL_DATE_TIME),
                createBeerInventory(UUID.fromString("9c3e5d7a-b8f2-41c3-82e9-f2b1d6e5c4f7"), 1, LOCAL_DATE_TIME),
                createBeerInventory(UUID.fromString("0d1e2f3b-5a7c-4d1f-8e9b-2f3d6a8b7c5f"), 1, LOCAL_DATE_TIME)
        );
        when(beerInventoryRepository.findAll()).thenReturn(inventories);

        // when
        final var result = beerInventoryService.findAll();

        // then
        assertEquals(10, result.size());
        assertEquals(inventories, result);
        verify(beerInventoryRepository).findAll();
    }

    @Test
    void shouldReturnEmptyListOfInventories() {
        // given
        when(beerInventoryRepository.findAll()).thenReturn(Collections.emptyList());

        // when
        final var result = beerInventoryService.findAll();

        // then
        assertEquals(0, result.size());
        verify(beerInventoryRepository).findAll();
    }

    @Test
    void shouldReturnInventoryWhenInventoryByIdExists() {
        // given
        final var inventory = createBeerInventory(UUID.fromString("71737f0e-11eb-4775-b8b4-ce945fdee936"), 1, LOCAL_DATE_TIME);
        when(beerInventoryRepository.findById(inventory.getBeerId())).thenReturn(Optional.of(inventory));

        // when
        final var result = beerInventoryService.findById(inventory.getBeerId());

        // then
        assertTrue(result.isPresent());
        assertEquals(inventory, result.get());
        verify(beerInventoryRepository).findById(inventory.getBeerId());
    }

    @Test
    void shouldReturnEmptyWhenInventoryByIdNotFound() {
        // given
        final var inventory = createBeerInventory(UUID.fromString("71737f0e-11eb-4775-b8b4-ce945fdee936"), 2, LOCAL_DATE_TIME);
        when(beerInventoryRepository.findById(inventory.getBeerId())).thenReturn(Optional.empty());

        // when
        final var inventoryOptional = beerInventoryService.findById(inventory.getBeerId());

        // then
        assertTrue(inventoryOptional.isEmpty());
        verify(beerInventoryRepository).findById(inventory.getBeerId());
    }

    @Test
    void shouldSaveAndReturnInventory() {
        // given
        final var inventory = createBeerInventory(UUID.fromString("71737f0e-11eb-4775-b8b4-ce945fdee936"), 3, LOCAL_DATE_TIME);
        when(beerInventoryRepository.save(inventory)).thenReturn(inventory);
        when(dateTimeProvider.now()).thenReturn(LOCAL_DATE_TIME);

        // when
        final var result = beerInventoryService.create(inventory.getBeerId(), inventory.getAvailableStock());

        // then
        assertEquals(inventory, result);
        verify(beerInventoryRepository).save(inventory);
    }

    @Test
    void shouldAddStockAndReturnInventory() {
        // given
        final var inventory = createBeerInventory(UUID.fromString("71737f0e-11eb-4775-b8b4-ce945fdee936"), 3, LOCAL_DATE_TIME);
        when(beerInventoryRepository.findById(inventory.getBeerId())).thenReturn(Optional.of(inventory));
        when(beerInventoryRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(dateTimeProvider.now()).thenReturn(LOCAL_DATE_TIME);

        // when
        final var result = beerInventoryService.addStock(inventory.getBeerId(), 10);

        // then
        assertEquals(inventory.getBeerId(), result.getBeerId());
        assertEquals(13, result.getAvailableStock());
        final var history = result.getHistory();
        assertEquals(2, history.size());
        final var beerInventoryEvent1 = history.getFirst();
        assertEquals(BeerHistoryType.BREWED, beerInventoryEvent1.getEventType());
        assertEquals(3, beerInventoryEvent1.getQuantity());
        final var beerInventoryEvent2 = history.getLast();
        assertEquals(BeerHistoryType.BREWED, beerInventoryEvent2.getEventType());
        assertEquals(10, beerInventoryEvent2.getQuantity());

        verify(beerInventoryRepository).save(inventory);
    }

    @Test
    void shouldReserveStockAndReturnInventory() {
        // given
        final var inventory = createBeerInventory(UUID.fromString("71737f0e-11eb-4775-b8b4-ce945fdee936"), 20, LOCAL_DATE_TIME);
        when(beerInventoryRepository.findById(inventory.getBeerId())).thenReturn(Optional.of(inventory));
        when(beerInventoryRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(dateTimeProvider.now()).thenReturn(LOCAL_DATE_TIME);

        // when
        final var result = beerInventoryService.reserveStock(inventory.getBeerId(), 10);

        // then
        assertEquals(inventory.getBeerId(), result.getBeerId());
        assertEquals(10, result.getAvailableStock());
        final var history = result.getHistory();
        assertEquals(2, history.size());
        final var beerInventoryEvent1 = history.getFirst();
        assertEquals(BeerHistoryType.BREWED, beerInventoryEvent1.getEventType());
        assertEquals(20, beerInventoryEvent1.getQuantity());
        final var beerInventoryEvent2 = history.getLast();
        assertEquals(BeerHistoryType.RESERVED, beerInventoryEvent2.getEventType());
        assertEquals(10, beerInventoryEvent2.getQuantity());

        verify(beerInventoryRepository).save(inventory);
    }

    @Test
    void shouldThrowExceptionWhenTryReserveStockButNotEnoughStock() {
        // given
        final var inventory = createBeerInventory(UUID.fromString("71737f0e-11eb-4775-b8b4-ce945fdee936"), 3, LOCAL_DATE_TIME);
        when(beerInventoryRepository.findById(inventory.getBeerId())).thenReturn(Optional.of(inventory));
        when(beerInventoryRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        final var exception = assertThrows(BeerInsufficientStockException.class, () -> beerInventoryService.reserveStock(inventory.getBeerId(), 10));

        // then
        assertNotNull(exception);
        assertEquals("Not enough stock to reserve.", exception.getMessage());
    }

    @Test
    void shouldReleaseStockAndReturnInventory() {
        // given
        final var inventory = createBeerInventory(UUID.fromString("71737f0e-11eb-4775-b8b4-ce945fdee936"), 20, LOCAL_DATE_TIME);
        when(beerInventoryRepository.findById(inventory.getBeerId())).thenReturn(Optional.of(inventory));
        when(beerInventoryRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(dateTimeProvider.now()).thenReturn(LOCAL_DATE_TIME);

        // when
        final var result = beerInventoryService.releaseStock(inventory.getBeerId(), 10);

        // then
        assertEquals(inventory.getBeerId(), result.getBeerId());
        assertEquals(30, result.getAvailableStock());
        final var history = result.getHistory();
        assertEquals(2, history.size());
        final var beerInventoryEvent1 = history.getFirst();
        assertEquals(BeerHistoryType.BREWED, beerInventoryEvent1.getEventType());
        assertEquals(20, beerInventoryEvent1.getQuantity());
        final var beerInventoryEvent2 = history.getLast();
        assertEquals(BeerHistoryType.CANCELLED, beerInventoryEvent2.getEventType());
        assertEquals(10, beerInventoryEvent2.getQuantity());

        verify(beerInventoryRepository).save(inventory);
    }

    @Test
    void shouldDeleteInventoryWhenInventoryExists() {
        // given
        final var inventory = createBeerInventory(UUID.fromString("71737f0e-11eb-4775-b8b4-ce945fdee936"), 1, LOCAL_DATE_TIME);
        when(beerInventoryRepository.findById(inventory.getBeerId())).thenReturn(Optional.of(inventory));
        doNothing().when(beerInventoryRepository).deleteById(inventory.getBeerId());

        // when
        assertDoesNotThrow(() -> beerInventoryService.delete(inventory.getBeerId()));

        // then
        verify(beerInventoryRepository).findById(inventory.getBeerId());
        verify(beerInventoryRepository).deleteById(inventory.getBeerId());
    }

    @Test
    void shouldThrowExceptionWhenInventoryNotFound() {
        // given
        final var inventory = createBeerInventory(UUID.fromString("71737f0e-11eb-4775-b8b4-ce945fdee936"), 1, LOCAL_DATE_TIME);
        when(beerInventoryRepository.findById(inventory.getBeerId())).thenReturn(Optional.empty());

        // when
        assertThrows(BeerInventoryNotFoundException.class, () -> beerInventoryService.delete(inventory.getBeerId()));

        // then
        verify(beerInventoryRepository).findById(inventory.getBeerId());
        verify(beerInventoryRepository, never()).deleteById(any());
    }

    private BeerInventory createBeerInventory(UUID id, int availableStock, LocalDateTime localDateTime) {
        final var beerInventory = BeerInventory.builder()
                .beerId(id)
                .build();
        beerInventory.addStock(availableStock, localDateTime);
        return beerInventory;
    }
}