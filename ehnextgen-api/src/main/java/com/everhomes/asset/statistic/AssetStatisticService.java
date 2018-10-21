
package com.everhomes.asset.statistic;

import com.everhomes.rest.asset.statistic.ListBillStatisticByCommunityCmd;
import com.everhomes.rest.asset.statistic.ListBillStatisticByCommunityResponse;

/**
 * @author created by ycx
 * @date 下午3:55:26
 */
public interface AssetStatisticService {

	ListBillStatisticByCommunityResponse listBillStatisticByCommunity(ListBillStatisticByCommunityCmd cmd);
	
	
	
}
