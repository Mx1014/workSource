// @formatter:off
package com.everhomes.parking;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.rest.parking.ParkingLotVendor;
import org.jooq.SortField;

public interface ParkingProvider {
	ParkingVendor findParkingVendorByName(String name);
	
    ParkingLot findParkingLotById(Long id);
    
    List<ParkingLot> listParkingLots(String ownerType,Long ownerId);
    
    List<ParkingRechargeRate> listParkingRechargeRates(String ownerType,Long ownerId, Long parkingLotId, String cardType);
    
    void createParkingRechargeRate(ParkingRechargeRate parkingRechargeRate);

    void requestParkingCard(ParkingCardRequest parkingCardRequest);
    
    List<ParkingCardRequest> listParkingCardRequests(Long userId, String ownerType, Long ownerId, Long parkingLotId,
    		String plateNumber, Byte requestStatus, Byte unRequestStatus, Long flowId, Long pageAnchor, Integer pageSize);
    
    List<ParkingCardRequest> listParkingCardRequests(Long userId, String ownerType, Long ownerId, Long parkingLotId,
    		String plateNumber, String order, Long pageAnchor, Integer pageSize);
    
    List<ParkingRechargeOrder> listParkingRechargeOrders(String ownerType, Long ownerId, Long parkingLotId,
    		String plateNumber, Long userId, Long pageAnchor, Integer pageSize);
    
    List<ParkingRechargeOrder> searchParkingRechargeOrders(String ownerType, Long ownerId, Long parkingLotId,
    		String plateNumber, String plateOwnerName, String payerPhone, Timestamp startDate,Timestamp endDate,
    		Byte rechargeType, String paidType, String cardNumber,  Byte status, Long pageAnchor, Integer pageSize);
    
    BigDecimal countParkingRechargeOrders(String ownerType, Long ownerId, Long parkingLotId,
    		String plateNumber, String plateOwnerName, String payerPhone, Timestamp startDate, Timestamp endDate,
    		Byte rechargeType, String paidType);
    
    void createParkingRechargeOrder(ParkingRechargeOrder parkingRechargeOrder);
    
    void deleteParkingRechargeRate(ParkingRechargeRate parkingRechargeRate);
    
    ParkingRechargeRate findParkingRechargeRatesById(Long id);
    
    List<ParkingCardRequest> searchParkingCardRequests(String ownerType, Long ownerId, Long parkingLotId,
                                                       String plateNumber, String plateOwnerName, String plateOwnerPhone, Timestamp startDate,
                                                       Timestamp endDate, Byte status, String carBrand, String carSerieName, String plateOwnerEntperiseName,
                                                       Long flowId, SortField order, Long pageAnchor, Integer pageSize);
    
    void setParkingLotConfig(ParkingLot parkingLot);
    
    ParkingCardRequest findParkingCardRequestById(Long id);
    
    void updateParkingCardRequest(List<ParkingCardRequest> list);

    Integer waitingCardCount(String ownerType, Long ownerId, Long parkingLotId, Timestamp createTime);
    
    ParkingRechargeOrder findParkingRechargeOrderById(Long id);

    ParkingRechargeOrder findParkingRechargeOrderByOrderNo(Long orderNo);

    void updateParkingRechargeOrder(ParkingRechargeOrder order);

    List<ParkingRechargeOrder> listParkingRechargeOrders(Integer pageSize,
			Timestamp startDate, Timestamp endDate,List<Byte> statuses,
			CrossShardListingLocator locator);
    
    List<ParkingCarSerie> listParkingCarSeries(Long parentId, Long pageAnchor, Integer pageSize);
    
    ParkingFlow getParkingRequestCardConfig(String ownerType, Long ownerId, Long parkingLotId, Long flowId);
    
    void updatetParkingRequestCardConfig(ParkingFlow parkingFlow);
    
    void createParkingRequestCardConfig(ParkingFlow parkingFlow);
    
    void createParkingStatistic(ParkingStatistic parkingStatistic);
    
    List<ParkingStatistic> listParkingStatistics(String ownerType, Long ownerId, Long parkingLotId, Timestamp dateStr);

    ParkingCarSerie findParkingCarSerie(Long id);
    
    void createParkingAttachment(ParkingAttachment parkingAttachment);
    
    List<ParkingAttachment> listParkingAttachments(Long ownerId, String ownerType);
    
    void updateParkingCardRequest(ParkingCardRequest parkingCardRequest);
    
    Integer countParkingCardRequest(String ownerType, Long ownerId, Long parkingLotId, Long flowId, Byte geStatus, Byte status);
    
    ParkingFlow findParkingRequestCardConfig(Long id);
}
