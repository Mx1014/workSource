// @formatter:off
package com.everhomes.parking.handler.haikangweishi;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.everhomes.bigcollection.Accessor;
import com.everhomes.bigcollection.BigCollectionProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.parking.ParkingLot;
import com.everhomes.parking.ParkingProvider;
import com.everhomes.parking.ParkingRechargeOrder;
import com.everhomes.parking.handler.DefaultParkingVendorHandler;
import com.everhomes.parking.handler.Utils;
import com.everhomes.parking.jieshun.JieShunResponse;
import com.everhomes.rest.parking.ListCardTypeCommand;
import com.everhomes.rest.parking.ListCardTypeResponse;
import com.everhomes.rest.parking.ParkingCardDTO;
import com.everhomes.rest.parking.ParkingErrorCode;
import com.everhomes.rest.parking.ParkingOrderType;
import com.everhomes.rest.parking.ParkingRechargeRateDTO;
import com.everhomes.rest.parking.ParkingRechargeType;
import com.everhomes.rest.parking.ParkingTempFeeDTO;
import com.everhomes.rest.parking.handler.haikangweishi.ErrorCodeEnum;
import com.everhomes.rest.parking.handler.haikangweishi.HkwsThirdResponse;
import com.everhomes.rest.parking.handler.haikangweishi.TempFeeInfo;
import com.everhomes.sms.DateUtil;
import com.everhomes.user.UserContext;
import com.everhomes.util.MD5Utils;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;

/**
 * @Author 明波[mingbo.huang@zuolin.com]
 * @Date 2018/11/22 产品功能 #40350 停车缴费V6.7.6（海康威视停车系统对接）
 */
