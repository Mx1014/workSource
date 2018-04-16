// @formatter:off
package com.everhomes.parking.handler;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.parking.ParkingLot;
import com.everhomes.parking.ParkingRechargeOrder;
import com.everhomes.parking.ParkingRechargeRate;
import com.everhomes.parking.ParkingVendorHandler;
import com.everhomes.parking.guangdawegu.GuangDaWeGuData;
import com.everhomes.parking.guangdawegu.GuangDaWeGuResponse;
import com.everhomes.parking.guangdawegu.GuangDaWeGuSignUtil;
import com.everhomes.parking.guangdawegu.GuangDaWeGuTempCarData;
import com.everhomes.rest.organization.VendorType;
import com.everhomes.rest.parking.*;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;
import com.everhomes.util.Tuple;
import org.jooq.util.derby.sys.Sys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.rmi.CORBA.Util;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 光大we谷停车场
 */
// "GUANG_DA_WE_GU"需与ParkingLotVendor.GUANG_DA_WE_GU的枚举值保持一致
@Component(ParkingVendorHandler.PARKING_VENDOR_PREFIX + "GUANG_DA_WE_GU")
public class GuangDaWeGuParkingVendorHandler extends DefaultParkingVendorHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(GuangDaWeGuParkingVendorHandler.class);


	private static final String GET_CAR_INFO = "/WechatPay-pay/wechat/car/getCarInfo";//获取车辆信息接口
	private static final String PARKING_GET_PAY_INFO = "/WechatPay-pay/wechat/parking/getPayInfo";//查询临保车缴费信息接口
	private static final String PARKING_PAY_PARKING = "/WechatPay-pay/wechat/parking/payParking";//临保车缴费接口
	private static final String CARD_GET_PAY_INFO = "/WechatPay-pay/wechat/card/getPayInfo";//获取月保车缴费金额
	private static final String CARD_PAY_CARD= "/WechatPay-pay/wechat/card/payCard";//月保车缴费接口

	public static final int TEMPORARY_CAR = 1;//临保车
	public static final int MONTH_CAR = 2;//月保车

//	DateTimeFormatter ymdFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");


	@Override
	public List<ParkingCardDTO> listParkingCardsByPlate(ParkingLot parkingLot, String plateNumber) {
		GuangDaWeGuResponse<GuangDaWeGuData> entity = getCardInfo(plateNumber);
		List<ParkingCardDTO> resultList = new ArrayList<>();
		if (isMonthCard(entity)) {
			ParkingCardDTO parkingCardDTO = new ParkingCardDTO();

			// 格式yyyyMMddHHmmss
			generateMaxStartEndTime(entity.getData(),parkingCardDTO);
			if(parkingCardDTO.getEndTime()==null){
				boolean sptNullEtime = configProvider.getBooleanValue("parking.guangdawegu.sptNullEtime", false);
				if(sptNullEtime){
					parkingCardDTO.setEndTime(System.currentTimeMillis());
				}else {
					return resultList;
				}
			}
			setCardStatus(parkingLot, parkingCardDTO.getEndTime(), parkingCardDTO);// 这里设置过期可用，正常可用

			parkingCardDTO.setOwnerType(parkingLot.getOwnerType());
			parkingCardDTO.setOwnerId(parkingLot.getOwnerId());
			parkingCardDTO.setParkingLotId(parkingLot.getId());

			parkingCardDTO.setPlateOwnerName(entity.getData().getUsername());// 车主名称
			parkingCardDTO.setPlateNumber(entity.getData().getCarnum());// 车牌号

			ParkingCardType cardType = createDefaultCardType();
			parkingCardDTO.setCardTypeId(cardType.getTypeId());
			parkingCardDTO.setCardType(cardType.getTypeName());
			resultList.add(parkingCardDTO);
		}
		return resultList;
	}

	/**
	 *
	 * @param data
	 * @param parkingCardDTO
	 * @return
	 * 			tuple<starttime,endTime></starttime,endTime>
	 */
	private Tuple<Long,Long> generateMaxStartEndTime(GuangDaWeGuData data, ParkingCardDTO parkingCardDTO) {
		if(data.getEnd1() == null && data.getEnd2() == null){
			return null;
		}
		if(data.getEnd1() == null){
			if(parkingCardDTO!=null) {
				parkingCardDTO.setEndTime(data.getEnd2());
				parkingCardDTO.setStartTime(data.getBegin2());
			}
			return new Tuple<>(data.getBegin2(),data.getEnd2());
		}
		if(data.getEnd2() == null){
			if(parkingCardDTO!=null) {
				parkingCardDTO.setEndTime(data.getEnd1());
				parkingCardDTO.setStartTime(data.getBegin1());
			}
			return new Tuple<>(data.getBegin1(),data.getEnd1());
		}
		if(data.getEnd2()>data.getEnd1()){
			if(parkingCardDTO!=null) {
				parkingCardDTO.setEndTime(data.getEnd2());
				parkingCardDTO.setStartTime(data.getBegin2());
			}
			return new Tuple<>(data.getBegin2(),data.getEnd2());
		}else{
			if(parkingCardDTO!=null) {
				parkingCardDTO.setEndTime(data.getEnd1());
				parkingCardDTO.setStartTime(data.getBegin1());
			}
			return new Tuple<>(data.getBegin1(),data.getEnd1());
		}
	}

	private boolean isMonthCard(GuangDaWeGuResponse<GuangDaWeGuData> entity) {
		return  entity != null
				&& entity.isSuccess()
				&& entity.getData() != null
				&& entity.getData().getCarattr()!=null
				&& entity.getData().getCarattr() == MONTH_CAR;
	}

