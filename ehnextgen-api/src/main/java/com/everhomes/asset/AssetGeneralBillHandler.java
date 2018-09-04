package com.everhomes.asset;

/**
 * @author created by ycx
 * @date 下午3:33:01
 */
public interface AssetGeneralBillHandler {
    static final String ASSET_GENERALBILL_PREFIX = "AssetGeneralBill-";
    
    /**
     * 
     * @param namespaceId
     * @param instanceConfig
     * @param appName
     */
     void payNotifyBillSourceModule(Long billId);
    
}
