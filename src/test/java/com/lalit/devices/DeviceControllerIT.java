package com.lalit.devices;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lalit.devices.dto.CreateDeviceRequest;
import com.lalit.devices.dto.UpdateDeviceRequest;
import com.lalit.devices.model.DeviceState;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class DeviceControllerIT extends BaseIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // ---------- CREATE ----------

    @Test
    void shouldCreateDevice() throws Exception {
        var request = new CreateDeviceRequest(
                "iPhone",
                "Apple",
                DeviceState.AVAILABLE
        );

        mockMvc.perform(post("/api/devices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("iPhone"))
                .andExpect(jsonPath("$.brand").value("Apple"))
                .andExpect(jsonPath("$.state").value("AVAILABLE"))
                .andExpect(jsonPath("$.createdAt").exists());
    }

    @Test
    void shouldReturn400ForInvalidCreateRequest() throws Exception {

        var invalidJson = """
                {
                  "name": "",
                  "brand": "",
                  "state": "AVAILABLE"
                }
                """;

        mockMvc.perform(post("/api/devices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400ForUnknownField() throws Exception {
        var jsonWithUnknownField = """
                {
                  "name": "Phone",
                  "brand": "Nokia",
                  "state": "AVAILABLE",
                  "unknownField": "boom"
                }
                """;

        mockMvc.perform(post("/api/devices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonWithUnknownField))
                .andExpect(status().isBadRequest());
    }

    // ---------- GET ----------

    @Test
    void shouldGetDeviceById() throws Exception {
        long id = createDevice("Pixel", "Google", DeviceState.AVAILABLE);

        mockMvc.perform(get("/api/devices/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Pixel"))
                .andExpect(jsonPath("$.brand").value("Google"));
    }

    @Test
    void shouldReturn404WhenDeviceNotFound() throws Exception {
        mockMvc.perform(get("/api/devices/{id}", 9999))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldGetAllDevices() throws Exception {
        createDevice("Phone", "Apple", DeviceState.AVAILABLE);
        createDevice("Laptop", "Dell", DeviceState.IN_USE);

        mockMvc.perform(get("/api/devices"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    // ---------- UPDATE ----------

    @Test
    void shouldUpdateDevice() throws Exception {
        long id = createDevice("Laptop", "Dell", DeviceState.AVAILABLE);

        var update = new UpdateDeviceRequest(
                "Laptop Pro",
                "Dell",
                DeviceState.IN_USE
        );

        mockMvc.perform(put("/api/devices/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Laptop Pro"))
                .andExpect(jsonPath("$.state").value("IN_USE"));
    }

    @Test
    void shouldRejectNameChangeWhenDeviceIsInUse() throws Exception {
        long id = createDevice("Router", "TP-Link", DeviceState.IN_USE);

        var update = new UpdateDeviceRequest(
                "NewRouter",
                "TP-Link",
                DeviceState.IN_USE
        );

        mockMvc.perform(put("/api/devices/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Name and brand cannot be updated when device is IN_USE"));
    }

    // ---------- PATCH ----------

    @Test
    void shouldPartiallyUpdateDevice() throws Exception {
        long id = createDevice("Watch", "Apple", DeviceState.AVAILABLE);

        var patch = """
                { "state": "IN_USE" }
                """;

        mockMvc.perform(patch("/api/devices/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patch))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.state").value("IN_USE"));
    }

    // ---------- DELETE ----------

    @Test
    void shouldDeleteDevice() throws Exception {
        long id = createDevice("Mouse", "Logitech", DeviceState.AVAILABLE);

        mockMvc.perform(delete("/api/devices/{id}", id))
                .andExpect(status().isOk());
    }

    @Test
    void shouldRejectDeleteWhenDeviceIsInUse() throws Exception {
        long id = createDevice("Server", "Dell", DeviceState.IN_USE);

        mockMvc.perform(delete("/api/devices/{id}", id))
                .andExpect(status().isBadRequest());
    }

    // ---------- Helper ----------

    private long createDevice(String name, String brand, DeviceState state) throws Exception {
        var request = new CreateDeviceRequest(name, brand, state);

        var response = mockMvc.perform(post("/api/devices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        return objectMapper.readTree(response).get("id").asLong();
    }
}
