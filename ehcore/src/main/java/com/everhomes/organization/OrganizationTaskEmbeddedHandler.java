// @formatter:off
package com.everhomes.organization;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.activity.ActivityPostCommand;
import com.everhomes.app.AppConstants;
import com.everhomes.category.CategoryConstants;
import com.everhomes.entity.EntityType;
import com.everhomes.forum.ForumEmbeddedHandler;
import com.everhomes.forum.Post;
import com.everhomes.forum.PostEntityTag;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationTaskStatus;
import com.everhomes.organization.OrganizationTaskType;
import com.everhomes.organization.OrganizationType;
import com.everhomes.util.DateHelper;
import com.everhomes.util.StringHelper;
import com.everhomes.visibility.VisibleRegionType;

@Component(OrganizationTaskEmbeddedHandler.FORUM_EMBEDED_OBJ_RESOLVER_PREFIX + AppConstants.APPID_ORGTASK)
public class OrganizationTaskEmbeddedHandler implements ForumEmbeddedHandler {

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private OrganizationProvider organizationProvider;
    
    @Override
    public String renderEmbeddedObjectSnapshot(Post post) {
//        ActivityDTO result = activityService.findSnapshotByPostId(post.getId());
//        return StringHelper.toJsonString(result);
    	  return null;
    }

    @Override
    public String renderEmbeddedObjectDetails(Post post) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Post preProcessEmbeddedObject(Post post) {
    	//post还没有存数据库，还没有id，先存0，后面再update
		//      ActivityPostCommand cmd = (ActivityPostCommand) StringHelper.fromJsonString(post.getEmbeddedJson(),
		//      ActivityPostCommand.class);
		//帖子post传过来的 appId 都是null。最后默认为2.    	
		//if(post != null  && post.getAppId() == AppConstants.APPID_PM && post.getCategoryId() == CategoryConstants.CATEGORY_ID_PM){
//		if(post != null  && post.getCategoryId() == CategoryConstants.CATEGORY_ID_PM){
//			CommunityPmTasks task = new CommunityPmTasks();
//			Long organizationId = post.getVisibleRegionId();
//			//如果是物业或者业委会发帖 ，帖子可见范围id存的是小区id。组织任务表中应该存组织id。 需要把小区id转成 组织id。
//			if(VisibleRegionType.fromCode(post.getVisibleRegionType()) == VisibleRegionType.COMMUNITY){
//				organizationId = propertyMgrService.findPropertyOrganizationId(post.getVisibleRegionId());
//			}
//			task.setOrganizationId(organizationId);
//			task.setEntityType(EntityType.TOPIC.getCode());
//			task.setEntityId(-1L);
//			task.setTaskStatus(OrganizationTaskStatus.UNPROCESSED.getCode());
//			task.setTaskType(OrganizationTaskType.fromCode(post.getActionCategory()).getCode());
//			task.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
//			propertyMgrProvider.createPmTask(task );
//			post.setEmbeddedId(task.getId());
//		}
//		
//		Organization cmd = (ActivityPostCommand) StringHelper.fromJsonString(post.getEmbeddedJson(),
//            ActivityPostCommand.class);
//		PostEntityTag entityTag = PostEntityTag.fromCode(post.getTargetTag());
		
		return post;	
    }

    @Override
    public Post postProcessEmbeddedObject(Post post) {
//    	if(post != null){
//    		CommunityPmTasks task = propertyMgrProvider.findPmTaskById(post.getEmbeddedId());
//    		if(task != null){
//    			task.setEntityId(post.getId());
//    			propertyMgrProvider.updatePmTask(task);
//    		}
//    	}
    	 return post;
    }

}
