// @formatter:off
package com.everhomes.parking.handler;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.flow.FlowCase;
import com.everhomes.parking.ParkingCardRequest;
import com.everhomes.parking.ParkingCardRequestType;
import com.everhomes.parking.ParkingFlow;
import com.everhomes.parking.ParkingLot;
import com.everhomes.parking.ParkingRechargeOrder;
import com.everhomes.parking.ParkingVendorHandler;
import com.everhomes.parking.dashi.DashiCarLocation;
import com.everhomes.parking.dashi.DashiEmptyPlaceFloorInfo;
import com.everhomes.parking.dashi.DashiEmptyPlaceInfo;
import com.everhomes.parking.dashi.DashiJsonEntity;
import com.everhomes.parking.ketuo.KetuoCard;
import com.everhomes.parking.ketuo.KetuoCardRate;
import com.everhomes.parking.ketuo.KetuoCardType;
import com.everhomes.parking.ketuo.KetuoRequestConfig;
import com.everhomes.rest.contentserver.UploadCsFileResponse;
import com.everhomes.rest.parking.*;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.MD5Utils;
import com.everhomes.util.WebTokenGenerator;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

/**
 * 深圳湾 停车对接
 */
@Component(ParkingVendorHandler.PARKING_VENDOR_PREFIX + "Mybay")
public class KetuoMybayParkingVendorHandler extends KetuoParkingVendorHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(KetuoMybayParkingVendorHandler.class);

	private static final String GET_EMPTY_PLACE = "/ParkingApi/QueryEmptyPlace";
	private static final String GET_CAR_LOCATION = "/ParkingApi/ReverseForCar";
	private static final String GET_PARKING_INFO = "/ParkingApi/GetParkingInfo";
	private static final String ADD_MONTH_CARD = "/api/card/AddCarCard";

	@Autowired
	private ContentServerService contentServerService;

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

	@Override
	public ParkingExpiredRechargeInfoDTO getExpiredRechargeInfo(ParkingLot parkingLot, GetExpiredRechargeInfoCommand cmd) {
		KetuoCard card = getCard(cmd.getPlateNumber());
		if (card == null) {
			return null;
		}
		List<ParkingRechargeRateDTO> parkingRechargeRates = getParkingRechargeRates(parkingLot, null, null);
		if(parkingRechargeRates==null || parkingRechargeRates.size()==0){
			return null;
		}

		ParkingRechargeRateDTO targetRateDTO = null;
		String cardTypeId = card.getCarType();
		for (ParkingRechargeRateDTO rateDTO : parkingRechargeRates) {
			if (rateDTO.getCardTypeId().equals(cardTypeId) && rateDTO.getMonthCount().intValue()==parkingLot.getExpiredRechargeMonthCount()) {
				targetRateDTO = rateDTO;
				break;
			}
		}

		if (null == targetRateDTO) {
			parkingRechargeRates.sort((r1,r2)->r1.getMonthCount().compareTo(r2.getMonthCount()));
			for (ParkingRechargeRateDTO rateDTO : parkingRechargeRates) {
				if (rateDTO.getCardTypeId().equals(cardTypeId)) {
					targetRateDTO = rateDTO;
					break;
				}
			}
		}
		if (null == targetRateDTO) {
			return null;
		}

		ParkingExpiredRechargeInfoDTO dto = ConvertHelper.convert(targetRateDTO,ParkingExpiredRechargeInfoDTO.class);
		dto.setCardTypeName(targetRateDTO.getCardType());
		if (card != null  && card.getValidTo() != null) {
			String expireDate = card.getValidTo();
			long newStartTime = strToLong(expireDate);
			dto.setStartPeriod(newStartTime);
			Timestamp rechargeEndTimestamp = Utils.getTimestampByAddDistanceMonthV2(newStartTime, parkingLot.getExpiredRechargeMonthCount());
			dto.setEndPeriod(rechargeEndTimestamp.getTime());
			dto.setMonthCount(new BigDecimal(parkingLot.getExpiredRechargeMonthCount()));
			dto.setRateName(parkingLot.getExpiredRechargeMonthCount()+configProvider.getValue("parking.ketuo.rateName","个月"));
			dto.setPrice(targetRateDTO.getPrice().divide(targetRateDTO.getMonthCount(),OPEN_CARD_RETAIN_DECIMAL, RoundingMode.HALF_UP)
					.multiply(new BigDecimal(parkingLot.getExpiredRechargeMonthCount())));
		}
		return dto;
	}

	@Override
	protected boolean rechargeMonthlyCard(ParkingRechargeOrder order) {
		return rechargeMonthlyCard(order,KetuoParkingVendorHandler.ADD_DISTANCE_MONTH);
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

		param.put("cardName", "");
		param.put("name", order.getPlateOwnerName());
		param.put("tel", order.getPayerPhone());
		param.put("areaInfo", "");
		//申请类型从申请记录中获取
		param.put("carType", request.getCardTypeId());
		param.put("validFrom", startTime);
		param.put("validTo", endTime);
		param.put("addUser", "");
		param.put("carNo", order.getPlateNumber());
		param.put("carColor", "");

		String json = post(param, ADD_MONTH_CARD);

		//将充值信息存入订单
		order.setErrorDescriptionJson(json);
		order.setStartPeriod(tempStart);
		order.setEndPeriod(tempEnd);

		JSONObject jsonObject = JSONObject.parseObject(json);
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
		if (StringUtils.isBlank(cardTypeId)) {
			List<ParkingCardRequestType> types = parkingProvider.listParkingCardTypes(parkingCardRequest.getOwnerType(),
					parkingCardRequest.getOwnerId(), parkingCardRequest.getParkingLotId());
			cardTypeId = types.get(0).getCardTypeId();
		}

		//月租车
		List<KetuoCardRate> rates = getCardRule(cardTypeId);
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
			dto.setPrice(new BigDecimal(rate.getRuleMoney()).divide(new BigDecimal(100), OPEN_CARD_RETAIN_DECIMAL, RoundingMode.HALF_UP));

			dto.setPlateNumber(cmd.getPlateNumber());
			long now = configProvider.getLongValue("parking.opencard.now",System.currentTimeMillis());
			dto.setOpenDate(now);
			dto.setExpireDate(Utils.getTimestampByAddDistanceMonthV2(now, requestMonthCount).getTime());
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
	
	private DashiCarLocation getDashiCarLocation(String plateNumber) {

		String url = configProvider.getValue("parking.mybay.searchCar.url", "");
		TreeMap<String, String> param = new TreeMap<>();
		param.put("CarNo", plateNumber);
		String json = Utils.post(url + GET_CAR_LOCATION, createRequestParam(param));

		DashiJsonEntity<DashiCarLocation> entity = JSONObject.parseObject(json, new TypeReference<DashiJsonEntity<DashiCarLocation>>(){});

		if (entity.isSuccess()) {

			List<DashiCarLocation> carLocations = entity.getData();

			if (null != carLocations && !carLocations.isEmpty()) {
				DashiCarLocation temp = carLocations.get(0);
				DashiCarLocation result = getDashiParkingInfo(temp.getSpaceCode());

				if (null != result && StringUtils.isNotBlank(result.getParkingPhoto())) {
					String token = WebTokenGenerator.getInstance().toWebToken(UserContext.current().getLogin().getLoginToken());
					String fileName = "parking-" + System.currentTimeMillis() + ".jpg";

					final Base64.Decoder decoder = Base64.getDecoder();
					try {
						// Base64解码
						byte[] bytes = decoder.decode(result.getParkingPhoto().split(",")[1]);
						InputStream is = new ByteArrayInputStream(bytes);
						UploadCsFileResponse fileResp = contentServerService.uploadFileToContentServer(is, fileName, token);

						if(fileResp.getErrorCode() == 0) {
							result.setParkingPhoto(fileResp.getResponse().getUrl());
						}
					} catch (Exception e) {
						LOGGER.error("Parking parse image error");
					}
				}
				return result;
			}
		}
		return null;
	}

	private DashiCarLocation getDashiParkingInfo(String spaceNo) {

		String url = configProvider.getValue("parking.mybay.searchCar.url", "");

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

		String url = configProvider.getValue("parking.mybay.searchCar.url", "");

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

		String md5Sign = MD5Utils.getMD5(p);
		params.put("SignString", md5Sign);

		return params;
	}

}
