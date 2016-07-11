package com.everhomes.techpark.park;

import java.util.Set;

import com.everhomes.rest.techpark.onlinePay.OnlinePayBillCommand;
import com.everhomes.rest.techpark.park.ApplyParkCardDTO;
import com.everhomes.rest.techpark.park.ApplyParkCardList;
import com.everhomes.rest.techpark.park.CreateParkingChargeCommand;
import com.everhomes.rest.techpark.park.CreateRechargeOrderCommand;
import com.everhomes.rest.techpark.park.DeleteParkingChargeCommand;
import com.everhomes.rest.techpark.park.FetchCardCommand;
import com.everhomes.rest.techpark.park.ListCardTypeCommand;
import com.everhomes.rest.techpark.park.ListCardTypeResponse;
import com.everhomes.rest.techpark.park.OfferCardCommand;
import com.everhomes.rest.techpark.park.ParkResponseList;
import com.everhomes.rest.techpark.park.ParkResponseListCommand;
import com.everhomes.rest.techpark.park.PaymentRankingCommand;
import com.everhomes.rest.techpark.park.PlateInfo;
import com.everhomes.rest.techpark.park.PlateNumberCommand;
import com.everhomes.rest.techpark.park.PreferentialRulesDTO;
import com.everhomes.rest.techpark.park.QryPreferentialRuleByCommunityIdCommand;
import com.everhomes.rest.techpark.park.RechargeOrderDTO;
import com.everhomes.rest.techpark.park.RechargeRecordList;
import com.everhomes.rest.techpark.park.RechargeRecordListCommand;
import com.everhomes.rest.techpark.park.RechargeSuccessResponse;
import com.everhomes.rest.techpark.park.SearchApplyCardCommand;
import com.everhomes.rest.techpark.park.SearchRechargeRecordCommand;
import com.everhomes.rest.techpark.park.SetPreferentialRuleCommand;

public interface ParkService {
	
	void addCharge(CreateParkingChargeCommand cmd);
	
	void deleteCharge(DeleteParkingChargeCommand cmd);
	
	ParkResponseList listParkingCharge(ParkResponseListCommand cmd);
	
	RechargeOrderDTO createRechargeOrder(CreateRechargeOrderCommand cmd);
	
	RechargeSuccessResponse getRechargeStatus(Long billId);
	
	RechargeRecordList listRechargeRecord(RechargeRecordListCommand cmd);
	
	String applyParkingCard(PlateNumberCommand cmd);
	
	ApplyParkCardList searchApplyCardList(SearchApplyCardCommand cmd);
	
	RechargeRecordList searchRechargeRecord(SearchRechargeRecordCommand cmd);
	
	ApplyParkCardList offerCard(OfferCardCommand cmd);

	ApplyParkCardDTO fetchCard(FetchCardCommand cmd);
	
	void invalidApplier();
	
	void  updateRechargeOrder(Long billId);
	
	RechargeSuccessResponse refreshParkingSystem(OnlinePayBillCommand cmd);
	
	Set<String> getRechargedPlate();
	
	PlateInfo verifyRechargedPlate(PlateNumberCommand cmd);
	
	String rechargeTop(PaymentRankingCommand cmd);
	
	void refreshRechargeStatus();

	ListCardTypeResponse listCardType(ListCardTypeCommand cmd);
	
	PreferentialRulesDTO qryPreferentialRuleByCommunityId(QryPreferentialRuleByCommunityIdCommand cmd);
	
	void setPreferentialRule(SetPreferentialRuleCommand cmd);

}
