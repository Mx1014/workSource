package com.everhomes.parking.handler;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.everhomes.flow.FlowCase;
import com.everhomes.parking.*;
import com.everhomes.parking.yinxingzhijiexiaomao.*;
import com.everhomes.rest.organization.VendorType;
import com.everhomes.rest.parking.*;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.StringHelper;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * 银星智界，银星科技园
 */
@Component(ParkingVendorHandler.PARKING_VENDOR_PREFIX + "YINXINGZHIJIE_TECHPARK")
public class XiaomaoYXParkParkingVendorHandler extends XiaomaoYinxingzhijieParkingVendorHandler{

    private static final Logger LOGGER = LoggerFactory.getLogger(XiaomaoYXParkParkingVendorHandler.class);

    private static final String HANDLER_MONTHCARD = "/park/handleMonthCard";// 新增月卡接口
    private static final String OPEN_MONTHCARD = "/park/openMonthCard";// 月卡续费接口
    private static final String GET_MONTHCARD_TYPE = "/park/getMonthCardType";// 获取月卡类型接口
    private static final String GET_MONTHCARD = "/park/getMonthCard";// 月卡查询
    private static final String CHARGING = "/park/charging";// 计费接口（临时车？）
    private static final String POSTCHARGE = "/park/postCharge";// 缴费接口(临时车？)

    @Override
    public List<ParkingCardDTO> listParkingCardsByPlate(ParkingLot parkingLot, String plateNumber) {
        YinxingzhijieXiaomaoCard entity = getCardInfo(plateNumber);
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

    @Override
    public ParkingTempFeeDTO getParkingTempFee(ParkingLot parkingLot, String plateNumber) {
        TreeMap<String, String> param = new TreeMap<String,String>();
        param.put("plateNo",plateNumber);
        String result = post(CHARGING, param);
        YinxingzhijieXiaomaoTempCard tempCard = JSONObject.parseObject(result, new TypeReference<YinxingzhijieXiaomaoTempCard>() {});
        ParkingTempFeeDTO dto = new ParkingTempFeeDTO();
        if(tempCard != null  && tempCard.isSuccess()) {
            dto.setPlateNumber(plateNumber);
            dto.setEntryTime(Utils.strToLong(tempCard.getStartTime(),Utils.DateStyle.DATE_TIME));
            long now = System.currentTimeMillis();
            dto.setPayTime(now);
            dto.setParkingTime(Integer.valueOf(String.valueOf((now-dto.getEntryTime())/60000L)));
            dto.setDelayTime(15);
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
//            dto.setMonthCount(rate.getMonthCount());
            dto.setMonthCount(BigDecimal.valueOf(requestMonthCount));
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

                BigDecimal price = dto.getPrice().multiply(new BigDecimal(requestMonthCount-1))
                        .add(dto.getPrice().multiply(new BigDecimal(maxDay-today+1))
                                .divide(new BigDecimal(DAY_COUNT), OPEN_CARD_RETAIN_DECIMAL, RoundingMode.HALF_UP));
                dto.setPayMoney(price);
            }
            dto.setOrderType(ParkingOrderType.OPEN_CARD.getCode());
        }

        return dto;
    }

    private YinxingzhijieXiaomaoCard getCardInfo(String plateNumber) {
        TreeMap<String,String> params = new TreeMap<String,String>();
        params.put("licenseNumber", plateNumber);
        String result = post(GET_MONTHCARD, params);
        YinxingzhijieXiaomaoCard entity = JSONObject.parseObject(result, new TypeReference<YinxingzhijieXiaomaoCard>(){});
        return entity;
    }

    @Override
    public List<ParkingRechargeRateDTO> getParkingRechargeRates(ParkingLot parkingLot, String plateNumber, String cardNo) {
        List<ParkingRechargeRate> parkingRechargeRateList;

        if(StringUtils.isBlank(plateNumber)) {
            parkingRechargeRateList = parkingProvider.listParkingRechargeRates(parkingLot.getOwnerType(), parkingLot.getOwnerId(),
                    parkingLot.getId(), null);
        }else{
            YinxingzhijieXiaomaoCard card = getCardInfo(plateNumber);
            String cardType = card.getMemberType();
            parkingRechargeRateList = parkingProvider.listParkingRechargeRates(parkingLot.getOwnerType(), parkingLot.getOwnerId(),
                    parkingLot.getId(), cardType);
        }

        TreeMap<String, String> params = new TreeMap<String,String>();
        String result = post(GET_MONTHCARD_TYPE, params);
        YinxingzhijieXiaomaoJsonEntity<List<YinxingzhijieXiaomaoCardType>> entity = JSONObject.parseObject(result, new TypeReference<YinxingzhijieXiaomaoJsonEntity<List<YinxingzhijieXiaomaoCardType>>>(){});


        List<ParkingRechargeRateDTO> dtos = new ArrayList<>();
        if(entity!=null && entity.isSuccess()) {
            dtos = parkingRechargeRateList.stream().map(r -> {
                ParkingRechargeRateDTO dto = ConvertHelper.convert(r, ParkingRechargeRateDTO.class);
                populaterate(entity.getData(), dto, r);
                return dto;
            }).collect(Collectors.toList());
        }
        return dtos;
    }

