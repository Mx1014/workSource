package com.everhomes.openapi.jindi;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.rentalv2.RentalOrder;
import com.everhomes.rentalv2.Rentalv2Provider;
import com.everhomes.rest.openapi.jindi.JindiActionSiteRentalDTO;
import com.everhomes.rest.openapi.jindi.JindiActionType;
import com.everhomes.rest.openapi.jindi.JindiDataType;
import com.everhomes.rest.openapi.jindi.JindiFetchDataCommand;

/**
 * 
 * <ul>
 * 抓取电商数据
 * </ul>
 */
@Component(JindiOpenHandler.JINDI_OPEN_HANDLER+JindiDataType.JindiDataTypeCode.ACTION_CODE+JindiActionType.JindiActionTypeCode.ACTION_TYPE_SITE_RENTAL_CODE)
public class JindiOpenActionSiteRentalHandler implements JindiOpenHandler {

	@Autowired
	private Rentalv2Provider rentalv2Provider;
	
	@Autowired
	private CommunityProvider communityProvider;
	
	@Override
	public String fetchData(JindiFetchDataCommand cmd) {
		return superFetchData(cmd, new JindiOpenCallback<RentalOrder>() {

			@Override
			public List<RentalOrder> fetchDataByUpdateTimeAndAnchor(Integer namespaceId, Long timestamp,
					Long pageAnchor, int pageSize) {
				return rentalv2Provider.listSiteRentalByUpdateTimeAndAnchor(cmd.getNamespaceId(), cmd.getTimestamp(), cmd.getPageAnchor(), pageSize+1);
			}

			@Override
			public List<RentalOrder> fetchDataByUpdateTime(Integer namespaceId, Long timestamp,
					int pageSize) {
				return rentalv2Provider.listSiteRentalByUpdateTime(cmd.getNamespaceId(), cmd.getTimestamp(), pageSize+1);
			}

			@Override
			public Object complementInfo(JindiFetchDataCommand cmd, RentalOrder src) {
				Long communityId = src.getCommunityId();
				if (communityId != null && communityId.longValue() == 0L) {
					communityId = null;
				}
				JindiActionSiteRentalDTO data = new JindiActionSiteRentalDTO();
				data.setId(src.getId());
				data.setUserId(src.getRentalUid());
				data.setCommunityId(src.getCommunityId());
				data.setPhone(src.getContactPhonenum());
				data.setStartTime(src.getStartTime());
				data.setEndTime(src.getEndTime());
				data.setOrderNo(src.getOrderNo());
				data.setVendorType(src.getVendorType());
				data.setReserveTime(src.getReserveTime());
				data.setPayTotalMoney(src.getPayTotalMoney());
				data.setCreateTime(src.getCreateTime());
				data.setUpdateTime(src.getOperateTime());
				
				if (communityId != null) {
					Community community = communityProvider.findCommunityById(src.getCommunityId());
					data.setCommunityName(community.getName());
				}
				
				return data;
			}
		});
	}
	
}
