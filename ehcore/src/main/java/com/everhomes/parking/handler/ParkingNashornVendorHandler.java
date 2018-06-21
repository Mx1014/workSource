// @formatter:off
package com.everhomes.parking.handler;

import com.everhomes.parking.ParkingLot;
import com.everhomes.parking.ParkingRechargeOrder;
import com.everhomes.parking.ParkingVendorHandler;
import com.everhomes.parking.clearance.ParkingClearanceLog;
import com.everhomes.rest.parking.*;
import com.everhomes.rest.parking.clearance.ParkingActualClearanceLogDTO;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author dengs[shuang.deng@zuolin.com]
 * @Date 2018/5/29 15:47
 */
//对接js
@Component(ParkingVendorHandler.PARKING_VENDOR_PREFIX+"NASHORN")
public class ParkingNashornVendorHandler extends DefaultParkingVendorHandler{

    @Override
    public List<ParkingCardDTO> listParkingCardsByPlate(ParkingLot parkingLot, String plateNumber) {
        return null;
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
    public ParkingRechargeRateDTO createParkingRechargeRate(CreateParkingRechargeRateCommand cmd) {
        return null;
    }

    @Override
    public void deleteParkingRechargeRate(DeleteParkingRechargeRateCommand cmd) {

    }

    @Override
    public ListCardTypeResponse listCardType(ListCardTypeCommand cmd) {
        return null;
    }

    @Override
    public void updateParkingRechargeOrderRate(ParkingLot parkingLot, ParkingRechargeOrder order) {

    }

    @Override
    public ParkingTempFeeDTO getParkingTempFee(ParkingLot parkingLot, String plateNumber) {
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
    public ParkingFreeSpaceNumDTO getFreeSpaceNum(GetFreeSpaceNumCommand cmd) {
        return null;
    }

    @Override
    public ParkingCarLocationDTO getCarLocation(ParkingLot parkingLot, GetCarLocationCommand cmd) {
        return null;
    }

    @Override
    public ParkingExpiredRechargeInfoDTO getExpiredRechargeInfo(ParkingLot parkingLot, GetExpiredRechargeInfoCommand cmd) {
        return null;
    }

    @Override
    public void setCellValues(List<ParkingRechargeOrder> list, Sheet sheet) {

    }

    @Override
    public List<ParkingActualClearanceLogDTO> getTempCardLogs(ParkingClearanceLog r) {
        return null;
    }

    @Override
    public String applyTempCard(ParkingClearanceLog log) {
        return null;
    }

    @Override
    public void refreshToken() {

    }
}
