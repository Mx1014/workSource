// @formatter:off
package com.everhomes.link;

import java.sql.Timestamp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.configuration.ConfigConstants;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.forum.ForumEmbeddedHandler;
import com.everhomes.forum.Post;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.link.CreateLinkCommand;
import com.everhomes.rest.link.LinkContentType;
import com.everhomes.rest.link.LinkSourceType;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.StringHelper;

@Component(LinkEmbeddedHandler.FORUM_EMBEDED_OBJ_RESOLVER_PREFIX + AppConstants.APPID_LINK)
public class LinkEmbeddedHandler implements ForumEmbeddedHandler {

    private static final Logger LOGGER=LoggerFactory.getLogger(LinkEmbeddedHandler.class);
    
    @Autowired
    private LinkProvider linkProvider;
    @Autowired
    private ConfigurationProvider configurationProvider;

    @Override
    public String renderEmbeddedObjectSnapshot(Post post) {
        try{
            Link link = linkProvider.findLinkByPostId(post.getId());
            if(link != null){
            	if(link.getContentType().equals(LinkContentType.CREATE.getCode())){
            		link.setRichContent(link.getContent());
            		String homeUrl = configurationProvider.getValue(ConfigConstants.HOME_URL, "");
            		link.setContent(homeUrl + "/web/lib/html/rich_text_review.html?id=" + link.getId());
            	}
            	return StringHelper.toJsonString(link);
	        }
            else {
            	LOGGER.error("Unable to find the post link.postId=" + post.getId());
            	
            }
        }catch(Exception e){
            LOGGER.error("handle link post error.postId=" + post.getId(),e);
        }
        
        return null;
    }

    @Override
    public String renderEmbeddedObjectDetails(Post post) {
    	try{
            Link link = linkProvider.findLinkByPostId(post.getId());
            if(link != null){
            	if(link.getContentType().equals(LinkContentType.CREATE.getCode())){
            		link.setRichContent(link.getContent());
            		String homeUrl = configurationProvider.getValue(ConfigConstants.HOME_URL, "");
            		link.setContent(homeUrl + "/web/lib/html/rich_text_review.html?id=" + link.getId());
            	}
            	return StringHelper.toJsonString(link);
	        }
            else {
            	LOGGER.error("Unable to find the post link.postId=" + post.getId());
            	
            }
        }catch(Exception e){
            LOGGER.error("handle link post error.postId=" + post.getId(),e);
        }
        
        return null;
    }

    @Override
    public Post preProcessEmbeddedObject(Post post) {
    	//post还没有存数据库，还没有id，先存-1，后面再update
    	User user = UserContext.current().getUser();
    	if(post != null ){
    		CreateLinkCommand command = (CreateLinkCommand)  StringHelper.fromJsonString(post.getEmbeddedJson(),CreateLinkCommand.class);
    		Link link =  ConvertHelper.convert(command, Link.class);
	    	link.setOwnerUid(user.getId());
	    	link.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
	    	link.setSourceId(-1L);
	    	link.setSourceType(LinkSourceType.POST.getCode());
	    	link.setStatus((byte)2);
	    	link.setDeleterUid(0L);
	    	linkProvider.createLink(link);
	    	post.setEmbeddedId(link.getId());
    	}
        return post;
    }

    @Override
    public Post postProcessEmbeddedObject(Post post) {
    	if(post != null){
    		Link link = linkProvider.findLinkById(post.getEmbeddedId());
    		if(link != null){
    			link.setSourceId(post.getId());
    			linkProvider.updateLink(link);
    		}
    	}
        return post;
    }

}
