// @formatter:off
package com.everhomes.organization;

import java.sql.Timestamp;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.everhomes.app.AppConstants;
import com.everhomes.category.CategoryConstants;
import com.everhomes.forum.ForumEmbeddedHandler;
import com.everhomes.forum.Post;
import com.everhomes.forum.PostEntityTag;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
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
            CreateOrganizationTaskCommand cmd = (CreateOrganizationTaskCommand) StringHelper.fromJsonString(post.getEmbeddedJson(),
                CreateOrganizationTaskCommand.class);
            Organization organization = getOrganization(post);
            if(organization == null) {
                LOGGER.error("Failed to locate the organization from post, post=" + post);
            } else {
                OrganizationTask task = new OrganizationTask();
                task.setOrganizationId(organization.getId());
                task.setOrganizationType(organization.getOrganizationType());
                task.setApplyEntityType(OrganizationTaskApplyEnityType.TOPIC.getCode());
                task.setApplyEntityId(0L); // 还没有帖子ID
                task.setCreatorUid(post.getCreatorUid());
                task.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                
                OrganizationTaskType taskType = getOrganizationTaskType(post);
                if(taskType != null) {
                    task.setTaskType(taskType.getCode());
                }
                
                if(cmd != null) {
                    String description = cmd.getDescription();
                    if(StringUtils.isEmpty(description)) {
                       description = post.getSubject(); 
                    }
                    task.setDescription(description);
                    
                    OrganizationTaskStatus status = OrganizationTaskStatus.fromCode(cmd.getTaskStatus());
                    if(status == null) {
                        status = OrganizationTaskStatus.UNPROCESSED;
                    }
                    task.setTaskStatus(status.getCode());
                }
                
                this.organizationProvider.createOrganizationTask(task);
                post.setEmbeddedId(task.getId());
                if(LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Create organization task, task=" + task);
                }
            }
        } catch(Exception e) {
            LOGGER.error("Failed to pre-process the organization task, postId=" + post.getId() + ", creatorId=" + post.getCreatorUid() 
                + ", subject=" + post.getSubject(), e);
        }
		
		return post;	
    }

    @Override
    public Post postProcessEmbeddedObject(Post post) {
        try {
            Long taskId = post.getEmbeddedId();
            if(taskId == null) {
                LOGGER.error("Failed to post-process the organization task, task id is null, postId=" + post.getId() 
                    + ", creatorId=" + post.getCreatorUid() + ", subject=" + post.getSubject());
            } else {
                OrganizationTask task = this.organizationProvider.findOrganizationTaskById(taskId);
                if(task != null) {
                    task.setApplyEntityType(OrganizationTaskApplyEnityType.TOPIC.getCode());
                    task.setApplyEntityId(post.getId());
                    
                    this.organizationProvider.updateOrganizationTask(task);
                } else {
                    LOGGER.error("Failed to post-process the organization task, task not found, postId=" + post.getId() 
                        + ", taskId=" + taskId + ", creatorId=" + post.getCreatorUid() + ", subject=" + post.getSubject());
                }
            }
        } catch(Exception e) {
            LOGGER.error("Failed to post-process the organization task, postId=" + post.getId() + ", creatorId=" + post.getCreatorUid() 
                + ", subject=" + post.getSubject(), e);
        }
        
    	return post;
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
    
    private Organization getOrganization(Post post) {
        PostEntityTag creatorTag = PostEntityTag.fromCode(post.getCreatorTag());
        PostEntityTag targetTag = PostEntityTag.fromCode(post.getTargetTag());
        Long regionId = post.getVisibleRegionId();
        
        Organization organization = null;
        if(creatorTag == null) {
            LOGGER.error("Invalid creater tag, postId=" + post.getId() + ", creatorId=" + post.getCreatorUid() 
                + ", subject=" + post.getSubject());
            return organization;
        }
        if(targetTag == null) {
            LOGGER.error("Invalid taget tag, postId=" + post.getId() + ", creatorId=" + post.getCreatorUid() 
                + ", subject=" + post.getSubject());
            return organization;
        }
        
        // 对于发帖人为普通用户，则目标必须为机构（如物业、业委、居委、公安等），如果目标不是机构则不应该到此Handler
        if(creatorTag == PostEntityTag.USER) {
            // 普通用户发的帖，其regionId肯定为小区ID，需要根据小区ID查询机构
            List<Organization> list = this.organizationProvider.findOrganizationByCommunityId(regionId);
            for(Organization temp : list) {
                if(targetTag == PostEntityTag.fromCode(temp.getOrganizationType())) {
                    organization = temp;
                    break;
                }
            }
        } else {
            switch(creatorTag) {
            case PM: // 对于物业、业委发的帖，其regionId为小区ID，需要根据小区ID查询机构
            case GARC:
                List<Organization> list = this.organizationProvider.findOrganizationByCommunityId(regionId);
                if(list != null && list.size() > 0) {
                    organization = list.get(0);
                }
                break;
            case GANC: // 对于居委、公安发的帖，其regionId为机构ID，可直接查机构
            case GAPS:
                organization = this.organizationProvider.findOrganizationById(regionId);
                break;
            default:
                LOGGER.error("Unsupported post entity tag, postId=" + post.getId() + ", creatorId=" + post.getCreatorUid() 
                + ", subject=" + post.getSubject());
            }
        }
        
        return organization;
    }

    private OrganizationTaskType getOrganizationTaskType(Post post) {
        Long contentCategoryId = post.getContentCategory();
        // Long actionCategoryId = post.getActionCategory();
        
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
        
        LOGGER.error("Failed to parse organization task type, postId=" + post.getId() + ", creatorId=" + post.getCreatorUid() 
                + ", contentCategoryId=" + contentCategoryId + ", subject=" + post.getSubject());
        
        return null;
    }
}
