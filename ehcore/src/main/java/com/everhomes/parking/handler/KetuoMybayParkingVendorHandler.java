// @formatter:off
package com.everhomes.parking.handler;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.parking.ParkingLot;
import com.everhomes.parking.ParkingVendorHandler;
import com.everhomes.parking.dashi.DashiCarLocation;
import com.everhomes.parking.dashi.DashiEmptyPlaceFloorInfo;
import com.everhomes.parking.dashi.DashiEmptyPlaceInfo;
import com.everhomes.parking.dashi.DashiJsonEntity;
import com.everhomes.parking.ketuo.KetuoCard;
import com.everhomes.parking.ketuo.KetuoRequestConfig;
import com.everhomes.rest.parking.GetCarLocationCommand;
import com.everhomes.rest.parking.GetFreeSpaceNumCommand;
import com.everhomes.rest.parking.ParkingCarLocationDTO;
import com.everhomes.rest.parking.ParkingFreeSpaceNumDTO;
import com.everhomes.util.ConvertHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 深圳湾 停车对接
 */
@Component(ParkingVendorHandler.PARKING_VENDOR_PREFIX + "Mybay")
public class KetuoMybayParkingVendorHandler extends Ketuo2ParkingVendorHandler{
	private static final Logger LOGGER = LoggerFactory.getLogger(KetuoMybayParkingVendorHandler.class);

	private static final String GET_EMPTY_PLACE = "/ParkingApi/QueryEmptyPlace";
	private static final String GET_CAR_LOCATION = "/ParkingApi/ReverseForCar";
	private static final String GET_PARKING_INFO = "/ParkingApi/GetParkingInfo";

	@Autowired
    private ConfigurationProvider configProvider;

	static String url = "http://szdas.iok.la:17508";

	@Override
	public KetuoCard getCard(String plateNumber) {
		KetuoCard card = super.getCard(plateNumber);

		//深圳湾月卡没有对接免费金额,设置成0
		if (null != card) {
			card.setFreeMoney(0);
		}
		return card;
	}

	protected KetuoRequestConfig getKetuoRequestConfig() {

		String url = configProvider.getValue("parking.mybay.url", "");
		String key = configProvider.getValue("parking.mybay.key", "");
		String user = configProvider.getValue("parking.mybay.user", "");
		String pwd = configProvider.getValue("parking.mybay.pwd", "");

		KetuoRequestConfig config = new KetuoRequestConfig();
		config.setUrl(url);
		config.setKey(key);
		config.setUser(user);
		config.setPwd(pwd);

		return config;
	}

	@Override
	public ParkingFreeSpaceNumDTO getFreeSpaceNum(GetFreeSpaceNumCommand cmd) {

		List<DashiEmptyPlaceFloorInfo> floorPlaceInfos = getDashiEmptyPlaceFloorInfos();
		if (null != floorPlaceInfos) {
			ParkingFreeSpaceNumDTO dto = ConvertHelper.convert(cmd, ParkingFreeSpaceNumDTO.class);
			int count = 0;
			for (DashiEmptyPlaceFloorInfo d: floorPlaceInfos) {
				count += Integer.valueOf(d.getParkingVacancy());
			}
			dto.setFreeSpaceNum(count);
			return dto;
		}
		return null;
	}

	private DashiCarLocation getDashiCarLocation(String plateNumber) {
		TreeMap<String, String> param = new TreeMap<>();
		param.put("CarNo", plateNumber);
		String json = Utils.post(url + GET_CAR_LOCATION, createRequestParam(param));

		DashiJsonEntity<DashiCarLocation> entity = JSONObject.parseObject(json, new TypeReference<DashiJsonEntity<DashiCarLocation>>(){});

		if (entity.isSuccess()) {

			List<DashiCarLocation> carLocations = entity.getData();

			if (null != carLocations && !carLocations.isEmpty()) {
				DashiCarLocation temp = carLocations.get(0);
				DashiCarLocation result = getDashiParkingInfo(temp.getSpaceCode());
				return result;
			}
		}
		return null;
	}

	private DashiCarLocation getDashiParkingInfo(String spaceNo) {
		TreeMap<String, String> param = new TreeMap<>();
		param.put("SpaceCode", spaceNo);
		String json = Utils.post(url + GET_PARKING_INFO, createRequestParam(param));

		DashiJsonEntity<DashiCarLocation> entity = JSONObject.parseObject(json, new TypeReference<DashiJsonEntity<DashiCarLocation>>(){});

		if (entity.isSuccess()) {

			List<DashiCarLocation> carLocations = entity.getData();

			if (null != carLocations && !carLocations.isEmpty()) {
				return carLocations.get(0);
			}
		}
		return null;
	}

	private List<DashiEmptyPlaceFloorInfo> getDashiEmptyPlaceFloorInfos() {
		String json = Utils.post(url + GET_EMPTY_PLACE, createRequestParam(new TreeMap<>()));

		DashiJsonEntity<DashiEmptyPlaceInfo> entity = JSONObject.parseObject(json, new TypeReference<DashiJsonEntity<DashiEmptyPlaceInfo>>(){});

		if (entity.isSuccess()) {

			List<DashiEmptyPlaceInfo> parkingPlaceInfos = entity.getData();

			if (null != parkingPlaceInfos && !parkingPlaceInfos.isEmpty()) {
				List<DashiEmptyPlaceFloorInfo> floorPlaceInfos = parkingPlaceInfos.get(0).getEmptyPlaceFloorInfo();
				if (null != floorPlaceInfos && !floorPlaceInfos.isEmpty()) {
					return floorPlaceInfos;
				}
			}
		}
		return null;
	}

	@Override
	public ParkingCarLocationDTO getCarLocation(ParkingLot parkingLot, GetCarLocationCommand cmd) {
		DashiCarLocation dashiCarLocation = getDashiCarLocation(cmd.getPlateNumber());

		if (null != dashiCarLocation) {
			ParkingCarLocationDTO dto = ConvertHelper.convert(cmd, ParkingCarLocationDTO.class);
			dto.setSpaceNo(dashiCarLocation.getSpaceCode());
			dto.setParkingName(parkingLot.getName());
			dto.setLocation(dashiCarLocation.getParkingPlace());
			dto.setParkingTime(dashiCarLocation.getParkingLength());

			String entryTime = dashiCarLocation.getParkingStartTime();
			if (null != entryTime) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				try {
					dto.setEntryTime(sdf.parse(entryTime).getTime());
				} catch (ParseException e) {
					LOGGER.error("Parking parse entryTime error, cmd={}, dashiCarLocation={}", cmd, dashiCarLocation);
				}
			}

			dto.setCarImageUrl(dashiCarLocation.getParkingPhoto());

			return dto;
		}

		return null;
	}

	private static Map<String, String> createRequestParam(TreeMap<String, String> params) {

		params.put("TimeStamp", String.valueOf(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)));

		params.put("From", "zl_parking");

		StringBuilder sb = new StringBuilder();
		params.forEach((key, value) -> sb.append(key).append("=").append(value).append("&"));
		String p = sb.substring(0,sb.length() - 1);

		String md5Sign = Utils.md5(p);
		params.put("SignString", md5Sign);

		return params;
	}

}
