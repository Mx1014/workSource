// @formatter:off
package com.everhomes.parking;

import java.sql.Timestamp;
import java.util.List;

import com.everhomes.rest.parking.ParkingLotVendor;

public interface ParkingProvider {
	ParkingVendor findParkingVendorByName(String name);
	
    ParkingLot findParkingLotById(Long id);
    
    List<ParkingLot> listParkingLots(String ownerType,Long ownerId);
    
    List<ParkingRechargeRate> listParkingRechargeRates(String ownerType,Long ownerId,Long parkingLotId,String cardType);
    
    void createParkingRechargeRate(ParkingRechargeRate parkingRechargeRate);
    
    boolean isApplied(String plateNumber,Long parkingLotId);
    
    void requestParkingCard(ParkingCardRequest parkingCardRequest);
    
    List<ParkingCardRequest> listParkingCardRequests(Long userId,String ownerType,Long ownerId
    		,Long parkingLotId,String plateNumber,Byte requestStatus,Byte unRequestStatus,
    		Long pageAnchor,Integer pageSize);
    
    List<ParkingCardRequest> listParkingCardRequests(Long userId,String ownerType,Long ownerId
    		,Long parkingLotId,String plateNumber,String order,
    		Long pageAnchor,Integer pageSize);
    
    List<ParkingRechargeOrder> listParkingRechargeOrders(String ownerType,Long ownerId
    		,Long parkingLotId,String plateNumber,Long pageAnchor,Integer pageSize,Long userId);
    
    List<ParkingRechargeOrder> searchParkingRechargeOrders(String ownerType,Long ownerId,
    		Long parkingLotId, String plateNumber,String plateOwnerName,String plateOwnerPhone
    		,String paidType ,String payerName,String payerPhone,Long pageAnchor,Integer pageSize,Timestamp startDate,
    		Timestamp endDate,Byte rechargeStatus/*,Long userId*/);
    
    void createParkingRechargeOrder(ParkingRechargeOrder parkingRechargeOrder);
    
    void deleteParkingRechargeRate(ParkingRechargeRate parkingRechargeRate);
    
    ParkingRechargeRate findParkingRechargeRatesById(Long id);
    
    List<ParkingCardRequest> searchParkingCardRequests(String ownerType,Long ownerId,
			Long parkingLotId,String plateNumber,String plateOwnerName,String plateOwnerPhone,
			Timestamp startDate,Timestamp endDate,Byte status,Long pageAnchor,Integer pageSize);
    
    void setParkingCardReserveDays(ParkingLot parkingLot);
    
    ParkingCardRequest findParkingCardRequestById(Long id);
    
    void updateParkingCardRequest(List<ParkingCardRequest> list);
    
    void updateInvalidAppliers(Timestamp time,Long parkingLotId);
    
    Integer waitingCardCount(String ownerType,Long ownerId
    		,Long parkingLotId,Timestamp createTime);
    
    ParkingRechargeOrder findParkingRechargeOrderById(Long id);
    
    void updateParkingRechargeOrder(ParkingRechargeOrder order);
    
    List<ParkingRechargeOrder> findWaitingParkingRechargeOrders(ParkingLotVendor vendor);
    
    ParkingActivity setParkingActivity(ParkingActivity parkingActivity);
    
    ParkingActivity getParkingActivity(String ownerType,Long ownerId,Long parkingLotId);
    
    void deleteParkingRechargeOrder(ParkingRechargeOrder parkingRechargeOrder);
}
