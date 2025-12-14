package com.lalit.devices.controller;


import com.lalit.devices.dto.CreateDeviceRequest;
import com.lalit.devices.dto.DeviceResponse;
import com.lalit.devices.dto.UpdateDeviceRequest;
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

    private final DeviceService deviceService;

    @PostMapping
    public DeviceResponse create(@Valid @RequestBody CreateDeviceRequest createDeviceRequest) {
        Device createdDevice = deviceService.create(createDeviceRequest);
        return toResponse(createdDevice);
    }

    @GetMapping("/{id}")
    public DeviceResponse getById(@PathVariable Long id) {
        return toResponse(deviceService.getById(id));
    }

    @GetMapping
    public List<DeviceResponse> getAll(
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) DeviceState state
    ) {
        if (brand != null) {
            return deviceService.getByBrand(brand).stream()
                    .map(this::toResponse)
                    .toList();
        }

        if (state != null) {
            return deviceService.getByState(state).stream()
                    .map(this::toResponse)
                    .toList();
        }

        return deviceService.getAll().stream()
                .map(this::toResponse)
                .toList();
    }

    /* ========= Full Update ========= */
    @PutMapping("/{id}")
    public DeviceResponse update(
            @PathVariable Long id,
            @RequestBody UpdateDeviceRequest request
    ) {
        return toResponse(deviceService.update(id, request));
    }

    /* ========= PArtial Update ========= */
    @PatchMapping("/{id}")
    public DeviceResponse partialUpdate(
            @PathVariable Long id,
            @RequestBody UpdateDeviceRequest request
    ) {
        return toResponse(deviceService.partialUpdate(id, request));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        deviceService.delete(id);
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
