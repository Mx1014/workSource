
package com.everhomes.asset.statistic;

import java.io.OutputStream;
import java.util.List;

import com.everhomes.rest.asset.statistic.ListBillStatisticByAddressCmd;
import com.everhomes.rest.asset.statistic.ListBillStatisticByAddressDTO;
import com.everhomes.rest.asset.statistic.ListBillStatisticByAddressResponse;
import com.everhomes.rest.asset.statistic.ListBillStatisticByAddressTotalCmd;
import com.everhomes.rest.asset.statistic.ListBillStatisticByBuildingCmd;
import com.everhomes.rest.asset.statistic.ListBillStatisticByBuildingDTO;
import com.everhomes.rest.asset.statistic.ListBillStatisticByBuildingResponse;
import com.everhomes.rest.asset.statistic.ListBillStatisticByBuildingTotalCmd;
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
	
	ListBillStatisticByBuildingResponse listBillStatisticByBuilding(ListBillStatisticByBuildingCmd cmd);
	
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
	
	/**
	 * 提供给资产获取“缴费信息汇总表-楼宇”列表接口
	 * @param namespaceId
	 * @param ownerId
	 * @param ownerType
	 * @param dateStrBegin
	 * @param dateStrEnd
	 * @param buildingNameList
	 * @return
	 */
	public List<ListBillStatisticByBuildingDTO> listBillStatisticByBuildingForProperty(Integer namespaceId, Long ownerId, String ownerType,
			String dateStrBegin, String dateStrEnd, List<String> buildingNameList);

	ListBillStatisticByCommunityDTO listBillStatisticByCommunityTotal(ListBillStatisticByCommunityTotalCmd cmd);
	
	ListBillStatisticByBuildingDTO listBillStatisticByBuildingTotal(ListBillStatisticByBuildingTotalCmd cmd);

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

	/**
	 * 提供给资产获取“缴费信息汇总表-楼宇-合计”列表接口
	 * @param namespaceId
	 * @param ownerId
	 * @param ownerType
	 * @param dateStrBegin
	 * @param dateStrEnd
	 * @param buildingNameList
	 * @return
	 */
	ListBillStatisticByBuildingDTO listBillStatisticByBuildingTotalForProperty(Integer namespaceId, Long ownerId,
			String ownerType, String dateStrBegin, String dateStrEnd, List<String> buildingNameList);

	ListBillStatisticByAddressResponse listBillStatisticByAddress(ListBillStatisticByAddressCmd cmd);

	ListBillStatisticByAddressDTO listBillStatisticByAddressTotal(ListBillStatisticByAddressTotalCmd cmd);

	void exportBillStatisticByCommunity(ListBillStatisticByCommunityCmd cmd);

	OutputStream exportOutputStreamBillStatisticByCommunity(ListBillStatisticByCommunityCmd cmd, Long taskId);
	
	
}
