// @formatter:off
package com.everhomes.parking.handler;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.parking.ParkingLot;
import com.everhomes.parking.ParkingRechargeOrder;
import com.everhomes.parking.ParkingRechargeRate;
import com.everhomes.parking.bee.*;
import com.everhomes.rest.parking.*;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
  *
  * @Author dengs[shuang.deng@zuolin.com]
  * @Date 2018/5/29 11:30
  */
public abstract class BeeVendorHandler extends DefaultParkingVendorHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(BeeVendorHandler.class);
    private static final String GET_CARD_LIST_BY_CAR_NUMBER = "getCardListByCarnumber";//月卡车
    private static final String ADD_CARD_ORDER_LIST = "addCardOrderList";//月卡充值
    private static final String GET_CARD_TYPE_LIST = "getCardTypeList";
    private static final String GET_ORDER = "getOrder";//临时车
    private static final String PAY_ORDER = "payOrder";//临时车充值

    @Override
    public List<ParkingCardDTO> listParkingCardsByPlate(ParkingLot parkingLot, String plateNumber) {
        MonthCardInfo cardInfo = getCardInfo(plateNumber);
        List<ParkingCardDTO> resultList = new ArrayList<>();
        if (cardInfo != null && cardInfo.isNormalCarStatus()) {
            ParkingCardDTO parkingCardDTO = new ParkingCardDTO();

            // 格式yyyyMMddHHmmss
            String validEnd = cardInfo.getEnddate();
            Long endTime = Long.valueOf(validEnd);

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
        List<ParkingRechargeRate> parkingRechargeRateList = null;
        if(plateNumber!=null) {
            MonthCardInfo cardInfo = getCardInfo(plateNumber);
            parkingRechargeRateList = parkingProvider
                    .listParkingRechargeRates(parkingLot.getOwnerType(), parkingLot.getOwnerId(), parkingLot.getId(), cardInfo.getCardtypeid());
        }else{
            parkingRechargeRateList = parkingProvider
                    .listParkingRechargeRates(parkingLot.getOwnerType(), parkingLot.getOwnerId(), parkingLot.getId(), null);
        }
        ListCardTypeResponse response = listCardType(null);

        List<ParkingRechargeRateDTO> result = parkingRechargeRateList.stream().map(r -> {
            ParkingRechargeRateDTO dto = ConvertHelper.convert(r, ParkingRechargeRateDTO.class);
            populaterate(response.getCardTypes(), dto, r);
            return dto;
        }).collect(Collectors.toList());

        return result;
    }

    private void populaterate(List<ParkingCardType> cardTypes, ParkingRechargeRateDTO dto, ParkingRechargeRate r) {
        ParkingCardType temp = null;
        for (ParkingCardType t : cardTypes) {
            if (t.getTypeId().equals(r.getCardType())) {
                temp = t;
            }
        }
        if(temp!=null) {
            dto.setCardTypeId(temp.getTypeId());
            dto.setCardType(temp.getTypeName());
            dto.setRateToken(r.getId().toString());
            dto.setVendorName(ParkingLotVendor.BEE_ZHONGTIAN.getCode());
        }
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

    private Boolean openMonthCard(ParkingRechargeOrder order) {
        return false;
    }

    private Boolean payTempCardFee(ParkingRechargeOrder order) {
        JSONObject params=new JSONObject();
        processJSONParams(params);
        params.put("orderid",order.getOrderToken());
        params.put("payAmount",order.getPrice());

        BeeResponse beeResponse = post(PAY_ORDER, params);

        order.setErrorDescriptionJson(beeResponse.toString());
        order.setErrorDescription(beeResponse.getMessage());
        return isSuccess(beeResponse);
    }


    private Boolean rechargeMonthlyCard(ParkingRechargeOrder order) {
        MonthCardInfo cardInfo = getCardInfo(order.getPlateNumber());
        if (cardInfo == null) {
            return false;
        }
        Long newStart = Long.valueOf(cardInfo.getEnddate());
        Timestamp timestampEnd = Utils.getTimestampByAddThirtyDays(newStart, order.getMonthCount().intValue());

        JSONObject params=new JSONObject();
        processJSONParams(params);
        params.put("cardid",order.getOrderToken());
        params.put("startdate",newStart+"");
        params.put("enddate",timestampEnd.getTime()+"");
        params.put("cardnum",order.getMonthCount());
        params.put("totalprice",order.getPrice());
//        params.put("paymode","2");//付费方式 (1-现金 ,2-刷卡, 3-转账
        params.put("paytype","1");//续费类型(1-充值续费 2-封存延期)

        order.setStartPeriod(new Timestamp(newStart));
        order.setEndPeriod(timestampEnd);
        BeeResponse beeResponse = post(ADD_CARD_ORDER_LIST, params);
        //将充值信息存入订单
        order.setErrorDescriptionJson(beeResponse.toString());
        order.setErrorDescription(beeResponse.getMessage());
        return isSuccess(beeResponse);
    }

    @Override
    public ListCardTypeResponse listCardType(ListCardTypeCommand cmd) {
        ListCardTypeResponse ret = new ListCardTypeResponse();
        JSONObject params = new JSONObject();
        processJSONParams(params);
        params.put("typestatus",1);
        params.put("pageSize",50);
        BeeResponse result = post(GET_CARD_TYPE_LIST, params);

        List<OutListEntity> entities= JSONObject.parseObject(result.getOutList().toString(), new TypeReference<List<OutListEntity>>(){});
        if(entities!=null && entities.size()>0) {
            OutListEntity outListEntity = entities.get(0);
            if(outListEntity!=null && outListEntity.getDatalist()!=null && outListEntity.getDatalist().toString().length()>0) {
                List<CardTypeInfo> cardTypeInfos = JSONObject.parseObject(outListEntity.getDatalist().toString(), new TypeReference<List<CardTypeInfo>>() {
                });
                if(cardTypeInfos!=null) {
                    ret.setCardTypes(cardTypeInfos.stream().map(card->{
                        ParkingCardType parkingCardType = new ParkingCardType();
                        parkingCardType.setTypeId(card.getId());
                        parkingCardType.setTypeName(card.getTypename());
                        return parkingCardType;
                    }).collect(Collectors.toList()));
                }
            }
        }

        return ret;
    }

    @Override
    public ParkingTempFeeDTO getParkingTempFee(ParkingLot parkingLot, String plateNumber) {
        ParkingTempFeeDTO dto = new ParkingTempFeeDTO();
        JSONObject params = new JSONObject();
        params.put("carnumber", plateNumber);
        BeeResponse response = post(GET_ORDER, params);

        List<TempCardInfo> tempCardInfos= JSONObject.parseObject(response.getOutList().toString(), new TypeReference<List<TempCardInfo>>(){});
        if(tempCardInfos!=null && tempCardInfos.size()>0) {
            TempCardInfo tempCardInfo = tempCardInfos.get(0);
            if(tempCardInfo==null || !tempCardInfo.isNormalState()) {
                return dto;
            }
            dto.setPlateNumber(tempCardInfo.getCarnumber());
            dto.setEntryTime(Long.valueOf(tempCardInfo.getIntime()));
            long now = System.currentTimeMillis();
            dto.setPayTime(now);
            dto.setParkingTime(Integer.valueOf(Long.valueOf(tempCardInfo.getParktime())/60000+""));
//            dto.setDelayTime(15);
            dto.setPrice(new BigDecimal(tempCardInfo.getReceivableprice()));
            dto.setOriginalPrice(new BigDecimal(tempCardInfo.getOrderamount()));
            //feestate	缴费状态	int	0=无数据;2=免缴费;3=已缴费未超时;4=已缴费需续费;5=需要缴费
            //这里需要干嘛，缴费完成，回调的时候，读取这个token，向停车场缴费需要这个参数。参考 payTempCardFee()
            dto.setOrderToken(tempCardInfo.getOrderid());
        }
        return dto;
    }

    @Override
    public void updateParkingRechargeOrderRate(ParkingLot parkingLot, ParkingRechargeOrder order) {

    }

    private MonthCardInfo getCardInfo(String plateNumber) {
        JSONObject params=new JSONObject();
        processJSONParams(params);
        params.put("carnumber",plateNumber);
        params.put("query","all");
        params.put("pageSize",50);
        BeeResponse result = post(GET_CARD_LIST_BY_CAR_NUMBER,params);
        List<OutListEntity> entities= JSONObject.parseObject(result.getOutList().toString(), new TypeReference<List<OutListEntity>>(){});
        if(entities!=null && entities.size()>0) {
            OutListEntity outListEntity = entities.get(0);
            if(outListEntity!=null && outListEntity.getDatalist()!=null && outListEntity.getDatalist().toString().length()>0) {
                List<MonthCardInfo> cardInfos = JSONObject.parseObject(outListEntity.getDatalist().toString(), new TypeReference<List<MonthCardInfo>>() {
                });
                return cardInfos == null || cardInfos.size() > 0 ? cardInfos.get(0) : null;
            }
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
        tmap.put("gcode","ZTGJ001");
        processMapParams(tmap);

        tmap.put("methodname",methodName);
        try {
            tmap.put("parameter",URLEncoder.encode(params.toJSONString(), "utf8"));
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("{}",e);
            throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.ERROR_SELF_DEFINE,
                    "encode error",e);
        }
        tmap.put("ms",System.currentTimeMillis());
        tmap.put("ve",1);

        String sign = null;
        try {
            sign = BeeUtils.sign(tmap, getParkingSysPrivatekey());
        } catch (Exception e) {
            LOGGER.error("{}",e);
            throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.ERROR_SELF_DEFINE,
                    "sign error",e);
        }
        tmap.put("sign", sign);

        String result = Utils.get(tmap, getParkingSysHost());
        BeeResponse response = JSONObject.parseObject(result, new TypeReference<BeeResponse>() {});
        if(isSuccess(response)){
            return response;
        }
        throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.ERROR_SELF_DEFINE,
                "请求停车系统结果失败");
    }

    private boolean isSuccess(BeeResponse response){
        return response!=null && response.isSuccess() && response.getOutList()!=null;
    }

    protected abstract void processMapParams(TreeMap<String, Object> tmap);
    protected abstract void processJSONParams(JSONObject params);

    public abstract String getParkingSysPrivatekey();
    public abstract String getParkingSysHost();
}
