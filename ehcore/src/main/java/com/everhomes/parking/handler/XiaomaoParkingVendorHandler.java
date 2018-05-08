package com.everhomes.parking.handler;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.everhomes.parking.*;
import com.everhomes.parking.xiaomao.XiaomaoCard;
import com.everhomes.parking.xiaomao.XiaomaoJsonEntity;
import com.everhomes.parking.yinxingzhijiexiaomao.YinxingzhijieXiaomaoTempCard;
import com.everhomes.rest.parking.*;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.MD5Utils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 小猫停车对接
 * Created by zhengsiting on 2017/8/16.
 */
@Component(ParkingVendorHandler.PARKING_VENDOR_PREFIX + "XIAOMAO")
public class XiaomaoParkingVendorHandler extends DefaultParkingVendorHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(XiaomaoParkingVendorHandler.class);

    private static final String GET_CARD = "/park/getMonthCard";
    //办理月卡，续费
    private static final String OPEN_CARD = "/park/openMonthCard";
    
    //临时车缴费单查询
    private static final String CHARGING = "/park/charging";
    
    //缴费通知
    private static final String POSTCHARGE = "/park/postCharge";
    
    

    private static final int SUCCESS = 1;

    @Override
    public List<ParkingCardDTO> listParkingCardsByPlate(ParkingLot parkingLot, String plateNumber) {
        List<ParkingCardDTO> resultList = new ArrayList<>();

        XiaomaoCard card = getCard(plateNumber, parkingLot.getId());

        if(null != card){
            Date expireDate = card.getEndTime();

            long expireTime = expireDate.getTime();

            ParkingCardDTO parkingCardDTO = convertCardInfo(parkingLot);
            setCardStatus(parkingLot, expireTime, parkingCardDTO);

            parkingCardDTO.setPlateNumber(plateNumber);
            parkingCardDTO.setPlateOwnerName(card.getUserName());
            //parkingCardDTO.setStartTime(startTime);
            parkingCardDTO.setEndTime(expireTime);

            List<ParkingCardType> types = getCardTypes(parkingLot.getId());
            String cardTypeName = null;
            for (ParkingCardType type: types) {
                if (type.getTypeId().equals(card.getMemberType())) {
                    cardTypeName = type.getTypeName();
                }
            }
            parkingCardDTO.setCardType(cardTypeName);
            parkingCardDTO.setCardTypeId(card.getMemberType());

            resultList.add(parkingCardDTO);
        }

        return resultList;
    }

    private XiaomaoCard getCard(String plateNumber, Long parkingId){
        JSONObject param = new JSONObject();

        param.put("licenseNumber",plateNumber);
        String jsonString = post(param,GET_CARD, parkingId);

        XiaomaoCard card = JSONObject.parseObject(jsonString, XiaomaoCard.class);
        if (card.getFlag() == SUCCESS){
            return card;
        }
        return null;
    }

    private boolean rechargeMonthlyCard(ParkingRechargeOrder order){

        XiaomaoCard card = getCard(order.getPlateNumber(), order.getParkingLotId());

        if (null != card) {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Long expireTime = card.getEndTime().getTime();

            Timestamp timestampStart = Utils.addSecond(expireTime, 1);
            Timestamp timestampEnd = Utils.getTimestampByAddNatureMonth(expireTime, order.getMonthCount().intValue());
            String validStart = sdf.format(timestampStart);
            String validEnd = sdf.format(timestampEnd);

            JSONObject param = new JSONObject();
            param.put("parkName", card.getParkName());
            param.put("memberType", card.getMemberType());
            param.put("licenseNumber", order.getPlateNumber());
            param.put("beginTime", validStart);
            param.put("endTime", validEnd);

            String json = post(param, OPEN_CARD, order.getParkingLotId());

            //将充值信息存入订单
            order.setErrorDescriptionJson(json);
            order.setStartPeriod(timestampStart);
            order.setEndPeriod(timestampEnd);

            XiaomaoJsonEntity entity = JSONObject.parseObject(json, XiaomaoJsonEntity.class);
            if (entity.getFlag() == SUCCESS){
                return true;
            }
        }
        return false;
    }

    public String post(JSONObject param, String type, Long parkingLotId) {
        String url = configProvider.getValue("parking.xiaomao.url", "") + type;
        String parkId = configProvider.getValue("parking.xiaomao.parkId." + parkingLotId, "");
        String keyId = configProvider.getValue("parking.xiaomao.accessKeyId."+parkingLotId, "");
        String key = configProvider.getValue("parking.xiaomao.accessKeyValue."+parkingLotId, "");
        
        param.put("parkId", parkId);
        param.put("accessKeyId", keyId);

        String sign = getSign(param, key);
        param.put("sign",sign);
        String result = Utils.post(url, param);
        LOGGER.info("get card info param:"+param.toJSONString()+" result:"+result);
        return result;

    }

    public String getSign(JSONObject param,String key){
        StringBuilder stringBuilder = new StringBuilder();
        Iterator iterator = param.entrySet().iterator();
        TreeMap map = new TreeMap();
        while (iterator.hasNext()){
            Map.Entry entry = (Map.Entry)iterator.next();
            map.put(entry.getKey(),entry.getValue());
        }
       Iterator iterator2 =  map.entrySet().iterator();
        while(iterator2.hasNext()){
            Map.Entry<String,String> entry = (Map.Entry)iterator2.next();
            stringBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        stringBuilder.append("accessKeyValue=").append(key);
        String s = stringBuilder.toString();
        return MD5Utils.getMD5(s);
    }

    @Override
    public List<ParkingRechargeRateDTO> getParkingRechargeRates(ParkingLot parkingLot, String plateNumber, String cardNo) {

        List<ParkingRechargeRate> parkingRechargeRateList;

        if(StringUtils.isBlank(plateNumber)) {
            parkingRechargeRateList = parkingProvider.listParkingRechargeRates(parkingLot.getOwnerType(), parkingLot.getOwnerId(),
                    parkingLot.getId(), null);
        }else{
            XiaomaoCard card = getCard(plateNumber, parkingLot.getId());
            String cardType = card.getMemberType();
            parkingRechargeRateList = parkingProvider.listParkingRechargeRates(parkingLot.getOwnerType(), parkingLot.getOwnerId(),
                    parkingLot.getId(), cardType);
        }

        List<ParkingCardType> types = getCardTypes(parkingLot.getId());

        List<ParkingRechargeRateDTO> result = parkingRechargeRateList.stream().map(r->{
            ParkingRechargeRateDTO dto = ConvertHelper.convert(r, ParkingRechargeRateDTO.class);

            populaterate(types, dto, r);
            return dto;
        }).collect(Collectors.toList());

        return result;
    }

    private void populaterate(List<ParkingCardType> types, ParkingRechargeRateDTO dto, ParkingRechargeRate r) {
        ParkingCardType temp = null;
        for(ParkingCardType t: types) {
            if(t.getTypeId().equals(r.getCardType())) {
                temp = t;
            }
        }
        dto.setCardTypeId(temp.getTypeId());
        dto.setCardType(temp.getTypeName());
        dto.setRateToken(r.getId().toString());
        dto.setVendorName(ParkingLotVendor.XIAOMAO.getCode());
    }

    @Override
	public Boolean notifyParkingRechargeOrderPayment(ParkingRechargeOrder order) {
		if (order.getOrderType().equals(ParkingOrderType.RECHARGE.getCode())) {
			if (order.getRechargeType().equals(ParkingRechargeType.MONTHLY.getCode())) {
				//月卡缴费
				return rechargeMonthlyCard(order);
			}

			if (order.getRechargeType().equals(ParkingRechargeType.TEMPORARY.getCode())) {
				//临时车缴费
				return payTempCardFee(order);
			}

			return false;
		}

		// 开通月卡
		if (order.getOrderType().equals(ParkingOrderType.OPEN_CARD.getCode())) {
			return addMonthCard(order);
		}

		LOGGER.info("unknown type = " + order.getRechargeType());
		return false;
	}

    private boolean addMonthCard(ParkingRechargeOrder order){

        ParkingCardRequest request;
        if (null != order.getCardRequestId()) {
            request = parkingProvider.findParkingCardRequestById(order.getCardRequestId());
        }else {
            request = getParkingCardRequestByOrder(order);
        }

        if (request == null) {
            return false;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        //精确到秒,毫秒记为0
        calendar.set(Calendar.MILLISECOND, 0);
        //获取到当前时间减一秒，计算有效期 开始时间在这个时间上加一秒，结束时间一般是在某一天的23:59:59
        long tempTime = calendar.getTimeInMillis() - 1000L;

        Timestamp timestampStart = Utils.addSecond(tempTime, 1);
        Timestamp timestampEnd = Utils.getTimestampByAddNatureMonth(tempTime, order.getMonthCount().intValue());
        String validStart = sdf.format(timestampStart);
        String validEnd = sdf.format(timestampEnd);

        JSONObject param = new JSONObject();
        param.put("memberType", request.getCardTypeId());
        param.put("licenseNumber", order.getPlateNumber());
        param.put("beginTime", validStart);
        param.put("endTime", validEnd);
        
        param.put("userName", request.getPlateOwnerName());
        param.put("userTel", request.getPlateOwnerPhone());
        param.put("companyName", request.getPlateOwnerEntperiseName());

        //TODO:测试
        boolean flag = configProvider.getBooleanValue("parking.order.amount", false);
        String json;
        if (flag) {
            json = "{\"flag\":1, \"message\":\"hhh\"}";
        }else {
            json = post(param, OPEN_CARD, order.getParkingLotId());
        }

        //将充值信息存入订单
        order.setErrorDescriptionJson(json);
        order.setStartPeriod(timestampStart);
        order.setEndPeriod(timestampEnd);

        XiaomaoJsonEntity entity = JSONObject.parseObject(json, XiaomaoJsonEntity.class);
        if (entity.getFlag() == SUCCESS){
            updateFlowStatus(request);
            return true;
        }
        return false;
    }

    @Override
    public ListCardTypeResponse listCardType(ListCardTypeCommand cmd) {

        ListCardTypeResponse response = new ListCardTypeResponse();

        Long parkingId = cmd.getParkingLotId();

        response.setCardTypes(getCardTypes(parkingId));
        return response;
    }

    private List<ParkingCardType> getCardTypes(Long parkingId) {
        List<ParkingCardType> cardTypes = new ArrayList<>();
        //初始化的停车场id eh_parking_lots表 10011  10012
        if (parkingId == 10011L) {
            String json = configProvider.getValue("parking.xiaomao.types." + parkingId, "");
            List<ParkingCardType> types = JSONArray.parseArray(json, ParkingCardType.class);
            cardTypes.addAll(types);

//            ParkingCardType type = new ParkingCardType();
//            type.setTypeId("02");
//            type.setTypeName("VIP月卡");
//            cardTypes.add(type);

        }else if(parkingId == 10012L) {
            String json = configProvider.getValue("parking.xiaomao.types." + parkingId, "");
            List<ParkingCardType> types = JSONArray.parseArray(json, ParkingCardType.class);
            cardTypes.addAll(types);

//            ParkingCardType type = new ParkingCardType();
//            type.setTypeId("11");
//            type.setTypeName("VIP月卡");
//            cardTypes.add(type);
//            type = new ParkingCardType();
//            type.setTypeId("5");
//            type.setTypeName("普通月卡");
//            cardTypes.add(type);
        }

        return cardTypes;
    }

    @Override
    public void updateParkingRechargeOrderRate(ParkingLot parkingLot, ParkingRechargeOrder order) {
        updateParkingRechargeOrderRateInfo(parkingLot, order);

    }

    ParkingRechargeRateDTO getOpenCardRate(ParkingCardRequest parkingCardRequest) {

        ParkingRechargeRate rate = parkingProvider.findParkingRechargeRateByMonthCount(parkingCardRequest.getOwnerType(), parkingCardRequest.getOwnerId(),
                parkingCardRequest.getParkingLotId(), parkingCardRequest.getCardTypeId(), new BigDecimal(1));

        if (null == rate) {
            //TODO:
            return null;
        }
        List<ParkingCardType> types = getCardTypes(parkingCardRequest.getParkingLotId());
        ParkingRechargeRateDTO dto = ConvertHelper.convert(rate, ParkingRechargeRateDTO.class);

        populaterate(types, dto, rate);

        return dto;
    }
    
    @Override
    public ParkingTempFeeDTO getParkingTempFee(ParkingLot parkingLot, String plateNumber) {
        JSONObject param = new JSONObject();
        param.put("plateNo",plateNumber);
        String result = post(param, CHARGING, parkingLot.getId());
        YinxingzhijieXiaomaoTempCard tempCard = JSONObject.parseObject(result, new TypeReference<YinxingzhijieXiaomaoTempCard>() {});
        ParkingTempFeeDTO dto = new ParkingTempFeeDTO();
        
        if (null == tempCard) {
        	tempCard = new YinxingzhijieXiaomaoTempCard();
        } 
        
        if(!tempCard.isSuccess()) {
        	long nowTime = System.currentTimeMillis();
        	long startTime = nowTime - 1000*60*10; //十分钟之前入场
        	long chargeTime = nowTime - 1000*60*9; //九分钟之前计费
        	
        	tempCard.setStartTime(Utils.longToString(startTime, Utils.DateStyle.DATE_TIME));
        	tempCard.setChargTime(Utils.longToString(chargeTime, Utils.DateStyle.DATE_TIME));
        	tempCard.setTimeOut(15); //缴费之后15分钟内必须要离开
        	tempCard.setShouldPay("0.00");
        	tempCard.setOrderId("hmb_test_orderId"+nowTime);
        	tempCard.setFlag(1);
        }
        
        if(tempCard != null  && tempCard.isSuccess()) {
            dto.setPlateNumber(plateNumber);
            dto.setEntryTime(Utils.strToLong(tempCard.getStartTime(),Utils.DateStyle.DATE_TIME));
            long now = System.currentTimeMillis();
            dto.setPayTime(now);
            dto.setParkingTime(Integer.valueOf(String.valueOf((now-dto.getEntryTime())/60000L)));
            dto.setDelayTime(155);
            dto.setPrice(new BigDecimal(tempCard.getShouldPay()));
            boolean flag = configProvider.getBooleanValue("parking.order.amount", false);
            if(flag) {
                dto.setPrice(new BigDecimal(0.01));
            }
            //feestate	缴费状态	int	0=无数据;2=免缴费;3=已缴费未超时;4=已缴费需续费;5=需要缴费
            //这里需要干嘛，缴费完成，回调的时候，读取这个token，向停车场缴费需要这个参数。参考 payTempCardFee()
            dto.setOrderToken(tempCard.getOrderId());
            dto.setRemainingTime(tempCard.getTimeOut());
        }
        return dto;

    }    
    
    private boolean payTempCardFee(ParkingRechargeOrder order) {
    	
        JSONObject params = new JSONObject();
        params.put("plateNo", order.getPlateNumber());
        params.put("orderId", order.getOrderToken());
        params.put("payMoney", order.getPrice().setScale(2).toString());
        String result = post(params, POSTCHARGE, order.getParkingLotId());
        order.setErrorDescriptionJson(result);//缴费结果储存

        JSONObject jsonObject = JSONObject.parseObject(result);
        Object obj = jsonObject.get("flag");
        order.setErrorDescription(jsonObject.getString("message"));
        if(null != obj ) {
            int resCode = (int) obj;
            if(resCode == 1) {
                return true;
            }
        }
        return false;
    }
    
    
    
}
