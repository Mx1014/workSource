package com.everhomes.openapi.jindi;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.activity.Activity;
import com.everhomes.activity.ActivityProivider;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.forum.ForumProvider;
import com.everhomes.forum.Post;
import com.everhomes.rest.openapi.jindi.JindiActionActivityDTO;
import com.everhomes.rest.openapi.jindi.JindiActionForumCommentDTO;
import com.everhomes.rest.openapi.jindi.JindiActionType;
import com.everhomes.rest.openapi.jindi.JindiDataType;
import com.everhomes.rest.openapi.jindi.JindiFetchDataCommand;

/**
 * 
 * <ul>
 * 抓取活动数据
 * </ul>
 */
@Component(JindiOpenHandler.JINDI_OPEN_HANDLER+JindiDataType.JindiDataTypeCode.ACTION_CODE+JindiActionType.JindiActionTypeCode.ACTION_TYPE_ACTIVITY_CODE)
public class JindiOpenActionActivityHandler implements JindiOpenHandler {

	@Autowired
	private ActivityProivider activityProivider;
	
	@Autowired
	private CommunityProvider communityProvider;
	
	@Override
	public String fetchData(JindiFetchDataCommand cmd) {
		return superFetchData(cmd, new JindiOpenCallback<Activity>() {

			@Override
			public List<Activity> fetchDataByUpdateTimeAndAnchor(Integer namespaceId, Long timestamp,
					Long pageAnchor, int pageSize) {
				return activityProivider.listActivityByUpdateTimeAndAnchor(cmd.getNamespaceId(), cmd.getTimestamp(), cmd.getPageAnchor(), pageSize+1);
			}

			@Override
			public List<Activity> fetchDataByUpdateTime(Integer namespaceId, Long timestamp,
					int pageSize) {
				return activityProivider.listActivityByUpdateTime(cmd.getNamespaceId(), cmd.getTimestamp(), pageSize+1);
			}

			@Override
			public Object complementInfo(JindiFetchDataCommand cmd, Activity src) {
				JindiActionActivityDTO data = new JindiActionActivityDTO();
				data.setId(src.getId());
				data.setUserId(src.getCreatorUid());
				data.setCreateTime(src.getCreateTime());
				
				if (src.getVisibleRegionId() != null) {
					Community community = communityProvider.findCommunityById(src.getVisibleRegionId());
					if (community != null) {
						data.setCommunityId(community.getId());
						data.setCommunityName(community.getName());
					}
				}
				
				return data;
			}
		});
	}
	
}
