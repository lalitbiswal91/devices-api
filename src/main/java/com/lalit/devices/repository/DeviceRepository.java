package com.lalit.devices.repository;

import com.lalit.devices.model.Device;
import com.lalit.devices.model.DeviceState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeviceRepository extends JpaRepository<Device, Long> {

    List<Device> findByBrandIgnoreCase(String brand);

    List<Device> findByState(DeviceState state);
}
