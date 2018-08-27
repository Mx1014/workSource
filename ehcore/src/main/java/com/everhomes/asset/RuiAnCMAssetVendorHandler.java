package com.everhomes.asset;



import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.rest.contract.CMBill;
import com.everhomes.rest.contract.CMDataObject;
import com.everhomes.rest.contract.CMSyncObject;

/**
 * @author created by ycx
 * @date 上午11:43:56
 */
@Component(AssetVendorHandler.ASSET_VENDOR_PREFIX + "RUIANCM")
public class RuiAnCMAssetVendorHandler extends AssetVendorHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(RuiAnCMAssetVendorHandler.class);

    @Autowired
    private AssetProvider assetProvider;
    
    /**
	 * 同步瑞安CM的账单数据到左邻的数据库表中
	 */
	public void syncRuiAnCMBillToZuolin(CMSyncObject cmSyncObject, Integer namespaceId){
		List<CMDataObject> data = cmSyncObject.getData();
		if(data != null) {
			for(CMDataObject cmDataObject : data) {
				List<CMBill> bills = cmDataObject.getBill();
			}
		}
	}
    
}