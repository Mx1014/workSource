package com.everhomes.investmentAd;

import com.everhomes.rest.investmentAd.InvestmentAdDetailDTO;

import java.util.List;

import com.everhomes.rest.investmentAd.ChangeInvestmentAdOrderCommand;
import com.everhomes.rest.investmentAd.ChangeInvestmentStatusCommand;
import com.everhomes.rest.investmentAd.CreateInvestmentAdCommand;
import com.everhomes.rest.investmentAd.DeleteInvestmentAdCommand;
import com.everhomes.rest.investmentAd.GetInvestmentAdCommand;
import com.everhomes.rest.investmentAd.GetRelatedAssetsCommand;
import com.everhomes.rest.investmentAd.IntentionCustomerCommand;
import com.everhomes.rest.investmentAd.ListInvestmentAdCommand;
import com.everhomes.rest.investmentAd.ListInvestmentAdResponse;
import com.everhomes.rest.investmentAd.RelatedAssetDTO;
import com.everhomes.rest.investmentAd.UpdateInvestmentAdCommand;

public interface InvestmentAdService {

	void createInvestmentAd(CreateInvestmentAdCommand cmd);

	void deleteInvestmentAd(DeleteInvestmentAdCommand cmd);

	void updateInvestmentAd(UpdateInvestmentAdCommand cmd);

	ListInvestmentAdResponse listInvestmentAds(ListInvestmentAdCommand cmd);

	void changeInvestmentAdOrder(ChangeInvestmentAdOrderCommand cmd);

	InvestmentAdDetailDTO getInvestmentAd(GetInvestmentAdCommand cmd);

	void exportInvestmentAds(ListInvestmentAdCommand cmd);

	void changeInvestmentStatus(ChangeInvestmentStatusCommand cmd);

	List<Long> transformToCustomer(IntentionCustomerCommand cmd);

	List<RelatedAssetDTO> getRelatedAssets(GetRelatedAssetsCommand cmd);

}
