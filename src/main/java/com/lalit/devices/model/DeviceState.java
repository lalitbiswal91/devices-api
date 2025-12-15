package com.lalit.devices.model;

public enum DeviceState {
    AVAILABLE,
    IN_USE,
    INACTIVE;

    public static DeviceState getDeviceState(String value) {
        return switch (value.toUpperCase()) {
            case "AVAILABLE" -> AVAILABLE;
            case "IN_USE" -> IN_USE;
            case "INACTIVE" -> INACTIVE;
            default -> throw new IllegalArgumentException("Invalid device state: " + value);
        };
    }
}
