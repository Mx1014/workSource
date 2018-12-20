// @formatter:off
package com.everhomes.parking;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.order.PaymentOrderRecord;
import com.everhomes.rest.order.ListBizPayeeAccountDTO;
import com.everhomes.rest.parking.ParkingCardType;
import com.everhomes.rest.parking.ParkingRechargeOrderDTO;

import org.jooq.SortField;
import org.jooq.TableField;

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
                                                           String plateNumber, String plateOwnerName, String payerPhone, Timestamp startDate, Timestamp endDate,
                                                           Byte rechargeType, String paidType, String cardNumber, Byte status, String paySource, String keyWords, 
                                                           Long pageAnchor, Integer pageSize, Byte payMode,Integer pageNum);
    
    BigDecimal countParkingRechargeOrders(String ownerType, Long ownerId, Long parkingLotId,
                                          String plateNumber, String plateOwnerName, String payerPhone, Timestamp startDate, Timestamp endDate,
                                          Byte rechargeType, String paidType, String cardNumber, Byte status, String paySource, String keyWords);
    
    void createParkingRechargeOrder(ParkingRechargeOrder parkingRechargeOrder);
    
    void deleteParkingRechargeRate(ParkingRechargeRate parkingRechargeRate);
    
    ParkingRechargeRate findParkingRechargeRatesById(Long id);
    
    List<ParkingCardRequest> searchParkingCardRequests(String ownerType, Long ownerId, Long parkingLotId,
                                                       String plateNumber, String plateOwnerName, String plateOwnerPhone, Timestamp startDate,
                                                       Timestamp endDate, Byte status, String carBrand, String carSeriesName, String plateOwnerEnterpriseName,
                                                       Long flowId, TableField field, int order, String cardTypeId, String ownerKeyWords, Long pageAnchor, 
                                                       Integer pageSize,Integer pageNum);
    
    void updateParkingLot(ParkingLot parkingLot);
    
    ParkingCardRequest findParkingCardRequestById(Long id);
    
    void updateParkingCardRequest(List<ParkingCardRequest> list);

    Integer waitingCardCount(String ownerType, Long ownerId, Long parkingLotId, Timestamp createTime);
    
    ParkingRechargeOrder findParkingRechargeOrderById(Long id);

    ParkingRechargeOrder findParkingRechargeOrderByBizOrderNum(String bizOrderNum);

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

    ParkingInvoiceType findParkingInvoiceTypeById(Long id);

    ParkingCardRequestType findParkingCardTypeByTypeId(String ownerType, Long ownerId, Long parkingLotId, String cardTypeId);

    List<ParkingInvoiceType> listParkingInvoiceTypes(String ownerType, Long ownerId, Long parkingLotId);

    List<ParkingCardRequestType> listParkingCardTypes(String ownerType, Long ownerId, Long parkingLotId);

    void createParkingUserInvoice(ParkingUserInvoice parkingUserInvoice);

    void updateParkingUserInvoice(ParkingUserInvoice parkingUserInvoice);

    ParkingUserInvoice findParkingUserInvoiceByUserId(String ownerType, Long ownerId, Long parkingLotId, Long userId);

    ParkingRechargeRate findParkingRechargeRateByMonthCount(String ownerType, Long ownerId, Long parkingLotId,
                                                            String cardType, BigDecimal monthCount);

    void createParkingCarVerification(ParkingCarVerification parkingCarVerification);

    void updateParkingCarVerification(ParkingCarVerification parkingCarVerification);

    ParkingCarVerification findParkingCarVerificationById(Long id);

    List<ParkingCarVerification> searchParkingCarVerifications(String ownerType, Long ownerId, Long parkingLotId,
                                                               String plateNumber, String plateOwnerName, String plateOwnerPhone,
                                                               Timestamp startDate, Timestamp endDate, Byte status,
                                                               String requestorEnterpriseName, String ownerKeyWords, Long pageAnchor, Integer pageSize);

    List<ParkingCarVerification> listParkingCarVerifications(String ownerType, Long ownerId, Long parkingLotId,
                                                             Long requestorUid, Byte sourceType, Long pageAnchor, Integer pageSize);

    ParkingCarVerification findParkingCarVerificationByUserId(String ownerType, Long ownerId, Long parkingLotId, String plateNumber,
                                                              Long userId);

    ParkingRechargeOrder getParkingRechargeTempOrder(String ownerType, Long ownerId, Long parkingLotId,
                                                     String plateNumber, Timestamp startDate, Timestamp endDate);

    ParkingSpace findParkingSpaceById(Long id);

    void updateParkingSpace(ParkingSpace parkingSpace);

    void createParkingSpace(ParkingSpace parkingSpace);

    List<ParkingSpace> searchParkingSpaces(Integer namespaceId, String ownerType, Long ownerId, Long parkingLotId,
                                           String keyword, String lockStatus, Long parkingHubsId,Long pageAnchor, Integer pageSize);

    List<ParkingSpaceLog> listParkingSpaceLogs(String spaceNo, Long startTime, Long endTime, Long pageAnchor, Integer pageSize);

    ParkingSpace findParkingSpaceByLockId(String lockId);

    ParkingSpace findParkingSpaceBySpaceNo(String spaceNo);

    Integer countParkingSpace(Integer namespaceId, String ownerType, Long ownerId, Long parkingLotId, List<String> spaces);

    ParkingSpace getAnyParkingSpace(Integer namespaceId, String ownerType, Long ownerId, Long parkingLotId, List<String> spaces);

    ParkingSpace getAnyFreeParkingSpace(Integer namespaceId, String ownerType, Long ownerId, Long parkingLotId);

    void createParkingSpaceLog(ParkingSpaceLog log);

    ListBizPayeeAccountDTO createPersonalPayUserIfAbsent(String userId, String accountCode,String userIdenify, String tag1, String tag2, String tag3);

    List<PaymentOrderRecord> listParkingPaymentOrderRecords(Long pageAnchor, Integer pageSize);

    List<ParkingRechargeOrder> listParkingRechargeOrdersByUserId(Long userId,Long startCreateTime,Long endCreateTime, Integer pageSize, Long pageAnchor);

    Long ParkingRechargeOrdersByUserId(Long userId,Long startCreateTime,Long endCreateTime);

    List<ParkingSpace> listParkingSpaceByParkingHubsId(Integer namespaceId, String ownerType, Long ownerId, Long parkingLotId, Long parkingHubsId);

    List<ParkingLot> findParkingLotByIdHash(String parkingLotToken);

	ParkingRechargeOrderDTO parkingRechargeOrdersByOrderNo(long orderNo);

	ParkingRechargeOrder findParkingRechargeOrderByGeneralOrderId(Long gorderId);

	String findParkingLotNameByVendorName(Integer namespaceId, String vendorName);

	ParkingCardRequest findParkingCardRequestByPlateNumber(String plateNumber);

	Long countRechargeOrdersPageNums(String ownerType, Long ownerId, Long parkingLotId, String plateNumber,
			String plateOwnerName, String payerPhone, Timestamp startDate, Timestamp endDate, Byte rechargeType,
			String paidType, String cardNumber, Byte status, String paySource, String keyWords);

	void createParkingCardType(ParkingCardRequestType parkingCardType);

}
