// @formatter:off
package com.everhomes.parking.handler;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.organization.pm.pay.GsonUtil;
import com.everhomes.parking.*;
import com.everhomes.parking.bosigao2.ParkWebService;
import com.everhomes.parking.bosigao2.ParkWebServiceSoap;
import com.everhomes.parking.bosigao2.rest.Bosigao2CardInfo;
import com.everhomes.parking.bosigao2.rest.Bosigao2GetCardCommand;
import com.everhomes.parking.bosigao2.rest.Bosigao2RechargeCommand;
import com.everhomes.parking.bosigao2.rest.Bosigao2ResultEntity;
import com.everhomes.parking.clearance.ParkingClearanceLog;
import com.everhomes.rest.parking.*;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;
import org.apache.commons.lang.StringUtils;
import org.jooq.util.derby.sys.Sys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 深业 停车
 */
@Component(ParkingVendorHandler.PARKING_VENDOR_PREFIX + "TEST")
public class TestParkingVendorHandler extends DefaultParkingVendorHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(TestParkingVendorHandler.class);

	
	@Override
    public List<ParkingCardDTO> listParkingCardsByPlate(ParkingLot parkingLot, String plateNumber) {
        
    	List<ParkingCardDTO> resultList = new ArrayList<>();


        ParkingCardDTO parkingCardDTO = new ParkingCardDTO();

		if (plateNumber.startsWith("粤B")) {
			parkingCardDTO.setCardStatus(ParkingCardStatus.NORMAL.getCode());

			parkingCardDTO.setOwnerType(parkingLot.getOwnerType());
			parkingCardDTO.setOwnerId(parkingLot.getOwnerId());
			parkingCardDTO.setParkingLotId(parkingLot.getId());

			parkingCardDTO.setPlateOwnerName("测试");
			parkingCardDTO.setPlateNumber(plateNumber);

			long expireTime = Utils.strToLong("20171029235959", Utils.DateStyle.DATE_TIME_STR);

			parkingCardDTO.setEndTime(expireTime);
			parkingCardDTO.setCardType("普通月卡");
			parkingCardDTO.setCardNumber("");
			parkingCardDTO.setPlateOwnerPhone("12345679812");
			parkingCardDTO.setIsValid(true);

			resultList.add(parkingCardDTO);
		}


        
        return resultList;
    }

    @Override
	public ListCardTypeResponse listCardType(ListCardTypeCommand cmd) {
    	ListCardTypeResponse ret = new ListCardTypeResponse();

		String json = configProvider.getValue("parking.default.card.type", "");
		List<ParkingCardType> list = new ArrayList<>();
		if(json.startsWith("{")) {
			ParkingCardType cardType = JSONObject.parseObject(json, ParkingCardType.class);
			list.add(cardType);
			ret.setCardTypes(list);
		}else if(json.startsWith("[")){
			JSONArray array = JSONObject.parseArray(json);
			for (Object o : array) {
				ParkingCardType cardType = JSONObject.parseObject(o.toString(),ParkingCardType.class);
				list.add(cardType);
			}
		}
		ret.setCardTypes(list);

    	return ret;
    }

	@Override
	public void updateParkingRechargeOrderRate(ParkingLot parkingLot, ParkingRechargeOrder order) {
		updateParkingRechargeOrderRateInfo(parkingLot, order);

	}

	@Override
	public ParkingTempFeeDTO getParkingTempFee(ParkingLot parkingLot, String plateNumber) {
		ParkingTempFeeDTO dto = new ParkingTempFeeDTO();
		dto.setPlateNumber(plateNumber);
		dto.setPrice(new BigDecimal(configProvider.getValue("parking.test.prices","1")));
//		dto.setOriginalPrice(new BigDecimal("1"));
		dto.setPayTime(System.currentTimeMillis());
		dto.setEntryTime(System.currentTimeMillis() - 5000000);
		dto.setOrderToken("11");
		dto.setParkingTime(1000);
		dto.setDelayTime(1);
		return dto;
	}

    @Override
    public List<ParkingRechargeRateDTO> getParkingRechargeRates(ParkingLot parkingLot,String plateNumber,String cardNo) {
    	List<ParkingRechargeRate> parkingRechargeRateList;

    	if(StringUtils.isBlank(plateNumber)) {
    		parkingRechargeRateList = parkingProvider.listParkingRechargeRates(parkingLot.getOwnerType(), parkingLot.getOwnerId(),
					parkingLot.getId(),null);
    		
    	}else{
    		String cardType = "普通月卡";
    		parkingRechargeRateList = parkingProvider.listParkingRechargeRates(parkingLot.getOwnerType(), parkingLot.getOwnerId(),
					parkingLot.getId(),cardType);
    	}
		List<ParkingRechargeRateDTO> result = parkingRechargeRateList.stream().map(r->{

			ParkingRechargeRateDTO dto = ConvertHelper.convert(r, ParkingRechargeRateDTO.class);
			dto.setCardTypeId("普通月卡");
			dto.setRateToken(String.valueOf(r.getId()));
			dto.setVendorName(ParkingLotVendor.TEST.getCode());
			return dto;
		}
		).collect(Collectors.toList());
		
		return result;
    }

    @Override
    public Boolean notifyParkingRechargeOrderPayment(ParkingRechargeOrder order) {
		boolean notifyresult = configProvider.getBooleanValue("parking.test.notifyresult", true);
		return notifyresult;

	}

	ParkingRechargeRateDTO getOpenCardRate(ParkingCardRequest parkingCardRequest) {
		LOGGER.info(TestParkingVendorHandler.class.getName());

		ParkingRechargeRate rate = parkingProvider.findParkingRechargeRateByMonthCount(parkingCardRequest.getOwnerType(), parkingCardRequest.getOwnerId(),
				parkingCardRequest.getParkingLotId(), parkingCardRequest.getCardTypeId(), new BigDecimal(1));

		if (null == rate) {
			//TODO:
			return null;
		}
		ParkingRechargeRateDTO dto = ConvertHelper.convert(rate, ParkingRechargeRateDTO.class);

		dto.setCardTypeId("普通月卡");
		dto.setCardType("普通月卡");
		dto.setRateToken(rate.getId().toString());
		dto.setVendorName(ParkingLotVendor.TEST.getCode());

		return dto;
	}

	@Override
	public String applyTempCard(ParkingClearanceLog log) {
		return "tempToken";
	}
}
