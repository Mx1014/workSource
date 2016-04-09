package com.everhomes.order;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.app.App;
import com.everhomes.app.AppProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.rest.order.CommonOrderCommand;
import com.everhomes.rest.order.CommonOrderDTO;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.SignatureHelper;

@Component
public class OrderUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(OrderUtil.class);
	
	@Autowired
	private ConfigurationProvider configurationProvider;
	@Autowired
	private AppProvider appProvider;
	
	public CommonOrderDTO convertToCommonOrderTemplate(CommonOrderCommand cmd) throws Exception{
		if(StringUtils.isEmpty(cmd.getOrderNo())||StringUtils.isEmpty(cmd.getOrderType())){
			LOGGER.error("parameter orderNo or orderType is null");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"parameter orderNo or orderType is null");
		}
		if(StringUtils.isEmpty(cmd.getSubject())||StringUtils.isEmpty(cmd.getBody())){
			LOGGER.error("parameter subject or body is null");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"parameter subject or body is null");
		}
		if(cmd.getTotalFee()==null){
			LOGGER.error("parameter totalFee is null");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"parameter totalFee is null");
		}
		CommonOrderDTO dto = new CommonOrderDTO();
		String appKey = configurationProvider.getValue("pay.appKey", "7bbb5727-9d37-443a-a080-55bbf37dc8e1");
		Long timestamp = System.currentTimeMillis();
		Integer randomNum = (int) (Math.random()*1000);
		App app = appProvider.findAppByKey(appKey);

		Map<String,String> map = new HashMap<String, String>();
		map.put("appKey",appKey);
		map.put("timestamp",timestamp+"");
		map.put("randomNum",randomNum+"");
		map.put("amount",cmd.getTotalFee().doubleValue()+"");
		String signature = SignatureHelper.computeSignature(map, app.getSecretKey());
		dto.setOrderNo(cmd.getOrderNo());
		dto.setOrderType(cmd.getOrderType());
		dto.setTotalFee(cmd.getTotalFee());
		dto.setSubject(cmd.getSubject());
		dto.setBody(cmd.getBody());
		dto.setAppKey(appKey);
		dto.setSignature(URLEncoder.encode(signature,"UTF-8"));
		dto.setTimestamp(timestamp);
		dto.setRandomNum(randomNum);
		return dto;
	}

}
