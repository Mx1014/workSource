// @formatter:off
package com.everhomes.parking.handler;

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

import com.everhomes.parking.*;
import com.everhomes.parking.ketuo.*;
import com.everhomes.rest.parking.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.db.DbProvider;
import com.everhomes.flow.Flow;
import com.everhomes.flow.FlowCase;
import com.everhomes.flow.FlowCaseProvider;
import com.everhomes.flow.FlowProvider;
import com.everhomes.flow.FlowService;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.parking.ketuo.KetuoTempFee;
import com.everhomes.rest.flow.FlowAutoStepDTO;
import com.everhomes.rest.flow.FlowStepType;
import com.everhomes.rest.organization.VendorType;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;

/**
 * 科兴 正中会 停车对接
 */
@Component
public class Ketuo2ParkingVendorHandler extends AbstractCommonParkingVendorHandler implements ParkingVendorHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(Ketuo2ParkingVendorHandler.class);

	private static final String RECHARGE = "/api/pay/CardRecharge";
	private static final String GET_CARD = "/api/pay/GetCarCardInfo";
	private static final String GET_TYPES = "/api/pay/GetCarTypeList";
	private static final String GET_CARd_RULE = "/api/pay/GetCardRule";
	private static final String GET_TEMP_FEE = "/api/pay/GetParkingPaymentInfo";
	private static final String PAY_TEMP_FEE = "/api/pay/PayParkingFee";
	private static final String ADD_MONTH_CARD = "/api/card/AddMonthCarCardNo_KX";
	private static final String RULE_TYPE = "1"; //只显示ruleType = 1时的充值项
	//科兴的需求，充值过期的月卡时，需要计算费率，标记为自定义custom的费率，其他停车场不建议做这样的功能。
	private static final String EXPIRE_CUSTOM_RATE_TOKEN = "custom";
	//科托系统：按30天计算
	private static final int DAY_COUNT = 30;
	//月租车 : 2
	private static final String CAR_TYPE = "2";
	@Autowired
	private ParkingProvider parkingProvider;
	@Autowired
	private LocaleTemplateService localeTemplateService;
	@Autowired
    private ConfigurationProvider configProvider;
	@Autowired
	private FlowService flowService;
    @Autowired
	private FlowProvider flowProvider;
    @Autowired
    private FlowCaseProvider flowCaseProvider;
	@Autowired
    private DbProvider dbProvider;
    
	@Override
    public List<ParkingCardDTO> listParkingCardsByPlate(ParkingLot parkingLot, String plateNumber) {
        
    	List<ParkingCardDTO> resultList = new ArrayList<ParkingCardDTO>();

    	KetuoCard card = getCard(plateNumber);

		if(null != card){
			String expireDate = card.getValidTo();
			this.checkExpireDateIsNull(expireDate,plateNumber);

			long expireTime = strToLong(expireDate);

			if (checkExpireTime(parkingLot, expireTime)) {
				return resultList;
			}

			ParkingCardDTO parkingCardDTO = convertCardInfo(parkingLot);

			if (null != card.getName()) {
				String plateOwnerName = card.getName();
				parkingCardDTO.setPlateOwnerName(plateOwnerName);
			}

			parkingCardDTO.setPlateNumber(plateNumber);
			parkingCardDTO.setPlateOwnerPhone("");

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

			if(null != card.getFreeMoney() && card.getFreeMoney() != 0) {
				parkingCardDTO.setFreeAmount(card.getFreeMoney() / 100 + "元/月");
			}
			if (null != card.getIsAllow()) {
				parkingCardDTO.setIsSupportOnlinePaid(card.getIsAllow().byteValue());
			}

			resultList.add(parkingCardDTO);
		}
        
        return resultList;
    }

    @Override
    public List<ParkingRechargeRateDTO> getParkingRechargeRates(ParkingLot parkingLot,
    		String plateNumber, String cardNo) {
    	List<KetuoCardRate> list = new ArrayList<>();
		List<KetuoCardType> types = getCardType();

    	if(StringUtils.isBlank(plateNumber)) {
    		for(KetuoCardType k: types) {
				populateRateInfo(k.getCarType(), k.getTypeName(), list);

    		}
    	}else{
    		KetuoCard cardInfo = getCard(plateNumber);
    		if(null != cardInfo) {
    			long now = System.currentTimeMillis();
				long expireTime = strToLong(cardInfo.getValidTo());

				if(expireTime < now) {
					KetuoCardRate ketuoCardRate = getExpiredRate(cardInfo, parkingLot, now);

					if (null != ketuoCardRate) {
						list.add(ketuoCardRate);
					}
    			}else {
    				String carType = cardInfo.getCarType();
        			String typeName = null;
        			for(KetuoCardType kt: types) {
    					if(carType.equals(kt.getCarType())) {
    						typeName = kt.getTypeName();
    						break;
    					}
    				}
					populateRateInfo(carType, typeName, list);

    			}
    		}
    	}

		List<ParkingRechargeRateDTO> result = list.stream().map(r -> convertParkingRechargeRateDTO(parkingLot, r)).collect(Collectors.toList());
		
		return result;
    }

	private KetuoCardRate getExpiredRate(KetuoCard cardInfo, ParkingLot parkingLot, long now) {
		KetuoCardRate ketuoCardRate = null;

		if(parkingLot.getIsSupportRecharge() == ParkingSupportRechargeStatus.SUPPORT.getCode()) {

			Integer rechargeMonthCount = parkingLot.getRechargeMonthCount();
			Integer freeMoney = null != cardInfo.getFreeMoney() ? cardInfo.getFreeMoney() : 0;
			if(rechargeMonthCount <= 0) {
				LOGGER.error("ParkingLot rechargeMonthCount less than 0, parkingLot={}", parkingLot);
				throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.ERROR_PLATE_EXIST,
						"ParkingLot rechargeMonthCount less than 0");
			}
			List<KetuoCardRate> rates = getCardRule(cardInfo.getCarType());
			if(rates.size() != 0) {
				KetuoCardRate rate = null;
				for(KetuoCardRate kr: rates) {
					if(Integer.valueOf(kr.getRuleAmount()) == 1)
						rate = kr;
				}
				if (null != rate) {
					//默认查询一个月的费率，如果不存在，就返回null。
					ketuoCardRate = new KetuoCardRate();

					if(parkingLot.getRechargeType() == ParkingLotRechargeType.ALL.getCode()) {
						//实际价格减去优惠金额，因为是一个月的费率，直接减。
						Integer actualPrice = Integer.valueOf(rate.getRuleMoney()) - freeMoney;
						ketuoCardRate.setRuleMoney(String.valueOf(actualPrice * rechargeMonthCount));

					}else {
						Calendar calendar = Calendar.getInstance();
						calendar.setTimeInMillis(now);
						int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
						int today = calendar.get(Calendar.DAY_OF_MONTH);
						Integer actualPrice = Integer.valueOf(rate.getRuleMoney()) - freeMoney;
						ketuoCardRate.setRuleMoney(String.valueOf(actualPrice * (rechargeMonthCount - 1)
								+ actualPrice * (maxDay - today + 1) / DAY_COUNT ));

					}

					ketuoCardRate.setRuleId(EXPIRE_CUSTOM_RATE_TOKEN);
//    				rate.setCarType(carType);
					ketuoCardRate.setRuleAmount(String.valueOf(parkingLot.getRechargeMonthCount()));
//    				rate.setRuleName(ruleName);
//    				rate.setRuleType(ruleType);
//    				rate.setTypeName(typeName);
				}
			}
		}
		return ketuoCardRate;
	}

	private void populateRateInfo(String carType, String typeName, List<KetuoCardRate> result) {
		for(KetuoCardRate rate: getCardRule(carType)) {
			if(RULE_TYPE.equals(rate.getRuleType())) {
				rate.setCarType(carType);
				rate.setTypeName(typeName);
				result.add(rate);
			}
		}
	}

    private ParkingRechargeRateDTO convertParkingRechargeRateDTO(ParkingLot parkingLot, KetuoCardRate rate) {
		ParkingRechargeRateDTO dto = new ParkingRechargeRateDTO();
		dto.setOwnerId(parkingLot.getOwnerId());
		dto.setOwnerType(parkingLot.getOwnerType());
		dto.setParkingLotId(parkingLot.getId());
		dto.setRateToken(rate.getRuleId());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("count", rate.getRuleAmount());
		String scope = ParkingNotificationTemplateCode.SCOPE;
		int code = ParkingNotificationTemplateCode.DEFAULT_RATE_NAME;
		String locale = "zh_CN";
		String rateName = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
		dto.setRateName(rateName);
		dto.setCardType(rate.getTypeName());
		dto.setMonthCount(new BigDecimal(rate.getRuleAmount()));
		dto.setPrice(new BigDecimal(Integer.parseInt(rate.getRuleMoney()) / 100));
		dto.setVendorName(ParkingLotVendor.KETUO2.getCode());
		return dto;
	}

    @Override
    public Boolean notifyParkingRechargeOrderPayment(ParkingRechargeOrder order) {
    	return recharge(order);
    }
    
    @Override
    public ParkingRechargeRateDTO createParkingRechargeRate(CreateParkingRechargeRateCommand cmd){
    	LOGGER.error("Not support create parkingRechargeRate.");
		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_UNSUPPORTED_USAGE,
				"Not support create parkingRechargeRate.");
    }
    
    @Override
    public void deleteParkingRechargeRate(DeleteParkingRechargeRateCommand cmd){
    	LOGGER.error("Not support delete parkingRechargeRate.");
		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_UNSUPPORTED_USAGE,
				"Not support delete parkingRechargeRate.");
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
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			ts = sdf.parse(str).getTime();
		} catch (ParseException e) {
			LOGGER.error("data format is not yyyy-MM-dd HH:mm:ss, str={}", str);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"data format is not yyyy-MM-dd HH:mm:ss.");
		}
		return ts;
	}
	
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
			if(null != entity.getData()) {
				result = entity.getData();
			}
		}
		
		return result;
	}
	
	private List<KetuoCardRate> getCardRule(String carType) {
		List<KetuoCardRate> result = new ArrayList<>();
		JSONObject param = new JSONObject();
		param.put("carType", carType);
		String json = post(param, GET_CARd_RULE);
		
		KetuoJsonEntity<KetuoCardRate> entity = JSONObject.parseObject(json, new TypeReference<KetuoJsonEntity<KetuoCardRate>>(){});
		if(entity.isSuccess()){
			if(null != entity.getData()) {
				result = entity.getData();
			}
		}
		return result;
	}
	
	protected KetuoCard getCard(String plateNumber) {
		KetuoCard card = null;
		JSONObject param = new JSONObject();
		
		//科托月卡车没有 归属地区分
    	plateNumber = plateNumber.substring(1, plateNumber.length());
		param.put("plateNo", plateNumber);
		String json = post(param, GET_CARD);
        
		KetuoJsonEntity<KetuoCard> entity = JSONObject.parseObject(json, new TypeReference<KetuoJsonEntity<KetuoCard>>(){});
		if(entity.isSuccess()){
			List<KetuoCard> list = entity.getData();
			if(null != list && !list.isEmpty()) {
				card = list.get(0);
			}
		}
        
        return card;
    }
	
	private boolean rechargeMonthlyCard(ParkingRechargeOrder originalOrder){

		ParkingRechargeOrder tempOrder = ConvertHelper.convert(originalOrder, ParkingRechargeOrder.class);
		String plateNumber = tempOrder.getPlateNumber();

		KetuoCard card = getCard(plateNumber);

		if(null == card) {

			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			//充值记录的开始时间
			Timestamp tempStart = new Timestamp(calendar.getTimeInMillis());
			originalOrder.setStartPeriod(tempStart);

			KetuoCardRate ketuoCardRate = null;
			for(KetuoCardRate rate: getCardRule(CAR_TYPE)) {
				if(rate.getRuleId().equals(tempOrder.getRateToken())) {
					ketuoCardRate = rate;
				}
			}
			if(null == ketuoCardRate) {
				LOGGER.error("Rate not found, cmd={}", tempOrder);
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
						"Rate not found.");
			}

			Integer payMoney = tempOrder.getPrice().intValue()*100 - Integer.parseInt(ketuoCardRate.getRuleMoney())
					* (tempOrder.getMonthCount().intValue() - 1);

			if(addMonthCard(tempOrder.getPlateNumber(), payMoney)) {
				Integer count = tempOrder.getMonthCount().intValue();
				
		    	LOGGER.debug("Parking addMonthCard,count={}", count);

				if(count > 1) {
					tempOrder.setMonthCount(new BigDecimal(count -1) );
					tempOrder.setPrice(new BigDecimal((tempOrder.getPrice().intValue() * 100 - payMoney) / 100));
					if(actualRechargeMonthlyCard(originalOrder, tempOrder)) {
						updateFlowStatus(tempOrder);
			    		return true;
					}
				}else {
					updateFlowStatus(tempOrder);
				}
				return true;
			}
			return false;
		}else {
			return actualRechargeMonthlyCard(originalOrder, tempOrder);
		}
		
    }

    private boolean actualRechargeMonthlyCard(ParkingRechargeOrder originalOrder, ParkingRechargeOrder tempOrder) {

		JSONObject param = new JSONObject();
		String plateNumber = tempOrder.getPlateNumber();

		KetuoCard card = getCard(plateNumber);
		String oldValidEnd = card.getValidTo();
		Long expireTime = strToLong(oldValidEnd);
		long now = System.currentTimeMillis();
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		if(expireTime < now) {
			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
			String date = sdf2.format(new Date(now)) + " 00:00:00";
			try {
				expireTime = sdf1.parse(date).getTime();
			} catch (ParseException e) {
				LOGGER.info("date={}, time={}", date, expireTime, e);
			}
		}

		Timestamp tempStart = Utils.addSeconds(expireTime, 1);
		Timestamp tempEnd = Utils.getTimestampByAddNatureMonth(expireTime, tempOrder.getMonthCount().intValue());
		String validStart = sdf1.format(tempStart);
		String validEnd = sdf1.format(tempEnd);

		param.put("cardId", card.getCardId());
		param.put("ruleType", RULE_TYPE);
		param.put("ruleAmount", String.valueOf(tempOrder.getMonthCount().intValue()));
		param.put("payMoney", tempOrder.getPrice().intValue()*100);
		param.put("startTime", validStart);
		param.put("endTime", validEnd);
		param.put("freeMoney", card.getFreeMoney() * tempOrder.getMonthCount().intValue());

		if(EXPIRE_CUSTOM_RATE_TOKEN.equals(tempOrder.getRateToken())) {
			ParkingLot parkingLot = parkingProvider.findParkingLotById(tempOrder.getParkingLotId());
			if(parkingLot.getRechargeType() == ParkingLotRechargeType.ACTUAL.getCode()) {
				Calendar calendar = Calendar.getInstance();
				calendar.setTimeInMillis(now);
				int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
				int today = calendar.get(Calendar.DAY_OF_MONTH);
				Integer actualPrice = card.getFreeMoney();
				Integer monthCount = tempOrder.getMonthCount().intValue();
				param.put("freeMoney", actualPrice * (monthCount - 1)
						+ actualPrice * (maxDay - today + 1) / DAY_COUNT);
			}
		}
		String json = post(param, RECHARGE);

		//将充值信息存入订单
		originalOrder.setErrorDescriptionJson(json);
		if (null == originalOrder.getStartPeriod()) {
			originalOrder.setStartPeriod(tempStart);
		}
		originalOrder.setEndPeriod(tempEnd);

		JSONObject jsonObject = JSONObject.parseObject(json);
		Object obj = jsonObject.get("resCode");
		if(null != obj ) {
			int resCode = (int) obj;
			if(resCode == 0) {
				return true;
			}
		}
		return false;
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
        
        JSONObject jsonObject = JSONObject.parseObject(json);
		Object obj = jsonObject.get("resCode");
		if(null != obj ) {
			int resCode = (int) obj;
			if(resCode == 0) {
				return true;
			}
		}
		return false;
    }
	
	private boolean payTempCardFee(ParkingRechargeOrder order){

		JSONObject param = new JSONObject();
		param.put("orderNo", order.getOrderToken());
		param.put("amount", order.getPrice().intValue() * 100);
	    param.put("discount", 0);
	    param.put("payType", VendorType.WEI_XIN.getCode().equals(order.getPaidType())?4:5);
		String json = post(param, PAY_TEMP_FEE);

        JSONObject jsonObject = JSONObject.parseObject(json);
		Object obj = jsonObject.get("resCode");
		if(null != obj ) {
			int resCode = (int) obj;
			if(resCode == 0) {
				return true;
			}
		}
		return false;
    }

	@Override
	public boolean recharge(ParkingRechargeOrder order){
		if(order.getRechargeType().equals(ParkingRechargeType.MONTHLY.getCode()))
			return rechargeMonthlyCard(order);
		return payTempCardFee(order);
    }
	
	public String post(JSONObject param, String type) {

		KetuoRequestConfig config = getKetuoRequestConfig();
		String url = config.getUrl() + type;
		String key = config.getKey();
		String user = config.getUser();
		String pwd = config.getPwd();

		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");

		String iv = sdf2.format(new Date());
        String data = null;
		try {
			data = EncryptUtil.getEncString(param, key, iv);
		} catch (Exception e) {
			LOGGER.error("Parking encrypt param error, param={}, key={}, iv={}", param, key, iv, e);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
    				"Parking encrypt param error.");
		}

		Map<String, String> params = new HashMap<>();
		params.put("data", data);
		Map<String, String> headers = new HashMap<>();
		headers.put("user", user);
		headers.put("pwd", pwd);

		LOGGER.info("Parking info, namespace={}", this.getClass().getName());
		return Utils.post(url, params, headers);
	}

	@Override
	public void updateParkingRechargeOrderRate(ParkingRechargeOrder order) {
		String plateNumber = order.getPlateNumber();
		if(EXPIRE_CUSTOM_RATE_TOKEN.equals(order.getRateToken())) {
			order.setRateName(EXPIRE_CUSTOM_RATE_TOKEN);

		}else {
			KetuoCard cardInfo = getCard(plateNumber);
			KetuoCardRate ketuoCardRate = null;
			String cardType = null;
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
	public ParkingTempFeeDTO getParkingTempFee(ParkingLot parkingLot, String plateNumber) {
		KetuoTempFee tempFee = getTempFee(plateNumber);
		
		ParkingTempFeeDTO dto = new ParkingTempFeeDTO();
		if(null == tempFee) {
			return dto;
		}
		dto.setPlateNumber(plateNumber);
		dto.setEntryTime(strToLong(tempFee.getEntryTime()));
		dto.setPayTime(strToLong(tempFee.getPayTime()));
		dto.setParkingTime(tempFee.getElapsedTime());
		dto.setDelayTime(tempFee.getDelayTime());
		dto.setPrice(new BigDecimal(2));
//		dto.setPrice(new BigDecimal(tempFee.getPayable() / 100));

		dto.setOrderToken(tempFee.getOrderNo());
		return dto;
	}

	@Override
	public OpenCardInfoDTO getOpenCardInfo(GetOpenCardInfoCommand cmd) {

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

		//月租车
		List<KetuoCardRate> rates = getCardRule(CAR_TYPE);
		if(null != rates && !rates.isEmpty()) {
			
			KetuoCardRate rate = null;
			for(KetuoCardRate r: rates) {
				if(Integer.valueOf(r.getRuleAmount()) == 1) {
					rate = r;
					break;
				}
			}

			if (null == rate) {
				//TODO:
				return null;
			}

			List<KetuoCardType> types = getCardType();
			String typeName = null;
			for(KetuoCardType kt: types) {
				if(CAR_TYPE.equals(kt.getCarType())) {
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
			dto.setRateName(rateName);
			dto.setCardType(typeName);
			dto.setMonthCount(new BigDecimal(rate.getRuleAmount()));
			dto.setPrice(new BigDecimal(Integer.parseInt(rate.getRuleMoney()) / 100));

			dto.setPlateNumber(cmd.getPlateNumber());
			long now = System.currentTimeMillis();
			dto.setOpenDate(now);
			dto.setExpireDate(Utils.getLongByAddNatureMonth(now, requestMonthCount));
			if(requestRechargeType == ParkingLotRechargeType.ALL.getCode()) {
				dto.setPayMoney(dto.getPrice().multiply(new BigDecimal(requestMonthCount)));
			}else {
				Calendar calendar = Calendar.getInstance();
				calendar.setTimeInMillis(now);
				int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
				int today = calendar.get(Calendar.DAY_OF_MONTH);
				//科兴要求：按30天计算
				BigDecimal price = dto.getPrice().multiply(new BigDecimal(requestMonthCount-1))
						.add(dto.getPrice().multiply(new BigDecimal(maxDay-today+1))
								.divide(new BigDecimal(DAY_COUNT), RoundingMode.HALF_EVEN));
				dto.setPayMoney(price);
			}
		}

		return dto;
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

	protected KetuoRequestConfig getKetuoRequestConfig() {
		return null;
	}
}
