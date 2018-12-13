package com.everhomes.contract.template;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Null;

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
import com.google.zxing.Result;

//@Component(ContractTemplateHandler.CONTRACTTEMPLATE_PREFIX + "apartments")
public class ApartmentsContractTemplate implements ContractTemplateHandler{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ApartmentsContractTemplate.class);
	
	@Autowired
	private AddressProvider addressProvider;
	
	@Autowired
	private CommunityProvider communityProvider;
	
	//声明这种变量，有点类似于redis缓存的那种意思，对于同一个合同文档，获取一次数据就行了，不用老去重复获取合同相关的数据
	private List<BuildingApartmentDTO> apartments;
	
	private List<ContractTemplateBuildingApartmentDTO> apartmentDetails;
	
	public List<BuildingApartmentDTO> getApartments() {
		return apartments;
	}
	
	public void setApartments(List<BuildingApartmentDTO> apartments) {
		this.apartments = apartments;
	}

	public List<ContractTemplateBuildingApartmentDTO> getApartmentDetails() {
		return apartmentDetails;
	}

	public void setApartmentDetails(List<ContractTemplateBuildingApartmentDTO> apartmentDetails) {
		this.apartmentDetails = apartmentDetails;
	}

	/**
	 * 合法的例子：
	 * 项目名称：apartments.0.communityName
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
		if (apartments == null) {
			apartments = contract.getApartments();
		}
		if (apartments != null && apartments.size()>0) {
			if (index >= apartments.size() || index < 0) {
				return false;
			}
		}else {
			return false;
		}
		if (!PropertyUtils.containsField(ContractTemplateBuildingApartmentDTO.class, segments[2])) {
			return false;
		}
		return true;
	}
	

	@Override
	public String getValue(ContractDetailDTO contract, String[] segments) {
		Object data = null;
		if (apartmentDetails == null) {
			apartmentDetails = getApartmentDetails(contract.getApartments());
		}
		Integer index = Integer.parseInt(segments[1]);
		ContractTemplateBuildingApartmentDTO apartmentDetail = apartmentDetails.get(index);
		
		String apartmentsInfoKey = segments[2];
		data = PropertyUtils.getProperty(apartmentDetail, apartmentsInfoKey);
		
		return formatValue(apartmentsInfoKey,data);
	}
	
	private String formatValue(String key,Object data){
		if (data == null) {
			return "";
		}
		
		String value = "";
		switch (key) {
			case "areaSize":
			case "chargeArea":
			case "sharedArea":	
				value = String.valueOf(doubleRoundHalfUp((double) data,2));
				break;
			default:
				value = data.toString();
				break;
		}
		return value;
	}

	//四舍五入截断double类型数据
	private double doubleRoundHalfUp(double input,int scale){
		BigDecimal digit = new BigDecimal(input);
		return digit.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	private List<ContractTemplateBuildingApartmentDTO> getApartmentDetails(List<BuildingApartmentDTO> buildingApartmentList){
		List<ContractTemplateBuildingApartmentDTO> result = new ArrayList<>();
		for(BuildingApartmentDTO dto : buildingApartmentList){
			ContractTemplateBuildingApartmentDTO apartmentDetail = getApartmentDetail(dto);
			result.add(apartmentDetail);
		}
		return result;
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
