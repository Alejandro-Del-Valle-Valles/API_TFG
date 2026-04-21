package com.tfg.API_TFG.core.service.interfaces;

import com.tfg.API_TFG.core.dto.BanerDTO;

import java.util.List;
import java.util.UUID;

public interface BanerService {
    List<BanerDTO> getTodayBaners();
    BanerDTO createBaner(BanerDTO banerDTO);
    BanerDTO updateBaner(String antiguaUrl, BanerDTO banerDTO);
    BanerDTO deleteBaner(String url);
}
