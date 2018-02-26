// @formatter:off
package com.everhomes.forum;

import com.everhomes.naming.NameMapper;
import com.everhomes.portal.PortalPublishHandler;
import com.everhomes.rest.common.ServiceModuleConstants;
import com.everhomes.rest.forum.FindDefaultForumCommand;
import com.everhomes.rest.forum.FindDefaultForumResponse;
import com.everhomes.rest.forum.ForumActionData;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.tables.pojos.EhForumCategories;
import com.everhomes.util.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component(PortalPublishHandler.PORTAL_PUBLISH_OBJECT_PREFIX + ServiceModuleConstants.FORUM_MODULE)
public class ForumPortalPublishHandler implements PortalPublishHandler {

    private static final Logger LOGGER=LoggerFactory.getLogger(ForumPortalPublishHandler.class);

    @Autowired
    private ForumService forumService;

	@Autowired
	private ForumProvider forumProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public String publish(Integer namespaceId, String instanceConfig, String appName) {

		ForumActionData actionDataObj = (ForumActionData) StringHelper.fromJsonString(instanceConfig, ForumActionData.class);
		if(actionDataObj == null){
			actionDataObj = new ForumActionData();
		}
		if(actionDataObj.getForumEntryId() == null){
			FindDefaultForumCommand cmd = new FindDefaultForumCommand();
			cmd.setNamespaceId(namespaceId);
			FindDefaultForumResponse defaultForum = forumService.findDefaultForum(cmd);
			if(defaultForum != null || defaultForum.getDefaultForum() != null){
				ForumCategory forumCategory = createForumCategory(namespaceId, defaultForum.getDefaultForum().getId(), appName);
				actionDataObj.setForumEntryId(forumCategory.getEntryId());
			}
		}

		return actionDataObj.toString();
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


	private ForumCategory createForumCategory(Integer namespaceId, Long forumId, String name){
		ForumCategory category = new ForumCategory();
		category.setNamespaceId(namespaceId);
		category.setForumId(forumId);
		long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhForumCategories.class));
		category.setId(id);
		category.setEntryId(id);
		category.setName(name);
		category.setActivityEntryId(0L);
		category.setCreateTime(new Timestamp(System.currentTimeMillis()));
		category.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		forumProvider.createForumCategory(category);
		return category;
	}
}
