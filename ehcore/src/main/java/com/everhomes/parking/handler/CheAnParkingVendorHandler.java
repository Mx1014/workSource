package com.everhomes.parking.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.parking.ParkingLot;
import com.everhomes.parking.ParkingRechargeOrder;
import com.everhomes.parking.ParkingVendorHandler;
import com.everhomes.parking.chean.*;
import com.everhomes.parking.ketuo.KetuoRequestConfig;
import com.everhomes.rest.organization.VendorType;
import com.everhomes.rest.parking.*;
import com.everhomes.util.RuntimeErrorException;
//import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component(ParkingVendorHandler.PARKING_VENDOR_PREFIX + "TEST")
public class CheAnParkingVendorHandler extends DefaultParkingVendorHandler implements ParkingVendorHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(CheAnParkingVendorHandler.class);

    private static final String CATEGORY_SEPARATOR = "/";

    private static final String GET_TEMP_FEE = "api.aspx/calc";

    private static final String PAY_TEMP_FEE = "api.aspx/commit";

    private static final String GET_MONTHCARD = "api.aspx/park.mcard.info";

    private static final String GET_MONTHCARD_TYPE = "api.aspx/park.monthtariffs.get";

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public ParkingTempFeeDTO getParkingTempFee(ParkingLot parkingLot, String plateNumber) {

        CheanTempFee tempFee = null;

        JSONObject param = new JSONObject();
        param.put("credentialtype", "1");
        param.put("credential", plateNumber);
//        取车场数据库当前时刻作为计费结束时间
        param.put("calcendtime", "1970-01-01 00:00:00");

        String json = this.post(param,GET_TEMP_FEE);

        CheanJsonEntity<CheanTempFee> entity = JSONObject.parseObject(json,new TypeReference<CheanJsonEntity<CheanTempFee>>(){});

        if (null != entity && entity.getStatus()){
            List<CheanTempFee> list = entity.getData();
            if(null != list && list.size() > 0){
                tempFee = list.get(0);
            }
        }

        ParkingTempFeeDTO dto = new ParkingTempFeeDTO();
        if(null == tempFee) {
            return dto;
        }
        dto.setPlateNumber(plateNumber);

        try {
            Date entertime = DATE_FORMAT.parse(tempFee.getEntertime());
            dto.setEntryTime(entertime.getTime());
            Date calcendtime = DATE_FORMAT.parse(tempFee.getCalcendtime());
            dto.setPayTime(calcendtime.getTime());
        } catch (ParseException e) {
            LOGGER.error("Parse time error,EntryTime={},PayTime={}",tempFee.getEntertime(),tempFee.getCalcendtime());
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                    "Parse time error");
        }

        dto.setParkingTime(tempFee.getParktime());
