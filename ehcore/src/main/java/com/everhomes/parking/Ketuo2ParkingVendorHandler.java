// @formatter:off
package com.everhomes.parking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PreDestroy;

import com.everhomes.parking.ketuo.*;
import com.everhomes.rest.parking.*;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.everhomes.bigcollection.Accessor;
import com.everhomes.bigcollection.BigCollectionProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.db.DbProvider;
import com.everhomes.flow.Flow;
import com.everhomes.flow.FlowCase;
import com.everhomes.flow.FlowCaseProvider;
import com.everhomes.flow.FlowProvider;
import com.everhomes.flow.FlowService;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.order.OrderUtil;
import com.everhomes.parking.ketuo.KetuoTempFee;
import com.everhomes.rest.flow.FlowAutoStepDTO;
import com.everhomes.rest.flow.FlowStepType;
import com.everhomes.rest.organization.VendorType;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;

@Component(ParkingVendorHandler.PARKING_VENDOR_PREFIX + "KETUO2")
public class Ketuo2ParkingVendorHandler implements ParkingVendorHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(Ketuo2ParkingVendorHandler.class);

	private static final String RECHARGE = "/api/pay/CardRecharge";
	private static final String GET_CARD = "/api/pay/GetCarCardInfo";
	private static final String GET_TYPES = "/api/pay/GetCarTypeList";
	private static final String GET_CARd_RULE = "/api/pay/GetCardRule";
	private static final String GET_TEMP_FEE = "/api/pay/GetParkingPaymentInfo";
	private static final String PAY_TEMP_FEE = "/api/pay/PayParkingFee";
	private static final String ADD_MONTH_CARD = "/api/card/AddMonthCarCardNo_KX";
	private static final String RULE_TYPE = "1"; //只显示ruleType = 1时的充值项
	private static final String CUSTOM = "custom"; //只显示ruleType = 1时的充值项

	
	private CloseableHttpClient httpclient = HttpClients.createDefault();
	
	@Autowired
	private ParkingProvider parkingProvider;
	@Autowired
	private LocaleStringService localeStringService;
	@Autowired
	private LocaleTemplateService localeTemplateService;
	@Autowired
    private ConfigurationProvider configProvider;
	@Autowired
    private UserProvider userProvider;
	@Autowired
	private OrderUtil commonOrderUtil;
	@Autowired
	private FlowService flowService;
    @Autowired
	private FlowProvider flowProvider;
    @Autowired
    private FlowCaseProvider flowCaseProvider;
    @Autowired
    private BigCollectionProvider bigCollectionProvider;
	
	@Autowired
    private DbProvider dbProvider;
    
	@Override
    public GetParkingCardsResponse getParkingCardsByPlate(String ownerType, Long ownerId,
    		Long parkingLotId, String plateNumber) {
        
    	List<ParkingCardDTO> resultList = new ArrayList<ParkingCardDTO>();
		GetParkingCardsResponse response = new GetParkingCardsResponse();
		response.setCards(resultList);

    	KetuoCard card = getCard(plateNumber);

        ParkingCardDTO parkingCardDTO = new ParkingCardDTO();
		if(null != card){
			String expireDate = card.getValidTo();
			this.checkExpireDateIsNull(expireDate,plateNumber);

			long expireTime = strToLong(expireDate);
			long now = System.currentTimeMillis();
			long cardReserveTime = 0;
			
	    	ParkingLot parkingLot = parkingProvider.findParkingLotById(parkingLotId);
	    	Byte isSupportRecharge = parkingLot.getIsSupportRecharge();
	    	if(ParkingSupportRechargeStatus.SUPPORT.getCode() == isSupportRecharge)	{
	    		Integer cardReserveDay = parkingLot.getCardReserveDays();
	    		cardReserveTime = cardReserveDay * 24 * 60 * 60 * 1000L;

	    	}
			
			if(expireTime + cardReserveTime < now){
				response.setToastType(ParkingToastType.CARD_EXPIRED.getCode());

				return response;
			}
			parkingCardDTO.setOwnerType(ParkingOwnerType.COMMUNITY.getCode());
			parkingCardDTO.setOwnerId(ownerId);
			parkingCardDTO.setParkingLotId(parkingLotId);

			if (null != card.getName()) {
				String plateOwnerName = card.getName();
				parkingCardDTO.setPlateOwnerName(plateOwnerName);
			}

			parkingCardDTO.setPlateNumber(plateNumber);
			parkingCardDTO.setPlateOwnerPhone("");
			if(card.getFreeMoney() != 0)
				parkingCardDTO.setFreeAmount(card.getFreeMoney() / 100 + "元/月");
			parkingCardDTO.setIsSupportOnlinePaid(card.getIsAllow().byteValue());
			//parkingCardDTO.setStartTime(startTime);
			parkingCardDTO.setEndTime(expireTime);
			List<KetuoCardType> types = getCardType();
			for(KetuoCardType kt: types) {
				if(card.getCarType().equals(kt.getCarType())) {
					parkingCardDTO.setCardType(kt.getTypeName());
					break;
				}
			}
			parkingCardDTO.setCardTypeId(card.getCarType());
			parkingCardDTO.setCardNumber(card.getCardId().toString());
			parkingCardDTO.setIsValid(true);
			
			resultList.add(parkingCardDTO);
		}else {
			response.setToastType(ParkingToastType.NOT_CARD_USER.getCode());

		}
        
        return response;
    }

    @Override
    public List<ParkingRechargeRateDTO> getParkingRechargeRates(String ownerType, Long ownerId, Long parkingLotId, 
    		String plateNumber, String cardNo) {
    	List<ParkingRechargeRateDTO> result = null;
    	List<KetuoCardRate> list = new ArrayList<>();
		List<KetuoCardType> types = getCardType();

    	if(StringUtils.isBlank(plateNumber)) {
    		for(KetuoCardType k: types) {
    			for(KetuoCardRate rate: getCardRule(k.getCarType())) {
    				if(RULE_TYPE.equals(rate.getRuleType())) {
    					rate.setCarType(k.getCarType());
    					rate.setTypeName(k.getTypeName());
    					list.add(rate);
    				}
    			}
    		}
    	}else{
    		KetuoCard cardInfo = getCard(plateNumber);
    		if(null != cardInfo) {
    			long now = System.currentTimeMillis();
    			if(strToLong(cardInfo.getValidTo()) < now ) {
    				KetuoCardRate ketuoCardRate = new KetuoCardRate();
    		    	ParkingLot parkingLot = parkingProvider.findParkingLotById(parkingLotId);
    		    	
    		    	if(parkingLot.getIsSupportRecharge() == ParkingSupportRechargeStatus.SUPPORT.getCode()) {
        				
        				Integer rechargeMonthCount = parkingLot.getRechargeMonthCount();
        				Integer freeMoney = cardInfo.getFreeMoney();
        				if(rechargeMonthCount <= 0) {
        					LOGGER.error("ParkingLot rechargeMonthCount less than 0, parkingLot={}", parkingLot);
        					throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.ERROR_PLATE_EXIST,
        							"ParkingLot rechargeMonthCount less than 0");
        				}
        				List<KetuoCardRate> rates = getCardRule("2");
        				if(rates.size() !=0) {
        					KetuoCardRate rate = rates.get(0);
        					for(KetuoCardRate kr: rates) {
        						if(Integer.valueOf(kr.getRuleAmount()) == 1)
        							rate = kr;
        					}
        					if(parkingLot.getRechargeType() == ParkingLotRechargeType.ALL.getCode()) {
        						Integer actualPrice = Integer.valueOf(rate.getRuleMoney()) - freeMoney;
        						ketuoCardRate.setRuleMoney(String.valueOf(actualPrice * rechargeMonthCount));

        					}else {
            					Calendar calendar = Calendar.getInstance();
            					calendar.setTimeInMillis(now);
            					int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            					int today = calendar.get(Calendar.DAY_OF_MONTH);
            					Integer actualPrice = Integer.valueOf(rate.getRuleMoney()) - freeMoney;
        						ketuoCardRate.setRuleMoney(String.valueOf(actualPrice * (rechargeMonthCount - 1)
        								+ actualPrice * (maxDay - today + 1) / maxDay ));

            				}
        				}
    		    	}

    		    	ketuoCardRate.setRuleId(CUSTOM);
//    				rate.setCarType(carType);
    		    	ketuoCardRate.setRuleAmount(String.valueOf(parkingLot.getRechargeMonthCount()));
    		    	list.add(ketuoCardRate);
//    				rate.setRuleName(ruleName);
//    				rate.setRuleType(ruleType);
//    				rate.setTypeName(typeName);
    				
    			}else {
    				String cardType = cardInfo.getCarType();
        			String typeName = null;
        			for(KetuoCardType kt: types) {
    					if(cardType.equals(kt.getCarType())) {
    						typeName = kt.getTypeName();
    						break;
    					}
    				}
        			for(KetuoCardRate rate: getCardRule(cardType)) {
        				if(RULE_TYPE.equals(rate.getRuleType())) {
        					rate.setCarType(cardType);
        					rate.setTypeName(typeName);
        					list.add(rate);
        				}
        			}
    			}
    		}
    	}
    	result = list.stream().map( r -> {
			ParkingRechargeRateDTO dto = new ParkingRechargeRateDTO();
			dto.setOwnerId(ownerId);
			dto.setOwnerType(ownerType);
			dto.setParkingLotId(parkingLotId);
			dto.setRateToken(r.getRuleId());
			Map<String, Object> map = new HashMap<String, Object>();
		    map.put("count", r.getRuleAmount());
			String scope = ParkingNotificationTemplateCode.SCOPE;
			int code = ParkingNotificationTemplateCode.DEFAULT_RATE_NAME;
			String locale = "zh_CN";
			String rateName = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
//			dto.setRateName(r.getRuleName());
			dto.setRateName(rateName);
//			dto.setCardType(r.getRuleType());
			dto.setCardType(r.getTypeName());
			dto.setMonthCount(new BigDecimal(r.getRuleAmount()));
			dto.setPrice(new BigDecimal(Integer.parseInt(r.getRuleMoney()) / 100));
			dto.setVendorName(ParkingLotVendor.KETUO2.getCode());
			return dto;
		}).collect(Collectors.toList());
		
		return result;
    }

    final StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

    @Override
    public void notifyParkingRechargeOrderPayment(ParkingRechargeOrder order, String payStatus) {
    	if(order.getRechargeStatus() != ParkingRechargeOrderRechargeStatus.RECHARGED.getCode()) {
			if(payStatus.toLowerCase().equals("fail")) {
				LOGGER.error("pay failed, orderNo={}", order.getId());
			}
			else {
				if(recharge(order)){
					dbProvider.execute((TransactionStatus transactionStatus) -> {
						order.setRechargeStatus(ParkingRechargeOrderRechargeStatus.RECHARGED.getCode());
						order.setRechargeTime(new Timestamp(System.currentTimeMillis()));
						parkingProvider.updateParkingRechargeOrder(order);
						
						String key = "parking-recharge" + order.getId();
						String value = String.valueOf(order.getId());
				        Accessor acc = this.bigCollectionProvider.getMapAccessor(key, "");
				        RedisTemplate redisTemplate = acc.getTemplate(stringRedisSerializer);
				      
				        LOGGER.error("Delete parking order key, key={}", key);
				        redisTemplate.delete(key);
			        
			        return null;
					});
				}
			}
		}
    }
    
    @Override
    public ParkingRechargeRateDTO createParkingRechargeRate(CreateParkingRechargeRateCommand cmd){
    	LOGGER.error("Not support create parkingRechageRate.");
		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_UNSUPPORTED_USAGE,
				"Not support create parkingRechageRate.");
    }
    
    @Override
    public void deleteParkingRechargeRate(DeleteParkingRechargeRateCommand cmd){
    	LOGGER.error("Not support delete parkingRechageRate.");
		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_UNSUPPORTED_USAGE,
				"Not support delete parkingRechageRate.");
    }
    
    private void checkExpireDateIsNull(String expireDate,String plateNo) {
		if(StringUtils.isBlank(expireDate)){
			LOGGER.error("ExpireDate is null, plateNo={}", plateNo);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"ExpireDate is null.");
		}
	}
    
    private long strToLong(String str) {

		long ts;
		try {
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			ts = sdf1.parse(str).getTime();
		} catch (ParseException e) {
			LOGGER.error("data format is not yyyyMMddHHmmss, str={}", str);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"data format is not yyyyMMddHHmmss.");
		}
		return ts;
	}
    
	
