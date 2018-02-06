// @formatter:off
package com.everhomes.forum;

import com.everhomes.portal.PortalPublishHandler;
import com.everhomes.rest.common.ServiceModuleConstants;
import com.everhomes.rest.forum.ForumActionData;
import com.everhomes.util.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component(PortalPublishHandler.PORTAL_PUBLISH_OBJECT_PREFIX + ServiceModuleConstants.FORUM_MODULE)
public class ForumPortalPublishHandler implements PortalPublishHandler {

    private static final Logger LOGGER=LoggerFactory.getLogger(ForumPortalPublishHandler.class);

	@Override
	public String publish(Integer namespaceId, String instanceConfig, String itemLabel) {


		return instanceConfig;
	}

	@Override
	public String getItemActionData(Integer namespaceId, String instanceConfig) {
		return instanceConfig;
	}

	@Override
	public String getAppInstanceConfig(Integer namespaceId, String actionData) {
		return actionData;
	}

	@Override
	public String processInstanceConfig(String instanceConfig) {
		return instanceConfig;
	}

	@Override
	public String getCustomTag(Integer namespaceId, Long moudleId, String actionData, String instanceConfig) {

		ForumActionData actionDataObj = (ForumActionData) StringHelper.fromJsonString(actionData, ForumActionData.class);

		if(actionDataObj != null && actionDataObj.getForumEntryId() != null){
			return String.valueOf(actionDataObj.getForumEntryId());
		}

		return null;
	}

	@Override
	public Long getWebMenuId(Integer namespaceId, Long moudleId, String actionData, String instanceConfig) {
		return 42050000L;
	}
}
