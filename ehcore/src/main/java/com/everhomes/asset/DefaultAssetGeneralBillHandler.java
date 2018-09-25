//@formatter:off
package com.everhomes.asset;

import org.springframework.stereotype.Component;

import com.everhomes.rest.asset.ListBillDetailResponse;
import com.everhomes.rest.common.AssetModuleNotifyConstants;

/**
 * @author created by ycx
 * @date 下午4:59:35
 */
@Component(AssetGeneralBillHandler.ASSET_GENERALBILL_PREFIX + AssetModuleNotifyConstants.ASSET_MODULE)
public class DefaultAssetGeneralBillHandler implements AssetGeneralBillHandler{

	@Override
	public void payNotifyBillSourceModule(ListBillDetailResponse billDetail) {
		// TODO Auto-generated method stub
		
	}

}
