// @formatter:off
package com.everhomes.parking;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.parking.jinyi.JinyiCard;
import com.everhomes.parking.jinyi.JinyiJsonEntity;
import com.everhomes.rest.parking.*;
import com.everhomes.util.RuntimeErrorException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component(ParkingVendorHandler.PARKING_VENDOR_PREFIX + "JIN_YI")
public class JinyiParkingVendorHandler implements ParkingVendorHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(JinyiParkingVendorHandler.class);

	private static final String GET_CARD = "parkingjet.open.s2s.parkingfee.month.calcfee.plateno";
	private static final String CREATE_ORDER = "parkingjet.open.s2s.parkingfee.month.order.create";
	private static final String NOTIFY = "parkingjet.open.s2s.parkingfee.month.payresult.notify";

	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
	DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	static String url = "http://tgd.poapi.parkingjet.cn:8082/CommonOpenApi/default.ashx";
	static String appid = "201706221000";
	static String appkey = "qyruirxn20145601739";
	static String parkingid = "0755000120170301000000000003";

	@Autowired
	private ParkingProvider parkingProvider;
	
	@Autowired
    private ConfigurationProvider configProvider;
	
	@Override
    public GetParkingCardsResponse getParkingCardsByPlate(String ownerType, Long ownerId,
    		Long parkingLotId, String plateNumber) {
        
    	List<ParkingCardDTO> resultList = new ArrayList<>();
		GetParkingCardsResponse response = new GetParkingCardsResponse();
		response.setCards(resultList);

		JinyiCard card = getCardInfo(plateNumber);
    	
        ParkingCardDTO parkingCardDTO = new ParkingCardDTO();
		if(null != card){
			//格式yyyy-MM-dd
			String expiredate = card.getExpiredate() + " 23:59:59";
			LocalDateTime time = LocalDateTime.parse(expiredate, dtf2);
			Long endTime = Timestamp.valueOf(time).getTime();

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
			
			String plateOwnerName = card.getOwnername();

			String cardNumber = null;
			ParkingCardType parkingCardType = createDefaultCardType();
			String cardType = parkingCardType.getTypeName();
			
			parkingCardDTO.setOwnerType(ParkingOwnerType.COMMUNITY.getCode());
			parkingCardDTO.setOwnerId(ownerId);
			parkingCardDTO.setParkingLotId(parkingLotId);
			
			parkingCardDTO.setPlateOwnerName(plateOwnerName);
			parkingCardDTO.setPlateNumber(card.getPlateno());
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

    private JinyiCard getCardInfo(String plateNumber){

		Map<String, String> params = createGeneralParam(GET_CARD, createGetCardParam(plateNumber));

		String responseJson = Utils.post(url, params);


		JinyiJsonEntity<JinyiCard> jsonEntity = JSONObject.parseObject(responseJson, new TypeReference<JinyiJsonEntity<JinyiCard>>(){});

		if(jsonEntity.isSuccess()) {
			return jsonEntity.getData();
		}

		return null;
    }

    private Map<String, String> createGeneralParam(String methodName, JSONObject json) {
		Map<String, String> params = new HashMap<>();
		params.put("methodname", methodName);
		params.put("appid", appid);

		LocalDateTime localDateTime = LocalDateTime.now();
		String timestamp = localDateTime.format(dtf);

		params.put("timestamp", timestamp);
		params.put("version", "1.0");
		params.put("postdata", json.toJSONString());

		List<String> keys = new ArrayList<>();
		keys.addAll(params.keySet());
		Collections.sort(keys);

		StringBuilder sb = new StringBuilder();
		keys.forEach(k -> {
			sb.append(k).append("=").append(params.get(k)).append("&");

		});
		String p = sb.substring(0,sb.length() - 1);

		String md5Sign = stringMD5(p + appkey);
		params.put("sign", md5Sign);

		return params;
	}

	private String stringMD5(String pw) {
		try {
			MessageDigest messageDigest =MessageDigest.getInstance("MD5");
			messageDigest.update(pw.getBytes());
			return new BigInteger(1, messageDigest.digest()).toString(16);
		} catch (NoSuchAlgorithmException e) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"Convert md5 failed");
		}
	}

	private JSONObject createGetCardParam(String plateNo) {
		JSONObject json = new JSONObject();
		json.put("parkingid", parkingid);
		json.put("plateno", plateNo);
		json.put("quantity", "1");
		return json;
	}

	private JSONObject createRechargeParam(ParkingRechargeOrder order) {
		JSONObject json = new JSONObject();
		json.put("parkingid", parkingid);
		json.put("orderid", order.getOrderToken());
		json.put("transid", order.getOrderNo());
		json.put("paidin", order.getPrice());
		json.put("discount", new BigDecimal(0));
		json.put("returninfo", "");
		LocalDateTime localDateTime = LocalDateTime.now();
		String payTime = localDateTime.format(dtf2);
		json.put("paytime", payTime);
		//支付状态 1 成功 2 失败
		json.put("status", "1");

		return json;
	}

	private JSONObject createOrderParam(ParkingRechargeOrder order) {
		JSONObject json = new JSONObject();
		json.put("parkingid", parkingid);
		json.put("plateno", order.getPlateNumber());
		json.put("receivable", order.getPrice());
		json.put("calcid", order.getOrderToken());
		json.put("paymenttype", 1209);
//        json.put("openid", "1");
//        json.put("receivable", "1");
		return json;
	}

    @Override
	public boolean recharge(ParkingRechargeOrder order){
		if(order.getRechargeType().equals(ParkingRechargeType.MONTHLY.getCode()))
			return rechargeMonthlyCard(order);
		return false;
	}

	private boolean rechargeMonthlyCard(ParkingRechargeOrder order){

		JinyiCard card = getCardInfo(order.getPlateNumber());

		if (null != card) {
			//根据查询月卡时的计费id来创建订单
			order.setOrderToken(card.getCalcid());

			//金溢停车先创建订单，然后根据订单id来充值
			Map<String, String> createOrderParams = createGeneralParam(CREATE_ORDER, createOrderParam(order));

			String responseJson = Utils.post(url, createOrderParams);

			JinyiJsonEntity<String> jsonEntity = JSONObject.parseObject(responseJson,
					new TypeReference<JinyiJsonEntity<String>>() {});

			if (jsonEntity.isSuccess()) {
				//根据创建订单返回的id去充值
				String orderId = jsonEntity.getData();

				order.setOrderToken(orderId);
				Map<String, String> notifyParams = createGeneralParam(NOTIFY, createRechargeParam(order));

				String notifyJson = Utils.post(url, notifyParams);

				JinyiJsonEntity<Object> notifyEntity = JSONObject.parseObject(notifyJson,
						new TypeReference<JinyiJsonEntity<Object>>(){});

				//将充值信息存入订单
				JinyiCard newCard = getCardInfo(order.getPlateNumber());

				if (null != newCard) {
					String expiredate = newCard.getExpiredate() + " 23:59:59";
					String effectdate = newCard.getEffectdate() + " 00:00:00";
					LocalDateTime expireTime = LocalDateTime.parse(expiredate, dtf2);
					LocalDateTime effectTime = LocalDateTime.parse(effectdate, dtf2);

					order.setErrorDescriptionJson(notifyJson);

					order.setStartPeriod(Timestamp.valueOf(effectTime));
					order.setEndPeriod(Timestamp.valueOf(expireTime));
				}

				if(notifyEntity.isSuccess()) {
					return true;
				}
			}

		}

		return false;
	}

	public ListCardTypeResponse listCardType(ListCardTypeCommand cmd) {
    	ListCardTypeResponse ret = new ListCardTypeResponse();

		List<ParkingCardType> list = new ArrayList<>();
		list.add(createDefaultCardType());
		ret.setCardTypes(list);

    	return ret;
    }

	private ParkingCardType createDefaultCardType() {
		//金溢对接停车 没有月卡类型，默认一个月卡类型
		ParkingCardType cardType = new ParkingCardType();
		cardType.setTypeId("月卡");
		cardType.setTypeName("月卡");

		return cardType;
	}

    @Override
    public List<ParkingRechargeRateDTO> getParkingRechargeRates(String ownerType, Long ownerId, Long parkingLotId,String plateNumber,String cardNo) {
    	
    	List<ParkingRechargeRateDTO> parkingRechargeRateList = new ArrayList<>();

    	if(StringUtils.isBlank(plateNumber)) {
//    		parkingRechargeRateList = parkingProvider.listParkingRechargeRates(ownerType, ownerId, parkingLotId, null);
    	}else{
    		JinyiCard card = getCardInfo(plateNumber);

			ParkingRechargeRateDTO rate = new ParkingRechargeRateDTO();
			rate.setOwnerId(ownerId);
			rate.setOwnerType(ownerType);
			rate.setParkingLotId(parkingLotId);
			rate.setRateToken("");
			rate.setRateName("一个月");

			ParkingCardType parkingCardType = createDefaultCardType();
			rate.setCardType(parkingCardType.getTypeName());
			rate.setMonthCount(new BigDecimal(1));
			rate.setPrice(card.getPaidin());

    		parkingRechargeRateList.add(rate);
    	}

//    	List<ParkingRechargeRateDTO> result = parkingRechargeRateList.stream().map(r->{
//			ParkingRechargeRateDTO dto = new ParkingRechargeRateDTO();
//			dto = ConvertHelper.convert(r, ParkingRechargeRateDTO.class);
//			dto.setRateToken(r.getId().toString());
//			dto.setVendorName(ParkingLotVendor.BOSIGAO.getCode());
//			return dto;
//		}).collect(Collectors.toList());

		return parkingRechargeRateList;
    }

    @Override
    public Boolean notifyParkingRechargeOrderPayment(ParkingRechargeOrder order) {
    	return recharge(order);
    }

    @Override
    public ParkingRechargeRateDTO createParkingRechargeRate(CreateParkingRechargeRateCommand cmd){

		return null;
    }
    
    @Override
    public void deleteParkingRechargeRate(DeleteParkingRechargeRateCommand cmd){

    }

	@Override
	public void updateParkingRechargeOrderRate(ParkingRechargeOrder order) {

		
	}

	@Override
	public ParkingTempFeeDTO getParkingTempFee(String ownerType, Long ownerId, Long parkingLotId, String plateNumber) {

		return null;
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

		return null;
	}
}
