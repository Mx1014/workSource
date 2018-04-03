package com.everhomes.openapi.jindi;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.forum.ForumProvider;
import com.everhomes.forum.Post;
import com.everhomes.rest.openapi.jindi.JindiActionForumPostDTO;
import com.everhomes.rest.openapi.jindi.JindiActionType;
import com.everhomes.rest.openapi.jindi.JindiDataType;
import com.everhomes.rest.openapi.jindi.JindiFetchDataCommand;
import com.everhomes.user.User;

/**
 * 
 * <ul>
 * 抓取论坛发帖数据
 * </ul>
 */
@Component(JindiOpenHandler.JINDI_OPEN_HANDLER+JindiDataType.JindiDataTypeCode.ACTION_CODE+JindiActionType.JindiActionTypeCode.ACTION_TYPE_FORUM_POST_CODE)
public class JindiOpenActionForumPostHandler implements JindiOpenHandler {

	@Autowired
	private ForumProvider forumProvider;
	
	@Autowired
	private CommunityProvider communityProvider;
	
	@Override
	public String fetchData(JindiFetchDataCommand cmd) {
		return superFetchData(cmd, new JindiOpenCallback<Post>() {

			@Override
			public List<Post> fetchDataByUpdateTimeAndAnchor(Integer namespaceId, Long timestamp,
					Long pageAnchor, int pageSize) {
				return forumProvider.listForumPostByUpdateTimeAndAnchor(cmd.getNamespaceId(), cmd.getBeginTime(), cmd.getPageAnchor(), pageSize+1);
			}

			@Override
			public List<Post> fetchDataByUpdateTime(Integer namespaceId, Long timestamp,
					int pageSize) {
				return forumProvider.listForumPostByUpdateTime(cmd.getNamespaceId(), cmd.getBeginTime(), pageSize+1);
			}

			@Override
			public Object complementInfo(JindiFetchDataCommand cmd, Post src) {
				User user = getUser(src.getCreatorUid());
				JindiActionForumPostDTO data = new JindiActionForumPostDTO();
				data.setId(src.getId());
				data.setUserId(src.getCreatorUid());
				data.setUserName(user.getNickName());
				data.setPhone(user.getIdentifierToken());
				data.setCreateTime(src.getCreateTime());
				data.setUpdateTime(src.getUpdateTime());
				data.setSubject(src.getSubject());
				data.setStatus(src.getStatus());
				
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
