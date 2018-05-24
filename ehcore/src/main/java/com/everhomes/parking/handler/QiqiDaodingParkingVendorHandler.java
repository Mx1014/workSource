package com.everhomes.parking.handler;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.everhomes.bigcollection.Accessor;
import com.everhomes.bigcollection.BigCollectionProvider;
import com.everhomes.flow.FlowCase;
import com.everhomes.parking.*;
import com.everhomes.parking.clearance.ParkingClearanceLog;
import com.everhomes.parking.qididaoding.*;
import com.everhomes.rest.organization.VendorType;
import com.everhomes.rest.parking.*;
import com.everhomes.rest.parking.clearance.ParkingActualClearanceLogDTO;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 启迪香山，启迪香山
 */
@Component(ParkingVendorHandler.PARKING_VENDOR_PREFIX + "QIDI_DAODING")
public class QiqiDaodingParkingVendorHandler extends DefaultParkingVendorHandler{

    private static final Logger LOGGER = LoggerFactory.getLogger(QiqiDaodingParkingVendorHandler.class);

    private static final String OPENAPI_TOKEN_GET = "/openapi/token/get";// 全局token获取
    private static final String OPENAPI_PARKING_INFO = "/openapi/parking/info";// 停车场详情
    private static final String OPENAPI_PARKING_MONTHCARD_GET = "/openapi/parking/monthcard/get";// 月卡（固定户）信息查询
    private static final String OPENAPI_PARKING_MONTHCARD_TYPE_GET = "/openapi/parking/monthcard/type/get";// 月卡类型获取
    private static final String OPENAPI_PARKING_MONTHCARD_RENEWALS = "/openapi/parking/monthcard/renewals";// 月卡充值（续费）
    private static final String OPENAPI_PARKING_MONTHCARD_CREATE = "/openapi/parking/monthcard/create";// 新增月卡用户
    private static final String OPENAPI_PARKING_TEMPORARY_GET = "/openapi/parking/temporary/get";// 临停车费用查询
    private static final String OPENAPI_PARKING_TEMPORARY_CREATE = "/openapi/parking/temporary/create";// 临停车创建缴费订单
    private static final String OPENAPI_PARKING_LOCKCAR_INFO = "/openapi/parking/lockCar/info";// 锁车状态查询
    private static final String OPENAPI_PARKING_LOCKCAR_OPERATING = "/openapi/parking/lockCar/operating";// 锁车/解锁
    private static final String OPENAPI_PARKING_TEMPORARY_OPEN_ADD = "/openapi/parking/temporary/open/add";// 临时放行
    private static final String OPENAPI_PARKING_TEMPORARY_OPEN_LIST = "/openapi/parking/temporary/open/list";// 临时放行记录

    private static final String PARKING_QIDI_DAODING_TICKENT = "PARKING_QIDI_DAODING_TICKENT";//存token
//    private static final String PARKING_GET_FREE_SPACE_NUM = "PARKING_GET_FREE_SPACE_NUM";//存空余车位数量

    @Autowired
    public BigCollectionProvider bigCollectionProvider;


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
            for (ParkingRechargeRateDTO r : parkingRechargeRates) {
                if (r.getCardTypeId().equals(cardTypeId)) {
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
            dto.setPrice(rate.getPrice().divide(rate.getMonthCount(), OPEN_CARD_RETAIN_DECIMAL, RoundingMode.UP));

            dto.setPlateNumber(cmd.getPlateNumber());
            long now = System.currentTimeMillis();
            dto.setOpenDate(now);
            dto.setExpireDate(Utils.getTimestampByAddRealMonth(now, requestMonthCount));
            dto.setPayMoney(dto.getPrice().multiply(new BigDecimal(requestMonthCount)));
            dto.setOrderType(ParkingOrderType.OPEN_CARD.getCode());
        }
        return dto;
    }

