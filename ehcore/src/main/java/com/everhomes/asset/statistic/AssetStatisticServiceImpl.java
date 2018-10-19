
package com.everhomes.asset.statistic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.DbProvider;

/**
 * @author created by ycx
 * @date 下午8:16:22
 */
@Component
public class AssetStatisticServiceImpl implements AssetStatisticService {
	private static final Logger LOGGER = LoggerFactory.getLogger(AssetStatisticServiceImpl.class);
	
	@Autowired
	private DbProvider dbProvider;
	
	
	
	
	
}