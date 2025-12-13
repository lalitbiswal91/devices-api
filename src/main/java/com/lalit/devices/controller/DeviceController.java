package com.lalit.devices.controller;


import com.lalit.devices.dto.CreateDeviceRequest;
import com.lalit.devices.dto.DeviceResponse;
import com.lalit.devices.model.Device;
import com.lalit.devices.model.DeviceState;
import com.lalit.devices.service.DeviceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/devices")
@RequiredArgsConstructor
public class DeviceController {

    private final DeviceService service;

    @PostMapping
    public DeviceResponse create(@Valid @RequestBody CreateDeviceRequest request) {
        Device device = new Device();
        device.setName(request.name());
        device.setBrand(request.brand());
        device.setState(request.state());

        Device saved = service.create(device);
        return toResponse(saved);
    }

    @GetMapping("/{id}")
    public DeviceResponse getById(@PathVariable Long id) {
        return toResponse(service.getById(id));
    }

    @GetMapping
    public List<DeviceResponse> getAll(
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) DeviceState state
    ) {
        if (brand != null) {
            return service.getByBrand(brand).stream()
                    .map(this::toResponse)
                    .toList();
        }

        if (state != null) {
            return service.getByState(state).stream()
                    .map(this::toResponse)
                    .toList();
        }

        return service.getAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
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
