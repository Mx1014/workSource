// @formatter:off
package com.everhomes.contract;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.everhomes.rest.portal.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.openapi.ContractProvider;
import com.everhomes.portal.PortalPublishHandler;
import com.everhomes.rest.common.ServiceModuleConstants;
import com.everhomes.rest.news.NewsStatus;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.StringHelper;

@Component(PortalPublishHandler.PORTAL_PUBLISH_OBJECT_PREFIX + ServiceModuleConstants.CONTRACT_MODULE)
public class ContractPortalPublishHandler implements PortalPublishHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(ContractPortalPublishHandler.class);

	@Autowired
	private ContractProvider contractProvider;

	@Autowired
	private ConfigurationProvider configurationProvider;

	@Override
	public String publish(Integer namespaceId, String instanceConfig, String appName, HandlerPublishCommand cmd) {
		LOGGER.error("publish contract. instanceConfig = {}, itemLabel = {}", instanceConfig, appName);
		ContractInstanceConfig contractInstanceConfig = (ContractInstanceConfig) StringHelper
				.fromJsonString(instanceConfig, ContractInstanceConfig.class);

		if (contractInstanceConfig == null) {
			contractInstanceConfig = new ContractInstanceConfig();
		}
		if (contractInstanceConfig.getCategoryId() == null) {
			ContractCategory contractCategory = createContractCategory(namespaceId, appName, contractInstanceConfig);
			contractInstanceConfig.setCategoryId(contractCategory.getId());
			contractInstanceConfig.setContractApplicationScene(contractInstanceConfig.getContractApplicationScene());
			contractInstanceConfig.setPrintSwitchStatus(contractInstanceConfig.getPrintSwitchStatus());
			contractInstanceConfig.setEditorSwitchStatus(contractInstanceConfig.getEditorSwitchStatus());
		} else {
			updateContractCategory(namespaceId, contractInstanceConfig, appName);
		}
		return StringHelper.toJsonString(contractInstanceConfig);
	}

	@Override
	public String processInstanceConfig(Integer namespaceId, String instanceConfig, HandlerProcessInstanceConfigCommand cmd) {
		return instanceConfig;
	}

	@Override
	public String getItemActionData(Integer namespaceId, String instanceConfig, HandlerGetItemActionDataCommand cmd) {
		ContractInstanceConfig map = (ContractInstanceConfig) StringHelper.fromJsonString(instanceConfig,
				ContractInstanceConfig.class);
		String categoryId = String.valueOf(map.getCategoryId());
		String homeUrl = configurationProvider.getValue("home.url", "");
		String contractAppUrl = configurationProvider.getValue("contract.app.url", "");
		Map<String, String> variables = new HashMap<String, String>();
		variables.put("categoryId", categoryId);
		variables.put("home.url", homeUrl);
		map.setUrl(StringHelper.interpolate(contractAppUrl, variables));
		return StringHelper.toJsonString(map);
				//StringHelper.toJsonString(StringHelper.interpolate(contractAppUrl, variables));
	}

	@Override
	public String getAppInstanceConfig(Integer namespaceId, String actionData, HandlerGetAppInstanceConfigCommand cmd) {
		return actionData;
	}

	final Pattern pattern = Pattern.compile("^.*\"categoryId\":[\\s]*([\\d]*)");

	@Override
	public String getCustomTag(Integer namespaceId, Long moudleId, String instanceConfig, HandlerGetCustomTagCommand cmd) {
		if (instanceConfig != null && instanceConfig.length() != 0) {
			Matcher m = pattern.matcher(instanceConfig);
			if (m.find()) {
				return m.group(1);
			}
		}
		LOGGER.info("ServiceAlliancePortalPublishHandler instanceConfig = {}", instanceConfig);
		return null;
	}

	@Override
	public Long getWebMenuId(Integer namespaceId, Long moudleId, String instanceConfig) {
		return null;
	}

	private ContractCategory createContractCategory(Integer namespaceId, String name, ContractInstanceConfig contractInstanceConfig) {
		User user = UserContext.current().getUser();
		ContractCategory contractCategory = new ContractCategory();
		contractCategory.setNamespaceId(namespaceId);
		contractCategory.setOwnerType("0");
		contractCategory.setOwnerId(0L);
		contractCategory.setParentId(0L);
		contractCategory.setName(name);
		contractCategory.setStatus(NewsStatus.ACTIVE.getCode());
		contractCategory.setCreatorUid(user.getId());
		contractCategory.setDeleteUid(user.getId());
		contractCategory.setContractApplicationScene(contractInstanceConfig.getContractApplicationScene());
		contractCategory.setContractApplicationScene(contractInstanceConfig.getPrintSwitchStatus());
		contractCategory.setEditorSwitchStatus(contractInstanceConfig.getEditorSwitchStatus());
		contractProvider.createContractCategory(contractCategory);
		return contractCategory;
	}

	private ContractCategory updateContractCategory(Integer namespaceId, ContractInstanceConfig contractInstanceConfig, String name) {
		ContractCategory contractCategory = contractProvider.findContractCategoryById(contractInstanceConfig.getCategoryId());
		if (null != contractCategory) {
			contractCategory.setName(name);
			contractCategory.setContractApplicationScene(contractInstanceConfig.getContractApplicationScene());
			contractCategory.setsetPrintSwitchStatus(contractInstanceConfig.getPrintSwitchStatus());
			contractCategory.setEditorSwitchStatus(contractInstanceConfig.getEditorSwitchStatus());
			contractProvider.updateContractCategory(contractCategory);
		} else {
			LOGGER.error("news category is null. contractInstanceConfig={}", contractInstanceConfig);
		}
		return contractCategory;
	}
}
