package com.everhomes.wifi;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;


import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.order.OrderEmbeddedHandler;
import com.everhomes.order.OrderUtil;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.order.CommonOrderCommand;
import com.everhomes.rest.order.CommonOrderDTO;
import com.everhomes.rest.order.OrderType;
import com.everhomes.rest.order.PayCallbackCommand;
import com.everhomes.rest.pmsy.AddressDTO;
import com.everhomes.rest.pmsy.CreatePmsyBillOrderCommand;
import com.everhomes.rest.pmsy.GetPmsyBills;
import com.everhomes.rest.pmsy.GetPmsyPropertyCommand;
import com.everhomes.rest.pmsy.ListPmsyBillsCommand;
import com.everhomes.rest.pmsy.ListResourceCommand;
import com.everhomes.rest.pmsy.PmBillsOrdersDTO;
import com.everhomes.rest.pmsy.PmsyBillType;
import com.everhomes.rest.pmsy.PmsyBillsDTO;
import com.everhomes.rest.pmsy.PmsyBillItemDTO;
import com.everhomes.rest.pmsy.PmsyCommunityDTO;
import com.everhomes.rest.pmsy.PmsyOrderStatus;
import com.everhomes.rest.pmsy.PmsyPayerDTO;
import com.everhomes.rest.pmsy.PmsyBillsResponse;
import com.everhomes.rest.pmsy.PmsyPayerStatus;
import com.everhomes.rest.pmsy.SearchBillsOrdersResponse;
import com.everhomes.rest.pmsy.SetPmsyPropertyCommand;
import com.everhomes.rest.pmsy.SearchBillsOrdersCommand;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import com.google.gson.Gson;

@Component
@SuppressWarnings({ "rawtypes", "unchecked" })
public class WifiServiceImpl implements WifiService{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(WifiServiceImpl.class);
	
	@Autowired
	private WifiProvider wifiProvider;
	
	/*@Override
	public List<PmsyPayerDTO> listPmPayers(){
		User user = UserContext.current().getUser();
		Integer namespaceId = UserContext.current().getNamespaceId();
		Long userId = user.getId();
		List<PmsyPayer> list = pmsyProvider.listPmPayers(userId, namespaceId);
		
		return list.stream().map(r -> ConvertHelper.convert(r, PmsyPayerDTO.class)).collect(Collectors.toList());
	}*/
	
	
	
	private String TimeToString(Long time){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		return sdf.format(new Date(time));
	}
	
	private Long StringToTime(String s){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		try {
			return sdf.parse(s).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
}
