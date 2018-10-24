package com.everhomes.general.order;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.general.order.GeneralOrderBizService;
import com.everhomes.paySDK.PaySettings;
import com.everhomes.paySDK.PayUtil;
import com.everhomes.portal.PlatformContextNoWarnning;
import com.everhomes.print.SiyinPrintServiceImpl;
import com.everhomes.rest.general.order.OrderCallBackCommand;
import com.everhomes.rest.order.OrderType;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.SignatureHelper;
import com.everhomes.util.StringHelper;

@Component
public class GeneralOrderBizServiceImpl implements GeneralOrderBizService{
	private static final Logger LOGGER = LoggerFactory.getLogger(GeneralOrderBizServiceImpl.class);
	@Override
	public void orderCallBack(OrderCallBackCommand cmd) {
		
		//检查签名
		if(!verifyCallbackSignature(cmd)){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"sign verify faild");
		}
		
		LOGGER.info("cmd:"+cmd.toString());
		GeneralOrderBizHandler bizHandler = getGeneralOrderBizHandler(cmd.getBusinessType());
		if (null == bizHandler) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"no business method to handle this callback");
		}
		
		bizHandler.dealCallBack(cmd);
		
	}
	private boolean verifyCallbackSignature(OrderCallBackCommand cmd) {
		Map<String, String> params = new HashMap<>();
        StringHelper.toStringMap((String)null, cmd, params);
        params.remove("signature");
        return SignatureHelper.verifySignature(params, PaySettings.getSecretKey(), cmd.getSignature());
	}
	
	private GeneralOrderBizHandler getGeneralOrderBizHandler(String orderType) {
		return PlatformContextNoWarnning.getComponent(GeneralOrderBizHandler.GENERAL_ORDER_HANDLER + orderType);
	}

	

}
