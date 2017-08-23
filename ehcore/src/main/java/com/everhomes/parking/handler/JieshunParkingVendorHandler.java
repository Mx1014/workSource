// @formatter:off
package com.everhomes.parking.handler;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.everhomes.bigcollection.BigCollectionProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.db.DbProvider;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.order.OrderUtil;
import com.everhomes.parking.*;
import com.everhomes.parking.ketuo.*;
import com.everhomes.rest.organization.VendorType;
import com.everhomes.rest.parking.*;
import com.everhomes.user.UserProvider;
import com.everhomes.util.RuntimeErrorException;
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
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Component(ParkingVendorHandler.PARKING_VENDOR_PREFIX + "JIESHUN")
public class JieshunParkingVendorHandler extends AbstractCommonParkingVendorHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(JieshunParkingVendorHandler.class);
	
	private SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
	
	private static final String RECHARGE = "/api/pay/CardRecharge";
	private static final String GET_CARD = "/api/pay/GetCarCardInfo";
	private static final String GET_TYPES = "/api/pay/GetCarTypeList";
	private static final String GET_CARd_RULE = "/api/pay/GetCardRule";
	private static final String GET_TEMP_FEE = "/api/pay/GetParkingPaymentInfo";
	private static final String PAY_TEMP_FEE = "/api/pay/PayParkingFee";
	private static final String RULE_TYPE = "1"; //只显示ruleType = 1时的充值项
	
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
    private BigCollectionProvider bigCollectionProvider;
	@Autowired
    private DbProvider dbProvider;
	
	@Override
    public List<ParkingCardDTO> listParkingCardsByPlate(ParkingLot parkingLot, String plateNumber) {
        
    	List<ParkingCardDTO> resultList = new ArrayList<ParkingCardDTO>();

    	KetuoCard card = getCard(plateNumber);

        ParkingCardDTO parkingCardDTO = new ParkingCardDTO();
		if(null != card){
			String expireDate = card.getValidTo();
			this.checkExpireDateIsNull(expireDate,plateNumber);

			long expireTime = strToLong(expireDate);

			if (checkExpireTime(parkingLot, expireTime)) {
				return resultList;
			}
			parkingCardDTO.setOwnerType(parkingLot.getOwnerType());
			parkingCardDTO.setOwnerId(parkingLot.getOwnerId());
			parkingCardDTO.setParkingLotId(parkingLot.getId());
			
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
			parkingCardDTO.setIsValid(true);
			
			resultList.add(parkingCardDTO);
		}
        
        return resultList;
    }

    @Override
    public List<ParkingRechargeRateDTO> getParkingRechargeRates(ParkingLot parkingLot,String plateNumber,String cardNo) {
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
    	result = list.stream().map( r -> {
			ParkingRechargeRateDTO dto = new ParkingRechargeRateDTO();
			dto.setOwnerId(parkingLot.getOwnerId());
			dto.setOwnerType(parkingLot.getOwnerType());
			dto.setParkingLotId(parkingLot.getId());
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
			dto.setVendorName(ParkingLotVendor.KETUO.getCode());
			return dto;
		}).collect(Collectors.toList());
		
		return result;
    }

    final StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

    @Override
    public Boolean notifyParkingRechargeOrderPayment(ParkingRechargeOrder order) {

    	return recharge(order);
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
			ts = sdf1.parse(str).getTime();
		} catch (ParseException e) {
			LOGGER.error("data format is not yyyyMMddHHmmss, str={}", str);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"data format is not yyyyMMddHHmmss.");
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
	
	private boolean rechargeMonthlyCard(ParkingRechargeOrder order){

		JSONObject param = new JSONObject();
		//储能月卡车没有 归属地区分
		String plateNumber = order.getPlateNumber();

		KetuoCard card = getCard(plateNumber);
		String oldValidEnd = card.getValidTo();
		Long time = strToLong(oldValidEnd);
		String validStart = sdf1.format(Utils.addSeconds(time, 1));
		String validEnd = sdf1.format(Utils.getLongByAddNatureMonth(time, order.getMonthCount().intValue()));
		
		param.put("cardId", Integer.parseInt(order.getCardNumber()));
		//修改科托ruleType 固定为1 表示月卡车
		param.put("ruleType", RULE_TYPE);
	    param.put("ruleAmount", String.valueOf(order.getMonthCount().intValue()));
	    param.put("payMoney", order.getPrice().intValue()*100);
	    param.put("startTime", validStart);
	    param.put("endTime", validEnd);
		String json = post(param, RECHARGE);
        
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
	@Override
	public boolean recharge(ParkingRechargeOrder order){
		if(order.getRechargeType().equals(ParkingRechargeType.MONTHLY.getCode()))
			return rechargeMonthlyCard(order);
		return payTempCardFee(order);
    }
	
	public String post(JSONObject param, String type) {
		HttpPost httpPost = new HttpPost(configProvider.getValue("parking.chuneng.url", "") + type);
		StringBuilder result = new StringBuilder();
		
        String key = configProvider.getValue("parking.chuneng.key", "");
        String iv = sdf2.format(new Date());
        String user = configProvider.getValue("parking.chuneng.user", "");
        String pwd = configProvider.getValue("parking.chuneng.pwd", "");
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
            	instream.close();
				response.close();
			} catch (IOException e) {
				LOGGER.error("Parking close instream, response error, param={}, key={}, iv={}", param, key, iv, e);
			}
        }
		String json = result.toString();
		if(LOGGER.isDebugEnabled())
			LOGGER.debug("Data from ketuo, json={}", json);
		
		return json;
	}

	@Override
	public void updateParkingRechargeOrderRate(ParkingRechargeOrder order) {
		//储能月卡车没有 归属地区分
		String plateNumber = order.getPlateNumber();

		KetuoCard cardInfo = getCard(plateNumber);
		KetuoCardRate ketuoCardRate = null;
		if(null != cardInfo) {
			String cardType = cardInfo.getCarType();
			for(KetuoCardRate rate: getCardRule(cardType)) {
				if(rate.getRuleId().equals(order.getRateToken())) {
					ketuoCardRate = rate;
				}
			}
		}
		if(null == ketuoCardRate) {
			LOGGER.error("Rate not found, cmd={}", order);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"Rate not found.");
		}
		order.setRateName(ketuoCardRate.getRuleName());
		
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
	public ParkingTempFeeDTO getParkingTempFee(ParkingLot parkingLot, String plateNumber) {
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
		// TODO Auto-generated method stub
		return null;
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
