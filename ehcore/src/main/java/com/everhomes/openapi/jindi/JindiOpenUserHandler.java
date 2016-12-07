package com.everhomes.openapi.jindi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.rest.openapi.jindi.JindiDataType;
import com.everhomes.rest.openapi.jindi.JindiFetchDataCommand;
import com.everhomes.user.UserProvider;

@Component(JindiOpenHandler.JINDI_OPEN_HANDLER+JindiDataType.JindiDataTypeCode.USER_CODE)
public class JindiOpenUserHandler implements JindiOpenHandler {
	@Autowired
	private UserProvider userProvider;
	
	@Override
	public String fetchData(JindiFetchDataCommand cmd) {
		Integer pageSize = getPageSize(cmd.getPageSize());
		
		//如果有锚点，则说明上次请求数据未取完，本次继续取当前时间戳的数据
		if (cmd.getPageAnchor() != null) {
			userProvider.listUserByUpdateTime(cmd.getNamespaceId(), cmd.getTimestamp(), cmd.getPageAnchor(), pageSize+1);
		}
		
		
		//如果当前时间戳还未取完，则直接返回并添加下次锚点
		
		//如果当前时间戳取完了，且不够，则从大于当前时间戳的时间点继续取剩余的条数
		
		return null;
	}
	
}
