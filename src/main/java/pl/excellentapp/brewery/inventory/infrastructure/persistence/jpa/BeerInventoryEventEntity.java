package pl.excellentapp.brewery.inventory.infrastructure.persistence.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import pl.excellentapp.brewery.inventory.domain.beerinventory.BeerHistoryType;

import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "beer_inventory_history")
public class BeerInventoryEventEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "beer_id", nullable = false)
    private BeerInventoryEntity beerInventory;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BeerHistoryType eventType;

    @Column(nullable = false)
    private int quantity;

    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdDate;

}
