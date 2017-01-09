package com.everhomes.openapi.jindi;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.rest.openapi.jindi.JindiDataType;
import com.everhomes.rest.openapi.jindi.JindiFetchDataCommand;
import com.everhomes.rest.openapi.jindi.JindiUserDTO;
import com.everhomes.user.User;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;

@Component(JindiOpenHandler.JINDI_OPEN_HANDLER+JindiDataType.JindiDataTypeCode.USER_CODE)
public class JindiOpenUserHandler implements JindiOpenHandler {
	@Autowired
	private UserProvider userProvider;
	
	@Override
	public String fetchData(JindiFetchDataCommand cmd) {
		return superFetchData(cmd, new JindiOpenCallback<User>() {

			@Override
			public List<User> fetchDataByUpdateTimeAndAnchor(Integer namespaceId, Long timestamp, Long pageAnchor,
					int pageSize) {
				return userProvider.listUserByUpdateTimeAndAnchor(cmd.getNamespaceId(), cmd.getBeginTime(), cmd.getPageAnchor(), pageSize+1);
			}

			@Override
			public List<User> fetchDataByUpdateTime(Integer namespaceId, Long timestamp, int pageSize) {
				return userProvider.listUserByUpdateTime(cmd.getNamespaceId(), cmd.getBeginTime(), pageSize+1);
			}

			@Override
			public Object complementInfo(JindiFetchDataCommand cmd, User user) {
				JindiUserDTO jindiUser = new JindiUserDTO();
				jindiUser.setId(user.getId());
				jindiUser.setNickName(user.getNickName());
				jindiUser.setGender(String.valueOf(user.getGender()));
				jindiUser.setBirthday(user.getBirthday()==null?null:String.valueOf(user.getBirthday().getTime()));
				jindiUser.setCreateTime(user.getCreateTime());
				jindiUser.setUpdateTime(user.getUpdateTime());
				
				List<UserIdentifier> userIdentifierList = userProvider.listUserIdentifiersOfUser(user.getId());
				if (userIdentifierList != null && !userIdentifierList.isEmpty()) {
					jindiUser.setPhone(userIdentifierList.get(0).getIdentifierToken());
				}
				
				return jindiUser;
			}

		});
	}
	
}
