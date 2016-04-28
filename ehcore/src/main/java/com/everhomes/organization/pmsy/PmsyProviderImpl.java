package com.everhomes.organization.pmsy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.DbProvider;

@Component
public class PmsyProviderImpl implements PmsyProvider {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PmsyProviderImpl.class);
	@Autowired
	private DbProvider dbProvider;
	
	
}
