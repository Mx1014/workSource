// @formatter:off
package com.everhomes.parking.handler;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.everhomes.parking.ParkingLot;
import com.everhomes.parking.ParkingRechargeOrder;
import com.everhomes.parking.ParkingVendorHandler;
import com.everhomes.parking.kexinxiaomao.KexinXiaomaoCard;
import com.everhomes.parking.kexinxiaomao.KexinXiaomaoCardType;
import com.everhomes.parking.kexinxiaomao.KexinXiaomaoJsonEntity;
import com.everhomes.parking.kexinxiaomao.KexinXiaomaoSignUtil;
import com.everhomes.rest.parking.ListCardTypeCommand;
import com.everhomes.rest.parking.ListCardTypeResponse;
import com.everhomes.rest.parking.ParkingCardDTO;
import com.everhomes.rest.parking.ParkingCardType;
import com.everhomes.rest.parking.ParkingLotVendor;
import com.everhomes.rest.parking.ParkingRechargeRateDTO;
import com.everhomes.rest.parking.ParkingRechargeType;
import com.everhomes.util.StringHelper;

/*
 * 正中会 正中时代广场停车场
 * // "KEXIN_XIAOMAO"需与ParkingLotVendor.KEXIN_XIAOMAO的枚举值保持一致
 */
@Component(ParkingVendorHandler.PARKING_VENDOR_PREFIX + "KEXIN_XIAOMAO")
public class XiaomaoKeXingParkingVendorHandler extends DefaultParkingVendorHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(XiaomaoKeXingParkingVendorHandler.class);

//	private static final String HANDLER_MONTHCARD = "/park/handleMonthCard";// 新增月卡接口
	private static final String OPEN_MONTHCARD = "/park/openMonthCard";// 月卡续费接口
