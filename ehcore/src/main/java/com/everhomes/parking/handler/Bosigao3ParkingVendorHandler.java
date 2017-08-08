// @formatter:off
package com.everhomes.parking.handler;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import com.alibaba.fastjson.TypeReference;
import com.everhomes.parking.*;
import com.everhomes.parking.bosigao.*;
import com.everhomes.rest.organization.VendorType;
import com.everhomes.rest.parking.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;

/**
 * 科技园停车
 */
// "BOSIGAO"需与ParkingLotVendor.BOSIGAO的枚举值保持一致
@Component(ParkingVendorHandler.PARKING_VENDOR_PREFIX + "BOSIGAO3")
public class Bosigao3ParkingVendorHandler implements ParkingVendorHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(Bosigao3ParkingVendorHandler.class);

//	private String ParkingID = "6e517beb-c295-4837-99ed-a73201157e2e";
//	private String CompanyID = "175c8e26-ea36-4993-b113-a7320114e370";

	@Autowired
	private ParkingProvider parkingProvider;
	
	@Autowired
	private LocaleTemplateService localeTemplateService;
	
	@Autowired
    private ConfigurationProvider configProvider;
	
	@Override
    public GetParkingCardsResponse getParkingCardsByPlate(String ownerType, Long ownerId,
    		Long parkingLotId, String plateNumber) {
        
    	List<ParkingCardDTO> resultList = new ArrayList<>();
		GetParkingCardsResponse response = new GetParkingCardsResponse();
		response.setCards(resultList);

    	BosigaoCardInfo card = getCardInfo(plateNumber);
    	
        ParkingCardDTO parkingCardDTO = new ParkingCardDTO();
		if(null != card){
			//格式yyyyMMddHHmmss
			String validEnd = card.getLimitEnd();
			Long endTime = strToLong2(validEnd);
			long now = System.currentTimeMillis();
			long cardReserveTime = 0;
			
	    	ParkingLot parkingLot = parkingProvider.findParkingLotById(parkingLotId);
	    	Byte isSupportRecharge = parkingLot.getIsSupportRecharge();
	    	if(ParkingSupportRechargeStatus.SUPPORT.getCode() == isSupportRecharge)	{
	    		Integer cardReserveDay = parkingLot.getCardReserveDays();
	    		cardReserveTime = cardReserveDay * 24 * 60 * 60 * 1000L;

	    	}
			
			if(endTime + cardReserveTime < now){
				response.setToastType(ParkingToastType.CARD_EXPIRED.getCode());
				return response;
			}
			
			String plateOwnerName = card.getUserName();

			String cardNumber = card.getCardID();
			String cardType = card.getOldCardTypeName();
			
			parkingCardDTO.setOwnerType(ParkingOwnerType.COMMUNITY.getCode());
			parkingCardDTO.setOwnerId(ownerId);
			parkingCardDTO.setParkingLotId(parkingLotId);
			
			parkingCardDTO.setPlateOwnerName(plateOwnerName);
			parkingCardDTO.setPlateNumber(card.getPlateNumber());
			parkingCardDTO.setEndTime(endTime);
			parkingCardDTO.setCardType(cardType);
			parkingCardDTO.setCardNumber(cardNumber);
			parkingCardDTO.setIsValid(true);
			
			resultList.add(parkingCardDTO);
		}else{
			response.setToastType(ParkingToastType.NOT_CARD_USER.getCode());
		}
        return response;
    }

    private BosigaoCardInfo getCardInfo(String plateNumber){
		String url = configProvider.getValue("parking.techpark.url", "");
		String companyId = configProvider.getValue("parking.techpark.companyId", "");

		JSONObject jsonParam = new JSONObject();
		jsonParam.put("CompanyID", companyId);
		jsonParam.put("listPlateNumber", new String[]{plateNumber});

		Map<String, String> params = new HashMap<>();
		params.put("data", jsonParam.toString());
        String json = Utils.post(url + "OISGetAccountCardCarZL", params);

		BosigaoJsonEntity<List<BosigaoCardInfo>> entity = JSONObject.parseObject(json, new TypeReference<BosigaoJsonEntity<List<BosigaoCardInfo>>>(){});

        BosigaoCardInfo card = null;
        
        if(entity.isSuccess()){
			List<BosigaoCardInfo> cards = entity.getData();
			if (null != cards && cards.size() != 0) {
				BosigaoCardInfo tempCard = cards.get(0);
				//卡状态 1：正常 2：挂失 3：停用 4：注销
				if (1 == tempCard.getState()) {
					card = tempCard;
				}
			}
        }
    	return card;
    }

    @Override
	public boolean recharge(ParkingRechargeOrder order){
		if(order.getRechargeType().equals(ParkingRechargeType.MONTHLY.getCode()))
			return rechargeMonthlyCard(order);
		return payTempCardFee(order);
	}

	private boolean rechargeMonthlyCard(ParkingRechargeOrder order){

		BosigaoCardInfo card = getCardInfo(order.getPlateNumber());

		String url = configProvider.getValue("parking.techpark.url", "");
		String cost = String.valueOf((order.getPrice().intValue() * 100));

		JSONObject jsonParam = new JSONObject();

		jsonParam.put("CardID", card.getCardID());
		jsonParam.put("ParkingID", card.getParkingID());
		jsonParam.put("MonthNum", String.valueOf(order.getMonthCount().intValue()));
		jsonParam.put("Amount", cost);
		jsonParam.put("PayWay", "10001".equals(order.getPaidType()) ? "3" : "2");
		jsonParam.put("OnlineOrderID", order.getId());
		jsonParam.put("PayDate", timestampToStr2(System.currentTimeMillis()));

		Map<String, String> params = new HashMap<>();
		params.put("data", jsonParam.toString());
		String json = Utils.post(url + "OISMonthlyRenewals", params);

		BosigaoJsonEntity<Object> entity = JSONObject.parseObject(json, new TypeReference<BosigaoJsonEntity<Object>>(){});

		Long startPeriod = strToLong2(card.getLimitEnd());

		//将充值信息存入订单
		order.setErrorDescriptionJson(json);
		//加一秒
		order.setStartPeriod(new Timestamp(startPeriod + 1000));
		order.setEndPeriod(Utils.getTimestampByAddNatureMonth(startPeriod, order.getMonthCount().intValue()));

		if(entity.isSuccess()) {
			JSONObject obj = (JSONObject) entity.getData();
			Integer result = obj.getInteger("Result");
			if (0 == result) {
				return true;
			}
		}

		return false;
	}

	private BosigaoTempFee getTempFee(String plateNumber) {
		BosigaoTempFee tempFee = null;


		String url = configProvider.getValue("parking.techpark.url", "");
		String companyId = configProvider.getValue("parking.techpark.companyId", "");

		JSONObject jsonParam = new JSONObject();
		jsonParam.put("PlateNumber", plateNumber);
		jsonParam.put("CompanyID", companyId);
		long now = System.currentTimeMillis();
		String calculatDate = timestampToStr2(now);
		jsonParam.put("CalculatDate", calculatDate);

		Map<String, String> params = new HashMap<>();
		params.put("data", jsonParam.toString());
		String json = Utils.post(url + "OISCalculatingTempCost", params);

		JSONObject entity = JSONObject.parseObject(json);
		if("0".equals(entity.get("result"))){
			String dataJson = entity.getString("data");
			tempFee = JSONObject.parseObject(dataJson, BosigaoTempFee.class);
			if(null != tempFee) {
				tempFee.setPayTime(now);
			}
		}

		return tempFee;
	}

	private boolean payTempCardFee(ParkingRechargeOrder order){

		String parkingId = configProvider.getValue("parking.techpark.parkingId", "");
		if (verifyParkingCar(order.getPlateNumber(), parkingId)) {
			String url = configProvider.getValue("parking.techpark.url", "");
			String cost = String.valueOf((order.getPrice().intValue() * 100));

			JSONObject jsonParam = new JSONObject();
			jsonParam.put("OrderID", order.getOrderToken());
			jsonParam.put("PayWay", VendorType.ZHI_FU_BAO.getCode().equals(order.getPaidType()) ? "3" : "2");
			jsonParam.put("Amount", cost);
			jsonParam.put("ParkingID", parkingId);
			jsonParam.put("OnlineOrderID", order.getId());
			jsonParam.put("PayDate", timestampToStr2(System.currentTimeMillis()));

			Map<String, String> params = new HashMap<>();
			params.put("data", jsonParam.toString());
			String json = Utils.post(url + "OISTempPayAmount", params);

			BosigaoJsonEntity<Object> entity = JSONObject.parseObject(json, new TypeReference<BosigaoJsonEntity<Object>>(){});

			if(entity.isSuccess()) {
				JSONObject obj = (JSONObject) entity.getData();
				Integer result = obj.getInteger("Result");
				if (0 == result) {
					return true;
				}
			}
		}

		return false;
	}

	public ListCardTypeResponse listCardType(ListCardTypeCommand cmd) {
    	ListCardTypeResponse ret = new ListCardTypeResponse();

		String url = configProvider.getValue("parking.techpark.url", "");
		String parkingId = configProvider.getValue("parking.techpark.parkingId", "");

		JSONObject jsonParam = new JSONObject();
		jsonParam.put("ParkingID", parkingId);

		Map<String, String> params = new HashMap<>();
		params.put("data", jsonParam.toString());
		String json = Utils.post(url + "OISGetCardType", params);

		BosigaoJsonEntity<List<BosigaoCardType>> entity = JSONObject.parseObject(json, new TypeReference<BosigaoJsonEntity<List<BosigaoCardType>>>(){});

		List<ParkingCardType> list = new ArrayList<>();
		if(entity.isSuccess()) {
			List<BosigaoCardType> types = entity.getData();
			if (null != types && types.size() != 0) {
				types.forEach(t -> {
					ParkingCardType parkingCardType = new ParkingCardType();
					parkingCardType.setTypeId(t.getParaName());
					parkingCardType.setTypeName(t.getParaName());
					list.add(parkingCardType);
				});
			}
			ret.setCardTypes(list);
		}
    	return ret;
    }
    
    @Override
    public List<ParkingRechargeRateDTO> getParkingRechargeRates(String ownerType, Long ownerId, Long parkingLotId,String plateNumber,String cardNo) {
    	
    	List<ParkingRechargeRate> parkingRechargeRateList = new ArrayList<>();
    	
    	if(StringUtils.isBlank(plateNumber)) {
    		parkingRechargeRateList = parkingProvider.listParkingRechargeRates(ownerType, ownerId, parkingLotId, null);
    	}else{
    		BosigaoCardInfo card = getCardInfo(plateNumber);
    		String cardType = card.getOldCardTypeName();
    		parkingRechargeRateList = parkingProvider.listParkingRechargeRates(ownerType, ownerId, parkingLotId, cardType);
    	}
    	
    	List<ParkingRechargeRateDTO> result = parkingRechargeRateList.stream().map(r->{
			ParkingRechargeRateDTO dto = new ParkingRechargeRateDTO();
			dto = ConvertHelper.convert(r, ParkingRechargeRateDTO.class);
			dto.setRateToken(r.getId().toString());
			dto.setVendorName(ParkingLotVendor.BOSIGAO.getCode());
			return dto;
		}).collect(Collectors.toList());
		
		return result;
    }

    @Override
    public Boolean notifyParkingRechargeOrderPayment(ParkingRechargeOrder order) {
    	return recharge(order);
    }

    @Override
    public ParkingRechargeRateDTO createParkingRechargeRate(CreateParkingRechargeRateCommand cmd){
    	User user = UserContext.current().getUser();
    	
    	ParkingRechargeRate parkingRechargeRate = new ParkingRechargeRate();
    	parkingRechargeRate.setOwnerType(cmd.getOwnerType());
    	parkingRechargeRate.setOwnerId(cmd.getOwnerId());
    	parkingRechargeRate.setParkingLotId(cmd.getParkingLotId());
    	parkingRechargeRate.setCardType(cmd.getCardType());
    	/*费率 名称默认设置 by sw*/
    	Map<String, Object> map = new HashMap<String, Object>();
	    map.put("count", cmd.getMonthCount().intValue());
		String scope = ParkingNotificationTemplateCode.SCOPE;
		int code = ParkingNotificationTemplateCode.DEFAULT_RATE_NAME;
		String locale = "zh_CN";
		String rateName = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
    	parkingRechargeRate.setRateName(rateName);
    	parkingRechargeRate.setMonthCount(cmd.getMonthCount());
    	parkingRechargeRate.setPrice(cmd.getPrice());
    	parkingRechargeRate.setCreatorUid(user.getId());
    	parkingRechargeRate.setCreateTime(new Timestamp(System.currentTimeMillis()));
    	parkingRechargeRate.setStatus(ParkingRechargeRateStatus.ACTIVE.getCode());
    	parkingProvider.createParkingRechargeRate(parkingRechargeRate);
    	return ConvertHelper.convert(parkingRechargeRate, ParkingRechargeRateDTO.class);
    }
    
    @Override
    public void deleteParkingRechargeRate(DeleteParkingRechargeRateCommand cmd){
    	try {
    		ParkingRechargeRate rate = parkingProvider.findParkingRechargeRatesById(Long.parseLong(cmd.getRateToken()));
    		if(rate == null){
    			LOGGER.error("remote search pay order return null.rateId="+cmd.getRateToken());
    			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
    					"remote search pay order return null.");
    		}else{
    			parkingProvider.deleteParkingRechargeRate(rate);
    		}
    	} catch (Exception e) {
			LOGGER.error("delete parkingRechargeRate fail."+cmd.getRateToken());
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_SQL_EXCEPTION,
    				"delete parkingRechargeRate fail."+cmd.getRateToken());
		}
    }
    
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

	private String timestampToStr2(Long time) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String str = sdf.format(time);
		return str;
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

	@Override
	public ParkingTempFeeDTO getParkingTempFee(String ownerType, Long ownerId, Long parkingLotId, String plateNumber) {
		BosigaoTempFee tempFee = getTempFee(plateNumber);

		ParkingTempFeeDTO dto = new ParkingTempFeeDTO();
		if(null == tempFee) {
			return dto;
		}

		if (tempFee.getResult() == 0) {
			Pkorder pkorder = tempFee.getPkorder();

			if(null == pkorder) {
				return dto;
			}
			dto.setPlateNumber(plateNumber);
			long entranceDate = strToLong2(tempFee.getEntranceDate());
			dto.setEntryTime(entranceDate);
	//		dto.setPayTime(tempFee.getPayTime());
			long payTime = strToLong2(tempFee.getPayDate());

			dto.setPayTime(payTime);
			dto.setParkingTime((int)((tempFee.getPayTime() - entranceDate) / (1000 * 60)));
			dto.setDelayTime(tempFee.getOutTime());
			dto.setPrice(pkorder.getAmount().divide(new BigDecimal(100)));
			dto.setOrderToken(pkorder.getOrderID());
		}else if (tempFee.getResult() == 2 || tempFee.getResult() == 10) {
			dto.setPlateNumber(plateNumber);
			long entranceDate = strToLong2(tempFee.getEntranceDate());
			dto.setEntryTime(entranceDate);
			//		dto.setPayTime(tempFee.getPayTime());
			long payTime = strToLong2(tempFee.getPayDate());

			dto.setPayTime(payTime);
			dto.setParkingTime((int)((tempFee.getPayTime() - entranceDate) / (1000 * 60)));
			dto.setDelayTime(tempFee.getOutTime());
			dto.setPrice(new BigDecimal(0));
		}else if (tempFee.getResult() == 3) {
			LOGGER.error("Not support app recharge, tempFee={}", tempFee);
			throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.NOT_SUPPORT_APP_RECHARGE,
					"Not support app recharge.");
		}else if (tempFee.getResult() == 4 || tempFee.getResult() == 5) {
			LOGGER.error("Not support app recharge, tempFee={}", tempFee);
			throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.ERROR_REQUEST_SERVER,
					"Not support app recharge.");
		}

		return dto;
	}

	@Override
	public OpenCardInfoDTO getOpenCardInfo(GetOpenCardInfoCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ParkingCarLockInfoDTO getParkingCarLockInfo(GetParkingCarLockInfoCommand cmd) {

		BosigaoCarLockInfo bosigaoCarLockInfo = getCarLockInfo(cmd.getPlateNumber());

		if (null == bosigaoCarLockInfo) {
			return null;
		}
		ParkingCarLockInfoDTO dto = new ParkingCarLockInfoDTO();
		dto.setEntryTime(strToLong2(bosigaoCarLockInfo.getEntranceDate()));
		long lockTime = strToLong2(bosigaoCarLockInfo.getLockDate());
		if (lockTime > 0) {
			dto.setLockCarTime(lockTime);
		}
		dto.setLockStatus(bosigaoCarLockInfo.getStatus().byteValue());
		return dto;
	}

	@Override
	public void lockParkingCar(LockParkingCarCommand cmd) {
		BosigaoCarLockInfo bosigaoCarLockInfo = getCarLockInfo(cmd.getPlateNumber());

		if (null == bosigaoCarLockInfo) {
			return;
		}

		if (cmd.getLockStatus().equals(ParkingCarLockStatus.UNLOCK.getCode())) {
			lockParkingCar(cmd.getPlateNumber(), bosigaoCarLockInfo.getParkingID());
		}else {
			unLockParkingCar(cmd.getPlateNumber(), bosigaoCarLockInfo.getParkingID());
		}

	}

	private BosigaoCarLockInfo getCarLockInfo(String plateNumber){
		String url = configProvider.getValue("parking.techpark.url", "");
		String companyId = configProvider.getValue("parking.techpark.companyId", "");

		JSONObject jsonParam = new JSONObject();
		jsonParam.put("CompanyID", companyId);
		jsonParam.put("listPlateNumber", new String[]{plateNumber});

		Map<String, String> params = new HashMap<>();
		params.put("data", jsonParam.toString());
		String json = Utils.post(url + "OISGetAccountLockCar", params);

		BosigaoJsonEntity<List<BosigaoCarLockInfo>> entity = JSONObject.parseObject(json, new TypeReference<BosigaoJsonEntity<List<BosigaoCarLockInfo>>>(){});

		BosigaoCarLockInfo cardLockInfo = null;

		if(entity.isSuccess()){
			List<BosigaoCarLockInfo> cars = entity.getData();
			if (null != cars && cars.size() != 0) {
				cardLockInfo = cars.get(0);
			}
		}
		return cardLockInfo;
	}

	private boolean lockParkingCar(String plateNumber, String parkingID){
		String url = configProvider.getValue("parking.techpark.url", "");

		JSONObject jsonParam = new JSONObject();
		jsonParam.put("ParkingID", parkingID);
		jsonParam.put("PlateNumber", plateNumber);

		Map<String, String> params = new HashMap<>();
		params.put("data", jsonParam.toString());
		String json = Utils.post(url + "OISYKTLockCar", params);

		BosigaoJsonEntity<Object> entity = JSONObject.parseObject(json, new TypeReference<BosigaoJsonEntity<Object>>(){});

		BosigaoCardInfo card = null;

		return entity.isSuccess();
	}

	private boolean unLockParkingCar(String plateNumber, String parkingID){
		String url = configProvider.getValue("parking.techpark.url", "");

		JSONObject jsonParam = new JSONObject();
		jsonParam.put("ParkingID", parkingID);
		jsonParam.put("PlateNumber", plateNumber);

		Map<String, String> params = new HashMap<>();
		params.put("data", jsonParam.toString());
		String json = Utils.post(url + "OISYKTUnLockCar", params);

		BosigaoJsonEntity<Object> entity = JSONObject.parseObject(json, new TypeReference<BosigaoJsonEntity<Object>>(){});

		BosigaoCardInfo card = null;

		return entity.isSuccess();
	}

	private boolean verifyParkingCar(String plateNumber, String parkingID){
		String url = configProvider.getValue("parking.techpark.url", "");

		JSONObject jsonParam = new JSONObject();
		jsonParam.put("ParkingID", parkingID);
		jsonParam.put("PlateNumber", plateNumber);

		Map<String, String> params = new HashMap<>();
		params.put("data", jsonParam.toString());
		String json = Utils.post(url + "OISGetOrderState", params);

		BosigaoJsonEntity<Object> entity = JSONObject.parseObject(json, new TypeReference<BosigaoJsonEntity<Object>>(){});

		return entity.isSuccess();
	}

	@Override
	public GetParkingCarNumsResponse getParkingCarNums(GetParkingCarNumsCommand cmd) {
		String url = configProvider.getValue("parking.techpark.url", "");
		String companyId = configProvider.getValue("parking.techpark.companyId", "");
		
		JSONObject jsonParam = new JSONObject();
		jsonParam.put("CompanyID", companyId);
		
		Map<String, String> params = new HashMap<>();
		params.put("data", jsonParam.toString());
		String json = Utils.post(url + "OISGetPKCarNum", params);
		
		BosigaoJsonEntity<List<BosigaoCarNum>> entity = JSONObject.parseObject(json, new TypeReference<BosigaoJsonEntity<List<BosigaoCarNum>>>(){});

		if(entity.isSuccess()){
			List<BosigaoCarNum> carNumList = entity.getData();
			LOGGER.info("carNumList = {}",carNumList);
			if(carNumList != null){
				for (BosigaoCarNum bosigaoCarNum : carNumList) {
					if(bosigaoCarNum!=null){
						GetParkingCarNumsResponse response = new GetParkingCarNumsResponse();
						response.setParkName(bosigaoCarNum.getParkName());
						response.setAllCarNum(bosigaoCarNum.getCarNum());
						response.setEmptyCarNum(bosigaoCarNum.getSpaceNum());
						response.setCarNum(bosigaoCarNum.getInCarNum());
						return response;
					}
				}
			}
		}else
			LOGGER.info("request {}OISGetPKCarNum failed! param = {}",url,params);
		return null;
	}
}
