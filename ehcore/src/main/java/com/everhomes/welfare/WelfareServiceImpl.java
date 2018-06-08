// @formatter:off
package com.everhomes.welfare;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.rest.welfare.DraftWelfareCommand;
import com.everhomes.rest.welfare.GetUserWelfareCommand;
import com.everhomes.rest.welfare.GetUserWelfareResponse;
import com.everhomes.rest.welfare.ListWelfaresCommand;
import com.everhomes.rest.welfare.ListWelfaresResponse;
import com.everhomes.rest.welfare.SendWelfareCommand;
import com.everhomes.techpark.punch.PunchServiceImpl;
import com.google.zxing.Result;

@Component
public class WelfareServiceImpl implements WelfareService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WelfareServiceImpl.class);
	@Autowired
	private WelfareProvider welfareProvider;
	@Autowired
	private WelfareItemProvider welfareItemProvider;
	@Autowired
	private WelfareReceiverProvider welfareReceiverProvider;
	
	@Override
	public ListWelfaresResponse listWelfares(ListWelfaresCommand cmd) {
		List<Welfare> results = welfareProvider.listWelfare();
		return new ListWelfaresResponse();
	}

	@Override
	public void draftWelfare(DraftWelfareCommand cmd) {
	

	}

	@Override
	public void sendWelfare(SendWelfareCommand cmd) {
	

	}

	@Override
	public GetUserWelfareResponse getUserWelfare(GetUserWelfareCommand cmd) {
	
		return new GetUserWelfareResponse();
	}

}