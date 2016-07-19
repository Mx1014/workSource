// @formatter:off
package com.everhomes.activity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.category.Category;
import com.everhomes.category.CategoryProvider;
import com.everhomes.forum.Forum;
import com.everhomes.forum.ForumEmbeddedHandler;
import com.everhomes.forum.ForumProvider;
import com.everhomes.forum.Post;
import com.everhomes.hotTag.HotTags;
import com.everhomes.namespace.Namespace;
import com.everhomes.rest.activity.ActivityDTO;
import com.everhomes.rest.activity.ActivityListResponse;
import com.everhomes.rest.activity.ActivityPostCommand;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.hotTag.HotTagServiceType;
import com.everhomes.rest.hotTag.HotTagStatus;
import com.everhomes.rest.organization.OfficialFlag;
import com.everhomes.search.HotTagSearcher;
import com.everhomes.server.schema.tables.pojos.EhActivities;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.StringHelper;

@Component(ActivityEmbeddedHandler.FORUM_EMBEDED_OBJ_RESOLVER_PREFIX + AppConstants.APPID_ACTIVITY)
public class ActivityEmbeddedHandler implements ForumEmbeddedHandler {

    private static final Logger LOGGER=LoggerFactory.getLogger(ActivityEmbeddedHandler.class);
    @Autowired
    private ActivityService activityService;
    
    @Autowired
    private ShardingProvider shardingProvider;
    
    @Autowired
    private ForumProvider forumProvider;
    
    @Autowired
	private HotTagSearcher hotTagSearcher;

    @Override
    public String renderEmbeddedObjectSnapshot(Post post) {
        try{
            ActivityDTO result = activityService.findSnapshotByPostId(post.getId());
            if(result!=null) 
                return StringHelper.toJsonString(result);
        }catch(Exception e){
            LOGGER.error("handle snapshot error",e);
        }
        
        return null;
    }

    @Override
    public String renderEmbeddedObjectDetails(Post post) {
        try{
            ActivityListResponse result = activityService.findActivityDetailsByPostId(post);
            if(result!=null)
                return StringHelper.toJsonString(result);
        }catch(Exception e){
            LOGGER.error("handle details error",e);
        }
        
        return null;
    }

    @Override
    public Post preProcessEmbeddedObject(Post post) {
        Long id=shardingProvider.allocShardableContentId(EhActivities.class).second();
        post.setEmbeddedId(id);
        
        ActivityPostCommand cmd = (ActivityPostCommand) StringHelper.fromJsonString(post.getEmbeddedJson(),
                ActivityPostCommand.class);
        
        if(null == cmd.getNamespaceId()){
        	Forum forum = forumProvider.findForumById(post.getForumId());
        	if(null != forum)
        		cmd.setNamespaceId(forum.getNamespaceId());
        	else
				cmd.setNamespaceId(Namespace.DEFAULT_NAMESPACE);
        }
        
        if(cmd.getTag() != null) {
            post.setTag(cmd.getTag());
        }
        
        if (OfficialFlag.fromCode(cmd.getOfficialFlag())!=OfficialFlag.YES) {
			cmd.setOfficialFlag(null);
		}
        
        post.setEmbeddedJson(StringHelper.toJsonString(cmd));
        
        return post;
    }

    @Override
    public Post postProcessEmbeddedObject(Post post) {
        try{
            ActivityPostCommand cmd = (ActivityPostCommand) StringHelper.fromJsonString(post.getEmbeddedJson(),
                    ActivityPostCommand.class);
            cmd.setId(post.getEmbeddedId());
            if(activityService.isPostIdExist(post.getId())){
            	activityService.updatePost(cmd, post.getId());
            }
            else{
            	activityService.createPost(cmd, post.getId()); 
            }
            
            HotTags tag = new HotTags();
            tag.setName(cmd.getTag());
            tag.setHotFlag(HotTagStatus.INACTIVE.getCode());
            tag.setServiceType(HotTagServiceType.ACTIVITY.getCode());
            hotTagSearcher.feedDoc(tag);
            
        }catch(Exception e){
            LOGGER.error("create activity error",e);
        }
  
        return post;
    }

}
