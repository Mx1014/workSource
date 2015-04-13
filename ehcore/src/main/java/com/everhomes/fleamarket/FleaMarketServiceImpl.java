package com.everhomes.fleamarket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.acl.AclProvider;
import com.everhomes.app.AppConstants;
import com.everhomes.forum.ForumProvider;
import com.everhomes.forum.Post;
import com.everhomes.forum.PostCustomField;
import com.everhomes.util.StringHelper;

@Component
public class FleaMarketServiceImpl implements FleaMarketService {
    
    @Autowired
    private AclProvider aclProvider;
    
    @Autowired
    private ForumProvider forumProvider;

    @Override
    public Post postItemToForum(FleaMarketPostCommand cmd) {
        
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