    @Override
    public List<ParkingCardDTO> listParkingCardsByPlate(ParkingLot parkingLot, String plateNumber) {
        QidiDaodingResponse<QidiDaodingMonthCardEntity> response = getCardInfo(plateNumber);
        List<ParkingCardDTO> resultList = new ArrayList<>();
        if (isRequestDataSuccess(response)) {
            ParkingCardDTO parkingCardDTO = new ParkingCardDTO();

            // 格式yyyyMMddHHmmss
            String validEnd = response.getData().getExpireDate();
            Long endTime = Utils.strToLong(validEnd, Utils.DateStyle.DATE_TIME);

            setCardStatus(parkingLot, endTime, parkingCardDTO);// 这里设置过期可用，正常可用

            parkingCardDTO.setOwnerType(parkingLot.getOwnerType());
            parkingCardDTO.setOwnerId(parkingLot.getOwnerId());
            parkingCardDTO.setParkingLotId(parkingLot.getId());

            parkingCardDTO.setPlateOwnerName(response.getData().getOwnerName());// 车主名称
            parkingCardDTO.setPlateNumber(response.getData().getPlateNo());// 车牌号
            parkingCardDTO.setEndTime(endTime);

            parkingCardDTO.setCardTypeId(response.getData().getTypeId());
            parkingCardDTO.setCardType(response.getData().getTypeName());
//            parkingCardDTO.setCardNumber(entity.getData().getMemberUuid());
            resultList.add(parkingCardDTO);
        }
        return resultList;
    }

    private QidiDaodingResponse<QidiDaodingMonthCardEntity> getCardInfo(String plateNumber) {
        String parkingId = configProvider.getValue("parking.qididaoding.parkingId", "");

        TreeMap map = new TreeMap();
        map.put("parkingId",parkingId);
        map.put("plateNo",plateNumber);

        return postWithToken(map,OPENAPI_PARKING_MONTHCARD_GET,QidiDaodingResponse.class,QidiDaodingMonthCardEntity.class);
    }

    @Override
    public List<ParkingRechargeRateDTO> getParkingRechargeRates(ParkingLot parkingLot, String plateNumber, String cardNo) {
        String parkingId = configProvider.getValue("parking.qididaoding.parkingId", "");

        TreeMap map = new TreeMap();
        map.put("parkingId",parkingId);

        QidiDaodingResponse<List> response = postWithToken(map, OPENAPI_PARKING_MONTHCARD_TYPE_GET, QidiDaodingResponse.class, List.class);
        List<ParkingRechargeRateDTO> list = new ArrayList<>();
        if (!isRequestDataSuccess(response)) {
            return null;
        }
        String typeId = null;
        if(plateNumber!=null){
            QidiDaodingResponse<QidiDaodingMonthCardEntity> cardInfo = getCardInfo(plateNumber);
            if(!isRequestDataSuccess(response)){
                return null;
            }
            typeId = cardInfo.getData().getTypeId();
        }
        for (Object o : response.getData()) {
            QidiDaodingCardTypeEntity entity = JSONObject.parseObject(o.toString(),QidiDaodingCardTypeEntity.class);
            if(typeId!=null){
                if(typeId.equals(entity.getId())){
                    ParkingRechargeRateDTO dto = generateParkingRechargeRateDTO(entity, parkingLot);
                    list.add(dto);
                    break;
                }
            }else {
                ParkingRechargeRateDTO dto = generateParkingRechargeRateDTO(entity,parkingLot);
                list.add(dto);
            }
        }
        return list;
    }

