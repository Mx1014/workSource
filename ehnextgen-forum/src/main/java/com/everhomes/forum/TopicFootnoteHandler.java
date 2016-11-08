package com.everhomes.forum;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

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
		if(postDto != null && postDto.getCreateTime() != null) {
			fn.setCreateTime(timeToStr(postDto.getCreateTime()));
			fn.setCreatorNickName(postDto.getCreatorNickName());
		}
		
		dto.setFootnoteJson(StringHelper.toJsonString(fn));
		return null;
	}
	
	private String timeToStr(Timestamp time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return sdf.format(time);
	}

}
