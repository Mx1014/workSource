package com.everhomes.techpark.park;

import java.util.Set;

import com.everhomes.rest.parking.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

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
import com.everhomes.util.RuntimeErrorException;


@Component
@Deprecated
public class ParkServiceImpl implements ParkService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ParkServiceImpl.class);

	@Override
	public void addCharge(CreateParkingChargeCommand cmd) {
        LOGGER.error("Not support card request", cmd);
        throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.NOT_SUPPORT_OLD_VERSION,
                "Not support card request");

	}

	@Override
	public void deleteCharge(DeleteParkingChargeCommand cmd) {
        LOGGER.error("Not support card request", cmd);
        throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.NOT_SUPPORT_OLD_VERSION,
                "Not support card request");
	}

	@Override
	public ParkResponseList listParkingCharge(ParkResponseListCommand cmd) {
        LOGGER.error("Not support card request", cmd);
        throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.NOT_SUPPORT_OLD_VERSION,
                "Not support card request");
	}

	@Override
	public RechargeOrderDTO createRechargeOrder(CreateRechargeOrderCommand cmd) {
        LOGGER.error("Not support", cmd);
        throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.NOT_SUPPORT_OLD_VERSION,
                "Not support");

	}

	@Override
	public RechargeSuccessResponse getRechargeStatus(Long billId) {

        LOGGER.error("Not support card request");
        throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.NOT_SUPPORT_OLD_VERSION,
                "Not support card request");
	}

	@Override
	public RechargeRecordList listRechargeRecord(RechargeRecordListCommand cmd) {

        LOGGER.error("Not support card request", cmd);
        throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.NOT_SUPPORT_OLD_VERSION,
                "Not support card request");
	}

	@Override
	public String applyParkingCard(PlateNumberCommand cmd) {

		LOGGER.error("Not support card request", cmd);
		throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.NOT_SUPPORT_OLD_VERSION,
				"Not support card request");

	}

	@Override
	public ApplyParkCardList searchApplyCardList(SearchApplyCardCommand cmd) {

        LOGGER.error("Not support card request", cmd);
        throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.NOT_SUPPORT_OLD_VERSION,
                "Not support card request");
	}

	@Override
	public RechargeRecordList searchRechargeRecord(
			SearchRechargeRecordCommand cmd) {
        LOGGER.error("Not support card request", cmd);
        throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.NOT_SUPPORT_OLD_VERSION,
                "Not support card request");

	}

	@Override
	public ApplyParkCardList offerCard(OfferCardCommand cmd) {

        LOGGER.error("Not support card request", cmd);
        throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.NOT_SUPPORT_OLD_VERSION,
                "Not support card request");
	}

	@Override
	public ApplyParkCardDTO fetchCard(FetchCardCommand cmd) {

        LOGGER.error("Not support card request", cmd);
        throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.NOT_SUPPORT_OLD_VERSION,
                "Not support card request");
	}

	@Override
	public void invalidApplier() {
        LOGGER.error("Not support card request");
        throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.NOT_SUPPORT_OLD_VERSION,
                "Not support card request");
	}

	@Override
	public void updateRechargeOrder(Long billId) {
        LOGGER.error("Not support card request");
        throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.NOT_SUPPORT_OLD_VERSION,
                "Not support card request");
	}

	@Override
	public RechargeSuccessResponse refreshParkingSystem(
			OnlinePayBillCommand cmd) {
        LOGGER.error("Not support", cmd);
        throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.NOT_SUPPORT_OLD_VERSION,
                "Not support");
	}

	@Override
	public Set<String> getRechargedPlate() {
        LOGGER.error("Not support card request");
        throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.NOT_SUPPORT_OLD_VERSION,
                "Not support card request");
	}

	@Override
	public PlateInfo verifyRechargedPlate(PlateNumberCommand cmd) {
        LOGGER.error("Not support", cmd);
        throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.NOT_SUPPORT_OLD_VERSION,
                "Not support");
	}


	@Override
	public String rechargeTop(PaymentRankingCommand cmd) {

        LOGGER.error("Not support card request", cmd);
        throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.NOT_SUPPORT_OLD_VERSION,
                "Not support card request");
	}

	@Override
	public void refreshRechargeStatus() {
        LOGGER.error("Not support");
        throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.NOT_SUPPORT_OLD_VERSION,
                "Not support");

	}

	@Override
	public ListCardTypeResponse listCardType(ListCardTypeCommand cmd) {
        LOGGER.error("Not support card request", cmd);
        throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.NOT_SUPPORT_OLD_VERSION,
                "Not support card request");
	}

	@Override
	public PreferentialRulesDTO qryPreferentialRuleByCommunityId(QryPreferentialRuleByCommunityIdCommand cmd){
        LOGGER.error("Not support card request", cmd);
        throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.NOT_SUPPORT_OLD_VERSION,
                "Not support card request");
	}

	@Override
	public void setPreferentialRule(SetPreferentialRuleCommand cmd){
        LOGGER.error("Not support card request", cmd);
        throw RuntimeErrorException.errorWith(ParkingErrorCode.SCOPE, ParkingErrorCode.NOT_SUPPORT_OLD_VERSION,
                "Not support card request");
	}

}
