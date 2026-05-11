package com.tfg.API_TFG.core.service.interfaces;

import com.tfg.API_TFG.core.dto.BanerDTO;

import java.util.List;
import java.util.UUID;

public interface BanerService {
    List<BanerDTO> getTodayBaners();
    List<BanerDTO> getAll();
    BanerDTO createBaner(BanerDTO banerDTO);

    BanerDTO updateBaner(BanerDTO banerDTO);

    BanerDTO deleteBaner(int id);
}
