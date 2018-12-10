package com.everhomes.asset.generalbill;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.asset.AssetModuleAppMapping;
import com.everhomes.asset.AssetProvider;
import com.everhomes.asset.GeneralBillHandler;
import com.everhomes.rest.asset.AssetSourceType;
import com.everhomes.rest.asset.BillItemDTO;
import com.everhomes.rest.asset.AssetSourceType.AssetSourceTypeEnum;
import com.everhomes.rest.asset.modulemapping.AssetInstanceConfigDTO;
import com.everhomes.rest.asset.modulemapping.AssetMapContractConfig;
import com.everhomes.rest.asset.modulemapping.AssetMapEnergyConfig;
import com.everhomes.rest.common.ServiceModuleConstants;
import com.everhomes.rest.promotion.order.GoodDTO;
import com.everhomes.serviceModuleApp.ServiceModuleApp;
import com.everhomes.serviceModuleApp.ServiceModuleAppService;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.StringHelper;

/**
 * @author created by ycx
 * @date 上午11:45:54
 */
@Component(GeneralBillHandler.GENERALBILL_PREFIX + AssetSourceType.ENERGY_MODULE)
public class EnergyGeneralBillHandler implements GeneralBillHandler{
	private Logger LOGGER = LoggerFactory.getLogger(EnergyGeneralBillHandler.class);

	@Autowired
	private AssetProvider assetProvider;
	
	public void createOrUpdateAssetModuleAppMapping(ServiceModuleApp app) {
    	String instanceConfig = app.getInstanceConfig();
    	try {
    		if(instanceConfig != null && instanceConfig != "") {
	    		//格式化instanceConfig的json成对象
				AssetInstanceConfigDTO assetInstanceConfigDTO = (AssetInstanceConfigDTO) StringHelper.fromJsonString(instanceConfig, AssetInstanceConfigDTO.class);
				if(assetInstanceConfigDTO != null) {
					AssetModuleAppMapping mapping = new AssetModuleAppMapping();
    				mapping.setAssetCategoryId(assetInstanceConfigDTO.getCategoryId());
    				mapping.setSourceType(AssetSourceTypeEnum.ENERGY_MODULE.getSourceType());
    				mapping.setSourceId(null);
    				mapping.setNamespaceId(app.getNamespaceId());
    				//组装个性化的config配置
    				AssetMapEnergyConfig config = new AssetMapEnergyConfig();
    				config.setEnergyFlag(assetInstanceConfigDTO.getEnergyFlag());
    		    	mapping.setConfig(config.toString());
    				
    		    	assetProvider.createOrUpdateAssetModuleAppMapping(mapping);
	    		}
			}
    	}catch (Exception e) {
            LOGGER.error("failed to save mapping of energy payment in AssetPortalHandler, instanceConfig is={}", instanceConfig);
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
		return good;
	}

}
