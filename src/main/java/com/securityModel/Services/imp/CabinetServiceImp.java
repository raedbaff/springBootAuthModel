package com.securityModel.Services.imp;

import com.securityModel.Services.CabinetService;
import com.securityModel.models.Cabinet;
import com.securityModel.repository.CabinetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service

public class CabinetServiceImp implements CabinetService {
    @Autowired
    CabinetRepository cabinetRepository;
    @Override
    public Cabinet AddCabinet(Cabinet cabinet) {
        return cabinetRepository.save(cabinet);
    }

    @Override
    public Cabinet GetCabinet(Long id) {
        return cabinetRepository.findById(id).get();
    }

    @Override
    public void DeleteCabinet(Long id) {
        cabinetRepository.deleteById(id);

    }
}
