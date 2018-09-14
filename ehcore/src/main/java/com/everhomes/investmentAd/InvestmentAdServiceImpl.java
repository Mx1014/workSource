package com.everhomes.investmentAd;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.everhomes.investmentAd.InvestmentAdService;
import com.everhomes.rest.investmentAd.InvestmentAdDetailDTO;
import com.everhomes.rest.investmentAd.ChangeInvestmentAdOrderCommand;
import com.everhomes.rest.investmentAd.CreateInvestmentAdCommand;
import com.everhomes.rest.investmentAd.DeleteInvestmentAdCommand;
import com.everhomes.rest.investmentAd.GetInvestmentAdCommand;
import com.everhomes.rest.investmentAd.ListInvestmentAdCommand;
import com.everhomes.rest.investmentAd.ListInvestmentAdResponse;
import com.everhomes.rest.investmentAd.UpdateInvestmentAdCommand;

@Component
public class InvestmentAdServiceImpl implements InvestmentAdService{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(InvestmentAdServiceImpl.class);

	@Override
	public void deleteInvestmentAd(DeleteInvestmentAdCommand cmd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createInvestmentAd(CreateInvestmentAdCommand cmd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateInvestmentAd(UpdateInvestmentAdCommand cmd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ListInvestmentAdResponse listInvestmentAds(ListInvestmentAdCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void changeInvestmentAdOrder(ChangeInvestmentAdOrderCommand cmd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public InvestmentAdDetailDTO getInvestmentAd(GetInvestmentAdCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