public abstract class HaiKangWeiShiVendorHandler extends DefaultParkingVendorHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(HaiKangWeiShiVendorHandler.class);

	private final String GET_TEMP_FEE_ORDER_URL = "/openapi/service/pms/charge/getChargeBill";
	private final String NOTIFY_TEMP_FEE_RECHARGE = "/openapi/service/pms/charge/payVehilceBill";
	private final String GET_DEFAULT_USER_UUID = "/openapi/service/base/user/getDefaultUserUuid";
	
	@Autowired
	BigCollectionProvider bigCollectionProvider;

	@Autowired
	ConfigurationProvider configurationProvider;

	@Autowired
	ParkingProvider parkingProvider;

	public abstract String getParkingVendorName();

	@Override
	public Boolean notifyParkingRechargeOrderPayment(ParkingRechargeOrder order) {
		if (order.getOrderType().equals(ParkingOrderType.RECHARGE.getCode())) {
			if (order.getRechargeType().equals(ParkingRechargeType.TEMPORARY.getCode())) {
				return payTempCardFee(order);
			}
		}

		LOGGER.info("unknown type = " + order.getRechargeType() + " orderId:" + order.getId() + " order.plate:"
				+ order.getPlateNumber());
		return false;
	}

	@Override
	public ParkingTempFeeDTO getParkingTempFee(ParkingLot parkingLot, String plateNumber) {
		ParkingTempFeeDTO dto = new ParkingTempFeeDTO();
		TempFeeInfo tempFeeInfo = getParkingTempFee(plateNumber);
		if (null == tempFeeInfo) {
			return dto;
		}

		dto.setPrice(new BigDecimal(tempFeeInfo.getCost()));
		dto.setPlateNumber(plateNumber);
		Timestamp entryTime = DateUtil.parseTimestamp(tempFeeInfo.getEnterTime());
		dto.setEntryTime(entryTime.getTime());
		long now = System.currentTimeMillis();
		dto.setPayTime(now);
		dto.setParkingTime(tempFeeInfo.getParkingTime());
		dto.setOriginalPrice(dto.getPrice());
		dto.setOrderToken(tempFeeInfo.getPreBillUuid());
		dto.setDelayTime(tempFeeInfo.getDelayTime());// 临时车缴费成功了之后，有多长预留的出场时间？一般15到30分，可以在车场配置的
		return dto;
	}

	@Override
	public void updateParkingRechargeOrderRate(ParkingLot parkingLot, ParkingRechargeOrder order) {

	}

	private Boolean payTempCardFee(ParkingRechargeOrder order) {

		HkwsThirdResponse resp = notifyTempFeeOrderPost(order.getOrderToken(), order.getOriginalPrice().toPlainString(),
				order.getPrice().toPlainString());
		if (null == resp) {
			return false;
		}

		order.setErrorDescriptionJson(StringHelper.toJsonString(resp));
		order.setErrorDescription(resp.getErrorMessage());

		if (!isSuccess(resp)) {
			return false;
		}

		return true;
	}
	
	@Override
	public List<ParkingCardDTO> listParkingCardsByPlate(ParkingLot parkingLot, String plateNumber) {
		return new ArrayList<>(1);
	}

	@Override
	public List<ParkingRechargeRateDTO> getParkingRechargeRates(ParkingLot parkingLot, String plateNumber,
			String cardNo) {
		return new ArrayList<>(1);
	}

	@Override
	public ListCardTypeResponse listCardType(ListCardTypeCommand cmd) {
		return new ListCardTypeResponse();
	}
	
	

	private HkwsThirdResponse notifyTempFeeOrderPost(String preBillUuid, String originCost, String currentCost) {
		JSONObject json = new JSONObject();
		json.put("preBillUuid", preBillUuid);
		json.put("totalCost", originCost);
		json.put("realCost", originCost);
		json.put("cost", currentCost);
		return post(NOTIFY_TEMP_FEE_RECHARGE, json);
	}

	private TempFeeInfo getParkingTempFee(String plateNumber) {

		HkwsThirdResponse resp = getParkingTempFeePost(plateNumber);
		if (!isSuccess(resp) || resp.isEmpty()) {
			return null;
		}

		return JSONObject.parseObject(resp.getData(), TempFeeInfo.class);
	}

	private HkwsThirdResponse getParkingTempFeePost(String plateNumber) {
		JSONObject json = new JSONObject();
		json.put("parkUuid", getParkUuid());
		json.put("plateNo", plateNumber);
		return post(GET_TEMP_FEE_ORDER_URL, json);
	}

	private String getOpUserUuidPost() {
		JSONObject json = new JSONObject();
		json.put("appkey", getAppyKey());
		json.put("time", System.currentTimeMillis());// 设置时间参数
		String ret = Utils.post(convetUrl(GET_DEFAULT_USER_UUID, json), json);
		if (StringUtils.isBlank(ret)) {
			return null;
		}

		HkwsThirdResponse resp = JSONObject.parseObject(ret, HkwsThirdResponse.class);
		if (!isSuccess(resp) || resp.isEmpty()) {
			return null;
		}

		return resp.getData();
	}

	private HkwsThirdResponse post(String urlSuffix, JSONObject json) {
		json.put("appkey", getAppyKey());
		json.put("time", System.currentTimeMillis());// 设置时间参数
		json.put("opUserUuid", getOpUserUuid(false));// 设置操作用户UUID
		String ret = Utils.post(convetUrl(urlSuffix, json), json);
		if (StringUtils.isEmpty(ret)) {
			return null;
		}

		HkwsThirdResponse resp = JSONObject.parseObject(ret, HkwsThirdResponse.class);
		if (ErrorCodeEnum.API_PARAM_NOT_VALID.getCode().equals(resp.getErrorCode())) {
			json.put("opUserUuid", getOpUserUuid(true));// 设置操作用户UUID
			ret = Utils.post(convetUrl(urlSuffix, json), json);
			resp = ret == null ? null : JSONObject.parseObject(ret, HkwsThirdResponse.class);
		}

		return resp;
	}

	private boolean isSuccess(HkwsThirdResponse resp) {
		if (null != resp && ErrorCodeEnum.SUCCESS.getCode().equals(resp.getErrorCode())) {
			return true;
		}
		return false;
	}

	private String convetUrl(String urlSuffix, JSONObject json) {
		return getHost() + urlSuffix + "?token=" + MD5Utils.getMD5(urlSuffix + json.toString() + getSecretKey());
	}

	private String getSpecificConfigPrefix() {
		return getBaseConfigPrefix() + getParkingVendorName() + ".";
	}

	private String getBaseConfigPrefix() {
		return "parking.hkws.";
	}

	private String getHost() {
		return configurationProvider.getValue(UserContext.getCurrentNamespaceId(),
				getSpecificConfigPrefix() + "host", "http://10.1.10.37:80");
	}

	private String getSecretKey() {
		return configurationProvider.getValue(UserContext.getCurrentNamespaceId(),
				getSpecificConfigPrefix() + "secretKey", "71aaeaf9687b48f5bc95ae3f8cf91d08");
	}

	private String getOpUserUuid(boolean forcedUpdate) {
		
		String key = getOpUserUuidRedisKey();
		if (!forcedUpdate) {
			String uuid =  getRedisValue(key);
			if (null!= uuid) {
				return uuid;
			}
		}

		String newUuid = getOpUserUuidPost();
		if (StringUtils.isBlank(newUuid)) {
			throwError(ParkingErrorCode.ERROR_HKWS_FETCH_OP_USER_UUID, "opUserUuid fetch error");
		}

		setRedisValue(key, newUuid, 365, TimeUnit.DAYS);
		return newUuid;
	}
	
	private String getOpUserUuidRedisKey() {
		return getSpecificConfigPrefix() + "opUserUuid";
	}

	private String getAppyKey() {
		return configurationProvider.getValue(UserContext.getCurrentNamespaceId(), getSpecificConfigPrefix() + "appKey",
				"028c85ec");
	}
	
	private String getParkUuid() {
		return configurationProvider.getValue(UserContext.getCurrentNamespaceId(), getSpecificConfigPrefix() + "parkUuid",
				"06dfa3ed3a5a4309bd087fd2625ea00e");
	}

	private void throwError(int errorCode, String errorMsg) {
		throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE_HKWS, errorCode, errorMsg);
	}
	
	private String getRedisValue(String key) {
		return  getRedisTemplate(key).opsForValue().get(key);
	}
	
	private void setRedisValue(String key, String value, long time, TimeUnit timeUnit) {
		getRedisTemplate(key).opsForValue().set(key, value, time, timeUnit);
	}
	
	@SuppressWarnings("unchecked")
	private RedisTemplate<String, String> getRedisTemplate(String key) {
		Accessor acc = bigCollectionProvider.getMapAccessor(key, "");
		return acc.getTemplate(new StringRedisSerializer());
	}

}
