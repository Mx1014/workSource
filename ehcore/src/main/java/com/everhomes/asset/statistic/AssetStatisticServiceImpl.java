
package com.everhomes.asset.statistic;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.rest.asset.statistic.ListBillStatisticByCommunityCmd;
import com.everhomes.rest.asset.statistic.ListBillStatisticByCommunityDTO;

/**
 * @author created by ycx
 * @date 下午8:16:22
 */
@Component
public class AssetStatisticServiceImpl implements AssetStatisticService {
	
	@Autowired
	private AssetStatisticProvider assetStatisticProvider;

	public List<ListBillStatisticByCommunityDTO> listBillStatisticByCommunity(ListBillStatisticByCommunityCmd cmd) {
		return assetStatisticProvider.listBillStatisticByCommunity(cmd.getNamespaceId(), cmd.getOwnerIdList(), cmd.getOwnerType(),
				cmd.getDateStrBegin(), cmd.getDateStrEnd());
	}
	
	
}