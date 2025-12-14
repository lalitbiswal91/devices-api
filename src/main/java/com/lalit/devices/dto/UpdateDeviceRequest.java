package com.lalit.devices.dto;

import com.lalit.devices.model.DeviceState;

public record UpdateDeviceRequest(
        String name,
        String brand,
        DeviceState state
) {}
