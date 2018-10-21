
package com.everhomes.asset.statistic;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.rest.asset.statistic.ListBillStatisticByCommunityCmd;
import com.everhomes.rest.asset.statistic.ListBillStatisticByCommunityDTO;
import com.everhomes.rest.asset.statistic.ListBillStatisticByCommunityResponse;

/**
 * @author created by ycx
 * @date 下午8:16:22
 */
@Component
public class AssetStatisticServiceImpl implements AssetStatisticService {
	
	@Autowired
	private AssetStatisticProvider assetStatisticProvider;
	
	/**
	 * 提供给资产那边做统计的接口
	 * @param namespaceId
	 * @param ownerIdList
	 * @param ownerType
	 * @param dateStrBegin
	 * @param dateStrEnd
	 * @return
	 */
	public List<ListBillStatisticByCommunityDTO> listBillStatisticByCommunityForProperty(Integer namespaceId, List<Long> ownerIdList, 
			String ownerType, String dateStrBegin, String dateStrEnd) {
        List<ListBillStatisticByCommunityDTO> list = assetStatisticProvider.listBillStatisticByCommunityForProperty(
        		namespaceId, ownerIdList, ownerType, dateStrBegin, dateStrEnd);
		return list;
	}
	
	public ListBillStatisticByCommunityResponse listBillStatisticByCommunity(ListBillStatisticByCommunityCmd cmd) {
		ListBillStatisticByCommunityResponse response = new ListBillStatisticByCommunityResponse();
		Long pageAnchor = cmd.getPageAnchor();
        Integer pageSize = cmd.getPageSize();
        //卸货完毕
        if (pageAnchor == null || pageAnchor < 1l) {
            pageAnchor = 0l;
        }
        if(pageSize == null){
            pageSize = 20;
        }
        Integer pageOffSet = pageAnchor.intValue();
        List<ListBillStatisticByCommunityDTO> list = assetStatisticProvider.listBillStatisticByCommunity(pageOffSet, pageSize, 
        		cmd.getNamespaceId(), cmd.getOwnerIdList(), cmd.getOwnerType(),cmd.getDateStrBegin(), cmd.getDateStrEnd());
        if(list.size() <= pageSize){
            response.setNextPageAnchor(null);
        }else {
            response.setNextPageAnchor(pageAnchor+pageSize.longValue());
            list.remove(list.size()-1);
        }
		response.setListBillStatisticByCommunityDTOs(list);
		return response;
	}
	
	
}