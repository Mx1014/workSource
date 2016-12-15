package com.everhomes.openapi.jindi;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.activity.ActivityProivider;
import com.everhomes.activity.ActivityRoster;
import com.everhomes.rest.openapi.jindi.JindiActionActivitySignupDTO;
import com.everhomes.rest.openapi.jindi.JindiActionType;
import com.everhomes.rest.openapi.jindi.JindiDataType;
import com.everhomes.rest.openapi.jindi.JindiFetchDataCommand;
import com.everhomes.user.User;
import com.everhomes.user.UserProvider;

/**
 * 
 * <ul>
 * 抓取活动报名数据
 * </ul>
 */
@Component(JindiOpenHandler.JINDI_OPEN_HANDLER+JindiDataType.JindiDataTypeCode.ACTION_CODE+JindiActionType.JindiActionTypeCode.ACTION_TYPE_ACTIVITY_SIGNUP_CODE)
public class JindiOpenActionActivitySignupHandler implements JindiOpenHandler {

	@Autowired
	private ActivityProivider activityProivider;
	
	@Autowired
	private UserProvider userProvider;
	
	@Override
	public String fetchData(JindiFetchDataCommand cmd) {
		return superFetchData(cmd, new JindiOpenCallback<ActivityRoster>() {

			@Override
			public List<ActivityRoster> fetchDataByUpdateTimeAndAnchor(Integer namespaceId, Long timestamp,
					Long pageAnchor, int pageSize) {
				return activityProivider.listActivitySignupByUpdateTimeAndAnchor(cmd.getNamespaceId(), cmd.getTimestamp(), cmd.getPageAnchor(), pageSize+1);
			}

			@Override
			public List<ActivityRoster> fetchDataByUpdateTime(Integer namespaceId, Long timestamp,
					int pageSize) {
				return activityProivider.listActivitySignupByUpdateTime(cmd.getNamespaceId(), cmd.getTimestamp(), pageSize+1);
			}

			@Override
			public Object complementInfo(JindiFetchDataCommand cmd, ActivityRoster src) {
				JindiActionActivitySignupDTO data = new JindiActionActivitySignupDTO();
				data.setId(src.getId());
				data.setUserId(src.getUid());
				data.setUserName(getUser(src.getUid()).getNickName());
				data.setActivityId(src.getActivityId());
				data.setCreateTime(src.getCreateTime());
				data.setUpdateTime(src.getCreateTime());
				return data;
			}
			
			private User getUser(Long id) {
				User user = null;
				if (id == null || (user = userProvider.findUserById(id)) == null) {
					user = new User();
				}
				return user;
			}
		});
	}
	
}
