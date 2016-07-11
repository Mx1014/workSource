package com.everhomes.techpark.park;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.parking.ParkingRechargeOrder;
import com.everhomes.parking.ParkingRechargeRate;

public interface ParkProvider {

	void addCharge(ParkCharge parkCharge);
	void deleteCharge(ParkCharge parkCharge);
	
	List<ParkCharge> listParkingChargeByEnterpriseCommunityId(Long communityId,String cardType, Integer offset, Integer pageSize);
	void createRechargeOrder(RechargeInfo order);
	
	List<RechargeInfo> listRechargeRecord(Long communityId, Long rechargeUid, CrossShardListingLocator locator, int count);
	void applyParkingCard(ParkApplyCard apply);
	
	int waitingCardCount(Long communityId);
	
	boolean isApplied(String plateNumber);
	
	List<ParkApplyCard> searchApply(Long communityId, String applierName, String applierPhone, String plateNumber, Byte applyStatus, Timestamp beginDay, Timestamp endDay, CrossShardListingLocator locator, int count);

	List<RechargeInfo> searchRechargeRecord(Long startTime, Long endTime, Byte rechargeStatus, Long communityId, String ownerName, String rechargePhone, String plateNumber, CrossShardListingLocator locator, int count);

	List<ParkApplyCard> searchTopAppliers(int count, Long communityId);
	
	void updateParkApplyCard(ParkApplyCard apply);
	
	ParkApplyCard findApplierByPhone(String applierPhone, Long communityId);
	
	void updateInvalidAppliers();
	
	Set<String> getRechargedPlate(Long rechargeUid);
	
	int getPaymentRanking(Timestamp rechargeTime, Timestamp begin);
	
	List<RechargeInfo> findPaysuccessAndWaitingrefreshInfo();
	ParkCharge findParkingChargeById(Long id);
	PreferentialRule findPreferentialRuleByCommunityId(String ownerType, Long ownerId);
	
	void updatePreferentialRuleById(PreferentialRule preferentialRule);
	
	ParkingRechargeRate findParkingRechargeRates(String ownerType,Long ownerId,Long parkingLotId,BigDecimal monthCount,BigDecimal price);
	ParkingRechargeOrder findParkingRechargeOrderByOrderNo(Long orderNo);
}