//	private static final String GET_MONTHCARD_TYPE = "/park/getMonthCardType";// 获取月卡类型接口
	private static final String GET_MONTHCARD = "/park/getMonthCard";// 月卡查询

	@Override
	public List<ParkingCardDTO> listParkingCardsByPlate(ParkingLot parkingLot, String plateNumber) {
		KexinXiaomaoCard entity = getCardInfo(plateNumber);
		List<ParkingCardDTO> resultList = new ArrayList<>();
		if (entity != null && entity.isSuccess()) {
			ParkingCardDTO parkingCardDTO = new ParkingCardDTO();

			// 格式yyyyMMddHHmmss
			String validEnd = entity.getEndTime();
			Long endTime = Utils.strToLong(validEnd, Utils.DateStyle.DATE_TIME);

			setCardStatus(parkingLot, endTime, parkingCardDTO);// 这里设置过期可用，正常可用

			parkingCardDTO.setOwnerType(parkingLot.getOwnerType());
			parkingCardDTO.setOwnerId(parkingLot.getOwnerId());
			parkingCardDTO.setParkingLotId(parkingLot.getId());

			parkingCardDTO.setPlateOwnerName(entity.getUserName());// 车主名称
			parkingCardDTO.setPlateNumber(entity.getLicenseNumber());// 车牌号
			parkingCardDTO.setEndTime(endTime);
			
			parkingCardDTO.setCardTypeId(entity.getMemberType());//月卡类型id
			parkingCardDTO.setCardType(entity.getStandardType());//月卡类型名称？
			parkingCardDTO.setCardName(entity.getStandardType());
			resultList.add(parkingCardDTO);
		}
		return resultList;
	}

	private KexinXiaomaoCard getCardInfo(String plateNumber) {
		TreeMap<String,String> params = new TreeMap<String,String>();
		params.put("licenseNumber", plateNumber);
		String result = post(GET_MONTHCARD, params);
		KexinXiaomaoCard entity = JSONObject.parseObject(result, new TypeReference<KexinXiaomaoCard>(){});
		return entity;
	}

	@Override
	public List<ParkingRechargeRateDTO> getParkingRechargeRates(ParkingLot parkingLot, String plateNumber,
			String cardNo) {
		KexinXiaomaoCard card = getCardInfo(plateNumber);
		List<ParkingRechargeRateDTO> dtos = new ArrayList<>();
		if (card == null || !card.isSuccess()) {
			return dtos;
		}
		
//    	TreeMap<String, String> params = new TreeMap<String,String>();
//    	String result = post(GET_MONTHCARD_TYPE, params);
//		LOGGER.info("Card type from kexin xiaomao={}", result);
//        
//		KexinXiaomaoJsonEntity<List<KexinXiaomaoCardType>> entity = JSONObject.parseObject(result, new TypeReference<KexinXiaomaoJsonEntity<List<KexinXiaomaoCardType>>>(){});
//		if(entity.isSuccess()){
//			for (KexinXiaomaoCardType cardType : entity.getData()) {
//				if(card.getMemberType().equals(cardType.getStandardId())){
		ParkingRechargeRateDTO dto = new ParkingRechargeRateDTO();
		dto.setCardTypeId(card.getMemberType());
        dto.setCardType(card.getStandardType());
        dto.setRateToken(card.getMemberType());
        dto.setRateName("1个月");
        dto.setPrice(new BigDecimal(card.getUnitMoney()));
        dto.setMonthCount(new BigDecimal(1));
        dto.setVendorName(ParkingLotVendor.KEXIN_XIAOMAO.getCode());
		dto.setParkingLotId(parkingLot.getId());
		dto.setOwnerType(parkingLot.getOwnerType());
		dto.setOwnerId(parkingLot.getOwnerId());
		dtos.add(dto);
//				}
//			}
//		}
		return dtos;
    }

	@Override
	public Boolean notifyParkingRechargeOrderPayment(ParkingRechargeOrder order) {
		if (order.getRechargeType().equals(ParkingRechargeType.MONTHLY.getCode())) {
			return rechargeMonthlyCard(order);
		}
		LOGGER.info("unknown type = " + order.getRechargeType());
		return false;
	}
	
    private boolean rechargeMonthlyCard(ParkingRechargeOrder order){

    	KexinXiaomaoCard card = getCardInfo(order.getPlateNumber());

        if (null != card && card.isSuccess()) {
            Timestamp timestampStart = new Timestamp(Utils.strToLong(card.getEndTime(), Utils.DateStyle.DATE_TIME)+1000);
            Timestamp timestampEnd = Utils.getTimestampByAddNatureMonth(timestampStart.getTime(), order.getMonthCount().intValue());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            
            String validStart = sdf.format(timestampStart);
            String validEnd = sdf.format(timestampEnd);

            TreeMap<String, String> params = new TreeMap<String,String>();
            params.put("memberType", card.getMemberType());
            params.put("beginTime", validStart);
            params.put("endTime", validEnd);
            params.put("licenseNumber", card.getLicenseNumber());

            String json = post(OPEN_MONTHCARD,params);

            //将充值信息存入订单
            order.setErrorDescriptionJson(json);
            order.setStartPeriod(timestampStart);
            order.setEndPeriod(timestampEnd);

            KexinXiaomaoJsonEntity<?> entity = JSONObject.parseObject(json, KexinXiaomaoJsonEntity.class);
            if (entity.isSuccess()){
                return true;
            }
        }
        return false;
    }

	@Override
	public ListCardTypeResponse listCardType(ListCardTypeCommand cmd) {
    	ListCardTypeResponse ret = new ListCardTypeResponse();
    	
//    	TreeMap<String, String> params = new TreeMap<String,String>();
//    	String result = post(GET_MONTHCARD_TYPE, params);
//		LOGGER.info("Card type from kexin xiaomao={}", result);
//        
//		KexinXiaomaoJsonEntity<List<KexinXiaomaoCardType>> entity = JSONObject.parseObject(result, new TypeReference<KexinXiaomaoJsonEntity<List<KexinXiaomaoCardType>>>(){});
//        List<ParkingCardType> list = new ArrayList<>();
//		if(entity.isSuccess()){
//			for (KexinXiaomaoCardType cardType : entity.getData()) {
//				ParkingCardType parkingCardType = new ParkingCardType();
//				parkingCardType.setTypeId(cardType.getStandardId());
//				parkingCardType.setTypeName(cardType.getStandardType());
//				list.add(parkingCardType);
//			}
//			ret.setCardTypes(list);
//		}
    	return ret;
    }

	@Override
	public void updateParkingRechargeOrderRate(ParkingLot parkingLot, ParkingRechargeOrder order) {
	}
	
	private String post(String context,TreeMap<String,String> params){
		String url = configProvider.getValue("parking.kexinxiaomao.url", "");
		String parkId = configProvider.getValue("parking.kexinxiaomao.parkId", "");
		String accessKeyId = configProvider.getValue("parking.kexinxiaomao.accessKeyId", "");
		String accessKeyValue = configProvider.getValue("parking.kexinxiaomao.accessKeyValue", "");
		
		params.put("parkId", parkId);
		params.put("accessKeyId", accessKeyId);
		params.put("sign", KexinXiaomaoSignUtil.getSign(params, accessKeyValue));
		return  Utils.post(url + context, JSONObject.parseObject(StringHelper.toJsonString(params)),
				StandardCharsets.UTF_8);
	}
}
