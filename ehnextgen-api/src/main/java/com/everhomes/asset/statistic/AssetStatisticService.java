
package com.everhomes.asset.statistic;

import java.util.List;

import com.everhomes.rest.asset.statistic.ListBillStatisticByCommunityCmd;
import com.everhomes.rest.asset.statistic.ListBillStatisticByCommunityDTO;

/**
 * @author created by ycx
 * @date 下午3:55:26
 */
public interface AssetStatisticService {

	List<ListBillStatisticByCommunityDTO> listBillStatisticByCommunity(ListBillStatisticByCommunityCmd cmd);
	
	
	
}
