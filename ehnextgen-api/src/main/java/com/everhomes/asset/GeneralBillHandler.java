package com.everhomes.asset;

import java.util.List;

import com.everhomes.rest.asset.AssetGeneralBillMappingCmd;
import com.everhomes.rest.asset.BillItemDTO;
import com.everhomes.serviceModuleApp.ServiceModuleApp;

/**
 * @author created by ycx
 * @date 下午3:33:01
 */
public interface GeneralBillHandler {
    static final String GENERALBILL_PREFIX = "GeneralBill-";
    
    /**
     * 创建或更新缴费模块与其他模块的映射关系
     * @param assetInstanceConfigDTO
     */
    default void createOrUpdateAssetModuleAppMapping(ServiceModuleApp app) {
    	
    }
    
    /**
     * 获取统一账单的映射关系
     * @param cmd
     * @return
     */
    default List<AssetModuleAppMapping> findAssetModuleAppMapping(AssetGeneralBillMappingCmd cmd){
    	return null;
    }
    
    
    /**
	 * 支付下单的时候需要根据不同的账单类型组装不同的订单说明
	 * @param cmd 下单请求信息
	 * @param billGroup 帐单组
	 * @return 扩展信息
	 */
    default String getPaymentExtendInfo(BillItemDTO billItemDTO) {
    	return null;
    }
    
}
