package com.everhomes.fleamarket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.acl.AclProvider;
import com.everhomes.acl.Privilege;
import com.everhomes.acl.ResourceUserRoleResolver;
import com.everhomes.app.AppConstants;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.entity.EntityType;
import com.everhomes.forum.Forum;
import com.everhomes.forum.ForumProvider;
import com.everhomes.forum.Post;
import com.everhomes.forum.PostCustomField;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;

@Component
public class FleaMarketServiceImpl implements FleaMarketService {
    
    @Autowired
    private AclProvider aclProvider;
    
    @Autowired
    private ForumProvider forumProvider;
    
    @Override
    public Post postItemToForum(FleaMarketPostCommand cmd) {
        assert(cmd.getForumId() != null);
        if(cmd.getForumId() == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid forumId parameter");
        
        Forum forum = this.forumProvider.getForumById(cmd.getForumId().longValue());
        if(forum == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Could not find the forum object");

        ResourceUserRoleResolver roleResolve = PlatformContext.getComponent(forum.getOwnerType());
        if(roleResolve == null) {
            assert(false);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, 
                    "Unable to find proper role resolver");
        }
        
        User user = UserContext.current().getUser();
        if(!this.aclProvider.checkAccess(
                EntityType.FORUM.getCode(), forum.getId(), 
                EntityType.USER.getCode(), user.getId(), 
                Privilege.Create, 
                roleResolve.determineRoleInResource(user.getId(), 
                        EntityType.FORUM.getCode(), forum.getId()))) {
            
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED, 
                    "Access denied: insufficient privilege");
        }
        
        Post post = new Post();
        post.setForumId(cmd.getForumId());
        post.setAppId(AppConstants.APPID_FLEAMARKET);
        post.setSubject(cmd.getSubject());
        post.setContent(cmd.getContent());
        
        FleaMarketItemDTO item = new FleaMarketItemDTO();
        item.setBarterFlag(cmd.getBarterFlag());
        item.setCloseFlag(cmd.getCloseFlag());
        item.setPrice(cmd.getPrice());
        
        post.setEmbeddedObjJson(StringHelper.toJsonString(item));
        post.setEmbeddedObjVersion(1);
        
        Long visibleScope = null;
        if(cmd.getVisibleRegionScope() != null)
            visibleScope = (long)cmd.getVisibleRegionScope().byteValue();
        PostCustomField.VISIBLE_SCOPE.setIntegralValue(post, visibleScope);
        this.forumProvider.createPost(post);
        
        // TODO
        // source info, attachment handling and copying to nearby community handing etc
        
        return post;
    }
}