//	public static void main(String[] args) {
//		new GuangDaWeGuParkingVendorHandler().getCardInfo("z1bi3r");
//	}

	private GuangDaWeGuResponse<GuangDaWeGuData> getCardInfo(String plateNumber) {
		TreeMap<String,String> params = new TreeMap();
		params.put("plateNumber", plateNumber);
		String result = post(GET_CAR_INFO,params);

		return JSONObject.parseObject(result,new TypeReference<GuangDaWeGuResponse<GuangDaWeGuData>>(){});
	}

	private String post(String uri,TreeMap<String,String> params){
		String url = configProvider.getValue("parking.guangdawegu.url", "http://120.25.238.52");
		String parkingCode = configProvider.getValue("parking.guangdawegu.parkingCode", "4d398d36-5e63-4e46-807c-2bb2ebd4ad38");
		String appKey = configProvider.getValue("parking.guangdawegu.appKey", "wwwbsznsmartcom20180130");

//		String url = "http://120.25.238.52";
//		String parkingCode = "4d398d36-5e63-4e46-807c-2bb2ebd4ad38";
//		String appKey = "wwwbsznsmartcom20180130";

		params.put("sign", GuangDaWeGuSignUtil.getSign(params, appKey));
		params.put("parkingCode", parkingCode);
		return Utils.post(url+uri, params);
	}


	@Override
	public List<ParkingRechargeRateDTO> getParkingRechargeRates(ParkingLot parkingLot, String plateNumber,
			String cardNo) {
		String month = configProvider.getValue("parking.guangdawegu.monthcardnums","1,3,6");
		List<Integer> monthCounts = new ArrayList<>();
		monthCounts.add(1);
		try {
			monthCounts = Arrays.asList(month.split(",")).stream().map(Integer::valueOf).collect(Collectors.toList());
		}catch (Exception e){
			LOGGER.error("config month {} format error. can't convert int",month);
		}

		List<ParkingRechargeRateDTO> rechargeRateDTOS = new ArrayList<>();
		for (Integer count : monthCounts) {
			TreeMap<String,String> params = new TreeMap();
			params.put("carType", "-1");//对面的预留参数，随便传的
			params.put("monthCount", count+"");
			String result = post(CARD_GET_PAY_INFO,params);
			GuangDaWeGuResponse<Double> entity = JSONObject.parseObject(result, new TypeReference<GuangDaWeGuResponse<Double>>() {});
			if(entity.isSuccess()) {
				rechargeRateDTOS.add(populaterate(entity.getData(), count, parkingLot));
			}
		}
		return rechargeRateDTOS;

	}

	private ParkingRechargeRateDTO populaterate(Double prices, int monthCount,ParkingLot parkingLot) {
		ParkingRechargeRateDTO dto = new ParkingRechargeRateDTO();
		ParkingCardType cardType = createDefaultCardType();
		dto.setCardTypeId(cardType.getTypeId());
		dto.setCardType(cardType.getTypeName());
		dto.setMonthCount(new BigDecimal(monthCount));
		dto.setPrice(new BigDecimal(prices));
		dto.setVendorName(ParkingLotVendor.GUANG_DA_WE_GU.getCode());
		dto.setParkingLotId(parkingLot.getId());
		dto.setOwnerType(parkingLot.getOwnerType());
		dto.setOwnerId(parkingLot.getOwnerId());
		dto.setRateName(monthCount+"个月");
		return dto;
	}

	@Override
	public Boolean notifyParkingRechargeOrderPayment(ParkingRechargeOrder order) {
		if (order.getRechargeType().equals(ParkingRechargeType.MONTHLY.getCode())) {
			return rechargeMonthlyCard(order);
		}
		if(order.getRechargeType().equals(ParkingRechargeType.TEMPORARY.getCode())){
			return payTempCardFee(order);
		}

		return false;
	}

	boolean payTempCardFee(ParkingRechargeOrder order){

		TreeMap<String,String> params = new TreeMap();
		params.put("plateNumber", order.getPlateNumber());
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		params.put("timeFrom", format.format(order.getStartPeriod()));
		params.put("timeTo", format.format(Utils.addSecond(System.currentTimeMillis(),60*order.getDelayTime())));
		params.put("paid", order.getPrice().multiply(new BigDecimal(100)).intValue()+"");//单位 分
		params.put("paytype", "4".equals(order.getOrderToken())?"3":"2");//2=临保缴费 3=临保续费 参考  getParkingTempFee()
		params.put("payway", ""+(VendorType.WEI_XIN.getCode().equals(order.getPaidType()) ? 4 : 5));
		params.put("tradeOutOrder", order.getOrderNo()+"");
		String result = post(PARKING_PAY_PARKING, params);
		order.setErrorDescription(result);//data 缴费记录号 存储

		GuangDaWeGuResponse<Integer> entity = JSONObject.parseObject(result, new TypeReference<GuangDaWeGuResponse<Integer>>() {});
		order.setErrorDescription(entity!=null?entity.getErrorMsg():null);
		if(null != entity && entity.isSuccess()) {
			return true;
		}
		return false;
	}

	private Boolean rechargeMonthlyCard(ParkingRechargeOrder order) {
		GuangDaWeGuResponse<GuangDaWeGuData> entity = getCardInfo(order.getPlateNumber());

		if (isMonthCard(entity)) {
			Tuple<Long, Long> peridTuple = generateMaxStartEndTime(entity.getData(), null);
			Long first,second;

			if(peridTuple == null){
				//支持对方月卡没有时间返回
				boolean sptNullEtime = configProvider.getBooleanValue("parking.guangdawegu.sptNullEtime", false);
				if(sptNullEtime){
					first = System.currentTimeMillis();
					second = first;
				}else{
					LOGGER.info("not sport null end time, peridTuple is null.");
					return false;
				}
			}else{
				first = peridTuple.first();
				second = peridTuple.second();
			}

			ParkingLot parkingLot = parkingProvider.findParkingLotById(order.getParkingLotId());
			Byte isSupportRecharge = parkingLot.getExpiredRechargeFlag();
			if(ParkingConfigFlag.SUPPORT.getCode() == isSupportRecharge){//支持过期缴费
				if(second<System.currentTimeMillis()) {
					second = System.currentTimeMillis();
				}
			}

			TreeMap<String,String> params = new TreeMap();
			params.put("plateNumber", order.getPlateNumber());
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			params.put("begin", format.format(new Date(first)));
			Long newEndTime = Utils.getLongByAddNatureMonth(second+1000, order.getMonthCount().intValue());
			params.put("end", format.format(new Date(newEndTime)));
			params.put("paid", order.getPrice().multiply(new BigDecimal(100)).intValue()+"");//单位是分

			String result = post(CARD_PAY_CARD,params);
			order.setErrorDescription(result); //data 缴费记录号 存储

			GuangDaWeGuResponse<Integer> response = JSONObject.parseObject(result, new TypeReference<GuangDaWeGuResponse<Integer>>() {});
			order.setErrorDescription(response!=null?response.getErrorMsg():null);
			if(response.isSuccess()){
				order.setStartPeriod(new Timestamp(second));
				order.setEndPeriod(new Timestamp(newEndTime));
				return true;
			}
		}
		return false;
	}

	@Override
	public ListCardTypeResponse listCardType(ListCardTypeCommand cmd) {
		ListCardTypeResponse ret = new ListCardTypeResponse();

		List<ParkingCardType> list = new ArrayList<>();
		list.add(createDefaultCardType());
		ret.setCardTypes(list);

		return ret;
	}

	@Override
	public void updateParkingRechargeOrderRate(ParkingLot parkingLot, ParkingRechargeOrder order) {
		List<ParkingRechargeRateDTO> parkingRechargeRates = getParkingRechargeRates(parkingLot, null, null);
		for (ParkingRechargeRateDTO rate : parkingRechargeRates) {
			if(rate.getMonthCount().compareTo(order.getMonthCount())==0){
				order.setRateName(rate.getRateName());
				return ;
			}
		}
		LOGGER.error("order monthCount not found, cmd={}", order);
		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
				"order monthCount not found.");
	}

	@Override
	public ParkingTempFeeDTO getParkingTempFee(ParkingLot parkingLot, String plateNumber) {
		TreeMap<String,String> params = new TreeMap();
		params.put("plateNumber", plateNumber);
		String result = post(PARKING_GET_PAY_INFO, params);


		GuangDaWeGuResponse<GuangDaWeGuTempCarData> entity = JSONObject.parseObject(result, new TypeReference<GuangDaWeGuResponse<GuangDaWeGuTempCarData>>() {});
		ParkingTempFeeDTO dto = new ParkingTempFeeDTO();
		if(entity != null  && entity.isSuccess() && entity.getData()!=null) {
			GuangDaWeGuTempCarData data = entity.getData();
			if(!data.canPayTemporaryCar()) {
				return dto;
			}
			dto.setPlateNumber(plateNumber);
			dto.setEntryTime(data.getIntime());
			long now = System.currentTimeMillis();
			dto.setPayTime(now);
			dto.setParkingTime(calculateParkTime(data.getStaydays(),data.getStayhours(),data.getStayminutes()));
			dto.setDelayTime(15);
			dto.setPrice(new BigDecimal(data.getDues()));
			//feestate	缴费状态	int	0=无数据;2=免缴费;3=已缴费未超时;4=已缴费需续费;5=需要缴费
			//这里需要干嘛，缴费完成，回调的时候，读取这个token，向停车场缴费需要这个参数。参考 payTempCardFee()
			dto.setOrderToken(data.getFeestate()+"");
		}
		return dto;
	}

	private Integer calculateParkTime(Integer staydays, Integer stayhours, Integer stayminutes) {
		int minutes = 0;
		if(staydays!=null && staydays !=0 ){
			minutes += staydays*24*60;
		}
		if(stayhours!=null && stayhours != 0){
			minutes += stayhours*60;
		}
		if(staydays!=null &&stayminutes!=0){
			minutes += stayminutes;
		}
		return minutes;
	}

}
