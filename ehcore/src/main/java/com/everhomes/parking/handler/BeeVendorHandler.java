// @formatter:off
package com.everhomes.parking.handler;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.flow.FlowCase;
import com.everhomes.parking.*;
import com.everhomes.parking.bee.*;
import com.everhomes.rest.parking.*;
import com.everhomes.sms.DateUtil;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
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
    private static final String ADD_CARD = "addCard";//开月卡
    private static final String ACTIVE_CARD_ORDER = "activeCardOrder";//激活月卡

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
            if(cardInfo==null){
                return new ArrayList<>();
            }
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
            dto.setVendorName(getParkingVendorName());
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
        ParkingLot lot = ConvertHelper.convert(cmd,ParkingLot.class);
        lot.setId(cmd.getParkingLotId());
        List<ParkingRechargeRateDTO> parkingRechargeRates = getParkingRechargeRates(lot, null, null);
        if(null != parkingRechargeRates && !parkingRechargeRates.isEmpty()) {
            ParkingRechargeRateDTO rate = null;
            for(ParkingRechargeRateDTO r: parkingRechargeRates) {
                if(r.getCardTypeId().equals(parkingCardRequest.getCardTypeId()) && r.getMonthCount().intValue() == 1) {
                    rate = r;
                    break;
                }
            }
            if(rate == null){
                for(ParkingRechargeRateDTO r: parkingRechargeRates) {
                    if(r.getCardTypeId().equals(parkingCardRequest.getCardTypeId())) {
                        rate = r;
                        break;
                    }
                }
            }
            if(rate == null){
                throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.ERROR_SELF_DEFINE,
                        "费率未设置");
            }

            dto.setOwnerId(cmd.getOwnerId());
            dto.setOwnerType(cmd.getOwnerType());
            dto.setParkingLotId(cmd.getParkingLotId());
            dto.setRateToken(rate.getRateToken());
            dto.setRateName(rate.getRateName());
            dto.setCardType(rate.getCardType());
            dto.setMonthCount(new BigDecimal(requestMonthCount));
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

    private Boolean openMonthCard(ParkingRechargeOrder order) {
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

    private boolean addMonthCard(ParkingRechargeOrder order, ParkingCardRequest request) {
        JSONObject params = new JSONObject();
        processJSONParams(params);
        params.put("ploid",getParkingPloid());
        params.put("realname",request.getPlateOwnerName());
        UserIdentifier identifier = userService.getUserIdentifier(order.getCreatorUid());
        if(identifier !=null) {
            params.put("tel", identifier.getIdentifierToken());
        }else{
            params.put("tel", "4008384688");//这是左邻官方账号，对接的人说必须搞个电话账号
        }
        params.put("sex","1");//sex  性别参数  1-男  0- 女
        params.put("carnumber",request.getPlateNumber());
        params.put("itemtype",request.getCardTypeId());
        params.put("startdate",order.getStartPeriod().getTime());
        params.put("enddate",order.getEndPeriod().getTime());
        params.put("cardnum",order.getMonthCount().intValue());
        ParkingRechargeRate rate = parkingProvider.findParkingRechargeRatesById(Long.valueOf(order.getRateToken()));
        if(rate!=null) {
            params.put("unitprice", rate.getPrice().divide(rate.getMonthCount()).intValue());
        }else{
            params.put("unitprice", order.getPrice().multiply(order.getMonthCount()));
        }
        params.put("totalprice",order.getPrice());
//        params.put("mebtype",null);
//        params.put("mebtypename",null);
//        params.put("mebremark",null);
        params.put("paymode","1");
        BeeResponse response=null;
        try {
        	response = post(ADD_CARD, params);
        }catch (Exception e){
            LOGGER.error("ADD_CARD exception", e);
            return false;
        }
        order.setErrorDescriptionJson(response.toString());
        if(isChargeSuccess(response)){
            List<OpenCardInfo> entities= JSONObject.parseObject(response.getOutList().toString(), new TypeReference<List<OpenCardInfo>>(){});
            if(entities!=null && entities.size()>0){
                OpenCardInfo openCardInfo = entities.get(0);
                if(openCardInfo!=null && openCardInfo.getCardid()!=null) {
                    //这里尝试三次激活，否则跳出
                    int activeCount = 0;
                    boolean activeFlag = false;
                    do {
                        JSONObject activeParams = new JSONObject();
                        activeParams.put("cardid", openCardInfo.getCardid());
                        try {
                            BeeResponse activeResponse = post(ACTIVE_CARD_ORDER, activeParams);
                            activeCount ++;
                            activeFlag = isChargeSuccess(activeResponse);
                            if(activeFlag){
                                return true;
                            }
                        }catch (Exception e){
                            LOGGER.error("ACTIVE_CARD_ORDER exception", e);
                        }
                    }while(!activeFlag && activeCount<3);
                    LOGGER.info("active card 3 times failed");
                }
            }
        }
        return false;
    }

    private Boolean payTempCardFee(ParkingRechargeOrder order) {
        JSONObject params=new JSONObject();
        processJSONParams(params);
        params.put("orderid",order.getOrderToken());
        params.put("amount","0");
        params.put("time","0");
        params.put("payAmount",order.getPrice());

        BeeResponse beeResponse;
        try {
        	beeResponse = post(PAY_ORDER, params);
        }catch (Exception e){
            LOGGER.error("PAY_ORDER exception", e);
            return false;
        }
        order.setErrorDescriptionJson(beeResponse.toString());
        order.setErrorDescription(beeResponse.getMessage());
        return isChargeSuccess(beeResponse);
    }


    private Boolean rechargeMonthlyCard(ParkingRechargeOrder order) {
        MonthCardInfo cardInfo = getCardInfo(order.getPlateNumber());
        if (cardInfo == null) {
            return false;
        }
        
        Long newStart = getMonthlyRechargeStartTime(Long.valueOf(cardInfo.getEnddate()));
        Timestamp timestampEnd = getMonthlyRechargeEndTime(newStart, order.getMonthCount().intValue());

        JSONObject params=new JSONObject();
        processJSONParams(params);
        params.put("cardid",cardInfo.getId());
        params.put("startdate",newStart+"");
        params.put("enddate",timestampEnd.getTime()+"");
        params.put("cardnum",order.getMonthCount());
        params.put("totalprice",order.getPrice());
        params.put("paymode","1");//付费方式 (1-现金 ,2-刷卡, 3-转账
        params.put("paytype","1");//续费类型(1-充值续费 2-封存延期)

        order.setStartPeriod(new Timestamp(newStart));
        order.setEndPeriod(timestampEnd);
        BeeResponse beeResponse;
        try{
        	beeResponse = post(ADD_CARD_ORDER_LIST,params);
        }catch (Exception e){
            LOGGER.error("ADD_CARD_ORDER_LIST exception", e);
            return false;
        }
        //将充值信息存入订单
        order.setErrorDescriptionJson(beeResponse.toString());
        order.setErrorDescription(beeResponse.getMessage());
        return isChargeSuccess(beeResponse);
    }

    @Override
    public ListCardTypeResponse listCardType(ListCardTypeCommand cmd) {
        ListCardTypeResponse ret = new ListCardTypeResponse();
        JSONObject params = new JSONObject();
        processJSONParams(params);
        params.put("typestatus",1);
        params.put("pageSize",50);
        BeeResponse result;
        try{
        	result = post(GET_CARD_TYPE_LIST,params);
        }catch (Exception e){
            LOGGER.error("GET_CARD_TYPE_LIST exception", e);
            return ret;
        }
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
        params.put("amount", "0");//优惠金额（元）曹丹，这里必须穿
        params.put("time", "0");//优惠时间（毫秒）曹丹，这里必须穿
        BeeResponse response;
        try{
        	response = post(GET_ORDER,params);
        }catch (Exception e){
            LOGGER.error("GET_ORDER exception", e);
            return null;
        }

        List<TempCardInfo> tempCardInfos= JSONObject.parseObject(response.getOutList().toString(), new TypeReference<List<TempCardInfo>>(){});
        if(tempCardInfos!=null && tempCardInfos.size()>0) {
            TempCardInfo tempCardInfo = tempCardInfos.get(0);
            if(tempCardInfo==null || !tempCardInfo.isNormalState() || tempCardInfo.isNotParking()) {
                return dto;
            }
            BigDecimal price = new BigDecimal(tempCardInfo.getReceivableprice());
            if(price.compareTo(new BigDecimal(0))==0){
                return dto;
            }
            dto.setPrice(price);
            dto.setPlateNumber(tempCardInfo.getCarnumber());
            dto.setEntryTime(Long.valueOf(tempCardInfo.getIntime()));
            long now = System.currentTimeMillis();
            dto.setPayTime(now);
            dto.setParkingTime(Integer.valueOf(Long.valueOf(tempCardInfo.getParktime())/60000+""));
//            dto.setDelayTime(15);
            dto.setOriginalPrice(new BigDecimal(tempCardInfo.getOrderamount()));
            //feestate	缴费状态	int	0=无数据;2=免缴费;3=已缴费未超时;4=已缴费需续费;5=需要缴费
            //这里需要干嘛，缴费完成，回调的时候，读取这个token，向停车场缴费需要这个参数。参考 payTempCardFee()
            dto.setOrderToken(tempCardInfo.getOrderid());
            dto.setDelayTime(configProvider.getIntValue("parking.bee.delayTime",15));//临时车缴费成功了之后，有多长预留的出场时间？一般15到30分，可以在车场配置的
        }
        return dto;
    }

    @Override
    public void updateParkingRechargeOrderRate(ParkingLot parkingLot, ParkingRechargeOrder order) {
        updateParkingRechargeOrderRateInfo(parkingLot, order);
    }

    @Override
    public ParkingExpiredRechargeInfoDTO getExpiredRechargeInfo(ParkingLot parkingLot, GetExpiredRechargeInfoCommand cmd) {
        List<ParkingCardDTO> parkingCardLists = listParkingCardsByPlate(parkingLot, cmd.getPlateNumber());
        if(parkingCardLists==null || parkingCardLists.size()==0){
            return null;
        }
        ParkingCardDTO cardInfo = parkingCardLists.get(0);
        if (cardInfo == null) {
            return null;
        }
        List<ParkingRechargeRateDTO> parkingRechargeRates = getParkingRechargeRates(parkingLot, null, null);
        if(parkingRechargeRates==null || parkingRechargeRates.size()==0){
            return null;
        }

        ParkingRechargeRateDTO targetRateDTO = null;
        String cardTypeId = cardInfo.getCardTypeId();
        for (ParkingRechargeRateDTO rateDTO : parkingRechargeRates) {
            if (rateDTO.getCardTypeId().equals(cardTypeId) && rateDTO.getMonthCount().intValue()==parkingLot.getExpiredRechargeMonthCount()) {
                targetRateDTO = rateDTO;
                break;
            }
        }

        if (null == targetRateDTO) {
            parkingRechargeRates.sort((r1,r2)->r1.getMonthCount().compareTo(r2.getMonthCount()));
            for (ParkingRechargeRateDTO rateDTO : parkingRechargeRates) {
                if (rateDTO.getCardTypeId().equals(cardTypeId)) {
                    targetRateDTO = rateDTO;
                    break;
                }
            }
        }
        if (null == targetRateDTO) {
            return null;
        }

        ParkingExpiredRechargeInfoDTO dto = ConvertHelper.convert(targetRateDTO,ParkingExpiredRechargeInfoDTO.class);
        dto.setCardTypeName(targetRateDTO.getCardType());
        if (cardInfo != null  && cardInfo.getEndTime() != null) {
        	
            long newStartTime = getMonthlyRechargeStartTime(cardInfo.getEndTime());
            Timestamp rechargeEndTimestamp = getMonthlyRechargeEndTime(newStartTime, parkingLot.getExpiredRechargeMonthCount());
            dto.setStartPeriod(newStartTime);
            dto.setEndPeriod(rechargeEndTimestamp.getTime());
            dto.setMonthCount(new BigDecimal(parkingLot.getExpiredRechargeMonthCount()));
            dto.setRateName(parkingLot.getExpiredRechargeMonthCount()+configProvider.getValue("parking.default.rateName","个月"));
            dto.setPrice(targetRateDTO.getPrice().divide(targetRateDTO.getMonthCount(),OPEN_CARD_RETAIN_DECIMAL, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal(parkingLot.getExpiredRechargeMonthCount())));
        }
        return dto;
    }

    private MonthCardInfo getCardInfo(String plateNumber) {
        JSONObject params=new JSONObject();
        processJSONParams(params);
        params.put("carnumber",plateNumber);
        params.put("query","all");
        params.put("pageSize",50);
        BeeResponse result;
        try{
        	result = post(GET_CARD_LIST_BY_CAR_NUMBER,params);
        }catch (Exception e){
            LOGGER.error("GET_CARD_LIST_BY_CAR_NUMBER exception", e);
            return null;
        }
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

    /**
     * 是否充值成功
     * @param response
     * @return
     */
    private boolean isChargeSuccess(BeeResponse response){
        if(isSuccess(response)){
            JSONArray outlist = JSONObject.parseArray(response.getOutList().toString());
            if(outlist!=null && outlist.size()>0){
                JSONObject result = JSONObject.parseObject(outlist.getString(0));
                return "1".equalsIgnoreCase(result.getString("state"));
            }
        }
        return false;
    }
    
	public long getMonthlyRechargeStartTime(Long endTime) {
    	
		long now = System.currentTimeMillis();
		if (now/1000 > endTime/1000) { // 比较时按秒计
			endTime = now;
		}
		
    	Calendar cal = Calendar.getInstance(); 
    	cal.setTimeInMillis(endTime);
    	cal.add(Calendar.DAY_OF_MONTH, 1);
    	cal.set(Calendar.HOUR_OF_DAY, 0);
    	cal.set(Calendar.MINUTE, 0);
    	cal.set(Calendar.SECOND, 0);

		return cal.getTimeInMillis(); //多加1秒
	}
	
	
	public Timestamp getMonthlyRechargeEndTime(long newStart, int monthCount) {
		return Utils.getTimestampByAddThirtyDays(newStart, monthCount);
	}
    

    protected abstract void processMapParams(TreeMap<String, Object> tmap);
    protected abstract void processJSONParams(JSONObject params);

    public abstract String getParkingSysPrivatekey();
    public abstract String getParkingSysHost();
    public abstract String getParkingPloid();
    public abstract String getParkingVendorName();
}
