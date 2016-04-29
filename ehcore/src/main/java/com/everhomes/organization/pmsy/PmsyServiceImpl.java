package com.everhomes.organization.pmsy;

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

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.order.OrderEmbeddedHandler;
import com.everhomes.rest.order.OrderType;
import com.everhomes.rest.order.PayCallbackCommand;
import com.everhomes.rest.pmsy.AddressDTO;
import com.everhomes.rest.pmsy.GetPmsyBills;
import com.everhomes.rest.pmsy.ListPmsyBillsCommand;
import com.everhomes.rest.pmsy.ListResourceCommand;
import com.everhomes.rest.pmsy.PmsyBillsDTO;
import com.everhomes.rest.pmsy.PmsyBillItemDTO;
import com.everhomes.rest.pmsy.PmsyPayerDTO;
import com.everhomes.rest.pmsy.PmsyBillsResponse;
import com.everhomes.rest.pmsy.PmsyPayerStatus;
import com.everhomes.rest.pmsy.SearchBillsOrdersResponse;
import com.everhomes.rest.pmsy.searchBillsOrdersCommand;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import com.google.gson.Gson;

@Component
@SuppressWarnings({ "rawtypes", "unchecked" })
public class PmsyServiceImpl implements PmsyService{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PmsyServiceImpl.class);
	
	@Autowired
	private PmsyProvider pmsyProvider;
	
	@Override
	public List<PmsyPayerDTO> listPmPayers(){
		User user = UserContext.current().getUser();
		Integer namespaceId = UserContext.current().getNamespaceId();
		Long userId = user.getId();
		List<PmsyPayer> list = pmsyProvider.listPmPayers(userId, namespaceId);
		
		return list.stream().map(r -> ConvertHelper.convert(r, PmsyPayerDTO.class)).collect(Collectors.toList());
	}
	
	@Override
	public List<AddressDTO> listAddresses(ListResourceCommand cmd){
		if(cmd.getUserName() == null){
			LOGGER.error("the username is not null.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"the username is not null.");
		}
		if(cmd.getUserContact() == null){
			LOGGER.error("the userContact is not null.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"the userContact is not null.");
		}
		if(cmd.getPayerId() != null){
			PmsyPayer pmsyPayer = pmsyProvider.findPmPayersById(cmd.getPayerId());
			if(pmsyPayer != null && (!pmsyPayer.getUserName().equals(cmd.getUserName()) ||
					!pmsyPayer.getUserContact().equals(cmd.getUserContact()))){
				LOGGER.error("the payerId is not exists.");
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
						"the payerId is not exists.");
			}
		}
		List<AddressDTO> resultList = new ArrayList<>();
		String json = PmsyHttpUtil.post("UserRev_OwnerVerify", cmd.getUserName(), cmd.getUserContact(), "", "", "", "", "");
		if(json == null){
			LOGGER.error("the request of siyuan is fail.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, 
					"the request of siyuan is fail.");
		}
		PmsyPayer newPmsyPayer = new PmsyPayer();
		User user = UserContext.current().getUser();
		Integer namespaceId = UserContext.current().getNamespaceId();
		Long userId = user.getId();
		newPmsyPayer.setCreateTime(new Timestamp(System.currentTimeMillis()));
		newPmsyPayer.setCreatorUid(userId);
		newPmsyPayer.setNamespaceId(namespaceId);
		newPmsyPayer.setStatus(PmsyPayerStatus.WAITING.getCode());
		newPmsyPayer.setUserContact(cmd.getUserContact());
		newPmsyPayer.setUserName(cmd.getUserName());
		pmsyProvider.createPmPayer(newPmsyPayer);
		
		Gson gson = new Gson();
		Map map = gson.fromJson(json, Map.class);
		List list = (List) map.get("UserRev_OwnerVerify");
		Map map2 = (Map) list.get(0);
		List list2 = (List) map2.get("Syswin");
		resultList = (List<AddressDTO>) list2.stream().map(r -> {
			Map map3 = (Map) r;
			AddressDTO dto = new AddressDTO();
			dto.setCustomerId((String)map3.get("CusID"));
			dto.setPayerId(cmd.getPayerId());
			dto.setProjectId((String)map3.get("ProjectID"));
			dto.setResourceId((String)map3.get("ResID"));
			StringBuilder resourceName = new StringBuilder();
			resourceName.append(map3.get("ProjectName"))
						.append(" ")
						.append(map3.get("BuildingName"))
						.append(" ")
						.append(map3.get("ResName"));
			dto.setResourceName(resourceName.toString());
			return dto;
		}).collect(Collectors.toList());
		return resultList;
	}
	@Override
	public PmsyBillsResponse listPmBills(ListPmsyBillsCommand cmd) {
		if(cmd.getCustomerId() == null){
			LOGGER.error("the id of customer is not null.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"the id of customer is not null.");
		}
		if(cmd.getProjectId() == null){
			LOGGER.error("the id of project is not null.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"the id of project is not null.");
		}
		if(StringUtils.isBlank(cmd.getBillType())){
			LOGGER.error("the billType is not null.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"the billType is not null.");
		}
		PmsyBillsResponse response = new PmsyBillsResponse();
		String startDate = "";
		String endDate = "";
		if(cmd.getStartDate() != null)
			startDate = TimeToString(cmd.getStartDate());
		if(cmd.getEndDate() != null)
			endDate = TimeToString(cmd.getEndDate());
		String json = PmsyHttpUtil.post("UserRev_GetFeeList", cmd.getCustomerId(), startDate,
				endDate, cmd.getProjectId(), cmd.getBillType(), "", "");
		Gson gson = new Gson();
		Map map = gson.fromJson(json, Map.class);
		List list = (List) map.get("UserRev_GetFeeList");
		Map map2 = (Map) list.get(0);
		List list2 = (List) map2.get("Syswin");
		HashSet<Long> calculateMonthSet = new HashSet<Long>();
		BigDecimal totalAmount = new BigDecimal(0);
		
		List<PmsyBillsDTO> requests = response.getRequests();
		
		outer:
		for(Object o:list2){
			Map map3 = (Map) o;
			if(!cmd.getResourceId().equals((String)map3.get("ResID")))
				continue;
			PmsyBillItemDTO dto = new PmsyBillItemDTO();
			dto.setCustomerId(cmd.getCustomerId());
			dto.setBillId((String)map3.get("ID"));
			Long billDateStr = StringToTime((String)map3.get("RepYears"));
			dto.setBillDateStr(billDateStr);
			dto.setItemName((String)map3.get("IpItemName"));
			BigDecimal priRev = new BigDecimal((String)map3.get("PriRev"));
			BigDecimal lFRev = new BigDecimal((String)map3.get("LFRev"));
			BigDecimal receivableAmount = priRev.add(lFRev);
			dto.setReceivableAmount(receivableAmount);
			BigDecimal priFailures = new BigDecimal((String)map3.get("PriFailures"));
			BigDecimal lFFailures = new BigDecimal((String)map3.get("LFFailures"));
			BigDecimal debtAmount = priFailures.add(lFFailures);
			dto.setDebtAmount(debtAmount);
			calculateMonthSet.add(billDateStr);
			totalAmount = totalAmount.add(debtAmount);
			
			for(PmsyBillsDTO mb:requests){
				if(mb.getBillDateStr().longValue() == billDateStr.longValue()){
					mb.getRequests().add(dto);
					BigDecimal monthlyReceivableAmount = mb.getMonthlyDebtAmount();
					BigDecimal monthlyDebtAmount = mb.getMonthlyDebtAmount();
					mb.setMonthlyReceivableAmount(monthlyReceivableAmount.add(receivableAmount));
					mb.setMonthlyDebtAmount(monthlyDebtAmount.add(debtAmount));
					continue outer;
				}
			}
			PmsyBillsDTO newMonthlyBill = new PmsyBillsDTO();
			newMonthlyBill.setBillDateStr(billDateStr);
			newMonthlyBill.setMonthlyDebtAmount(debtAmount);
			newMonthlyBill.setMonthlyReceivableAmount(receivableAmount);
			List<PmsyBillItemDTO> newRequests = new ArrayList<PmsyBillItemDTO>();
			newRequests.add(dto);
			newMonthlyBill.setRequests(newRequests);
			requests.add(newMonthlyBill);
		}
		response.setContact("123123");
		response.setCustomerId(cmd.getCustomerId());
		response.setMonthCount(calculateMonthSet.size());
		response.setPayerId(cmd.getPayerId());
		response.setResourceId(cmd.getResourceId());
		response.setTip("11111111111");
		response.setTotalAmount(totalAmount);
		response.setProjectId(cmd.getProjectId());
		//按时间排序
		Collections.sort(requests);
		return response;
	}
	@Override
	public PmsyBillsDTO findMonthlyPmBill(GetPmsyBills cmd){
		PmsyBillsDTO mb = new PmsyBillsDTO();
		List<PmsyBillItemDTO> requests = mb.getRequests();
		if(requests == null)
			requests = new ArrayList<PmsyBillItemDTO>();
		mb.setRequests(requests);
		BigDecimal monthlyReceivableAmount = mb.getMonthlyDebtAmount();
		if(monthlyReceivableAmount == null)
			monthlyReceivableAmount = new BigDecimal(0);
		BigDecimal monthlyDebtAmount = mb.getMonthlyDebtAmount();
		if(monthlyDebtAmount == null)
			monthlyDebtAmount = new BigDecimal(0);
		
		String json = PmsyHttpUtil.post("UserRev_GetFeeList", cmd.getCustomerId(), TimeToString(cmd.getBillDateStr()),
				TimeToString(cmd.getBillDateStr()), cmd.getProjectId(), "01", "", "");
		Gson gson = new Gson();
		Map map = gson.fromJson(json, Map.class);
		List list = (List) map.get("UserRev_GetFeeList");
		Map map2 = (Map) list.get(0);
		List list2 = (List) map2.get("Syswin");
		
		for(Object o:list2){
			Map map3 = (Map) o;
			if(!cmd.getResourceId().equals((String)map3.get("ResID")))
				continue;
			Long billDateStr = StringToTime((String)map3.get("RepYears"));
			if(cmd.getBillDateStr().longValue() != billDateStr.longValue())
				continue;
			PmsyBillItemDTO dto = new PmsyBillItemDTO();
			dto.setCustomerId(cmd.getCustomerId());
			dto.setBillId((String)map3.get("ID"));
				
			dto.setBillDateStr(billDateStr);
			dto.setItemName((String)map3.get("IpItemName"));
			BigDecimal priRev = new BigDecimal((String)map3.get("PriRev"));
			BigDecimal lFRev = new BigDecimal((String)map3.get("LFRev"));
			BigDecimal receivableAmount = priRev.add(lFRev);
			dto.setReceivableAmount(receivableAmount);
			BigDecimal priFailures = new BigDecimal((String)map3.get("PriFailures"));
			BigDecimal lFFailures = new BigDecimal((String)map3.get("LFFailures"));
			BigDecimal debtAmount = priFailures.add(lFFailures);
			dto.setDebtAmount(debtAmount);
			
			requests.add(dto);
			mb.setMonthlyReceivableAmount(monthlyReceivableAmount.add(receivableAmount));
			mb.setMonthlyDebtAmount(monthlyDebtAmount.add(debtAmount));
			mb.setBillDateStr(billDateStr);
		}
		return mb;
	}
	
	@Override
	public SearchBillsOrdersResponse searchBillingOrders(searchBillsOrdersCommand cmd){
		SearchBillsOrdersResponse response = new SearchBillsOrdersResponse();
		
		return response;
	}
	
	@Override
	public void notifyPmsyOrderPayment(PayCallbackCommand cmd){
		OrderEmbeddedHandler orderEmbeddedHandler = this.getOrderHandler(cmd.getOrderType());
		LOGGER.debug("OrderEmbeddedHandler="+orderEmbeddedHandler.getClass().getName());
		if(cmd.getPayStatus().equalsIgnoreCase("success"))
			orderEmbeddedHandler.paySuccess(cmd);
		if(cmd.getPayStatus().equalsIgnoreCase("fail"))
			orderEmbeddedHandler.payFail(cmd);
	}
	
	private OrderEmbeddedHandler getOrderHandler(String orderType) {
		return PlatformContext.getComponent(OrderEmbeddedHandler.ORDER_EMBEDED_OBJ_RESOLVER_PREFIX+this.getOrderTypeCode(orderType));
	}
	
	private String getOrderTypeCode(String orderType) {
		Integer code = OrderType.OrderTypeEnum.getCodeByPyCode(orderType);
		if(code==null){
			LOGGER.error("Invalid parameter,orderType not found in OrderType.orderType="+orderType);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid parameter,orderType not found in OrderType");
		}
		LOGGER.debug("orderTypeCode="+code);
		return String.valueOf(code);
	}
	
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
