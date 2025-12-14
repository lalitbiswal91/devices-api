package com.lalit.devices.controller;


import com.lalit.devices.dto.CreateDeviceRequest;
import com.lalit.devices.dto.DeviceResponse;
import com.lalit.devices.dto.UpdateDeviceRequest;
import com.lalit.devices.model.Device;
import com.lalit.devices.model.DeviceState;
import com.lalit.devices.service.DeviceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/devices")
@RequiredArgsConstructor
public class DeviceController {

    private final DeviceService deviceService;

    @PostMapping
    public DeviceResponse create(@Valid @RequestBody CreateDeviceRequest createDeviceRequest) {
        log.info("Creating device: {} - {}", createDeviceRequest.name(), createDeviceRequest.brand());
        Device createdDevice = deviceService.create(createDeviceRequest);
        DeviceResponse deviceResponse = toResponse(createdDevice);
        log.info("Created device with id: {}", deviceResponse.id());
        return deviceResponse;
    }

    @GetMapping("/{id}")
    public DeviceResponse getById(@PathVariable Long id) {
        log.info("Fetching device with id: {}", id);
        return toResponse(deviceService.getById(id));
    }

    @GetMapping
    public List<DeviceResponse> getAll(
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) DeviceState state
    ) {
        log.info("Fetching devices with brand={} state={}", brand, state);
        List<DeviceResponse> responses;

        if (brand != null) {
            responses = deviceService.getByBrand(brand).stream()
                    .map(this::toResponse)
                    .toList();
        } else if (state != null) {
            responses = deviceService.getByState(state).stream()
                    .map(this::toResponse)
                    .toList();
        } else {
            responses = deviceService.getAll().stream()
                    .map(this::toResponse)
                    .toList();
        }
        log.info("Fetched {} devices", responses.size());
        return responses;

    }

    @PutMapping("/{id}")
    public DeviceResponse update(
            @PathVariable Long id,
            @RequestBody UpdateDeviceRequest request
    ) {
        log.info("Updating device {} with data: {}", id, request);
        return toResponse(deviceService.update(id, request));
    }

    @PatchMapping("/{id}")
    public DeviceResponse partialUpdate(
            @PathVariable Long id,
            @RequestBody UpdateDeviceRequest request
    ) {
        log.info("Partially updating device {} with data: {}", id, request);
        return toResponse(deviceService.partialUpdate(id, request));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        log.info("Deleting device with id: {}", id);
        deviceService.delete(id);
        log.info("Deleted device with id: {}", id);
    }


    private DeviceResponse toResponse(Device device) {
        return new DeviceResponse(
                device.getId(),
                device.getName(),
                device.getBrand(),
                device.getState(),
                device.getCreatedAt()
        );
    }

}