    private void populaterate(List<YinxingzhijieXiaomaoCardType> types, ParkingRechargeRateDTO dto, ParkingRechargeRate r) {
        YinxingzhijieXiaomaoCardType temp = null;
        for(YinxingzhijieXiaomaoCardType t: types) {
            if(t.getStandardId().equals(r.getCardType())) {
                temp = t;
                break;
            }
        }
        
        dto.setCardTypeId(temp.getStandardId());
        dto.setCardType(temp.getStandardType());
        dto.setRateToken(r.getId().toString());
        dto.setVendorName(ParkingLotVendor.YINXINGZHIJIE_TECHPARK.getCode());
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

    private boolean payTempCardFee(ParkingRechargeOrder order) {
        TreeMap<String,String> params = new TreeMap();
        params.put("plateNo", order.getPlateNumber());
        params.put("orderId", order.getOrderToken());
        params.put("payMoney", order.getPrice().setScale(2).toString());
        String result = post(POSTCHARGE, params);
        order.setErrorDescriptionJson(result);//data 缴费记录号 存储

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

        if(addMonthCard(order, request)) {
            updateFlowStatus(request);
            return true;
        }
        return false;
    }

    private boolean addMonthCard(ParkingRechargeOrder order, ParkingCardRequest request){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String validStart = sdf.format(order.getStartPeriod());
        String validEnd = sdf.format(order.getEndPeriod());

        TreeMap<String, String> param = new TreeMap<String,String>();
        param.put("standardId",request.getCardTypeId());
        param.put("beginTime",validStart);//月卡开始时间，按照你大爷的，现在时间来
        param.put("stopTime",validEnd);//结束时间
        param.put("licenseNumber", order.getPlateNumber());
        param.put("money", order.getPrice().setScale(2).toString());
        param.put("payType", VendorType.WEI_XIN.getCode().equals(order.getPaidType())?"wxpay" : "alipay");

        param.put("userName", request.getPlateOwnerName());
        param.put("userTel", request.getPlateOwnerPhone());
        param.put("companyName", request.getPlateOwnerEntperiseName());


        String json = post(HANDLER_MONTHCARD,param);
        order.setErrorDescriptionJson(json);

        JSONObject jsonObject = JSONObject.parseObject(json);
        Object obj = jsonObject.get("flag");
        if(null != obj ) {
            int resCode = (int) obj;
            if(resCode == 1) {
                return true;
            }
        }

        return false;
    }


    private boolean rechargeMonthlyCard(ParkingRechargeOrder order){

        YinxingzhijieXiaomaoCard card = getCardInfo(order.getPlateNumber());

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

            YinxingzhijieXiaomaoJsonEntity<?> entity = JSONObject.parseObject(json, YinxingzhijieXiaomaoJsonEntity.class);
            order.setErrorDescription(entity!=null?entity.getMessage():null);
            if (entity.isSuccess()){
                return true;
            }
        }
        return false;
    }

    @Override
    public ListCardTypeResponse listCardType(ListCardTypeCommand cmd) {
        ListCardTypeResponse ret = new ListCardTypeResponse();

    	TreeMap<String, String> params = new TreeMap<String,String>();
    	String result = post(GET_MONTHCARD_TYPE, params);
		LOGGER.info("Card type from kexin xiaomao={}", result);

		YinxingzhijieXiaomaoJsonEntity<List<YinxingzhijieXiaomaoCardType>> entity = JSONObject.parseObject(result, new TypeReference<YinxingzhijieXiaomaoJsonEntity<List<YinxingzhijieXiaomaoCardType>>>(){});
        List<ParkingCardType> list = new ArrayList<>();
		if(entity.isSuccess()){
			for (YinxingzhijieXiaomaoCardType cardType : entity.getData()) {
				ParkingCardType parkingCardType = new ParkingCardType();
				parkingCardType.setTypeId(cardType.getStandardId());
				parkingCardType.setTypeName(cardType.getStandardType());
				list.add(parkingCardType);
			}
			ret.setCardTypes(list);
		}
        return ret;
    }

    @Override
    public void updateParkingRechargeOrderRate(ParkingLot parkingLot, ParkingRechargeOrder order) {
        super.updateParkingRechargeOrderRateInfo(parkingLot, order);
    }

    private String post(String context,TreeMap<String,String> params){
        String url = configProvider.getValue("parking.yinxingzhijietechpark.url", "");
        String parkId = configProvider.getValue("parking.yinxingzhijietechpark.parkId", "");
        String accessKeyId = configProvider.getValue("parking.yinxingzhijietechpark.accessKeyId", "");
        String accessKeyValue = configProvider.getValue("parking.yinxingzhijietechpark.accessKeyValue", "");

        params.put("parkId", parkId);
        params.put("accessKeyId", accessKeyId);
        params.put("sign", YinxingzhijieXiaomaoSignUtil.getSign(params, accessKeyValue));
        return  Utils.post(url + context, JSONObject.parseObject(StringHelper.toJsonString(params)),
                StandardCharsets.UTF_8);
    }

}