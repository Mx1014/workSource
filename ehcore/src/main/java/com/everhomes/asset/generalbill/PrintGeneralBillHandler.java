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
import com.everhomes.rest.asset.BillItemDTO;
import com.everhomes.rest.asset.AssetSourceType.AssetSourceTypeEnum;
import com.everhomes.rest.asset.modulemapping.AssetInstanceConfigDTO;
import com.everhomes.rest.asset.modulemapping.AssetMapContractConfig;
import com.everhomes.rest.asset.modulemapping.PrintInstanceConfigDTO;
import com.everhomes.serviceModuleApp.ServiceModuleApp;
import com.everhomes.serviceModuleApp.ServiceModuleAppService;
import com.everhomes.util.StringHelper;

/**
 * @author created by ycx
 * @date 上午11:45:54
 */
@Component(GeneralBillHandler.GENERALBILL_PREFIX + AssetSourceType.PRINT_MODULE)
public class PrintGeneralBillHandler implements GeneralBillHandler{
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
					List<PrintInstanceConfigDTO> printInstanceConfigDTOList = assetInstanceConfigDTO.getPrintInstanceConfigDTOList();
					for(PrintInstanceConfigDTO printInstanceConfigDTO : printInstanceConfigDTOList) {
	    				AssetModuleAppMapping mapping = new AssetModuleAppMapping();
	    				mapping.setAssetCategoryId(assetInstanceConfigDTO.getCategoryId());
	    				mapping.setSourceType(AssetSourceTypeEnum.PRINT_MODULE.getSourceType());
	    				mapping.setNamespaceId(app.getNamespaceId());
	    				mapping.setOwnerType(printInstanceConfigDTO.getOwnerType());
	    				mapping.setOwnerId(printInstanceConfigDTO.getOwnerId());
	    				mapping.setBillGroupId(printInstanceConfigDTO.getBillGroupId());
	    				mapping.setChargingItemId(printInstanceConfigDTO.getChargingItemId());
	    				
	    		    	assetProvider.createOrUpdateAssetModuleAppMapping(mapping);
					}
	    		}
			}
    	}catch (Exception e) {
            LOGGER.error("failed to save mapping of print payment in AssetPortalHandler, instanceConfig is={}", instanceConfig);
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

}
