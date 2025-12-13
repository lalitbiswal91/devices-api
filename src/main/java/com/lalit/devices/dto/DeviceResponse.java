package com.lalit.devices.dto;

import com.lalit.devices.model.DeviceState;

import java.time.Instant;

public record DeviceResponse(
        Long id,
        String name,
        String brand,
        DeviceState state,
        Instant createdAt
) {}
