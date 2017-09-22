package com.everhomes.parking.handler;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.db.DbProvider;
import com.everhomes.flow.*;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.parking.*;
import com.everhomes.rest.flow.FlowAutoStepDTO;
import com.everhomes.rest.flow.FlowStepType;
import com.everhomes.rest.parking.*;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author sw on 2017/8/16.
 */
public abstract class DefaultParkingVendorHandler implements ParkingVendorHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultParkingVendorHandler.class);

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

    boolean checkExpireTime(ParkingLot parkingLot, long expireTime) {
        long now = System.currentTimeMillis();
        long cardReserveTime = 0;

        Byte isSupportRecharge = parkingLot.getExpiredRechargeFlag();
        if(ParkingConfigFlag.SUPPORT.getCode() == isSupportRecharge)	{
            Integer cardReserveDay = parkingLot.getMaxExpiredDay();
            cardReserveTime = cardReserveDay * 24 * 60 * 60 * 1000L;

        }

        return expireTime + cardReserveTime < now;
    }

    ParkingCardDTO convertCardInfo(ParkingLot parkingLot) {
        ParkingCardDTO parkingCardDTO = new ParkingCardDTO();

        parkingCardDTO.setOwnerType(parkingLot.getOwnerType());
        parkingCardDTO.setOwnerId(parkingLot.getOwnerId());
        parkingCardDTO.setParkingLotId(parkingLot.getId());
        parkingCardDTO.setIsValid(true);//兼容历史app

        return parkingCardDTO;
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

    void updateFlowStatus(ParkingRechargeOrder order) {
        User user = UserContext.current().getUser();
        LOGGER.debug("ParkingCardRequest pay callback user={}", user);

        List<ParkingCardRequest> list = parkingProvider.listParkingCardRequests(order.getCreatorUid(), order.getOwnerType(),
                order.getOwnerId(), order.getParkingLotId(), order.getPlateNumber(), ParkingCardRequestStatus.SUCCEED.getCode(),
                null, null, null, null);

        LOGGER.debug("ParkingCardRequest list size={}", list.size());
        dbProvider.execute((TransactionStatus transactionStatus) -> {
            ParkingCardRequest parkingCardRequest = null;
            for(ParkingCardRequest p: list) {
                FlowCase flowCase = flowCaseProvider.getFlowCaseById(p.getFlowCaseId());

                Flow flow = flowProvider.findSnapshotFlow(flowCase.getFlowMainId(), flowCase.getFlowVersion());
                String tag1 = flow.getStringTag1();
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
