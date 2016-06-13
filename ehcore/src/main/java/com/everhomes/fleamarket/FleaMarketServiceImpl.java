// @formatter:off
package com.everhomes.fleamarket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.acl.AclProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.forum.ForumProvider;
import com.everhomes.forum.ForumService;
import com.everhomes.forum.Post;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.fleamarket.FleaMarketItemDTO;
import com.everhomes.rest.fleamarket.FleaMarketPostCommand;
import com.everhomes.rest.fleamarket.FleaMarketUpdateCommand;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;

@Component
public class FleaMarketServiceImpl implements FleaMarketService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FleaMarketServiceImpl.class);
    
    @Autowired
    private AclProvider aclProvider;
    
    @Autowired
    private ForumProvider forumProvider;
    
    @Autowired
    private ForumService forumService;
    
    @Autowired
    private CoordinationProvider coordinationProvider;
    
    @Override
    public Post postItemToForum(FleaMarketPostCommand cmd) {
        this.forumService.checkForumPostPrivilege(cmd.getForumId());
        
        Post post = new Post();
        post.setForumId(cmd.getForumId());
        post.setAppId(AppConstants.APPID_FLEAMARKET);
        post.setSubject(cmd.getSubject());
        post.setContent(cmd.getContent());
        
        FleaMarketItemDTO item = new FleaMarketItemDTO();
        item.setBarterFlag(cmd.getBarterFlag());
        item.setCloseFlag(cmd.getCloseFlag());
        item.setPrice(cmd.getPrice());
        
        post.setEmbeddedAppId(AppConstants.APPID_FLEAMARKET);
        post.setEmbeddedJson(StringHelper.toJsonString(item));
        
        //post.setVisibleFlag(cmd.getVisibleFlag());
        this.forumProvider.createPost(post);
        
        // TODO
        // source info, attachment handling and copying to nearby community handing etc
        
        return post;
    }
    
    public void updateItem(FleaMarketUpdateCommand cmd) {
        this.forumService.checkForumModifyItemPrivilege(cmd.getForumId(), cmd.getTopicId());
        
        Post post = this.forumProvider.findPostById(cmd.getTopicId());
        if(post.getAppId() == null || post.getAppId().longValue() != AppConstants.APPID_FLEAMARKET)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "It is not a flea market item");

        FleaMarketItemDTO fleaItem = (FleaMarketItemDTO)StringHelper.fromJsonString(post.getEmbeddedJson(), 
                FleaMarketItemDTO.class);
        
        if(fleaItem != null) {
            if(cmd.getBarterFlag() != null)
                fleaItem.setBarterFlag(cmd.getBarterFlag());
            
            if(cmd.getCloseFlag() != null)
                fleaItem.setCloseFlag(cmd.getCloseFlag());
            
            if(cmd.getPrice() != null)
                fleaItem.setPrice(cmd.getPrice());
            
            post.setEmbeddedJson(StringHelper.toJsonString(fleaItem));
        }
        
        try {
            this.coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_POST.getCode()).enter(()-> {
                this.forumProvider.updatePost(post);
               return null;
            });
        } catch(Exception e) {
            LOGGER.error("Failed to update the fleamarket item, postId=" + cmd.getTopicId(), e);
        }
    }
}
