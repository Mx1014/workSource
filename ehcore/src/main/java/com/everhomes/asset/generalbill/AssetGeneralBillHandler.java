package com.everhomes.asset.generalbill;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.address.Address;
import com.everhomes.address.AddressProvider;
import com.everhomes.asset.AssetProvider;
import com.everhomes.asset.GeneralBillHandler;
import com.everhomes.rest.asset.AssetSourceType;
import com.everhomes.rest.asset.BillItemDTO;
import com.everhomes.rest.common.ServiceModuleConstants;
import com.everhomes.rest.promotion.order.GoodDTO;

/**
 * @author created by ycx
 * @date 上午11:45:54
 */
@Component(GeneralBillHandler.GENERALBILL_PREFIX + AssetSourceType.ASSET_MODULE)
public class AssetGeneralBillHandler implements GeneralBillHandler{

	@Autowired
	private AssetProvider assetProvider;
	
	@Autowired
	private AddressProvider addressProvider;

	public String getPaymentExtendInfo(BillItemDTO billItemDTO) {
		//资产：项目-楼栋-门牌
		String projectName = assetProvider.getProjectNameByBillID(billItemDTO.getBillId());
		String buildingName = billItemDTO.getBuildingName();
		String apartmentName = billItemDTO.getApartmentName();
		return projectName + "-" + buildingName + "-" + apartmentName;
	}

	/**
     * 组装商品的基本信息
     * @param billItemDTO
     * @return
     */
	public GoodDTO getGoodsInfo(BillItemDTO billItemDTO) {
		//服务类别	域空间	服务提供方标识1	服务提供方标识2	服务提供方标识3	服务提供方标识4	服务提供方标识5	服务提供方名称	商品标识		商品名称	商品说明	数量	金额
		//资产	NS			项目			楼栋			门牌			/			/		项目-楼栋-门牌	费项ID	租金/水费/电费	    面积	/	x
		GoodDTO good = new GoodDTO();
		good.setNamespace("NS");
		//资产：项目-楼栋-门牌
		String projectName = assetProvider.getProjectNameByBillID(billItemDTO.getBillId());
		String buildingName = billItemDTO.getBuildingName();
		String apartmentName = billItemDTO.getApartmentName();
		good.setTag1(projectName);
		good.setTag2(buildingName);
		good.setTag3(apartmentName);
		good.setServeType(ServiceModuleConstants.ASSET_MODULE + "");
		good.setServeApplyName(projectName + "-" + buildingName + "-" + apartmentName);//服务提供方名称
		good.setGoodTag(billItemDTO.getBillItemId() + "");//商品标识
		good.setGoodName(billItemDTO.getBillItemName());//商品名称
//		Address address = addressProvider.findAddressById(billItemDTO.getAddressId());
//		if(address != null) {
//			good.setGoodDescription(address.getChargeArea() + "");
//		}
		return good;
	}

}
