package com.everhomes.poll;

import org.springframework.stereotype.Component;

import com.everhomes.forum.ForumFootnoteHandler;
import com.everhomes.rest.forum.PostDTO;
import com.everhomes.rest.search.SearchContentConstants;
import com.everhomes.rest.ui.user.ContentBriefDTO;
import com.everhomes.rest.ui.user.PollFootnote;
import com.everhomes.util.StringHelper;

@Component(ForumFootnoteHandler.FORUM_FOOTNOTE_RESOLVER_PREFIX + SearchContentConstants.POLL)
public class PollFootnoteHandler implements ForumFootnoteHandler {

	@Override
	public String renderContentFootnote(ContentBriefDTO dto, PostDTO postDto) {
        
        PollFootnote fn = new PollFootnote();
        fn.setCreateTime(postDto.getCreateTime());
        fn.setCreatorNickName(postDto.getCreatorNickName());
        
        dto.setFootnoteJson(StringHelper.toJsonString(fn));
		
        return null;
	}

}
