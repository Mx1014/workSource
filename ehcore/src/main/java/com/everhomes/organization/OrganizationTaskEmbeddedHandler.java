// @formatter:off
package com.everhomes.organization;

import java.sql.Timestamp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.app.AppConstants;
import com.everhomes.category.CategoryConstants;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.forum.ForumEmbeddedHandler;
import com.everhomes.forum.Post;
import com.everhomes.forum.PostEntityTag;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;

@Component(OrganizationTaskEmbeddedHandler.FORUM_EMBEDED_OBJ_RESOLVER_PREFIX + AppConstants.APPID_ORGTASK)
public class OrganizationTaskEmbeddedHandler implements ForumEmbeddedHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(OrganizationTaskEmbeddedHandler.class);

	@Autowired
	private OrganizationService organizationService;

	@Autowired
	private OrganizationProvider organizationProvider;

	@Override
	public String renderEmbeddedObjectSnapshot(Post post) {
		return getOrganizationTaskRenderString(post);
	}

	@Override
	public String renderEmbeddedObjectDetails(Post post) {
		return getOrganizationTaskRenderString(post);
	}

	@Override
	public Post preProcessEmbeddedObject(Post post) {
		try {

			Organization organization = getOrganization(post);
			if(organization == null){
				LOGGER.error("Unable to find the organization.");
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
						"Unable to find the organization.");
			}
			this.checkUserHaveRightToNewTopic(post,organization);
			OrganizationTask task = new OrganizationTask();
			task.setOrganizationId(organization.getId());
			task.setOrganizationType(organization.getOrganizationType());
			task.setApplyEntityType(OrganizationTaskApplyEnityType.TOPIC.getCode());
			task.setApplyEntityId(0L); // 还没有帖子ID
			task.setTargetType(post.getTargetTag());
			task.setTargetId(organization.getId());
			task.setCreatorUid(post.getCreatorUid());
			task.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

			OrganizationTaskType taskType = getOrganizationTaskType(post);
			if(taskType != null) {
				task.setTaskType(taskType.getCode());
			}

			task.setTaskStatus(OrganizationTaskStatus.UNPROCESSED.getCode());
			task.setOperatorUid(0L);
			this.organizationProvider.createOrganizationTask(task);
			post.setEmbeddedId(task.getId());
			/*if(LOGGER.isDebugEnabled()) {
				LOGGER.debug("Create organization task, task=" + task);
			}*/
		} catch(Exception e) {
			LOGGER.error("Failed to pre-process the organization task, postId=" + post.getId() + ", creatorId=" + post.getCreatorUid() 
					+ ", subject=" + post.getSubject(), e);
		}

		return post;	
	}



	private void checkUserHaveRightToNewTopic(Post post,
			Organization organization) {
		User user = UserContext.current().getUser();
		PostEntityTag creatorTag = PostEntityTag.fromCode(post.getCreatorTag());
		if(creatorTag == null){
			LOGGER.error("creatorTag format is wrong.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"creatorTag format is wrong.");
		}
		if(!creatorTag.getCode().equals(PostEntityTag.USER.getCode())){
			OrganizationMember member = this.organizationProvider.findOrganizationMemberByOrgIdAndUId(user.getId(), organization.getId());
			if(member == null){
				LOGGER.error("could not found member in the organization.");
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
						"could not found member in the organization.");
			}
		}
	}

	@Override
	public Post postProcessEmbeddedObject(Post post) {
		Long taskId = post.getEmbeddedId();
		if(taskId == null) {
			LOGGER.error("could not get taskId.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"could not get taskId.");
		} 
		OrganizationTask task = this.organizationProvider.findOrganizationTaskById(taskId);
		if(task == null) {
			LOGGER.error("could not found task.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"could not found task.");
		}
		task.setApplyEntityType(OrganizationTaskApplyEnityType.TOPIC.getCode());
		task.setApplyEntityId(post.getId());
		this.organizationProvider.updateOrganizationTask(task);
		return post;
	}

	private Organization getOrganization(Post post) {
		PostEntityTag creatorTag = PostEntityTag.fromCode(post.getCreatorTag());
		PostEntityTag targetTag = PostEntityTag.fromCode(post.getTargetTag());

		Organization organization = null;
		if(creatorTag == null) {
			LOGGER.error("creatorTag could not be null.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"creatorTag could not be null.");
		}
		if(targetTag == null) {
			LOGGER.error("targetTag could not be null.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"targetTag could not be null.");
		}
		//PostEntityTag.USER
		switch(targetTag){
		case USER:
		case PM:
		case GARC:
			organization = this.organizationProvider.findOrganizationByCommunityIdAndOrgType(post.getVisibleRegionId(), post.getTargetTag());break;
		case GANC:
		case GAPS:
			organization = this.organizationProvider.findOrganizationById(post.getVisibleRegionId());break;
		default:
			LOGGER.error("targetTag format is wrong.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"targetTag format is wrong.");
		}

		return organization;
	}

	private OrganizationTaskType getOrganizationTaskType(Post post) {
		Long contentCategoryId = post.getContentCategory();

		if(contentCategoryId != null) {
			if(contentCategoryId == CategoryConstants.CATEGORY_ID_NOTICE) {
				return OrganizationTaskType.NOTICE;
			}
			if(contentCategoryId == CategoryConstants.CATEGORY_ID_REPAIRS) {
				return OrganizationTaskType.REPAIRS;
			}
			if(contentCategoryId == CategoryConstants.CATEGORY_ID_CONSULT_APPEAL) {
				return OrganizationTaskType.CONSULT_APPEAL;
			}
			if(contentCategoryId == CategoryConstants.CATEGORY_ID_COMPLAINT_ADVICE) {
				return OrganizationTaskType.COMPLAINT_ADVICE;
			}
		}

		LOGGER.error("contentCategory format is wrong.");
		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
				"contentCategory format is wrong.");
	}

	private String getOrganizationTaskRenderString(Post post) {
		String str = post.getEmbeddedJson();

		try {
			Long taskId = post.getEmbeddedId();
			if(taskId == null) {
				LOGGER.error("Failed to render the organization task, task id is null, postId=" + post.getId());
			} else {
				OrganizationTask task = this.organizationProvider.findOrganizationTaskById(taskId);
				if(task != null) {
					OrganizationTaskDTO taskDto = ConvertHelper.convert(task, OrganizationTaskDTO.class);
					str = StringHelper.toJsonString(taskDto);
				} else {
					LOGGER.error("Failed to render the organization task, task not found, postId=" + post.getId() + ", taskId=" + taskId);
				}
			}
		} catch(Exception e) {
			LOGGER.error("Failed to post-process the organization task, postId=" + post.getId() + ", creatorId=" + post.getCreatorUid() 
					+ ", subject=" + post.getSubject(), e);
		}

		return str;
	}
}
