// @formatter:off
package com.everhomes.parking.handler;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.db.DbProvider;
import com.everhomes.flow.*;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.parking.*;
import com.everhomes.parking.ketuo.*;
import com.everhomes.rest.flow.FlowAutoStepDTO;
import com.everhomes.rest.flow.FlowStepType;
import com.everhomes.rest.organization.VendorType;
import com.everhomes.rest.parking.*;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 科兴 正中会 停车对接
 */
@Component(ParkingVendorHandler.PARKING_VENDOR_PREFIX + "KETUO2")
public class KetuoKexingParkingVendorHandler extends Ketuo2ParkingVendorHandler{
	private static final Logger LOGGER = LoggerFactory.getLogger(KetuoKexingParkingVendorHandler.class);

	@Autowired
    private ConfigurationProvider configProvider;
	@Autowired
	private LocaleStringService localeStringService;

	private static final boolean isSupportOpenCard = true;

	private static final String GET_PARKINGS = "/api/find/GetParkingLotList";
	private static final String GET_FREE_SPACE_NUM = "/api/find/GetFreeSpaceNum";
	private static final String GET_CAR_LOCATION = "/api/find/GetCarLocInfo";

	String appId = "1";
	String appkey = "b20887292a374637b4a9d6e9f940b1e6";
	String url = "http://220.160.111.114:8099";
	Integer parkingId = 1;

	//支持开卡，返回true
	public boolean getOpenCardFlag() {
		return true;
	}

	public KetuoRequestConfig getKetuoRequestConfig() {

		String url = configProvider.getValue("parking.kexing.url", "");
		String key = configProvider.getValue("parking.kexing.key", "");
		String user = configProvider.getValue("parking.kexing.user", "");
		String pwd = configProvider.getValue("parking.kexing.pwd", "");

		KetuoRequestConfig config = new KetuoRequestConfig();
		config.setUrl(url);
		config.setKey(key);
		config.setUser(user);
		config.setPwd(pwd);

		return config;
	}

	@Override
	public ParkingFreeSpaceNumDTO getFreeSpaceNum(GetFreeSpaceNumCommand cmd) {

		ParkingFreeSpaceNumDTO dto = ConvertHelper.convert(cmd, ParkingFreeSpaceNumDTO.class);

		KexingFreeSpaceNum kexingFreeSpaceNum = getKexingFreeSpaceNum();

		if (null != kexingFreeSpaceNum) {
			dto.setFreeSpaceNum(kexingFreeSpaceNum.getFreeSpaceNum());

			return dto;
		}
		return null;
	}

	private KexingFreeSpaceNum getKexingFreeSpaceNum() {

		LinkedHashMap<String, Object> param = new LinkedHashMap<>();
		param.put("parkId", parkingId);

		JSONObject params = createRequestParam(param);
		String json = Utils.post(url + GET_FREE_SPACE_NUM, params);

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

		String str = sb.append(sdf.format(new Date())).append(appkey).toString();
		String key = Utils.md5(str);

		JSONObject params = new JSONObject();
		params.put("appId", appId );
		params.put("key", key );

		for (Map.Entry entry: entries) {
			params.put((String)entry.getKey(), entry.getValue());
		}

		return params;
	}

	private List<KetuoParking> getKetuoParkings() {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

		String str = sdf.format(new Date()) + appkey;
		String key = Utils.md5(str);

		JSONObject params = new JSONObject();
		params.put("appId", appId );
		params.put("key", key );
		String json = Utils.post(url + GET_PARKINGS, params);

		KetuoJsonEntity<KetuoParking> entity = JSONObject.parseObject(json, new TypeReference<KetuoJsonEntity<KetuoParking>>(){});
		if(entity.isSuccess()){
			List<KetuoParking> list = entity.getData();
			if(null != list && !list.isEmpty()) {
				return list;
			}
		}
		return new ArrayList<>();
	}

	@Override
	public ParkingCarLocationDTO getCarLocation(ParkingLot parkingLot, GetCarLocationCommand cmd) {
		ParkingCarLocationDTO dto = ConvertHelper.convert(cmd, ParkingCarLocationDTO.class);

		KetuoCarLocation ketuoCarLocation = getCarLocation(cmd.getPlateNumber());

		if (null != ketuoCarLocation) {
			dto.setSpaceNo(ketuoCarLocation.getSpaceNo());
			dto.setParkingName(parkingLot.getName());
			dto.setFloorName(ketuoCarLocation.getFloorName());
			dto.setLocation(ketuoCarLocation.getArea());

			String scope = ParkingNotificationTemplateCode.SCOPE;
			int code = ParkingNotificationTemplateCode.DEFAULT_MINUTE_UNIT;
			String locale = Locale.SIMPLIFIED_CHINESE.toString();
			String unit = localeStringService.getLocalizedString(scope, String.valueOf(code), locale, "");
			dto.setParkingTime(ketuoCarLocation.getParkTime() + unit);

			String entryTime = ketuoCarLocation.getInTime();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			try {
				dto.setEntryTime(sdf.parse(entryTime).getTime());
			} catch (ParseException e) {
				LOGGER.error("Parking parse entryTime error, cmd={}, ketuoCarLocation={}", cmd, ketuoCarLocation);
			}
			dto.setCarImageUrl(ketuoCarLocation.getCarImage());

			return dto;
		}
		return null;
	}

	private KetuoCarLocation getCarLocation(String plateNumber) {
		LinkedHashMap<String, Object> param = new LinkedHashMap<>();
		param.put("parkId", parkingId);
		param.put("plateNo", plateNumber);

		JSONObject params = createRequestParam(param);
		String json = Utils.post(url + GET_CAR_LOCATION, params);

		KetuoJsonEntity<KetuoCarLocation> entity = JSONObject.parseObject(json, new TypeReference<KetuoJsonEntity<KetuoCarLocation>>(){});

		if(entity.isSuccess()){
			List<KetuoCarLocation> list = entity.getData();
			if(null != list && !list.isEmpty()) {
				return list.get(0);
			}
		}
		return null;
	}
}
