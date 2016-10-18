package com.everhomes.activity;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.configuration.ConfigConstants;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.entity.EntityType;
import com.everhomes.forum.ForumFootnoteHandler;
import com.everhomes.rest.forum.PostDTO;
import com.everhomes.rest.search.SearchContentConstants;
import com.everhomes.rest.ui.user.ActivityFootnote;
import com.everhomes.rest.ui.user.ContentBriefDTO;
import com.everhomes.util.StringHelper;

@Component(ForumFootnoteHandler.FORUM_FOOTNOTE_RESOLVER_PREFIX + SearchContentConstants.ACITVITY)
public class ActivityFootnoteHandler implements ForumFootnoteHandler {

    @Autowired
    private ActivityProivider activityProvider;

    @Autowired
    private ConfigurationProvider configurationProvider;
    
    @Autowired
    private ContentServerService contentServerService;
	
	@Override
	public String renderContentFootnote(ContentBriefDTO dto, PostDTO postDto) {
		if(postDto != null) {
			Activity activity = activityProvider.findSnapshotByPostId(postDto.getId());
			ActivityFootnote fn = new ActivityFootnote();
			
			
			if(activity != null && activity.getCreateTime() != null) {
				fn.setLocation(activity.getLocation());
				fn.setStartTime(timeToStr(activity.getStartTime()));
				dto.setPostUrl(getActivityPosterUrl(activity));
			}
			dto.setFootnoteJson(StringHelper.toJsonString(fn));
		}
		return null;
	}

	private String timeToStr(Timestamp time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return sdf.format(time);
	}
	
	private String getActivityPosterUrl(Activity activity) {
		
		if(activity.getPosterUri() == null) {
			String posterUrl = contentServerService.parserUri(configurationProvider.getValue(ConfigConstants.ACTIVITY_POSTER_DEFAULT_URL, ""), EntityType.ACTIVITY.getCode(), activity.getId());
			return posterUrl;
		} else {
			String posterUrl = contentServerService.parserUri(activity.getPosterUri(), EntityType.ACTIVITY.getCode(), activity.getId());
			if(posterUrl.equals(activity.getPosterUri())) {
				posterUrl = contentServerService.parserUri(configurationProvider.getValue(ConfigConstants.ACTIVITY_POSTER_DEFAULT_URL, ""), EntityType.ACTIVITY.getCode(), activity.getId());
			}
			return posterUrl;
		}
		
	}
		
}
