// @formatter:off
package com.everhomes.parking;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
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
import com.everhomes.locale.LocaleStringService;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.order.OrderUtil;
import com.everhomes.parking.wanke.WankeCardInfo;
import com.everhomes.parking.wanke.WankeCardType;
import com.everhomes.parking.wanke.WankeJsonEntity;
import com.everhomes.parking.wanke.WankeTemoFee;
import com.everhomes.rest.organization.VendorType;
import com.everhomes.rest.parking.CreateParkingRechargeRateCommand;
import com.everhomes.rest.parking.DeleteParkingRechargeRateCommand;
import com.everhomes.rest.parking.GetOpenCardInfoCommand;
import com.everhomes.rest.parking.ListCardTypeCommand;
import com.everhomes.rest.parking.ListCardTypeResponse;
import com.everhomes.rest.parking.OpenCardInfoDTO;
import com.everhomes.rest.parking.ParkingCardDTO;
import com.everhomes.rest.parking.ParkingCardType;
import com.everhomes.rest.parking.ParkingLotVendor;
import com.everhomes.rest.parking.ParkingOwnerType;
import com.everhomes.rest.parking.ParkingRechargeOrderRechargeStatus;
import com.everhomes.rest.parking.ParkingRechargeRateDTO;
import com.everhomes.rest.parking.ParkingRechargeType;
import com.everhomes.rest.parking.ParkingSupportRechargeStatus;
import com.everhomes.rest.parking.ParkingTempFeeDTO;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;