//        dto.setDelayTime(tempFee.getDelayTime());
        dto.setPrice(new BigDecimal(tempFee.getAmount()));

        dto.setOrderToken(tempFee.getOrderNo());
        return dto;

    }

    @Override
    public List<ParkingCardDTO> listParkingCardsByPlate(ParkingLot parkingLot, String plateNumber) {

        JSONObject param = new JSONObject();
        param.put("credentialtype", "1");
        param.put("credential", plateNumber);

        String json = this.post(param,GET_MONTHCARD);

        CheanJsonEntity<CheanCard> entity = JSONObject.parseObject(json,new TypeReference<CheanJsonEntity<CheanCard>>(){});

        List<ParkingCardDTO> resultList = new ArrayList<>();

        if (entity != null && entity.getStatus()) {
            CheanCard card = entity.getData().get(0);
            ParkingCardDTO parkingCardDTO = new ParkingCardDTO();

            // 格式yyyyMMddHHmmss
            String validEnd = card.getExpirydate();
            Long endTime = Utils.strToLong(validEnd, Utils.DateStyle.DATE_TIME);

            setCardStatus(parkingLot, endTime, parkingCardDTO);// 这里设置过期可用，正常可用

            parkingCardDTO.setOwnerType(parkingLot.getOwnerType());
            parkingCardDTO.setOwnerId(parkingLot.getOwnerId());
            parkingCardDTO.setParkingLotId(parkingLot.getId());

            parkingCardDTO.setPlateOwnerName(card.getName());// 车主名称
            parkingCardDTO.setPlateNumber(card.getCarno());// 车牌号
            parkingCardDTO.setEndTime(endTime);

            String json1 = this.post(null,GET_MONTHCARD_TYPE);

            CheanJsonEntity<CheanCardType> entity1 = JSONObject.parseObject(json1,new TypeReference<CheanJsonEntity<CheanCardType>>(){});
            if (entity1 != null && entity1.getStatus()) {
                List<CheanCardType> types =  entity1.getData();
                types.stream().forEach(type ->{
                    if(type.getTariffID().equals(card.getTariffid())){
                        parkingCardDTO.setCardTypeId(type.getTariffID());//月卡类型id
                        parkingCardDTO.setCardType(type.getTariffName());//月卡类型名称？
//                        parkingCardDTO.setCardName(entity.getStandardType());
                    }
                });
            }


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
//        if(order.getRechargeType().equals(ParkingRechargeType.MONTHLY.getCode()))
//            return rechargeMonthlyCard(order);
        return payTempCardFee(order);
    }

    boolean payTempCardFee(ParkingRechargeOrder order){

        JSONObject param = new JSONObject();
        param.put("orderNo", order.getOrderToken());
        param.put("amount", (order.getOriginalPrice().movePointLeft(2).toString()));
        param.put("discount", 0);
        param.put("payType", VendorType.WEI_XIN.getCode().equals(order.getPaidType()) ? 4 : 5);
        String json = post(param, PAY_TEMP_FEE);

        JSONObject jsonObject = JSONObject.parseObject(json);
        String returnFlag = jsonObject.getString("flag");
        if(null != returnFlag && "1".equals(returnFlag)) {
            Integer FreeLeaveMinutes = Integer.valueOf(jsonObject.getString("FreeLeaveMinutes"));
            return true;
        }
        return false;
    }

    @Override
    public ListCardTypeResponse listCardType(ListCardTypeCommand cmd) {
        return null;
    }

    @Override
    public void updateParkingRechargeOrderRate(ParkingLot parkingLot, ParkingRechargeOrder order) {

    }


    protected String post(Object data, String type) {

//        String url = configProvider.getValue("parking.chean.url","http://113.98.59.44:9022");
        String url = "http://113.98.59.44:9022";

        url += CATEGORY_SEPARATOR + type;

//        String accessKeyId = configProvider.getValue("parking.chean.accessKeyId","UT");
//        String key = configProvider.getValue("parking.chean.privatekey","71cfa1c59773ddfa289994e6d505bba3");
//        String branchno = configProvider.getValue("parking.chean.branchno","0");
        String accessKeyId = "UT";
        String key = "71cfa1c59773ddfa289994e6d505bba3";
        String branchno = "0";

        String iv = DATE_FORMAT.format(new Date());
        int nonce = (int) Math.random() * 100;
        Map<String, Object> params = new HashMap<>();
        params.put("from",accessKeyId);
        params.put("timestamp",iv);
        params.put("nonce",String.valueOf(nonce));
        String text = null;
        String sign = null;
        try {
            LOGGER.info("The request info, url={}, not encrypt,from={},timestamp={},nonce={},branchno={},data={}", url, accessKeyId, iv, nonce, branchno, data);
            text = EncryptUtil.signing(params);
            text = text + data;  //此处追加data进行加密
            LOGGER.info("encrypt text with data, text={}", text);
            sign = EncryptUtil.encode(text, key);
        } catch (Exception e) {
            LOGGER.error("Parking encrypt param error, text={}, key={}", text, key, iv);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                    "Parking encrypt param error.");
        }
        params.put("sign",sign);
        params.put("branchno",branchno);
        params.put("data", data);
        params.put("queryFormat",null);
        LOGGER.info("Parking info, namespace={}", this.getClass().getName());
        String postString = JSON.toJSONString(params);
        LOGGER.info("Http request entity={}", postString);
        String result = HttpUtil.sendPost(url, postString);
        LOGGER.info("Http response entity={}", result);
        return result;
    }

//    public static void main(String[] args) {
//        CheAnParkingVendorHandler bean = new CheAnParkingVendorHandler();
//        bean.getParkingTempFee(null,"粤BMP525");
//
////        LOGGER.info("amount={}",new BigDecimal("24.00"));
//    }
}
