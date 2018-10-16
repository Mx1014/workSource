package com.everhomes.parking.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.flow.FlowCase;
import com.everhomes.parking.*;
import com.everhomes.parking.chean.*;
import com.everhomes.parking.clearance.ParkingClearanceLog;
import com.everhomes.parking.ketuo.KetuoRequestConfig;
import com.everhomes.parking.yinxingzhijiexiaomao.YinxingzhijieXiaomaoJsonEntity;
import com.everhomes.rest.organization.VendorType;
import com.everhomes.rest.parking.*;
import com.everhomes.rest.parking.clearance.ParkingActualClearanceLogDTO;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
//import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Component(ParkingVendorHandler.PARKING_VENDOR_PREFIX + "CHEAN")
public class CheAnParkingVendorHandler extends DefaultParkingVendorHandler implements ParkingVendorHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(CheAnParkingVendorHandler.class);

    private static final String CATEGORY_SEPARATOR = "/";

    private static final String GET_TEMP_FEE = "api.aspx/calc";//获取临时车缴费信息

    private static final String PAY_TEMP_FEE = "api.aspx/commit";//临时车缴费

    private static final String GET_MONTHCARD = "api.aspx/park.mcard.info";//获取月卡缴费信息

    private static final String GET_CAR_TYPE = "api.aspx/park.cartypes.get";//获取车型

    private static final String GET_MONTHCARD_TYPE = "api.aspx/park.monthtariffs.get";//获取缴费类型（车型）

    private static final String OPEN_MONTHCARD = "api.aspx/pub.card.add";//新建月卡

    private static final String CREATE_ORDER = "api.aspx/park.mcard.order.create";//创建月卡缴费订单

    private static final String MONTHCARD_CHARGE = "api.aspx/park.mcard.charge";//月卡缴费

    private static final String GET_CAR_LOCATION = "api.aspx/pls.car.pos.getByLP";
    private static final String GET_CAR_BY_LOCATION = "api.aspx/pls.car.pos.getByNo";

    private static final String IN_INFO = "api.aspx/park.in.info";

    private static final String OUT_INFO = "api.aspx/park.out.info";

    private ThreadLocal<SimpleDateFormat> DATE_FORMAT = new ThreadLocal<SimpleDateFormat>() {
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.SIMPLIFIED_CHINESE);
        }
    };

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
            tempFee = entity.getData();
        }

        ParkingTempFeeDTO dto = new ParkingTempFeeDTO();
        if(null == tempFee) {
            return dto;
        }
        dto.setPlateNumber(plateNumber);

        try {
            Date entertime = DATE_FORMAT.get().parse(tempFee.getEntertime());
            dto.setEntryTime(entertime.getTime());
            Date calcendtime = DATE_FORMAT.get().parse(tempFee.getCalcendtime());
            dto.setPayTime(calcendtime.getTime());
            dto.setParkingTime(tempFee.getParktime());
        } catch (ParseException e) {
            LOGGER.error("Parse time error,EntryTime={},PayTime={}",tempFee.getEntertime(),tempFee.getCalcendtime());
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                    "Parse time error");
        }

        dto.setParkingTime(tempFee.getParktime());
        dto.setDelayTime(15);
        dto.setPrice(new BigDecimal(tempFee.getAmount()));

        dto.setOrderToken(tempFee.getOrderNo());
        return dto;

    }

    @Override
    public List<ParkingCardDTO> listParkingCardsByPlate(ParkingLot parkingLot, String plateNumber) {

        CheanCard card = getCardInfo(plateNumber,null);

        List<ParkingCardDTO> resultList = new ArrayList<>();

        if (card != null) {
            ParkingCardDTO parkingCardDTO = new ParkingCardDTO();

            List<ParkingCardRequestType> cardTypes = parkingProvider.listParkingCardTypes(null,null,parkingLot.getId());
            ParkingCardRequestType cardType = new ParkingCardRequestType();
            if(null != cardTypes && cardTypes.size() > 0){
                cardType = cardTypes.get(0);
            }

            // 格式yyyyMMddHHmmss
            String validEnd = card.getExpirydate();
            Long endTime = Utils.strToLong(validEnd, Utils.DateStyle.DATE_TIME);

            setCardStatus(parkingLot, endTime, parkingCardDTO);// 这里设置过期可用，正常可用

            parkingCardDTO.setOwnerType(parkingLot.getOwnerType());
            parkingCardDTO.setOwnerId(parkingLot.getOwnerId());
            parkingCardDTO.setParkingLotId(parkingLot.getId());
            List<ParkingCardRequest> cardRequests = parkingProvider.listParkingCardRequests(null,null,null,parkingLot.getId(),plateNumber,ParkingCardRequestStatus.PROCESSING.getCode(),null,null,null,20);
            ParkingCardRequest cardRequest = null;
            if(null != cardRequests && cardRequests.size() > 0){
                cardRequest = cardRequests.get(0);
                parkingCardDTO.setPlateOwnerName(cardRequest.getPlateOwnerName());// 车主名称

            }
            parkingCardDTO.setPlateNumber(card.getCarno());// 车牌号
            parkingCardDTO.setEndTime(endTime);

            parkingCardDTO.setCardTypeId(String.valueOf(cardType.getId()));//月卡类型id
            parkingCardDTO.setCardType(cardType.getCardTypeName());//月卡类型名称？
//            parkingCardDTO.setCardName(entity.getStandardType());

            resultList.add(parkingCardDTO);
        }
        return resultList;
    }

    @Override
    public List<ParkingRechargeRateDTO> getParkingRechargeRates(ParkingLot parkingLot, String plateNumber, String cardNo) {

        List<ParkingRechargeRateDTO> ratedtos = new ArrayList<>();

        List<ParkingCardRequestType> cardTypes = parkingProvider.listParkingCardTypes(null,null,parkingLot.getId());
        ParkingCardRequestType cardType = new ParkingCardRequestType();
        if(null != cardTypes && cardTypes.size() > 0){
            cardType = cardTypes.get(0);
        }

        String carTypeId = null;
        String monthlyrent = null;
        JSONObject param = new JSONObject();
        String json = post(param,GET_CAR_TYPE);
        CheanJsonArray<CheanCarType> entity = JSONObject.parseObject(json,new TypeReference<CheanJsonArray<CheanCarType>>(){});
        if (null != entity && entity.getStatus() && entity.getData().size() > 0){
            List<CheanCarType> carTypes = entity.getData();
            for(CheanCarType carType : carTypes){
                if("True".equals(carType.getDefaultCharge())){
                    carTypeId = carType.getCarTypeID();
                    break;
                }
            }
        }

        String json1 = post(param,GET_MONTHCARD_TYPE);
        CheanJsonArray<CheanCardType> entity1 = JSONObject.parseObject(json1,new TypeReference<CheanJsonArray<CheanCardType>>(){});
        if (null != entity1 && entity1.getStatus() && entity1.getData().size() > 0){
            List<CheanCardType> list = entity1.getData();
            for (CheanCardType type:list) {
                if(carTypeId.equals(type.getCarTypeID())){
                    monthlyrent = type.getCharge();
                    break;
                }
            }
        }

        for(int i = 1;i <= 3;i++){
            ParkingRechargeRateDTO cardRate = new ParkingRechargeRateDTO();
            cardRate.setCardTypeId(String.valueOf(cardType.getCardTypeId()));
            cardRate.setCardType(cardType.getCardTypeName());
            cardRate.setPrice(new BigDecimal(monthlyrent).multiply(new BigDecimal(i)).setScale(2));
            cardRate.setRateName(i + "个月");
            cardRate.setMonthCount(new BigDecimal(i));
            ratedtos.add(cardRate);
        }

//      测试用配置费用
        String debugfee = configProvider.getValue("parking.test.monthfee","0");
        if(!debugfee.equals("0")){
            ratedtos.forEach(dto ->{
                dto.setPrice(new BigDecimal(debugfee));
            });
        }

        return ratedtos;
    }

    @Override
    public ParkingCarLocationDTO getCarLocation(ParkingLot parkingLot, GetCarLocationCommand cmd) {
        ParkingCarLocationDTO dto = new ParkingCarLocationDTO();
        JSONObject param = new JSONObject();
        param.put("licensePlate",cmd.getPlateNumber());
        String json = post(param,GET_CAR_LOCATION);

        CheanJsonEntity<CheanLocation> entity = JSONObject.parseObject(json,new TypeReference<CheanJsonEntity<CheanLocation>>(){});

        if(null != entity && entity.getStatus() && null != entity.getData()){
            CheanLocation location = entity.getData();

            dto.setPlateNumber(cmd.getPlateNumber());
            dto.setOwnerType(parkingLot.getOwnerType());
            dto.setOwnerId(parkingLot.getOwnerId());
            dto.setParkingLotId(parkingLot.getId());
            dto.setParkingName(parkingLot.getName());

            dto.setSpaceNo(location.getParkLotName());
            dto.setLocation(location.getFloorName() + location.getParkLotName());
            dto.setFloorName(location.getFloorName());
            dto.setCarImageUrl(location.getImgUrl());

            JSONObject param1 = new JSONObject();
            param1.put("parkLotName",location.getParkLotName());

            String json1 = post(param1,GET_CAR_BY_LOCATION);
            CheanJsonEntity<JSONObject> entity1 = JSONObject.parseObject(json1,new TypeReference<CheanJsonEntity<JSONObject>>(){});
            if(null != entity1 && entity1.getStatus()){
                JSONObject timeInfo = entity1.getData();
                Date intime = new Date();
                Date now = new Date();
                try {
                    intime = DATE_FORMAT.get().parse(timeInfo.getString("InDate"));
                    now = DATE_FORMAT.get().parse(entity1.getTime());
                } catch (ParseException e) {
                    LOGGER.error("parse expiry date error, date={}", timeInfo.getString("InDate"));
                    throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                            "Parse expiry date error.");
                }
                dto.setEntryTime(intime.getTime());
                Long parkingTime = now.getTime() - intime.getTime();
                dto.setParkingTime(String.valueOf(parkingTime/(60*1000)));
            }


        }else{
            LOGGER.error("get car location error, msg={}", entity.getMessage());
            throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.CAR_ENTRY_INFO_NOT_FOUND,
                    "get car location error");
        }
        return dto;
    }

    @Override
    public String applyTempCard(ParkingClearanceLog log) {
        String result = null;

        JSONObject param = new JSONObject();
        param.put("NumNo",log.getPlateNumber());
        JSONObject park = new JSONObject();
        park.put("StartDate",DATE_FORMAT.get().format(log.getApplyTime()));
        park.put("ExpiryDate",DATE_FORMAT.get().format(log.getClearanceTime()));
        param.put("Park",park);
        String json = post(param,OPEN_MONTHCARD);
        CheanJsonEntity<JSONObject> entity = JSONObject.parseObject(json,new TypeReference<CheanJsonEntity<JSONObject>>(){});
        if(null != entity && entity.getStatus() && null != entity.getData()){
            JSONObject data = entity.getData();
            result = data.getString("CardNo");
        }
        return result;
    }

    @Override
    public List<ParkingActualClearanceLogDTO> getTempCardLogs(ParkingClearanceLog r) {

        JSONObject param = new JSONObject();
        param.put("credentialtype","1");
        param.put("credential",r.getPlateNumber());
        param.put("starttime",DATE_FORMAT.get().format(r.getApplyTime()));
        param.put("endtime",DATE_FORMAT.get().format(r.getClearanceTime()));
        String json = post(param,OUT_INFO);
        CheanJsonArray<JSONObject> entity = JSONObject.parseObject(json,new TypeReference<CheanJsonArray<JSONObject>>(){});
        if(null != entity && entity.getStatus() && entity.getData().size() > 0){
            List<JSONObject> datas = entity.getData();
            return datas.stream().map(data -> {
                ParkingActualClearanceLogDTO dto = new ParkingActualClearanceLogDTO();
                dto.setEntryTime(Timestamp.valueOf(data.getString("entertime")));
                dto.setEntryTime(Timestamp.valueOf(data.getString("exittime")));
                return dto;
            }).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public Boolean notifyParkingRechargeOrderPayment(ParkingRechargeOrder order) {
        if (order.getOrderType().equals(ParkingOrderType.RECHARGE.getCode())) {
            if(order.getRechargeType().equals(ParkingRechargeType.MONTHLY.getCode())) {
                return rechargeMonthlyCard(order);
            }else if(order.getRechargeType().equals(ParkingRechargeType.TEMPORARY.getCode())) {
                return payTempCardFee(order);
            }
        } else if(order.getOrderType().equals(ParkingOrderType.OPEN_CARD.getCode())) {
            return openMonthCard(order);
        }
        LOGGER.info("unknown type = " + order.getRechargeType());
        return false;
    }

    boolean payTempCardFee(ParkingRechargeOrder order){

        JSONObject param = new JSONObject();
        param.put("credentialtype", "1");
        param.put("credential", order.getPlateNumber());
        param.put("orderNo", order.getOrderToken());
        param.put("parkcost", order.getOriginalPrice().toString());
        param.put("amount", order.getOriginalPrice().toString());
        param.put("discount", 0);
        param.put("payType", VendorType.WEI_XIN.getCode().equals(order.getPaidType()) ? 4 : 5);
        String json = post(param, PAY_TEMP_FEE);

        CheanJsonEntity<JSONObject> entity = JSONObject.parseObject(json,new TypeReference<CheanJsonEntity<JSONObject>>(){});
        if(null != entity && entity.getStatus()){
            JSONObject jsonObject = entity.getData();
            String returnFlag = jsonObject.getString("flag");
            if(null != returnFlag && "1".equals(returnFlag)) {
                Integer FreeLeaveMinutes = Integer.valueOf(jsonObject.getString("FreeLeaveMinutes"));
                return true;
            }
        }
        return false;
    }

    private boolean openMonthCard(ParkingRechargeOrder order){
        ParkingCardRequest request;
        if (null != order.getCardRequestId()) {
            request = parkingProvider.findParkingCardRequestById(order.getCardRequestId());
        }else {
            request = getParkingCardRequestByOrder(order);
        }
        Timestamp timestampStart = new Timestamp(System.currentTimeMillis());
        Timestamp timestampEnd = new Timestamp(Utils.getLongByAddNatureMonth(timestampStart.getTime(), order.getMonthCount().intValue(),true));
        order.setStartPeriod(timestampStart);
        order.setEndPeriod(timestampEnd);
        if(createMonthCard(request.getPlateNumber(),DATE_FORMAT.get().format(timestampStart),DATE_FORMAT.get().format(timestampEnd))){
            updateFlowStatus(request);
            return true;
        }
//        if(addMonthCard(order, request)) {
//            updateFlowStatus(request);
//            return true;
//        }
        return false;
    }

    private boolean addMonthCard(ParkingRechargeOrder order, ParkingCardRequest request){
        JSONObject param = new JSONObject();

        param.put("credentialtype","1");
        param.put("credential", order.getPlateNumber());
        param.put("number",order.getMonthCount().toString());//缴费月数
        param.put("payType", VendorType.WEI_XIN.getCode().equals(order.getPaidType())?"7" : "5");

        String json = post(param,MONTHCARD_CHARGE);
        order.setErrorDescriptionJson(json);

        CheanJsonEntity<JSONObject> entity = JSONObject.parseObject(json,new TypeReference<CheanJsonEntity<JSONObject>>(){});
        if(null != entity && entity.getStatus()){
            JSONObject data = entity.getData();
            if(null != data && 1 == data.getIntValue("flag")) {
                return true;
            }
        }
        return false;
    }

    private boolean createMonthCard(String plateNumber,String StartDate,String ExpiryDate){

        JSONObject param = new JSONObject();
        param.put("NumNo",plateNumber);
        JSONObject park = new JSONObject();
        park.put("StartDate", StartDate);
        park.put("ExpiryDate",ExpiryDate);
        param.put("Park",park);

        String json = post(param,OPEN_MONTHCARD);

        CheanJsonEntity<JSONObject> entity = JSONObject.parseObject(json,new TypeReference<CheanJsonEntity<JSONObject>>(){});
        if(null != entity && entity.getStatus()){
            JSONObject data = entity.getData();
            if(null != data) {
                return true;
            }
        }
        return false;
    }

    private boolean rechargeMonthlyCard(ParkingRechargeOrder order){

        CheanCard card = getCardInfo(order.getPlateNumber(),order.getMonthCount().intValue());

        if (null != card) {
            Timestamp timestampStart = new Timestamp(Utils.strToLong(card.getExpirydate(), Utils.DateStyle.DATE_TIME)+1000);
            Timestamp timestampEnd = Utils.getTimestampByAddNatureMonth(timestampStart.getTime(), order.getMonthCount().intValue());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            String validStart = sdf.format(timestampStart);
            String validEnd = sdf.format(timestampEnd);

            JSONObject param = new JSONObject();
            param.put("credentialtype","1");
            param.put("credential", order.getPlateNumber());
            param.put("number",order.getMonthCount().setScale(0).toString());//缴费月数
            param.put("payType", VendorType.WEI_XIN.getCode().equals(order.getPaidType())?"7" : "5");

            String json = post(param,MONTHCARD_CHARGE);

            //将充值信息存入订单
            order.setErrorDescriptionJson(json);
            order.setStartPeriod(timestampStart);
            order.setEndPeriod(timestampEnd);

            CheanJsonEntity<JSONObject> entity = JSONObject.parseObject(json,new TypeReference<CheanJsonEntity<JSONObject>>(){});
            if(null != entity && entity.getStatus()){
                JSONObject data = entity.getData();
                if(null != data && 1 == data.getIntValue("flag")) {
                    return true;
                }
            }
        }
        return false;
    }

    private CheanCard getCardInfo(String plateNumber,Integer month) {
        JSONObject param = new JSONObject();
        param.put("credentialtype", "1");
        param.put("credential", plateNumber);
        if(null != month)
            param.put("number",month);
        String json = this.post(param,GET_MONTHCARD);
        CheanJsonEntity<CheanCard> entity = JSONObject.parseObject(json,new TypeReference<CheanJsonEntity<CheanCard>>(){});
        if(null != entity && entity.getStatus()){
            return entity.getData();
        }
        return null;
    }

    private String createOrder(String plateNumber,BigDecimal payMoney,String preExpireDate,String currExpireDate){
        JSONObject param = new JSONObject();
        param.put("credentialType", "1");
        param.put("credential", plateNumber);
        param.put("aeceivable", payMoney.setScale(2).toString());
        param.put("amount", payMoney.setScale(2).toString());
        param.put("preExpireDate", preExpireDate);
        param.put("currExpireDate", currExpireDate);

        String orderNo = null;
        String json = this.post(param,CREATE_ORDER);
        CheanJsonEntity<JSONObject> entity = JSONObject.parseObject(json,new TypeReference<CheanJsonEntity<JSONObject>>(){});
        if(null != entity && entity.getStatus()){
            JSONObject data = entity.getData();
            orderNo = data.getString("orderNo");
        }
        return orderNo;
    }

    @Override
    public ListCardTypeResponse listCardType(ListCardTypeCommand cmd) {
        ListCardTypeResponse response = new ListCardTypeResponse();

        List<ParkingCardRequestType> typeList = parkingProvider.listParkingCardTypes(cmd.getOwnerType(),
                cmd.getOwnerId(), cmd.getParkingLotId());

        List<ParkingCardType> cardlist = new ArrayList<>();
        if(null != typeList)
            cardlist = typeList.stream().map(r->{
                ParkingCardType cardType = new ParkingCardType();
                cardType.setTypeId(r.getCardTypeId());
                cardType.setTypeName(r.getCardTypeName());
                return cardType;
            }).collect(Collectors.toList());
        response.setCardTypes(cardlist);

        return response;
    }

    @Override
    public OpenCardInfoDTO getOpenCardInfo(GetOpenCardInfoCommand cmd) {

        ParkingCardRequest parkingCardRequest = parkingProvider.findParkingCardRequestById(cmd.getParkingRequestId());

        FlowCase flowCase = flowCaseProvider.getFlowCaseById(parkingCardRequest.getFlowCaseId());

        ParkingFlow parkingFlow = parkingProvider.getParkingRequestCardConfig(cmd.getOwnerType(), cmd.getOwnerId(),
                cmd.getParkingLotId(), flowCase.getFlowMainId());

        Integer requestMonthCount = REQUEST_MONTH_COUNT;
        Byte requestRechargeType = REQUEST_RECHARGE_TYPE;

        if(null != parkingFlow) {
            requestMonthCount = parkingFlow.getRequestMonthCount();
            requestRechargeType = parkingFlow.getRequestRechargeType();
        }

        OpenCardInfoDTO dto = new OpenCardInfoDTO();
        String cardTypeId = parkingCardRequest.getCardTypeId();

        ParkingLot lot = ConvertHelper.convert(cmd,ParkingLot.class);
        lot.setId(cmd.getParkingLotId());

        List<ParkingRechargeRateDTO> parkingRechargeRates = getParkingRechargeRates(lot, null, null);
        if(null != parkingRechargeRates && !parkingRechargeRates.isEmpty()) {

            ParkingRechargeRateDTO rate = null;
            for(ParkingRechargeRateDTO r: parkingRechargeRates) {
                if(r.getCardTypeId().equals(cardTypeId)) {
                    rate = r;
                    break;
                }
            }

            if (null == rate) {
                return null;
            }
//            dto = ConvertHelper.convert(rate,OpenCardInfoDTO.class);
            dto.setOwnerId(cmd.getOwnerId());
            dto.setOwnerType(cmd.getOwnerType());
            dto.setParkingLotId(cmd.getParkingLotId());
            dto.setRateToken(rate.getRateToken());
            dto.setRateName(rate.getRateName());
            dto.setCardType(rate.getCardType());
            dto.setMonthCount(rate.getMonthCount());
//            dto.setMonthCount(BigDecimal.valueOf(requestMonthCount));
            dto.setPrice(rate.getPrice().divide(rate.getMonthCount(),OPEN_CARD_RETAIN_DECIMAL, RoundingMode.UP));

            dto.setPlateNumber(cmd.getPlateNumber());
            long now = System.currentTimeMillis();
            dto.setOpenDate(now);
            dto.setExpireDate(Utils.getLongByAddNatureMonth(now, requestMonthCount,true));
            if(requestRechargeType == ParkingCardExpiredRechargeType.ALL.getCode()) {
                dto.setPayMoney(dto.getPrice().multiply(new BigDecimal(requestMonthCount)));
            }else {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(now);
                int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                int today = calendar.get(Calendar.DAY_OF_MONTH);

                BigDecimal price = dto.getPrice().multiply(new BigDecimal(requestMonthCount))
                        .add(dto.getPrice().multiply(new BigDecimal(maxDay-today+1))
                                .divide(new BigDecimal(DAY_COUNT), OPEN_CARD_RETAIN_DECIMAL, RoundingMode.HALF_UP));
                dto.setPayMoney(price);
            }
            dto.setOrderType(ParkingOrderType.OPEN_CARD.getCode());
        }

//      测试用配置费用
        String debugfee = configProvider.getValue("parking.test.monthfee","0");
        if(!debugfee.equals("0")){
            dto.setPayMoney(new BigDecimal(debugfee));
        }

        return dto;
    }


    @Override
    public ParkingExpiredRechargeInfoDTO getExpiredRechargeInfo(ParkingLot parkingLot, GetExpiredRechargeInfoCommand cmd) {
        ParkingExpiredRechargeInfoDTO dto = new ParkingExpiredRechargeInfoDTO();
        List<ParkingRechargeRateDTO> rates = getParkingRechargeRates(parkingLot,cmd.getPlateNumber(),null);

        CheanCard card = getCardInfo(cmd.getPlateNumber(),null);
        if(null != card) {
            long now = System.currentTimeMillis();
            long expireTime;
            try {
                expireTime = DATE_FORMAT.get().parse(card.getExpirydate()).getTime();
            } catch (ParseException e) {
                LOGGER.error("Parse time error,Expirydate={}",card.getExpirydate());
                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                        "Parse time error");
            }
            BigDecimal price = new BigDecimal(0);
            for(ParkingRechargeRateDTO rate: rates) {
                price = rate.getPrice();
                break;
            }
            if(expireTime < now) {
                dto.setOwnerId(cmd.getOwnerId());
                dto.setOwnerType(cmd.getOwnerType());
                dto.setParkingLotId(parkingLot.getId());
                dto.setMonthCount(new BigDecimal(parkingLot.getExpiredRechargeMonthCount()));
                dto.setPrice(price.multiply(dto.getMonthCount()));
                dto.setStartPeriod(expireTime);
                dto.setEndPeriod(Utils.getLongByAddNatureMonth(Utils.getLastDayOfMonth(now), parkingLot.getExpiredRechargeMonthCount() -1));
            }
        }
        return dto;
    }


    @Override
    public void updateParkingRechargeOrderRate(ParkingLot parkingLot, ParkingRechargeOrder order) {

    }


    protected String post(JSONObject data, String type) {

        String url = configProvider.getValue("parking.chean.url","");
//        String url = "http://113.98.59.44:9022";

        url += CATEGORY_SEPARATOR + type;

        String accessKeyId = configProvider.getValue("parking.chean.accessKeyId","");
        String key = configProvider.getValue("parking.chean.privatekey","");
        String branchno = configProvider.getValue("parking.chean.branchno","");
//        String accessKeyId = "UT";
//        String key = "71cfa1c59773ddfa289994e6d505bba3";
//        String branchno ="0";

        String iv = DATE_FORMAT.get().format(new Date());
        int nonce = (int) (Math.random() * 100);
        Map<String, Object> params = new HashMap<>();
        params.put("from",accessKeyId);
        params.put("timestamp",iv);
        params.put("nonce",String.valueOf(nonce));
        String text = null;
        String sign = null;
        try {
            LOGGER.info("The request info, url={}, not encrypt,from={},timestamp={},nonce={},branchno={},data={}", url, accessKeyId, iv, nonce, branchno, data);
            text = EncryptUtil.signing(params);
            if(!data.isEmpty())
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

}
