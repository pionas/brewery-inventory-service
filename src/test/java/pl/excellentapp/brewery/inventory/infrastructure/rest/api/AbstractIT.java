package pl.excellentapp.brewery.inventory.infrastructure.rest.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Import({RestTemplateConfiguration.class})
@ActiveProfiles("it")
public abstract class AbstractIT {

    @Autowired
    protected TestRestTemplate restTemplate;

}