package com.lalit.devices.service.impl;

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

    private final DeviceRepository repository;

    @Override
    public Device create(Device device) {
        return repository.save(device);
    }

    @Override
    public Device getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new DeviceNotFoundException(id));
    }

    @Override
    public List<Device> getAll() {
        return repository.findAll();
    }

    @Override
    public List<Device> getByBrand(String brand) {
        return repository.findByBrandIgnoreCase(brand);
    }

    @Override
    public List<Device> getByState(DeviceState state) {
        return repository.findByState(state);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
