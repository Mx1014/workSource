package com.everhomes.asset.statistic;

/**
 * @author created by ycx
 * @date 下午3:53:07
 */
public interface AssetStatisticProvider {

	void createStatisticByCommnunity(Integer namespaceId, Long ownerId, String ownerType, String dateStr);
	
	
	
}
