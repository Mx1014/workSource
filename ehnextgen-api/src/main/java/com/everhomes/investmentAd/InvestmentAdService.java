package com.everhomes.investmentAd;

import com.everhomes.rest.investmentAd.InvestmentAdDetailDTO;
import com.everhomes.rest.investmentAd.ChangeInvestmentAdOrderCommand;
import com.everhomes.rest.investmentAd.CreateInvestmentAdCommand;
import com.everhomes.rest.investmentAd.DeleteInvestmentAdCommand;
import com.everhomes.rest.investmentAd.GetInvestmentAdCommand;
import com.everhomes.rest.investmentAd.ListInvestmentAdCommand;
import com.everhomes.rest.investmentAd.ListInvestmentAdResponse;
import com.everhomes.rest.investmentAd.UpdateInvestmentAdCommand;

public interface InvestmentAdService {

	void deleteInvestmentAd(DeleteInvestmentAdCommand cmd);

	void createInvestmentAd(CreateInvestmentAdCommand cmd);

	void updateInvestmentAd(UpdateInvestmentAdCommand cmd);

	ListInvestmentAdResponse listInvestmentAds(ListInvestmentAdCommand cmd);

	void changeInvestmentAdOrder(ChangeInvestmentAdOrderCommand cmd);

	InvestmentAdDetailDTO getInvestmentAd(GetInvestmentAdCommand cmd);

}
