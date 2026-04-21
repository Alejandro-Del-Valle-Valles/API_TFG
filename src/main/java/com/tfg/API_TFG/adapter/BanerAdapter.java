package com.tfg.API_TFG.adapter;

import com.tfg.API_TFG.core.dto.BanerDTO;
import com.tfg.API_TFG.core.entity.Baner;

public class BanerAdapter {

    public static BanerDTO toDTO(Baner baner) {
        return new BanerDTO(baner.getId(), baner.getUrl(), baner.getEmpieza(), baner.getTermina());
    }
}
