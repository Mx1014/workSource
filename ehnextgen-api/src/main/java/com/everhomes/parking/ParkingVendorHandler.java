// @formatter:off
package com.everhomes.parking;

import java.util.List;

import com.everhomes.parking.clearance.ParkingClearanceLog;
import com.everhomes.rest.order.CommonOrderDTO;
import com.everhomes.rest.parking.*;
import com.everhomes.rest.parking.clearance.ParkingActualClearanceLogDTO;
import org.apache.poi.ss.usermodel.Sheet;

public interface ParkingVendorHandler {
    String PARKING_VENDOR_PREFIX = "ParkingVendor-";

    /**
     * 过期充值月数，默认两个月
     */
    int REQUEST_MONTH_COUNT = 2;
    /**
     * 过期充值类型，默认为按实际天数计算
     * ParkingCardExpiredRechargeType.ACTUAL.getCode()
     */
    byte REQUEST_RECHARGE_TYPE = 2;

    /**
     * 根据车牌查询月卡
     * @param parkingLot
     * @param plateNumber
     * @return
     */
    List<ParkingCardDTO> listParkingCardsByPlate(ParkingLot parkingLot, String plateNumber);
    /**
     * 获取费率列表
     * @param parkingLot
     * @param plateNumber
     * @param cardNo
     * @return
     */
    List<ParkingRechargeRateDTO> getParkingRechargeRates(ParkingLot parkingLot,String plateNumber,String cardNo);
    /**
     * 支付订单通知
     * @param order
     * @return
     */
    Boolean notifyParkingRechargeOrderPayment(ParkingRechargeOrder order);
    /**
     * 创建费率
     * @param cmd
     * @return
     */
    ParkingRechargeRateDTO createParkingRechargeRate(CreateParkingRechargeRateCommand cmd);
    /**
     * 删除费率
     * @param cmd
     * @return
     */
    void deleteParkingRechargeRate(DeleteParkingRechargeRateCommand cmd);
    /**
     * 获取卡类型
     * @param cmd
     * @return
     */
    ListCardTypeResponse listCardType(ListCardTypeCommand cmd);
    /**
     * 创建订单时，更新费率信息
     * @param order
     */
    void updateParkingRechargeOrderRate(ParkingLot parkingLot, ParkingRechargeOrder order);
    /**
     * 获取临时车费用
     * @param parkingLot
     * @param plateNumber
     * @return
     */
    ParkingTempFeeDTO getParkingTempFee(ParkingLot parkingLot, String plateNumber);
    /**
     * 获取工作流开卡信息
     * @param cmd
     * @return
     */
    OpenCardInfoDTO getOpenCardInfo(GetOpenCardInfoCommand cmd);
    /**
     * 获取锁车信息
     * @param cmd
     * @return
     */
    ParkingCarLockInfoDTO getParkingCarLockInfo(GetParkingCarLockInfoCommand cmd);
    /**
     * 锁车/解锁
     * @param cmd
     * @return
     */
    void lockParkingCar(LockParkingCarCommand cmd);
    /**
     * 获取停车场当前在场车数量
     * @param cmd
     * @return
     */
	GetParkingCarNumsResponse getParkingCarNums(GetParkingCarNumsCommand cmd);
    /**
     * 寻车 获取空余数量
     * @param cmd
     * @return
     */
    ParkingFreeSpaceNumDTO getFreeSpaceNum(GetFreeSpaceNumCommand cmd);
    /**
     * 寻车 获取车位置
     * @param cmd
     * @return
     */
    ParkingCarLocationDTO getCarLocation(ParkingLot parkingLot, GetCarLocationCommand cmd);

    /**
     * 寻车 获取车位置
     * @param cmd
     * @return
     */
    ParkingExpiredRechargeInfoDTO getExpiredRechargeInfo(ParkingLot parkingLot, GetExpiredRechargeInfoCommand cmd);
    
    /**
     * excel导出
     * @param list
     * @param sheet
     */
    void setCellValues(List<ParkingRechargeOrder> list, Sheet sheet);

    List<ParkingActualClearanceLogDTO> getTempCardLogs(ParkingClearanceLog r);

    String applyTempCard(ParkingClearanceLog log);

    void refreshToken();
}
