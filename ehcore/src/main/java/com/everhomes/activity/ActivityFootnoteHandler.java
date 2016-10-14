package com.everhomes.activity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.forum.ForumFootnoteHandler;
import com.everhomes.rest.activity.ActivityDTO;
import com.everhomes.rest.forum.PostDTO;
import com.everhomes.rest.search.SearchContentConstants;
import com.everhomes.rest.ui.user.ActivityFootnote;
import com.everhomes.rest.ui.user.ContentBriefDTO;
import com.everhomes.util.StringHelper;

@Component(ForumFootnoteHandler.FORUM_FOOTNOTE_RESOLVER_PREFIX + SearchContentConstants.ACITVITY)
public class ActivityFootnoteHandler implements ForumFootnoteHandler {

	@Autowired
    private ActivityService activityService;
	
	@Override
	public String renderContentFootnote(ContentBriefDTO dto, PostDTO postDto) {
		ActivityDTO result = activityService.findSnapshotByPostId(postDto.getId());
		ActivityFootnote fn = new ActivityFootnote();
		fn.setLocation(result.getLocation());
		fn.setStartTime(result.getStartTime().substring(0, result.getStartTime().lastIndexOf(":")));
		
		dto.setPostUrl(result.getPosterUrl());
		dto.setFootnoteJson(StringHelper.toJsonString(fn));
		return null;
	}

}
