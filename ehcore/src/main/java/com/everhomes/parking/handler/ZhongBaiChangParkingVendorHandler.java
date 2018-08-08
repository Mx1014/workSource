// @formatter:off
package com.everhomes.parking.handler;

import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

import com.alibaba.fastjson.TypeReference;
import com.everhomes.parking.*;
import com.everhomes.parking.zhongbaichang.ZhongBaiChangCardInfo;
import com.everhomes.parking.zhongbaichang.ZhongBaiChangData;
import com.everhomes.parking.zhongbaichang.ZhongBaiChangSignatureUtil;
import com.everhomes.rest.parking.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.StringHelper;

/**
 * 广兴源停车
 */
// "ZHONGBAICHANG"需与ParkingLotVendor.ZHONGBAICHANG的枚举值保持一致
@Component(ParkingVendorHandler.PARKING_VENDOR_PREFIX + "ZHONG_BAI_CHANG")
public class ZhongBaiChangParkingVendorHandler extends DefaultParkingVendorHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(ZhongBaiChangParkingVendorHandler.class);

	@Override
	public List<ParkingCardDTO> listParkingCardsByPlate(ParkingLot parkingLot, String plateNumber) {
		ZhongBaiChangCardInfo<ZhongBaiChangData> entity = getCardInfo(plateNumber);
		List<ParkingCardDTO> resultList = new ArrayList<>();
		if (entity != null && entity.isSuccess() && entity.getData() != null) {
			ParkingCardDTO parkingCardDTO = new ParkingCardDTO();

			// 格式yyyyMMddHHmmss
			String validEnd = entity.getData().getEndTime();
			Long endTime = Utils.strToLong(validEnd, Utils.DateStyle.DATE_TIME);

			setCardStatus(parkingLot, endTime, parkingCardDTO);// 这里设置过期可用，正常可用

			parkingCardDTO.setOwnerType(parkingLot.getOwnerType());
			parkingCardDTO.setOwnerId(parkingLot.getOwnerId());
			parkingCardDTO.setParkingLotId(parkingLot.getId());

			parkingCardDTO.setPlateOwnerName(entity.getData().getMemberName());// 车主名称
			parkingCardDTO.setPlateNumber(entity.getData().getCarNo());// 车牌号
			parkingCardDTO.setEndTime(endTime);

			ParkingCardType cardType = createDefaultCardType();
			parkingCardDTO.setCardTypeId(cardType.getTypeId());
			parkingCardDTO.setCardType(cardType.getTypeName());
			parkingCardDTO.setCardNumber(entity.getData().getMemberUuid());
			resultList.add(parkingCardDTO);
		}
		return resultList;
	}

	private ZhongBaiChangCardInfo<ZhongBaiChangData> getCardInfo(String plateNumber) {
		String url = configProvider.getValue("parking.guangxinyuan.url", "http://187k01282j.iask.in");
		String url_context = configProvider.getValue("parking.guangxinyuan.getcard.url_context",
				"/index/member/queryMemBerByCarNo.action");
		String trade_code = configProvider.getValue("parking.guangxinyuan.trade_code", "YYZZ_GXYS4672_GXYT7374");
		String secretKey = configProvider.getValue("parking.guangxinyuan.secretKey", "YYZZ_GXYS4672_GXYT7374");

		Map params = new HashMap<>();
		params.put("trade_code", trade_code);
		params.put("car_no", plateNumber);
		params.put("signature", ZhongBaiChangSignatureUtil.getSign(params, secretKey));
		String result = "";
		int maxTryPosts = configProvider.getIntValue("parking.max.trypost",3);
		int i=0;
		while(i<maxTryPosts) {
			try {
				result = Utils.post(url + url_context, JSONObject.parseObject(StringHelper.toJsonString(params)),
						StandardCharsets.UTF_8);
				break;
			} catch (Exception e) {
				LOGGER.error("The request error,", e);
				if(i==maxTryPosts-1) {
					return null;
				}
			}
			i++;
		}

		ZhongBaiChangCardInfo<ZhongBaiChangData> entity = JSONObject.parseObject(result,
				new TypeReference<ZhongBaiChangCardInfo<ZhongBaiChangData>>() {
				});
		return entity;
	}

	// public static void main(String[] args) {
	// ZhongBaiChangParkingVendorHandler h = new
	// ZhongBaiChangParkingVendorHandler();
	// h.listParkingCardsByPlate(null, null);
	// // h.notifyParkingRechargeOrderPayment(null);
	// }

	@Override
	public List<ParkingRechargeRateDTO> getParkingRechargeRates(ParkingLot parkingLot, String plateNumber,
			String cardNo) {
		List<ParkingRechargeRate> parkingRechargeRateList = parkingProvider
				.listParkingRechargeRates(parkingLot.getOwnerType(), parkingLot.getOwnerId(), parkingLot.getId(), null);
		ListCardTypeResponse response = listCardType(null);

		List<ParkingRechargeRateDTO> result = parkingRechargeRateList.stream().map(r -> {
			ParkingRechargeRateDTO dto = ConvertHelper.convert(r, ParkingRechargeRateDTO.class);
			populaterate(response.getCardTypes(), dto, r);
			return dto;
		}).collect(Collectors.toList());

		return result;

	}

	private void populaterate(List<ParkingCardType> types, ParkingRechargeRateDTO dto, ParkingRechargeRate r) {
		ParkingCardType temp = null;
		for (ParkingCardType t : types) {
			if (t.getTypeId().equals(r.getCardType())) {
				temp = t;
			}
		}
		dto.setCardTypeId(temp.getTypeId());
		dto.setCardType(temp.getTypeName());
		dto.setRateToken(r.getId().toString());
		dto.setVendorName(ParkingLotVendor.ZHONG_BAI_CHANG.getCode());
	}

	@Override
	public Boolean notifyParkingRechargeOrderPayment(ParkingRechargeOrder order) {
		if (order.getRechargeType().equals(ParkingRechargeType.MONTHLY.getCode())) {
			return rechargeMonthlyCard(order);
		}
		LOGGER.info("unknown type = " + order.getRechargeType());
		return false;
	}

	private Boolean rechargeMonthlyCard(ParkingRechargeOrder order) {
		ZhongBaiChangCardInfo<ZhongBaiChangData> card = getCardInfo(order.getPlateNumber());

		if (card != null && card.isSuccess() && card.getData() != null) {
			String url = configProvider.getValue("parking.guangxinyuan.url", "http://187k01282j.iask.in");
			String url_context = configProvider.getValue(
					"parking.guangxinyuan.recharge.url_context", "/index/member/update.action");
			String trade_code = configProvider.getValue("parking.guangxinyuan.trade_code", "YYZZ_GXYS4672_GXYT7374");
			String secretKey = configProvider.getValue("parking.guangxinyuan.secretKey", "YYZZ_GXYS4672_GXYT7374");

			Map params = new HashMap();
			params.put("trade_code", trade_code);
			params.put("car_no", card.getData().getCarNo());
			params.put("member_name", card.getData().getMemberName());
			params.put("telephone", card.getData().getTelephone());
			params.put("start_date", card.getData().getStartTime());
			long newStartTime = Utils.strToLong(card.getData().getEndTime(),Utils.DateStyle.DATE_TIME);
			long now = System.currentTimeMillis();
			if (newStartTime < now) {
				newStartTime = now;
			}

			// 根据充值的几个月，重新计算月卡结束日期
			//这里加一秒钟，原因是自然月只会计算时间到 月末那一天的 23:59:59 秒
			//下一充值，需要从 月初第一天的 00:00:00 开始计算
			Timestamp rechargeEndTimestamp = Utils.getTimestampByAddThisMonth(newStartTime, order.getMonthCount().intValue());
			String endTimestr = Utils.dateToStr(rechargeEndTimestamp, Utils.DateStyle.DATE_TIME);

			params.put("end_date", endTimestr);
			params.put("remark", null);
			params.put("signature", ZhongBaiChangSignatureUtil.getSign(params, secretKey));
			order.setStartPeriod(new Timestamp(newStartTime));
			order.setEndPeriod(rechargeEndTimestamp);
			String result = null;
			int maxTryPosts = configProvider.getIntValue("parking.max.trypost",3);
			int i=0;
			while(i<maxTryPosts) {
				try {
					result = Utils.post(url + url_context, JSONObject.parseObject(StringHelper.toJsonString(params)),
							StandardCharsets.UTF_8);
					break;
				} catch (Exception e) {
					LOGGER.error("The request times{} error,", i+1, e);
					if(i==maxTryPosts-1){
						return false;
					}
				}
				i++;
			}
			//将充值信息存入订单
			order.setErrorDescriptionJson(result);

			
			ZhongBaiChangCardInfo<ZhongBaiChangData> entity = JSONObject.parseObject(result,
					new TypeReference<ZhongBaiChangCardInfo<ZhongBaiChangData>>() {
					});
			order.setErrorDescription(entity!=null?entity.getErrorMsg():null);
			return  entity != null && entity.isSuccess();
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
	public ParkingExpiredRechargeInfoDTO getExpiredRechargeInfo(ParkingLot parkingLot, GetExpiredRechargeInfoCommand cmd) {
		List<ParkingRechargeRateDTO> parkingRechargeRates = getParkingRechargeRates(parkingLot, null, null);
		if(parkingRechargeRates==null || parkingRechargeRates.size()==0){
			return null;
		}
		Integer expiredRechargeMonthCount = parkingLot.getExpiredRechargeMonthCount();
		if(expiredRechargeMonthCount==null){
			expiredRechargeMonthCount=REQUEST_MONTH_COUNT;
		}
		ParkingRechargeRateDTO rate = null;
		for (ParkingRechargeRateDTO parkingRechargeRate : parkingRechargeRates) {
			if(parkingRechargeRate.getMonthCount().intValue()==expiredRechargeMonthCount){
				rate = parkingRechargeRate;
				break;
			}
		}
		if(rate==null) {
			rate = parkingRechargeRates.get(configProvider.getIntValue("parking.recharge.rateseq", 0));
		}
		ParkingExpiredRechargeInfoDTO dto = ConvertHelper.convert(rate,ParkingExpiredRechargeInfoDTO.class);
		dto.setCardTypeName(rate.getCardType());
		ZhongBaiChangCardInfo<ZhongBaiChangData> card = getCardInfo(cmd.getPlateNumber());
		if (card != null && card.isSuccess() && card.getData() != null) {
			long newStartTime = Utils.strToLong(card.getData().getEndTime(),Utils.DateStyle.DATE_TIME);
			long now = System.currentTimeMillis();
			if (newStartTime < now) {
				newStartTime = now;
			}
			dto.setStartPeriod(newStartTime);
			Timestamp rechargeEndTimestamp = Utils.getTimestampByAddThisMonth(newStartTime, rate.getMonthCount().intValue());
			dto.setEndPeriod(rechargeEndTimestamp.getTime());
		}
		return dto;
	}

	@Override
	public void updateParkingRechargeOrderRate(ParkingLot parkingLot, ParkingRechargeOrder order) {
		super.updateParkingRechargeOrderRateInfo(parkingLot, order);
	}

}
