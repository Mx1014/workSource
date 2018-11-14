package com.everhomes.portal;

import com.everhomes.rest.portal.HandlerGetAppInstanceConfigCommand;
import com.everhomes.rest.portal.HandlerGetItemActionDataCommand;
import com.everhomes.rest.portal.HandlerProcessInstanceConfigCommand;
import com.everhomes.rest.portal.HandlerPublishCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.asset.AssetModuleAppMapping;
import com.everhomes.asset.AssetService;
import com.everhomes.rest.asset.AssetSourceType.AssetSourceTypeEnum;
import com.everhomes.rest.common.ServiceModuleConstants;
import com.everhomes.rest.print.SiyinPrintInstanceConfig;

@Component(PortalPublishHandler.PORTAL_PUBLISH_OBJECT_PREFIX + ServiceModuleConstants.PRINT_MODULE)
public class SiyinPrintPortalPublishHandler implements PortalPublishHandler{
	
    private static final Logger LOGGER = LoggerFactory.getLogger(SiyinPrintPortalPublishHandler.class);
	
    @Autowired
    protected AssetService assetService;

	@Override
	public String publish(Integer namespaceId, String instanceConfig, String appName, HandlerPublishCommand cmd) {
		return instanceConfig;
	} 

	@Override
	public String processInstanceConfig(Integer namespaceId, String instanceConfig, HandlerProcessInstanceConfigCommand cmd) {
		return instanceConfig;
	}

	@Override
	public String getItemActionData(Integer namespaceId, String instanceConfig, HandlerGetItemActionDataCommand cmd) {
		return instanceConfig;
	}

	@Override
	public String getAppInstanceConfig(Integer namespaceId, String actionData, HandlerGetAppInstanceConfigCommand cmd) {
		return actionData;
	}
	
	private void saveChargeConfig(Integer namespaceId, SiyinPrintInstanceConfig config) {
		if (null == config.getChargeAppToken() || null == config.getBillGroupToken()
				|| null == config.getChargeItemTorken()) {
			return;
		}

		AssetModuleAppMapping cmd = new AssetModuleAppMapping();
		cmd.setNamespaceId(namespaceId);
		cmd.setSourceType(AssetSourceTypeEnum.PRINT_MODULE.getSourceType());
		cmd.setSourceId(ServiceModuleConstants.PRINT_MODULE);
		cmd.setAssetCategoryId(config.getChargeAppToken());
		cmd.setBillGroupId(config.getBillGroupToken());
		cmd.setChargingItemId(config.getChargeItemTorken());
		//assetService.createOrUpdateAssetMapping(cmd);
		LOGGER.info("saveChargeConfig:"+cmd.toString());
	}

}
