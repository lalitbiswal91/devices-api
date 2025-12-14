package com.lalit.devices.service.impl;

import com.lalit.devices.dto.CreateDeviceRequest;
import com.lalit.devices.dto.UpdateDeviceRequest;
import com.lalit.devices.exception.DeviceNotFoundException;
import com.lalit.devices.model.Device;
import com.lalit.devices.model.DeviceState;
import com.lalit.devices.repository.DeviceRepository;
import com.lalit.devices.service.DeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeviceServiceImpl implements DeviceService {

    private final DeviceRepository deviceRepository;

    @Override
    public Device create(CreateDeviceRequest createDeviceRequest) {
        Device device = new Device();
        device.setName(createDeviceRequest.name());
        device.setBrand(createDeviceRequest.brand());
        device.setState(createDeviceRequest.state());

        return deviceRepository.save(device);
    }


    @Override
    public Device update(Long id, UpdateDeviceRequest updateDeviceRequest) {
        Device existing = getById(id);

        validateNameBrandWhenInUse(existing, updateDeviceRequest.name(), updateDeviceRequest.brand());

        existing.setName(updateDeviceRequest.name());
        existing.setBrand(updateDeviceRequest.brand());
        existing.setState(updateDeviceRequest.state());

        return deviceRepository.save(existing);
    }


    @Override
    public Device partialUpdate(Long id, UpdateDeviceRequest updateDeviceRequest) {
        Device existing = getById(id);

        validateNameBrandWhenInUse(existing, updateDeviceRequest.name(), updateDeviceRequest.brand());

        if (updateDeviceRequest.name() != null) {
            existing.setName(updateDeviceRequest.name());
        }
        if (updateDeviceRequest.brand() != null) {
            existing.setBrand(updateDeviceRequest.brand());
        }
        if (updateDeviceRequest.state() != null) {
            existing.setState(updateDeviceRequest.state());
        }

        return deviceRepository.save(existing);
    }

    @Override
    public Device getById(Long id) {
        return deviceRepository.findById(id)
                .orElseThrow(() -> new DeviceNotFoundException(id));
    }

    @Override
    public List<Device> getAll() {
        return deviceRepository.findAll();
    }

    @Override
    public List<Device> getByBrand(String brand) {
        return deviceRepository.findByBrandIgnoreCase(brand);
    }

    @Override
    public List<Device> getByState(DeviceState state) {
        return deviceRepository.findByState(state);
    }


    @Override
    public void delete(Long id) {
        Device device = getById(id);

        if (device.getState() == DeviceState.IN_USE) {
            throw new IllegalStateException("IN_USE devices cannot be deleted");
        }

        deviceRepository.delete(device);
    }

    private void validateNameBrandWhenInUse(Device device, String newName, String newBrand) {
        if (device.getState() == DeviceState.IN_USE &&
                ((newName != null && !newName.equals(device.getName())) ||
                        (newBrand != null && !newBrand.equals(device.getBrand())))) {
            throw new IllegalStateException(
                    "Name and brand cannot be updated when device is IN_USE"
            );
        }
    }
}