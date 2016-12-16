package com.everhomes.openapi.jindi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.address.Address;
import com.everhomes.address.AddressProvider;
import com.everhomes.building.Building;
import com.everhomes.building.BuildingProvider;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.officecubicle.OfficeCubicleOrder;
import com.everhomes.officecubicle.OfficeCubicleProvider;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.pmtask.PmTask;
import com.everhomes.pmtask.PmTaskLog;
import com.everhomes.pmtask.PmTaskProvider;
import com.everhomes.rest.openapi.jindi.JindiActionRepairDTO;
import com.everhomes.rest.openapi.jindi.JindiActionType;
import com.everhomes.rest.openapi.jindi.JindiDataType;
import com.everhomes.rest.openapi.jindi.JindiFetchDataCommand;
import com.everhomes.user.User;
import com.everhomes.user.UserProvider;

/**
 * 
 * <ul>
 * 抓取工位预订数据
 * </ul>
 */
@Component(JindiOpenHandler.JINDI_OPEN_HANDLER+JindiDataType.JindiDataTypeCode.ACTION_CODE+JindiActionType.JindiActionTypeCode.ACTION_TYPE_STATION_RENTAL_CODE)
public class JindiOpenActionStationRentalHandler implements JindiOpenHandler {
	
	@Autowired
	private OfficeCubicleProvider officeCubicleProvider;
	
	@Autowired
	private UserProvider userProvider;
	
	@Override
	public String fetchData(JindiFetchDataCommand cmd) {
		return superFetchData(cmd, new JindiOpenCallback<OfficeCubicleOrder>() {
			
			@Override
			public List<OfficeCubicleOrder> fetchDataByUpdateTimeAndAnchor(Integer namespaceId, Long timestamp,
					Long pageAnchor, int pageSize) {
				return officeCubicleProvider.listStationByUpdateTimeAndAnchor(cmd.getNamespaceId(), cmd.getTimestamp(), cmd.getPageAnchor(), pageSize+1);
			}

			@Override
			public List<OfficeCubicleOrder> fetchDataByUpdateTime(Integer namespaceId, Long timestamp,
					int pageSize) {
				return officeCubicleProvider.listStationByUpdateTime(cmd.getNamespaceId(), cmd.getTimestamp(), pageSize+1);
			}

			@Override
			public Object complementInfo(JindiFetchDataCommand cmd, OfficeCubicleOrder src) {
				JindiActionRepairDTO data = new JindiActionRepairDTO();
				data.setId(src.getId());
				data.setUserId(pmTask.getCreatorUid());
				data.setUserName(getUser(pmTask.getCreatorUid()).getNickName());
				data.setCommunityId(community.getId());
				data.setCommunityName(community.getName());
				data.setBuildingId(building.getId());
				data.setBuildingName(building.getName());
				data.setAddressId(address.getId());
				data.setApartmentName(address.getApartmentName());
				data.setOrganizationId(organization.getId());
				data.setOrganizationName(organization.getName());
				data.setRequestorName(pmTask.getRequestorName());
				data.setRequestorPhone(pmTask.getRequestorPhone());
				data.setReceiveTime(pmTask.getProcessingTime());
				data.setContent(pmTask.getContent());
				data.setReceiveByName(getUser(src.getTargetId()).getNickName());
				data.setReceiveEndTime(pmTask.getProcessedTime());
				data.setStar(pmTask.getStar()==null?null:pmTask.getStar().intValue());
				data.setStatus(src.getStatus());
				data.setCreateTime(pmTask.getCreateTime());
				data.setUpdateTime(src.getOperatorTime());
				
				return data;
			}
			
			private User getUser(Long id) {
				User user = null;
				if (id == null || (user = userProvider.findUserById(id)) == null) {
					user = new User();
				}
				return user;
			}
		});
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
