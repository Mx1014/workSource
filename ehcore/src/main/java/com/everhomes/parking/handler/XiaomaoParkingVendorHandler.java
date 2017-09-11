package com.everhomes.parking.handler;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.parking.ParkingLot;
import com.everhomes.parking.ParkingRechargeOrder;
import com.everhomes.parking.ParkingRechargeRate;
import com.everhomes.parking.ParkingVendorHandler;
import com.everhomes.parking.xiaomao.XiaomaoCard;
import com.everhomes.parking.xiaomao.XiaomaoJsonEntity;
import com.everhomes.rest.parking.*;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Override
    public List<ParkingCardDTO> listParkingCardsByPlate(ParkingLot parkingLot, String plateNumber) {
        List<ParkingCardDTO> resultList = new ArrayList<>();

        XiaomaoCard card = getCard(plateNumber, parkingLot.getId());

        if(null != card){
            Date expireDate = card.getEndTime();

            long expireTime = expireDate.getTime();

            if (checkExpireTime(parkingLot, expireTime)) {
                return resultList;
            }

            ParkingCardDTO parkingCardDTO = convertCardInfo(parkingLot);

            parkingCardDTO.setPlateNumber(plateNumber);

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

        String parkId = configProvider.getValue("parking.xiaomao.parkId." + parkingId, "");

        param.put("parkId", parkId);
        param.put("licenseNumber",plateNumber);
        String jsonString = post(param,GET_CARD);

        XiaomaoCard card = JSONObject.parseObject(jsonString, XiaomaoCard.class);
        if (card.getFlag() == 1){
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
            param.put("parkId", card.getParkId());
            param.put("parkName", card.getParkName());
            param.put("memberType", card.getMemberType());
            param.put("licenseNumber", order.getPlateNumber());
            param.put("beginTime", validStart);
            param.put("endTime", validEnd);

            String json = post(param, OPEN_CARD);

            //将充值信息存入订单
            order.setErrorDescriptionJson(json);
            order.setStartPeriod(timestampStart);
            order.setEndPeriod(timestampEnd);

            XiaomaoJsonEntity entity = JSONObject.parseObject(json, XiaomaoJsonEntity.class);
            if (entity.getFlag() == 1){
                updateFlowStatus(order);
                return true;
            }
        }
        return false;
    }

    public String post(JSONObject param, String type) {
        String url = configProvider.getValue("parking.xiaomao.url", "") + type;
        String keyId = configProvider.getValue("parking.xiaomao.accessKeyId", "");
        String key = configProvider.getValue("parking.xiaomao.accessKeyValue", "");
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
        return Utils.md5(s);
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

            String type = null;
            for(ParkingCardType t: types) {
                if(t.getTypeId().equals(r.getCardType())) {
                    type = t.getTypeName();
                }
            }

            dto.setCardType(type);
            dto.setRateToken(r.getId().toString());
            dto.setVendorName(ParkingLotVendor.XIAOMAO.getCode());
            return dto;
        }).collect(Collectors.toList());

        return result;
    }

    @Override
    public Boolean notifyParkingRechargeOrderPayment(ParkingRechargeOrder order) {
        if (order.getOrderType().equals(ParkingOrderType.RECHARGE.getCode())) {
            if(order.getRechargeType().equals(ParkingRechargeType.MONTHLY.getCode())) {
                return rechargeMonthlyCard(order);
            }
            return false;
        }else {
            return addMonthCard(order);
        }
    }

    private boolean addMonthCard(ParkingRechargeOrder order){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        long tempTime = calendar.getTimeInMillis();

        Timestamp timestampStart = Utils.addSecond(tempTime, 1);
        Timestamp timestampEnd = Utils.getTimestampByAddNatureMonth(tempTime, order.getMonthCount().intValue());
        String validStart = sdf.format(timestampStart);
        String validEnd = sdf.format(timestampEnd);

        String parkId = configProvider.getValue("parking.xiaomao.parkId." + order.getParkingLotId(), "");

        JSONObject param = new JSONObject();
        param.put("parkId", parkId);
        param.put("memberType", "11");
        param.put("licenseNumber", order.getPlateNumber());
        param.put("beginTime", validStart);
        param.put("endTime", validEnd);

        String json = post(param, OPEN_CARD);

        //将充值信息存入订单
        order.setErrorDescriptionJson(json);
        order.setStartPeriod(timestampStart);
        order.setEndPeriod(timestampEnd);

        XiaomaoJsonEntity entity = JSONObject.parseObject(json, XiaomaoJsonEntity.class);
        if (entity.getFlag() == 1){
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
    public void updateParkingRechargeOrderRate(ParkingRechargeOrder order) {

    }

    @Override
    public ParkingFreeSpaceNumDTO getFreeSpaceNum(GetFreeSpaceNumCommand cmd) {
        String handlerPrefix = ParkingVendorHandler.PARKING_VENDOR_PREFIX;
        ParkingVendorHandler handler = PlatformContext.getComponent(handlerPrefix + "Mybay");
        return handler.getFreeSpaceNum(cmd);
    }

    @Override
    public ParkingCarLocationDTO getCarLocation(ParkingLot parkingLot, GetCarLocationCommand cmd) {
        String handlerPrefix = ParkingVendorHandler.PARKING_VENDOR_PREFIX;
        ParkingVendorHandler handler = PlatformContext.getComponent(handlerPrefix + "Mybay");
        return handler.getCarLocation(parkingLot, cmd);
    }
}
