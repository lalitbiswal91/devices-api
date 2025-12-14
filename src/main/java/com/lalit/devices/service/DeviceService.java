package com.lalit.devices.service;

import com.lalit.devices.dto.CreateDeviceRequest;
import com.lalit.devices.dto.UpdateDeviceRequest;
import com.lalit.devices.model.Device;
import com.lalit.devices.model.DeviceState;

import java.util.List;

public interface DeviceService {

    Device create(CreateDeviceRequest createDeviceRequest);

    Device getById(Long id);

    List<Device> getAll();

    List<Device> getByBrand(String brand);

    List<Device> getByState(DeviceState state);

    Device update(Long id, UpdateDeviceRequest updateDeviceRequest);        // PUT

    Device partialUpdate(Long id, UpdateDeviceRequest updateDeviceRequest); // PATCH

    void delete(Long id);
}
