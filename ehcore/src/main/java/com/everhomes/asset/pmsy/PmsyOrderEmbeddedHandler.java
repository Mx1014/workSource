package com.everhomes.asset.pmsy;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.order.OrderEmbeddedHandler;
import com.everhomes.organization.pmsy.PmsyOrder;
import com.everhomes.organization.pmsy.PmsyOrderItem;
import com.everhomes.organization.pmsy.PmsyPayer;
import com.everhomes.organization.pmsy.PmsyProvider;
import com.everhomes.rest.order.OrderType;
import com.everhomes.rest.order.PayCallbackCommand;
import com.everhomes.rest.pmsy.PmsyBillType;
import com.everhomes.rest.pmsy.PmsyOrderStatus;
import com.everhomes.rest.pmsy.PmsyPayerStatus;
import com.everhomes.util.RuntimeErrorException;
import com.google.gson.Gson;

@Component(OrderEmbeddedHandler.ORDER_EMBEDED_OBJ_RESOLVER_PREFIX + OrderType.PM_SIYUAN_CODE )
public class PmsyOrderEmbeddedHandler implements OrderEmbeddedHandler{

	private static final Logger LOGGER = LoggerFactory.getLogger(PmsyOrderEmbeddedHandler.class);
	
	@Autowired
	private PmsyProvider pmsyProvider;
	@Autowired
    private ConfigurationProvider configProvider;
	
	@Override
	public void paySuccess(PayCallbackCommand cmd) {
		
		PmsyOrder order = checkOrder(Long.parseLong(cmd.getOrderNo()));
		
		Timestamp payTimeStamp = new Timestamp(System.currentTimeMillis());
		order.setStatus(PmsyOrderStatus.PAID.getCode());
		order.setPaidTime(payTimeStamp);
		order.setPaidType(cmd.getVendorType());
		//order.setPaidTime(cmd.getPayTime());
		pmsyProvider.updatePmsyOrder(order);
		
		List<PmsyOrderItem> orderItems = pmsyProvider.ListPmsyOrderItem(order.getId());
		if(orderItems.isEmpty()){
			LOGGER.error("bill list is empty.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"bill list is empty.");
		}
		
		String feeJson = PmsyHttpUtil.post(configProvider.getValue("haian.siyuan", ""),"UserRev_GetFeeList", order.getCustomerId(), "",
				"", order.getProjectId(), PmsyBillType.UNPAID.getCode(), "", "");
		Gson gson = new Gson();
		Map map = gson.fromJson(feeJson, Map.class);
		List feeList = (List) map.get("UserRev_GetFeeList");
		Map map2 = (Map) feeList.get(0);
		List feeList2 = (List) map2.get("Syswin");
		
		List<Map<String,String>> billList = new ArrayList<Map<String,String>>();
		feeListOuter:
			for(Object o:feeList2){
				Map map3 = (Map) o;
				for(PmsyOrderItem orderItem:orderItems){
					if(!orderItem.getBillId().equals((String)map3.get("ID")))
						continue;
					Map<String,String> tempMap = new HashMap<String,String>();
					tempMap.put("RevID", orderItem.getBillId());
					BigDecimal priFailures = new BigDecimal((String)map3.get("PriFailures"));
					BigDecimal lFFailures = new BigDecimal((String)map3.get("LFFailures"));
					BigDecimal debtAmount = priFailures.add(lFFailures);
					tempMap.put("RevMoney", debtAmount.toString());
					tempMap.put("billNo", orderItem.getId().toString());
					billList.add(tempMap);
					continue feeListOuter;
				}
				
			}
		Map<String,Object> jsonMap = new HashMap<String,Object>();
		jsonMap.put("Syswin", billList);
		String billListJson = gson.toJson(jsonMap, Map.class);
		String json = PmsyHttpUtil.post(configProvider.getValue("haian.siyuan", ""),"UserRev_PayFee", order.getCustomerId(), order.getProjectId(),
				"", "siyuan", "支付宝支付", "", billListJson);
		Map payFeeMap = gson.fromJson(json, Map.class);
		List payFeeList = (List) payFeeMap.get("UserRev_PayFee");
		if(payFeeList == null){
			order.setStatus(PmsyOrderStatus.FAIL.getCode());
			pmsyProvider.updatePmsyOrder(order);
			LOGGER.error("the pay of fee is fail.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, 
					"the pay of fee is fail.");
		}
		Map payFeeMap2 = (Map) payFeeList.get(0);
		List payFeeList2 = (List) payFeeMap2.get("Syswin");
		
		payFeeListOuter:
		for(Object o:payFeeList2){
			Map map3 = (Map) o;
			for(PmsyOrderItem orderItem:orderItems){
				if(!orderItem.getBillId().equals((String)map3.get("ID")))
					continue;
				orderItem.setStatus((byte)map3.get("State"));
				pmsyProvider.updatePmsyOrderItem(orderItem);
				continue payFeeListOuter;
			}
			
		}
		order.setStatus(PmsyOrderStatus.SUCCESS.getCode());
		pmsyProvider.updatePmsyOrder(order);
		
		PmsyPayer pmsyPayer = pmsyProvider.findPmPayersByNameAndContact(order.getUserName(), order.getUserContact());
		if(pmsyPayer != null){
			pmsyPayer.setStatus(PmsyPayerStatus.ACTIVE.getCode());
			pmsyPayer.setCreateTime(new Timestamp(System.currentTimeMillis()));
			pmsyProvider.updatePmPayer(pmsyPayer);
		}
		
	}

	@Override
	public void payFail(PayCallbackCommand cmd) {
		if(LOGGER.isDebugEnabled())
			LOGGER.error("onlinePayBillFail");
		this.checkOrderNoIsNull(cmd.getOrderNo());
		Long orderId = Long.parseLong(cmd.getOrderNo());
		PmsyOrder order = checkOrder(orderId);
				
		Timestamp payTimeStamp = new Timestamp(System.currentTimeMillis());
		order.setStatus(PmsyOrderStatus.INACTIVE.getCode());
		order.setPaidTime(payTimeStamp);
		order.setPaidType(cmd.getVendorType());
		//order.setPaidTime(cmd.getPayTime());
		pmsyProvider.updatePmsyOrder(order);
		
		PmsyPayer pmsyPayer = pmsyProvider.findPmPayersByNameAndContact(order.getUserName(), order.getUserContact());
		if(pmsyPayer != null){
			pmsyPayer.setStatus(PmsyPayerStatus.INACTIVE.getCode());
			pmsyProvider.updatePmPayer(pmsyPayer);
		}
		
	}

	private PmsyOrder checkOrder(Long orderId) {
		PmsyOrder order = pmsyProvider.findPmsyOrderById(orderId);
		if(order == null){
			LOGGER.error("the order {} not found.",orderId);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"the order not found.");
		}
		return order;
	}
	
	private void checkOrderNoIsNull(String orderNo) {
		if(StringUtils.isBlank(orderNo)){
			LOGGER.error("orderNo is null or empty.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"orderNo is null or empty.");
		}
	}
}
