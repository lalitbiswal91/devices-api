package com.lalit.devices;

import com.lalit.devices.repository.DeviceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
public abstract class BaseIntegrationTest {
    @Autowired
    protected DeviceRepository deviceRepository;

    @BeforeEach
    void cleanDatabase() {
        deviceRepository.deleteAll(); // ensuring DB is empty before each test
    }
}
