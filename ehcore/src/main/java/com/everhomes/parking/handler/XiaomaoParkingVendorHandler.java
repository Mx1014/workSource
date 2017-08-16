package com.everhomes.parking.handler;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.parking.ParkingRechargeOrder;
import com.everhomes.parking.ParkingVendorHandler;
import com.everhomes.parking.ketuo.EncryptUtil;
import com.everhomes.parking.xiaomao.XiaomaoCard;
import com.everhomes.rest.parking.*;
import com.everhomes.util.RuntimeErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 小猫停车对接
 * Created by zhengsiting on 2017/8/16.
 */
@Component(ParkingVendorHandler.PARKING_VENDOR_PREFIX + "XIAOMAO")
public class XiaomaoParkingVendorHandler implements ParkingVendorHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(XiaomaoParkingVendorHandler.class);

    private static final String GET_CARD = "/park/getMonthCard";

    @Autowired
    private ConfigurationProvider configProvider;

    @Override
    public GetParkingCardsResponse getParkingCardsByPlate(String ownerType, Long ownerId, Long parkingLotId, String plateNumber) {
        List<ParkingCardDTO> resultList = new ArrayList<ParkingCardDTO>();
        GetParkingCardsResponse response = new GetParkingCardsResponse();
        response.setCards(resultList);

        XiaomaoCard card = getCard(parkingLotId,plateNumber);

        return null;
    }

    private XiaomaoCard getCard(Long parkingLotId, String plateNumber){
        XiaomaoCard card = null;
        JSONObject param = new JSONObject();

        param.put("parkId",parkingLotId);
        param.put("licenseNumber",plateNumber);
        String jsonString = post(param,GET_CARD);
        JSONObject object = JSONObject.parseObject(jsonString);
        if (object.getInteger("flag")==1){
            card = new XiaomaoCard();
            card.setBeginTime(object.getString("beginTime"));
            card.setEndTime(object.getString("endTime"));
            card.setCreateTime(object.getString("createTime"));

        }
        return null;
    }

    public String post(JSONObject param, String type) {
        String url = configProvider.getValue("parking.xiaomao.url", "") + type;
        String keyId = configProvider.getValue("parking.xiaomao.keyId", "");
        String key = configProvider.getValue("parking.xiaomao.key", "");
        param.put("accessKeyId",keyId);

        String sign = getSign(param,key);
        param.put("sign",sign);
        String result = Utils.post(url,param);
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
            stringBuilder.append(entry.getKey()+"="+entry.getValue()+"&");
        }
        stringBuilder.append("accessKeyValue="+key);
        String s =stringBuilder.toString();
        return EncryptUtil.md5Encode(s);
    }

    @Override
    public List<ParkingRechargeRateDTO> getParkingRechargeRates(String ownerType, Long ownerId, Long parkingLotId, String palteNumber, String cardNo) {
        return null;
    }

    @Override
    public Boolean notifyParkingRechargeOrderPayment(ParkingRechargeOrder order) {
        return null;
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
        return null;
    }

    @Override
    public void updateParkingRechargeOrderRate(ParkingRechargeOrder order) {

    }

    @Override
    public ParkingTempFeeDTO getParkingTempFee(String ownerType, Long ownerId, Long parkingLotId, String plateNumber) {
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

    @Override
    public boolean recharge(ParkingRechargeOrder order) {
        return false;
    }

    public static void main(String[] args){

    }
}
