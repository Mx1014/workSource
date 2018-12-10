package com.everhomes.asset.generalbill;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.asset.AssetModuleAppMapping;
import com.everhomes.asset.AssetProvider;
import com.everhomes.asset.GeneralBillHandler;
import com.everhomes.rest.asset.AssetSourceType;
import com.everhomes.rest.asset.AssetSourceType.AssetSourceTypeEnum;
import com.everhomes.rest.asset.BillItemDTO;
import com.everhomes.rest.asset.modulemapping.AssetInstanceConfigDTO;
import com.everhomes.rest.asset.modulemapping.AssetMapContractConfig;
import com.everhomes.rest.promotion.order.GoodDTO;
import com.everhomes.serviceModuleApp.ServiceModuleApp;
import com.everhomes.serviceModuleApp.ServiceModuleAppService;
import com.everhomes.util.StringHelper;

/**
 * @author created by ycx
 * @date 上午11:45:54
 */
@Component(GeneralBillHandler.GENERALBILL_PREFIX + AssetSourceType.CONTRACT_MODULE)
public class ContractGeneralBillHandler implements GeneralBillHandler{
	private Logger LOGGER = LoggerFactory.getLogger(ContractGeneralBillHandler.class);

	@Autowired
	private AssetProvider assetProvider;
	
	@Autowired
    private ServiceModuleAppService serviceModuleAppService;
	
	public void createOrUpdateAssetModuleAppMapping(ServiceModuleApp app) {
    	String instanceConfig = app.getInstanceConfig();
    	try {
    		if(instanceConfig != null && instanceConfig != "") {
	    		//格式化instanceConfig的json成对象
				AssetInstanceConfigDTO assetInstanceConfigDTO = (AssetInstanceConfigDTO) StringHelper.fromJsonString(instanceConfig, AssetInstanceConfigDTO.class);
				if(assetInstanceConfigDTO != null) {
	    			Long originId = assetInstanceConfigDTO.getContractOriginId();
	    			ServiceModuleApp contractApp = serviceModuleAppService.findReleaseServiceModuleAppByOriginId(originId);
	    			if(contractApp != null) {
	    				AssetInstanceConfigDTO contractInstanceConfigDTO = 
	    						(AssetInstanceConfigDTO) StringHelper.fromJsonString(contractApp.getInstanceConfig(), AssetInstanceConfigDTO.class);
	    				AssetModuleAppMapping mapping = new AssetModuleAppMapping();
	    				mapping.setAssetCategoryId(assetInstanceConfigDTO.getCategoryId());
	    				mapping.setSourceType(AssetSourceTypeEnum.CONTRACT_MODULE.getSourceType());
	    				mapping.setSourceId(contractInstanceConfigDTO.getCategoryId());
	    				mapping.setNamespaceId(app.getNamespaceId());
	    				//组装个性化的config配置
	    		        AssetMapContractConfig config = new AssetMapContractConfig();
	    		    	config.setContractOriginId(assetInstanceConfigDTO.getContractOriginId());
	    		    	config.setContractChangeFlag(assetInstanceConfigDTO.getContractChangeFlag());
	    		    	mapping.setConfig(config.toString());
	    				
	    		    	assetProvider.createOrUpdateAssetModuleAppMapping(mapping);
	    			}
	    		}
			}
    	}catch (Exception e) {
            LOGGER.error("failed to save mapping of contract payment in AssetPortalHandler, instanceConfig is={}", instanceConfig);
            e.printStackTrace();
        }
	}
	
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
		
		return null;
	}

}
