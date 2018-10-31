// @formatter:off
package com.everhomes.parking.handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import com.everhomes.flow.FlowCase;
import com.everhomes.parking.*;
import com.everhomes.parking.ketuo.*;
import com.everhomes.rest.parking.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.rest.organization.VendorType;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.MD5Utils;
import com.everhomes.util.RuntimeErrorException;

/**
 * 停车对接
 */
@Component(ParkingVendorHandler.PARKING_VENDOR_PREFIX + "KETUO_TEST")
public class KetuoTestParkingVendorHandler extends DefaultParkingVendorHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(KetuoTestParkingVendorHandler.class);

	static final String RECHARGE = "/api/wec/PayCarCardFee";
	private static final String GET_CARD = "/api/wec/GetCarCardInfo";
	private static final String GET_FREE_SPACE_NUM = "/api/wec/GetFreeSpaceNum";
	private static final String GET_CARD_RULE = "/api/wec/GetCarCardRule2";
	private static final String GET_TEMP_FEE = "/api/wec/GetParkingPaymentInfo";
	private static final String PAY_TEMP_FEE = "/api/wec/PayParkingFee2";
	private static final String ADD_MONTH_CARD = "/api/wec/AddCarCardNo";

	protected static final String ADD_NATURAL_MONTH = "ADD_NATURAL_MONTH";//按照自然月计算
	protected static final String ADD_DISTANCE_MONTH = "ADD_DISTANCE_MONTH";//用户新有效期=目标月份自然月月底-「1」所得剩余天数。rp6.4
	//只显示ruleType = 1时的充值项
	static final String RULE_TYPE = "1";
	//月租车 : 2
	static final String CAR_TYPE = "2";
	String URL = configProvider.getValue("parking.ketuotest.url", "http://220.160.111.118:8099/");
	String appId = configProvider.getValue("parking.ketuotest.appId", "1");
	String appSecret = configProvider.getValue("parking.ketuotest.appId", "b20887292a374637b4a9d6e9f940b1e6");
	String parkId = configProvider.getValue("parking.ketuotest.parkId", "1");
	
	@Override
    public List<ParkingCardDTO> listParkingCardsByPlate(ParkingLot parkingLot, String plateNumber) {
        
    	List<ParkingCardDTO> resultList = new ArrayList<>();

    	KetuoTestCard card = getCard(plateNumber);

		if(null != card){
			String expireDate = card.getValidTo();
			this.checkExpireDateIsNull(expireDate,plateNumber);

			long expireTime = strToLong(expireDate);

			ParkingCardDTO parkingCardDTO = convertCardInfo(parkingLot);

			setCardStatus(parkingLot, expireTime, parkingCardDTO);

			parkingCardDTO.setPlateNumber(plateNumber);
			parkingCardDTO.setPlateOwnerPhone("");

			parkingCardDTO.setEndTime(expireTime);
			parkingCardDTO.setCardTypeId(card.getCarType());
			parkingCardDTO.setCardNumber(card.getCardId().toString());

			resultList.add(parkingCardDTO);
		}
        
        return resultList;
    }

    @Override
    public List<ParkingRechargeRateDTO> getParkingRechargeRates(ParkingLot parkingLot,
    		String plateNumber, String cardNo) {
    	List<KetuoCardRate> list = new ArrayList<>();

    	if(StringUtils.isBlank(plateNumber)) {
    		return null;
    	}else{
    		KetuoTestCard cardInfo = getCard(plateNumber);
    		if(null != cardInfo) {
				Integer ruleId = cardInfo.getRuleId();
				List<KetuoTestCardRule> rules =  getCardRule(ruleId);
    		}
    	}

		return list.stream().map(r -> convertParkingRechargeRateDTO(parkingLot, r)).collect(Collectors.toList());
    }


	ParkingRechargeRateDTO convertParkingRechargeRateDTO(ParkingLot parkingLot, KetuoCardRate rate) {
		ParkingRechargeRateDTO dto = new ParkingRechargeRateDTO();
		dto.setOwnerId(parkingLot.getOwnerId());
		dto.setOwnerType(parkingLot.getOwnerType());
		dto.setParkingLotId(parkingLot.getId());
		dto.setRateToken(rate.getRuleId());
		Map<String, Object> map = new HashMap<>();
		map.put("count", rate.getRuleAmount());
		String scope = ParkingNotificationTemplateCode.SCOPE;
		int code = ParkingNotificationTemplateCode.DEFAULT_RATE_NAME;
		String locale = Locale.SIMPLIFIED_CHINESE.toString();
		String rateName = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
		dto.setRateName(rateName);
		dto.setCardTypeId(rate.getCarType());
		dto.setCardType(rate.getTypeName());
		dto.setMonthCount(new BigDecimal(rate.getRuleAmount()));
		dto.setPrice(new BigDecimal(rate.getRuleMoney()).divide(new BigDecimal(100), CARD_RATE_RETAIN_DECIMAL, RoundingMode.HALF_UP));
		dto.setVendorName(ParkingLotVendor.KETUO2.getCode());
		return dto;
	}

    @Override
    public Boolean notifyParkingRechargeOrderPayment(ParkingRechargeOrder order) {
		if (order.getOrderType().equals(ParkingOrderType.RECHARGE.getCode())) {
			if(order.getRechargeType().equals(ParkingRechargeType.MONTHLY.getCode()))
				return rechargeMonthlyCard(order);
			return payTempCardFee(order);
		}else {
			return addMonthCard(order);
		}
    }
    
    public void checkExpireDateIsNull(String expireDate,String plateNo) {
		if(StringUtils.isBlank(expireDate)){
			LOGGER.error("ExpireDate is null, plateNo={}", plateNo);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"ExpireDate is null.");
		}
	}
    
	long strToLong(String str) {
		return Utils.strToLong(str, Utils.DateStyle.DATE_TIME);
	}
	
	
	List<KetuoTestCardRule> getCardRule(Integer ruleId) {
		List<KetuoTestCardRule> result = new ArrayList<>();
		JSONObject param = new JSONObject();
		param.put("carType", ruleId);
    	String parkId = "1";

        String urlPath = URL+GET_CARD_RULE;
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
        String iv = sdf2.format(new Date());
        String p = parkId + ruleId + iv + appSecret;
        String key = MD5Utils.getMD5(p);
        param.put("appId", appId);
        param.put("key", key);
    	param.put("parkId", parkId);
		param.put("ruleId", ruleId);
        String json = param.toJSONString();
		String resp = doJsonPost(json, urlPath);

		KetuoJsonEntity<KetuoTestCardRule> entity = JSONObject.parseObject(resp, new TypeReference<KetuoJsonEntity<KetuoTestCardRule>>(){});
		if(entity.isSuccess()){
			if(null != entity.getData()) {
				result = entity.getData();
			}
		}
		return result;
	}
	
	KetuoTestCard getCard(String plateNumber) {
		KetuoTestCard card = null;
		JSONObject param = new JSONObject();
		
		//科托月卡车没有 归属地区分
    	plateNumber = plateNumber.substring(1, plateNumber.length());

        String urlPath = URL+GET_CARD;
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
        String iv = sdf2.format(new Date());
        String p = parkId + plateNumber + iv + appSecret;
        String key = MD5Utils.getMD5(p);
        param.put("appId", appId);
        param.put("key", key);
    	param.put("parkId", parkId);
		param.put("plateNo", plateNumber);
        String json = param.toJSONString();
		String result = doJsonPost(json, urlPath);
        
		KetuoJsonEntity<KetuoTestCard> entity = JSONObject.parseObject(result, new TypeReference<KetuoJsonEntity<KetuoTestCard>>(){});
		if(entity.isSuccess()){
			List<KetuoTestCard> list = entity.getData();
			if(null != list && !list.isEmpty()) {
				card = list.get(0);
			}
		}
        
        return card;
    }
	
    boolean payTempCardFee(ParkingRechargeOrder order){

		JSONObject param = new JSONObject();
		String urlPath = URL+PAY_TEMP_FEE;
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
        String iv = sdf2.format(new Date());
        Integer amount = (order.getOriginalPrice().multiply(new BigDecimal(100))).intValue();
        Integer payType = VendorType.WEI_XIN.getCode().equals(order.getPaidType()) ? 4 : 5;
        String payMethod = "0";
        String freeMoney = "0";
        String freeTime = "0";
        String p = order.getOrderToken() + amount + payType + payMethod + freeMoney + freeTime + iv + appSecret;
        String key = MD5Utils.getMD5(p);
        param.put("appId", appId);
        param.put("key", key);
        param.put("orderNo", order.getOrderToken());
        param.put("amount", amount);
        param.put("payType", payType);
        param.put("payMethod", payMethod);
        param.put("freeMoney", freeMoney);
        param.put("freeTime", freeTime);

        String json = param.toJSONString();
		String result = doJsonPost(json, urlPath);

        JSONObject jsonObject = JSONObject.parseObject(result);
		Object obj = jsonObject.get("resCode");
		if(null != obj ) {
			int resCode = (int) obj;
			if(resCode == 0) {
				return true;
			}
		}
		return false;
    }

	protected boolean rechargeMonthlyCard(ParkingRechargeOrder order) {
		return rechargeMonthlyCard(order,ADD_NATURAL_MONTH);
	}

	protected final boolean rechargeMonthlyCard(ParkingRechargeOrder order, String monthCardTimeArithmetic) {

		JSONObject param = new JSONObject();
		String plateNumber = order.getPlateNumber();

		KetuoTestCard card = getCard(plateNumber);
		String oldValidEnd = card.getValidTo();
		Long expireTime = strToLong(oldValidEnd);
		boolean baseNewestTime = configProvider.getBooleanValue("parking.ketuo.baseNewestTime." + order.getParkingLotId(), false);
		if(baseNewestTime){
			Long now = System.currentTimeMillis();
			if(now>expireTime){
				expireTime=now;
			}
		}

		Timestamp tempStart = Utils.addSecond(expireTime, 1);
		Timestamp tempEnd = null;
		if(ADD_DISTANCE_MONTH.equals(monthCardTimeArithmetic)) {
			tempEnd = Utils.getTimestampByAddDistanceMonthV2(expireTime,order.getMonthCount().intValue());
		}else{
			tempEnd = new Timestamp(Utils.getLongByAddNatureMonth(expireTime, order.getMonthCount().intValue()));

		}


		String urlPath = URL+GET_CARD;
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
        String iv = sdf2.format(new Date());
        String p = order.getOrderToken() + order.getOriginalPrice() + iv + appSecret;
        String key = MD5Utils.getMD5(p);
        param.put("appId", appId);
        param.put("key", key);
    	param.put("orderNo", order.getOrderToken());
		param.put("amount", order.getOriginalPrice());
        String json = param.toJSONString();
		String result = doJsonPost(json, urlPath);

		//将充值信息存入订单
		order.setErrorDescriptionJson(json);
        order.setStartPeriod(tempStart);
		order.setEndPeriod(tempEnd);

		JSONObject jsonObject = JSONObject.parseObject(result);
		Object obj = jsonObject.get("resCode");
		if(null != obj ) {
			int resCode = (int) obj;
			if(resCode == 0) {
				return true;
			}
		}
		return false;
	}

	private String doJsonPost(String json, String urlPath){
		
        String result = "";
        BufferedReader reader = null;
        try {
            URL url = new URL(urlPath);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Charset", "UTF-8");
            // 设置文件类型:
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            // 设置接收类型否则返回415错误
            conn.setRequestProperty("accept", "*/*");// 此处为暴力方法设置接受所有类型，以此来防范返回415;
            conn.setRequestProperty("accept", "application/json");
            // 往服务器里面发送数据
            if (json != null && !("").equals(json)) {
                byte[] writebytes = json.getBytes();
                // 设置文件长度
                conn.setRequestProperty("Content-Length", String.valueOf(writebytes.length));
                OutputStream outwritestream = (OutputStream) conn.getOutputStream();
                outwritestream.write(json.getBytes());
                outwritestream.flush();
                outwritestream.close();

            }
            if (conn.getResponseCode() == 200) {
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                result = reader.readLine();
            }
        } catch (Exception e) {
            LOGGER.error("The request error, param={}", json, e);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                    "The request error.");
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        LOGGER.info("SendResult from third, url={}, result={}", urlPath, result);
        return result;
    }


	private KetuoTempFee getTempFee(String plateNumber) {
		KetuoTempFee tempFee = null;
		JSONObject param = new JSONObject();
        String urlPath = URL+GET_TEMP_FEE;
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
        String iv = sdf2.format(new Date());
        String p = parkId + plateNumber + iv + appSecret;
        String key = MD5Utils.getMD5(p);
        param.put("appId", appId);
        param.put("key", key);
    	param.put("parkId", parkId);
		param.put("plateNo", plateNumber);
        String json = param.toJSONString();
		String result = doJsonPost(json, urlPath);
        
		KetuoJsonEntity<KetuoTempFee> entity = JSONObject.parseObject(result, new TypeReference<KetuoJsonEntity<KetuoTempFee>>(){});
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
		dto.setPrice(new BigDecimal(tempFee.getPayable()).divide(new BigDecimal(100), TEMP_FEE_RETAIN_DECIMAL, RoundingMode.HALF_UP));

		dto.setOrderToken(tempFee.getOrderNo());
		return dto;
	}

	@Override
	public OpenCardInfoDTO getOpenCardInfo(GetOpenCardInfoCommand cmd) {

		ParkingCardRequest parkingCardRequest = parkingProvider.findParkingCardRequestById(cmd.getParkingRequestId());

		FlowCase flowCase = flowCaseProvider.getFlowCaseById(parkingCardRequest.getFlowCaseId());

		ParkingFlow parkingFlow = parkingProvider.getParkingRequestCardConfig(cmd.getOwnerType(), cmd.getOwnerId(), 
				cmd.getParkingLotId(), flowCase.getFlowMainId());

		Integer requestMonthCount = REQUEST_MONTH_COUNT;
		Byte requestRechargeType = REQUEST_RECHARGE_TYPE;
		
		if(null != parkingFlow) {
			requestMonthCount = parkingFlow.getRequestMonthCount();
			requestRechargeType = parkingFlow.getRequestRechargeType();
		}
		
		OpenCardInfoDTO dto = new OpenCardInfoDTO();
		String cardTypeId = parkingCardRequest.getCardTypeId();
		Integer ruleId;
		if (StringUtils.isBlank(cardTypeId)) {
			List<ParkingCardRequestType> types = parkingProvider.listParkingCardTypes(parkingCardRequest.getOwnerType(),
					parkingCardRequest.getOwnerId(), parkingCardRequest.getParkingLotId());
			ruleId = Integer.valueOf(types.get(0).getCardTypeId());
		} else {
			ruleId = Integer.valueOf(parkingCardRequest.getCardTypeId());
		}

		//月租车
		List<KetuoTestCardRule> rules = getCardRule(ruleId);
		if(null != rules && !rules.isEmpty()) {
			
			KetuoTestCardRule rate = null;
			for(KetuoTestCardRule r: rules) {
				if(Integer.valueOf(r.getRuleAmount()) == 1) {
					rate = r;
					break;
				}
			}

			if (null == rate) {
				//TODO:
				return null;
			}


			dto.setOwnerId(cmd.getOwnerId());
			dto.setOwnerType(cmd.getOwnerType());
			dto.setParkingLotId(cmd.getParkingLotId());
			dto.setRateToken(rate.getRuleId().toString());
			Map<String, Object> map = new HashMap<String, Object>();
		    map.put("count", rate.getRuleAmount());
			String scope = ParkingNotificationTemplateCode.SCOPE;
			int code = ParkingNotificationTemplateCode.DEFAULT_RATE_NAME;
			String locale = "zh_CN";
			String rateName = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
			dto.setRateName(rateName);
			dto.setMonthCount(new BigDecimal(rate.getRuleAmount()));
			dto.setPrice(new BigDecimal(rate.getRuleFee()).divide(new BigDecimal(100), OPEN_CARD_RETAIN_DECIMAL, RoundingMode.HALF_UP));

			dto.setPlateNumber(cmd.getPlateNumber());
			long now = configProvider.getLongValue("parking.opencard.now",System.currentTimeMillis());
			dto.setOpenDate(now);
			dto.setExpireDate(Utils.getLongByAddNatureMonth(now, requestMonthCount,true));
			if(requestRechargeType == ParkingCardExpiredRechargeType.ALL.getCode()) {
				dto.setPayMoney(dto.getPrice().multiply(new BigDecimal(requestMonthCount)));
			}else {
				Calendar calendar = Calendar.getInstance();
				calendar.setTimeInMillis(now);
				int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
				int today = calendar.get(Calendar.DAY_OF_MONTH);

				BigDecimal firstMonthPrice = dto.getPrice().multiply(new BigDecimal(maxDay-today+1))
						.divide(new BigDecimal(DAY_COUNT), OPEN_CARD_RETAIN_DECIMAL, RoundingMode.HALF_UP);
				if(firstMonthPrice.compareTo(dto.getPrice())>0){
					firstMonthPrice = dto.getPrice();
				}
				BigDecimal price = dto.getPrice().multiply(new BigDecimal(requestMonthCount-1))
						.add(firstMonthPrice);
				dto.setPayMoney(price);
			}
			if(configProvider.getBooleanValue("parking.ketuo.debug",false)){
				LOGGER.debug("parking.ketuo.debug is true, pay 0.01 RMB");
				dto.setPayMoney(new BigDecimal(0.01));
			}
			dto.setOrderType(ParkingOrderType.OPEN_CARD.getCode());
		}

		return dto;
	}
	
	private boolean addMonthCard(ParkingRechargeOrder order){

		//开通月卡走工作流 流程，为了兼容以前老版本app，这里查询做了判断，现根据订单CardRequestId来查询申请记录，
		//如果查不到，在去查询当前用户 当前工作流模式， 当前车牌的有效申请记录，这个记录一定是唯一一条，如果查询出来多条，则业务逻辑错误
		ParkingCardRequest request;
		if (null != order.getCardRequestId()) {
			request = parkingProvider.findParkingCardRequestById(order.getCardRequestId());
		}else {
			request = getParkingCardRequestByOrder(order);
		}

		if (request == null) {
			return false;
		}

		JSONObject param = new JSONObject();

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		long tempTime = calendar.getTimeInMillis();
		Timestamp tempStart = new Timestamp(tempTime);
		String startTime = Utils.dateToStr(tempStart, Utils.DateStyle.DATE_TIME);
		Timestamp tempEnd = Utils.getTimestampByAddDistanceMonthV2(tempTime, order.getMonthCount().intValue());
		String endTime = Utils.dateToStr(tempEnd, Utils.DateStyle.DATE_TIME);
        String urlPath = URL+ ADD_MONTH_CARD;
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
        String iv = sdf2.format(new Date());
        String p = parkId + order.getPlateNumber() +"2" +startTime+ endTime +  iv + appSecret;
        String key = MD5Utils.getMD5(p);
        param.put("appId", appId);
        param.put("key", key);
    	param.put("parkId", parkId);
		param.put("plateNo", order.getPlateNumber());
		param.put("carType", "2");
		param.put("ValidFrom", startTime);
		param.put("ValidTo", endTime);
        String json = param.toJSONString();
		String result = doJsonPost(json, urlPath);
		

		//将充值信息存入订单
		order.setErrorDescriptionJson(result);
		order.setStartPeriod(tempStart);
		order.setEndPeriod(tempEnd);

		JSONObject jsonObject = JSONObject.parseObject(result);
		Object obj = jsonObject.get("resCode");
		if(null != obj ) {
			int resCode = (int) obj;
			if(resCode == 0) {
				updateFlowStatus(request);
				return true;
			}
		}
		return false;
	}

    @Override
    public ParkingFreeSpaceNumDTO getFreeSpaceNum(GetFreeSpaceNumCommand cmd) {
		ParkingFreeSpaceNumDTO dto = ConvertHelper.convert(cmd, ParkingFreeSpaceNumDTO.class);

		KexingFreeSpaceNum kexingFreeSpaceNum = getFreeSpaceNum();

		if (null != kexingFreeSpaceNum) {
			dto.setFreeSpaceNum(kexingFreeSpaceNum.getFreeSpaceNum());

			return dto;
		}
		return null;
    }
    
	private KexingFreeSpaceNum getFreeSpaceNum() {


		LinkedHashMap<String, Object> param = new LinkedHashMap<>();
		param.put("parkId", parkId);

		JSONObject params = createRequestParam(param);
		String json = Utils.post(URL + GET_FREE_SPACE_NUM, params);

		KetuoJsonEntity<KexingFreeSpaceNum> entity = JSONObject.parseObject(json, new TypeReference<KetuoJsonEntity<KexingFreeSpaceNum>>(){});

		if(entity.isSuccess()){
			List<KexingFreeSpaceNum> list = entity.getData();
			if(null != list && !list.isEmpty()) {
				return list.get(0);
			}
		}
		return null;
	}
	
	private JSONObject createRequestParam(LinkedHashMap<String, Object> param) {


		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

		StringBuilder sb = new StringBuilder();
		Set<Map.Entry<String, Object>> entries = param.entrySet();
		for (Map.Entry entry: entries) {
			sb.append(entry.getValue());
		}

		String str = sb.append(sdf.format(new Date())).append(appSecret).toString();
		String key = MD5Utils.getMD5(str);

		JSONObject params = new JSONObject();
		params.put("appId", appId);
		params.put("key", key );

		for (Map.Entry entry: entries) {
			params.put((String)entry.getKey(), entry.getValue());
		}

		return params;
	}

	@Override
	public ListCardTypeResponse listCardType(ListCardTypeCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateParkingRechargeOrderRate(ParkingLot parkingLot, ParkingRechargeOrder order) {
		// TODO Auto-generated method stub
		
	}

}
