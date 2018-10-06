package com.everhomes.general.order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.everhomes.general.order.GeneralOrderBizService;
import com.everhomes.print.SiyinPrintServiceImpl;
import com.everhomes.rest.general.order.OrderCallBackCommand;

@Component
public class GeneralOrderBizServiceImpl implements GeneralOrderBizService{
	private static final Logger LOGGER = LoggerFactory.getLogger(GeneralOrderBizServiceImpl.class);
	@Override
	public void orderCallBack(OrderCallBackCommand cmd) {
		
		LOGGER.info("cmd:"+cmd.toString());
		
		switch (cmd.getCallBackType()) {
		case 0:
			
			break;
		case 1:
			
			break;

		default:
			break;
		}
	}
	
	
	
	
	

}
