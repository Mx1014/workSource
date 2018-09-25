package com.everhomes.parking.handler;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.db.DbProvider;
import com.everhomes.flow.*;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.parking.*;
import com.everhomes.flow.FlowAutoStepDTO;
import com.everhomes.parking.clearance.ParkingClearanceLog;
import com.everhomes.rest.flow.FlowStepType;
import com.everhomes.rest.organization.VendorType;
import com.everhomes.rest.parking.*;
import com.everhomes.rest.parking.clearance.ParkingActualClearanceLogDTO;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserProvider;
import com.everhomes.user.UserService;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionStatus;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author sw on 2017/8/16.
 */
public abstract class DefaultParkingVendorHandler implements ParkingVendorHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultParkingVendorHandler.class);

    //开卡按30天计算
    static final int DAY_COUNT = 30;

    //开卡费用计算保留小数
    public static final int OPEN_CARD_RETAIN_DECIMAL = 0;
    //临时车费用计算保留小数
    public static final int TEMP_FEE_RETAIN_DECIMAL = 2;
    //月卡充值费率计算保留小数
    public static final int CARD_RATE_RETAIN_DECIMAL = 2;

    @Autowired
    ParkingProvider parkingProvider;
    @Autowired
    LocaleTemplateService localeTemplateService;
    @Autowired
    ConfigurationProvider configProvider;
    @Autowired
    private FlowService flowService;
    @Autowired
    private FlowProvider flowProvider;
    @Autowired
    FlowCaseProvider flowCaseProvider;
    @Autowired
    private DbProvider dbProvider;
    @Autowired
    UserService userService;

   public void setCardStatus(ParkingLot parkingLot, long expireTime, ParkingCardDTO parkingCardDTO) {
        long now = System.currentTimeMillis();

        parkingCardDTO.setCardStatus(ParkingCardStatus.NORMAL.getCode());

        if (expireTime < now) {
            parkingCardDTO.setCardStatus(ParkingCardStatus.EXPIRED.getCode());

            Byte isSupportRecharge = parkingLot.getExpiredRechargeFlag();
            if(ParkingConfigFlag.SUPPORT.getCode() == isSupportRecharge)	{
                Integer cardReserveDay = parkingLot.getMaxExpiredDay();
                long cardReserveTime = cardReserveDay * 24 * 60 * 60 * 1000L;

                if (expireTime + cardReserveTime >= now) {
                    parkingCardDTO.setCardStatus(ParkingCardStatus.SUPPORT_EXPIRED_RECHARGE.getCode());
                }
            }
        }
    }

    ParkingCardDTO convertCardInfo(ParkingLot parkingLot) {
        ParkingCardDTO parkingCardDTO = new ParkingCardDTO();

        parkingCardDTO.setOwnerType(parkingLot.getOwnerType());
        parkingCardDTO.setOwnerId(parkingLot.getOwnerId());
        parkingCardDTO.setParkingLotId(parkingLot.getId());
        parkingCardDTO.setIsValid(true);//兼容历史app

        return parkingCardDTO;
    }

    void updateParkingRechargeOrderRateInfo(ParkingLot parkingLot, ParkingRechargeOrder order) {
        ParkingRechargeRate rate = parkingProvider.findParkingRechargeRatesById(Long.parseLong(order.getRateToken()));
        if(null == rate) {
            LOGGER.error("Rate not found, cmd={}", order);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Rate not found.");
        }
        order.setRateName(rate.getRateName());

        checkAndSetOrderPrice(parkingLot, order, rate.getPrice());

    }

    void checkAndSetOrderPrice(ParkingLot parkingLot, ParkingRechargeOrder order, BigDecimal ratePrice) {

        BigDecimal originalPrice = ratePrice;
        if (null != parkingLot.getMonthlyDiscountFlag()) {
            if (ParkingConfigFlag.SUPPORT.getCode() == parkingLot.getMonthlyDiscountFlag()) {
                ratePrice = ratePrice.multiply(new BigDecimal(parkingLot.getMonthlyDiscount()))
                        .divide(new BigDecimal(10), CARD_RATE_RETAIN_DECIMAL, RoundingMode.HALF_UP);
            }
        }
        if (order.getPrice().compareTo(ratePrice) != 0) {
            LOGGER.error("Invalid order price, orderPrice={}, ratePrice={}", order.getPrice(), ratePrice);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid order price.");
        }
        order.setOriginalPrice(originalPrice);
    }

    /**
     * 有些停车场没有月卡类型，默认返回的就是月卡的费率，此时设置一个默认的月卡类型，以供app显示
     * @return
     */
    ParkingCardType createDefaultCardType() {

        String json = configProvider.getValue("parking.default.card.type", "");
        ParkingCardType cardType = JSONObject.parseObject(json, ParkingCardType.class);

        return cardType;
    }

    @Override
    public ParkingRechargeRateDTO createParkingRechargeRate(CreateParkingRechargeRateCommand cmd){

        ParkingRechargeRate parkingRechargeRate = new ParkingRechargeRate();
        parkingRechargeRate.setOwnerType(cmd.getOwnerType());
        parkingRechargeRate.setOwnerId(cmd.getOwnerId());
        parkingRechargeRate.setParkingLotId(cmd.getParkingLotId());
        parkingRechargeRate.setCardType(cmd.getCardType());

    	//费率 名称默认设置 by sw
        Map<String, Object> map = new HashMap<>();
        map.put("count", cmd.getMonthCount().intValue());
        String scope = ParkingNotificationTemplateCode.SCOPE;
        int code = ParkingNotificationTemplateCode.DEFAULT_RATE_NAME;
        String locale = Locale.SIMPLIFIED_CHINESE.toString();
        String rateName = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
        parkingRechargeRate.setRateName(rateName);

        parkingRechargeRate.setMonthCount(cmd.getMonthCount());
        parkingRechargeRate.setPrice(cmd.getPrice());
        parkingRechargeRate.setCreatorUid(UserContext.currentUserId());
        parkingRechargeRate.setCreateTime(new Timestamp(System.currentTimeMillis()));
        parkingRechargeRate.setStatus(ParkingRechargeRateStatus.ACTIVE.getCode());
        parkingProvider.createParkingRechargeRate(parkingRechargeRate);
        return ConvertHelper.convert(parkingRechargeRate, ParkingRechargeRateDTO.class);
    }

    @Override
    public void deleteParkingRechargeRate(DeleteParkingRechargeRateCommand cmd){
        ParkingRechargeRate rate = parkingProvider.findParkingRechargeRatesById(Long.parseLong(cmd.getRateToken()));
        if(rate == null){
            LOGGER.error("ParkingRechargeRate not found, cmd={}", cmd);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "ParkingRechargeRate not found");
        }
        parkingProvider.deleteParkingRechargeRate(rate);
    }

    ParkingCardRequest getParkingCardRequestByOrder(ParkingRechargeOrder order) {
        List<ParkingCardRequest> list = parkingProvider.listParkingCardRequests(order.getCreatorUid(), order.getOwnerType(),
                order.getOwnerId(), order.getParkingLotId(), order.getPlateNumber(), ParkingCardRequestStatus.SUCCEED.getCode(),
                null, null, null, null);

        LOGGER.debug("ParkingCardRequest list size={}", list.size());

        ParkingCardRequest parkingCardRequest = null;

        for(ParkingCardRequest p: list) {
            FlowCase flowCase = flowCaseProvider.getFlowCaseById(p.getFlowCaseId());

            Flow flow = flowProvider.findSnapshotFlow(flowCase.getFlowMainId(), flowCase.getFlowVersion());
            ParkingLot parkingLot = parkingProvider.findParkingLotById(order.getParkingLotId());
            Integer tag1 = parkingLot.getFlowMode();
            if(null == tag1) {
                LOGGER.error("Flow tag is null, flow={}", flow);
                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                        "Flow tag is null.");
            }
            if(ParkingRequestFlowType.INTELLIGENT.getCode().equals(Integer.valueOf(tag1))) {
                parkingCardRequest = p;
                break;
            }
        }

        return parkingCardRequest;
    }

    void updateFlowStatus(ParkingCardRequest parkingCardRequest) {
        User user = UserContext.current().getUser();
        LOGGER.debug("ParkingCardRequest pay callback user={}", user);

        dbProvider.execute((TransactionStatus transactionStatus) -> {

            if(null != parkingCardRequest) {
                FlowCase flowCase = flowCaseProvider.getFlowCaseById(parkingCardRequest.getFlowCaseId());

                FlowAutoStepDTO stepDTO = new FlowAutoStepDTO();
                stepDTO.setFlowCaseId(parkingCardRequest.getFlowCaseId());
                stepDTO.setFlowMainId(flowCase.getFlowMainId());
                stepDTO.setFlowVersion(flowCase.getFlowVersion());
                stepDTO.setFlowNodeId(flowCase.getCurrentNodeId());
                stepDTO.setAutoStepType(FlowStepType.APPROVE_STEP.getCode());
                stepDTO.setStepCount(flowCase.getStepCount());
                flowService.processAutoStep(stepDTO);

                parkingCardRequest.setStatus(ParkingCardRequestStatus.OPENED.getCode());
                parkingCardRequest.setOpenCardTime(new Timestamp(System.currentTimeMillis()));
                parkingProvider.updateParkingCardRequest(parkingCardRequest);

            }
            return null;
        });
    }

    @Override
    public ParkingTempFeeDTO getParkingTempFee(ParkingLot parkingLot, String plateNumber) {
        return null;
    }

    @Override
    public ParkingFreeSpaceNumDTO getFreeSpaceNum(GetFreeSpaceNumCommand cmd) {
        return null;
    }

    @Override
    public ParkingCarLocationDTO getCarLocation(ParkingLot parkingLot, GetCarLocationCommand cmd) {
        return null;
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

        ParkingRechargeRateDTO rate = getOpenCardRate(parkingCardRequest);

        OpenCardInfoDTO dto = ConvertHelper.convert(rate, OpenCardInfoDTO.class);
//            dto.setOwnerId(cmd.getOwnerId());
//            dto.setOwnerType(cmd.getOwnerType());
//            dto.setParkingLotId(cmd.getParkingLotId());
//            dto.setRateToken(rate.getRuleId());
//            Map<String, Object> map = new HashMap<String, Object>();
//            map.put("count", rate.getRuleAmount());
//            String scope = ParkingNotificationTemplateCode.SCOPE;
//            int code = ParkingNotificationTemplateCode.DEFAULT_RATE_NAME;
//            String locale = "zh_CN";
//            String rateName = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
//            dto.setRateName(rateName);
//            dto.setCardType(typeName);
//            dto.setMonthCount(new BigDecimal(rate.getRuleAmount()));
//            dto.setPrice(new BigDecimal(rate.getRuleMoney()).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP));

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

                BigDecimal price = dto.getPrice().multiply(new BigDecimal(requestMonthCount - 1))
                        .add(dto.getPrice().multiply(new BigDecimal(maxDay-today + 1))
                                .divide(new BigDecimal(DAY_COUNT), OPEN_CARD_RETAIN_DECIMAL, RoundingMode.HALF_UP));
                dto.setPayMoney(price);
            }

            dto.setOrderType(ParkingOrderType.OPEN_CARD.getCode());


        return dto;
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

    /**
     * 需要开卡的停车场，需要实现这个方法
     * @param parkingCardRequest
     * @return
     */
    ParkingRechargeRateDTO getOpenCardRate(ParkingCardRequest parkingCardRequest) {
        return null;
    }

    /**
     * 查询 过期月卡充值信息
     * @param cmd
     * @return
     */
    @Override
    public ParkingExpiredRechargeInfoDTO getExpiredRechargeInfo(ParkingLot parkingLot, GetExpiredRechargeInfoCommand cmd) {
        return null;
    }

    @Override
    public void setCellValues(List<ParkingRechargeOrder> list, Sheet sheet) {
        SimpleDateFormat datetimeSF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for(int i=0, size = list.size();i<size;i++){
            Row tempRow = sheet.createRow(i + 1);
            ParkingRechargeOrder order = list.get(i);
            tempRow.createCell(0).setCellValue(String.valueOf(order.getOrderNo()));
            tempRow.createCell(1).setCellValue(order.getPlateNumber());
            tempRow.createCell(2).setCellValue(order.getPlateOwnerName());
            tempRow.createCell(3).setCellValue(order.getPayerPhone());
            tempRow.createCell(4).setCellValue(datetimeSF.format(order.getCreateTime()));
            tempRow.createCell(5).setCellValue("");
            tempRow.createCell(6).setCellValue("");
            if (order.getRechargeType()!=null &&
                    order.getRechargeType().byteValue()==ParkingRechargeType.MONTHLY.getCode()) {
                if(order.getStartPeriod()!=null) {
                    tempRow.createCell(5).setCellValue(datetimeSF.format(order.getStartPeriod()));
                }
                if(order.getEndPeriod()!=null) {
                    tempRow.createCell(6).setCellValue(datetimeSF.format(order.getEndPeriod()));
                }
            }
            tempRow.createCell(7).setCellValue(null == order.getMonthCount()?"":order.getMonthCount().toString());
            tempRow.createCell(8).setCellValue("");
            tempRow.createCell(9).setCellValue("");
            tempRow.createCell(10).setCellValue("");
            if (order.getRechargeType()!=null &&
                    order.getRechargeType().byteValue()==ParkingRechargeType.TEMPORARY.getCode()) {
                if(order.getStartPeriod()!=null) {
                    tempRow.createCell(8).setCellValue(datetimeSF.format(order.getStartPeriod()));
                }
                if(order.getEndPeriod()!=null) {
                    tempRow.createCell(9).setCellValue(datetimeSF.format(order.getEndPeriod()));
                }
                if(order.getParkingTime()!=null) {
                    tempRow.createCell(10).setCellValue(order.getParkingTime());
                }
            }
            tempRow.createCell(11).setCellValue(String.valueOf(order.getPrice().doubleValue()));
            tempRow.createCell(12).setCellValue(order.getOriginalPrice()==null?
                    String.valueOf(order.getPrice().doubleValue())
                    :String.valueOf(order.getOriginalPrice().doubleValue()));
            VendorType type = VendorType.fromCode(order.getPaidType());
            tempRow.createCell(13).setCellValue(null==type?"":type.getDescribe());
            ParkingRechargeType parkingRechargeType = ParkingRechargeType.fromCode(order.getRechargeType());
            tempRow.createCell(14).setCellValue(parkingRechargeType==null?"":parkingRechargeType.getDescribe());
            ParkingRechargeOrderStatus orderStatus = ParkingRechargeOrderStatus.fromCode(order.getStatus());
            tempRow.createCell(15).setCellValue(orderStatus==null?"":orderStatus.getDescription());
            ParkingPaySourceType sourceType = ParkingPaySourceType.fromCode(order.getPaySource());
            tempRow.createCell(16).setCellValue(sourceType==null?"":sourceType.getDesc());
        }
    }
    @Override
    public List<ParkingActualClearanceLogDTO> getTempCardLogs(ParkingClearanceLog r){
        return null;
    }
    @Override
    public String applyTempCard(ParkingClearanceLog log){
        return null;
    }

    @Override
    public void refreshToken() {

    }
}