@Component(ParkingVendorHandler.PARKING_VENDOR_PREFIX + "WANKE")
public class WankeParkingVendorHandler implements ParkingVendorHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(WankeParkingVendorHandler.class);
	
	private SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmss");
	private SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
	
	private static final String RECHARGE = "/Parking/MouthCardRecharge";
	private static final String GET_CARD = "/Parking/CardDataQuery";
	private static final String GET_TYPES = "/Parking/GetMonthCardList";
	private static final String GET_TEMP_FEE = "/Parking/GetCost";
	private static final String PAY_TEMP_FEE = "/Parking/PayCost";
	
	private CloseableHttpClient httpclient = null;
	
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
	
	@PostConstruct
	public void init() {
		httpclient = HttpClients.createDefault();
	}
	
	@Override
    public List<ParkingCardDTO> getParkingCardsByPlate(String ownerType, Long ownerId,
    		Long parkingLotId, String plateNumber) {
        
    	List<ParkingCardDTO> resultList = new ArrayList<ParkingCardDTO>();
    	
    	WankeCardInfo card = getCard(plateNumber);

        ParkingCardDTO parkingCardDTO = new ParkingCardDTO();
		if(null != card){
			String expireDate = card.getExpireDate();
			this.checkExpireDateIsNull(expireDate,plateNumber);

			long expireTime = strToLong2(expireDate);
			long now = System.currentTimeMillis();
			long cardReserveTime = 0;
			
	    	ParkingLot parkingLot = parkingProvider.findParkingLotById(parkingLotId);
	    	Byte isSupportRecharge = parkingLot.getIsSupportRecharge();
	    	if(ParkingSupportRechargeStatus.SUPPORT.getCode() == isSupportRecharge)	{
	    		Integer cardReserveDay = parkingLot.getCardReserveDays();
	    		cardReserveTime = cardReserveDay * 24 * 60 * 60 * 1000L;

	    	}
			
			if(expireTime + cardReserveTime < now){
				return resultList;
			}
			parkingCardDTO.setOwnerType(ParkingOwnerType.COMMUNITY.getCode());
			parkingCardDTO.setOwnerId(ownerId);
			parkingCardDTO.setParkingLotId(parkingLotId);
			
			String plateOwnerName = card.getUserName();
			String plateOwnerPhone = card.getMobile();
			parkingCardDTO.setPlateOwnerName(plateOwnerName);
			parkingCardDTO.setPlateNumber(plateNumber);
			parkingCardDTO.setPlateOwnerPhone(plateOwnerPhone);

			parkingCardDTO.setEndTime(expireTime);
			
			parkingCardDTO.setCardType(card.getCardType());
			parkingCardDTO.setCardNumber(card.getCardNo());
			parkingCardDTO.setIsValid(true);
			
			resultList.add(parkingCardDTO);
		}
        
        return resultList;
    }

	@Override
    public List<ParkingRechargeRateDTO> getParkingRechargeRates(String ownerType, Long ownerId, Long parkingLotId,String plateNumber,String cardNo) {
    	List<ParkingRechargeRate> parkingRechargeRateList = null;
    	List<ParkingRechargeRateDTO> result = null;
    	if(StringUtils.isBlank(plateNumber)) {
    		parkingRechargeRateList = parkingProvider.listParkingRechargeRates(ownerType, ownerId, parkingLotId,null);
    		
    	}else{
    		WankeCardInfo cardInfo = getCard(plateNumber);           
			
    		String cardType = cardInfo.getCardType();
    		parkingRechargeRateList = parkingProvider.listParkingRechargeRates(ownerType, ownerId, parkingLotId,cardType);
    	}
		result = parkingRechargeRateList.stream().map(r->{
			ParkingRechargeRateDTO dto = new ParkingRechargeRateDTO();
			dto = ConvertHelper.convert(r, ParkingRechargeRateDTO.class);
			dto.setRateToken(r.getId().toString());
			dto.setVendorName(ParkingLotVendor.WANKE.getCode());
			return dto;
		}
		).collect(Collectors.toList());
		
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
    
//    private long strToLong(String str) {
//
//		long ts;
//		try {
//			ts = sdf1.parse(str).getTime();
//		} catch (ParseException e) {
//			LOGGER.error("data format is not yyyyMMddHHmmss, str={}", str);
//			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
//					"data format is not yyyyMMddHHmmss.");
//		}
//		return ts;
//	}
    
    private Long strToLong2(String str) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		
		Long ts = null;
		try {
			ts = sdf.parse(str).getTime();
		} catch (ParseException e) {
			LOGGER.error("validityPeriod data format is not yyyymmdd.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"validityPeriod data format is not yyyymmdd.");
		}
		
		return ts;
	}
    
    private Long strToLong3(String str) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		Long ts = null;
		try {
			ts = sdf.parse(str).getTime();
		} catch (ParseException e) {
			LOGGER.error("validityPeriod data format is not yyyymmdd.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"validityPeriod data format is not yyyymmdd.");
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

		List<WankeCardType> list = getCardType();
		for(WankeCardType t: list) {
			
			ParkingCardType parkingCardType = new ParkingCardType();
			parkingCardType.setTypeId(t.getId());
			parkingCardType.setTypeName(t.getName());
			resultTypes.add(parkingCardType);
		}
		ret.setCardTypes(resultTypes);	
    	return ret;
    }
	
	private List<WankeCardType> getCardType() {
		
		List<WankeCardType> result = new ArrayList<>();
		JSONObject param = new JSONObject();
		String json = postToWanke(param, GET_TYPES);
		
		WankeJsonEntity<List<WankeCardType>> entity = JSONObject.parseObject(json, new TypeReference<WankeJsonEntity<List<WankeCardType>>>(){});

		if(entity.isSuccess()){
			if(null != entity.getData())
				result = entity.getData();
		}
		
		return result;
	}
	
	private WankeCardInfo getCard(String plateNumber) {
		WankeCardInfo card = null;
		JSONObject param = new JSONObject();
		
		param.put("plateNo", plateNumber);
		param.put("flag", "2");
		String json = postToWanke(param, GET_CARD);
        
        if(LOGGER.isDebugEnabled())
			LOGGER.debug("Result={}, param={}", json, param);
        
        WankeJsonEntity<WankeCardInfo> entity = JSONObject.parseObject(json, new TypeReference<WankeJsonEntity<WankeCardInfo>>(){});
		if(entity.isSuccess()){
			card = entity.getData();
			if(null != card)
				card.setExpireDate(card.getExpireDate()+"235959");
		}
        
        return card;
    }
	
	private boolean rechargeMonthlyCard(ParkingRechargeOrder order){

		JSONObject param = new JSONObject();

		param.put("plateNo", order.getPlateNumber());
		param.put("flag", "2");
	    param.put("amount", order.getPrice().intValue()*100);
	    param.put("payMons", String.valueOf(order.getMonthCount().intValue()));
	    param.put("chargePaidNo", order.getId());
	    param.put("payTime", sdf1.format(new Date()));
	    param.put("sign", "");
	    
		String json = postToWanke(param, RECHARGE);
        
        if(LOGGER.isDebugEnabled())
			LOGGER.debug("Result={}, param={}", json, param);
        
        WankeJsonEntity<Object> entity = JSONObject.parseObject(json, new TypeReference<WankeJsonEntity<Object>>(){});
		return entity.isSuccess();
		
//		JSONObject jsonObject = JSONObject.parseObject(json);
//		Object obj = jsonObject.get("errorCode");
//		if(null != obj ) {
//			int resCode = (int) obj;
//			if(resCode == 0)
//				return true;
//			
//		}
//		return false;
    }
	
	private boolean payTempCardFee(ParkingRechargeOrder order){

		JSONObject param = new JSONObject();

		param.put("orderNo", order.getOrderToken());
		param.put("amount", order.getPrice().intValue() * 100);
	    param.put("payType", VendorType.WEI_XIN.getCode().equals(order.getPaidType())?1:2);
		String json = postToWanke(param, PAY_TEMP_FEE);
        
        if(LOGGER.isDebugEnabled())
			LOGGER.debug("Result={}, param={}", json, param);
        
        WankeJsonEntity<Object> entity = JSONObject.parseObject(json, new TypeReference<WankeJsonEntity<Object>>(){});
		return entity.isSuccess();
		
//        JSONObject jsonObject = JSONObject.parseObject(json);
//		Object obj = jsonObject.get("errorCode");
//		if(null != obj ) {
//			int resCode = (Integer) obj;
//			if(resCode == 0)
//				return true;
//			
//		}
//		return false;
    }

	private boolean recharge(ParkingRechargeOrder order){
		if(order.getRechargeType().equals(ParkingRechargeType.MONTHLY.getCode()))
			return rechargeMonthlyCard(order);
		return payTempCardFee(order);
    }
	
	public String postToWanke(JSONObject param, String type) {
		HttpPost httpPost = new HttpPost("http://122.224.250.35:7021" + type);
		CloseableHttpClient httpclient = HttpClients.createDefault();
		String json = null;
		
		CloseableHttpResponse response = null;
		
		try {
			StringEntity stringEntity = new StringEntity(param.toString(), "utf8");
			httpPost.setEntity(stringEntity);
			response = httpclient.execute(httpPost);
			
			int status = response.getStatusLine().getStatusCode();
			
			if(LOGGER.isDebugEnabled())
				LOGGER.debug("Data from wanke, status={}, param={}", status, param);
			if(status == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();
				
				if (entity != null) {
					json = EntityUtils.toString(entity);
				}
			}
			
		} catch (IOException e) {
			LOGGER.error("Parking request error, param={}", param, e);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
    				"Parking request error.");
		}finally {
            try {
				response.close();
			} catch (IOException e) {
				LOGGER.error("Parking close instream, response error, param={}", param, e);
			}
        }
		if(LOGGER.isDebugEnabled())
			LOGGER.debug("Data from wanke, json={}", json);
		
		return json;
	}
	
	@Override
	public void updateParkingRechargeOrderRate(ParkingRechargeOrder order) {
		ParkingRechargeRate rate = parkingProvider.findParkingRechargeRatesById(Long.parseLong(order.getRateToken()));
		if(null == rate) {
			LOGGER.error("Rate not found, cmd={}", order);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"Rate not found.");
		}
		order.setRateName(rate.getRateName());
		
	}

	private WankeTemoFee getTempFee(String plateNumber) {
		WankeTemoFee tempFee = null;
		JSONObject param = new JSONObject();
		param.put("plateNo", plateNumber);
		
		String json = postToWanke(param, GET_TEMP_FEE);
        
        if(LOGGER.isDebugEnabled())
			LOGGER.debug("Result={}, param={}", json, param);
        
        WankeJsonEntity<WankeTemoFee> entity = JSONObject.parseObject(json, new TypeReference<WankeJsonEntity<WankeTemoFee>>(){});
        
		if(entity.isSuccess()){
			tempFee = entity.getData();
		}
        
        return tempFee;
    }

	@Override
	public ParkingTempFeeDTO getParkingTempFee(String ownerType, Long ownerId, Long parkingLotId, String plateNumber) {
		
		WankeTemoFee tempFee = getTempFee(plateNumber);
		
		ParkingTempFeeDTO dto = new ParkingTempFeeDTO();
		if(null == tempFee)
			return dto;
		dto.setPlateNumber(plateNumber);
		dto.setEntryTime(strToLong3(tempFee.getEntryTime()));
		dto.setParkingTime(Integer.valueOf(tempFee.getParkingTime()));
		dto.setDelayTime(Integer.valueOf(tempFee.getDelayTime()));
		dto.setPrice(new BigDecimal(Integer.valueOf(tempFee.getAmount()) / 100));
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
	
}
