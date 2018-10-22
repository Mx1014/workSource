
package com.everhomes.asset.statistic;

import java.util.List;

import com.everhomes.rest.asset.statistic.ListBillStatisticByCommunityCmd;
import com.everhomes.rest.asset.statistic.ListBillStatisticByCommunityDTO;
import com.everhomes.rest.asset.statistic.ListBillStatisticByCommunityResponse;
import com.everhomes.rest.asset.statistic.ListBillStatisticByCommunityTotalCmd;

/**
 * @author created by ycx
 * @date 下午3:55:26
 */
public interface AssetStatisticService {

	ListBillStatisticByCommunityResponse listBillStatisticByCommunity(ListBillStatisticByCommunityCmd cmd);

	/**
	 * 提供给资产获取“缴费信息汇总表-项目”列表接口
	 * @param namespaceId
	 * @param ownerIdList
	 * @param ownerType
	 * @param dateStrBegin
	 * @param dateStrEnd
	 * @return
	 */
	public List<ListBillStatisticByCommunityDTO> listBillStatisticByCommunityForProperty(Integer namespaceId, List<Long> ownerIdList, 
			String ownerType, String dateStrBegin, String dateStrEnd);

	ListBillStatisticByCommunityDTO listBillStatisticByCommunityTotal(ListBillStatisticByCommunityTotalCmd cmd);

	/**
	 * 提供给资产获取“缴费信息汇总表-项目-合计”列表接口
	 * @param namespaceId
	 * @param ownerIdList
	 * @param ownerType
	 * @param dateStrBegin
	 * @param dateStrEnd
	 * @return
	 */
	ListBillStatisticByCommunityDTO listBillStatisticByCommunityTotalForProperty(Integer namespaceId,
			List<Long> ownerIdList, String ownerType, String dateStrBegin, String dateStrEnd);
	
	
}
