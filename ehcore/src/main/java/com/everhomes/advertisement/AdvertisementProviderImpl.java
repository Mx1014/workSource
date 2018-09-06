package com.everhomes.advertisement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.DbProvider;
import com.everhomes.sequence.SequenceProvider;

@Component
public class AdvertisementProviderImpl implements AdvertisementProvider{

	private static final Logger LOGGER = LoggerFactory.getLogger(AdvertisementProviderImpl.class);
	
	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequnceProvider;
	
	
}
