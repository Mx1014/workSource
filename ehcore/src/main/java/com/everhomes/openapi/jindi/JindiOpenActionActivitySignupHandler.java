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
	
	@Override
	public String fetchData(JindiFetchDataCommand cmd) {
		return superFetchData(cmd, new JindiOpenCallback<ActivityRoster>() {

			@Override
			public List<ActivityRoster> fetchDataByUpdateTimeAndAnchor(Integer namespaceId, Long timestamp,
					Long pageAnchor, int pageSize) {
				return activityProivider.listActivitySignupByUpdateTimeAndAnchor(cmd.getNamespaceId(), cmd.getBeginTime(), cmd.getPageAnchor(), pageSize+1);
			}

			@Override
			public List<ActivityRoster> fetchDataByUpdateTime(Integer namespaceId, Long timestamp,
					int pageSize) {
				return activityProivider.listActivitySignupByUpdateTime(cmd.getNamespaceId(), cmd.getBeginTime(), pageSize+1);
			}

			@Override
			public Object complementInfo(JindiFetchDataCommand cmd, ActivityRoster src) {
				User user = getUser(src.getUid());
				JindiActionActivitySignupDTO data = new JindiActionActivitySignupDTO();
				data.setId(src.getId());
				data.setUserId(src.getUid());
				data.setUserName(user.getNickName());
				data.setPhone(user.getIdentifierToken());
				data.setActivityId(src.getActivityId());
				data.setCreateTime(src.getCreateTime());
				data.setUpdateTime(src.getCreateTime());
				return data;
			}
		});
	}
	
}
