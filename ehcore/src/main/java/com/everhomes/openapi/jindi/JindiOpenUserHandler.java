package com.everhomes.openapi.jindi;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.everhomes.rest.openapi.jindi.JindiDataType;
import com.everhomes.rest.openapi.jindi.JindiFetchDataCommand;
import com.everhomes.rest.openapi.jindi.JindiUserDTO;
import com.everhomes.rest.openapi.jindi.JindiUserResponse;
import com.everhomes.user.User;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;

@Component(JindiOpenHandler.JINDI_OPEN_HANDLER+JindiDataType.JindiDataTypeCode.USER_CODE)
public class JindiOpenUserHandler implements JindiOpenHandler {
	@Autowired
	private UserProvider userProvider;
	
	@Override
	public String fetchData(JindiFetchDataCommand cmd) {
		Integer pageSize = getPageSize(cmd.getPageSize());
		//pageAnchor和nextPageAnchor只有在同一个时间戳有未取完的数据时才有用
		Long nextPageAnchor = null;
		Byte hasMore = null;
		List<User> userList = new ArrayList<>();
		//如果有锚点，则说明上次请求数据未取完，本次继续取当前时间戳的数据
		if (cmd.getPageAnchor() != null) {
			List<User> thisList = userProvider.listUserByUpdateTimeAndAnchor(cmd.getNamespaceId(), cmd.getTimestamp(), cmd.getPageAnchor(), pageSize+1);
			userList.addAll(thisList);
			//如果当前时间戳还未取完，则直接返回并添加下次锚点
			if (userList.size() > pageSize) {
				userList.remove(userList.size()-1);
				nextPageAnchor = userList.get(userList.size()-1).getId();
				hasMore = (byte)1;
			}else {
				//如果正好取完，则没有锚点了
				//如果当前时间戳取完了，还不够，那就需要从当前时间戳之后再取一些补充到pageSize大小
				//此处无代码，方便说明逻辑
			}
		}
		
		//如果没有锚点，或者上述过程未取满或正好取满，则到此处，如果是正好取满，则这里会再取一条，看看有没有多余的数据了
		if (hasMore == null  && userList.size() <= pageSize) {
			pageSize = pageSize - userList.size();
			List<User> thisList = userProvider.listUserByUpdateTime(cmd.getNamespaceId(), cmd.getTimestamp(), pageSize+1);
			if (thisList.size() > pageSize) {
				thisList.remove(thisList.size()-1);
				hasMore = (byte)1;
				//如果此处数据大于pageSize说明，还有数据
				if (thisList.size()>=2 && thisList.get(thisList.size()-1).getUpdateTime().getTime() == thisList.get(thisList.size()-2).getUpdateTime().getTime()) {
					//如果最后两条数据的更新时间一样，说明这个时间戳还没有取完数据，下次还要从这个时间戳开始取，需要设置一下锚点
					nextPageAnchor = thisList.get(thisList.size()-1).getId();
				}
			}else {
				//到这里，说明数据已经取完了
				//此处无代码，方便说明逻辑
			}
			userList.addAll(thisList);
		}
		
		List<JindiUserDTO> jindiUserList = userList.stream().map(u->{
			JindiUserDTO jindiUser = new JindiUserDTO();
			jindiUser.setId(u.getId());
			jindiUser.setNickName(u.getNickName());
			jindiUser.setGender(String.valueOf(u.getGender()));
			jindiUser.setBirthday(u.getBirthday()==null?null:String.valueOf(u.getBirthday().getTime()));
			jindiUser.setCreateTime(u.getCreateTime());
			jindiUser.setUpdateTime(u.getUpdateTime());
			
			List<UserIdentifier> userIdentifierList = userProvider.listUserIdentifiersOfUser(u.getId());
			if (userIdentifierList != null && !userIdentifierList.isEmpty()) {
				jindiUser.setPhone(userIdentifierList.get(0).getIdentifierToken());
			}
			
//			Organization organization = userProvider.findAnyUserRelatedOrganization(u.getId(), cmd.getNamespaceId());
//			if (organization != null) {
//				jindiUser.setOrganizationId(String.valueOf(organization.getId()));
//				jindiUser.setOrganizationName(organization.getName());
//			}
			
			return jindiUser;
		}).collect(Collectors.toList());

		JindiUserResponse response = new JindiUserResponse();
		response.setHasMore(hasMore);
		response.setNextPageAnchor(nextPageAnchor);
		response.setUserList(jindiUserList);
		
		return JSON.toJSONString(response);
	}
	
}
