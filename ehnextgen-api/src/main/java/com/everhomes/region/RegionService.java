package com.everhomes.region;

import com.everhomes.rest.region.*;

import java.util.List;

/**
 * Created by sfyan on 2016/10/11.
 */
public interface RegionService {

    /**
     * 查询区域区号
     * @return
     */
    List<RegionCodeDTO> listRegionCodes();

    /**
     * 创建区域区号
     * @param dto
     */
    void createRegionCode(RegionCodeDTO dto);

    /**
     * 修改区域区号
     * @param dto
     */
    void updateRegionCode(RegionCodeDTO dto);

    RegionDTO findRegionById(GetRegionCommand cmd);

    RegionTreeResponse regionTree(RegionTreeCommand cmd);
}
