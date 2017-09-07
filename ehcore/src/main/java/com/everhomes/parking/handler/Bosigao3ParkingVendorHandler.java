// @formatter:off
package com.everhomes.parking.handler;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
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
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;

/**
 * 科技园停车
 */
// "BOSIGAO"需与ParkingLotVendor.BOSIGAO的枚举值保持一致
@Component(ParkingVendorHandler.PARKING_VENDOR_PREFIX + "BOSIGAO3")
public class Bosigao3ParkingVendorHandler extends DefaultParkingVendorHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(Bosigao3ParkingVendorHandler.class);

	@Autowired
	private ParkingProvider parkingProvider;
	@Autowired
    private ConfigurationProvider configProvider;
	
	@Override
    public List<ParkingCardDTO> listParkingCardsByPlate(ParkingLot parkingLot, String plateNumber) {
        
    	List<ParkingCardDTO> resultList = new ArrayList<>();

    	BosigaoCardInfo card = getCardInfo(plateNumber);
    	
        ParkingCardDTO parkingCardDTO = new ParkingCardDTO();
		if(null != card){
			//格式yyyyMMddHHmmss
			String validEnd = card.getLimitEnd();
			Long endTime = strToLong(validEnd);
			if (checkExpireTime(parkingLot, endTime)) {
				return resultList;
			}
			
			String plateOwnerName = card.getUserName();

			String cardNumber = card.getCardID();
			String cardType = card.getOldCardTypeName();

			parkingCardDTO.setOwnerType(parkingLot.getOwnerType());
			parkingCardDTO.setOwnerId(parkingLot.getOwnerId());
			parkingCardDTO.setParkingLotId(parkingLot.getId());
			
			parkingCardDTO.setPlateOwnerName(plateOwnerName);
			parkingCardDTO.setPlateNumber(card.getPlateNumber());
			parkingCardDTO.setEndTime(endTime);
			parkingCardDTO.setCardType(cardType);
			parkingCardDTO.setCardNumber(cardNumber);
			parkingCardDTO.setIsValid(true);
			
			resultList.add(parkingCardDTO);
		}
        return resultList;
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

		Long startPeriod = strToLong(card.getLimitEnd());

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
    public List<ParkingRechargeRateDTO> getParkingRechargeRates(ParkingLot parkingLot,String plateNumber,String cardNo) {
    	
    	List<ParkingRechargeRate> parkingRechargeRateList;
    	
    	if(StringUtils.isBlank(plateNumber)) {
    		parkingRechargeRateList = parkingProvider.listParkingRechargeRates(parkingLot.getOwnerType(), parkingLot.getOwnerId(),
					parkingLot.getId(), null);
    	}else{
    		BosigaoCardInfo card = getCardInfo(plateNumber);
    		String cardType = card.getOldCardTypeName();
    		parkingRechargeRateList = parkingProvider.listParkingRechargeRates(parkingLot.getOwnerType(), parkingLot.getOwnerId(),
					parkingLot.getId(), cardType);
    	}
    	
    	List<ParkingRechargeRateDTO> result = parkingRechargeRateList.stream().map(r->{
			ParkingRechargeRateDTO dto = ConvertHelper.convert(r, ParkingRechargeRateDTO.class);
			dto.setRateToken(r.getId().toString());
			dto.setVendorName(ParkingLotVendor.BOSIGAO.getCode());
			return dto;
		}).collect(Collectors.toList());
		
		return result;
    }

    @Override
    public Boolean notifyParkingRechargeOrderPayment(ParkingRechargeOrder order) {
		if(order.getRechargeType().equals(ParkingRechargeType.MONTHLY.getCode()))
			return rechargeMonthlyCard(order);
		return payTempCardFee(order);    }
    
    private Long strToLong(String str) {
		return Utils.strToLong(str, Utils.DateStyle.DATE_TIME_STR);
	}

	private String timestampToStr2(Long time) {
		Date date = new Date();
		date.setTime(time);
		return Utils.dateToStr(date, Utils.DateStyle.DATE_TIME_STR);
	}

	@Override
	public ParkingTempFeeDTO getParkingTempFee(ParkingLot parkingLot, String plateNumber) {
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
			long entranceDate = strToLong(tempFee.getEntranceDate());
			dto.setEntryTime(entranceDate);
			long payTime = strToLong(tempFee.getPayDate());

			dto.setPayTime(payTime);
			dto.setParkingTime((int)((tempFee.getPayTime() - entranceDate) / (1000 * 60)));
			dto.setDelayTime(tempFee.getOutTime());
			dto.setPrice(pkorder.getAmount().divide(new BigDecimal(100), 2, RoundingMode.HALF_UP));
			dto.setOrderToken(pkorder.getOrderID());
		}else if (tempFee.getResult() == 2 || tempFee.getResult() == 10) {
			dto.setPlateNumber(plateNumber);
			long entranceDate = strToLong(tempFee.getEntranceDate());
			dto.setEntryTime(entranceDate);
			//		dto.setPayTime(tempFee.getPayTime());
			long payTime = strToLong(tempFee.getPayDate());

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
	public ParkingCarLockInfoDTO getParkingCarLockInfo(GetParkingCarLockInfoCommand cmd) {

		BosigaoCarLockInfo bosigaoCarLockInfo = getCarLockInfo(cmd.getPlateNumber());

		if (null == bosigaoCarLockInfo) {
			return null;
		}
		ParkingCarLockInfoDTO dto = new ParkingCarLockInfoDTO();
		dto.setEntryTime(strToLong(bosigaoCarLockInfo.getEntranceDate()));
		long lockTime = strToLong(bosigaoCarLockInfo.getLockDate());
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
		}else {
			LOGGER.info("request {} OISGetPKCarNum failed! param = {}",url,params);
		}
		return null;
	}
}
