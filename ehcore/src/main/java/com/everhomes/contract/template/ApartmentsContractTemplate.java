package com.everhomes.contract.template;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.address.Address;
import com.everhomes.address.AddressProvider;
import com.everhomes.community.Building;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.rest.contract.BuildingApartmentDTO;
import com.everhomes.rest.contract.ContractDetailDTO;
import com.everhomes.rest.contract.ContractTemplateBuildingApartmentDTO;

@Component(ContractTemplateHandler.CONTRACTTEMPLATE_PREFIX + "apartments")
public class ApartmentsContractTemplate implements ContractTemplateHandler{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ApartmentsContractTemplate.class);
	
	@Autowired
	private AddressProvider addressProvider;
	
	@Autowired
	private CommunityProvider communityProvider;
	
	/**
	 * 合法的例子：
	 * 单价含税：chargingItems.0.chargingVariables.gdjebhs
	 */
	@Override
	public boolean isValid(ContractDetailDTO contract, String[] segments) {
		//入参不能为空
		if (contract == null || segments == null || segments.length == 0 ) {
			return false;
		}
		//segments第二位必须是数字
		Integer index = null;
		try{
			index = Integer.parseInt(segments[1]);
		}catch (Exception e) {
			LOGGER.info("index is not a number,index is {}. Exception message is {}",segments[1],e.getMessage());
			return false;
		}
		//index不能超出list的范围
		List<BuildingApartmentDTO> apartments = contract.getApartments();
		if (apartments != null && apartments.size()>0) {
			if (index >= apartments.size() || index < 0) {
				return false;
			}
		}else {
			return false;
		}
		return true;
	}
	

	@Override
	public String getValue(ContractDetailDTO contract, String[] segments) {
		String value = "";
		Object data = null;
		
		List<BuildingApartmentDTO> buildingApartmentList = contract.getApartments();

		Integer index = Integer.parseInt(segments[1]);
		BuildingApartmentDTO buildingApartmentDTO = buildingApartmentList.get(index);
		ContractTemplateBuildingApartmentDTO apartmentDetail = getApartmentDetail(buildingApartmentDTO);
		
		String apartmentsInfoKey = segments[2];
		data = PropertyUtils.getProperty(apartmentDetail, apartmentsInfoKey);
		if (data != null) {
			value = data.toString();
		}
		return value;
	}

	@Override
	public boolean isValid(ContractDetailDTO contract, String[] segments, String type) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getValue(ContractDetailDTO contract, String[] segments, String type) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private ContractTemplateBuildingApartmentDTO getApartmentDetail(BuildingApartmentDTO apartment){
		ContractTemplateBuildingApartmentDTO result = new ContractTemplateBuildingApartmentDTO();
		result.setChargeArea(apartment.getChargeArea());
		result.setApartmentName(apartment.getApartmentName());
		
		Building building = null;
		Community comminity = null;
		Address address = addressProvider.findAddressById(apartment.getAddressId());
		if (address != null) {
			result.setAreaSize(address.getAreaSize());
			result.setSharedArea(address.getSharedArea());
			if(address.getBuildingId() != null){
				building = communityProvider.findBuildingById(address.getBuildingId());
			}
			if (address.getCommunityId() != null) {
				comminity = communityProvider.findCommunityById(address.getCommunityId());
			}
		}
		if (building != null) {
			result.setBuildingName(building.getName());
			result.setBuildingAddress(building.getAddress());
		}
		if (comminity != null) {
			result.setCommunityName(comminity.getName());
			result.setCommunityAddress(comminity.getAddress());
		}
		return result;
	}

}
