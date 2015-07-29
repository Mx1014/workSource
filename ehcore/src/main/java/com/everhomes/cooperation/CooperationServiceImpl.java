package com.everhomes.cooperation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.util.ConvertHelper;
 

@Component
public class CooperationServiceImpl implements CooperationService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(CooperationServiceImpl.class);
	@Autowired
	CooperationProvider cooperationProvider;

	@Override
	public void newCooperation(NewCooperationCommand cmd) {
		// TODO Auto-generated method stub
		// 类型强制为电话
		cmd.setContactType((byte) 0);
		LOGGER.debug("new Cooperation begin insert , "+cmd.toString()+"");
		CooperationRequests cooperationRequests = ConvertHelper.convert(cmd,CooperationRequests.class);
		cooperationProvider.newCooperation(cooperationRequests);
	}

}
