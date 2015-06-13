// @formatter:off
package com.everhomes.pm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.app.AppConstants;
import com.everhomes.category.CategoryConstants;
import com.everhomes.entity.EntityType;
import com.everhomes.forum.ForumEmbeddedHandler;
import com.everhomes.forum.Post;

@Component(PropertyEmbeddedHandler.FORUM_EMBEDED_OBJ_RESOLVER_PREFIX + AppConstants.APPID_PM)
public class PropertyEmbeddedHandler implements ForumEmbeddedHandler {

    @Autowired
    private PropertyMgrService propertyMgrService;

    @Autowired
    private PropertyMgrProvider propertyMgrProvider;
    
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
        return null;
    }

    @Override
    public Post postProcessEmbeddedObject(Post post) {
//        ActivityPostCommand cmd = (ActivityPostCommand) StringHelper.fromJsonString(post.getEmbeddedJson(),
//                ActivityPostCommand.class);
    	
    	if(post != null  && post.getAppId() == AppConstants.APPID_PM && post.getCategoryId() == CategoryConstants.CATEGORY_ID_PM){
    		CommunityPmTasks task = new CommunityPmTasks();
    		task.setCommunityId(post.getVisibleRegionId());
    		task.setEntityType(EntityType.TOPIC.getCode());
    		task.setEntityId(post.getId());
    		task.setTargetType(EntityType.USER.getCode());
    		task.setTargetId(0l);
    		task.setTaskStatus(PmTaskStatus.UNTREATED.getCode());
    		task.setTaskType(PmTaskType.fromCode(post.getActionCategoryId()).getCode());
    		propertyMgrProvider.createPmTask(task );
    	}
		return post;
    }

}