//	@Scheduled(cron="0 0 0/2 * * ? ")
//	@Override
//	public void refreshParkingRechargeOrderStatus() {
//		LOGGER.info("refresh recharge status.");
//		List<ParkingRechargeOrder> orderList = parkingProvider.findWaitingParkingRechargeOrders(ParkingLotVendor.KETUO);
//		orderList.stream().map(order -> {
//			
//			if(recharge(order)){
//				order.setRechargeStatus(ParkingRechargeOrderRechargeStatus.RECHARGED.getCode());
//				order.setRechargeTime(new Timestamp(System.currentTimeMillis()));
//				parkingProvider.updateParkingRechargeOrder(order);
//			}
//			return null;
//		});
//	}
	
	public ListCardTypeResponse listCardType(ListCardTypeCommand cmd) {
    	ListCardTypeResponse ret = new ListCardTypeResponse();
    	
        List<ParkingCardType> resultTypes = new ArrayList<>();

		List<KetuoCardType> list = getCardType();
		for(KetuoCardType t: list) {
			ParkingCardType parkingCardType = new ParkingCardType();
			parkingCardType.setTypeId(t.getCarType());
			parkingCardType.setTypeName(t.getTypeName());
			resultTypes.add(parkingCardType);
		}
		ret.setCardTypes(resultTypes);	
    	return ret;
    }
	
	private List<KetuoCardType> getCardType() {
		List<KetuoCardType> result = new ArrayList<>();
		JSONObject param = new JSONObject();
		String json = post(param, GET_TYPES);
		KetuoJsonEntity<KetuoCardType> entity = JSONObject.parseObject(json, new TypeReference<KetuoJsonEntity<KetuoCardType>>(){});

		if(entity.isSuccess()){
			if(null != entity.getData())
				result = entity.getData();
		}
		
		return result;
	}
	
	private List<KetuoCardRate> getCardRule(String carType) {
		List<KetuoCardRate> result = new ArrayList<>();
		JSONObject param = new JSONObject();
		param.put("carType", carType);
		String json = post(param, GET_CARd_RULE);
		
		if(LOGGER.isDebugEnabled())
			LOGGER.debug("Result={}, param={}", json, param);
		
		KetuoJsonEntity<KetuoCardRate> entity = JSONObject.parseObject(json, new TypeReference<KetuoJsonEntity<KetuoCardRate>>(){});
		if(entity.isSuccess()){
			if(null != entity.getData())
				result = entity.getData();
		}
		return result;
	}
	
	private KetuoCard getCard(String plateNumber) {
		KetuoCard card = null;
		JSONObject param = new JSONObject();
		
		//储能月卡车没有 归属地区分
    	plateNumber = plateNumber.substring(1, plateNumber.length());
		param.put("plateNo", plateNumber);
		String json = post(param, GET_CARD);
        
        if(LOGGER.isDebugEnabled())
			LOGGER.debug("Result={}, param={}", json, param);
        
		KetuoJsonEntity<KetuoCard> entity = JSONObject.parseObject(json, new TypeReference<KetuoJsonEntity<KetuoCard>>(){});
		if(entity.isSuccess()){
			List<KetuoCard> list = entity.getData();
			if(null != list && !list.isEmpty()) {
				card = list.get(0);
			}
		}
        
        return card;
    }
	
	private boolean rechargeMonthlyCard(ParkingRechargeOrder order1){

		ParkingRechargeOrder order = ConvertHelper.convert(order1, ParkingRechargeOrder.class);
		JSONObject param = new JSONObject();
		//月卡车没有 归属地区分
		String plateNumber = order.getPlateNumber();

		KetuoCard card = getCard(plateNumber);
		
		if(null == card) {
			String cardType = "2";
			KetuoCardRate ketuoCardRate = null;
			for(KetuoCardRate rate: getCardRule(cardType)) {
				if(rate.getRuleId().equals(order.getRateToken())) {
					ketuoCardRate = rate;
				}
			}
			if(null == ketuoCardRate) {
				LOGGER.error("Rate not found, cmd={}", order);
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
						"Rate not found.");
			}
//			order.setRateName(ketuoCardRate.getRuleName());
//			order.setMonthCount(new BigDecimal(ketuoCardRate.getRuleAmount()));
//			order.setPrice(new BigDecimal(Integer.parseInt(ketuoCardRate.getRuleMoney()) / 100));
			
			Integer payMoney = order.getPrice().intValue()*100 - Integer.parseInt(ketuoCardRate.getRuleMoney()) 
					* (order.getMonthCount().intValue() - 1);

			if(addMonthCard(order.getPlateNumber(), payMoney)) {
				Integer count = order.getMonthCount().intValue();
				
		    	LOGGER.debug("Parking addMonthCard,count={}", count);

				if(count > 1) {
					order.setMonthCount(new BigDecimal(count -1) );
					order.setPrice(new BigDecimal(order.getPrice().intValue()*100 - payMoney));
					if(rechargeMonthlyCard(order)) {
						updateFlowStatus(order);
			    		return true;
					}
				}else {
					updateFlowStatus(order);
				}
				return true;
			}
			return false;
		}else {
			String oldValidEnd = card.getValidTo();
			Long time = strToLong(oldValidEnd);
			long now = System.currentTimeMillis();
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			if(time < now) {
				SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
				String date = sdf2.format(new Date(now)) + " 00:00:00";
				try {
					time = sdf1.parse(date).getTime();
				} catch (ParseException e) {
					LOGGER.info("date={}, time={}", date, time, e);
				}
			}

			String validStart = sdf1.format(addDays(time, 1));
			String validEnd = sdf1.format(addMonth(time, order.getMonthCount().intValue()));
			
			param.put("cardId", card.getCardId());
//			param.put("ruleType", CUSTOM.equals(order.getRateToken())?"2":order.getRateToken());
			param.put("ruleType", RULE_TYPE);
		    param.put("ruleAmount", String.valueOf(order.getMonthCount().intValue()));
		    param.put("payMoney", order.getPrice().intValue()*100);
		    param.put("startTime", validStart);
		    param.put("endTime", validEnd);
		    if(CUSTOM.equals(order.getRateToken())) {
		    	ParkingLot parkingLot = parkingProvider.findParkingLotById(order.getParkingLotId());

		    	if(parkingLot.getRechargeType() == ParkingLotRechargeType.ALL.getCode()) {
		    		param.put("freeMoney", card.getFreeMoney() * order.getMonthCount().intValue());

				}else {
					Calendar calendar = Calendar.getInstance();
					calendar.setTimeInMillis(now);
					int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
					int today = calendar.get(Calendar.DAY_OF_MONTH);
					Integer actualPrice = card.getFreeMoney();
					Integer monthCount = order.getMonthCount().intValue();
					param.put("freeMoney", actualPrice * (monthCount - 1)
							+ actualPrice * (maxDay - today + 1) / maxDay);
				}
			}else{
			    param.put("freeMoney", card.getFreeMoney() * order.getMonthCount().intValue());
			}
			String json = post(param, RECHARGE);
	        
	        if(LOGGER.isDebugEnabled())
				LOGGER.debug("Result={}, param={}", json, param);
	        
			JSONObject jsonObject = JSONObject.parseObject(json);
			Object obj = jsonObject.get("resCode");
			if(null != obj ) {
				int resCode = (int) obj;
				if(resCode == 0)
					return true;
				
			}
			return false;
		}
		
    }
	
	private void updateFlowStatus(ParkingRechargeOrder order) {
		User user = UserContext.current().getUser();
    	LOGGER.debug("ParkingCardRequest pay callback user={}", user);

    	List<ParkingCardRequest> list = parkingProvider.listParkingCardRequests(order.getCreatorUid(), order.getOwnerType(), 
    			order.getOwnerId(), order.getParkingLotId(), order.getPlateNumber(), ParkingCardRequestStatus.SUCCEED.getCode(),
    			null, null, null, null);
    	
    	LOGGER.debug("ParkingCardRequest list size={}", list.size());
    	dbProvider.execute((TransactionStatus transactionStatus) -> {
    		ParkingCardRequest parkingCardRequest = null;
        	for(ParkingCardRequest p: list) {
        		Flow flow = flowProvider.findSnapshotFlow(p.getFlowId(), p.getFlowVersion());
        		String tag1 = flow.getStringTag1();
        		if(null == tag1) {
        			LOGGER.error("Flow tag is null, flow={}", flow);
        			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
        					"Flow tag is null.");
        		}
        		if(ParkingRequestFlowType.INTELLIGENT.getCode() == Integer.valueOf(tag1)) {
        			parkingCardRequest = p;
        			break;
        		}
        	}
        	if(null != parkingCardRequest) {
    			FlowCase flowCase = flowCaseProvider.getFlowCaseById(parkingCardRequest.getFlowCaseId());

        		
            		FlowAutoStepDTO stepDTO = new FlowAutoStepDTO();
            		stepDTO.setFlowCaseId(parkingCardRequest.getFlowCaseId());
            		stepDTO.setFlowMainId(parkingCardRequest.getFlowId());
            		stepDTO.setFlowVersion(parkingCardRequest.getFlowVersion());
            		stepDTO.setFlowNodeId(flowCase.getCurrentNodeId());
            		stepDTO.setAutoStepType(FlowStepType.APPROVE_STEP.getCode());
            		stepDTO.setStepCount(flowCase.getStepCount());
            		flowService.processAutoStep(stepDTO);
            		
            		parkingCardRequest.setStatus(ParkingCardRequestStatus.OPENED.getCode());
        			parkingCardRequest.setOpenCardTime(new Timestamp(System.currentTimeMillis()));
        			parkingProvider.updateParkingCardRequest(parkingCardRequest);
        			
        		
        	}
    		return null;
		});
	}
	
	private boolean addMonthCard(String plateNo, Integer money){

		JSONObject param = new JSONObject();
		plateNo = plateNo.substring(1, plateNo.length());

		param.put("plateNo", plateNo);
		param.put("money", money);
		String json = post(param, ADD_MONTH_CARD);
        
        if(LOGGER.isDebugEnabled())
			LOGGER.debug("Result={}, param={}", json, param);
        
        JSONObject jsonObject = JSONObject.parseObject(json);
		Object obj = jsonObject.get("resCode");
		if(null != obj ) {
			int resCode = (int) obj;
			if(resCode == 0)
				return true;
			
		}
		return false;
    }
	
	private boolean payTempCardFee(ParkingRechargeOrder order){

		JSONObject param = new JSONObject();
//		KetuoTempFee tempFee = getTempFee(order.getPlateNumber());
		param.put("orderNo", order.getOrderToken());
//		param.put("amount", order.getPrice().intValue()*100);
		param.put("amount", order.getPrice().intValue() * 100);
	    param.put("discount", 0);
	    param.put("payType", VendorType.WEI_XIN.getCode().equals(order.getPaidType())?4:5);
		String json = post(param, PAY_TEMP_FEE);
        
        if(LOGGER.isDebugEnabled())
			LOGGER.debug("Result={}, param={}", json, param);
        
//		KetuoJsonEntity entity = JSONObject.parseObject(json, new TypeReference<KetuoJsonEntity>(){});
//		
//		return entity.isSuccess();
        JSONObject jsonObject = JSONObject.parseObject(json);
		Object obj = jsonObject.get("resCode");
		if(null != obj ) {
			int resCode = (int) obj;
			if(resCode == 0)
				return true;
			
		}
		return false;
    }
