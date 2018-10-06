package com.everhomes.asset;

import java.util.List;

import com.everhomes.rest.asset.AssetGeneralBillMappingCmd;

/**
 * @author created by ycx
 * @date 下午3:33:01
 */
public interface GeneralBillHandler {
    static final String GENERALBILL_PREFIX = "GeneralBill-";
    
    /**
     * 获取统一账单的映射关系
     * @param cmd
     * @return
     */
    List<AssetModuleAppMapping> findAssetModuleAppMapping(AssetGeneralBillMappingCmd cmd);
    
    
    
}
