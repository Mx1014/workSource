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
				LOGGER.warn("Unable to find the organization.postId=" + post.getId() + ", creatorId=" + post.getCreatorUid() 
					+ ", subject=" + post.getSubject());
				post.setEmbeddedAppId(0L);
				return post;
				/*throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_CLASS_NOT_FOUND,
						"Unable to find the organization.");*/
			}
			OrganizationTask task = new OrganizationTask();
			task.setOrganizationId(organization.getId());
			task.setOrganizationType(organization.getOrganizationType());
			task.setApplyEntityType(OrganizationTaskApplyEnityType.TOPIC.getCode());
			task.setApplyEntityId(0L); // 还没有帖子ID
			task.setTargetType(post.getTargetTag());
			if(post.getTargetTag().equals(PostEntityTag.USER.getCode()))
				task.setTargetId(post.getCreatorUid() == null ? 0L:post.getCreatorUid());
			else
				task.setTargetId(organization.getId());
			task.setCreatorUid(post.getCreatorUid() == null ? 0L:post.getCreatorUid());
			task.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
			task.setUnprocessedTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

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
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_CLASS_NOT_FOUND,
					"Failed to pre-process the organization task.");
		}

		return post;	
	}

	@Override
	public Post postProcessEmbeddedObject(Post post) {
		Long taskId = post.getEmbeddedId();
		if(taskId == null) {
			LOGGER.warn("could not get taskId.");
			return post;
			/*throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"could not get taskId.");*/
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
			if(creatorTag.getCode().equals(PostEntityTag.PM.getCode()) || creatorTag.getCode().equals(PostEntityTag.GARC.getCode())){
				organization = this.organizationProvider.findOrganizationByCommunityIdAndOrgType(post.getVisibleRegionId(), post.getCreatorTag());break;
			}
			else if(!creatorTag.getCode().equals(PostEntityTag.USER.getCode())){
				organization = this.organizationProvider.findOrganizationById(post.getVisibleRegionId());break;
			}
		case PM:
		case GARC:
			organization = this.organizationProvider.findOrganizationByCommunityIdAndOrgType(post.getVisibleRegionId(), post.getTargetTag());break;
		case GANC:
		case GAPS:
		case GACW:
			organization = this.organizationProvider.findOrganizationById(post.getVisibleRegionId());break;
		default:
			LOGGER.error("creatorTag or targetTag format is wrong.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"creatorTag or targetTag format is wrong.");
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
		String str = null;
		try {
			Long taskId = post.getEmbeddedId();
			if(taskId == null) {
				LOGGER.error("Failed to render the organization task, task id is null, postId=" + post.getId());
				return post.getEmbeddedJson();
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
