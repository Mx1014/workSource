package com.everhomes.openapi.jindi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.address.Address;
import com.everhomes.address.AddressProvider;
import com.everhomes.building.Building;
import com.everhomes.building.BuildingProvider;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
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
 * 抓取物业报修数据
 * </ul>
 */
@Component(JindiOpenHandler.JINDI_OPEN_HANDLER+JindiDataType.JindiDataTypeCode.ACTION_CODE+JindiActionType.JindiActionTypeCode.ACTION_TYPE_REPAIR_CODE)
public class JindiOpenActionRepairHandler implements JindiOpenHandler {

	@Autowired
	private PmTaskProvider pmTaskProvider;
	
	@Autowired
	private CommunityProvider communityProvider;
	
	@Autowired
	private OrganizationProvider organizationProvider;
	
	@Autowired
	private AddressProvider addressProvider;
	
	@Autowired
	private BuildingProvider buildingProvider;
	
	@Override
	public String fetchData(JindiFetchDataCommand cmd) {
		return superFetchData(cmd, new JindiOpenCallback<PmTaskLog>() {
			private Map<Long, PmTask> cachePmTask = new HashMap<>();
			private Map<Long, Community> cacheCommunity = new HashMap<>();
			private Map<Long, Organization> cacheOrganization = new HashMap<>();
			private Map<Long, Address> cacheAddress = new HashMap<>();
			private Map<String, Building> cacheBuilding = new HashMap<>();
			
			@Override
			public List<PmTaskLog> fetchDataByUpdateTimeAndAnchor(Integer namespaceId, Long timestamp,
					Long pageAnchor, int pageSize) {
				return pmTaskProvider.listRepairByUpdateTimeAndAnchor(cmd.getNamespaceId(), cmd.getBeginTime(), cmd.getPageAnchor(), pageSize+1);
			}

			@Override
			public List<PmTaskLog> fetchDataByUpdateTime(Integer namespaceId, Long timestamp,
					int pageSize) {
				return pmTaskProvider.listRepairByUpdateTime(cmd.getNamespaceId(), cmd.getBeginTime(), pageSize+1);
			}

			@Override
			public Object complementInfo(JindiFetchDataCommand cmd, PmTaskLog src) {
				PmTask pmTask = gePmTask(src.getTaskId());
				Community community = getCommunity(pmTask.getOwnerId());
				Organization organization = getOrganization(pmTask.getOrganizationId());
				Address address = getAddress(pmTask.getAddressId());
				Building building = getBuilding(src.getNamespaceId(), pmTask.getOwnerId(), address.getBuildingName());
				
				User user = getUser(pmTask.getCreatorUid());
				if (StringUtils.isEmpty(user.getNickName())) {
					user = getUser(src.getOperatorUid());
				}
				JindiActionRepairDTO data = new JindiActionRepairDTO();
				data.setId(src.getId());
				data.setUserId(pmTask.getCreatorUid());
				data.setUserName(user.getNickName());
				data.setPhone(user.getIdentifierToken());
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

			//简单做下缓存
			private PmTask gePmTask(Long id){
				PmTask pmTask = cachePmTask.get(id);
				if (pmTask != null) {
					return pmTask;
				}
				pmTask = pmTaskProvider.findTaskById(id);
				if (pmTask == null) {
					pmTask = new PmTask();
				}
				cachePmTask.put(id, pmTask);
				return pmTask;
			}
			
			private Community getCommunity(Long id) {
				if (id == null) {
					id = -1L;
				}
				Community community = cacheCommunity.get(id);
				if (community != null) {
					return community;
				}
				if (id.longValue() == -1L || (community = communityProvider.findCommunityById(id)) == null) {
					community = new Community();
				}
				cacheCommunity.put(id, community);
				return community;
			}
			
			private Organization getOrganization(Long id) {
				if (id == null) {
					id = -1L;
				}
				Organization organization = cacheOrganization.get(id);
				if (organization != null) {
					return organization;
				}
				
				if (id.longValue() == -1L || (organization = organizationProvider.findOrganizationById(id)) == null) {
					organization = new Organization();
				}
				cacheOrganization.put(id, organization);
				return organization;
			} 
			
			private Address getAddress(Long id) {
				if (id == null) {
					id = -1L;
				}
				Address address = cacheAddress.get(id);
				if (address != null) {
					return address;
				}
				
				if (id.longValue() == -1L || (address = addressProvider.findAddressById(id)) == null) {
					address = new Address();
				}
				cacheAddress.put(id, address);
				return address;
			} 
			
			private Building getBuilding(Integer namespaceId, Long communityId, String name) {
				if (name == null) {
					name = "";
				}
				Building building = cacheBuilding.get(name);
				if (building != null) {
					return building;
				}
				
				if ("".equals(name) || (building = buildingProvider.findBuildingByName(namespaceId, communityId, name)) == null) {
					building = new Building();
				}
				cacheBuilding.put(name, building);
				return building;
			} 
		});
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
