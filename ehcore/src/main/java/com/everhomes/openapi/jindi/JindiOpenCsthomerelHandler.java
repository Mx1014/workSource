package com.everhomes.openapi.jindi;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.address.Address;
import com.everhomes.address.AddressProvider;
import com.everhomes.community.Building;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.pm.CommunityAddressMapping;
import com.everhomes.rest.openapi.jindi.JindiCsthomerelDTO;
import com.everhomes.rest.openapi.jindi.JindiDataType;
import com.everhomes.rest.openapi.jindi.JindiFetchDataCommand;
import com.everhomes.user.UserContext;

/**
 * 
 * <ul>
 * 抓取客房数据
 * </ul>
 */
@Component(JindiOpenHandler.JINDI_OPEN_HANDLER+JindiDataType.JindiDataTypeCode.CSTHOMEREL_CODE)
public class JindiOpenCsthomerelHandler implements JindiOpenHandler {
	
	@Autowired
	private OrganizationProvider organizationProvider;
	
	@Autowired
	private CommunityProvider communityProvider;
	
	@Autowired
	private AddressProvider addressProvider;
	
	@Override
	public String fetchData(JindiFetchDataCommand cmd) {
		return superFetchData(cmd, new JindiOpenCallback<CommunityAddressMapping>() {

			@Override
			public List<CommunityAddressMapping> fetchDataByUpdateTimeAndAnchor(Integer namespaceId, Long timestamp,
					Long pageAnchor, int pageSize) {
				return organizationProvider.listCsthomerelByUpdateTimeAndAnchor(cmd.getNamespaceId(), cmd.getBeginTime(), cmd.getPageAnchor(), pageSize+1);
			}

			@Override
			public List<CommunityAddressMapping> fetchDataByUpdateTime(Integer namespaceId, Long timestamp,
					int pageSize) {
				return organizationProvider.listCsthomerelByUpdateTime(cmd.getNamespaceId(), cmd.getBeginTime(), pageSize+1);
			}

			@Override
			public Object complementInfo(JindiFetchDataCommand cmd, CommunityAddressMapping communityAddressMapping) {
				JindiCsthomerelDTO data = new JindiCsthomerelDTO();
				data.setOrganizationId(communityAddressMapping.getOrganizationId());
				data.setCommunityId(communityAddressMapping.getCommunityId());
				data.setAddressId(communityAddressMapping.getAddressId());
				data.setCreateTime(communityAddressMapping.getCreateTime());
				data.setUpdateTime(communityAddressMapping.getUpdateTime());
				
				Community community = communityProvider.findCommunityById(communityAddressMapping.getCommunityId());
				if (community != null) {
					data.setCommunityName(community.getName());
				}
				
				Address address = addressProvider.findAddressById(communityAddressMapping.getAddressId());
				if (address != null) {
					data.setApartmentName(address.getApartmentName());
					data.setBuildingName(address.getBuildingName());
					
					UserContext.current().setNamespaceId(cmd.getNamespaceId());
					Building building = communityProvider.findBuildingByCommunityIdAndName(communityAddressMapping.getCommunityId(), address.getBuildingName());
					if (building != null) {
						data.setBuildingId(building.getId());
					}
				}
				
				return data;
			}
		});
	}
	
}