    private ParkingRechargeRateDTO generateParkingRechargeRateDTO(QidiDaodingCardTypeEntity entity, ParkingLot parkingLot) {
        ParkingRechargeRateDTO dto = ConvertHelper.convert(parkingLot,ParkingRechargeRateDTO.class);
        dto.setCardTypeId(entity.getId());
        dto.setCardType(entity.getTypeName());
        dto.setRateName(entity.getMonthCount()+"个月");
        dto.setPrice(entity.getUnitPrice());
        dto.setRateToken(entity.getId());
        dto.setMonthCount(new BigDecimal(entity.getMonthCount()));
        return dto;
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

    private Boolean payTempCardFee(ParkingRechargeOrder order) {
        String parkingId = configProvider.getValue("parking.qididaoding.parkingId", "");

        TreeMap<String,String> map = new TreeMap();
        map.put("parkingId",parkingId);
        map.put("plateNo",order.getPlateNumber());
        map.put("receivable",String.valueOf(order.getPrice()));
        map.put("calcId",order.getOrderToken());
        map.put("paymentType", VendorType.ZHI_FU_BAO.getCode().equals(order.getPaidType()) ? "121001" : "120901");
        QidiDaodingResponse<JSONObject> response = postWithToken(map, OPENAPI_PARKING_TEMPORARY_CREATE, QidiDaodingResponse.class, null);
        order.setErrorDescription(response.getErrorMsg());
        order.setErrorDescriptionJson(response.toString());
        return isRequestDataSuccess(response);
    }

    private Boolean openMonthCard(ParkingRechargeOrder order) {
        ParkingCardRequest request;
        if (null != order.getCardRequestId()) {
            request = parkingProvider.findParkingCardRequestById(order.getCardRequestId());
        }else {
            request = getParkingCardRequestByOrder(order);
        }

        Timestamp timestampStart = new Timestamp(System.currentTimeMillis());
        Timestamp timestampEnd = Utils.getTimestampByAddNatureMonth(timestampStart.getTime(), order.getMonthCount().intValue());
        order.setStartPeriod(timestampStart);
        order.setEndPeriod(timestampEnd);

        if(addMonthCard(order, request)) {
            updateFlowStatus(request);
            return true;
        }
        return false;
    }

    private boolean addMonthCard(ParkingRechargeOrder order, ParkingCardRequest request) {
        String parkingId = configProvider.getValue("parking.qididaoding.parkingId", "");

        TreeMap<String,String> map = new TreeMap();
        map.put("parkingId",parkingId);
        map.put("plateNo",order.getPlateNumber());
        map.put("typeId",request.getCardTypeId());
//        map.put("plateColor",request.getCardTypeId());
        map.put("ownerMobile",request.getPlateOwnerPhone());
        map.put("ownerName",request.getPlateOwnerName());
//        map.put("address",request.getPlateOwnerEntperiseName());
//        map.put("ownerNo",request.getPlateOwnerEntperiseName());
        map.put("typeCount",String.valueOf(order.getMonthCount().intValue()));
        map.put("receivable",String.valueOf(order.getPrice().multiply(new BigDecimal("100"))));//单位分
        map.put("paymentMode",VendorType.ZHI_FU_BAO.getCode().equals(order.getPaidType()) ? "121001" : "120901");
        QidiDaodingResponse response = postWithToken(map, OPENAPI_PARKING_MONTHCARD_CREATE, QidiDaodingResponse.class, null);
        order.setErrorDescriptionJson(response.toString());
        order.setErrorDescription(response.getErrorMsg());
        if(isRequestSuccess(response)){
            updateFlowStatus(request);
            return true;
        }
        return false;

    }

    private Boolean rechargeMonthlyCard(ParkingRechargeOrder order) {
        QidiDaodingResponse<QidiDaodingMonthCardEntity> cardInfo = getCardInfo(order.getPlateNumber());
        if (isRequestDataSuccess(cardInfo)) {
            String parkingId = configProvider.getValue("parking.qididaoding.parkingId", "");
            TreeMap<String, String> map = new TreeMap();
            map.put("parkingId", parkingId);
            map.put("plateNo", order.getPlateNumber());
            map.put("typeId", cardInfo.getData().getTypeId());
            map.put("typeCount", "1");//类型数量	typeCount	是	蓝	类型月数 * 类型数量=月卡月数
            map.put("receivable", String.valueOf(order.getPrice().multiply(new BigDecimal("100"))));//单位分
            map.put("paymentMode", VendorType.ZHI_FU_BAO.getCode().equals(order.getPaidType()) ? "121001" : "120901");
            Timestamp rechargeStartTimestamp = new Timestamp(Utils.strToLong(cardInfo.getData().getExpireDate(), Utils.DateStyle.DATE_TIME));
            order.setStartPeriod(rechargeStartTimestamp);
            QidiDaodingResponse qidiDaodingResponse = postWithToken(map, OPENAPI_PARKING_MONTHCARD_RENEWALS, QidiDaodingResponse.class, null);

            order.setErrorDescriptionJson(qidiDaodingResponse.toString());
            order.setErrorDescription(qidiDaodingResponse.getErrorMsg());
            boolean isrequestSuccess = isRequestSuccess(qidiDaodingResponse);
            if(isrequestSuccess){
                QidiDaodingResponse<QidiDaodingMonthCardEntity> cardInfo2 = getCardInfo(order.getPlateNumber());
                if (isRequestDataSuccess(cardInfo2)) {
                    Timestamp rechargeEndTimestamp = new Timestamp(Utils.strToLong(cardInfo2.getData().getExpireDate(), Utils.DateStyle.DATE_TIME));
                    order.setEndPeriod(rechargeEndTimestamp);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public ParkingCarLockInfoDTO getParkingCarLockInfo(GetParkingCarLockInfoCommand cmd) {
        String parkingId = configProvider.getValue("parking.qididaoding.parkingId", "");

        TreeMap map = new TreeMap();
        map.put("parkingId",parkingId);
        map.put("plateNo",cmd.getPlateNumber());

        QidiDaodingResponse<QidiDaodingLockCarInfo> response = postWithToken(map, OPENAPI_PARKING_LOCKCAR_INFO, QidiDaodingResponse.class, QidiDaodingLockCarInfo.class);
        if (!isRequestDataSuccess(response)) {
            return null;
        }

        ParkingCarLockInfoDTO dto = new ParkingCarLockInfoDTO();
        Long time = Utils.strToLong(response.getData().getLockCarTime(),Utils.DateStyle.DATE_TIME);
//        dto.setEntryTime(time);
        dto.setLockCarTime(time);
        dto.setLockStatus(response.getData().getStatus().byteValue());
        return dto;
//        map.put("token",getToken(false));
//        String result = post(map, OPENAPI_PARKING_LOCKCAR_INFO);
//        if(StringUtils.isEmpty(result)){
//            return null;
//        }
//        JSONObject jsonObject = JSONObject.parseObject(result);
//        ParkingCarLockInfoDTO dto = new ParkingCarLockInfoDTO();
//        Long time = Utils.strToLong(jsonObject.getJSONObject("data").getString("lockCarTime"),Utils.DateStyle.DATE_TIME);
//        dto.setEntryTime(time);
//        dto.setLockCarTime(time);
//        dto.setLockStatus(jsonObject.getJSONObject("data").getByte("status").byteValue());
//        return dto;
    }

    @Override
    public void lockParkingCar(LockParkingCarCommand cmd) {
        String parkingId = configProvider.getValue("parking.qididaoding.parkingId", "");

        TreeMap map = new TreeMap();
        map.put("parkingId",parkingId);
        map.put("plateNo",cmd.getPlateNumber());
        //客户端回传的是车位当前状态，现在是要当前状态的相反操作
        map.put("status",String.valueOf(1-cmd.getLockStatus()));

        QidiDaodingResponse<QidiDaodingLockCarInfo> response = postWithToken(map, OPENAPI_PARKING_LOCKCAR_OPERATING, QidiDaodingResponse.class, null);
        if (!isRequestSuccess(response)) {
            throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.ERROR_GET_RESULT,
                    response!=null&&response.getErrorCode()!=null?response.getErrorCode()+","+response.getErrorMsg():"请求停车系统失败");
        }
    }

    @Override
    public ListCardTypeResponse listCardType(ListCardTypeCommand cmd) {
        String parkingId = configProvider.getValue("parking.qididaoding.parkingId", "");

        TreeMap map = new TreeMap();
        map.put("parkingId",parkingId);

        QidiDaodingResponse<List> response = postWithToken(map, OPENAPI_PARKING_MONTHCARD_TYPE_GET, QidiDaodingResponse.class, List.class);
        List<ParkingRechargeRateDTO> list = new ArrayList<>();
        if (!isRequestDataSuccess(response)) {
            return null;
        }
        ListCardTypeResponse listCardTypeResponse = new ListCardTypeResponse();
        List<ParkingCardType> cardTypes = new ArrayList<>();
        for (Object o : response.getData()) {
            QidiDaodingCardTypeEntity entity = JSONObject.parseObject(o.toString(),QidiDaodingCardTypeEntity.class);
            ParkingCardType dto = new ParkingCardType();
            dto.setTypeId(entity.getId());
            dto.setTypeName(entity.getTypeName());
            cardTypes.add(dto);
        }
        listCardTypeResponse.setCardTypes(cardTypes);
        return listCardTypeResponse;
    }

    @Override
    public void updateParkingRechargeOrderRate(ParkingLot parkingLot, ParkingRechargeOrder order) {
        order.setOriginalPrice(order.getPrice());
    }

    @Override
    public ParkingFreeSpaceNumDTO getFreeSpaceNum(GetFreeSpaceNumCommand cmd) {
        String parkingId = configProvider.getValue("parking.qididaoding.parkingId", "");

        TreeMap map = new TreeMap();
        map.put("parkingId",parkingId);

        QidiDaodingResponse<QidiDaodingParkingInfoEntity> response = postWithToken(map,OPENAPI_PARKING_INFO,QidiDaodingResponse.class,QidiDaodingParkingInfoEntity.class);
        if(isRequestDataSuccess(response)) {
            ParkingFreeSpaceNumDTO dto = ConvertHelper.convert(cmd, ParkingFreeSpaceNumDTO.class);
            dto.setFreeSpaceNum(response.getData().getEmptyParks());
            return  dto;
        }
        return null;
    }

    private <T extends QidiDaodingResponse,S>  T postWithToken(TreeMap map, String context, Class<T> cls, Class<S> subCls) {
        map.put("token",getToken(false));
        String result = post(map, context);
        if(StringUtils.isEmpty(result)){
           return null;
        }
        T response = JSONObject.parseObject(result,cls);
        if(isTokenOutOfDate(response)){
            map.put("token",getToken(true));
            result = post(map,context);
            response = JSONObject.parseObject(result,cls);
        }
        if(!isRequestSuccess(response)){
            throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.ERROR_GET_RESULT,
                    response!=null&&response.getErrorCode()!=null?response.getErrorCode()+","+response.getErrorMsg():"请求停车系统失败");
        }
        if(subCls!=null) {
            response.setData(JSONObject.parseObject(response.getData().toString(), subCls));
        }
        return response;
    }

    private boolean isRequestDataSuccess(QidiDaodingResponse cardInfo) {
        return cardInfo != null && cardInfo.isSuccess() && cardInfo.getData()!=null;
    }

    private boolean isRequestSuccess(QidiDaodingResponse delayMonthCardResult) {
        return delayMonthCardResult != null && delayMonthCardResult.isSuccess();
    }

    private <T extends QidiDaodingResponse> boolean isTokenOutOfDate(T response){
        return response!=null && response.isTokenOutOfDate();
    }


    /**
     *
     * @param refreshFlag 是否从第三方获取token,并缓存到redis
     * @return token
     */
    private String getToken(boolean refreshFlag){
        String rediskey = UserContext.getCurrentNamespaceId()+PARKING_QIDI_DAODING_TICKENT;
        Accessor acc = this.bigCollectionProvider.getMapAccessor(rediskey, "");
        final StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        RedisTemplate redisTemplate = acc.getTemplate(stringRedisSerializer);
        Integer lifecycle = configProvider.getIntValue("parking.qididaoding.tokenlifecycle", 6500);
        if(refreshFlag) {
            String token = requestToken();
            redisTemplate.opsForValue().set(rediskey, token.toString(),lifecycle, TimeUnit.SECONDS);
            return token;
        }
        Object redisToken = redisTemplate.opsForValue().get(rediskey);
        if(redisToken != null) {
            return redisToken.toString();
        }
        String token = requestToken();
        if(token == null) {
            return null;
        }
        //这里存储6500秒，因为你大爷的，对面只给两个小时有效token时间
        redisTemplate.opsForValue().set(rediskey, token.toString(),lifecycle, TimeUnit.SECONDS);
        return token;


    }

    @Override
    public ParkingTempFeeDTO getParkingTempFee(ParkingLot parkingLot, String plateNumber) {
        String parkingId = configProvider.getValue("parking.qididaoding.parkingId", "");

        TreeMap map = new TreeMap();
        map.put("parkingId",parkingId);
        map.put("plateNo",plateNumber);
//        map.put("plateColor",plateNumber);//车颜色
        QidiDaodingResponse<QidiDaodingTemporaryCarEntity> response =
                postWithToken(map, OPENAPI_PARKING_TEMPORARY_GET, QidiDaodingResponse.class, QidiDaodingTemporaryCarEntity.class);
        if(!isRequestDataSuccess(response)){
            return  null;
        }
        ParkingTempFeeDTO dto = new ParkingTempFeeDTO();
        Long aLong = Utils.subtractionTime(System.currentTimeMillis(), response.getData().getValidLeavingTime());
        dto.setDelayTime(0);
        if(aLong!=null && aLong>0) {
            dto.setRemainingTime(Integer.valueOf(aLong.toString()));
            dto.setDelayTime(Integer.valueOf(aLong.toString()));
        }
        dto.setPrice(response.getData().getPaidAmt());
        dto.setEntryTime(Utils.strToLong(response.getData().getEntryTime(),Utils.DateStyle.DATE_TIME));
        dto.setParkingTime(response.getData().getParkingTimes());
        dto.setPlateNumber(plateNumber);
        dto.setPayTime(System.currentTimeMillis());
        dto.setOrderToken(response.getData().getCalcId());
        return dto;
    }

    @Override
    public List<ParkingActualClearanceLogDTO> getTempCardLogs(ParkingClearanceLog r){
        List<ParkingActualClearanceLogDTO> list = new ArrayList<>();
        String parkingId = configProvider.getValue("parking.qididaoding.parkingId", "");

        TreeMap map = new TreeMap();
        map.put("parkingId",parkingId);
        map.put("plateNo",r.getPlateNumber());
        map.put("startTime",generateClearanceStartTime(r.getClearanceTime()));
        map.put("endTime",generateClearanceEndTime(r.getClearanceTime()));
        QidiDaodingResponse response =
                postWithToken(map, OPENAPI_PARKING_TEMPORARY_OPEN_LIST, QidiDaodingResponse.class, null);
        if(!isRequestDataSuccess(response)){
            return list;
        }
        JSONArray array =JSONObject.parseArray(response.getData().toString());
        if(array==null||array.size()==0){
            return list;
        }
        for (Object o : array) {
            JSONObject item  = JSONObject.parseObject(o.toString());
            ParkingActualClearanceLogDTO dto = new ParkingActualClearanceLogDTO();
            String plateNo = item.getString("plateNo");
            String inTime = item.getString("inTime");
            String outTime = item.getString("outTime");
            if(plateNo==null || !plateNo.equals(r.getPlateNumber())){
                continue;
            }
            if(inTime!=null) {
                dto.setEntryTime(Utils.strToTimeStamp(inTime,Utils.DateStyle.DATE_TIME));
            }
            if(outTime!=null) {
                dto.setExitTime(Utils.strToTimeStamp(outTime,Utils.DateStyle.DATE_TIME));
            }
            list.add(dto);
        }
        return list;
    }
    @Override
    public String applyTempCard(ParkingClearanceLog log){
        String parkingId = configProvider.getValue("parking.qididaoding.parkingId", "");

        TreeMap map = new TreeMap();
        map.put("parkingId",parkingId);
        map.put("plateNo",log.getPlateNumber());
        map.put("startTime",generateClearanceStartTime(log.getClearanceTime()));
        map.put("endTime",generateClearanceEndTime(log.getClearanceTime()));
        QidiDaodingResponse<QidiDaodingTemporaryCarEntity> response =
                postWithToken(map, OPENAPI_PARKING_TEMPORARY_OPEN_ADD, QidiDaodingResponse.class, null);
        return response.toString();
    }

    DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private String generateClearanceStartTime(Timestamp clearanceTime) {
        LocalDateTime start = clearanceTime.toLocalDateTime();
        return start.format(dtf2)+" 00:00:00";
    }

    private String generateClearanceEndTime(Timestamp clearanceTime) {
        LocalDateTime end = clearanceTime.toLocalDateTime();
        return end.format(dtf2) + " 23:59:59";
    }
    /**
     *
     * @return 从第三方获取token
     */
    private String requestToken() {
        String url = configProvider.getValue("parking.qididaoding.url", "");
        String code = configProvider.getValue("parking.qididaoding.code", "");
        String secret = configProvider.getValue("parking.qididaoding.secret", "");
        String key = configProvider.getValue("parking.qididaoding.key", "");
        String parkingId = configProvider.getValue("parking.qididaoding.parkingId", "");

//        String url = "http://test.daodingtech.com:9999/xdrpark-app";
//        String code = "qdxs001";
//        String secret = "ed62d4335a294932849415a4cc171e8c";
//        String key = "TdBMEZBxeRGQIRrN";
//        String parkingId = "20170104000000000002";

        TreeMap map = new TreeMap();
        map.put("code",code);
        map.put("secret",secret);
        map.put("parkingId",parkingId);
        map.put("sign", QidiDaodingSignUtil.getSign(map,key));

        int i = 0;
        for (;;) {
            String result =Utils.postUrlencoded(url+OPENAPI_TOKEN_GET,map);
            QidiDaodingResponse response = JSONObject.parseObject(result, new TypeReference<QidiDaodingResponse>() {
            });
            if (!response.isSuccess() || response.getData()==null || JSONObject.parseObject(response.getData().toString()).getString("token")==null) {
                i++;
                LOGGER.error("get token from elive failed, try again times {}",i);
                if(i>2) {
                    throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.ERROR_GET_TOKEN,
                            "获取token失败");
                }
                continue;
            }
            return JSONObject.parseObject(response.getData().toString()).getString("token");
        }
    }

    private String post(TreeMap map, String conext){
        String url = configProvider.getValue("parking.qididaoding.url", "");
        String paramKey = configProvider.getValue("parking.qididaoding.paramKey", "");
        map.put("sign", QidiDaodingSignUtil.getSign(map,paramKey));
        return  Utils.postUrlencoded(url+conext,map);
    }

//    public static void main(String[] args) {
//        System.out.println(new QiqiDaodingParkingVendorHandler().requestToken(OPENAPI_TOKEN_GET));
//    }


    @Override
    public void refreshToken() {
        this.getToken(true);
    }
}