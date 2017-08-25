package com.everhomes.parking.handler;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.parking.ParkingLot;
import com.everhomes.parking.ParkingRechargeOrder;
import com.everhomes.parking.ParkingVendorHandler;
import com.everhomes.parking.xiaomao.XiaomaoCard;
import com.everhomes.parking.xiaomao.XiaomaoJsonEntity;
import com.everhomes.rest.parking.*;
import com.everhomes.util.RuntimeErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

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

        XiaomaoCard card = getCard(plateNumber);

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

    private XiaomaoCard getCard(String plateNumber){
        JSONObject param = new JSONObject();

        String parkId = configProvider.getValue("parking.xiaomao.parkId", "");

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

        XiaomaoCard card = getCard(order.getPlateNumber());

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

        List<ParkingRechargeRateDTO> result = new ArrayList<>();
        ParkingRechargeRateDTO dto = new ParkingRechargeRateDTO();
        dto.setOwnerId(parkingLot.getOwnerId());
        dto.setOwnerType(parkingLot.getOwnerType());
        dto.setParkingLotId(parkingLot.getId());
        dto.setRateToken("1");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("count", 1);
        String scope = ParkingNotificationTemplateCode.SCOPE;
        int code = ParkingNotificationTemplateCode.DEFAULT_RATE_NAME;
        String locale = "zh_CN";
        String rateName = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
        dto.setRateName(rateName);
        dto.setCardType("月卡");
        dto.setMonthCount(new BigDecimal(1));
        dto.setPrice(new BigDecimal(1));
        dto.setVendorName(ParkingLotVendor.KETUO2.getCode());

        result.add(dto);
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

        String parkId = configProvider.getValue("parking.xiaomao.parkId", "");

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
    public ParkingRechargeRateDTO createParkingRechargeRate(CreateParkingRechargeRateCommand cmd) {
        LOGGER.error("Not support create parkingRechargeRate.");
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_UNSUPPORTED_USAGE,
                "Not support create parkingRechargeRate.");
    }

    @Override
    public void deleteParkingRechargeRate(DeleteParkingRechargeRateCommand cmd) {
        LOGGER.error("Not support delete parkingRechargeRate.");
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_UNSUPPORTED_USAGE,
                "Not support delete parkingRechargeRate.");
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
            ParkingCardType type = new ParkingCardType();
            type.setTypeId("02");
            type.setTypeName("VIP月卡");
            cardTypes.add(type);

        }else if(parkingId == 10012L) {
            ParkingCardType type = new ParkingCardType();
            type.setTypeId("11");
            type.setTypeName("VIP月卡");
            cardTypes.add(type);
            type = new ParkingCardType();
            type.setTypeId("5");
            type.setTypeName("普通月卡");
            cardTypes.add(type);
        }

        return cardTypes;
    }

    @Override
    public void updateParkingRechargeOrderRate(ParkingRechargeOrder order) {

    }

    @Override
    public ParkingTempFeeDTO getParkingTempFee(ParkingLot parkingLot, String plateNumber) {
        return null;
    }

    @Override
    public OpenCardInfoDTO getOpenCardInfo(GetOpenCardInfoCommand cmd) {
        return null;
    }

    @Override
    public ParkingCarLockInfoDTO getParkingCarLockInfo(GetParkingCarLockInfoCommand cmd) {
        return null;
    }

    @Override
    public void lockParkingCar(LockParkingCarCommand cmd) {

    }

    @Override
    public GetParkingCarNumsResponse getParkingCarNums(GetParkingCarNumsCommand cmd) {
        return null;
    }

}
