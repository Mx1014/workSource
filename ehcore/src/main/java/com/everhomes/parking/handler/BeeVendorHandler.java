// @formatter:off
package com.everhomes.parking.handler;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.parking.ParkingLot;
import com.everhomes.parking.ParkingRechargeOrder;
import com.everhomes.parking.bee.BeeResponse;
import com.everhomes.parking.bee.BeeUtils;
import com.everhomes.parking.bee.MonthCardEntity;
import com.everhomes.parking.bee.MonthCardInfo;
import com.everhomes.rest.parking.ListCardTypeCommand;
import com.everhomes.rest.parking.ListCardTypeResponse;
import com.everhomes.rest.parking.ParkingCardDTO;
import com.everhomes.rest.parking.ParkingRechargeRateDTO;
import com.everhomes.util.RuntimeErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
  *
  * @Author dengs[shuang.deng@zuolin.com]
  * @Date 2018/5/29 11:30
  */
public abstract class BeeVendorHandler extends DefaultParkingVendorHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(BeeVendorHandler.class);
    private static final String GET_CARD_LIST = "getCardList";

    @Override
    public List<ParkingCardDTO> listParkingCardsByPlate(ParkingLot parkingLot, String plateNumber) {
        MonthCardInfo cardInfo = getCardInfo(plateNumber);
        List<ParkingCardDTO> resultList = new ArrayList<>();
        if (cardInfo != null) {
            ParkingCardDTO parkingCardDTO = new ParkingCardDTO();

            // 格式yyyyMMddHHmmss
            String validEnd = cardInfo.getEnddate();
            Long endTime = Utils.strToLong(validEnd, Utils.DateStyle.DATE_TIME);

            setCardStatus(parkingLot, endTime, parkingCardDTO);// 这里设置过期可用，正常可用

            parkingCardDTO.setOwnerType(parkingLot.getOwnerType());
            parkingCardDTO.setOwnerId(parkingLot.getOwnerId());
            parkingCardDTO.setParkingLotId(parkingLot.getId());

            parkingCardDTO.setPlateOwnerName(cardInfo.getOperatorname());// 车主名称
            parkingCardDTO.setPlateNumber(cardInfo.getCarnumber());// 车牌号
            parkingCardDTO.setEndTime(endTime);

            parkingCardDTO.setCardTypeId(cardInfo.getCardtypeid());
            parkingCardDTO.setCardType(cardInfo.getTypename());
            parkingCardDTO.setCardNumber(cardInfo.getCarnumber());
            resultList.add(parkingCardDTO);
        }
        return resultList;
    }

    @Override
    public List<ParkingRechargeRateDTO> getParkingRechargeRates(ParkingLot parkingLot, String plateNumber, String cardNo) {
        return null;
    }

    @Override
    public Boolean notifyParkingRechargeOrderPayment(ParkingRechargeOrder order) {
        return null;
    }

    @Override
    public ListCardTypeResponse listCardType(ListCardTypeCommand cmd) {
        return null;
    }

    @Override
    public void updateParkingRechargeOrderRate(ParkingLot parkingLot, ParkingRechargeOrder order) {

    }

    private MonthCardInfo getCardInfo(String plateNumber) {
        JSONObject params=new JSONObject();
        params.put("comid","000000771");
        params.put("keyvalue",plateNumber);
        params.put("query","all");
        params.put("pageSize",50);
        BeeResponse result = post(GET_CARD_LIST,params);
        List<MonthCardEntity> entities= JSONObject.parseObject(result.getOutList().toString(), new TypeReference<List<MonthCardEntity>>(){});
        if(entities!=null && entities.size()>0) {
            List<MonthCardInfo> cardInfos = JSONObject.parseObject(entities.get(0).toString(), new TypeReference<List<MonthCardInfo>>() {
            });
            return cardInfos==null || cardInfos.size()>0?cardInfos.get(0):null;
        }
        return null;
    }


    public BeeResponse post(String methodName, JSONObject params) {
        TreeMap<String, Object> tmap = new TreeMap<String, Object>();

        tmap.put("clientType","java");
        tmap.put("method","doActivity");
        tmap.put("module","activity");

        tmap.put("service","Entrance");
        tmap.put("typeflag","ThirdQuery");
        tmap.put("clientType","java");
        tmap.put("gcode","ZTGJ001");

        tmap.put("methodname",methodName);
        try {
            tmap.put("parameter",URLEncoder.encode(params.toJSONString(), "utf8"));
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("{}",e);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "encode error",e);
        }
        tmap.put("ms",System.currentTimeMillis());
        tmap.put("ve",1);

        String sign = null;
        try {
            sign = BeeUtils.sign(tmap, getParkingSysPrivatekey());
        } catch (Exception e) {
            LOGGER.error("{}",e);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "sign error",e);
        }
        tmap.put("sign", sign);

        String result = Utils.get(tmap, getParkingSysHost());
        BeeResponse response = JSONObject.parseObject(result, new TypeReference<BeeResponse>() {});
        if(isSuccess(response)){
            return response;
        }
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                "request error "+result);
    }

    private boolean isSuccess(BeeResponse response){
        return response!=null && response.isSuccess() && response.getOutList()!=null;
    }

    public abstract String getParkingSysPrivatekey();
    public abstract String getParkingSysHost();
}
