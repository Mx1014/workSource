package com.everhomes.poll;

import java.util.HashMap;
import java.util.Map;

import com.everhomes.hotTag.HotTags;
import com.everhomes.rest.hotTag.HotFlag;
import com.everhomes.rest.hotTag.HotTagServiceType;
import com.everhomes.search.HotTagSearcher;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.forum.Forum;
import com.everhomes.forum.ForumEmbeddedHandler;
import com.everhomes.forum.ForumProvider;
import com.everhomes.forum.Post;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.poll.PollDTO;
import com.everhomes.rest.poll.PollPostCommand;
import com.everhomes.rest.poll.PollShowResultResponse;
import com.everhomes.server.schema.tables.pojos.EhPolls;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.util.StringHelper;

@Component(ForumEmbeddedHandler.FORUM_EMBEDED_OBJ_RESOLVER_PREFIX + AppConstants.APPID_POLL)
public class PollEmbeddedHandler implements ForumEmbeddedHandler {

    private static final Logger LOGGER= LoggerFactory.getLogger(PollEmbeddedHandler.class);

    @Autowired
    private PollService pollService;
    
    @Autowired
    private ShardingProvider shardingProvider;
    
    @Autowired
    private ForumProvider forumProvider;

    @Autowired
    private HotTagSearcher hotTagSearcher;

    @Override
    public String renderEmbeddedObjectSnapshot(Post post) {
//        PollShowResultResponse result = pollService.showResult(post.getId());
    	PollDTO result = pollService.showResultBrief(post.getId());
        if (result == null) {
            return null;
        }
        Map<String,PollDTO> obj = new HashMap<String, PollDTO>();
        obj.put("poll", result);
        return StringHelper.toJsonString(obj);
    }

    @Override
    public String renderEmbeddedObjectDetails(Post post) {
        PollShowResultResponse poll = pollService.showResult(post.getId());
        if (poll == null) {
            return null;
        }
        return StringHelper.toJsonString(poll);
    }

    @Override
    public Post preProcessEmbeddedObject(Post post) {
        Long id = shardingProvider.allocShardableContentId(EhPolls.class).second();
        post.setEmbeddedId(id);
        PollPostCommand cmd = (PollPostCommand) StringHelper.fromJsonString(post.getEmbeddedJson(),
            PollPostCommand.class);
        
        if(null == cmd.getNamespaceId()){
        	Forum forum = forumProvider.findForumById(post.getForumId());
        	if(null != forum)
        		cmd.setNamespaceId(forum.getNamespaceId());
        	else
				cmd.setNamespaceId(0);
        }

        // 投票添加标签   add by yanjun 20170613
        if(cmd.getTag() != null) {
            post.setTag(cmd.getTag());
        }
        
        post.setEmbeddedJson(StringHelper.toJsonString(cmd));
        return post;
    }

    @Override
    public Post postProcessEmbeddedObject(Post post) {
        PollPostCommand cmd = (PollPostCommand) StringHelper.fromJsonString(post.getEmbeddedJson(),
                PollPostCommand.class);
        cmd.setId(post.getEmbeddedId());
        pollService.createPoll(cmd, post.getId());

        post.setTag(cmd.getTag());

        // 将tag保存到搜索引擎  add by yanjun 20170613
        if(StringUtils.isNotEmpty(cmd.getTag())){
            try{
                HotTags tag = new HotTags();
                tag.setName(cmd.getTag());
                tag.setHotFlag(HotFlag.NORMAL.getCode());
                tag.setServiceType(HotTagServiceType.POLL.getCode());
                hotTagSearcher.feedDoc(tag);
            }catch (Exception e){
                LOGGER.error("feedDoc poll tag error",e);
            }
        }

        return post;
    }

}
