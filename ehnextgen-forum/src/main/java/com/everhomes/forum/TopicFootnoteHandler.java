package com.everhomes.forum;

import org.springframework.stereotype.Component;

import com.everhomes.rest.forum.PostDTO;
import com.everhomes.rest.search.SearchContentConstants;
import com.everhomes.rest.ui.user.ContentBriefDTO;
import com.everhomes.rest.ui.user.TopicFootnote;
import com.everhomes.util.StringHelper;

@Component(ForumFootnoteHandler.FORUM_FOOTNOTE_RESOLVER_PREFIX + SearchContentConstants.TOPIC)
public class TopicFootnoteHandler implements ForumFootnoteHandler {

	@Override
	public String renderContentFootnote(ContentBriefDTO dto, PostDTO postDto) {

		TopicFootnote fn = new TopicFootnote();
		fn.setCreateTime(postDto.getCreateTime());
		fn.setCreatorNickName(postDto.getCreatorNickName());
		
		dto.setFootnoteJson(StringHelper.toJsonString(fn));
		return null;
	}

}
