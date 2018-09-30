package com.everhomes.asset;

import java.util.List;

import com.everhomes.rest.asset.ListBillDetailResponse;

/**
 * @author created by ycx
 * @date 下午3:33:01
 */
public interface AssetGeneralBillHandler {
    static final String ASSET_GENERALBILL_PREFIX = "AssetGeneralBill-";
    
    /**
     * 获取统一账单的映射关系
     * @param billDetail
     */
    List<AssetModuleAppMapping> findAssetModuleAppMapping(ListBillDetailResponse billDetail);
    
    
    
}
