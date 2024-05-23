package com.securityModel.Services;

import com.securityModel.models.Cabinet;

public interface CabinetService {
    public Cabinet AddCabinet(Cabinet cabinet);
    public Cabinet GetCabinet(Long id);
    public void DeleteCabinet(Long id);

}
