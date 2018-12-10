package com.everhomes.asset.generalbill;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.asset.AssetModuleAppMapping;
import com.everhomes.asset.AssetProvider;
import com.everhomes.asset.GeneralBillHandler;
import com.everhomes.rest.asset.AssetGeneralBillMappingCmd;
import com.everhomes.rest.asset.AssetSourceType;
import com.everhomes.rest.asset.AssetSourceType.AssetSourceTypeEnum;
import com.everhomes.rest.asset.BillItemDTO;
import com.everhomes.rest.asset.modulemapping.AssetInstanceConfigDTO;
import com.everhomes.rest.asset.modulemapping.ParkingInstanceConfigDTO;
import com.everhomes.rest.asset.modulemapping.PrintInstanceConfigDTO;
import com.everhomes.rest.promotion.order.GoodDTO;
import com.everhomes.serviceModuleApp.ServiceModuleApp;
import com.everhomes.util.StringHelper;

/**
 * @author created by ycx
 * @date 下午6:46:56
 */
@Component(GeneralBillHandler.GENERALBILL_PREFIX + AssetSourceType.PARKING_MODULE)
public class ParkingGeneralBillHandler implements GeneralBillHandler{
	private Logger LOGGER = LoggerFactory.getLogger(ContractGeneralBillHandler.class);
	
	@Autowired
	private AssetProvider assetProvider;
	
	public void createOrUpdateAssetModuleAppMapping(ServiceModuleApp app) {
    	String instanceConfig = app.getInstanceConfig();
    	try {
    		if(instanceConfig != null && instanceConfig != "") {
	    		//格式化instanceConfig的json成对象
				AssetInstanceConfigDTO assetInstanceConfigDTO = (AssetInstanceConfigDTO) StringHelper.fromJsonString(instanceConfig, AssetInstanceConfigDTO.class);
				if(assetInstanceConfigDTO != null) {
					List<ParkingInstanceConfigDTO> parkingInstanceConfigDTOList = assetInstanceConfigDTO.getParkingInstanceConfigDTOList();
					for(ParkingInstanceConfigDTO parkingInstanceConfigDTO : parkingInstanceConfigDTOList) {
	    				AssetModuleAppMapping mapping = new AssetModuleAppMapping();
	    				mapping.setAssetCategoryId(assetInstanceConfigDTO.getCategoryId());
	    				mapping.setSourceType(AssetSourceTypeEnum.PARKING_MODULE.getSourceType());
	    				mapping.setNamespaceId(app.getNamespaceId());
	    				mapping.setOwnerType(parkingInstanceConfigDTO.getOwnerType());
	    				mapping.setOwnerId(parkingInstanceConfigDTO.getOwnerId());
	    				mapping.setBillGroupId(parkingInstanceConfigDTO.getBillGroupId());
	    				mapping.setChargingItemId(parkingInstanceConfigDTO.getChargingItemId());
	    				
	    		    	assetProvider.createOrUpdateAssetModuleAppMapping(mapping);
					}
	    		}
			}
    	}catch (Exception e) {
            LOGGER.error("failed to save mapping of parking payment in AssetPortalHandler, instanceConfig is={}", instanceConfig);
            e.printStackTrace();
        }
	}
	
	public List<AssetModuleAppMapping> findAssetModuleAppMapping(AssetGeneralBillMappingCmd cmd) {
		List<AssetModuleAppMapping> dtos = assetProvider.findAssetModuleAppMapping(cmd);
		return dtos;
	}

	public String getPaymentExtendInfo(BillItemDTO billItemDTO) {
		return billItemDTO.getGoodsServeApplyName();
	}

	/**
     * 组装商品的基本信息
     * @param billItemDTO
     * @return
     */
	public GoodDTO getGoodsInfo(BillItemDTO billItemDTO) {
		
		return null;
	}
}
