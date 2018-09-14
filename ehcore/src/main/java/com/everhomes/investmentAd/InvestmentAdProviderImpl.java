package com.everhomes.investmentAd;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.DbProvider;
import com.everhomes.investmentAd.InvestmentAdProvider;
import com.everhomes.sequence.SequenceProvider;

@Component
public class InvestmentAdProviderImpl implements InvestmentAdProvider{

	private static final Logger LOGGER = LoggerFactory.getLogger(InvestmentAdProviderImpl.class);
	
	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequnceProvider;
	
	
}
