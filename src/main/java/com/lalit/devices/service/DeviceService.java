package com.lalit.devices.service;

import com.lalit.devices.model.Device;
import com.lalit.devices.model.DeviceState;

import java.util.List;

public interface DeviceService {

    Device create(Device device);

    Device getById(Long id);

    List<Device> getAll();

    List<Device> getByBrand(String brand);

    List<Device> getByState(DeviceState state);

    void delete(Long id);
}