//	public static void main(String[] args) {
//		String s= "{\"data\":{\"parkingTime\":109568},\"resCode\":0,\"resMsg\":null}";
//		JSONObject json = JSONObject.parseObject(s);
//		Object obj = json.get("resCode");
//		if(null != obj ) {
//			int resCode = (int) obj;
//			if(resCode == 0)
//				return true;
//			
//		}
//		return false;
//		
//	}
	private boolean recharge(ParkingRechargeOrder order){
		if(order.getRechargeType().equals(ParkingRechargeType.MONTHLY.getCode()))
			return rechargeMonthlyCard(order);
		return payTempCardFee(order);
    }
	
	public String post(JSONObject param, String type) {
		HttpPost httpPost = new HttpPost(configProvider.getValue("parking.kexing.url", "") + type);
		StringBuilder result = new StringBuilder();
		
        String key = configProvider.getValue("parking.kexing.key", "");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");

		String iv = sdf2.format(new Date());
        String user = configProvider.getValue("parking.kexing.user", "");
        String pwd = configProvider.getValue("parking.kexing.pwd", "");
        String data = null;
		try {
			data = EncryptUtil.getEncString(param, key, iv);
		} catch (Exception e) {
			LOGGER.error("Parking encrypt param error, param={}, key={}, iv={}", param, key, iv, e);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
    				"Parking encrypt param error.");
		}
		List <NameValuePair> nvps = new ArrayList <NameValuePair>();
		nvps.add(new BasicNameValuePair("data", data));
		CloseableHttpResponse response = null;
		InputStream instream = null;
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF8"));
			httpPost.addHeader("user", user);
			httpPost.addHeader("pwd", pwd);
			response = httpclient.execute(httpPost);
			HttpEntity entity = response.getEntity();

			LOGGER.info("Data from ketuo, status={}", response.getStatusLine().getStatusCode());

			if (entity != null) {
				instream = entity.getContent();
				BufferedReader reader = null;
				reader = new BufferedReader(new InputStreamReader(instream,"utf8"));
				String s;
            	
            	while((s = reader.readLine()) != null){
            		result.append(s);
				}
			}
		} catch (IOException e) {
			LOGGER.error("Parking request error, param={}, key={}, iv={}", param, key, iv, e);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
    				"Parking request error.");
		}finally {
            try {
            	if (null != instream) {
					instream.close();
				}
				if (null != response) {
					response.close();
				}
			} catch (IOException e) {
				LOGGER.error("Parking close instream, response error, param={}, key={}, iv={}", param, key, iv, e);
			}
        }
		String json = result.toString();

		LOGGER.info("Data from ketuo, json={}", json);
		
		return json;
	}
	
	private Timestamp addDays(Long oldPeriod, int days) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(oldPeriod);
		calendar.add(Calendar.SECOND, days);
		Timestamp time = new Timestamp(calendar.getTimeInMillis());
		
		return time;
	}
	
	private Timestamp addMonth(Long oldPeriod, int month) {
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(oldPeriod);
		
		int day = calendar.get(Calendar.DAY_OF_MONTH); 
		if(day == calendar.getActualMaximum(Calendar.DAY_OF_MONTH)){
			calendar.add(Calendar.MONTH, month);
			int d = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
			calendar.set(Calendar.DAY_OF_MONTH, d);
		}else{
			calendar.add(Calendar.MONTH, month - 1);
			int d = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
			calendar.set(Calendar.DAY_OF_MONTH, d);
		}
		
		Timestamp newPeriod = new Timestamp(calendar.getTimeInMillis());
		
		return newPeriod;
	}

	@Override
	public void updateParkingRechargeOrderRate(ParkingRechargeOrder order) {
		//储能月卡车没有 归属地区分
		String plateNumber = order.getPlateNumber();
		if(CUSTOM.equals(order.getRateToken())) {
			order.setRateName(CUSTOM);

		}else {
			KetuoCard cardInfo = getCard(plateNumber);
			KetuoCardRate ketuoCardRate = null;
			String cardType = "2";
			Integer freeMoney = 0;
			if(null != cardInfo) {
				cardType = cardInfo.getCarType();
				freeMoney = cardInfo.getFreeMoney();
			}
			for(KetuoCardRate rate: getCardRule(cardType)) {
				if(rate.getRuleId().equals(order.getRateToken())) {
					ketuoCardRate = rate;
				}
			}   
			if(null == ketuoCardRate) {
				LOGGER.error("Rate not found, cmd={}", order);
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
						"Rate not found.");
			}
			order.setRateName(ketuoCardRate.getRuleName());

			order.setPrice(new BigDecimal(order.getPrice().intValue() - (freeMoney / 100 * order.getMonthCount().intValue() )));

		}
		
	}

	private KetuoTempFee getTempFee(String plateNumber) {
		KetuoTempFee tempFee = null;
		JSONObject param = new JSONObject();
		param.put("plateNo", plateNumber);
		String json = post(param, GET_TEMP_FEE);
        
        if(LOGGER.isDebugEnabled())
			LOGGER.debug("Result={}, param={}", json, param);
        
		KetuoJsonEntity<KetuoTempFee> entity = JSONObject.parseObject(json, new TypeReference<KetuoJsonEntity<KetuoTempFee>>(){});
		if(entity.isSuccess()){
			List<KetuoTempFee> list = entity.getData();
			if(null != list && !list.isEmpty()) {
				tempFee = list.get(0);
			}
		}
        
        return tempFee;
    }

	@Override
	public ParkingTempFeeDTO getParkingTempFee(String ownerType, Long ownerId,
			Long parkingLotId, String plateNumber) {
		KetuoTempFee tempFee = getTempFee(plateNumber);
		
		ParkingTempFeeDTO dto = new ParkingTempFeeDTO();
		if(null == tempFee)
			return dto;
		dto.setPlateNumber(plateNumber);
		dto.setEntryTime(strToLong(tempFee.getEntryTime()));
		dto.setPayTime(strToLong(tempFee.getPayTime()));
		dto.setParkingTime(tempFee.getElapsedTime());
		dto.setDelayTime(tempFee.getDelayTime());
		dto.setPrice(new BigDecimal(tempFee.getPayable() / 100));
//		dto.setPrice(new BigDecimal(0.02).setScale(2, RoundingMode.FLOOR));
		dto.setOrderToken(tempFee.getOrderNo());
		return dto;
	}

	@PreDestroy
	public void destroy() {
		if (null != httpclient) {
			try {
				httpclient.close();
			} catch (IOException e) {
				LOGGER.error("Close httpclient error.");
			}
		}
	}

	@Override
	public OpenCardInfoDTO getOpenCardInfo(GetOpenCardInfoCommand cmd) {
		//TODO:
		ParkingCardRequest parkingCardRequest = parkingProvider.findParkingCardRequestById(cmd.getParkingRequestId());

		ParkingFlow parkingFlow = parkingProvider.getParkingRequestCardConfig(cmd.getOwnerType(), cmd.getOwnerId(), 
				cmd.getParkingLotId(), parkingCardRequest.getFlowId());

		Integer requestMonthCount = 2;
		Byte requestRechargeType = ParkingLotRechargeType.ACTUAL.getCode();
		
		if(null != parkingFlow) {
			requestMonthCount = parkingFlow.getRequestMonthCount();
			requestRechargeType = parkingFlow.getRequestRechargeType();
		}
		
		OpenCardInfoDTO dto = new OpenCardInfoDTO();
		
//		List<ParkingRechargeRateDTO> rates = getParkingRechargeRates(cmd.getOwnerType(), cmd.getOwnerId(), 
//				cmd.getParkingLotId(), "B5720Z", null);
		List<KetuoCardRate> rates = getCardRule("2");
		if(rates.size() !=0) {
			
			KetuoCardRate rate = rates.get(0);
			for(KetuoCardRate kr: rates) {
				if(Integer.valueOf(kr.getRuleAmount()) == 1)
					rate = kr;
			}
			
			List<KetuoCardType> types = getCardType();
			String typeName = null;
			for(KetuoCardType kt: types) {
				if("2".equals(kt.getCarType())) {
					typeName = kt.getTypeName();
					break;
				}
			}
			
			dto.setOwnerId(cmd.getOwnerId());
			dto.setOwnerType(cmd.getOwnerType());
			dto.setParkingLotId(cmd.getParkingLotId());
			dto.setRateToken(rate.getRuleId());
			Map<String, Object> map = new HashMap<String, Object>();
		    map.put("count", rate.getRuleAmount());
			String scope = ParkingNotificationTemplateCode.SCOPE;
			int code = ParkingNotificationTemplateCode.DEFAULT_RATE_NAME;
			String locale = "zh_CN";
			String rateName = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
//			dto.setRateName(r.getRuleName());
			dto.setRateName(rateName);
//			dto.setCardType(r.getRuleType());
			dto.setCardType(typeName);
			dto.setMonthCount(new BigDecimal(rate.getRuleAmount()));
			dto.setPrice(new BigDecimal(Integer.parseInt(rate.getRuleMoney()) / 100));

		}
		
//		dto.setCardNumber("123");
		dto.setPlateNumber(cmd.getPlateNumber());
		Long now = System.currentTimeMillis();
		dto.setOpenDate(now);
		dto.setExpireDate(addMonth2(now, requestMonthCount));
		if(requestRechargeType == ParkingLotRechargeType.ALL.getCode())
			dto.setPayMoney(dto.getPrice().multiply(new BigDecimal(requestMonthCount)));
		else {
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(now);
			int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
			int today = calendar.get(Calendar.DAY_OF_MONTH);
			dto.setPayMoney(dto.getPrice().multiply(new BigDecimal(requestMonthCount-1))
					.add(dto.getPrice().multiply(new BigDecimal(maxDay-today+1)).divide(new BigDecimal(maxDay), RoundingMode.HALF_EVEN)) );

		}

		return dto;
	}
	
	private Long addMonth2(Long oldPeriod, int month) {
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(oldPeriod);
		
		int day = calendar.get(Calendar.DAY_OF_MONTH); 
		if(day == calendar.getActualMaximum(Calendar.DAY_OF_MONTH)){
			calendar.add(Calendar.MONTH, month);
			int d = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
			calendar.set(Calendar.DAY_OF_MONTH, d);
		}else{
			calendar.add(Calendar.MONTH, month-1);
			int d = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
			calendar.set(Calendar.DAY_OF_MONTH, d);
		}
		
		
		return calendar.getTimeInMillis();
	}

	@Override
	public ParkingCarLockInfoDTO getParkingCarLockInfo(GetParkingCarLockInfoCommand cmd) {
		return null;
	}

	@Override
	public void lockParkingCar(LockParkingCarCommand cmd) {

	}

	@Override
	public GetParkingCarNumsResponse getParkingCarNums(GetParkingCarNumsCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}
}
