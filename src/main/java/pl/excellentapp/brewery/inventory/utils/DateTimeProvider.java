package pl.excellentapp.brewery.inventory.utils;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Component
public class DateTimeProvider {

    public LocalDateTime now() {
        return LocalDateTime.now();
    }
}
