package com.everhomes.asset.generalbill;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.asset.AssetProvider;
import com.everhomes.asset.GeneralBillHandler;
import com.everhomes.rest.asset.AssetSourceType;
import com.everhomes.rest.asset.BillItemDTO;

/**
 * @author created by ycx
 * @date 上午11:45:54
 */
@Component(GeneralBillHandler.GENERALBILL_PREFIX + AssetSourceType.ASSET_MODULE)
public class AssetGeneralBillHandler implements GeneralBillHandler{

	@Autowired
	private AssetProvider assetProvider;

	public String getPaymentExtendInfo(BillItemDTO billItemDTO) {
		//资产：项目-楼栋-门牌
		String projectName = assetProvider.getProjectNameByBillID(billItemDTO.getBillId());
		String buildingName = billItemDTO.getBuildingName();
		String apartmentName = billItemDTO.getApartmentName();
		return projectName + "-" + buildingName + "-" + apartmentName;
	}

}
