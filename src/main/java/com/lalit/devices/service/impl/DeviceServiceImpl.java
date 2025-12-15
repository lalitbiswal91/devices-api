package com.lalit.devices.service.impl;

import com.lalit.devices.dto.CreateDeviceRequest;
import com.lalit.devices.dto.UpdateDeviceRequest;
import com.lalit.devices.exception.DeviceNotFoundException;
import com.lalit.devices.model.Device;
import com.lalit.devices.model.DeviceState;
import com.lalit.devices.repository.DeviceRepository;
import com.lalit.devices.service.DeviceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeviceServiceImpl implements DeviceService {

    private final DeviceRepository deviceRepository;

    @Override
    public Device create(CreateDeviceRequest createDeviceRequest) {
        log.info("Creating device with name={} brand={} state={}",
                createDeviceRequest.name(), createDeviceRequest.brand(), createDeviceRequest.state());

        var device = new Device();
        device.setName(createDeviceRequest.name());
        device.setBrand(createDeviceRequest.brand());
        device.setState(createDeviceRequest.state());

        var createdDevice = deviceRepository.save(device);
        log.info("Device created with id {}", createdDevice.getId());

        return createdDevice;
    }


    @Override
    public Device update(Long id, UpdateDeviceRequest updateDeviceRequest) {
        log.info("Updating device id: {}", id);
        var existingDevice = getById(id);

        validateNameBrandWhenInUse(existingDevice, updateDeviceRequest.name(), updateDeviceRequest.brand());

        existingDevice.setName(updateDeviceRequest.name());
        existingDevice.setBrand(updateDeviceRequest.brand());
        existingDevice.setState(updateDeviceRequest.state());

        var updatedDevice = deviceRepository.save(existingDevice);

        log.info("Updated device id: {}", updatedDevice.getId());
        return updatedDevice;
    }


    @Override
    public Device partialUpdate(Long id, UpdateDeviceRequest updateDeviceRequest) {
        log.info("Partially updating device id: {}", id);
        var existingDevice = getById(id);

        validateNameBrandWhenInUse(existingDevice, updateDeviceRequest.name(), updateDeviceRequest.brand());

        if (updateDeviceRequest.name() != null) {
            log.info("Updating name for device id={} from {} to {}", id, existingDevice.getName(), updateDeviceRequest.name());
            existingDevice.setName(updateDeviceRequest.name());
        }
        if (updateDeviceRequest.brand() != null) {
            log.info("Updating brand for device id={} from {} to {}", id, existingDevice.getBrand(), updateDeviceRequest.brand());
            existingDevice.setBrand(updateDeviceRequest.brand());
        }
        if (updateDeviceRequest.state() != null) {
            log.info("Updating state for device id={} from {} to {}", id, existingDevice.getState(), updateDeviceRequest.state());
            existingDevice.setState(updateDeviceRequest.state());
        }

        var updatedDevice = deviceRepository.save(existingDevice);
        log.info("Partial update completed for device id={}", updatedDevice.getId());
        return updatedDevice;
    }

    @Override
    public Device getById(Long id) {
        log.info("Fetching device by id={}", id);
        return deviceRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Device not found with id={}", id);
                    return new DeviceNotFoundException(id);
                });
    }

    @Override
    public List<Device> getAll() {
        log.info("Fetching all devices");
        var devices = deviceRepository.findAll();
        log.info("Fetched {} devices", devices.size());
        return devices;
    }

    @Override
    public List<Device> getByBrand(String brand) {
        log.info("Fetching devices by brand={}", brand);
        var devicesByBrand = deviceRepository.findByBrandIgnoreCase(brand);
        log.info("Found {} devices for brand={}", devicesByBrand.size(), brand);
        return devicesByBrand;
    }

    @Override
    public List<Device> getByState(DeviceState state) {
        log.info("Fetching devices by state={}", state);
        var devicesByState = deviceRepository.findByState(state);
        log.info("Found {} devices with state={}", devicesByState.size(), state);
        return devicesByState;
    }


    @Override
    public void delete(Long id) {
        log.info("Deleting device id={}", id);
        var existingDevice = getById(id);

        if (existingDevice.getState() == DeviceState.IN_USE) {
            log.warn("Trying to delete IN_USE device id={}", id);
            throw new IllegalStateException("IN_USE devices cannot be deleted");
        }

        deviceRepository.delete(existingDevice);
        log.info("Device deleted successfully id={}", id);
    }

    private void validateNameBrandWhenInUse(Device device, String newName, String newBrand) {
        if (device.getState() == DeviceState.IN_USE &&
                ((newName != null && !newName.equals(device.getName())) ||
                        (newBrand != null && !newBrand.equals(device.getBrand())))) {
            log.warn("Trying to update name/brand on IN_USE device id: {}", device.getId());
            throw new IllegalStateException(
                    "Name and brand cannot be updated when device is IN_USE"
            );
        }
    }
}