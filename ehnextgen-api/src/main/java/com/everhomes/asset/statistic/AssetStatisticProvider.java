package com.everhomes.asset.statistic;

import java.util.List;

import com.everhomes.rest.asset.statistic.ListBillStatisticByBuildingDTO;
import com.everhomes.rest.asset.statistic.ListBillStatisticByCommunityDTO;

/**
 * @author created by ycx
 * @date 下午3:53:07
 */
public interface AssetStatisticProvider {

	void createStatisticByCommnunity(Integer namespaceId, Long ownerId, String ownerType, String dateStr);
	
	void createStatisticByBuilding(Integer namespaceId, Long ownerId, String ownerType, String dateStr,String buildingName);

	boolean checkIsNeedRefreshStatistic(Integer namespaceId, Long ownerId, String ownerType, String dateStr,String beforeDateStr);
			
	boolean checkIsNeedRefreshStatistic(Integer namespaceId, Long ownerId, String ownerType, String dateStr,String buildingName, String beforeDateStr);
			
	void updateStatisticByCommnunity(Integer namespaceId, Long ownerId, String ownerType, String dateStr);
	
	void updateStatisticByBuilding(Integer namespaceId, Long ownerId, String ownerType, String dateStr,String buildingName);

	List<ListBillStatisticByCommunityDTO> listBillStatisticByCommunity(Integer pageOffSet, Integer pageSize, 
			Integer namespaceId, List<Long> ownerIdList, String ownerType, String dateStrBegin, String dateStrEnd);
	
	List<ListBillStatisticByBuildingDTO> listBillStatisticByBuilding(Integer pageOffSet, Integer pageSize,
			Integer namespaceId, Long ownerId, String ownerType, String dateStrBegin, String dateStrEnd,
			List<String> buildingNameList);
	
	ListBillStatisticByCommunityDTO listBillStatisticByCommunityTotal(Integer namespaceId, List<Long> ownerIdList,
			String ownerType, String dateStrBegin, String dateStrEnd);
	
	ListBillStatisticByBuildingDTO listBillStatisticByBuildingTotal(Integer namespaceId, Long ownerId, String ownerType,
			String dateStrBegin, String dateStrEnd, List<String> buildingNameList);

	/**
	 * 提供给资产获取“缴费信息汇总表-项目”列表接口
	 * @param namespaceId
	 * @param ownerIdList
	 * @param ownerType
	 * @param dateStrBegin
	 * @param dateStrEnd
	 * @return
	 */
	List<ListBillStatisticByCommunityDTO> listBillStatisticByCommunityForProperty(Integer namespaceId,
			List<Long> ownerIdList, String ownerType, String dateStrBegin, String dateStrEnd);

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
