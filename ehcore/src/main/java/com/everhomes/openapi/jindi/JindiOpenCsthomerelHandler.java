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
				return organizationProvider.listCsthomerelByUpdateTimeAndAnchor(cmd.getNamespaceId(), cmd.getTimestamp(), cmd.getPageAnchor(), pageSize+1);
			}

			@Override
			public List<CommunityAddressMapping> fetchDataByUpdateTime(Integer namespaceId, Long timestamp,
					int pageSize) {
				return organizationProvider.listCsthomerelByUpdateTime(cmd.getNamespaceId(), cmd.getTimestamp(), pageSize+1);
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
		
		
//		Integer pageSize = getPageSize(cmd.getPageSize());
//		//pageAnchor和nextPageAnchor只有在同一个时间戳有未取完的数据时才有用
//		Long nextPageAnchor = null;
//		Byte hasMore = null;
//		List<CommunityAddressMapping> resultList = new ArrayList<>();
//		//如果有锚点，则说明上次请求数据未取完，本次继续取当前时间戳的数据
//		if (cmd.getPageAnchor() != null) {
//			List<CommunityAddressMapping> thisList = organizationProvider.listCsthomerelByUpdateTimeAndAnchor(cmd.getNamespaceId(), cmd.getTimestamp(), cmd.getPageAnchor(), pageSize+1);
//			resultList.addAll(thisList);
//			//如果当前时间戳还未取完，则直接返回并添加下次锚点
//			if (resultList.size() > pageSize) {
//				resultList.remove(resultList.size()-1);
//				nextPageAnchor = resultList.get(resultList.size()-1).getId();
//				hasMore = (byte)1;
//			}else {
//				//如果正好取完，则没有锚点了
//				//如果当前时间戳取完了，还不够，那就需要从当前时间戳之后再取一些补充到pageSize大小
//				//此处无代码，方便说明逻辑
//			}
//		}
//		
//		//如果没有锚点，或者上述过程未取满或正好取满，则到此处，如果是正好取满，则这里会再取一条，看看有没有多余的数据了
//		if (hasMore == null  && resultList.size() <= pageSize) {
//			pageSize = pageSize - resultList.size();
//			List<CommunityAddressMapping> thisList = organizationProvider.listCsthomerelByUpdateTime(cmd.getNamespaceId(), cmd.getTimestamp(), pageSize+1);
//			if (thisList.size() > pageSize) {
//				thisList.remove(thisList.size()-1);
//				hasMore = (byte)1;
//				//如果此处数据大于pageSize说明，还有数据
//				if (thisList.size()>=2 && thisList.get(thisList.size()-1).getUpdateTime().getTime() == thisList.get(thisList.size()-2).getUpdateTime().getTime()) {
//					//如果最后两条数据的更新时间一样，说明这个时间戳还没有取完数据，下次还要从这个时间戳开始取，需要设置一下锚点
//					nextPageAnchor = thisList.get(thisList.size()-1).getId();
//				}
//			}else {
//				//到这里，说明数据已经取完了
//				//此处无代码，方便说明逻辑
//			}
//			resultList.addAll(thisList);
//		}
//		
//		List<JindiCsthomerelDTO> dataList = resultList.stream().map(r->{
//			JindiCsthomerelDTO data = new JindiCsthomerelDTO();
//			data.setOrganizationId(r.getOrganizationId());
//			data.setCommunityId(r.getCommunityId());
//			data.setAddressId(r.getAddressId());
//			data.setCreateTime(r.getCreateTime());
//			data.setUpdateTime(r.getUpdateTime());
//			
//			Community community = communityProvider.findCommunityById(r.getCommunityId());
//			if (community != null) {
//				data.setCommunityName(community.getName());
//			}
//			
//			Address address = addressProvider.findAddressById(r.getAddressId());
//			if (address != null) {
//				data.setApartmentName(address.getApartmentName());
//				data.setBuildingName(address.getBuildingName());
//				
//				UserContext.current().setNamespaceId(cmd.getNamespaceId());
//				Building building = communityProvider.findBuildingByCommunityIdAndName(r.getCommunityId(), address.getBuildingName());
//				if (building != null) {
//					data.setBuildingId(building.getId());
//				}
//			}
//			
//			return data;
//		}).collect(Collectors.toList());
//
//		JindiCsthomerelResponse response = new JindiCsthomerelResponse();
//		response.setHasMore(hasMore);
//		response.setNextPageAnchor(nextPageAnchor);
//		response.setCsthomerelList(dataList);
//		
//		return JSON.toJSONString(response);
	}
	
}
