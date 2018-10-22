package com.everhomes.asset.statistic;

import java.util.List;

import com.everhomes.rest.asset.statistic.ListBillStatisticByCommunityDTO;

/**
 * @author created by ycx
 * @date 下午3:53:07
 */
public interface AssetStatisticProvider {

	void createStatisticByCommnunity(Integer namespaceId, Long ownerId, String ownerType, String dateStr);

	boolean checkIsNeedRefreshStatistic(Integer namespaceId, Long ownerId, String ownerType, String dateStr,
			String beforeDateStr);

	void updateStatisticByCommnunity(Integer namespaceId, Long ownerId, String ownerType, String dateStr);

	List<ListBillStatisticByCommunityDTO> listBillStatisticByCommunity(Integer pageOffSet, Integer pageSize, 
			Integer namespaceId, List<Long> ownerIdList, String ownerType, String dateStrBegin, String dateStrEnd);

	/**
	 * 提供给资产那边做统计的接口
	 * @param namespaceId
	 * @param ownerIdList
	 * @param ownerType
	 * @param dateStrBegin
	 * @param dateStrEnd
	 * @return
	 */
	List<ListBillStatisticByCommunityDTO> listBillStatisticByCommunityForProperty(Integer namespaceId,
			List<Long> ownerIdList, String ownerType, String dateStrBegin, String dateStrEnd);

	ListBillStatisticByCommunityDTO listBillStatisticByCommunityTotal(Integer namespaceId, List<Long> ownerIdList,
			String ownerType, String dateStrBegin, String dateStrEnd);
	
	
}
