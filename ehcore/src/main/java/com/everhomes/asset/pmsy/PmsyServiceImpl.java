package com.everhomes.asset.pmsy;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.asset.AssetErrorCodes;
import com.everhomes.asset.DefaultAssetVendorHandler;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.http.HttpUtils;
import com.everhomes.order.OrderEmbeddedHandler;
import com.everhomes.order.OrderUtil;
import com.everhomes.organization.pmsy.PmsyCommunity;
import com.everhomes.organization.pmsy.PmsyOrder;
import com.everhomes.organization.pmsy.PmsyOrderItem;
import com.everhomes.organization.pmsy.PmsyPayer;
import com.everhomes.organization.pmsy.PmsyProvider;
import com.everhomes.organization.pmsy.PmsyService;
import com.everhomes.pay.order.OrderPaymentNotificationCommand;
import com.everhomes.pay.order.SourceType;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.asset.BillIdAndAmount;
import com.everhomes.rest.asset.CreatePaymentBillOrderCommand;
import com.everhomes.rest.order.CommonOrderCommand;
import com.everhomes.rest.order.CommonOrderDTO;
import com.everhomes.rest.order.OrderType;
import com.everhomes.rest.order.PayCallbackCommand;
import com.everhomes.rest.order.PreOrderDTO;
import com.everhomes.rest.pmsy.AddressDTO;
import com.everhomes.rest.pmsy.CreatePmsyBillOrderCommand;
import com.everhomes.rest.pmsy.GetPmsyBills;
import com.everhomes.rest.pmsy.GetPmsyPropertyCommand;
import com.everhomes.rest.pmsy.ListPmsyBillsCommand;
import com.everhomes.rest.pmsy.ListResourceCommand;
import com.everhomes.rest.pmsy.PmBillsOrdersDTO;
import com.everhomes.rest.pmsy.PmsyBillItemDTO;
import com.everhomes.rest.pmsy.PmsyBillType;
import com.everhomes.rest.pmsy.PmsyBillsDTO;
import com.everhomes.rest.pmsy.PmsyBillsResponse;
import com.everhomes.rest.pmsy.PmsyCommunityDTO;
import com.everhomes.rest.pmsy.PmsyOrderStatus;
import com.everhomes.rest.pmsy.PmsyPayerDTO;
import com.everhomes.rest.pmsy.PmsyPayerStatus;
import com.everhomes.rest.pmsy.SearchBillsOrdersCommand;
import com.everhomes.rest.pmsy.SearchBillsOrdersResponse;
import com.everhomes.rest.pmsy.SetPmsyPropertyCommand;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.EhPaymentBillGroups;
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
	
	@Autowired
	private OrderUtil commonOrderUtil;
	
	@Autowired
    private ConfigurationProvider configProvider;
	
	@Autowired
    private DbProvider dbProvider;
	
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
		List<AddressDTO> resultList = new ArrayList<>();
		//判断是否处于左邻测试环境，如果是，则伪造测试数据
		if(isZuolinTest()) {
			//处于左邻测试环境，则伪造测试数据
			AddressDTO dto1 = new AddressDTO();
			dto1.setCustomerId("0100102016050000000742");
			dto1.setPayerId(10L);
			dto1.setProjectId("0040042021605000000001");
			dto1.setResourceId("010010201605000000742");
			dto1.setResourceName("西海明珠花园");
			resultList.add(dto1);
			
			AddressDTO dto2 = new AddressDTO();
			dto2.setCustomerId("0100102016050000000742");
			dto2.setPayerId(10L);
			dto2.setProjectId("0040042021605000000001");
			dto2.setResourceId("010010201605000000742");
			dto2.setResourceName("西海明珠大厦");
			resultList.add(dto2);
		}else {
			//处于正式环境，走正式的对接流程
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
			PmsyPayer pmsyPayer = null;
			if(cmd.getPayerId() != null){
				pmsyPayer = pmsyProvider.findPmPayersById(cmd.getPayerId());
				if(pmsyPayer != null && (!pmsyPayer.getUserName().equals(cmd.getUserName()) ||
						!pmsyPayer.getUserContact().equals(cmd.getUserContact()))){
					LOGGER.error("the payer is not exists.");
					throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
							"the payer is not exists.");
				}
			}else{
				pmsyPayer = pmsyProvider.findPmPayersByNameAndContact(cmd.getUserName(),cmd.getUserContact());
				if(pmsyPayer == null){
					pmsyPayer = new PmsyPayer();
					User user = UserContext.current().getUser();
					Integer namespaceId = UserContext.current().getNamespaceId();
					Long userId = user.getId();
					pmsyPayer.setCreateTime(new Timestamp(System.currentTimeMillis()));
					pmsyPayer.setCreatorUid(userId);
					pmsyPayer.setNamespaceId(namespaceId);
					pmsyPayer.setStatus(PmsyPayerStatus.WAITING.getCode());
					pmsyPayer.setUserContact(cmd.getUserContact());
					pmsyPayer.setUserName(cmd.getUserName());
					pmsyProvider.createPmPayer(pmsyPayer);
				}
			}
			String json = PmsyHttpUtil.post(configProvider.getValue("haian.siyuan", ""),"UserRev_OwnerVerify", cmd.getUserName(), cmd.getUserContact(), "", "", "", "", "");
			if(json == null){
				LOGGER.error("the request of siyuan is fail.");
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, 
						"the request of siyuan is fail.");
			}
			
			Long payerId = pmsyPayer.getId();
			Gson gson = new Gson();
			Map map = gson.fromJson(json, Map.class);
			if(map.containsKey("_ERROR"))
				return resultList;
			List list = (List) map.get("UserRev_OwnerVerify");
			Map map2 = (Map) list.get(0);
			List list2 = (List) map2.get("Syswin");
			resultList = (List<AddressDTO>) list2.stream().map(r -> {
				Map map3 = (Map) r;
				AddressDTO dto = new AddressDTO();
				dto.setCustomerId((String)map3.get("CusID"));
				dto.setPayerId(payerId);
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
		}
		return resultList;
	}
	@Override
	public PmsyBillsResponse listPmBills(ListPmsyBillsCommand cmd) {
		PmsyBillsResponse response = new PmsyBillsResponse();
		List<PmsyBillsDTO> requests = response.getRequests();
		//判断是否处于左邻测试环境，如果是，则伪造测试数据
		if(isZuolinTest()) {
			//处于左邻测试环境，则伪造测试数据
			BigDecimal totalAmount = BigDecimal.ZERO;
			for(int i = 0;i < 1;i++) {
				PmsyBillItemDTO dto = new PmsyBillItemDTO();
				dto.setBillId("00200220180900003956");
				dto.setCustomerId(cmd.getCustomerId());
				dto.setBillDateStr(1283901723L);
				dto.setReceivableAmount(new BigDecimal("0.01"));
				dto.setDebtAmount(new BigDecimal("0.01"));
				totalAmount = totalAmount.add(new BigDecimal("0.01"));
				PmsyBillsDTO newMonthlyBill = new PmsyBillsDTO();
				newMonthlyBill.setBillDateStr(1283901723L);
				newMonthlyBill.setMonthlyDebtAmount(new BigDecimal("0.01"));
				newMonthlyBill.setMonthlyReceivableAmount(new BigDecimal("0.01"));
				List<PmsyBillItemDTO> newRequests = new ArrayList<PmsyBillItemDTO>();
				newRequests.add(dto);
				newMonthlyBill.setRequests(newRequests);
				requests.add(newMonthlyBill);
			}
			response.setTotalAmount(new BigDecimal("0.01"));
		}else {
			//处于正式环境，走正式的对接流程
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
			if(cmd.getBillType() == null){
				LOGGER.error("the billType is not null.");
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
						"the billType is not null.");
			}
			String startDate = "";
			String endDate = "";
			if(cmd.getStartDate() != null)
				startDate = TimeToString(cmd.getStartDate());
			if(cmd.getEndDate() != null)
				endDate = TimeToString(cmd.getEndDate());
			String json = null;
			json = PmsyHttpUtil.post(configProvider.getValue("haian.siyuan", ""),"UserRev_GetFeeList", cmd.getCustomerId(), startDate,
					endDate, cmd.getProjectId(), cmd.getBillType().getCode(), "", "");
			Gson gson = new Gson();
			Map map = gson.fromJson(json, Map.class);
			
			List list = (List) map.get("UserRev_GetFeeList");
			Map map2 = (Map) list.get(0);
			List list2 = (List) map2.get("Syswin");
			HashSet<Long> calculateMonthSet = new HashSet<Long>();
			BigDecimal totalAmount = new BigDecimal(0);
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
						monthlyReceivableAmount = monthlyReceivableAmount.add(receivableAmount);
						monthlyDebtAmount = monthlyDebtAmount.add(debtAmount);
						mb.setMonthlyReceivableAmount(monthlyReceivableAmount);
						mb.setMonthlyDebtAmount(monthlyDebtAmount);
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
			/*PmsyCommunity pmsyCommunity = pmsyProvider.findPmsyCommunityByToken(cmd.getProjectId());
			if(pmsyCommunity == null){
				LOGGER.error("pmsyCommunity relation is not exist.");
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
						"pmsyCommunity relation is not exist.");
			}*/
			//response.setContact(pmsyCommunity.getContact());
			response.setCustomerId(cmd.getCustomerId());
			response.setMonthCount(calculateMonthSet.size());
			response.setPayerId(cmd.getPayerId());
			response.setResourceId(cmd.getResourceId());
			//response.setBillTip(pmsyCommunity.getBillTip());
			response.setTotalAmount(totalAmount);
			response.setProjectId(cmd.getProjectId());
			//按时间排序
			Collections.sort(requests);
		}
		return response;
	}
	
	@Override
	public PmsyBillsDTO getMonthlyPmBill(GetPmsyBills cmd){
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
		
		String url = configProvider.getValue("haian.siyuan", "");
		
		/*String json = PmsyHttpUtil.post(url,"UserRev_GetFeeList", cmd.getCustomerId(), TimeToString(cmd.getBillDateStr()),
				TimeToString(cmd.getBillDateStr()), cmd.getProjectId(), "01", "", "");*/
		
		Map<String, String> params = new HashMap<String, String>();
		try {
			params.put("strToken", "syswin");
			params.put("p0", "UserRev_GetFeeList");
			params.put("p1", cmd.getCustomerId());
			if(LOGGER.isDebugEnabled()) {
    			LOGGER.debug("siyuan->/pmsy/getPmsyBills->getMonthlyPmBill()-TimeToString(), billDateStr={}, TimeToString={}",
    					cmd.getBillDateStr(), cmd.getBillDateStr());
    		}
			params.put("p2", cmd.getBillDateStr());
			params.put("p3", cmd.getBillDateStr());
			params.put("p4", cmd.getProjectId());
			params.put("p5", "01");
			params.put("p6", "");
			params.put("p7", "");
			if(LOGGER.isDebugEnabled()) {
    			LOGGER.debug("Request to siyuan, url={}, param={}", url, params);
    		}
			String result = HttpUtils.post(url, params, 600, false);
			if(LOGGER.isDebugEnabled()) {
    			LOGGER.debug("Response from siyuan, result={}", result);
    		}
			String json = result.substring(result.indexOf(">{")+1, result.indexOf("</string>"));
			if(LOGGER.isDebugEnabled()) {
    			LOGGER.debug("Response from siyuan, result->substring={}", json);
    		}
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
				if(!cmd.getBillDateStr().equals((String)map3.get("RepYears")))
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
				monthlyReceivableAmount = monthlyReceivableAmount.add(receivableAmount);
				monthlyDebtAmount = monthlyDebtAmount.add(debtAmount);
				
				mb.setBillDateStr(billDateStr);
			}
			mb.setMonthlyReceivableAmount(monthlyReceivableAmount);
			mb.setMonthlyDebtAmount(monthlyDebtAmount);
		} catch (Exception e) {
			LOGGER.debug("getMonthlyPmBill from siyuan, url={}, param={}, exception={}", url, params, e);
		}
		return mb;
	}
	
	@Override
	public SearchBillsOrdersResponse searchBillingOrders(SearchBillsOrdersCommand cmd){
		SearchBillsOrdersResponse response = new SearchBillsOrdersResponse();
		if(cmd.getCommunityId() == null){
			LOGGER.error("Invalid parameter , communityId is not null.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid parameter,communityId is not null");
		}
		Timestamp startDate = null;
		Timestamp endDate = null;
		if(cmd.getStartDate() != null)
			startDate = new Timestamp(cmd.getStartDate());
		if(cmd.getEndDate() != null)
			endDate = new Timestamp(cmd.getEndDate());
		List<PmsyOrder> list = pmsyProvider.searchBillingOrders(AppConstants.PAGINATION_DEFAULT_SIZE,cmd.getCommunityId(),cmd.getPageAnchor(),startDate, endDate, cmd.getUserName(), cmd.getUserContact());
		
		Long[] ids = list.stream().map(r -> r.getId()).collect(Collectors.toList()).toArray(new Long[0]);
		List<PmsyOrderItem> orderItemList = pmsyProvider.ListBillOrderItems(ids);
		outer:
		for(PmsyOrder order:list){
			long billdate = 0;
			for(PmsyOrderItem item:orderItemList){
				if(order.getId().equals(item.getOrderId())){
					
					List<PmsyOrderItem> items = order.getItems();
					if(items == null)
						items = new ArrayList<PmsyOrderItem>();
					items.add(item);
					order.setItems(items);
					order.setBillDate(item.getBillDate().getTime());
					if(items.size()==1){
						billdate = item.getBillDate().getTime();
					}
					if(billdate != item.getBillDate().getTime()){
						order.setBillDate(0L);
						continue outer;
					}
						
				}
			}
		}
		
		if(list.size() > 0){
    		response.setRequests(list.stream().map(r -> ConvertHelper.convert(r, PmBillsOrdersDTO.class))
    				.collect(Collectors.toList()));
    		if(list.size() != AppConstants.PAGINATION_DEFAULT_SIZE){
        		response.setNextPageAnchor(null);
        	}else{
        		response.setNextPageAnchor(list.get(list.size()-1).getId());
        	}
    	}
		
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
	
	@Override
	public CommonOrderDTO createPmBillOrder(CreatePmsyBillOrderCommand cmd){
		
		PmsyOrder order = createPmsyOrder(cmd);
		
		//调用统一处理订单接口，返回统一订单格式
		CommonOrderCommand orderCmd = new CommonOrderCommand();
		orderCmd.setBody(order.getUserName());
		orderCmd.setOrderNo(order.getId().toString());
		orderCmd.setOrderType(OrderType.OrderTypeEnum.PMSIYUAN.getPycode());
		orderCmd.setSubject("物业缴费订单简要描述");
		orderCmd.setTotalFee(order.getOrderAmount());
		CommonOrderDTO dto = null;
		try {
			dto = commonOrderUtil.convertToCommonOrderTemplate(orderCmd);
		} catch (Exception e) {
			LOGGER.error("convertToCommonOrder is fail.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"convertToCommonOrder is fail.");
		}
		return dto;
	}
	
	public PreOrderDTO createPmBillOrderV2(CreatePmsyBillOrderCommand cmd){
		PmsyOrder order = new PmsyOrder();
		//判断是否处于左邻测试环境，如果是，则伪造测试数据
		if(isZuolinTest()) {
			//处于左邻测试环境，则伪造测试数据
			order = createPmsyOrderForZuolinTest(cmd);
		}else {
			//处于正式环境，走正式的对接流程
			//避免重复支付
			List<String> billIds = cmd.getBillIds();
			for(String billId : billIds) {
				List<PmsyOrder> results = pmsyProvider.findPmsyOrderItemsByOrderId(billId);
				if(results != null && results.size() > 0) {
					throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, "Bills have been paid");
				}
			}
			order = createPmsyOrder(cmd);
		}
		String handlerPrefix = DefaultAssetVendorHandler.DEFAULT_ASSET_VENDOR_PREFIX;
        DefaultAssetVendorHandler handler = PlatformContext.getComponent(handlerPrefix + "HAIAN");
        CreatePaymentBillOrderCommand createPaymentBillOrderCommand = new CreatePaymentBillOrderCommand(); 
        createPaymentBillOrderCommand.setBusinessOrderType(OrderType.OrderTypeEnum.PMSIYUAN.getPycode());
        String amount = changePayAmount(order.getOrderAmount());
        createPaymentBillOrderCommand.setAmount(amount);
        createPaymentBillOrderCommand.setNamespaceId(UserContext.getCurrentNamespaceId());
        createPaymentBillOrderCommand.setClientAppName(cmd.getClientAppName());
        //通过账单组获取到账单组的bizPayeeType（收款方账户类型）和bizPayeeId（收款方账户id）
  		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
  		EhPaymentBillGroups t = Tables.EH_PAYMENT_BILL_GROUPS.as("t");
  		SelectQuery<Record> query = context.selectQuery();
  		query.addSelect(t.ID, t.BIZ_PAYEE_ID, t.BIZ_PAYEE_TYPE);
  			query.addFrom(t);
  			query.addConditions(t.NAMESPACE_ID.eq(999993));//这里写死了海岸馨的域空间，因为是定制开发
  			query.fetch().map(r -> {
        	  createPaymentBillOrderCommand.setBillGroupId(r.getValue(t.ID));
              return null;
        });
  		createPaymentBillOrderCommand.setSourceType(SourceType.MOBILE.getCode());//手机APP支付
  		
  		//issue-27397 ： 物业缴费V6.8（海岸馨服务项目对接）
  		createPaymentBillOrderCommand.setExtendInfo(cmd.getHaianCommunityName());
  		//issue-38347: 【物业缴费6.8】beta环境，账单支付成功后依然存在，可重复支付
  		createPaymentBillOrderCommand.setPmsyOrderId(order.getId().toString());
  		
        return handler.createOrder(createPaymentBillOrderCommand);
	}
	
	private PmsyOrder createPmsyOrderForZuolinTest(CreatePmsyBillOrderCommand cmd){
		User user = UserContext.current().getUser();
		Integer namespaceId = UserContext.getCurrentNamespaceId();
		Long userId = user.getId();
		
		PmsyOrder order = new PmsyOrder();
		order.setCreateTime(new Timestamp(System.currentTimeMillis()));
		order.setCreatorUid(userId);
		order.setNamespaceId(namespaceId);
		order.setOwnerType(cmd.getOwnerType());
		order.setStatus(PmsyOrderStatus.UNPAID.getCode());
		order.setOrderAmount(cmd.getOrderAmount());
		order.setProjectId(cmd.getProjectId());
		order.setCustomerId(cmd.getCustomerId());
		pmsyProvider.createPmsyOrder(order);
		
		List<PmsyOrderItem> pmsyOrderItemList = new ArrayList<PmsyOrderItem>();
		for(String id:cmd.getBillIds()){
			PmsyOrderItem orderItem = new PmsyOrderItem();
			orderItem.setCreateTime(new Timestamp(System.currentTimeMillis()));
			orderItem.setBillId(id);
			orderItem.setCustomerId(cmd.getCustomerId());
			orderItem.setOrderId(order.getId());
			pmsyOrderItemList.add(orderItem);
		}
		pmsyProvider.createPmsyOrderItem(pmsyOrderItemList);
		return order;
	}
	
	private PmsyOrder createPmsyOrder(CreatePmsyBillOrderCommand cmd){
		User user = UserContext.current().getUser();
		Integer namespaceId = UserContext.getCurrentNamespaceId();
		Long userId = user.getId();
		
		/*PmsyCommunity pmsyCommunity = pmsyProvider.findPmsyCommunityByToken(cmd.getProjectId());
		if(pmsyCommunity == null){
			LOGGER.error("pmsyCommunity relation is not exist.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"pmsyCommunity relation is not exist.");
		}*/
		PmsyPayer payer = pmsyProvider.findPmPayersById(cmd.getPmPayerId());
		
		PmsyOrder order = new PmsyOrder();
		order.setCreateTime(new Timestamp(System.currentTimeMillis()));
		order.setCreatorUid(userId);
		order.setNamespaceId(namespaceId);
		//order.setOwnerId(pmsyCommunity.getCommunityId());
		order.setOwnerType(cmd.getOwnerType());
		order.setStatus(PmsyOrderStatus.UNPAID.getCode());
		order.setUserContact(payer.getUserContact());
		order.setUserName(payer.getUserName());
		order.setOrderAmount(cmd.getOrderAmount());
		order.setProjectId(cmd.getProjectId());
		order.setCustomerId(cmd.getCustomerId());
		pmsyProvider.createPmsyOrder(order);
		
		String json = PmsyHttpUtil.post(configProvider.getValue("haian.siyuan", ""),"UserRev_GetFeeList", cmd.getCustomerId(), "",
				"", cmd.getProjectId(), PmsyBillType.UNPAID.getCode(), "", "");
		Gson gson = new Gson();
		Map map = gson.fromJson(json, Map.class);
		List list = (List) map.get("UserRev_GetFeeList");
		Map map2 = (Map) list.get(0);
		List list2 = (List) map2.get("Syswin");
		
		List<PmsyOrderItem> pmsyOrderItemList = new ArrayList<PmsyOrderItem>();
		for(Object o:list2){
			Map map3 = (Map) o;
			for(String id:cmd.getBillIds()){
				if(id.equals((String)map3.get("ID"))){
					PmsyOrderItem orderItem = new PmsyOrderItem();
					orderItem.setCreateTime(new Timestamp(System.currentTimeMillis()));
					orderItem.setBillId(id);
					Long billDateStr = StringToTime((String)map3.get("RepYears"));
					orderItem.setBillDate(new Timestamp(billDateStr));
					orderItem.setCustomerId(cmd.getCustomerId());
					orderItem.setItemName((String)map3.get("IpItemName"));
					orderItem.setOrderId(order.getId());
					orderItem.setResourceId((String)map3.get("ResID"));
					BigDecimal priFailures = new BigDecimal((String)map3.get("PriFailures"));
					BigDecimal lFFailures = new BigDecimal((String)map3.get("LFFailures"));
					BigDecimal debtAmount = priFailures.add(lFFailures);
					orderItem.setBillAmount(debtAmount);
					pmsyOrderItemList.add(orderItem);
				}
			}
			
		}
		pmsyProvider.createPmsyOrderItem(pmsyOrderItemList);
		return order;
	}
	
	@Override
	public void setPmProperty(SetPmsyPropertyCommand cmd){
		PmsyCommunity pmsyCommunity = pmsyProvider.findPmsyCommunityById(cmd.getCommunityId());
		pmsyCommunity.setBillTip(cmd.getBillTip());
		pmsyCommunity.setContact(cmd.getContact());
		
		pmsyProvider.updatePmsyCommunity(pmsyCommunity);
	}
	
	@Override
	public PmsyCommunityDTO getPmProperty(GetPmsyPropertyCommand cmd){
		
		return ConvertHelper.convert(pmsyProvider.findPmsyCommunityById(cmd.getCommunityId()), PmsyCommunityDTO.class);
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
	
	private String changePayAmount(BigDecimal amount){

        if(amount == null){
            return "0";
        }
        return amount.multiply(new BigDecimal(100)).toString();
    }

	public void payNotify(OrderPaymentNotificationCommand cmd) {
		String handlerPrefix = DefaultAssetVendorHandler.DEFAULT_ASSET_VENDOR_PREFIX;
        DefaultAssetVendorHandler handler = PlatformContext.getComponent(handlerPrefix + "HAIAN");
        //支付模块回调接口，通知支付结果
        handler.payNotify(cmd);
    }
	
	public boolean isZuolinTest() {
		//判断是否处于左邻测试环境，如果是，则伪造测试数据
		String haianEnvironment = configProvider.getValue(999993, "pay.v2.asset.haian_environment", "");
		if(haianEnvironment.equals("beta")) {
			return true;
		}
		return false;
	}
}
