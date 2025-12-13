package com.lalit.devices.dto;

import com.lalit.devices.model.DeviceState;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateDeviceRequest(
        @NotBlank String name,
        @NotBlank String brand,
        @NotNull DeviceState state
) {}
